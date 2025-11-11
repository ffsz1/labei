package com.erban.main.testclient;

import com.erban.main.service.ErBanNetEaseService;

/**
 * Created by liuguofu on 2017/10/5.
 */
public class RoomClient {
    static ErBanNetEaseService erBanNetEaseService=new ErBanNetEaseService();
    public static void main(String args[]) throws Exception{
        getRoomInfo(10740002l);
//        getRoomMemberListInfo(17856834L,90575L);

    }

    public static  String getRoomInfo(Long roomId) throws Exception{
        erBanNetEaseService.getRoomMessage(roomId);
        return "";
    }
    public static  String getRoomMemberListInfo(Long roomId,Long uid) throws Exception{
        erBanNetEaseService.getRoomMemberListInfo(roomId,uid);
        return "";
    }
}
