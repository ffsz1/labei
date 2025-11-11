package com.juxiao.xchat.manager.common.draw.impl;

import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.manager.common.draw.GiftDrawManager;
import com.juxiao.xchat.manager.common.draw.conf.GiftDrawConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 高概率捡海螺
 */
@Service("HighGiftDrawManager")
public class HighGiftDrawManagerImpl extends BaseGiftDrawManager implements GiftDrawManager {
    @Autowired
    private GiftDrawConf giftDrawConf;
    int _totalDrawNum;
    boolean _isXq;
    boolean _isHd;


    @Override
    public boolean check(Long uid, RoomConfDTO roomConfDto, int totalDrawNum, boolean isXq, boolean isHd) {
        _totalDrawNum = totalDrawNum;
        _isXq = isXq;
        _isHd = isHd;
        return true;
//        return roomConfDto != null && roomConfDto.getDrawType() != null && roomConfDto.getDrawType() == 2;
    }

    @Override
    double[] getDrawRates() {
        if (_isXq) {
            if (1 == _totalDrawNum) {
                return giftDrawConf.getXqFirstDrawRates();
            } else if (10 == _totalDrawNum) {
                return giftDrawConf.getXqLowDrawRates();
            } else {
                return giftDrawConf.getXqHighDrawRates();
            }
        } else if (_isHd) {
            if (1 == _totalDrawNum) {
                return giftDrawConf.getHdFirstDrawRates();
            } else if (10 == _totalDrawNum) {
                return giftDrawConf.getHdLowDrawRates();
            } else {
                return giftDrawConf.getHdHighDrawRates();
            }
        } else {
            if (1 == _totalDrawNum) {
                return giftDrawConf.getFirstDrawRates();
            } else if (10 == _totalDrawNum) {
                return giftDrawConf.getLowDrawRates();
            } else {
                return giftDrawConf.getHighDrawRates();
            }
        }
    }

    @Override
    int[] getDrawGifts() {
        if (_isXq) {
            return giftDrawConf.getXqDrawGifts();
        } else if (_isHd) {
            return giftDrawConf.getHdDrawGifts();
        } else {
            return giftDrawConf.getDrawGifts();
        }
    }

    @Override
    String getType() {
        return "高概率";
    }
}
