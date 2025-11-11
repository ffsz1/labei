package com.tongdaxing.xchat_core.room.bean;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/6/20
 */
public class StartFingerGuessingGameInfo {


    /**
     * giftInfoVOList : [{"giftGold":88,"giftId":129,"giftName":"小王子","giftNum":9,"giftUrl":"https://pic.hnyueqiang.com/Fpo74E2N0GRDhX-88uXBvJPQ9I8s?imageslim"},{"giftGold":100,"giftId":130,"giftName":"珍珠夹","giftNum":20,"giftUrl":"https://pic.hnyueqiang.com/FhmKkiIt3nFgCjYoSM4IrU0175Ry?imageslim"},{"giftGold":188,"giftId":131,"giftName":"口红","giftNum":29,"giftUrl":"https://pic.hnyueqiang.com/Fgg6QVtoRJRuh1NKQeIQ1zCT1_3A?imageslim"}]
     * moraTime : 30
     * num : 50
     */

    private int moraTime;
    private int num;
    private List<GiftInfoVOListBean> giftInfoVOList;

    public int getMoraTime() {
        return moraTime;
    }

    public void setMoraTime(int moraTime) {
        this.moraTime = moraTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<GiftInfoVOListBean> getGiftInfoVOList() {
        return giftInfoVOList;
    }

    public void setGiftInfoVOList(List<GiftInfoVOListBean> giftInfoVOList) {
        this.giftInfoVOList = giftInfoVOList;
    }

    public static class GiftInfoVOListBean {
        /**
         * giftGold : 88
         * giftId : 129
         * giftName : 小王子
         * giftNum : 9
         * giftUrl : https://pic.hnyueqiang.com/Fpo74E2N0GRDhX-88uXBvJPQ9I8s?imageslim
         */

        private int giftGold;
        private int giftId;
        private String giftName;
        private int giftNum;
        private String giftUrl;

        public int getGiftGold() {
            return giftGold;
        }

        public void setGiftGold(int giftGold) {
            this.giftGold = giftGold;
        }

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(int giftNum) {
            this.giftNum = giftNum;
        }

        public String getGiftUrl() {
            return giftUrl;
        }

        public void setGiftUrl(String giftUrl) {
            this.giftUrl = giftUrl;
        }
    }
}
