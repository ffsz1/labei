package com.xchat.oauth2.service.core.domain;

/**
 * 专题评论对象
 * @author liuguofu
 * on 2015/7/24.
 */
public class Comment {
    private String targetId;
    private String appId;

    public Comment() {
    }

    public Comment(String targetId) {
        this.targetId = targetId;
    }

    public Comment(String targetId, String appId) {
        this.targetId = targetId;
        this.appId = appId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAppId() {
        return appId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "targetId='" + targetId + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
