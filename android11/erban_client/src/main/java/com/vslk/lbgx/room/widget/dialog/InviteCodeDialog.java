package com.vslk.lbgx.room.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * @author xiaoyu
 * @date 2017/12/13
 */

public class InviteCodeDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "InviteCodeDialog";

    private ImageView btnCancel;
    private EditText inviteCode;
    private TextView btnOk;

    public InviteCodeDialog(Context context) {
        super(context, R.style.ErbanUserInfoDialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
        setContentView(R.layout.layout_invite_code_dialog);

        btnCancel = findViewById(R.id.btn_cancel);
        inviteCode = findViewById(R.id.edit_code);
        btnOk = findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = ScreenUtil.getScreenWidth(getContext()) - ScreenUtil.dip2px(84);
            window.setWindowAnimations(R.style.ErbanCommonWindowAnimationStyle);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    //处理Unable to add window -- token android.os.BinderProxy@f9e7485 is not valid; is your activity running?
    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //not attached to window manager
    @Override
    public void dismiss() {
        //清空数据
        inviteCode.getText().clear();
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                saveInviteCode();
                break;
            default:
                break;
        }
    }

    private OnInviteCodeSaveListener listener;

    public void setOnInviteCodeSaveListener(OnInviteCodeSaveListener listener){
        this.listener = listener;
    }

    public interface OnInviteCodeSaveListener {
        void onSaved(String inviteCode);
    }

    private void saveInviteCode() {
        String code = inviteCode.getText().toString().trim();
        if (StringUtils.isEmpty(code)) {
            SingleToastUtil.showToast("请输入邀请码!");
            return;
        }
        dismiss();
        if (listener != null) {
            listener.onSaved(code);
        }
    }
}
