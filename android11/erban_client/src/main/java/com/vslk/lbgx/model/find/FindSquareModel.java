package com.vslk.lbgx.model.find;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.find.AlertInfo;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.im.IMProCallBack;

import java.util.List;
import java.util.Map;

public class FindSquareModel extends BaseMvpModel{


    /**
     * 检测公聊大厅的 -- 广场类型
     * @param myCallBack
     */
    public void checkSquareRoomVersion(OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid()+"");
        params.put("ticket",CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getClientChannel(), params, myCallBack);
    }


    /**
     * 进入广场的公聊房间
     * @param roomId
     * @param imProCallBack
     */
    public void enterPublicRoom(String roomId,IMProCallBack imProCallBack){
        ReUsedSocketManager.get().enterChatHallMessage(roomId + "", imProCallBack);
    }

    /**
     * 检测公聊大厅的上报
     * @param myCallBack
     */
    public void checkReport(OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",CoreManager.getCore(IAuthCore.class).getCurrentUid()+"");
        params.put("ticket",CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.reportPublic(), params,myCallBack);
    }


    /**
     * 检测公聊大厅的上报
     * @param imProCallBack
     */
    public void senPublicMsg(String roomId,String content,IMProCallBack imProCallBack) {
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomId);
        PublicChatRoomAttachment publicChatRoomAttachment = new PublicChatRoomAttachment(CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM, CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM);
        publicChatRoomAttachment.setMsg(content);
        if (userInfo != null) {
            publicChatRoomAttachment.setAvatar(userInfo.getAvatar());
            publicChatRoomAttachment.setUid(userInfo.getUid());
            publicChatRoomAttachment.setExperLevel(userInfo.getExperLevel());
            publicChatRoomAttachment.setCharmLevel(userInfo.getCharmLevel());
            publicChatRoomAttachment.setNick(userInfo.getNick());
            message.setAttachment(publicChatRoomAttachment);
        }
        ReUsedSocketManager.get().sendPulicMessage(roomId, message, imProCallBack);
    }

    /**
     * 获取活动
     */
    public void findSquareActivity(OkHttpManager.MyCallBack<ServiceResult<List<AlertInfo>>> callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("type", "1");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        getRequest(UriProvider.getRedBagDialogType(), params, callBack);
    }

    /**
     * 获取交友列表数据
     */
    public void getMeetYouList(int pageNum, OkHttpManager.MyCallBack<ServiceResult<List<HomeRoom>>> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        //交友tag为8
        param.put("tagId", String.valueOf(8));
        param.put("pageNum", String.valueOf(pageNum));
        param.put("pageSize", String.valueOf(Constants.PAGE_SIZE));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        getRequest(UriProvider.getMainDataByTab(), param, callBack);
    }

    /**
     * 获取广场头部数量
     *
     * @param roomId roomId
     */
    public void publicTitle(long roomId, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("room_id", String.valueOf(roomId));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        postRequest(UriProvider.publicTitle(), params, callBack);
    }
}
