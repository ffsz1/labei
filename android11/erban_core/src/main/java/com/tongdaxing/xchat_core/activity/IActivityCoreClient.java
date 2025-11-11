package com.tongdaxing.xchat_core.activity;

import com.tongdaxing.xchat_core.activity.bean.LotteryInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by chenran on 2017/12/26.
 */

public interface IActivityCoreClient extends ICoreClient {
    public static final String METHOD_ON_RECEIVE_LOTTERY_ACTIVITY= "onReceiveLotteryActivity";

    void onReceiveLotteryActivity(LotteryInfo lotteryInfo);
}
