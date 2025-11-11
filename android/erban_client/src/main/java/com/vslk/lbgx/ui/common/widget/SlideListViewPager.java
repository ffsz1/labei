package com.vslk.lbgx.ui.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class SlideListViewPager extends ViewPager {

	private Context context;

	public SlideListViewPager(Context context) {
		super(context);
		// Auto-generated constructor stub
		this.context = context;
	}

	public SlideListViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Auto-generated constructor stub
		this.context = context;
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent event) {//限制出发滑动的范围
//		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			float x = event.getX();
//			if(x < 10 || x > getScreenWidth(context) - 10){
//				return true;
//			}else{
//				return false;
//			}
//		}
//		return super.onInterceptTouchEvent(event);
//	}

	// 获取屏幕的宽度
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	// 获取屏幕的宽度
	public static int getScreenWidths(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;

		int screenHeight = dm.heightPixels;
		return screenWidth;
	}
}
