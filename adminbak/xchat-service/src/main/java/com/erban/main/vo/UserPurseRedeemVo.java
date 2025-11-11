package com.erban.main.vo;

import com.erban.main.model.UserPurse;

public class UserPurseRedeemVo extends UserPurse {

    private long amount; // 兑换金额

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
