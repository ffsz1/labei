package com.tongdaxing.xchat_core.room.model;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.FingerGuessingGameAttachment;
import com.tongdaxing.xchat_core.im.room.IIMRoomCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

public class FingerGuessingGameModel extends RoomBaseModel {

//    public void sendStartFingerGuessingGameMsg(String avatar, String nick, int level, String giftUrl) {
//        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
//        FingerGuessingGameAttachment gameAttachment = new FingerGuessingGameAttachment(IMCustomAttachment.CUSTOM_MSG_START_FINGER_GUESSING_GAME_FIRST,
//                IMCustomAttachment.CUSTOM_MSG_START_FINGER_GUESSING_GAME_SECOND);
//        gameAttachment.setNick(nick);
//        gameAttachment.setAvatar(avatar);
//        gameAttachment.setLevel(level);
//        gameAttachment.setGiftUrl(giftUrl);
//        ChatRoomMessage message = new ChatRoomMessage();
//        message.setRoom_id(roomInfo.getRoomId() + "");
//        message.setAttachment(gameAttachment);
//        CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
//    }

    public FingerGuessingGameModel() {
    }

    /**
     * 获取猜拳状态
     *
     * @param roomId
     */
    public void getFingerGuessingGameState(long roomId, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("roomId", String.valueOf(roomId));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getFingerGuessingGameState(), param, myCallBack);
    }

    /**
     * 发起猜拳
     *
     * @param roomId
     * @param probability
     * @param myCallBack
     */
    public void startFingerGuessingGame(long roomId, int probability, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("probability", String.valueOf(probability));
        param.put("roomId", String.valueOf(roomId));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.startFingerGuessingGame(), param, myCallBack);
    }

    public void confirmFingerGuessingGame(long roomId, int probability, int choose, int giftId, int giftNum, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("choose", String.valueOf(choose));
        param.put("giftId", String.valueOf(giftId));
        param.put("giftNum", String.valueOf(giftNum));
        param.put("probability", String.valueOf(probability));
        param.put("roomId", String.valueOf(roomId));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.confirmFingerGuessingGame(), param, myCallBack);
    }

    public void pkFingerGuessingGame(long recordId, int probability, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("probability", String.valueOf(probability));
        param.put("recordId", String.valueOf(recordId));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.pkFingerGuessingGame(), param, myCallBack);
    }

    public void confrimPkFingerGuessingGame(String recordId, int choose, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("recordId", recordId);
        param.put("choose", String.valueOf(choose));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.confrimPkFingerGuessingGame(), param, myCallBack);
    }

    /**
     * 获取猜拳记录
     *
     * @param pageSize
     * @param myCallBack
     */
    public void getFingerGuessingGameRecord(long pageSize, long current, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("pageSize", String.valueOf(pageSize));
        param.put("current", String.valueOf(current));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getFingerGuessingGameRecord(), param, myCallBack);
    }

    /**
     * 获取房间内未被PK的猜拳消息
     *
     * @param roomId
     * @param myCallBack
     */
    public void getListFingerGuessingGame(long roomId, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("roomId", String.valueOf(roomId));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getListFingerGuessingGame(), param, myCallBack);
    }

    public void getProbability(long roomId, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("roomId", String.valueOf(roomId));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getProbability(), param, myCallBack);
    }
}











