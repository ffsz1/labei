package com.tongdaxing.xchat_core.initial;


import java.io.Serializable;

/**
 * @author xiaoyu
 * @date 2017/12/8
 */

public class FaceComponent implements Serializable{
    private int id;
    private String version;
    private int status;
    private long createTime;
    private String json;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "FaceComponent{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", json='" + json + '\'' +
                '}';
    }
}
