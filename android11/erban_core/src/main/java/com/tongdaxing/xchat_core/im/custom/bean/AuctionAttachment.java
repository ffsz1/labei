package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/6/8.
 */

public class AuctionAttachment extends IMCustomAttachment {

    private String uid;

    private AuctionInfo auctionInfo;

    public AuctionInfo getAuctionInfo() {
        return auctionInfo;
    }

    public void setAuctionInfo(AuctionInfo auctionInfo) {
        this.auctionInfo = auctionInfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AuctionAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data1) {
        this.auctionInfo = new AuctionInfo();
        this.auctionInfo.setUid(data1.getLong("uid"));
        this.auctionInfo.setAuctId(data1.getString("auctId"));
        this.auctionInfo.setAuctUid(data1.getLong("auctUid"));
        this.auctionInfo.setAuctMoney(data1.getInteger("auctMoney"));
        this.auctionInfo.setServDura(data1.getInteger("servDura"));
        this.auctionInfo.setMinRaiseMoney(data1.getInteger("minRaiseMoney"));
        this.auctionInfo.setCurMaxUid(data1.getLong("curMaxUid"));
        this.auctionInfo.setCurMaxMoney(data1.getInteger("curMaxMoney"));
        this.auctionInfo.setAuctDesc(data1.getString("auctDesc"));
//        this.auctionInfo.setCreateTime(data1.getLong("createTime"));
        this.auctionInfo.setCurStatus(data1.getInteger("curStatus"));

        JSONArray rivals = data1.getJSONArray("rivals");
        List<AuctionUser> rivalList = new ArrayList<>();
        if (rivals != null) {
            for (int i = 0; i < rivals.size(); i++) {
                JSONObject rivalJson = (JSONObject) rivals.get(i);
                AuctionUser order = new AuctionUser(rivalJson.getString("rivalId"), rivalJson.getString("auctId"), rivalJson.getLong("uid"), rivalJson.getInteger("auctMoney"));
                rivalList.add(order);
            }
        }
        this.auctionInfo.setRivals(rivalList);
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        object.put("data", new Gson().toJson(auctionInfo));
        return object;
    }
}
