package com.vslk.lbgx.ui.me.withdraw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.Map;

public class DiamondsCoreImpl implements IDiamondsCore {

    private DiamondsCoreImpl() {
    }

    public static DiamondsCoreImpl getInstance() {
        return DiamondsCoreImpl.Holder.sInstance;
    }

    private static class Holder {
        private static final DiamondsCoreImpl sInstance = new DiamondsCoreImpl();
    }

    public IDiamondsCore get() {
        return this;
    }

    @Override
    public void getFinancialAccount(ISuccessListener<BindingBean> listener) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getFinancialAccount(), params, new OkHttpManager.MyCallBack<ServiceResult<BindingBean>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<BindingBean> response) {
                if (response.isSuccess()) {
                    listener.success(response.getData());
                } else {
                    listener.success(new BindingBean());
                }
            }
        });
    }

    @Override
    public void isAuthentication(ISuccessListener<Boolean> listener) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.realnameUser(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                SingleToastUtil.showToast(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Json response) {
                JSONObject it = JSON.parseObject(response.toString());
                String message = it.getString("message");
                int code = it.getIntValue("code");
                //未实名认证跳转
                if (code == 200) {
                    listener.success(true);
                } else if (code == 2501) {
                    listener.success(false);
                } else {
                    SingleToastUtil.showToast(message);
                }
            }
        });
    }

    @Override
    public void bindWithdrawAccount(ISuccessListener<Boolean> listener, String... args) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("account", args[0]);
        params.put("accountName", args[1]);
        params.put("accountType", args[2]);
        params.put("diamondId", args[3]);
        params.put("phone", args[4]);
        params.put("smsCode", args[5]);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.bindWithdrawAccount(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                SingleToastUtil.showToast(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Json response) {
                JSONObject it = JSON.parseObject(response.toString());
                String message = it.getString("message");
                int code = it.getIntValue("code");
                if (code == 200) {
                    listener.success(true);
                } else {
                    SingleToastUtil.showToast(message);
                }
            }
        });
    }
}
