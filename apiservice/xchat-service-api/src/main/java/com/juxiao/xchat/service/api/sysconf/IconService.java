package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.IconDTO;

import java.util.List;

public interface IconService {

    /**
     * 获取首页icon
     *
     * @param isCheck icon
     * @return
     */
    List<IconDTO> findIconList(boolean isCheck, String app, String appVersion,String os);


}
