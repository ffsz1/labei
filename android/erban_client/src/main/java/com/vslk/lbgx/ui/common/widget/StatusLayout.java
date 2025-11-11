package com.vslk.lbgx.ui.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.lang.ref.WeakReference;


/**
 * 状态布局：无数据，网络错误，加载中
 * Created by xujiexing on 14-7-16.
 */
public class StatusLayout extends RelativeLayout {

    private int paddingBottom;

    public StatusLayout(Context context) {
        super(context);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs);
    }

    public StatusLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParams(context, attrs);
    }

    private TextView mLoadingMore;
    private View mLoadingContainer;
    private View mProgress;

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusLayout);
        paddingBottom = a.getDimensionPixelOffset(R.styleable.StatusLayout_status_bottom, 0);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addLoadingFooter();
        addStatus();
    }

    private void addLoadingFooter() {
        View status = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_more, null);
        RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        status.setVisibility(View.GONE);
        this.addView(status, getChildCount(), params);
        mLoadingMore = (TextView) findViewById(R.id.loading_text);
        mLoadingContainer = findViewById(R.id.loading_more);
        mProgress = findViewById(R.id.loading_progress);
    }

    /**
     * 加载，出错，网络错误等状态显示的container
     */
    private void addStatus() {
      /*
        final RelativeLayout status = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_status_container, null);
        if (paddingBottom > 0) {
            status.setPadding(0, 0, 0, paddingBottom);
            status.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    status.setBackgroundResource(R.color.common_bg_color);
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {
                    status.setBackgroundResource(R.color.transparent);
                }
            });
        }
        this.addView(status, getChildCount(), new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));*/

        setId(R.id.status_layout);
        if (paddingBottom > 0) {
            setPadding(0, 0, 0, paddingBottom);
            setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    setBackgroundResource(R.color.common_bg_color);
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {
                    setBackgroundResource(R.color.transparent);
                }
            });
        }
        setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    public void showErrorPage(int tips, OnClickListener listener) {
        if (mLoadingMore == null)
            return;
        if (tips <= 0)
            tips = R.string.click_or_pull_refresh;
        String text = getContext().getString(tips);

        mLoadingMore.setText(text);
        mLoadingContainer.setOnClickListener(listener);
        mProgress.setVisibility(View.GONE);
        mLoadingContainer.setVisibility(View.VISIBLE);
    }

    public void showLoadMore() {
        mLoadingMore.setText(getContext().getString(R.string.loading));
        mProgress.setVisibility(View.VISIBLE);
        mLoadingContainer.setOnClickListener(null);
        mLoadingContainer.setVisibility(View.VISIBLE);
    }

    public void hideLoadMore() {
        try {
            mLoadingContainer.setVisibility(View.GONE);
        } catch (Exception e) {
            MLog.error(this, "hideLoadMore error ", e);
            mHandler.sendEmptyMessage(0);
        }

    }

    StatusLayoutHandler mHandler = new StatusLayoutHandler(this);

    static class StatusLayoutHandler extends Handler {
        WeakReference<StatusLayout> statusLayout;

        public StatusLayoutHandler(StatusLayout statusLayout) {
            this.statusLayout = new WeakReference<>(statusLayout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (statusLayout == null || statusLayout.get() == null)
                return;
            statusLayout.get().mLoadingContainer.setVisibility(View.GONE);
            MLog.info(this, "hideLoadMore in handler", "");
        }
    }
}
