package com.erban.main.vo;

import java.util.List;

public class ClockMyResultVo {
    private String goldAmount;
    private String resultAmount;
    private String successClock;
    List<ClockResultVo> resultVoList;

    public String getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(String goldAmount) {
        this.goldAmount = goldAmount;
    }

    public String getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(String resultAmount) {
        this.resultAmount = resultAmount;
    }

    public String getSuccessClock() {
        return successClock;
    }

    public void setSuccessClock(String successClock) {
        this.successClock = successClock;
    }

    public List<ClockResultVo> getResultVoList() {
        return resultVoList;
    }

    public void setResultVoList(List<ClockResultVo> resultVoList) {
        this.resultVoList = resultVoList;
    }
}
