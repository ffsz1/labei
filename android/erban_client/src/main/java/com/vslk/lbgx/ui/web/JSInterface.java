package com.vslk.lbgx.ui.web;

import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.find.activity.InviteAwardActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.UIHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.JsResponseInfo;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.http_image.util.DeviceUuidFactory;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.json.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> html js 与webview 交互接口</p>
 * Created by ${user} on 2017/11/6.
 */
public class JSInterface {
    private static final String TAG = JSInterface.class.getSimpleName();
    private WebView mWebView;
    private CommonWebViewActivity mActivity;
    private int mPosition;

    public JSInterface(WebView webView, CommonWebViewActivity activity) {
        mWebView = webView;
        mActivity = activity;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * 调转个人主页
     *
     * @param uid 用户id
     */
    @JavascriptInterface
    public void openPersonPage(String uid) {
        LogUtil.i(TAG, "openPersonPage：" + uid);
        if (!TextUtils.isEmpty(uid)) {
            try {
                long uidLong = Long.parseLong(uid);
                UIHelper.showUserInfoAct(mActivity, uidLong);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @JavascriptInterface
    public void closeWin(boolean flag) {
        mActivity.finish();
    }

    @JavascriptInterface
    public void openTeenagerModelCallback() {//成功设置青少年模式
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getTeenagerModelInfo(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                SingleToastUtil.showToast("青少年模式设置失败!");
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200 && response.obj("data") != null) {
                    CoreManager.notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_OPEN_TEENAGER_MODEL_DIALOG);
                } else {
                    onError(new Exception());
                }
            }
        });
    }

    /**
     * 跳转充值页面
     */
    @JavascriptInterface
    public void openChargePage() {
        if (mActivity != null) {
            WalletActivity.start(mActivity);
        }
    }

    @JavascriptInterface
    public void showShareButton(boolean isShow) {
        if (mActivity != null && mActivity.getToolbar() != null) {
            mActivity.runOnUiThread(() -> {
                if (mActivity != null && mActivity.getToolbar() != null) {
                    mActivity.getToolbar().setRightImageBtnVisibility(isShow ? View.VISIBLE : View.GONE);
                }
            });

        }
    }

    @JavascriptInterface
    public String getUserPhoneNumber() {
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getPhone()) && !userInfo.getPhone().equals(String.valueOf(userInfo.getErbanNo()))) {//手机号为用户id 也是属于未绑定手机号
            return userInfo.getPhone();
        }
        return "";
    }

    /**
     * 通知H5图片选择结果
     */
    public void onImageChooserResult(String imageUrl) {
        mWebView.evaluateJavascript("onImageChooserResult('" + imageUrl + "')", value -> {

        });
    }

    @JavascriptInterface
    public void skipToInviteFriends(String json) {//跳转邀请页面
        if (mActivity != null) {
            InviteAwardActivity.start(mActivity);
        }
    }

    /**
     * 请求调起拍照+相册选择器
     */
    @JavascriptInterface
    public void requestImageChooser() {
        if (mActivity != null) {
            mActivity.showImageChooser();
        }
    }

    @JavascriptInterface
    public void setupNavigationBarRightItem(String imageUrl) {
        if (mActivity != null && !mActivity.isFinishing() && !mActivity.isDestroyed()) {
            mActivity.runOnUiThread(() -> {
                if (mActivity == null || mActivity.isFinishing() || mActivity.isDestroyed())
                    return;
                if (mActivity.getToolbar() != null && StringUtils.isNotEmpty(imageUrl)) {
                    Json json = Json.parse(imageUrl);
                    Json data = json.json("data");
                    if (data != null && data.has("imageUrl")) {
                        String url = data.str("imageUrl");
                        if (StringUtils.isNotEmpty(url)) {
                            ImageView imageView = mActivity.getToolbar().getIvRight();
                            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                            rl.width = ConvertUtils.dp2px(35);
                            rl.height = rl.width;
                            imageView.setLayoutParams(rl);
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mWebView != null) {
                                        mWebView.loadUrl("javascript:onNavigationBarRightItemDidClicked()");
                                    }
                                }
                            });
                            ImageLoadUtils.loadImage(mActivity, url, imageView);
                        }
                    }
                }
            });
        }
    }

    private Map<String, String> mapObejctsToMapStrings(Map<String, Object> mapObjects) {
        Map<String, String> mapStrings = new HashMap<>();
        if (mapObjects != null) {
            for (Map.Entry<String, Object> mapObject : mapObjects.entrySet()) {
                mapStrings.put(mapObject.getKey(), mapObject.getValue().toString());
            }
        }
        return mapStrings;
    }

    /**
     * 发起网络请求
     */
    @JavascriptInterface
    public void httpRequest(int requestMethod, String urlController, String headerMapString, String paramMapString) {
        Map<String, String> header;
        Map<String, String> params;
        try {
            header = mapObejctsToMapStrings(JsonParser.toMap(headerMapString));
            params = mapObejctsToMapStrings(JsonParser.toMap(paramMapString));
        } catch (Exception e) {
            e.printStackTrace();
            onHttpResponse(urlController, true, "", e.getMessage());
            return;
        }
        if (params == null) {
            params = new HashMap<>();
        }
        params = CommonParamUtil.getDefaultParam(params);
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        String url = UriProvider.JAVA_WEB_URL.concat(urlController);
        if (requestMethod == 1) {
            OkHttpManager.getInstance().doPostRequest(url, header, params, new OkHttpManager.MyCallBack<Json>() {
                @Override
                public void onError(Exception e) {
                    onHttpResponse(urlController, true, "", e.getMessage());
                }

                @Override
                public void onResponse(Json response) {
                    if (response != null) {
                        onHttpResponse(urlController, false, response.toString(), "");
                    } else {
                        onHttpResponse(urlController, true, "", "数据异常");
                    }
                }
            });
        } else {
            OkHttpManager.getInstance().getRequest(url, header, params, new OkHttpManager.MyCallBack<Json>() {
                @Override
                public void onError(Exception e) {
                    onHttpResponse(urlController, true, "", e.getMessage());
                }

                @Override
                public void onResponse(Json response) {
                    if (response != null) {
                        onHttpResponse(urlController, false, response.toString(), "");
                    } else {
                        onHttpResponse(urlController, true, "", "数据异常");
                    }
                }
            });
        }
    }

    /**
     * 通知H5请求结果
     */
    public void onHttpResponse(String urlController, boolean isRequestError, String bodyString, String errorMsg) {
        String responseStr;
        JsResponseInfo responseInfo = new JsResponseInfo();
        responseInfo.setUrlController(urlController);
        responseInfo.setRequestError(isRequestError);
        responseInfo.setBodyString(bodyString);
        responseInfo.setErrorMsg(errorMsg);
        responseStr = JsonParser.toJson(responseInfo);
        mWebView.evaluateJavascript("onHttpResponse(" + responseStr + ")", value -> {

        });
    }


    @JavascriptInterface
    public void openSharePage() {
        if (mActivity != null) {
            ShareDialog shareDialog = new ShareDialog(mActivity);
            shareDialog.setOnShareDialogItemClick(mActivity);
            shareDialog.show();
        }
    }

    /**
     * 调转钱包页
     */
    @JavascriptInterface
    public void openPurse() {
        LogUtil.i(TAG, "openPurse：");
    }

    /**
     * 调转房间
     *
     * @param uid 房主uid
     */
    @JavascriptInterface
    public void openRoom(String uid) {
        LogUtil.i(TAG, "openRoom：" + uid);
        if (!TextUtils.isEmpty(uid)) {
            try {
                long uidLong = Long.parseLong(uid);
                AVRoomActivity.start(mActivity, uidLong);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 获取用户ticket
     *
     * @return
     */
    @JavascriptInterface
    public String getTicket() {
        return CoreManager.getCore(IAuthCore.class).getTicket();
    }

    /**
     * 获取设备ID
     *
     * @return 设备id
     */
    @JavascriptInterface
    public String getDeviceId() {
        return DeviceUuidFactory.getDeviceId(BasicConfig.INSTANCE.getAppContext());
    }

    /**
     * 获取uid
     *
     * @return uid
     */
    @JavascriptInterface
    public String getUid() {
        return String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
    }

    @JavascriptInterface
    public String getPosition() {
        return String.valueOf(mPosition);
    }
}
