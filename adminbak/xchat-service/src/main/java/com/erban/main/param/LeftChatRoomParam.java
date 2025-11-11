package com.erban.main.param;

import com.google.gson.Gson;

public class LeftChatRoomParam {
    private String eventType;	//	值为9，特别注意这里是9，表示是一个主播或管理员进出聊天室事件
    private Long roomId;	//	聊天室id
    private String event;	//	进入或退出。IN：进入聊天室；OUT：主动退出聊天室，或掉线
    private String accid;	//	用户帐号，字符串类型
    private String clientIp;	//	客户端的ip地址，OUT时不保证能提供此字段
    private String clientType;	//	客户端类型： AOS、IOS、PC、WINPHONE、WEB、REST，字符串类型
    private Integer code;	//	返回码，可转为Integer类型数据
    private String sdkVersion;	//	当前sdk的版本信息，字符串类型
    private Long timestamp;	//	事件发生时的时间戳，可转为Long型数据

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public static void main(String args[]){
        String str="{\"timestamp\":\"1497496814034\",\"accid\":\"900118\",\"event\":\"OUT\",\"roomId\":\"9420384\",\"code\":\"200\",\"eventType\":\"9\",\"clientType\":\"IOS\",\"sdkVersion\":\"34\"}";
        Gson gson=new Gson();
        LeftChatRoomParam LeftChatRoomParam=gson.fromJson(str,LeftChatRoomParam.class);
        System.out.println(LeftChatRoomParam.getTimestamp());
    }
}
