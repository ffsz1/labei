package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UserGiftWallDTO;

import java.util.List;

/**
 * @class: UserGiftWallService.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface UserGiftWallService {

    /**
     * 查询用户礼物墙
     *
     * @param uid       用户ID
     * @param orderType 1,收到的礼物数量多少排序;2,礼物价格高低排序
     * @return
     */
    List<UserGiftWallDTO> listUserWalls(Long uid, Integer orderType) throws WebServiceException;

    /**
     * 查询神秘礼物墙
     * @param uid 用户ID
     * @param orderType 1,收到的礼物数量多少排序;2,礼物价格高低排序
     * @return
     * @throws WebServiceException
     */
    List<UserGiftWallDTO> listMystic(Long uid, Integer orderType) throws WebServiceException;
}
