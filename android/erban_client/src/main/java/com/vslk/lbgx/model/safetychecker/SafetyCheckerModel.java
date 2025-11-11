package com.vslk.lbgx.model.safetychecker;

import com.hncxco.safetychecker.bean.SafetyCheckResultBean;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.json.JsonParser;

import java.util.Map;

/**
 * 安全检测model
 */
public class SafetyCheckerModel extends BaseMvpModel {

    /**
     * 上报安全检测结果
     *
     * @param checkResult 安全检测结果
     */
    public void reportSafetyCheckResult(SafetyCheckResultBean checkResult) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("msgId", System.currentTimeMillis() + "");
        params.put("content", JsonParser.toJson(checkResult));
        OkHttpManager.getInstance().doPostRequest(UriProvider.reportSafetyCheckResult(), params, null);
    }
}
