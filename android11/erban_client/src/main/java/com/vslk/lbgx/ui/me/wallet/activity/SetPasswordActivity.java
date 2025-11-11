package com.vslk.lbgx.ui.me.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.login.activity.ForgetPswActivity;
import com.vslk.lbgx.ui.me.MePresenter;
import com.vslk.lbgx.ui.me.task.view.IMeView;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.UriProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/24
 * 描述        设置登录密码
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(MePresenter.class)
public class SetPasswordActivity extends BaseMvpActivity<IMeView, MePresenter> implements IMeView {
    @BindView(R.id.tv_current_pw_title)
    TextView tvCurrentPwTitle;
    @BindView(R.id.toolbar)
    AppToolBar mToolBar;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.oldPassword)
    TextInputLayout pwContent;
    @BindView(R.id.oldPw)
    EditText oldPassword;
    @BindView(R.id.newPw)
    EditText newPassword;
    @BindView(R.id.confirmNewPw)
    EditText confirmPassword;
    @BindView(R.id.btnSave)
    Button btnSave;

    private TextWatcherListener listener;
    private boolean isSetPassWord;

    public static void start(Context context, boolean isSetPassWord) {
        Intent intent = new Intent(context, SetPasswordActivity.class);
        intent.putExtra("isSetPassWord", isSetPassWord);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        isSetPassWord = getIntent().getBooleanExtra("isSetPassWord", false);
        ButterKnife.bind(this);
        initView();
        setListener();
    }

    private void initView() {
        if (!isSetPassWord) {
            tvCurrentPwTitle.setVisibility(View.GONE);
            pwContent.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        listener = new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (checkInput()) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }
        };
        oldPassword.addTextChangedListener(listener);
        newPassword.addTextChangedListener(listener);
        confirmPassword.addTextChangedListener(listener);

        btnSave.setOnClickListener(view -> {
            if (!verifierInput()) {
                return;
            }
            getDialogManager().showProgressDialog(this);
            String old = oldPassword.getText().toString();
            String newPwd = newPassword.getText().toString();
            String confirm = confirmPassword.getText().toString();
            if (isSetPassWord) {
                getMvpPresenter().modifyPassword(old, newPwd, confirm, UriProvider.modifyPwd());
            } else {
                getMvpPresenter().modifyPassword(old, newPwd, confirm, UriProvider.setPassWord());
            }
        });
        mToolBar.setOnBackBtnListener(view -> finish());
        forget.setOnClickListener(view -> {
            ForgetPswActivity.start(this);
            finish();
        });

    }

    private boolean checkInput() {
        if (isSetPassWord) {
            return oldPassword.getText() != null && oldPassword.getText().length() > 5 &&
                    newPassword.getText() != null && newPassword.getText().length() > 5 &&
                    confirmPassword.getText() != null && confirmPassword.getText().length() > 5;
        }
        return newPassword.getText() != null && newPassword.getText().length() > 5 &&
                confirmPassword.getText() != null && confirmPassword.getText().length() > 5;
    }

    private boolean verifierInput() {
        if (isSetPassWord) {
            if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                toast("新密码与确认密码不一致，请重新输入");
                return false;
            }
            if (newPassword.getText().toString().equals(oldPassword.getText().toString())) {
                toast("新密码与当前密码一致，请重新输入");
                return false;
            }
        } else {
            if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                toast("新密码与确认密码不一致，请重新输入");
                return false;
            }
        }
        return true;
    }

    @Override
    public void modifyPassword() {
        getDialogManager().dismissDialog();
        toast(isSetPassWord ? "修改登录密码成功" : "设置登录密码成功");
        finish();
    }

    @Override
    public void modifyPasswordFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }
}
