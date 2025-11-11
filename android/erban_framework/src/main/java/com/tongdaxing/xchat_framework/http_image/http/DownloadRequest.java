package com.tongdaxing.xchat_framework.http_image.http;

/**
 * 下载文件请求
 *
 * @author zhongyongsheng on 14-6-15.
 */
public class DownloadRequest extends StringQueryRequest<String> {

    private static final int TIMEOUT_MS = 5000;
    private static final int MAX_RETRIES = 2;
    private static final float BACKOFF_MULT = 1f;

    /**
     * @param url                 下载服务器路径
     * @param downloadFilePath    下载本地路径
     * @param successListener     成功回调
     * @param errorListener       失败回调
     * @param progressListener    进度回调
     * @param useContinueDownload 是否使用断点续传
     */
    public DownloadRequest(String url,
                           String downloadFilePath,
                           ResponseListener successListener,
                           ResponseErrorListener errorListener,
                           ProgressListener progressListener,
                           boolean useContinueDownload) {
        super(new NoCache(), url, successListener, errorListener);
        mProgressListener = progressListener;
        if (downloadFilePath == null || downloadFilePath.length() == 0) {
            HttpLog.e("DownloadFilePath is empty.");
            return;
        }
        if (useContinueDownload) {
            mNetwork = new DownloadContinueNetwork(downloadFilePath, this);
        } else {
            mNetwork = new DownloadNetwork(downloadFilePath, this);
        }
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
    }

}
