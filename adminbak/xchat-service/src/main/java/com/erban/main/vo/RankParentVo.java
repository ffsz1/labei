package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/11/7.
 */
public class RankParentVo {
    private List<RankVo> rankVoList;
    private RankMineVo me;

    public List<RankVo> getRankVoList() {
        return rankVoList;
    }

    public void setRankVoList(List<RankVo> rankVoList) {
        this.rankVoList = rankVoList;
    }

    public RankMineVo getMe() {
        return me;
    }

    public void setMe(RankMineVo me) {
        this.me = me;
    }
}
