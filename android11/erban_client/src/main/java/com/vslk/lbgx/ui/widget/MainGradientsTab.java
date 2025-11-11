package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.vslk.lbgx.ui.home.GradientsTool;

/**
 * <p> main tab 有消息个数 控件  (
 * </p>
 * Created by Administrator on 2017/11/14.
 */
public class MainGradientsTab extends RelativeLayout {
    private ImageView ivIcon;
    private TextView tvText;
    private int mTabIconSelect, mTabIcon;

    public MainGradientsTab(@NonNull Context context) {
        this(context, null);
    }

    public MainGradientsTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainGradientsTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.maint_tab_gradients_layout, this);
        ivIcon = findViewById(R.id.iv_icon);
        tvText = findViewById(R.id.tv_text);
    }

    public void setIcon(String text, int sid, int did) {
        mTabIconSelect = sid;
        mTabIcon = did;
        tvText.setText(text);
    }

    public void select(boolean select) {
        if (select) {
            GradientsTool.setGradients(tvText, "#FF81A4", "#FF81A4");
        } else {
            GradientsTool.setGradients(tvText, "#979797", "#979797");
        }
        ivIcon.setImageResource(select ? mTabIconSelect : mTabIcon);
    }
}
