package com.erban.main.dto;

/**
 * @author chris
 * @Title:
 * @date 2018/11/12
 * @time 14:22
 */
public class RoomFlowEchartsDTO {

    private String period;
    private Long uid;
    private Long totalGold;
    private String nick;
    private Long erbanNo;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
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
}
