package com.tongdaxing.xchat_core.room.lotterybox;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface ILotteryBoxClient extends ICoreClient {
    String onLotterySuccess = "onLotterySuccess";
    String onLOtteryError = "onLOtteryError";

    void onLotterySuccess(String json);

    void onLOtteryError(String errorMsg);
}
