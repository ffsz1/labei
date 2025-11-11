package com.vslk.lbgx.ui.me.setting.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dell
 */
public class AboutActivity extends BaseActivity{

    TextView versinos;
    AppToolBar mToolBar;
    private ClipboardManager mClipboardManager;
    private ClipData clipData;
    @BindView(R.id.user_self_rule)
    TextView userRuleTv;
    @BindView(R.id.self_rule)
    TextView selfRuleTv;
    @BindView(R.id.me_item_about)
    TextView liveRuleTv;
    @BindView(R.id.kefu_number_tv)
    TextView kefuNumTv;
    @BindView(R.id.common_num_tv)
    TextView commonNumTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        mClipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        userRuleTv.setOnClickListener(this);
        selfRuleTv.setOnClickListener(this);
        liveRuleTv.setOnClickListener(this);
        kefuNumTv.setOnClickListener(this);
        commonNumTv.setOnClickListener(this);

    }

    private void initData() {
        versinos.setText(String.format(Locale.getDefault(), getString(R.string.about_version_name),
                BasicConfig.getLocalVersionName(getApplication())));
    }

    private void initView() {
        versinos = (TextView) findViewById(R.id.versions);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);

        commonNumTv.setText("拉贝星球("+getString(R.string.txt_common_num)+")");
        back(mToolBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_self_rule://用户隐私协议
                CommonWebViewActivity.start(this, WebUrl.USER_AGREEMENT);
                break;
            case R.id.self_rule://隐私协议
                CommonWebViewActivity.start(this, WebUrl.USER_POLICY);
                break;
            case R.id.me_item_about://直播协议
                CommonWebViewActivity.start(this, WebUrl.LIVE_RULE);
                break;
            case R.id.kefu_number_tv://复制客服账号
                clipData = ClipData.newPlainText("微信客服复制成功!", String.valueOf(kefuNumTv.getText()));
                mClipboardManager.setPrimaryClip(clipData);
                SingleToastUtil.showToast("微信客服复制成功!");
                break;
            case R.id.common_num_tv://复制微信公众号
                clipData = ClipData.newPlainText("微信公众号复制成功!",  getString(R.string.txt_common_num));
                mClipboardManager.setPrimaryClip(clipData);
                SingleToastUtil.showToast("微信公众号复制成功!");
                break;
        }
    }
}
