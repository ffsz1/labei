package com.erban.main.dto;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/10/23
 * @time 17:45
 */
public class AndroidReviewWhitelistDTO {

    private Integer id;

    private Long uid;

    private Date createTime;

    private String nick;

    private String avatar;

    private Long erbanNo;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }
}
