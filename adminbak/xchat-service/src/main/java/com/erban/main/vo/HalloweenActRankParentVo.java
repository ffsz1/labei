package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/10/28.
 */
public class HalloweenActRankParentVo {
    List<HalloweenActRankVo> rankList;
    private HalloweenActRankVo me;

    public List<HalloweenActRankVo> getRankList() {
        return rankList;
    }

    public void setRankList(List<HalloweenActRankVo> rankList) {
        this.rankList = rankList;
    }

    public HalloweenActRankVo getMe() {
        return me;
    }

    public void setMe(HalloweenActRankVo me) {
        this.me = me;
    }
}
