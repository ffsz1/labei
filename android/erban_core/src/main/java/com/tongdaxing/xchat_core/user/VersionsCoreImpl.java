package com.tongdaxing.xchat_core.user;

import android.text.TextUtils;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.user.bean.CheckUpdataBean;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SpUtils;

import java.util.Map;


/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class VersionsCoreImpl extends AbstractBaseCore implements VersionsCore {

    public VersionsCoreImpl() {
        String configStr = (String) SpUtils.get(getContext(), SpEvent.config_key, "");
        configdata = Json.parse(configStr);
    }

    private int checkKick = 0;
    private Json configdata;
    private String sensitiveWordData;

    @Override
    public void getVersions(int version) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("version", String.valueOf(1.10));

        OkHttpManager.getInstance().getRequest(UriProvider.getVersions(), params, new OkHttpManager.MyCallBack<ServiceResult<Boolean>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(AttentionCoreClient.class, VersionsCoreClient.METHOD_GET_VERSION_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<Boolean> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(AttentionCoreClient.class, VersionsCoreClient.METHOD_GET_VERSION, response.getData());
                    } else {
                        notifyClients(AttentionCoreClient.class, VersionsCoreClient.METHOD_GET_VERSION_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void checkVersion() {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        OkHttpManager.getInstance().getRequest(UriProvider.checkUpdata(), params, new OkHttpManager.MyCallBack<ServiceResult<CheckUpdataBean>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<CheckUpdataBean> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        CheckUpdataBean data = response.getData();
                        notifyClients(VersionsCoreClient.class, VersionsCoreClient.METHOD_VERSION_UPDATA_DIALOG, data);
                        if (data != null) {
                            checkKick = data.isKickWaiting();
                        }
                    }
                }
            }
        });
    }

    /**
     * 能否使用支付宝
     *
     * @return true可以使用，false不可以使用
     */
    @Override
    public boolean canUseAliPay() {
        Json json = getConfigData();
        String temp = json.str(Constants.BASE_CONFIG_ALI_PAY_SWITCH);
        if (!TextUtils.isEmpty(temp) && temp.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int checkKick() {
        return checkKick;
    }

    @Override
    public void getConfig() {

        Map<String, String> params = CommonParamUtil.getDefaultParam();

        OkHttpManager.getInstance().getRequest(UriProvider.getConfigUrl(), params, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {
                if (response.num("code") == 200) {
                    Json data = response.json_ok("data");
                    configdata = data;
                    SpUtils.put(getContext(), SpEvent.config_key, data + "");
                }
            }
        });
    }

    @Override
    public Json getConfigData() {
        return configdata;
    }

    @Override
    public void requestSensitiveWord() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        OkHttpManager.getInstance().getRequest(UriProvider.getSensitiveWord(), params, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {
                if (response.num("code") == 200) {
                    String data = response.str("data");
                    sensitiveWordData = data;
                }
            }
        });
    }

    @Override
    public String getSensitiveWordData() {
        return sensitiveWordData == null ? "" : sensitiveWordData;
    }


}
