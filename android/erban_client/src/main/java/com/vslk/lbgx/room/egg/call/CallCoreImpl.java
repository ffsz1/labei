package com.vslk.lbgx.room.egg.call;

import com.blankj.utilcode.util.ToastUtils;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.vslk.lbgx.room.egg.bean.CallBean;

import java.util.Map;

public class CallCoreImpl implements ICallCore {

    private CallCoreImpl() {
    }

    public static CallCoreImpl getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final CallCoreImpl sInstance = new CallCoreImpl();
    }

    public ICallCore getICallCore() {
        return this;
    }

    @Override
    public void sendCall(MicMemberInfo user, GiftInfo info, ICallBackListener<CallBean> listener) {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo == null) return;
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("giftId", info.getGiftId() + "");
        params.put("targetUid", user.getUid() + "");
        params.put("uid", uid + "");
        params.put("ticket", ticket);
        params.put("roomUid", currentRoomInfo.getUid() + "");
        params.put("giftNum", "1");
        if (user.getUid() == currentRoomInfo.getUid()) {
            params.put("type", "1");
        } else {
            params.put("type", "3");
        }
        OkHttpManager.getInstance().doPostRequest(UriProvider.callForUser(), params, new OkHttpManager.MyCallBack<ServiceResult<CallBean>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<CallBean> response) {
                if (response.isSuccess()) {
                    ToastUtils.showLong("打call成功");
                    if (listener != null) listener.onSuccess(response.getData());

                    CallBean callBean = response.getData();
                    CoreManager.getCore(IGiftCore.class).sendCallGiftMeg(callBean.getUid(), callBean.getGiftName(),callBean.getNick(),callBean.getTargetNick(),callBean.getGiftPic(),currentRoomInfo.getRoomId());
                } else {
                    ToastUtils.showLong(response.getMessage());
                }
            }
        });
    }

    @Override
    public void getGold(ICallBackListener<WalletInfo> listener) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("Cache-Control", "no-cache");
        OkHttpManager.getInstance().getRequest(UriProvider.getWalletInfos(), params, new OkHttpManager.MyCallBack<ServiceResult<WalletInfo>>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(ServiceResult<WalletInfo> response) {
                if (null != response && response.isSuccess()) {
                    if (listener != null) listener.onSuccess(response.getData());
                }
            }
        });
    }


    public interface ICallBackListener<T> {
        void onSuccess(T response);
    }
}
