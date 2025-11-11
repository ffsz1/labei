package com.erban.admin.main.vo;

import com.erban.main.model.Users;

public class UsersVo extends Users {
    private long goldNum;

    private double diamondNum;

    public long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(long goldNum) {
        this.goldNum = goldNum;
    }

    public double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(double diamondNum) {
        this.diamondNum = diamondNum;
    }

    @Override
    public String toString() {
        return "UsersVo{" +
                "goldNum=" + goldNum +
                ", diamondNum=" + diamondNum +
                '}';
    }
}
