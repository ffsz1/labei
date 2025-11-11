package com.erban.main.mybatismapper;

import com.erban.main.model.StatPacketActivity;
import com.erban.main.model.StatPacketActivityExample;
import java.util.List;

public interface StatPacketActivityMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(StatPacketActivity record);

    int insertSelective(StatPacketActivity record);

    List<StatPacketActivity> selectByExample(StatPacketActivityExample example);

    StatPacketActivity selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(StatPacketActivity record);

    int updateByPrimaryKey(StatPacketActivity record);
}
