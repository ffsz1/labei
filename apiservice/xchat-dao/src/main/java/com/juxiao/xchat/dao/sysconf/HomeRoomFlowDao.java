package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.HomeRoomFlowDTO;

import java.util.List;

public interface HomeRoomFlowDao {
    @TargetDataSource(name = "ds2")
    List<HomeRoomFlowDTO> listHomeHotManualRoom();
}
