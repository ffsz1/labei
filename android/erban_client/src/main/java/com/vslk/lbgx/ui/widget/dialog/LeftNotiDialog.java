package com.vslk.lbgx.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tongdaxing.erban.R;

/**
 * Created by zhouxiangfeng on 2017/5/13.
 */

public class LeftNotiDialog extends Dialog implements View.OnClickListener {

    /**
     * 上下文
     */
    private Context context;
    private WindowManager windowManager;
    private int width;
    private TextView tvTitle;
    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvCancel;
    private TextView tvNoti;

    public LeftNotiDialog(Context context, boolean cancelable,
                          OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // Auto-generated constructor stub
        this.context = context;
    }


    public LeftNotiDialog(Context context) {
        super(context, R.style.BottomSelectDialog);
        // Auto-generated constructor stub
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_left_noti_dialog);
        findViewById(R.id.ll_content).setOnClickListener(this);
        tvNoti = (TextView) findViewById(R.id.tv_noti);
        setDialogShowAttributes(context, this);// 设置Dialog显示参数，出入动画
    }

    public static void setDialogShowAttributes(Context context, Dialog dialog) {

        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获取窗口管理�?
        int mScreenWidth = winManager.getDefaultDisplay().getWidth();//获取屏幕宽度,将此宽度设为要显示的Dialog窗口的宽�?
        int mScreenHeight = winManager.getDefaultDisplay().getHeight();//获取屏幕宽度,将此宽度设为要显示的Dialog窗口的宽�?
        Window mWindow = dialog.getWindow();//获取Dialog窗口
        mWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams Params = mWindow.getAttributes();
        Params.windowAnimations = R.style.left_dialog_anim_style;//设置窗口出入动画
//        Params.width = mScreenWidth/3;
        Params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        Params.height = mScreenHeight;
        mWindow.setAttributes(Params);
        mWindow.setBackgroundDrawableResource(R.color.color_7F000000);
        mWindow.setGravity(Gravity.LEFT);
    }

    public void setNoti(String noti) {
        tvNoti.setText(noti);
    }


    @Override
    public void onClick(View v) {
        // Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_content:
                dismiss();
                break;

            default:
                break;
        }
    }


}
