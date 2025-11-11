package com.tongdaxing.xchat_framework.list;

import android.view.ViewGroup;

/**
 * Created by lijun on 2014/11/7.
 */
public interface ListItem {

    ViewHolder createViewHolder(ViewGroup group);

    void updateHolder(ViewHolder holder, int groupPos, int childPos);

    boolean isEnabled();

    boolean isSelected();

    int getViewType();

}
