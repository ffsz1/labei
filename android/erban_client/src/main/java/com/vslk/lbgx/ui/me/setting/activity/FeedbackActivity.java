package com.vslk.lbgx.ui.me.setting.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.me.MePresenter;
import com.vslk.lbgx.ui.me.task.view.IMeView;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;

@CreatePresenter(MePresenter.class)
public class FeedbackActivity extends BaseMvpActivity<IMeView, MePresenter> implements IMeView {

    private EditText edtContent;
    private EditText edtContact;
    private AppToolBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
        initData();
        SetListener();
    }

    private void SetListener() {
        titleBar.setOnBackBtnListener(view -> finish());
        titleBar.setOnRightBtnClickListener(view -> {
            String etContent = edtContent.getText().toString().trim();
            if (StringUtil.isEmpty(etContent)) {
                toast("请输入您要反馈的内容");
                return;
            }
            String etContact = edtContact.getText().toString().trim();
            if (StringUtil.isEmpty(etContact)) {
                toast("请输入您的微信或QQ号码");
                return;
            }
            getMvpPresenter().commitFeedback(etContent, etContact);
        });
    }

    private void initData() {

    }

    private void initView() {
        edtContent = (EditText) findViewById(R.id.edt_content);
        edtContact = (EditText) findViewById(R.id.edt_contact);
        titleBar = (AppToolBar) findViewById(R.id.toolbar);
    }

    @Override
    public void commitFeedback() {
        getDialogManager().showProgressDialog(FeedbackActivity.this, "正在上传请稍后...");
        toast("提交成功");
        finish();
    }

    @Override
    public void commitFeedbackFail(String errorMsg) {
        toast(errorMsg);
    }
}
