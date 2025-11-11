package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by chenran on 2017/7/24.
 */
public class SquareImageView extends android.support.v7.widget.AppCompatImageView {


    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context) {
        super(context);
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();
//        int childHeightSize = getMeasuredHeight();

        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
