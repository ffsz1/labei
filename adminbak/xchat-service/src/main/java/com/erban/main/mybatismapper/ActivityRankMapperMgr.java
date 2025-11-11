package com.erban.main.mybatismapper;


import com.erban.main.vo.activity.RankActVo;

import java.util.List;
import java.util.Map;


public interface ActivityRankMapperMgr {

    List<RankActVo> queryRichActRank(Map<String, Object> param);

    List<RankActVo> queryStarActRank(Map<String, Object> param);

    List<RankActVo> queryRoomActRank(Map<String, Object> param);
}
