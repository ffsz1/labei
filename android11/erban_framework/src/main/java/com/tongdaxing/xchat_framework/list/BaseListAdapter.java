package com.tongdaxing.xchat_framework.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by lijun on 2014/11/12.
 */
public abstract class BaseListAdapter extends BaseAdapter {

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public abstract ListItem getItem(int position);

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final ListItem item = getItem(position);

        if (null == item) {
            throw new RuntimeException("list item is never null. pos:" + position);
        }

        if (null == convertView) {
            holder = createViewHolder(parent, item);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        item.updateHolder(holder, position, -1);
        return convertView;
    }

    private ViewHolder createViewHolder(ViewGroup group, ListItem item) {
        return item.createViewHolder(group);
    }

    @Override
    public boolean isEnabled(int position) {
        ListItem item = getItem(position);
        if (null != item) {
            return item.isEnabled();
        }

        return super.isEnabled(position);
    }
}
