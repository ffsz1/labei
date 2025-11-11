package com.vslk.lbgx.ui.home;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.tongdaxing.erban.R;

import butterknife.ButterKnife;


public class GenderSelectWindow extends PopupWindow {
    private OnGenderSelectListener onGenderSelectListener;
    private Context context;

    public GenderSelectWindow(Context context, OnGenderSelectListener onGenderSelectListener) {
        super(context);
        this.context = context;
        this.onGenderSelectListener = onGenderSelectListener;
        init();
    }

    private void init() {
        setFocusable(true);
        setOutsideTouchable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_gender_select, null);
        Drawable backgroundDrawable = new ColorDrawable(0x00000000);//默认为透明
        setBackgroundDrawable(backgroundDrawable);
        setContentView(view);
        ButterKnife.bind(view);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gender = 0;
                switch (v.getId()) {
                    case R.id.tv_girl:
                        gender = 2;
                        break;
                    case R.id.tv_man:
                        gender = 1;
                        break;
                    case R.id.tv_all:
                        gender = 0;
                        break;
                }
                onGenderSelectListener.onGenderSelect(gender);
                dismiss();
            }
        };
        view.findViewById(R.id.tv_all).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_man).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_girl).setOnClickListener(onClickListener);
    }

    public interface OnGenderSelectListener {
        void onGenderSelect(int gender);
    }
}
