
package com.vslk.lbgx.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tongdaxing.erban.R;


/**
 * 描述:Loading加载控件
 *
 * @author zhengsun
 * @since 2014年8月28日 下午4:13:37
 */
public class LoadingImageView extends ImageView {

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoadingImageView(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.comm_loading);
        startAnimation(hyperspaceJumpAnimation);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            clearAnimation();
            return;
        }
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.comm_loading);
        startAnimation(hyperspaceJumpAnimation);
    }
}
