package com.erban.main.param.neteasepush;

import com.alibaba.fastjson.JSONArray;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/5.
 */
public class GroupMsg {
    private Date expireTime;
    private String body;
    private Date createTime;
    private boolean isOffline;
    private String broadcastId;
    private JSONArray targetOs;



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public JSONArray getTargetOs() {
        return targetOs;
    }

    public void setTargetOs(JSONArray targetOs) {
        this.targetOs = targetOs;
    }
}
