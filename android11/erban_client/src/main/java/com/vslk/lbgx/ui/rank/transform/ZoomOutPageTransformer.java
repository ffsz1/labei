package com.vslk.lbgx.ui.rank.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/3
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    /**
     * 自由控制缩放比例
     */
    private static final float MAX_SCALE = 1f;
    /**
     * 0.85f
     */
    private static final float MIN_SCALE = 0.8f;

    @Override
    public void transformPage(View page, float position) {

        if (position <= 1) {

            float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);

            page.setScaleX(scaleFactor);

            if (position > 0) {
                page.setTranslationX(-scaleFactor * 2);
            } else if (position < 0) {
                page.setTranslationX(scaleFactor * 2);
            }
            page.setScaleY(scaleFactor);
        } else {

            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
    }
}
