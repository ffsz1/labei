package com.vslk.lbgx.ui.widget;


import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tongdaxing.erban.R;

/**
 * <p> 底部tab导航 </p>
 * Created by Administrator on 2017/11/14.
 */
public class MainTabLayout extends LinearLayout implements View.OnClickListener {
    private MainGradientsTab mHomeTab, mAttentionTab, mMeTab;
    private MainRedPointTab mMsgTab;
    private int mLastPosition = -1;

    public MainTabLayout(Context context) {
        this(context, null);
    }

    public MainTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);
        inflate(context, R.layout.main_tab_layout, this);
//        setBackgroundResource(R.drawable.shape_fffafafa_corner_15dp);
        mHomeTab = (MainGradientsTab) findViewById(R.id.main_home_tab);
        mAttentionTab = (MainGradientsTab) findViewById(R.id.main_attention_tab);
        mMsgTab = (MainRedPointTab) findViewById(R.id.main_msg_tab);
        mMeTab = (MainGradientsTab) findViewById(R.id.main_me_tab);
        mHomeTab.select(true);
        mHomeTab.setOnClickListener(this);
        mAttentionTab.setOnClickListener(this);
        mMsgTab.setOnClickListener(this);
        mMeTab.setOnClickListener(this);
    }


    public MainTabLayout setHomeIcon(String text, int sid, int did) {
        mHomeTab.setIcon(text, sid, did);
        return this;
    }


    public MainTabLayout setAttentionIcon(String text, int sid, int did) {
        mAttentionTab.setIcon(text, sid, did);
        return this;
    }

    public MainTabLayout setMsgIcon(String text, int sid, int did) {
        mMsgTab.setIcon(text, sid, did);
        return this;
    }

    public MainTabLayout setMeIcon(String text, int sid, int did) {
        mMeTab.setIcon(text, sid, did);
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_home_tab:
                select(0);
                break;
            case R.id.main_attention_tab:
                select(1);
                break;
            case R.id.main_msg_tab:
                select(2);
                break;
            case R.id.main_me_tab:
                select(3);
                break;
        }
    }




    public void setMsgNum(int number) {
        mMsgTab.setNumber(number);
    }

    public void select(int position) {
        if (mLastPosition == position) return;
        mHomeTab.select(position == 0);
        mAttentionTab.select(position == 1);
        mMsgTab.select(position == 2);
        mMeTab.select(position == 3);

        if (mOnTabClickListener != null) {
            mOnTabClickListener.onTabClick(position);
        }
        mLastPosition = position;
    }

    private OnTabClickListener mOnTabClickListener;

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    public interface OnTabClickListener {
        void onTabClick(int position);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

    }
}
