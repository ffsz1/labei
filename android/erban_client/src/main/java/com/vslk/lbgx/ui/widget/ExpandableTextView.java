package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 创建者      Created by dell
 * 创建时间    2019/1/8
 * 描述
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class ExpandableTextView extends RelativeLayout {

    private TextView mText;

    private TextView mExpandText;

    private int mTextColor = Color.BLACK;

    private int mTextLine = 2;

    private int mStart;

    private int mEnd;

    private boolean isFirst = true;

    private boolean isExpand = false;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final ViewGroup.LayoutParams params = getLayoutParams();
        if (isFirst) {
            isFirst = false;
            mText.post(() -> {
                mStart = mText.getLineHeight() * mText.getLineCount();
                params.height = mStart;
                setLayoutParams(params);
            });
            mExpandText.post(() -> mEnd = mExpandText.getLineHeight() * mExpandText.getLineCount());
        }
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mText = new TextView(context, attrs);
        mText.setTextColor(mTextColor);
        mText.setEllipsize(TextUtils.TruncateAt.END);
        mText.setMaxLines(mTextLine);
        addView(mText, params);
        mExpandText = new TextView(context);
        mExpandText.setTextColor(Color.TRANSPARENT);
        addView(mExpandText, params);
    }

    public void setText(String text) {
        isFirst = true;
        mText.setText(text);
        mExpandText.setText(text);
        requestLayout();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mText.setTextColor(color);
    }

    public void setTextSize(int size) {
        isFirst = true;
        mText.setTextSize(size);
        mExpandText.setTextSize(size);
        requestLayout();
    }

    public void setTextMaxLine(int num) {
        mTextLine = num;
        mText.setMaxLines(num);
    }

    @Override
    public void setGravity(int gravity) {
        mText.setGravity(gravity);
        mExpandText.setGravity(gravity);
    }

    public void setEllipsize(TextUtils.TruncateAt ell) {
        mText.setEllipsize(ell);
    }

    public void setTextLineSpacingExtra(float spac) {
        mText.setLineSpacing(spac, 1.0f);
        mExpandText.setLineSpacing(spac, 1.0f);
    }

    public TextView text() {
        return mText;
    }

    public TextView expandText() {
        return mExpandText;
    }

    public int line() {
        return mTextLine;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void expand() {
        if (!isExpand) {
            isExpand = true;
            mText.setTextColor(Color.TRANSPARENT);
            mExpandText.setTextColor(mTextColor);
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = mStart + (int) ((mEnd - mStart) * interpolatedTime);
                    setLayoutParams(params);
                }
            };
            animation.setDuration(350);
            startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    if (listener != null) {
                        listener.onExpandabled(isExpand);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    public void shrink() {
        if (isExpand) {
            isExpand = false;
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = mStart + (int) ((mEnd - mStart) * (1 - interpolatedTime));
                    setLayoutParams(params);
                }
            };
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    if (listener != null) {
                        listener.onExpandabled(isExpand);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mText.setTextColor(mTextColor);
                    mExpandText.setTextColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(350);
            startAnimation(animation);
        }
    }

    public void switchs() {
        if (isExpand) {
            shrink();
        } else {
            expand();
        }
    }

    private OnExpandableListener listener;

    public void setOnExpandableListener(OnExpandableListener listener) {
        this.listener = listener;
    }

    public interface OnExpandableListener {
        void onExpandabled(boolean isExpand);
    }
}
