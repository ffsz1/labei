package com.erban.main.mybatismapper;

import com.erban.main.model.RoomReward;
import com.erban.main.model.RoomRewardExample;
import java.util.List;

public interface RoomRewardMapper {
    int insert(RoomReward record);

    int insertSelective(RoomReward record);

    List<RoomReward> selectByExample(RoomRewardExample example);
}
