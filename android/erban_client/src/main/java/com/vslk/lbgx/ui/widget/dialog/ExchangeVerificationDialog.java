package com.vslk.lbgx.ui.widget.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ExchangeVerificationDialog extends BaseDialogFragment {


    @BindView(R.id.edt_exchange_verification)
    EditText edtExchangeVerification;
    @BindView(R.id.bu_exchange_verification_get_sms)
    Button buExchangeVerificationGetSms;
    @BindView(R.id.bu_exchange_verification_submit)
    Button buExchangeVerificationSubmit;
    Unbinder unbinder;
    private CountDownTimer countDownTimer;

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    public static ExchangeVerificationDialog newInstance() {
        ExchangeVerificationDialog exchangeAwardsDialog = new ExchangeVerificationDialog();
        return exchangeAwardsDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (countDownTimer != null)
            countDownTimer.cancel();
        CoreManager.removeClient(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = inflater.inflate(R.layout.dialog_exchange_verification, window.findViewById(android.R.id.content), false);

        unbinder = ButterKnife.bind(this, view);
        buExchangeVerificationGetSms.setOnClickListener(v -> {
            String phone = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getPhone();
            CoreManager.getCore(IAuthCore.class).requestSMSCode(phone, 3);
            buExchangeVerificationGetSms.setEnabled(false);
        });

        buExchangeVerificationSubmit.setOnClickListener(v -> {
            if (iOnSubmit != null)
                iOnSubmit.onSubmit(edtExchangeVerification.getText().toString().trim());
        });
        return view;
    }

    public IOnSubmit iOnSubmit;

    public interface IOnSubmit {
        void onSubmit(String sms);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsSuccess() {
        countDown();
        Toast.makeText(getContext(), "短信发送成功", Toast.LENGTH_SHORT).show();
    }

    private void countDown() {
        buExchangeVerificationGetSms.setBackgroundResource(R.drawable.shape_exchange_verification_waiting);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) (millisUntilFinished / 1000);
                if (buExchangeVerificationGetSms == null) {
                    return;
                }
                buExchangeVerificationGetSms.setText(time + "S");
            }

            @Override
            public void onFinish() {
                if (buExchangeVerificationGetSms == null) {
                    return;
                }
                buExchangeVerificationGetSms.setText("获取验证码");
                buExchangeVerificationGetSms.setBackgroundResource(R.drawable.shape_car_pay);
            }
        };
        countDownTimer.start();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsFail(String error) {
        buExchangeVerificationGetSms.setEnabled(true);
        Toast.makeText(getContext(), error + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
