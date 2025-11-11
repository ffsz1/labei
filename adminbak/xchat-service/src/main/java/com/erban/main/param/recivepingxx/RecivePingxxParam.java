package com.erban.main.param.recivepingxx;

/**
 * Created by liuguofu on 2017/7/7.
 */
public class RecivePingxxParam {
    private String object;
    private String request;
    private String pending_webhooks;
    private String id;
    private String created;
    private boolean livemode;
    private String type;
    private PingxxParamData data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getPending_webhooks() {
        return pending_webhooks;
    }

    public void setPending_webhooks(String pending_webhooks) {
        this.pending_webhooks = pending_webhooks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PingxxParamData getData() {
        return data;
    }

    public void setData(PingxxParamData data) {
        this.data = data;
    }
}
