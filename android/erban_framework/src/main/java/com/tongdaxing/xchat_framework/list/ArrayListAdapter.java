package com.tongdaxing.xchat_framework.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lijun on 2014/11/12.
 * Modified by wjc133 on 2015/8/27.
 */
public class ArrayListAdapter extends BaseListAdapter {

    private boolean mNotifyOnChange = true;
    private final Object mLock = new Object();
    private List<ListItem> items = new ArrayList<>();


    public void addItem(ListItem item) {
        synchronized (mLock) {
            items.add(item);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addItems(List<ListItem> items) {
        synchronized (mLock) {
            this.items.addAll(items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addItems(ListItem... items) {
        synchronized (mLock) {
            Collections.addAll(this.items, items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(ListItem item, int index) {
        synchronized (mLock) {
            items.add(index, item);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void remove(ListItem object) {
        synchronized (mLock) {
            if (items.contains(object)) {
                items.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            items.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public ListItem getItem(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    protected List<ListItem> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

}
