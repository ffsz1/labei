package com.juxiao.xchat.manager.common.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.IconDTO;

import java.util.List;

/**
 * @class: IconManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface IconManager {

    String refreshIconListCache(String jedisCode, String types);

    List<IconDTO> getList(String[] jedisIdList);

}
