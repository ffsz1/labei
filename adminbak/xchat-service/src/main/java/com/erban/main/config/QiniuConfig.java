package com.erban.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/2/3.
 */
@Component
public class QiniuConfig {
    private String accessUrl;
    private String accessKey;
    private String secretKey;
    private String bucket;

    @Value("http://pic.haijiaoxingqiu.cn/")
    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    @Value("${qiniu.access_key}")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Value("${qiniu.secret_key}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${qiniu.bucket}")
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBucket() {
        return bucket;
    }
}
