package com.vslk.lbgx.room.avroom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;

/**
 * Created by huangmeng1 on 2018/1/18.
 */
public class SliddingView extends HorizontalScrollView {
	private LinearLayout mLayout;
	private View delete;
	private int deletewidth;
	private boolean once;

	public SliddingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!once) {
			mLayout = (LinearLayout) getChildAt(0);
			delete =  mLayout.getChildAt(1);
			deletewidth = delete.getLayoutParams().width = UIUtil.dip2px(getContext(),80);
			once = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			int scollx=getScrollX();
			if (scollx>=deletewidth/2) {
				smoothScrollTo(deletewidth, 0);
			}else {
				smoothScrollTo(0, 0);
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}


}
