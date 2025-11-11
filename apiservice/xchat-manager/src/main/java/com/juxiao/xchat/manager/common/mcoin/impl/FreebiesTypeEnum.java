package com.juxiao.xchat.manager.common.mcoin.impl;


import com.juxiao.xchat.base.spring.SpringAppContext;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;

/**
 * 抽奖产品类型枚举
 */
public enum FreebiesTypeEnum {
    /**
     * 抽奖得头饰
     */
    HEADWEAR((byte) 1) {
        @Override
        public void sendFreebiesId(Long uid, Integer freebiesId, String message) {
            HeadwearManager headwearManager = SpringAppContext.getBean(HeadwearManager.class);
            if (headwearManager == null) {
                return;
            }
            HeadwearDTO headwearDto = headwearManager.getHeadwear(freebiesId);
            if (headwearDto == null) {
                return;
            }
            StringBuilder defaultMessage = new StringBuilder();
            defaultMessage.append("恭喜你完成"+ message +"，获得点点币专属头饰“");
            defaultMessage.append(headwearDto.getHeadwearName());
            defaultMessage.append("” * 7天，快带上去浪吧！");
            message = defaultMessage.toString();
            headwearManager.saveUserHeadwear(uid, freebiesId, 7, 5, message);
        }
    },

    /**
     * 抽奖得座驾
     */
    GIFT_CAR((byte) 2) {
        @Override
        public void sendFreebiesId(Long uid, Integer freebiesId, String message) {
            GiftCarManager giftCarManager = SpringAppContext.getBean(GiftCarManager.class);
            if (giftCarManager == null) {
                return;
            }

            GiftCarDTO giftcarDto = giftCarManager.getGiftCar(freebiesId);
            if (giftcarDto == null) {
                return;
            }
            StringBuilder defaultMessage = new StringBuilder();
            defaultMessage.append("恭喜你完成"+message+"，获得点点币专属座驾“");
            defaultMessage.append(giftcarDto.getCarName());
            defaultMessage.append("”*7天，快坐上去浪吧！");
            message = defaultMessage.toString();
            giftCarManager.saveUserCar(uid, freebiesId, 7, 5, message);
        }
    },;

    private Byte itemType;

    FreebiesTypeEnum(byte itemType) {
        this.itemType = itemType;
    }

    /**
     * 发送奖励
     *
     * @param uid
     * @param freebiesId
     */
    public abstract void sendFreebiesId(Long uid, Integer freebiesId, String message);

    public static FreebiesTypeEnum itemTypeOf(Byte itemType) {
        if (itemType == null) {
            return null;
        }

        for (FreebiesTypeEnum typeName : FreebiesTypeEnum.values()) {
            if (typeName.itemType == itemType.byteValue()) {
                return typeName;
            }
        }
        return null;
    }
}
