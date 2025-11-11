package com.erban.main.vo.drawprize;

import java.util.Date;

/**
 * Created by liuguofu on 2017/12/15.
 */
public class UserDrawVo {
    private Long uid;

    private Integer leftDrawNum;

    private Integer totalDrawNum;

    private Integer totalWinDrawNum;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getLeftDrawNum() {
        return leftDrawNum;
    }

    public void setLeftDrawNum(Integer leftDrawNum) {
        this.leftDrawNum = leftDrawNum;
    }

    public Integer getTotalDrawNum() {
        return totalDrawNum;
    }

    public void setTotalDrawNum(Integer totalDrawNum) {
        this.totalDrawNum = totalDrawNum;
    }

    public Integer getTotalWinDrawNum() {
        return totalWinDrawNum;
    }

    public void setTotalWinDrawNum(Integer totalWinDrawNum) {
        this.totalWinDrawNum = totalWinDrawNum;
    }
}
