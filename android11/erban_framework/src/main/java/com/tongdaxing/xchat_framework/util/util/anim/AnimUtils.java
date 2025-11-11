package com.tongdaxing.xchat_framework.util.util.anim;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by lijun on 2014/11/13.
 */
public class AnimUtils {

    private static final long BG_ANIM_DURATION = 15000;

    public static Animator createTextColorAnim(final TextView textView, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer)animator.getAnimatedValue());
            }
        });

        return colorAnimation;
    }

    @TargetApi(21)
    private ObjectAnimator fade(final float alpha, View v) {
        final ObjectAnimator fade = ObjectAnimator.ofFloat(v, "alpha", alpha);
        fade.setInterpolator(AnimationUtils.loadInterpolator(v.getContext(),
                android.R.anim.accelerate_decelerate_interpolator));
        fade.setDuration(400);
        fade.start();
        return fade;
    }

    public static ValueAnimator buildRotationAnimator() {
        ValueAnimator bgAnimator = ValueAnimator.ofFloat(0f, 360f);
        bgAnimator.setDuration(BG_ANIM_DURATION); // miliseconds
        bgAnimator.setInterpolator(new LinearInterpolator());
        bgAnimator.setRepeatCount(ValueAnimator.INFINITE);
        return bgAnimator;
    }



}
