package com.juxiao.xchat.manager.common.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;

import java.util.List;

/**
 * @class: BannerManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface BannerManager {
    String refreshBannerListCache(String jedisCode, String jedisKey);

    List<BannerDTO> getList(String str);

    String refreshBannerListByTagIdCache(String jedisCode, String jedisKey, String osType, Long tagId);
    
    List<BannerDTO> refreshValidateBannerList(String osType);
}
