package com.vslk.lbgx.ui.me.setting.presenter;

import com.vslk.lbgx.ui.me.setting.vew.IMsgNotDisturbView;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

public class MessageNotDisturbPresenter extends AbstractMvpPresenter<IMsgNotDisturbView>{


    /**
     * 获取免打扰状态
     */
    public void getMsgDisturbState(){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
        params.put("queryUid",uid);
        params.put("uid",uid);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.getFocusMsgSwitch(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null)
                    getMvpView().getDisturbStateFail(e != null?e.getMessage():"网络异常");
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200){
                    int chatPermission = 0;
                    Json json = response.json("data");
                    if (json != null) {
                        chatPermission = json.num("chatPermission");
                    }
                    if (getMvpView() != null)
                        getMvpView().getDisturbStateSuccess(chatPermission);
                }else {
                    if (getMvpView() != null)
                        getMvpView().getDisturbStateFail(response != null?response.str("message"):"接口异常");
                }
            }
        });
    }


    /**
     * 修改免打扰状态
     * @param state
     */
    public void changeMsgDisturbState(int state){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("chatPermission",state+"");
        params.put("uid",CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.saveFocusMsgSwitch(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null)
                    getMvpView().saveDisturbStateFail(e != null?e.getMessage():"网络异常");
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200){
                    if (getMvpView() != null)
                        getMvpView().saveDisturbStateSuccess(state);
                }else {
                    if (getMvpView() != null)
                        getMvpView().saveDisturbStateFail(response != null?response.str("message"):"接口异常");
                }
            }
        });
    }
}
