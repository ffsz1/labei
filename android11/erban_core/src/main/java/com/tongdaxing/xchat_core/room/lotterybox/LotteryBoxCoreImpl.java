package com.tongdaxing.xchat_core.room.lotterybox;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.ConchGiftInfo;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

import static com.tongdaxing.xchat_core.UriProvider.JAVA_WEB_URL;

/**
 * Created by Administrator on 2018/4/12.
 */

public class LotteryBoxCoreImpl extends AbstractBaseCore implements ILotteryBoxCore {
    /**
     * @param type 1是一次，2是十次，3是自动捡海螺
     */
    @Override
    public void lotteryRequest(int type, OkHttpManager.MyCallBack<ServiceResult<List<EggGiftInfo>>> jsonMyCallBack) {
        int numbersType = type == 3 ? 1 : type;//自动捡海螺：1次1次的砸
        Map<String, String> requestParam = CommonParamUtil.getDefaultParam();
        IAuthCore core = CoreManager.getCore(IAuthCore.class);
        String url = JAVA_WEB_URL + "/user/giftPurse/dy/draw";
        requestParam.put("uid", core.getCurrentUid() + "");
        requestParam.put("type", String.valueOf(numbersType));
        requestParam.put("ticket", core.getTicket());
        requestParam.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo != null ? (AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "") : "");
        OkHttpManager.getInstance().doPostRequest(url, requestParam, jsonMyCallBack);
    }

    @Override
    public void freeGiftRequest() {

    }
}
