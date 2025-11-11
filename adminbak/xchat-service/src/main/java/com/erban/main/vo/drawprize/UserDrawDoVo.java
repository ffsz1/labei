package com.erban.main.vo.drawprize;

/**
 * Created by liuguofu on 2017/12/15.
 */

import com.erban.main.vo.UserVo;

/**
 * 用户进行抽奖操作，返回结果
 *
 */
public class UserDrawDoVo {

    private UserVo userVo;

    private Integer leftDrawNum;

    private Integer totalDrawNum;

    private Integer totalWinDrawNum;

    private Byte drawStatus;//中奖纪录状态，1创建，2未中奖，3已中奖

    private String srcObjName;

    private String drawPrizeName;

    private Integer drawPrizeId;

    public Integer getDrawPrizeId() {
        return drawPrizeId;
    }

    public void setDrawPrizeId(Integer drawPrizeId) {
        this.drawPrizeId = drawPrizeId;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
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

    public Byte getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(Byte drawStatus) {
        this.drawStatus = drawStatus;
    }

    public String getSrcObjName() {
        return srcObjName;
    }

    public void setSrcObjName(String srcObjName) {
        this.srcObjName = srcObjName;
    }

    public String getDrawPrizeName() {
        return drawPrizeName;
    }

    public void setDrawPrizeName(String drawPrizeName) {
        this.drawPrizeName = drawPrizeName;
    }
}
