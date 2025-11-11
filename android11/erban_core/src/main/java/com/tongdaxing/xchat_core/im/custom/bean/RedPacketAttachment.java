package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;

/**
 * Created by chenran on 2017/10/4.
 */

public class RedPacketAttachment extends CustomAttachment {
    private RedPacketInfoV2 redPacketInfo;

    public RedPacketInfoV2 getRedPacketInfo() {
        return redPacketInfo;
    }

    public void setRedPacketInfo(RedPacketInfoV2 redPacketInfo) {
        this.redPacketInfo = redPacketInfo;
    }

    public RedPacketAttachment(int first, int second) {
        super(first,second);
    }

    @Override
    protected void parseData(JSONObject data) {
        super.parseData(data);
        redPacketInfo = new RedPacketInfoV2();
        redPacketInfo.setType(data.getIntValue("type"));
        redPacketInfo.setUid(data.getLongValue("uid"));
        redPacketInfo.setNeedAlert(data.getBooleanValue("needAlert"));
        redPacketInfo.setPacketNum(data.getDoubleValue("packetNum"));
        redPacketInfo.setPacketName(data.getString("packetName"));
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("type", redPacketInfo.getType());
        object.put("uid", redPacketInfo.getUid());
        object.put("needAlert", redPacketInfo.isNeedAlert());
        object.put("packetNum", redPacketInfo.getPacketNum());
        object.put("packetName", redPacketInfo.getPacketName());
        return object;
    }
}
