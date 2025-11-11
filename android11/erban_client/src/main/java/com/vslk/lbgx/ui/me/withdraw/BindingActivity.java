package com.vslk.lbgx.ui.me.withdraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindingActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rg_gift_indicator)
    TextView rgGiftIndicator;
    @BindView(R.id.rb_zfb)
    CheckBox rbZfb;
    @BindView(R.id.et_zfb_name)
    EditText etZfbName;
    @BindView(R.id.et_zfb_num)
    EditText etZfbNum;
    @BindView(R.id.rb_yhk)
    CheckBox rbYhk;
    @BindView(R.id.et_yhk_name)
    EditText etYhkName;
    @BindView(R.id.et_yhk_num)
    EditText etYhkNum;
    @BindView(R.id.ok_btn)
    TextView okBtn;
    @BindView(R.id.ll_zfb)
    LinearLayout llZfb;
    @BindView(R.id.ll_yhk)
    LinearLayout llYhk;

    private int type = 0;
    private String selectName = "";
    private String selectNum = "";
    private IDiamondsCore iDiamondsCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type", 0);
            intent.putExtra("name", selectName);
            intent.putExtra("num", selectNum);
            setResult(10020, intent);
            finish();
        });
        okBtn.setOnClickListener(v -> {
            if (type != 0) {
                if (type == 1) {
                    selectName = etZfbName.getText().toString();
                    selectNum = etZfbNum.getText().toString();
                } else {
                    selectName = etYhkName.getText().toString();
                    selectNum = etYhkNum.getText().toString();
                }
                if (isNull(selectName, selectNum)) {
                    Logger.e(selectName + "," + selectNum);
                    Intent intent = new Intent();
                    intent.putExtra("type", type);
                    intent.putExtra("name", selectName);
                    intent.putExtra("num", selectNum);
                    setResult(10020, intent);
                    finish();
                } else {
                    SingleToastUtil.showToast(type == 1 ? "填写支付宝信息" : "填写银行卡信息");
                }
            }
        });

        iDiamondsCore = DiamondsCoreImpl.getInstance().get();
        iDiamondsCore.getFinancialAccount((it) -> {
            if (it.getAlipayAccountName() != null) etZfbName.setText(it.getAlipayAccountName());
            if (it.getAlipayAccount() != null) etZfbNum.setText(it.getAlipayAccount());
            if (it.getBankCardName() != null) etYhkName.setText(it.getBankCardName());
            if (it.getBankCard() != null) etYhkNum.setText(it.getBankCard());
        });
    }

    @OnClick({R.id.ll_zfb, R.id.ll_yhk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_zfb:
                selectType(1);
                break;
            case R.id.ll_yhk:
                selectType(2);
                break;
        }
    }

    private void selectType(int index) {
        type = index;
        switch (index) {
            case 1:
                if (rbZfb.isChecked()) {
                    rbZfb.setChecked(false);
                } else {
                    rbZfb.setChecked(true);
                }
                rbYhk.setChecked(false);
                break;
            case 2:
                if (rbYhk.isChecked()) {
                    rbYhk.setChecked(false);
                } else {
                    rbYhk.setChecked(true);
                }
                rbZfb.setChecked(false);
                break;
        }
        okBtn.setBackgroundResource((rbYhk.isChecked() || rbZfb.isChecked()) ? R.drawable.bg_binding_btn :
                R.drawable.bg_binding_btn_off);
    }


    private boolean isNull(String name, String num) {
        return !name.isEmpty() && !num.isEmpty();
    }

    private void hieInput() {
        InputMethodManager manager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.hideSoftInputFromWindow(rgGiftIndicator.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iDiamondsCore != null) iDiamondsCore = null;
    }
}
