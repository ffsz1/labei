package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;

import java.util.List;

public interface BannerService {
    /**
     * 首页列表查询,
     *
     * @param uid 用户ID
     * @param os  手机系统
     * @return
     */
    List<BannerDTO> findBannerList(Long uid, String os, String app);

    /**
     * 首页列表查询
     *
     * @param uid 用户ID
     * @param os  手机系统
     * @return
     */
    List<BannerDTO> findBannerListByTagId(Long uid, String os, Long tagId);

    /**
     * 查找发现页Item
     *
     * @param uid 用户ID
     * @param os  手机系统
     * @return
     */
    List<BannerDTO> findDiscoverItemList(Long uid, String os, Long tagId);

    /**
     * 首页加载有效期内的banner
     * @return
     */
    List<BannerDTO> refreshValidateBannerList(String os);
}
