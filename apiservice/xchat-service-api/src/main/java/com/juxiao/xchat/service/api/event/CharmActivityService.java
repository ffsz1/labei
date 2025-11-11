package com.juxiao.xchat.service.api.event;

import com.juxiao.xchat.service.api.event.vo.UsersCharmVO;

import java.util.List;

/**
 * @author chris
 * @date 2019-07-10
 */
public interface CharmActivityService {

    /**
     *  获取魅力周榜数据
     * @param uid uid
     * @return List
     */
    List<UsersCharmVO> getUsersCharmByPage(Long uid);
}
