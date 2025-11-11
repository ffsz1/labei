package com.tongdaxing.xchat_core.manager;


import com.tongdaxing.xchat_framework.im.IMCallBack;
import com.tongdaxing.xchat_framework.im.IMKey;
import com.tongdaxing.xchat_framework.im.IMSendRoute;
import com.tongdaxing.xchat_framework.util.util.Json;

public class IMModelFactory {

    private static IMModelFactory factory;

    public static IMModelFactory get() {
        if (factory == null) {
            synchronized (IMModelFactory.class) {
                if (factory == null) {
                    factory = new IMModelFactory();
                }
            }
        }
        return factory;
    }


    public Json createRequestData(String route, Json requestData) {
        Json json = new Json();
        json.set(IMKey.route, route);

        if (requestData != null)
            json.set("req_data", requestData);
        return json;

    }


    public Json createRequestData(String route) {
        return createRequestData(route, null);
    }

    public static int getSendId() {
        return ++IMCallBack.callbackIndex;
    }


    public Json createLoginModel(String ticket, String uid) {
        Json json = new Json();
        json.set("ticket", ticket);
        json.set("uid", uid);
        //android为2
        json.set("page_name", 2);
        return createRequestData(IMSendRoute.login, json);
    }


    /**
     * 创建房间非文本消息
     *
     * @param roomId
     * @param msgJson
     * @return
     */
    public Json createNotTxtMsg(String roomId, String msgJson) {
        Json json = new Json();
        json.set("room_id", roomId);
        return createRequestData(IMSendRoute.sendMessage, json);
    }

    /**
     * 创建房间文本消息
     *
     * @param room_id
     * @param msgJson
     * @return
     */
    public Json createTxtMsg(String room_id, String msgJson) {
        Json json = new Json();
        json.set("room_id", room_id);
        return createRequestData(IMSendRoute.sendText, json);
    }

    /**
     * 进入聊天室
     *
     * @param room_id
     * @return
     */
    public Json createJoinAvRoomModel(long room_id) {
        Json json = new Json();
        json.set("room_id", room_id);
        return createRequestData(IMSendRoute.enterChatRoom, json);
    }

    /**
     * 进入聊天室
     *
     * @param room_id
     * @return
     */
    public Json createExitRoom(long room_id) {
        Json json = new Json();
        json.set("room_id", room_id);
        return createRequestData(IMSendRoute.exitChatRoom, json);
    }


    /**
     * 更新队列 -- 加入新的队列元素
     * @param roomId 房间id
     * @param position 队列位置 -1 房主位  1-7 主播位
     * @param uid 加入队列用户uid
     * @return
     */
    public Json createUpdateQueue(String roomId,int position,long uid){
        Json json = new Json();
        json.set("room_id",roomId);
        json.set("key",position);
        json.set("uid",uid);
        return createRequestData(IMSendRoute.updateQueue,json);
    }

    /**
     * 更新队列 -- 加入新的队列元素
     * @param roomId 房间id
     * @param position 队列位置 -1 房主位  1-7 主播位
     * @return
     */
    public Json createPollQueue(String roomId,int position){
        Json json = new Json();
        json.set("room_id",roomId);
        json.set("key",position);
        return createRequestData(IMSendRoute.pollQueue,json);
    }

}
