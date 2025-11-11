package com.juxiao.xchat.service.api.room;

/**
 * @author chris
 * @Title:
 * @date 2019-05-22
 * @time 17:33
 */
public interface PublicRoomService {

    /**
     * 接收大厅消息
     *
     * @param body
     */
    void receiveMsg(String body);

}
