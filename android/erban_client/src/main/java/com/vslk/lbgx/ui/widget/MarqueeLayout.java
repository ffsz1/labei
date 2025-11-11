package com.vslk.lbgx.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tongdaxing.xchat_framework.util.util.ResolutionUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

/**
 * Created by xxf-kaede on 2015/1/7.
 */

public class MarqueeLayout extends FrameLayout {
	TextView view;
	private AnimatorSet animatorSet;

	public MarqueeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 使用MarqueeLayout后，一定要在界面销毁的时候调用reserverAnimation()
	 * 不然可能导致内存泄露
	 */
	public void startMarquee() {
		MLog.debug(this, "[xxf-kaede] startMarquee");
		reserverAnimation();

		view = (TextView) getChildAt(0);
		if (view!=null)
		{
			TextPaint paint = view.getPaint();
			float len = paint.measureText(view.getText().toString());


			MLog.debug(this, "[xxf-kaede] 用户昵称字体长度="+len);
			if (len>= ResolutionUtils.convertDpToPixel(220f,this.getContext()))
			{
				//开始跑马灯动画
				LayoutParams lp = new LayoutParams((int) len, LayoutParams.WRAP_CONTENT);
				view.setLayoutParams(lp);
				animatorSet = new AnimatorSet();
				ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, -(int) len);
				objectAnimator.setStartDelay(2000);
				objectAnimator.setDuration(5000);
				objectAnimator.setInterpolator(new LinearInterpolator());
				//objectAnimator.setRepeatCount(1);

				ObjectAnimator valueAnimator = ObjectAnimator.ofFloat(view, "translationX", (int) len, -(int) len);
				valueAnimator.setStartDelay(0);
				valueAnimator.setRepeatCount(Animation.INFINITE);
				valueAnimator.setRepeatMode(Animation.RESTART);
				valueAnimator.setInterpolator(new LinearInterpolator());
				valueAnimator.setDuration(10000);

				animatorSet.play(objectAnimator).before(valueAnimator);
				animatorSet.start();
			}
		}
	}

	//清除属性动画
	public void reserverAnimation()
	{
		MLog.debug(this, "[xxf-kaede] reserverAnimation");
		if (animatorSet!=null){
			animatorSet.end();
			animatorSet=null;
		}
		if (view!=null) view.setTranslationX(0);
	}
}
