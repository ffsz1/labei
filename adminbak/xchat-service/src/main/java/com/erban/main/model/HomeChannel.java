package com.erban.main.model;

import java.util.Date;

public class HomeChannel {
    private Long id;

    private String channel;

    private String channelUrl;// 用来后台显示

    private Integer homeDefault;

    private Byte isNews;

    private Date createTime;

    private String outputLink;

    private Byte type;

    private Integer groupId;

    private String groupName;

    public Long getId() {
        return id;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getOutputLink() {
        return outputLink;
    }

    public void setOutputLink(String outputLink) {
        this.outputLink = outputLink;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Integer getHomeDefault() {
        return homeDefault;
    }

    public void setHomeDefault(Integer homeDefault) {
        this.homeDefault = homeDefault;
    }

    public Byte getIsNews() {
        return isNews;
    }

    public void setIsNews(Byte isNews) {
        this.isNews = isNews;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
