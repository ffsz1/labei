package com.vslk.lbgx.ui.me.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.pay.bean.ExchangeAwardInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCore;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.vslk.lbgx.ui.me.wallet.presenter.ExchangeGoldPresenter;
import com.vslk.lbgx.ui.me.wallet.view.IExchangeGoldView;
import com.vslk.lbgx.ui.me.withdraw.DiamondsCoreImpl;
import com.vslk.lbgx.ui.me.withdraw.IDiamondsCore;
import com.vslk.lbgx.ui.me.withdraw.IsAuthDialog;
import com.vslk.lbgx.ui.widget.dialog.ExchangeAwardsDialog;
import com.vslk.lbgx.ui.widget.dialog.ExchangeVerificationDialog;
import com.vslk.lbgx.utils.NumberFormatUtils;

import java.util.Locale;

import butterknife.BindView;

/**
 * @author MadisonRong
 * @date 09/01/2018
 */
@CreatePresenter(ExchangeGoldPresenter.class)
public class ExchangeGoldFragment extends BaseMvpFragment<IExchangeGoldView, ExchangeGoldPresenter> implements IExchangeGoldView, View.OnClickListener, TextWatcher {


    @BindView(R.id.et_exchange_gold_diamond_amount)
    EditText inputEditText;
    @BindView(R.id.tv_exchange_gold_diamond_balance)
    TextView diamondBalanceTextView;
    @BindView(R.id.tv_exchange_gold_result)
    TextView resultTextView;
    @BindView(R.id.tv_exchange_gold_gold_balance)
    TextView goldBalanceTextView;
    @BindView(R.id.btn_exchange_gold_confirm)
    DrawableTextView confirmButton;

    private TextView phoneTv;
    private TextView getCodeBtn;
    private CodeDownTimer mTimer;
    private UserInfo userInfo;
    private EditText authEt;
    private IDiamondsCore iDiamondsCore;

    private ExchangeVerificationDialog exchangeVerificationDialog;

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_exchange_gold;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        iDiamondsCore = DiamondsCoreImpl.getInstance().get();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMvpPresenter().refreshWalletInfo(false);
    }

    @Override
    public void initiate() {
        initListeners();
    }

    private void initListeners() {
        confirmButton.setOnClickListener(this);
        inputEditText.addTextChangedListener(this);
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetSmsCode() {
        if (userInfo != null) {
            phoneTv.setText("验证码已发送至您绑定的手机号"+ NumberFormatUtils.hideMiddleExtend(userInfo.getPhone()));
        }
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetSmsCodeFail(String error) {
        toast(error);
    }

    @Override
    public void onClick(View view) {
        //判断是否实名
        if (iDiamondsCore != null) iDiamondsCore.isAuthentication((it) -> {
            if (it) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.get_code_dialog,null,false);
                getDialogManager().showCustomViewDialog(view1);
                getCodeBtn = view1.findViewById(R.id.tv_get_code);
                phoneTv = view1.findViewById(R.id.text_show_tv);
                authEt = view1.findViewById(R.id.et_auth_code);

                getCodeBtn.setOnClickListener(view2 ->{
                    mTimer = new CodeDownTimer(getCodeBtn, 60000, 1000);
                    mTimer.start();
                    CoreManager.getCore(IWithdrawCore.class).getSmsCode(CoreManager.getCore(IAuthCore.class).getCurrentUid());
                });
                view1.findViewById(R.id.btn_cancel).setOnClickListener(view2 ->{
                    getDialogManager().dismissDialog();
                    return;
                });
                view1.findViewById(R.id.btn_ok).setOnClickListener(view2 ->{
                    if(authEt.getText().toString().isEmpty()){
                        toast("验证码为空，请重新输入");
                        return;
                    }
                    getDialogManager().dismissDialog();
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer.onFinish();
                    }

                    String str = inputEditText.getText().toString();
                    getMvpPresenter().confirmToExchangeGold(str,getContext(),authEt.getText().toString(),userInfo == null?"":userInfo.getPhone());
                });
            } else {
                IsAuthDialog isAuthDialog = new IsAuthDialog();
                isAuthDialog.show(getChildFragmentManager(), "isAuth");
            }
        });
    }

    @Override
    public void setupUserWalletBalance(WalletInfo walletInfo) {
        diamondBalanceTextView.setText(String.format(Locale.getDefault(), "%.2f", walletInfo.diamondNum));
        goldBalanceTextView.setText(String.format(Locale.getDefault(), "%.2f", walletInfo.goldNum));
    }

    @Override
    public void getUserWalletInfoFail(String error) {
        toast(error);
        diamondBalanceTextView.setText("0");
        goldBalanceTextView.setText("0");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (inputEditText.getText().toString().equals("0")) {
            inputEditText.setText("");
        }
        String str = inputEditText.getText().toString();
        getMvpPresenter().calculateResult(str);
    }

    @Override
    public void toastForError(int errorResId) {
        toast(errorResId);
    }

    @Override
    public void displayResult(String result) {
        resultTextView.setText(result);
    }

    @Override
    public void requestExchangeGold(long value) {
        getMvpPresenter().exchangeGold(String.valueOf(value),authEt.getText().toString(),userInfo.getPhone());
    }

    @Override
    public void requestExchangeGold(long value, String sms) {
        getMvpPresenter().exchangeGold(String.valueOf(value), sms);
    }

    @Override
    public void exchangeGold(WalletInfo walletInfo) {
        toast(R.string.exchange_gold_success);
        inputEditText.setText("");
        resultTextView.setText("0");
        if (exchangeVerificationDialog != null) {
            exchangeVerificationDialog.dismiss();
        }
    }

    @Override
    public void exchangeGoldFail(int code, String error) {
        //需要验证账号的
//        if (code == 401) {
//            if (exchangeVerificationDialog != null) {
//                exchangeVerificationDialog.dismiss();
//            }
//
//            String phone = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getPhone();
//            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
//                toast("请先绑定手机");
//                return;
//            }
//
//            exchangeVerificationDialog = ExchangeVerificationDialog.newInstance();
//            exchangeVerificationDialog.iOnSubmit = sms -> {
//                String str = inputEditText.getText().toString();
//                getMvpPresenter().confirmToExchangeGold(str, sms);
//            };
//            exchangeVerificationDialog.show(getChildFragmentManager(), null);
//            return;
//        }

        toast(error);
    }

    @Override
    public void showAward(ExchangeAwardInfo data) {
        String drawMsg = data.getDrawMsg();
        String drawUrl = data.getDrawUrl();
        if (TextUtils.isEmpty(drawMsg) || TextUtils.isEmpty(drawUrl)) {
            return;
        }
        ExchangeAwardsDialog exchangeAwardsDialog = ExchangeAwardsDialog.newInstance();
        exchangeAwardsDialog.setData(data);
        exchangeAwardsDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
