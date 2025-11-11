package com.erban.main.mybatismapper;

import com.erban.main.model.StatPacketBouns;
import com.erban.main.model.StatPacketBounsExample;
import java.util.List;

public interface StatPacketBounsMapper {
    int insert(StatPacketBouns record);

    int insertSelective(StatPacketBouns record);

    List<StatPacketBouns> selectByExample(StatPacketBounsExample example);
}
