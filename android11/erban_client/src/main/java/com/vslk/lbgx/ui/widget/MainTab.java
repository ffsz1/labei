package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.vslk.lbgx.ui.home.GradientsTool;

/**
 * <p> main tab 控件 </p>
 * Created by Administrator on 2017/11/14.
 */
public class MainTab extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_COLOR = Color.parseColor("#333333");
    private int mTabIcon, mTabIconSelect;
    //    private int mTabtextColor, mTabTextSelectColor;
    private String mTabText;
    private Context mContext;

    public MainTab(Context context) {
        this(context, null);
    }

    public MainTab(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        setGravity(Gravity.CENTER);
        setCompoundDrawablePadding(ScreenUtil.dip2px(1));

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainTab);
        mTabIcon = typedArray.getResourceId(R.styleable.MainTab_tab_icon, R.mipmap.ic_main_tab_home);
        mTabIconSelect = typedArray.getResourceId(R.styleable.MainTab_tab_icon_select, R.mipmap.ic_main_tab_home_pressed);
//        mTabtextColor = typedArray.getColor(R.styleable.MainTab_tab_text_color, DEFAULT_COLOR);
//        mTabTextSelectColor = typedArray.getColor(R.styleable.MainTab_tab_text_color_select, DEFAULT_COLOR);
//        mTabText = typedArray.getString(R.styleable.MainTab_tab_text);
        typedArray.recycle();

        select(false);
//        setText(mTabText);
    }

    public void setIcon(int iconResId) {
        Drawable mainTabHome = ContextCompat.getDrawable(mContext, iconResId);
        //必须加上这句，否则不显示
        mainTabHome.setBounds(0, 0, mainTabHome.getMinimumWidth(), mainTabHome.getIntrinsicHeight());
        setCompoundDrawables(null, mainTabHome, null, null);
    }

    public void setIcon(String text, int sid, int did) {
        mTabIcon = did;
        mTabIconSelect = sid;
        setText(text);
    }

    public void select(boolean select) {
        if (select) {
            GradientsTool.setGradients((TextView) this, "#60A6FF", "#E27CFF");
        } else {
            GradientsTool.setGradients((TextView) this, "#333333", "#333333");
        }
//        setTextColor(select ? mTabTextSelectColor : mTabtextColor);
        setIcon(select ? mTabIconSelect : mTabIcon);
    }
}
