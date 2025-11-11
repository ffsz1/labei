package com.erban.main.model.dto;

import java.util.Date;

/**
 * @author laizhilong
 * @Title:  拉贝统计记录
 * @Package com.erban.main.model.dto
 * @date 2018/8/9
 * @time 17:36
 */
public class UserReportRecordDTO {


    private Long uid;

    private String nick;

    private Long erbanNo;

    private Integer frequency;

    private Long totalGoldNum;

    private String date;

    private Date createTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Long getTotalGoldNum() {
        return totalGoldNum;
    }

    public void setTotalGoldNum(Long totalGoldNum) {
        this.totalGoldNum = totalGoldNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

