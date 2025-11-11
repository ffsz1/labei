package com.tongdaxing.xchat_core.bean.attachmsg;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;

/**
 * <p>  队列自定义消息</p>
 *
 * @author jiahui
 * @date 2017/12/18
 */
public class RoomQueueMsgAttachment extends IMCustomAttachment {
    public RoomQueueInfo roomQueueInfo;
    public String uid;
    public int micPosition;
    public String adminOrManagerUid;//群主或者管理员uid(抱上麦)
    public String micName;//麦上用户名称

    public RoomQueueMsgAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        if (uid != null) {
            jsonObject.put("uid", uid);
        }
        if (adminOrManagerUid != null) {
            jsonObject.put("adminOrManagerUid", adminOrManagerUid);
        }
        if (micName != null) {
            jsonObject.put("micName", micName);
        }
        jsonObject.put("micPosition", micPosition);
        return jsonObject;
    }

    @Override
    protected void parseData(JSONObject data) {
        super.parseData(data);
        if (data != null) {
            if (data.containsKey("uid")) {
                uid = data.getString("uid");
            }
            if (data.containsKey("adminOrManagerUid")) {
                adminOrManagerUid = data.getString("adminOrManagerUid");
            }
            if (data.containsKey("micName")) {
                micName = data.getString("micName");
            }
            if (data.containsKey("micPosition")) {
                micPosition = data.getIntValue("micPosition");
            }
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAdminOrManagerUid() {
        return adminOrManagerUid;
    }
    public void setAdminOrManagerUid(String adminOrManagerUid) {
        this.adminOrManagerUid = adminOrManagerUid;
    }

    public String getMicName() {
        return micName;
    }
    public void setMicName(String micName) {
        this.micName = micName;
    }
}
