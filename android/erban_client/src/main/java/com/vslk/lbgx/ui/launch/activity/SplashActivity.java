package com.vslk.lbgx.ui.launch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.hncxco.safetychecker.SafetyChecker;
import com.hncxco.safetychecker.bean.SafetyCheckResultBean;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.model.safetychecker.SafetyCheckerModel;
import com.vslk.lbgx.ui.MainActivity;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.initial.IInitView;
import com.tongdaxing.xchat_core.initial.InitInfo;
import com.tongdaxing.xchat_core.initial.InitPresenter;
import com.tongdaxing.xchat_core.utils.ThreadUtil;

/**
 * @author xiaoyu
 * @date 2017/12/30
 */
@CreatePresenter(InitPresenter.class)
public class SplashActivity extends BaseMvpActivity<IInitView, InitPresenter> implements IInitView, View.OnClickListener {

    private ImageView ivActivity;
    private InitInfo mLocalSplashVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStatusBar();
        ivActivity = (ImageView) findViewById(R.id.iv_activity);
        ivActivity.setOnClickListener(this);
        setSwipeBackEnable(false);
        if (savedInstanceState != null) {
            // 从堆栈恢复，不再重复解析之前的intent
            setIntent(new Intent());
        }
        initiate();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    protected void initiate() {
        showSplash();
        getMvpPresenter().init();
        animation();
        safetyCheck();
    }

    private void showSplash() {
        // 不过期的，并且已经下载出来图片的闪屏页数据
        mLocalSplashVo = getMvpPresenter().getLocalSplashVo();
        if (mLocalSplashVo != null && mLocalSplashVo.getSplashVo() != null
                && !TextUtils.isEmpty(mLocalSplashVo.getSplashVo().getPict())) {
            GlideApp.with(this)
                    .load(mLocalSplashVo.getSplashVo().getPict())
                    .into(ivActivity);
        } else {
            // 其他情况显示的是默认的图片
//            ivActivity.setImageResource(R.drawable.icon_splash);
        }
    }

    private boolean needJump = false;

    private void animation() {
        ivActivity.setAlpha(0.4F);
        ivActivity.animate().alpha(1).setDuration(1000).setStartDelay(0).start();
        ivActivity.animate().setDuration(2000).withEndAction(() -> {
            if (needJump) {
                return;
            }
            MainActivity.start(SplashActivity.this);
            finish();
        }).setStartDelay(1000).start();
    }

    /**
     * 安全检测
     */
    private void safetyCheck() {
        ThreadUtil.getThreadPool().execute(() -> {
            SafetyCheckResultBean checkResult = SafetyChecker.getInstance().check(this.getApplicationContext());
            //不安全的，上报
            if (checkResult.getCheckStatus() != 0) {
                ThreadUtil.runOnUiThread(() -> {
                    SafetyCheckerModel safetyCheckerModel = new SafetyCheckerModel();
                    safetyCheckerModel.reportSafetyCheckResult(checkResult);
                });
            }
        });
    }

    @Override
    public void onInitSuccess(InitInfo data) {
        // 底层已经进行数据处理完成
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_activity) {
            if (mLocalSplashVo == null || mLocalSplashVo.getSplashVo() == null) {
                return;
            }
            String link = mLocalSplashVo.getSplashVo().getLink();
            int type = mLocalSplashVo.getSplashVo().getType();
            if (TextUtils.isEmpty(link) || type == 0) {
                return;
            }
            needJump = true;
            Intent intent = new Intent();
            intent.putExtra("url", link);
            intent.putExtra("type", type);
            MainActivity.start(this, intent);
        }
    }

    @Override
    protected void onDestroy() {
        if (ivActivity != null) {
            ivActivity.clearAnimation();
        }
        super.onDestroy();
    }
}
