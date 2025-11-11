package com.vslk.lbgx.base.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p> 具有加载更多的recycler view </p>
 * Created by Administrator on 2017/11/13.
 */
public class LRecyclerView extends RecyclerView {

    private boolean mIsLoading = false;
    private int initPreItem = 0;  //预加载个数
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public LRecyclerView(Context context) {
        this(context, null);
    }

    public LRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(mOnScrollListener);
    }

    /**
     * 绑定刷新控件
     *
     * @param swipeRefreshLayout
     */
    public void bindSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public void finishLoading() {
        mIsLoading = false;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    /**
     * 设置预加载的item个数
     *
     * @param initPreItem
     */
    public void setInitPreItem(int initPreItem) {
        this.initPreItem = initPreItem;
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            checkSwipeRefreshNull();
           /* Log.i("liao", "newState=" + newState);

            //第一种方式  有个bug：当最后一个item刚显示出来的时候停止滑动这个时候也会触发滑动到底部的操作
            LayoutManager layoutManager = getLayoutManager();
            int itemCount = layoutManager.getItemCount();
            int lastVisibleItemPosition = 0;
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
            }

            if (!mIsLoading && ((lastVisibleItemPosition + 1) >= itemCount) && newState == SCROLL_STATE_IDLE) {

                Log.i("liao", "加载.....lastVisibleItemPosition=" + lastVisibleItemPosition + ",itemCount=" + itemCount);
                mIsLoading = true;
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnLoadingListener != null) {
                            mOnLoadingListener.onLoading();
                        }
                    }
                });
            }*/

            //第二种方式
            //得到当前显示的最后一个item的view
            View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
            //得到lastChildView的bottom坐标值
            int lastChildBottom = lastChildView.getBottom();
            //得到Recycler view的底部坐标减去底部padding值，也就是显示内容最底部的坐标
            int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
            //通过这个lastChildView得到这个view当前的position值
            int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

            //判断lastChildView的bottom值跟recyclerBottom
            //判断lastPosition是不是最后一个position
            //如果两个条件都满足则说明是真正的滑动到了底部
            if (!mIsLoading
                    && lastChildBottom == recyclerBottom
                    && (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1 - initPreItem)
                    && newState == SCROLL_STATE_IDLE
                    && !mSwipeRefreshLayout.isRefreshing()) {
                mIsLoading = true;
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnLoadingListener != null) {
                            mOnLoadingListener.onLoading();
                        }
                    }
                });
            }
            mSwipeRefreshLayout.setEnabled(!mIsLoading);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //处理刷新冲突问题
//            int topRowVerticalPosition =
//                    (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
//            checkSwipeRefreshNull();
//            mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0 );

        }
    };

    private void checkSwipeRefreshNull() {
        if (mSwipeRefreshLayout == null)
            throw new IllegalArgumentException("必须调用bindSwipeRefreshLayout(SwipeRefreshLayout)绑定SwipeRefreshLayout，解决冲突！！！");
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private OnLoadingListener mOnLoadingListener;

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    public interface OnLoadingListener {
        void onLoading();
    }
}
