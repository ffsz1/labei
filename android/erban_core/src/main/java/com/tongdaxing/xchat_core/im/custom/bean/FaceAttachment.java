package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tongdaxing.xchat_core.room.face.FaceReceiveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenran
 * @date 2017/9/12
 */

public class FaceAttachment extends IMCustomAttachment {
    private List<FaceReceiveInfo> faceReceiveInfos;
    private long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public FaceAttachment(int first, int second) {
        super(first, second);
    }

    //财富等级
    private int experLevel;
    //魅力等级
    private int charmLevel;

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    public List<FaceReceiveInfo> getFaceReceiveInfos() {
        return faceReceiveInfos;
    }

    public void setFaceReceiveInfos(List<FaceReceiveInfo> faceReceiveInfos) {
        this.faceReceiveInfos = faceReceiveInfos;
    }

    @Override
    protected void parseData(JSONObject data) {

        uid = data.getLong("uid");
        faceReceiveInfos = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        faceReceiveInfos = JSON.parseObject(jsonArray.toJSONString(), new TypeReference<List<FaceReceiveInfo>>() {
        });
        experLevel = data.getInteger("experLevel");
    }

    @Override
    protected JSONObject packData() {

        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(faceReceiveInfos));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        jsonObject.put("uid", uid);
        jsonObject.put("experLevel", experLevel);
        return jsonObject;
    }
}
