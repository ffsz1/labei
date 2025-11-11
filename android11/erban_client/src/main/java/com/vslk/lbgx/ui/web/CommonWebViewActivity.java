package com.vslk.lbgx.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.hncxco.library_ui.widget.AppToolBar;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.common.ICommonClient;
import com.tongdaxing.xchat_core.count.IChargeClient;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.file.IFileCoreClient;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.WebViewInfo;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.DisplayUtils;
import com.tongdaxing.xchat_framework.util.util.file.JXFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;

import static com.tongdaxing.xchat_core.WebUrl.USER_AGENT;


/**
 * @author dell
 */
public class CommonWebViewActivity extends TakePhotoActivity implements ShareDialog.OnShareDialogItemClick {
    private static final String CAMERA_PREFIX = "picture_";
    private AppToolBar mToolBar;
    private WebView webView;
    private ProgressBar mProgressBar;
    private WebChromeClient wvcc;
    private WebViewInfo webViewInfo = null;
    private String url;

    private static final String POSITION = "position";
    private int mPosition;
    private int mProgress;

    private Handler mHandler = new Handler();

    @Override
    public void takeSuccess(TResult result) {
        getDialogManager().showProgressDialog(CommonWebViewActivity.this, "请稍后");
        CoreManager.getCore(IFileCore.class).upload(new File(result.getImage().getCompressPath()));
    }

    @Override
    public void takeFail(TResult result, String msg) {
//        toast(msg);
        if (jsInterface != null) {
            jsInterface.onImageChooserResult("");
        }
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUpload(String url) {
        getDialogManager().dismissDialog();
        if (jsInterface != null) {
            jsInterface.onImageChooserResult(url);
        }
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadFail() {
        toast("上传失败");
        getDialogManager().dismissDialog();
        if (jsInterface != null) {
            jsInterface.onImageChooserResult("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mHandler.post(() -> webView.evaluateJavascript("onStart()", value -> {

            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePhoto() {
        String mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
        File cameraOutFile = JXFileUtils.getTempFile(CommonWebViewActivity.this, mCameraCapturingName);
        if (!cameraOutFile.getParentFile().exists()) {
            cameraOutFile.getParentFile().mkdirs();
        }
        Uri uri = Uri.fromFile(cameraOutFile);
        getTakePhoto().onPickFromCapture(uri);
    }

    public void showImageChooser() {
        //图片压缩配置 暂时写死 长或宽不超过当前屏幕的长或宽、图片大小不超过2M
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxPixel(DisplayUtils.getScreenHeight(this)).setMaxSize(2048 * 1024).create();
        getTakePhoto().onEnableCompress(compressConfig, true);

        ButtonItem buttonItem = new ButtonItem("拍照上传", () -> checkPermission(this::takePhoto, R.string.ask_camera, android.Manifest.permission.CAMERA)); //授权检查
        ButtonItem buttonItem1 = new ButtonItem("本地相册", () -> getTakePhoto().onPickFromGallery());
        List<ButtonItem> buttonItems = new ArrayList<>();
        buttonItems.add(buttonItem);
        buttonItems.add(buttonItem1);
        getDialogManager().showCommonPopupDialog(buttonItems, "取消", false);
    }

    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mProgress < 96) {
                mProgress += 3;
                mProgressBar.setProgress(mProgress);
                mHandler.postDelayed(mProgressRunnable, 10);
            }
        }
    };

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, CommonWebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * 排行榜专用
     */
    public static void start(Context context, String url, int position) {
        Intent intent = new Intent(context, CommonWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra(POSITION, position);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web_view);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        mPosition = intent.getIntExtra(POSITION, 0) + 1;
        initView();
        setListener();
        ShowWebView(url);
    }

    private void setListener() {
        mToolBar.setOnBackBtnListener(view -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        });
        mToolBar.setOnRightBtnClickListener(view -> {
            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.setOnShareDialogItemClick(this);
            shareDialog.show();
        });
    }


    private void initView() {
        webView = (WebView) findViewById(R.id.webview);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private JSInterface jsInterface;

    @SuppressLint("SetJavaScriptEnabled")
    private void initData() {
        mHandler.post(mProgressRunnable);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        jsInterface = new JSInterface(webView, this);
        jsInterface.setPosition(mPosition);
        webView.addJavascriptInterface(jsInterface, "androidJsObj");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error); //一定要去掉
                // handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
                // handleMessage(Message msg);// 进行其他处理
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setProgress(100);
                mProgressBar.setVisibility(View.GONE);

                try {
                    webView.evaluateJavascript("shareInfo()", value -> {
                        com.tongdaxing.xchat_framework.util.util.LogUtil.d("onReceiveValue11", webViewInfo + "");
                        if (!StringUtil.isEmpty(value) || !value.equals("null")) {
                            String jsonData = value.replace("\\", "");
                            if (jsonData.indexOf("\"") == 0) {
                                //去掉第一个 "
                                jsonData = jsonData.substring(1, jsonData.length());
                            }
                            if (jsonData.lastIndexOf("\"") == (jsonData.length() - 1)) {
                                jsonData = jsonData.substring(0, jsonData.length() - 1);
                            }
                            try {
                                Gson gson = new Gson();
                                webViewInfo = gson.fromJson(jsonData, WebViewInfo.class);
                                com.tongdaxing.xchat_framework.util.util.LogUtil.d("onReceiveValue",
                                        webViewInfo + "");
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    com.tongdaxing.xchat_framework.util.util.LogUtil.d("onReceiveValue11Exception",
                            webViewInfo + "   " + e.toString());
                }
                super.onPageFinished(view, url);
            }
        });
        //获取webviewtitle作为titlebar的title
        wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolBar.setTitle(title);

            }
        };
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);
        // 设置Webview的user-agent
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + USER_AGENT);
    }

    public void ShowWebView(String url) {
        webView.loadUrl(url);
        initData();
    }

    //调用系统按键返回上一层
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return true;
    }

    @Override
    public void onSharePlatformClick(Platform platform) {
        if (webViewInfo != null) {
            CoreManager.getCore(IShareCore.class).shareH5(webViewInfo, platform);
        }
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareWebView() {
        toast("分享成功");
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareWebViewError() {
        toast("分享失败，请重试");
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareWebViewCanle() {
        toast("取消分享");
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareViewRedError(String error) {
        toast(error);
    }

    @CoreEvent(coreClientClass = ICommonClient.class)
    public void onRecieveNeedRefreshWebView() {
        if (!StringUtil.isEmpty(url)) {
            ShowWebView(url);
        }
    }

    @CoreEvent(coreClientClass = IChargeClient.class)
    public void chargeAction(String action) {
        LogUtil.d("chargeAction", "sdad");
        try {
            mHandler.post(() -> webView.evaluateJavascript("getStatus()", value -> {

            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AppToolBar getToolbar() {
        return mToolBar;
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mProgressRunnable = null;
            mHandler = null;
        }
        super.onDestroy();
    }
}
