package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.UserGiftWallDao;
import com.juxiao.xchat.dao.user.dto.UserGiftWallDTO;
import com.juxiao.xchat.dao.user.query.UserGiftWallOrderTypeQuery;
import com.juxiao.xchat.service.api.user.UserGiftWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @class: UserGiftWallServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Service
public class UserGiftWallServiceImpl implements UserGiftWallService {

    @Autowired
    private UserGiftWallDao giftWallDao;

    @Override
    public List<UserGiftWallDTO> listUserWalls(Long uid, Integer orderType) throws WebServiceException {
        if (uid == null || uid == 0 || orderType == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        List<UserGiftWallDTO> list;

        if (orderType == 1 || orderType == 2) {//收到的礼物数量多少排序
            list = giftWallDao.listUserGiftOrderTypeWall(new UserGiftWallOrderTypeQuery(uid, orderType, 2));
        } else {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        //取出含有南瓜糖的礼物
        List<UserGiftWallDTO> nanguas = Lists.newArrayList();
        List<UserGiftWallDTO> others = Lists.newArrayList();
        for (UserGiftWallDTO userGift : list) {
            if (userGift.getGiftId() == 1025 || userGift.getGiftId() == 1026) {
                nanguas.add(userGift);
            } else {
                others.add(userGift);
            }
        }

        if (nanguas.size() <= 1) {
            return list;
        }

        int nanguaTotalCount = 0;
        for (UserGiftWallDTO nangua : nanguas) {
            nanguaTotalCount = nanguaTotalCount + nangua.getReciveCount();
        }

        UserGiftWallDTO nanguaTotal = nanguas.get(nanguas.size() - 1);
        nanguaTotal.setReciveCount(nanguaTotalCount);
        others.add(0, nanguaTotal);
        return others;
    }

    @Override
    public List<UserGiftWallDTO> listMystic(Long uid, Integer orderType) throws WebServiceException {
        if (uid == null) {
            return Lists.newArrayList();
        }
        List<UserGiftWallDTO> list;
        if (orderType == 1 || orderType == 2) {
            //收到的礼物数量多少排序, 3 表示神秘礼物
            list = giftWallDao.listUserGiftOrderTypeWall(new UserGiftWallOrderTypeQuery(uid, orderType, 3));
        } else {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        return list;
    }
}
