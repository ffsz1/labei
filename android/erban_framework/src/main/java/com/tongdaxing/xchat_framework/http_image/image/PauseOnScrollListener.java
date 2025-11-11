package com.tongdaxing.xchat_framework.http_image.image;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 滚动列表时,停止图片加载,优化图片显示速度
 *
 * @author zhongyongsheng
 */
public class PauseOnScrollListener implements OnScrollListener {

    private ImageManager mImageManager;
    private final boolean mPauseOnScroll;
    private final boolean mPauseOnFling;
    private final OnScrollListener mExternalListener;

    /**
     * @param imageManager
     * @param pauseOnScroll 滚动时是否停止加载图片
     * @param pauseOnFling  手势划动时是否停止加载图片
     */
    public PauseOnScrollListener(ImageManager imageManager, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageManager, pauseOnScroll, pauseOnFling, null);
    }

    /**
     * @param imageManager
     * @param pauseOnScroll    滚动时是否停止加载图片
     * @param pauseOnFling     手势划动时是否停止加载图片
     * @param externalListener 额外的滚动监听器
     */
    public PauseOnScrollListener(ImageManager imageManager, boolean pauseOnScroll, boolean pauseOnFling, OnScrollListener externalListener) {
        this.mImageManager = imageManager;
        this.mPauseOnScroll = pauseOnScroll;
        this.mPauseOnFling = pauseOnFling;
        this.mExternalListener = externalListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                mImageManager.getProcessor().resume();
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (mPauseOnScroll) {
                    mImageManager.getProcessor().pause();
                }
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                if (mPauseOnFling) {
                    mImageManager.getProcessor().pause();
                }
                break;
            default:
                break;
        }
        if (mExternalListener != null) {
            mExternalListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mExternalListener != null) {
            mExternalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
