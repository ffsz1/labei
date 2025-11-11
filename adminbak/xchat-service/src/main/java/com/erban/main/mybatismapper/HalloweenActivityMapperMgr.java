package com.erban.main.mybatismapper;

import com.erban.main.vo.HalloweenActRankVo;

import java.util.List;

/**
 * Created by liuguofu on 2017/10/28.
 */
public interface HalloweenActivityMapperMgr {

    List<HalloweenActRankVo> queryRicherHalloweenActRank();
    int queryMineRicherHalloweenActRank(Long totalCount);
    HalloweenActRankVo queryMyRicherHalloweenGoldNum(Long uid);


    List<HalloweenActRankVo> queryStarHalloweenActRank();
    HalloweenActRankVo  queryMyStarHalloweenGoldNum(Long uid);
    int queryMineStarHalloweenActRank(Long totalCount);


    List<HalloweenActRankVo> queryRoomHalloweenActRank();
    HalloweenActRankVo  queryMyRoomHalloweenGoldNum(Long uid);
    int queryMineRoomHalloweenActRank(Long totalCount);
}
