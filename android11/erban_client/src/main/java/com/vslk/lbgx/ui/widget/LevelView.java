package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tongdaxing.erban.R;


/**
 * Created by Administrator on 2018/3/10.
 */

public class LevelView extends LinearLayout {

    ImageView mExperLevel;
    ImageView mCharmLevel;
    private Context mContext;

    /**
     * 解决聊天室公屏占位过多
     */
    public View seatView;

    public LevelView(Context context) {
        this(context, null);
    }

    public ImageView getExperLevel() {

        return mExperLevel;
    }

    public ImageView getCharmLevel() {
        return mCharmLevel;
    }

    public LevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View inflate = View.inflate(context, R.layout.level_view, this);
        mCharmLevel = inflate.findViewById(R.id.charm_level);
        mExperLevel = inflate.findViewById(R.id.exper_level);
        seatView =  inflate.findViewById(R.id.seat);

    }

    public void setExperLevel(int experLevel) {
        if (experLevel < 0) {
            mExperLevel.setVisibility(GONE);
            seatView.setVisibility(GONE);
            return;
        }
        if (experLevel > 50) {
            experLevel = 50;
        }
        mExperLevel.setVisibility(VISIBLE);

        int drawableId = getResources().getIdentifier("lv" + experLevel, "drawable", mContext.getPackageName());
        mExperLevel.setBackgroundResource(drawableId);
    }

    public void setCharmLevel(int charmLevel) {
        if (charmLevel < 0) {
           mCharmLevel.setVisibility(GONE);
           return;
        }

        if (charmLevel >= 50) {
            charmLevel = 50;
        }
        mCharmLevel.setVisibility(VISIBLE);
        int drawableId = getResources().getIdentifier("ml" + charmLevel, "drawable", mContext.getPackageName());

        mCharmLevel.setBackgroundResource(drawableId);
    }
}
