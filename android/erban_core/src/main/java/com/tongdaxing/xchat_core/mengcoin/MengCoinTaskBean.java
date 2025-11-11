package com.tongdaxing.xchat_core.mengcoin;

import java.util.List;

/**
 * 文件描述：
 * 萌币任务列表封装bean类
 *
 * @auther：zwk
 * @data：2019/1/15
 */
public class MengCoinTaskBean {
    private float mcoinNum;
    private List<MengCoinBean> beginnerMissions;
    private List<MengCoinBean> dailyMissions;
    private List<MengCoinBean> weeklyMissions;

    public float getMcoinNum() {
        return mcoinNum;
    }

    public void setMcoinNum(float mcoinNum) {
        this.mcoinNum = mcoinNum;
    }

    public List<MengCoinBean> getBeginnerMissions() {
        return beginnerMissions;
    }

    public void setBeginnerMissions(List<MengCoinBean> beginnerMissions) {
        this.beginnerMissions = beginnerMissions;
    }

    public List<MengCoinBean> getDailyMissions() {
        return dailyMissions;
    }

    public void setDailyMissions(List<MengCoinBean> dailyMissions) {
        this.dailyMissions = dailyMissions;
    }

    public List<MengCoinBean> getWeeklyMissions() {
        return weeklyMissions;
    }

    public void setWeeklyMissions(List<MengCoinBean> weeklyMissions) {
        this.weeklyMissions = weeklyMissions;
    }
}
