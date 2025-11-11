package com.tongdaxing.xchat_core.im.custom.bean.nim;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;

public class TransferAttachment extends CustomAttachment {
    private int sendUid;
    private String sendName;
    private String sendAvatar;
    private int recvUid;
    private String recvName;
    private String recvAvatar;
    private int goldNum;
//    private String sendDesc;

    public TransferAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("sendUid", sendUid);
        object.put("recvUid", recvUid);
        object.put("goldNum", goldNum);
        object.put("sendName", sendName);
        object.put("sendAvatar", sendAvatar);
        object.put("recvName", recvName);
        object.put("recvAvatar", recvAvatar);
//        object.put("sendDesc", sendDesc);
        return object;
    }

    @Override
    protected void parseData(JSONObject data) {
        sendUid = data.getIntValue("sendUid");
        recvUid = data.getIntValue("recvUid");
        sendAvatar = data.getString("sendAvatar");
        goldNum = data.getIntValue("goldNum");
        sendName = data.getString("sendName");
        recvName = data.getString("recvName");
        recvAvatar = data.getString("recvAvatar");
//        sendDesc = data.getString("sendDesc");
    }


    public int getSendUid() {
        return sendUid;
    }

    public void setSendUid(int sendUid) {
        this.sendUid = sendUid;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendAvatar() {
        return sendAvatar;
    }

    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }

    public int getRecvUid() {
        return recvUid;
    }

    public void setRecvUid(int recvUid) {
        this.recvUid = recvUid;
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }

    public String getRecvAvatar() {
        return recvAvatar;
    }

    public void setRecvAvatar(String recvAvatar) {
        this.recvAvatar = recvAvatar;
    }

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }

//    public String getSendDesc() {
//        return sendDesc;
//    }
//
//    public void setSendDesc(String sendDesc) {
//        this.sendDesc = sendDesc;
//    }
}
