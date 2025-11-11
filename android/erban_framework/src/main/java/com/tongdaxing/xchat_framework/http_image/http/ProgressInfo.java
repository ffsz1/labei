package com.tongdaxing.xchat_framework.http_image.http;

/**
 * 进度信息
 *
 * @author zhongyongsheng on 14-9-25.
 */
public class ProgressInfo {
    private long progress;
    private long total;

    public ProgressInfo(long progress, long total) {
        this.progress = progress;
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ProgressInfo{" +
                "progress=" + progress +
                ", total=" + total +
                '}';
    }
}