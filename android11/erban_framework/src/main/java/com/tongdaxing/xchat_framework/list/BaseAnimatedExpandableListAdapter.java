package com.tongdaxing.xchat_framework.list;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lijun on 2014/11/11.
 */
public abstract class BaseAnimatedExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    @Override
    public abstract int getGroupCount();

    @Override
    public abstract GroupItem getGroup(int groupPosition);

    @Override
    public ListItem getChild(int groupPosition, int childPosition) {
        GroupItem groupItem = getGroup(groupPosition);
        if (null != groupItem.getChildItems()) {
            return groupItem.getChildItems().get(childPosition);
        }

        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        Log.d("BaseAnimatedExpandableListAdapter:", "getRealChildView, groupPosition:" + groupPosition +
//                ", childPosition:" + childPosition + " convertView:" + convertView);

        final ViewHolder holder;
        ListItem childItem = getChild(groupPosition, childPosition);
        if (null == childItem) {
            throw new RuntimeException("list item is never null. pos:" + groupPosition);
        }

        if (null == convertView) {
            holder = createViewHolder(parent, childItem);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        childItem.updateHolder(holder, groupPosition, childPosition);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        GroupItem groupItem = getGroup(groupPosition);
        if (null != groupItem.getChildItems()) {
            return groupItem.getChildItems().size();
        }

        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        Log.d("BaseAnimatedExpandableListAdapter:", "getGroupView:" + groupPosition + " convertView:" + convertView);

        final ViewHolder holder;
        final ListItem item = getGroup(groupPosition);

        if (null == item) {
            throw new RuntimeException("list item is never null. pos:" + groupPosition);
        }

        if (null == convertView) {
            holder = createViewHolder(parent, item);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        item.updateHolder(holder, groupPosition, -1);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    private ViewHolder createViewHolder(ViewGroup group, ListItem item) {
        return item.createViewHolder(group);
    }

}