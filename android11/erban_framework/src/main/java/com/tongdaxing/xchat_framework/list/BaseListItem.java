package com.tongdaxing.xchat_framework.list;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Creator: 舒强睿
 * Date:2015/4/24
 * Time:19:03
 * <p/>
 * Description：
 */
public class BaseListItem implements ListItem {

    protected Context mContext;
    protected int viewType;
    protected boolean isSelected;

    public BaseListItem(Context mContext) {
        this.mContext = mContext;
    }

    public BaseListItem(Context mContext, int viewType) {
        this.mContext = mContext;
        this.viewType = viewType;
    }

    protected Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder createViewHolder(ViewGroup group) {
        return null;
    }

    @Override
    public void updateHolder(ViewHolder holder, int groupPos, int childPos) {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
