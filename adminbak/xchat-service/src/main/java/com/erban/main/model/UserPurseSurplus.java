package com.erban.main.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/10/9
 * @time 16:23
 */
public class UserPurseSurplus {

    private Date surplusDate;

    private BigDecimal surplusGold;

    private BigDecimal surplusDiamonds;

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
