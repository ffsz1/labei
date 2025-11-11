package com.tongdaxing.xchat_core.room.lotterybox;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.gift.ConchGiftInfo;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface ILotteryBoxCore extends IBaseCore {
    void lotteryRequest(int type,OkHttpManager.MyCallBack<ServiceResult<List<EggGiftInfo>>> jsonMyCallBack);
    void freeGiftRequest();
}
