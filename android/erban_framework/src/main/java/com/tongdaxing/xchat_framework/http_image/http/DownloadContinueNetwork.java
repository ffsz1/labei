package com.tongdaxing.xchat_framework.http_image.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 下载网络处理,支持断点续传
 *
 * @author zhongyongsheng on 14-6-15.
 */
public class DownloadContinueNetwork extends BaseNetwork {

    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final String TMP_SURFIX = ".tmp";
    public static final String CONFIG_SURFIX = ".cfg";
    public static final String DOWNLOAD_KEY_PROGRESS = "PROGRESS";
    public static final String CONTENT_RANGE = "Content-Range";

    protected String mDownloadFilePath;
    protected String mDownloadFileTempPath;
    protected String mDownloadFileConfigPath;
    protected DownloadRequest mDownloadRequest;
    protected RandomAccessFile mRandomAccessFile;
    protected DownloadContinueConfig mDownloadContinueConfig;

    public DownloadContinueNetwork(String downloadFilePath, DownloadRequest downloadRequest) {
        HttpLog.d("Download file path " + downloadFilePath);
        mDownloadFilePath = downloadFilePath;
        mDownloadRequest = downloadRequest;
        mDownloadFileTempPath = createTempPath(mDownloadFilePath);
        mDownloadFileConfigPath = createConfigPath(mDownloadFilePath);
    }

    protected static String createTempPath(final String orginalPath) {
        String fileName = TMP_SURFIX;
        fileName = orginalPath.concat(fileName);
        return fileName;
    }

    protected static String createConfigPath(final String orginalPath) {
        String fileName = CONFIG_SURFIX;
        fileName = orginalPath.concat(fileName);
        return fileName;
    }

    @Override
    public ResponseData performRequest(Request<?> request) throws RequestError {
        try {
            File tempFile = new File(this.mDownloadFileTempPath);

            mDownloadContinueConfig = new DownloadContinueConfig(mDownloadFileConfigPath);
            if (tempFile.exists()) {
                if (mDownloadContinueConfig.exists()) {
                    mDownloadContinueConfig.load();
                    int lastProgress = mDownloadContinueConfig.getInt(getProgressKey(), 0);
                    HttpLog.d("Last progress = " + lastProgress);
                    request.getHeaders().put("Range", "bytes=" + lastProgress + "-");
                } else {
                    mDownloadContinueConfig.create();
                    mDownloadContinueConfig.put(getProgressKey(), "0");
                    mDownloadContinueConfig.save();
                }
            } else {
                tempFile.createNewFile();
            }
            mRandomAccessFile = new RandomAccessFile(tempFile, "rwd");
        } catch (Exception e) {
            HttpLog.e(e, "Load config file error");
        }
        return super.performRequest(request);
    }

    public String getProgressKey() {
        return DOWNLOAD_KEY_PROGRESS;
    }

    public byte[] entityToBytes(Request<?> request, HttpResponse httpResponse) throws IOException, ServerError {
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        if (statusCode < 200 || statusCode > 299) {//下载失败,不下载文件,直接返回错误文字
            return super.entityToBytes(request, httpResponse);
        } else {
            HttpEntity entity = httpResponse.getEntity();
            ByteArrayPool pool = request.getRequestProcessor().getByteArrayPool();
            HttpLog.d("Start download url=" + request.getUrl());
            HttpLog.d("Download file tmp path " + mDownloadFileTempPath);

            int seekLocation = seekLocationAndReturn(httpResponse);

            byte[] buffer = null;
            long progress = seekLocation;

            try {
                InputStream in = entity.getContent();
                if (in == null) {
                    throw new ServerError();
                }
                long total = entity.getContentLength() + seekLocation;
                HttpLog.d("Download content length %d", total);

                buffer = pool.getBuf(DEFAULT_BUFFER_SIZE);
                int count;

                while ((count = in.read(buffer)) != -1) {
                    mRandomAccessFile.write(buffer, 0, count);
                    progress += count;

                    if (request.isCanceled()) {
                        HttpLog.d("Download cancel.");
                        onCancel(progress);
                        return new byte[0];
                    }

                    if (needProgress(count, total, request)) {
                        ProgressInfo progressInfo = new ProgressInfo(progress, total);
                        request.postProgress(progressInfo);
                    }
                }
                HttpLog.d("File download completed");
                new File(mDownloadFileTempPath).renameTo(new File(mDownloadFilePath));
                HttpLog.d("File rename completed");

                if (mDownloadContinueConfig.delete()) {
                    HttpLog.d("Config File delete completed");
                } else {
                    HttpLog.e("Config File delete fail");
                }

                return mDownloadFilePath.getBytes();
            } catch (IOException e) {
                mDownloadContinueConfig.put(getProgressKey(), String.valueOf(progress));
                mDownloadContinueConfig.save();
                throw e;
            } finally {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    HttpLog.d("entity to bytes consumingContent error");
                }
                pool.returnBuf(buffer);
                mRandomAccessFile.close();
            }
        }
    }

    public int seekLocationAndReturn(HttpResponse httpResponse) throws IOException {
        int seekLocation = 0;
        if (httpResponse.containsHeader(CONTENT_RANGE)) {
            Header rangeHeader = httpResponse.getFirstHeader(CONTENT_RANGE);
            String rangeValue = rangeHeader.getValue();
            String[] rangeValues = rangeValue.split(" ");
            if (rangeValues.length > 1) {
                if (rangeValues[1].contains("-")) {
                    String bytesString = rangeValues[1].split("-")[0];

                    try {
                        seekLocation = Integer.parseInt(bytesString);
                    } catch (NumberFormatException e) {
                        HttpLog.e(e, "Range number parse error");
                    }
                    HttpLog.v("SeekLocation = " + seekLocation);
                    mRandomAccessFile.seek(seekLocation);
                }
            }
        }
        return seekLocation;
    }

    protected void onCancel(long progress) throws IOException {
        HttpLog.d("OnCancel");
        mDownloadContinueConfig.put(getProgressKey(), String.valueOf(progress));
        mDownloadContinueConfig.save();
        abort();
    }
}
