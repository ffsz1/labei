package com.erban.main.vo;

import java.util.List;

public class StatPacketActivityParentVo {

    private List<StatPacketActRankVo> rankList;
    private StatPacketActRankVo me;

    public List<StatPacketActRankVo> getRankList() {
        return rankList;
    }

    public void setRankList(List<StatPacketActRankVo> rankList) {
        this.rankList = rankList;
    }

    public StatPacketActRankVo getMe() {
        return me;
    }

    public void setMe(StatPacketActRankVo me) {
        this.me = me;
    }
}
