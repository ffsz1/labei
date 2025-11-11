package com.erban.main.service.activity.enumarator;

import com.erban.main.service.SpringAppContext;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.gift.GiftService;
import com.erban.main.service.headwear.HeadwearService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;


public enum YoungGPrizeType {
    prize_1(1, "玫瑰花", 1000) {
        @Override
        public void updateGift(Long uid) {
            GiftService giftService = SpringAppContext.getBean(GiftService.class);
            giftService.updateUserGiftPurse(uid, 1);
        }
    },
    prize_2(2, "棒棒糖", 200) {
        @Override
        public void updateGift(Long uid) {
            GiftService giftService = SpringAppContext.getBean(GiftService.class);
            giftService.updateUserGiftPurse(uid, 2);
        }
    },
    prize_3(3, "么么哒", 100) {
        @Override
        public void updateGift(Long uid) {
            GiftService giftService = SpringAppContext.getBean(GiftService.class);
            giftService.updateUserGiftPurse(uid, 3);
        }
    },
    prize_4(4, "小熊", 20) {
        @Override
        public void updateGift(Long uid) {
            GiftService giftService = SpringAppContext.getBean(GiftService.class);
            giftService.updateUserGiftPurse(uid, 16);
        }
    },
    prize_5(5, "young-g专属头饰*3天", 100) {
        @Override
        public void updateGift(Long uid) {
            HeadwearService headwearService = SpringAppContext.getBean(HeadwearService.class);
            headwearService.purse4Free(uid, 11, 3);
        }
    },
    prize_6(6, "爱你哦*3天", 100) {
        @Override
        public void updateGift(Long uid) {
            HeadwearService headwearService = SpringAppContext.getBean(HeadwearService.class);
            headwearService.purse4Free(uid, 8, 3);
        }
    },
    prize_7(7, "霓虹灯*3天", 100) {
        @Override
        public void updateGift(Long uid) {
            HeadwearService headwearService = SpringAppContext.getBean(HeadwearService.class);
            headwearService.purse4Free(uid, 3, 3);
        }
    },
    prize_8(8, "可乐小埋*3天", 20) {
        @Override
        public void updateGift(Long uid) {
            GiftCarService giftCarService = SpringAppContext.getBean(GiftCarService.class);
            giftCarService.puseFree(uid, 10, 3);
        }
    },
    prize_9(9, "猫耳小女仆*3天", 10) {
        @Override
        public void updateGift(Long uid) {
            GiftCarService giftCarService = SpringAppContext.getBean(GiftCarService.class);
            giftCarService.puseFree(uid, 9, 3);
        }
    },
    prize_10(10, "s级赛车*3天", 5) {
        @Override
        public void updateGift(Long uid) {
            GiftCarService giftCarService = SpringAppContext.getBean(GiftCarService.class);
            giftCarService.puseFree(uid, 3, 3);
        }
    },
    prize_11(11, "养鸡专属座驾*3天", 20) {
        @Override
        public void updateGift(Long uid) {
            GiftCarService giftCarService = SpringAppContext.getBean(GiftCarService.class);
            giftCarService.puseFree(uid, 12, 3);
        }
    },
    prize_12(12, "养鸡的签名照", 3) {
        @Override
        public void updateGift(Long uid) {
        }
    };

    private int prizeId;
    private String prizeName;
    private int maxLimit;

    YoungGPrizeType(int prizeId, String prizeName, int maxLimit) {
        this.prizeId = prizeId;
        this.prizeName = prizeName;
        this.maxLimit = maxLimit;
    }

    public abstract void updateGift(Long uid);

    public int getPrizeId() {
        return prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }
}
