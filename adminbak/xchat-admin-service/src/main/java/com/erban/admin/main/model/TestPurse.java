package com.erban.admin.main.model;

import java.util.Date;

public class TestPurse {
    private Integer id;

    private Long uid;

    private Long erbanNo;

    private String nick;

    private Long mysqlGold;

    private Long cacheGold;

    private Double mysqlDiamond;

    private Double cacheDiamond;

    private Date createTime;

    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public Long getMysqlGold() {
        return mysqlGold;
    }

    public void setMysqlGold(Long mysqlGold) {
        this.mysqlGold = mysqlGold;
    }

    public Long getCacheGold() {
        return cacheGold;
    }

    public void setCacheGold(Long cacheGold) {
        this.cacheGold = cacheGold;
    }

    public Double getMysqlDiamond() {
        return mysqlDiamond;
    }

    public void setMysqlDiamond(Double mysqlDiamond) {
        this.mysqlDiamond = mysqlDiamond;
    }

    public Double getCacheDiamond() {
        return cacheDiamond;
    }

    public void setCacheDiamond(Double cacheDiamond) {
        this.cacheDiamond = cacheDiamond;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
