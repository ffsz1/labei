package com.erban.admin.web.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/10/9
 * @time 17:05
 */
public class UserPurseSurplusBO {

    @Excel(name = "统计时间", format = "yyyy-MM-dd", width = 20)
    private Date surplusDate;
    @Excel(name = "剩余金币" , width = 50)
    private BigDecimal surplusGold;
    @Excel(name = "剩余钻石" , width = 50)
    private BigDecimal surplusDiamonds;
    @Excel(name = "剩余全服" , width = 50)
    private BigDecimal surplusFull;

    public Date getSurplusDate() {
        return surplusDate;
    }

    public void setSurplusDate(Date surplusDate) {
        this.surplusDate = surplusDate;
    }

    public BigDecimal getSurplusGold() {
        return surplusGold;
    }

    public void setSurplusGold(BigDecimal surplusGold) {
        this.surplusGold = surplusGold;
    }

    public BigDecimal getSurplusDiamonds() {
        return surplusDiamonds;
    }

    public void setSurplusDiamonds(BigDecimal surplusDiamonds) {
        this.surplusDiamonds = surplusDiamonds;
    }

    public BigDecimal getSurplusFull() {
        return surplusFull;
    }

    public void setSurplusFull(BigDecimal surplusFull) {
        this.surplusFull = surplusFull;
    }
}
