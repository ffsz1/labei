package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tongdaxing.xchat_core.bean.RoomCharmInfo;

import java.util.Map;

/**
 * 文件描述：
 *
 * @auther：zwk
 * @data：2019/3/5
 */
public class RoomCharmAttachment extends IMCustomAttachment {
    private long timestamps;
    public Map<String,RoomCharmInfo> latestCharm;

    public RoomCharmAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data) {
        if (data != null && data.containsKey("latestCharm")){
           JSONObject object =  data.getJSONObject("latestCharm");
           timestamps = data.getLongValue("timestamps");
           latestCharm = JSONObject.parseObject(object.toJSONString(),new TypeReference<Map<String, RoomCharmInfo>>(){});
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamps",timestamps);
        jsonObject.put("latestCharm", JSONObject.toJSON(latestCharm));
        return jsonObject;
    }

    public Map<String, RoomCharmInfo> getLatestCharm() {
        return latestCharm;
    }

    public void setLatestCharm(Map<String, RoomCharmInfo> latestCharm) {
        this.latestCharm = latestCharm;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }
}
