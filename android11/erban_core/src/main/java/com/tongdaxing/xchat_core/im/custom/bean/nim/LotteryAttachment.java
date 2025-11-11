package com.tongdaxing.xchat_core.im.custom.bean.nim;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.activity.bean.LotteryInfo;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;

/**
 * Created by chenran on 2017/12/26.
 */

public class LotteryAttachment extends CustomAttachment {
    private LotteryInfo lotteryInfo;

    public LotteryInfo getLotteryInfo() {
        return lotteryInfo;
    }

    public void setLotteryInfo(LotteryInfo lotteryInfo) {
        this.lotteryInfo = lotteryInfo;
    }

    public LotteryAttachment(int first, int second) {
        super(first,second);
    }

    @Override
    protected void parseData(JSONObject data) {
        super.parseData(data);
        lotteryInfo = new LotteryInfo();
        lotteryInfo.setLeftDrawNum(data.getIntValue("leftDrawNum"));
        lotteryInfo.setUid(data.getLongValue("uid"));
        lotteryInfo.setTotalDrawNum(data.getIntValue("totalDrawNum"));
        lotteryInfo.setTotalWinDrawNum(data.getIntValue("totalWinDrawNum"));
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("leftDrawNum", lotteryInfo.getLeftDrawNum());
        object.put("uid", lotteryInfo.getUid());
        object.put("totalDrawNum", lotteryInfo.getTotalDrawNum());
        object.put("totalWinDrawNum", lotteryInfo.getTotalWinDrawNum());
        return object;
    }
}
