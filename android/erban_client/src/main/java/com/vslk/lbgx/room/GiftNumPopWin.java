package com.vslk.lbgx.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tongdaxing.erban.R;

public class GiftNumPopWin extends PopupWindow {

    EditText etNum;
    TextView okBtn;
    private View view;

    @SuppressLint("ClickableViewAccessibility")
    public GiftNumPopWin(Context mContext, IGiftNumListener listener) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.dialog_gift_num, null);
        etNum = view.findViewById(R.id.et_num);
        okBtn = view.findViewById(R.id.ok_btn);
        view.findViewById(R.id.close).setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(etNum);
            dismiss();
        });

        okBtn.setOnClickListener(v -> {
            String str = etNum.getText().toString();
            if (str.isEmpty()) {
                ToastUtils.showLong("请输入数量");
                return;
            }
            listener.onNum(Integer.parseInt(str));
            KeyboardUtils.hideSoftInput(etNum);
            dismiss();
        });
        // 设置按钮监听
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        this.view.setOnTouchListener((View v, MotionEvent event) -> {
            int height = view.findViewById(R.id.pop_layout).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    KeyboardUtils.hideSoftInput(etNum);
                    dismiss();
                }
            }
            return true;
        });
        this.setBackgroundDrawable(new BitmapDrawable());
        KeyboardUtils.showSoftInput(etNum);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.dialgBottomDialogAnimation);

        this.getBackground().setAlpha(0);
    }

    public interface IGiftNumListener {
        void onNum(int num);
    }


    public static void showPopFormBottom(View v, IGiftNumListener listener) {
        GiftNumPopWin takePhotoPopWin = new GiftNumPopWin(v.getContext(), listener);
        takePhotoPopWin.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}