package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongdaxing.erban.R;

/**
 * <p> 默认工具toolbar 界面  </p>
 *
 * @author Administrator
 * @date 2017/11/29
 */
public class DefaultToolBar extends Toolbar implements View.OnClickListener {
    private TextView mTvCenterTitle;
    private TextView mTvRightText;

    public DefaultToolBar(Context context) {
        this(context, null);
    }

    public DefaultToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.default_toolbar_layout, this);
        //ThemeOverlay.AppCompat.Dark.ActionBar

        MarginLayoutParams layoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelOffset(R.dimen.common_toolbar_height));
        setLayoutParams(layoutParams);
        setContentInsetsAbsolute(0, 0);
        setContentInsetsRelative(0, 0);
        setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        setPopupTheme(R.style.AppTheme_PopupOverlay);
        setId(R.id.toolbar);
        mTvCenterTitle = (TextView) findViewById(R.id.tv_center_title);
        mTvRightText = (TextView) findViewById(R.id.tv_right_text);


        mTvRightText.setVisibility(GONE);
        mTvRightText.setOnClickListener(this);
    }

    public void setCenterTitle(int resId) {
        mTvCenterTitle.setText(resId);
    }

    public void setCenterTitle(CharSequence title) {
        mTvCenterTitle.setText(title);
    }

    public void setRightText(CharSequence rightText) {
        mTvRightText.setVisibility(TextUtils.isEmpty(rightText) ? GONE : VISIBLE);
        mTvRightText.setText(rightText);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:

                break;
            case R.id.tv_right_text:
                if (mOnRightTextClickListener != null) {
                    mOnRightTextClickListener.onRightTextClick();
                }
                break;
            default:
        }
    }

    private OnRightTextClickListener mOnRightTextClickListener;

    public void setOnRightTextClickListener(OnRightTextClickListener onRightTextClickListener) {
        mOnRightTextClickListener = onRightTextClickListener;
    }

    public interface OnRightTextClickListener {
        void onRightTextClick();
    }

}
