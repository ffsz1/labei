package com.erban.admin.web.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class RoomMasterAccountBO {
    @Excel(name = "用户UID")
    private Long uid;
    @Excel(name = "拉贝号")
    private Long erbanNo;
    @Excel(name = "昵称")
    private String nick;
    @Excel(name = "性别")
    private String gender;
    @Excel(name = "手机号码")
    private String phone;
    @Excel(name = "创建时间", format = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date createTime;

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
        this.nick = nick;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RoomMasterAccountBO{" +
                "uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
