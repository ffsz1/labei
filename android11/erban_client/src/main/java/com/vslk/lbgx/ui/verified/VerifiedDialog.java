package com.vslk.lbgx.ui.verified;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * 文件描述：实名认证提示弹框
 * 三个不同的位置：创建房间1、房间内发言2、大厅内发言3
 * 三个状态：实名认证 1 、绑定手机2 和 审核期-1
 *
 * @auther：zwk
 * @data：2019/2/19
 */
public class VerifiedDialog extends BaseDialogFragment implements View.OnClickListener {
    private TextView tvTitle;
    private TextView tvVerify, tvCancle;
    private String title = "";
    private int limitType = -1;
//    private int limitPosition = 0;

    public static VerifiedDialog newInstance(String title, int limitType) {
        VerifiedDialog verified = new VerifiedDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("limitType", limitType);
//        bundle.putInt("limitPosition", limitPosition);
        verified.setArguments(bundle);
        return verified;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title", "很抱歉审核期内暂时无法使用该功能");
            limitType = bundle.getInt("limitType", -1);
//            limitPosition = bundle.getInt("limitPosition", 1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_verified, container, false);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
            setCancelable(false);
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_verify_title);
        tvVerify = view.findViewById(R.id.tv_verify);
        tvCancle = view.findViewById(R.id.tv_verify_cancel);
        tvVerify.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
    }

    private void initData() {
        if (limitType == IMError.USER_REAL_NAME_NEED_PHONE) {
            tvVerify.setText("去绑定");
        } else if (limitType == IMError.USER_REAL_NAME_NEED_VERIFIED) {
            tvVerify.setText("去认证");
        } else {
            tvCancle.setVisibility(View.GONE);
            tvVerify.setText("关闭");
        }
        if (StringUtils.isNotEmpty(title)) {
            tvTitle.setText(title);
        }
//        if (getDialog() != null) {
//            getDialog().setOnKeyListener(this);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_verify:
                if (limitType != -1) {
                    if (getContext() != null) {
                        if (limitType == IMError.USER_REAL_NAME_NEED_PHONE) {
                            BinderPhoneActivity.start(getContext());
                        } else if (limitType == IMError.USER_REAL_NAME_NEED_VERIFIED) {
                            CommonWebViewActivity.start(getContext(), WebUrl.VERIFIED_REAL_NAME);
                        }
                    }
                }
                dismiss();
                break;
            case R.id.tv_verify_cancel:
                dismiss();
                break;
        }
    }
}
