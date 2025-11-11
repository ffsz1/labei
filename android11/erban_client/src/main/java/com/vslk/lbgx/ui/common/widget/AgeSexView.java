package com.vslk.lbgx.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;

/**
 * Created by zhouxiangfeng on 17/2/26.
 */

public class AgeSexView extends LinearLayout {

    private ImageView sexIv;
    private TextView ageTv;

    public AgeSexView(Context context) {
        super(context);
        init();
    }

    public AgeSexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AgeSexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
       View mView =  LayoutInflater.from(getContext()).inflate(R.layout.layout_zone_age_sex_view,this,true);
        sexIv = (ImageView)mView.findViewById(R.id.sex_iv);
        ageTv = (TextView)mView.findViewById(R.id.age_tv);
    }



    public AgeSexView setSexImg(int resId){
        sexIv.setImageResource(resId);
        return this;
    }


    public AgeSexView setAgeText(int age){
        ageTv.setText(String.valueOf(age));
        return this;
    }

}
