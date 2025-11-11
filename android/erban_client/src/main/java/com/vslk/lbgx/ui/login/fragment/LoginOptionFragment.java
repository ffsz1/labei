package com.vslk.lbgx.ui.login.fragment;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.tongdaxing.xchat_core.login.presenter.LoginOptionPresenter;
import com.tongdaxing.xchat_core.login.view.ILoginOptionView;
import com.tongdaxing.xchat_core.login.view.ILoginView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;

import butterknife.BindView;

/**
 * Function:
 * Author: Edward on 2019/6/4
 */
@CreatePresenter(LoginOptionPresenter.class)
public class LoginOptionFragment extends BaseMvpFragment<ILoginOptionView, LoginOptionPresenter> implements ILoginOptionView, View.OnClickListener {
    public static final String TAG = "LoginOptionFragment";
    private ILoginView iLoginView;
    @BindView(R.id.tv_phone_login)
    TextView tvPhoneLogin;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_self_protocol)
    TextView tvSelfProtocol;
    @BindView(R.id.tv_live_protocol)
    TextView tvLiveProtocol;
    @BindView(R.id.tv_wechat_login)
    TextView tvWechatLogin;
    @BindView(R.id.tv_qq_login)
    TextView tvQqLogin;
    @BindView(R.id.cb)
    CheckBox cb;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILoginView) {
            iLoginView = (ILoginView) context;
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_login_option;
    }

    @Override
    public void onFindViews() {
        tvQqLogin.setOnClickListener(this);
        tvPhoneLogin.setOnClickListener(this);
        tvWechatLogin.setOnClickListener(this);
        tvUserProtocol.setOnClickListener(this);
        tvSelfProtocol.setOnClickListener(this);
        tvLiveProtocol.setOnClickListener(this);
    }

    @Override
    public void initiate() {
    }

    @Override
    public void onSetListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone_login:
                if (iLoginView != null) {
                    iLoginView.openPhoneLoginPage(cb.isChecked());
                }
                break;
            case R.id.tv_user_protocol:
                if (iLoginView != null) {
                iLoginView.openProtocolPage();
            }
                break;
            case R.id.tv_self_protocol:
                if (iLoginView != null) {
                    iLoginView.openSelfProtocolPage();
                }
                break;
            case R.id.tv_live_protocol:
                if (iLoginView != null) {
                    iLoginView.openLiveProtocolPage();
                }
                break;
            case R.id.tv_qq_login:
                if (iLoginView != null) {
                    iLoginView.onQQLogin(cb.isChecked());
                }
                break;
            case R.id.tv_wechat_login:
                if (iLoginView != null) {
                    iLoginView.onWechatLogin(cb.isChecked());
                }
                break;
        }
    }
}
