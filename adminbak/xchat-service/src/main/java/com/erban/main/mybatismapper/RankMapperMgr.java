package com.erban.main.mybatismapper;

import com.erban.main.param.RankParam;
import com.erban.main.vo.RankVo;

import java.util.List;
import java.util.Map;

public interface RankMapperMgr {
    //查询所有巨星排行榜数据
    List<RankVo> getAllStarRankList(RankParam rankParam);
    List<RankVo> getNobelRankList(RankParam rankParam);
    List<RankVo> getAllRoomRankList(RankParam rankParam);
//    //查询我的巨星排行榜数值
//    RankVo getMyStarRankVo();
//    //查询我的巨星名次
//    int getMyStarRankSeqNo();

}
