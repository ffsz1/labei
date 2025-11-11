package com.tongdaxing.xchat_framework.list;

import android.view.View;

/**
 * Created by lijun on 2014/11/7.
 */
public abstract class ViewHolder {

    public final View itemView;

    public ViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
    }
}
