package com.tongdaxing.xchat_core.room.bean;

/**
 * Function:
 * Author: Edward on 2019/6/21
 */
public class MoreUnpkFingerGuessingMsgInfo {


    /**
     * data : {"roomId":10010589,"timestamps":1561106539452,"moraRecordMessage":"{\"erbanNo\":3862990,\"uid\":10241,\"nick\":\"Edward\",\"avatar\":\"https://pic.hnyueqiang.com/FtPDUcf119fbwCYsKiollKTspkTR?imageslim\",\"experienceLevel\":27,\"charmLevel\":1,\"subject\":\"发起猜拳\",\"recordId\":33,\"giftId\":129,\"giftName\":\"小王子\",\"giftUrl\":\"https://pic.hnyueqiang.com/Fpo74E2N0GRDhX-88uXBvJPQ9I8s?imageslim\",\"giftNum\":\"3\",\"createTime\":\"Jun 21, 2019 4:02:32 PM\"}"}
     * first : 34
     * second : 1
     */

    private DataBean data;
    private int first;
    private int second;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public static class DataBean {
        /**
         * roomId : 10010589
         * timestamps : 1561106539452
         * moraRecordMessage : {"erbanNo":3862990,"uid":10241,"nick":"Edward","avatar":"https://pic.hnyueqiang.com/FtPDUcf119fbwCYsKiollKTspkTR?imageslim","experienceLevel":27,"charmLevel":1,"subject":"发起猜拳","recordId":33,"giftId":129,"giftName":"小王子","giftUrl":"https://pic.hnyueqiang.com/Fpo74E2N0GRDhX-88uXBvJPQ9I8s?imageslim","giftNum":"3","createTime":"Jun 21, 2019 4:02:32 PM"}
         */

        private int roomId;
        private long timestamps;
        private String moraRecordMessage;

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public long getTimestamps() {
            return timestamps;
        }

        public void setTimestamps(long timestamps) {
            this.timestamps = timestamps;
        }

        public String getMoraRecordMessage() {
            return moraRecordMessage;
        }

        public void setMoraRecordMessage(String moraRecordMessage) {
            this.moraRecordMessage = moraRecordMessage;
        }
    }
}
