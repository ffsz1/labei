package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 跑马灯效果
 */
public class MarqueeTextView extends AppCompatTextView {

    private boolean isMarquee = true;

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }

    @Override
    public boolean isFocused() {
        if (isMarquee) {
            return true;
        } else {
            return false;
        }
    }

    public void setMarquee(boolean isMarquee) {
        this.isMarquee = isMarquee;
        this.setFocusable(isMarquee);
        this.setFocusableInTouchMode(isMarquee);
        this.setHorizontallyScrolling(isMarquee);
    }
}
