package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.common.util.string.StringUtil;

/**
 * Created by chenran on 2017/10/4.
 */

public class RoomTipAttachment extends IMCustomAttachment {
    private long uid;
    private String nick;
    private long targetUid;
    private String targetNick;

    public RoomTipAttachment(int first, int second) {
        super(first, second);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(long targetUid) {
        this.targetUid = targetUid;
    }

    public String getTargetNick() {
        return targetNick;
    }

    public void setTargetNick(String targetNick) {
        this.targetNick = targetNick;
    }

    @Override
    protected void parseData(JSONObject data) {
        uid = data.getLong("uid");
        JSONObject jsonObject = data.getJSONObject("data");
        if (jsonObject.containsKey("nick")) {
            nick = jsonObject.getString("nick");
        }
        if (jsonObject.containsKey("targetUid")) {
            targetUid = jsonObject.getLongValue("targetUid");
        }
        if (jsonObject.containsKey("targetNick")) {
            targetNick = jsonObject.getString("targetNick");
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nick", nick);
        jsonObject.put("targetUid", targetUid);

        if (StringUtil.isEmpty(targetNick)) {
            targetNick = "某某";
        }
        jsonObject.put("targetNick", targetNick);
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        object.put("data", jsonObject);
        return object;
    }
}
