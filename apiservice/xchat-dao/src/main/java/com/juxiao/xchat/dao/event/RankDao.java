package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.event.dto.RankDTO;
import com.juxiao.xchat.dao.event.query.RankQuery;

import java.util.List;

public interface RankDao {

    //查询所有巨星排行榜数据
    @TargetDataSource(name = "ds2")
    List<RankDTO> getAllStarRankList(RankQuery rankParam);

    @TargetDataSource(name = "ds2")
    List<RankDTO> getNobelRankList(RankQuery rankParam);

    @TargetDataSource(name = "ds2")
    List<RankDTO> getAllRoomRankList(RankQuery rankParam);


    @TargetDataSource(name = "ds2")
    RankDTO getUidStarRank(RankQuery randQuery);

    @TargetDataSource(name = "ds2")
    RankDTO getUidNobelRank(RankQuery randQuery);
}
