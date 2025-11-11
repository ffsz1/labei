package com.juxiao.xchat.manager.common.draw.impl;

import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.manager.common.draw.GiftDrawManager;
import com.juxiao.xchat.manager.common.draw.conf.GiftDrawConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LowGiftDrawManager")
public class LowGiftDrawManagerImpl extends BaseGiftDrawManager implements GiftDrawManager {
    @Autowired
    private GiftDrawConf giftDrawConf;

    @Override
    public boolean check(Long uid, RoomConfDTO roomConfDto, int totalDrawNum, boolean isXq, boolean isHd) {
        return false;
//        return roomConfDto == null || roomConfDto.getDrawType() == null || roomConfDto.getDrawType() == 1;
    }

    @Override
    double[] getDrawRates() {
        return giftDrawConf.getLowDrawRates();
    }

    @Override
    int[] getDrawGifts() {
        return giftDrawConf.getDrawGifts();
    }

    @Override
    String getType() {
        return "低概率";
    }

}
