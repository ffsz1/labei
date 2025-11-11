package com.vslk.lbgx.ui.widget;

import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongdaxing.erban.R;

import java.util.List;

/**
 * <p> 具有加载更多的adapter </p>
 * Created by Administrator on 2017/11/13.
 */
public abstract class LoadingAdapter<B> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTER = 10000;
    private static final int NORMAL = 10001;

    public static final int STATUS_LOADING = 10;
    public static final int STATUS_LOADED = 11;
//    public static final int STATUS_LOADED = 11;

    @IntDef({STATUS_LOADING, STATUS_LOADED})
    public @interface Status {
    }

    private int mStatus = STATUS_LOADED;

    private List<B> mDataList;


    public void setDataList(List<B> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setStatus(@Status int status) {
        mStatus = status;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            return new LoadingViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.loading_more_layout, parent, false));
        } else
            return onCreateHolder(parent, viewType);
    }

    protected abstract RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) return;
        onBindHolder(holder, position);
    }

    protected abstract void onBindHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 2 && mStatus == STATUS_LOADING) {
            return FOOTER;
        }
        return NORMAL;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size() + (mStatus == STATUS_LOADING ? 1 : 0);
    }

    //解决GridLayoutManager 占用问题
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isFooter(position) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isFooter(int position) {
        return mStatus == STATUS_LOADING && position == getItemCount();
    }

    //处理StaggeredGridLayoutManager 占用问题
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition());
        }
    }

    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        return layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams;
    }

    private void handleLayoutIfStaggeredGridLayout(RecyclerView.ViewHolder holder, int position) {
        if (/*isHeader(position) ||*/ isFooter(position)) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {

        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }
}
