package com.vslk.lbgx.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by zhouxiangfeng on 2017/4/18.
 */

public class DesignUtils {

    /**
     * 显示控件
     * @param myView
     */
    private void show(final View myView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;
            // get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            myView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏控件
     * @param myView
     */
    private void hide(final View myView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;
            // get the initial radius for the clipping circle
            int initialRadius = myView.getWidth();
            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            myView.setVisibility(View.INVISIBLE);
        }
    }

    private void showAnim(Activity act, int type) {
        if(Build.VERSION.SDK_INT >= 21){
            switch (type) {
                case 1:
                    Explode explode = new Explode();
                    explode.setDuration(300L);
                    act.getWindow().setEnterTransition(explode);
                    break;
                case 2:
                    Slide slide = new Slide(Gravity.RIGHT);
                    slide.setDuration(300L);
                    act.getWindow().setEnterTransition(slide);
                    break;
                case 3:
                    Fade fade = new Fade();
                    fade.setDuration(300L);
                    act.getWindow().setEnterTransition(fade);
                    break;
            }
        }
    }
}
