package com.vslk.lbgx.ui.me.withdraw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;

/**
 * 文件描述：
 *
 * @auther：zwk
 * @data：2019/2/15
 */
public class WithdrawWayDialog extends BottomSheetDialog implements View.OnClickListener {

    private RelativeLayout rlWx;
    private LinearLayout rlAli;
    private WithdrawInfo withdrawInfo;
    private Context context;
    private OnWithdrawWayChangeListener onWithdrawWayChangeListener;

    public WithdrawWayDialog(WithdrawInfo withdrawInfo, @NonNull Context context) {
        super(context, R.style.WithDrawWayBottomSheetDialog);
        this.context = context;
        this.withdrawInfo = withdrawInfo;
    }

    public static WithdrawWayDialog newInstance(WithdrawInfo withdrawInfo, Context context) {
        return new WithdrawWayDialog(withdrawInfo, context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_style_layout);
        setCanceledOnTouchOutside(true);

        FrameLayout bottomSheet = findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight((int) context.getResources().getDimension(R.dimen.dialog_payment_height) +
                    (Utils.hasSoftKeys(context) ? Utils.getNavigationBarHeight(context) : 0));
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setWindowAnimations(R.style.ErbanCommonWindowAnimationStyle);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        findViews();
    }

    private void findViews() {
        rlWx = findViewById(R.id.rl_weichat_pay_way);
        rlAli = findViewById(R.id.rl_alipay_way);
        ImageView ivWXLogo = findViewById(R.id.iv_weichat_pay_logo);
        ImageView ivAliLogo = findViewById(R.id.iv_alipay_logo);
//        DrawableTextView dtvWx = findViewById(R.id.dtv_weichat_pay_state);
        DrawableTextView dtvAli = findViewById(R.id.dtv_alipay_state);
        Button ivClose = findViewById(R.id.iv_dialog_close);
        /*if (withdrawInfo != null) {
            if (withdrawInfo.hasAlipay()) {
                ivAliLogo.setSelected(true);
                dtvAli.setSelected(true);
                dtvAli.setText("修改信息");
            }
            if (withdrawInfo.hasWx()) {
                ivWXLogo.setSelected(true);
                dtvWx.setSelected(true);
                dtvWx.setText("修改信息");
            }
            //优先以后端状态为主
            if (withdrawInfo.payment == 2) {
                //有支付宝账号
                if (ivAliLogo.isSelected()) {
                    rlAli.setSelected(true);
                } else {//默认微信
                    //有微信账号改变状态，否则默认
                    if (withdrawInfo.hasWx()) {
                        rlWx.setSelected(true);
                    }
                }
            } else {
                //有微信账号改变状态，否则默认
                if (withdrawInfo.hasWx()) {
                    rlWx.setSelected(true);
                } else {
                    //有支付宝账号
                    if (ivAliLogo.isSelected()) {
                        rlAli.setSelected(true);
                    }
                }
            }
        }*/
        rlWx.setOnClickListener(this);
        rlAli.setOnClickListener(this);
        ivClose.setOnClickListener(this);
//        dtvWx.setOnClickListener(this);
        dtvAli.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.rl_weichat_pay_way:
                if (withdrawInfo != null && withdrawInfo.hasWx()) {
                    if (withdrawInfo.payment != 1) {
                        rlWx.setSelected(true);
                        withdrawInfo.payment = 1;
                        if (onWithdrawWayChangeListener != null) {
                            onWithdrawWayChangeListener.onChangeListener(withdrawInfo);
                        }
                    }
                    dismiss();
                }
                break;
            case R.id.rl_alipay_way:
                if (withdrawInfo != null && withdrawInfo.hasAlipay()) {
                    if (withdrawInfo.payment != 2) {
                        rlAli.setSelected(true);
                        withdrawInfo.payment = 2;
                        if (onWithdrawWayChangeListener != null) {
                            onWithdrawWayChangeListener.onChangeListener(withdrawInfo);
                        }
                    }
                    dismiss();
                }
                break;*/
            case R.id.rl_weichat_pay_way:
                if (withdrawInfo != null) {
//                    Log.e("withdrawInfo.hasWx", "onClick: " + withdrawInfo.hasWx);
                    if (!withdrawInfo.hasWx) {
                        BinderWeixinPayActivity.start(getContext(), withdrawInfo);
                        dismiss();
                    } else {
                        dismiss();
                    }
                }
                break;
            case R.id.dtv_alipay_state:
                if (withdrawInfo != null && context != null) {
                    //跳转绑定手机号码，绑定成功以后显示bindersucceed
                    BinderAlipayActivity.start(context);
                    dismiss();
                }
                break;
            case R.id.iv_dialog_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setOnWithdrawWayChangeListener(OnWithdrawWayChangeListener onWithdrawWayChangeListener) {
        this.onWithdrawWayChangeListener = onWithdrawWayChangeListener;
    }

    public interface OnWithdrawWayChangeListener {
        void onChangeListener(WithdrawInfo withdrawInfo);
    }
}
