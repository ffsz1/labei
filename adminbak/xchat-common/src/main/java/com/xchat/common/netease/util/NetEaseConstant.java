
package com.xchat.common.netease.util;

import com.xchat.common.utils.PropertyUtil;

public  class NetEaseConstant {

    public  static final String appKey= PropertyUtil.getProperty("netEaseAppKey");
    public static final String appSecret=PropertyUtil.getProperty("netEaseAppSecret");

    public  static final String smsAppKey= PropertyUtil.getProperty("netEaseSmsAppKey");
    public static final String smsAppSecret=PropertyUtil.getProperty("netEaseSmsAppSecret");

    public static String basicUrl="https://api.netease.im/nimserver";
    public static String smsBasicUrl="https://api.netease.im/sms";
    public static final int smsCodeLen=5;
    public static final int smsTemplateid=Integer.valueOf(PropertyUtil.getProperty("smsTemplateid"));
    public static class AccUrl{
        public  static String get="/user/getUinfos.action";
        public  static String create="/user/create.action";
        public  static String refreshToken="/user/refreshToken.action";
        public  static String block="/user/block.action";
        public  static String unblock="/user/unblock.action";
    }

    public static class UserUrl{
        public  static String get="/user/getUinfos.action";
        public  static String update="/user/update.action";
        public  static String updateUinfo="/user/updateUinfo.action";

    }

    public static class FriendsUrl{
        public  static String friendAdd="/friend/add.action";
        public  static String friendDelete="/friend/delete.action";
    }

    public static class RoomUrl{
        public  static String create="/chatroom/create.action";
        public  static String get="/chatroom/get.action";
        public  static String update="/chatroom/update.action";
        public  static String sendMsg="/chatroom/sendMsg.action";
        public  static String toggleCloseStat="/chatroom/toggleCloseStat.action";
        public  static String setMemberRole="/chatroom/setMemberRole.action";
        public  static String addRobot="/chatroom/addRobot.action";
        public  static String removeRobot="/chatroom/removeRobot.action";
        public  static String temporaryMute="/chatroom/temporaryMute.action";
        public  static String membersByPage="/chatroom/membersByPage.action";
        public  static String membersList="/chatroom/queryMembers.action";
        public  static String queueDrop="/chatroom/queueDrop.action";
        public  static String queueList="/chatroom/queueList.action";    // 列出队列中的所有元素
        public  static String queueOffer="/chatroom/queueOffer.action";  // 往聊天室有序队列中新加或更新元素
        public  static String queuePoll="/chatroom/queuePoll.action";
        public  static String requestAddr="/chatroom/requestAddr.action";
        // 初始化云信队列
        public  static String initQueue = "/chatroom/queueInit.action";
    }

    public static class SmsUrl{
        public  static String smsVerify="/verifycode.action";
        public  static String sendSms="/sendcode.action";
    }
    public static class MsgUrl{
        public  static String sendMsg="/msg/sendMsg.action";
        public  static String sendBatchMsg="/msg/sendBatchMsg.action";
        public  static String broadcastMsg="/msg/broadcastMsg.action";

        public  static String sendAttachMsg="/msg/sendAttachMsg.action";
        public  static String sendBatchAttachMsg="/msg/sendBatchAttachMsg.action";

        public  static String uploadImg="/msg/upload.action";
    }

}
