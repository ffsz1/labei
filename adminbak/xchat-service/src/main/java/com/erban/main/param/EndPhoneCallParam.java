package com.erban.main.param;

public class EndPhoneCallParam {
    private String channelId;//		通道号
    private Long createtime;//音视频通话/白板开始的事件, 可转为13位时间戳

    private Integer duration;//此通通话/白板的通话时长，精确到秒，可转为Integer类型
    private int eventType;//		为5，表示是实时音视频/白板时长类型事件

    private int live;//是否是互动直播的音视频，0：否，1：是
    private String members;//	表示通话/白板的参与者： accid为用户帐号；如果是通话的发起者的话，caller字段为true，否则无caller字段；duration表示对应accid用户的单方时长，其中白板消息暂无此单方时长的统计

    private String status;//通话/白板状态： SUCCESS：表示正常挂断； TIMEOUT：表示超时；SINGLE_PARTICIPATE：表示只有一个参与者；UNKNOWN：表示未知状态
    private String type;//类型： AUDIO：表示音频通话； VEDIO：表示视频通话； DataTunnel：表示白板事件

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
