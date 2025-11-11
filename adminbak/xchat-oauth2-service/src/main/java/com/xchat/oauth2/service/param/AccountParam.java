package com.xchat.oauth2.service.param;


import com.xchat.oauth2.service.core.util.StringUtils;

import java.util.List;

/**
 * 后台管理-体现请求入参
 */
public class AccountParam {
    private Integer page;
    private Integer size;
    // 拉贝号
    private Long erbanNo;
    private String erbanNos;
    private List<Long> erbanNosList;
    // 开始时间 yyyy-MM-dd
    private String beginDate;
    // 结束时间 yyyy-MM-dd
    private String endDate;
    // 平台
    private String os;
    // 手机号
    private String phone;
    // 注册类型 [1.手机号注册; 2.微信注册; 3.QQ注册]
    private Byte registerType;
    private String channel;
    private Byte gender;
    private String nick;
    private Byte orderBy;
    private String signBegin;
    private String signEnd;
    private List<Long> uids;
    private String userIds;

    public String getSignBegin() {
        return signBegin;
    }

    public void setSignBegin(String signBegin) {
        this.signBegin = signBegin;
    }

    public String getSignEnd() {
        return signEnd;
    }

    public void setSignEnd(String signEnd) {
        this.signEnd = signEnd;
    }

    public List<Long> getUids() {
        return uids;
    }

    public void setUids(List<Long> uids) {
        this.uids = uids;
    }

    public Byte getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Byte orderBy) {
        this.orderBy = orderBy;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = StringUtils.isBlank(beginDate) ? null : StringUtils.trim(beginDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = StringUtils.isBlank(endDate) ? null : StringUtils.trim(endDate);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = StringUtils.isBlank(phone) ? null : StringUtils.trim(phone);
    }

    public Byte getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Byte registerType) {
        this.registerType = registerType;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = StringUtils.isBlank(os) ? null : StringUtils.trim(os);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = StringUtils.isBlank(channel) ? null : StringUtils.trim(channel);
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = StringUtils.isBlank(nick) ? null : StringUtils.trim(nick);
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getErbanNos() {
        return erbanNos;
    }

    public void setErbanNos(String erbanNos) {
        this.erbanNos = erbanNos;
    }

    public List<Long> getErbanNosList() {
        return erbanNosList;
    }

    public void setErbanNosList(List<Long> erbanNosList) {
        this.erbanNosList = erbanNosList;
    }
}
