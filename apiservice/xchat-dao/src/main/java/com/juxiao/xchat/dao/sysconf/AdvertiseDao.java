package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.AdvertiseDTO;

import java.util.List;

public interface AdvertiseDao {

    @TargetDataSource(name = "ds2")
    List<AdvertiseDTO> list();
}
