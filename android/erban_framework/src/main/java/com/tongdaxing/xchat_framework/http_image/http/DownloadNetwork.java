package com.tongdaxing.xchat_framework.http_image.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 下载网络处理
 *
 * @author zhongyongsheng on 14-6-15.
 */
public class DownloadNetwork extends BaseNetwork {

    public static final String TMP_SURFIX = ".tmp";
    private static final long DEFAULT_PROGRESS_PERCENT = 100;
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    protected String mDownloadFilePath;
    protected String mDownloadFileTempPath;
    protected DownloadRequest mDownloadRequest;

    public DownloadNetwork(String downloadFilePath, DownloadRequest downloadRequest) {
        HttpLog.d("Download file path " + downloadFilePath);
        mDownloadFilePath = downloadFilePath;
        mDownloadRequest = downloadRequest;
        mDownloadFileTempPath = createTempPath(mDownloadFilePath);
    }

    protected static String createTempPath(final String orginalPath) {
        String fileName = TMP_SURFIX;
        fileName = orginalPath.concat(fileName);
        return fileName;
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
            File file = new File(mDownloadFileTempPath);
            OutputStream outputStream =
                    new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = null;
            try {
                InputStream in = entity.getContent();
                if (in == null) {
                    throw new ServerError();
                }
                long total = entity.getContentLength();
                HttpLog.d("Download content length %d", total);

                buffer = pool.getBuf(DEFAULT_BUFFER_SIZE);
                int count;
                long progress = 0;

                while ((count = in.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);

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
                file.renameTo(new File(mDownloadFilePath));
                HttpLog.d("File rename completed");
                return mDownloadFilePath.getBytes();
            } finally {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    HttpLog.d("entity to bytes consumingContent error");
                }
                pool.returnBuf(buffer);
                outputStream.close();
            }
        }
    }

    protected void onCancel(long progress) throws IOException {
        HttpLog.d("OnCancel");
        abort();
    }

}
