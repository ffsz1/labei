package com.vslk.lbgx.room.avroom.adapter;

import com.tongdaxing.xchat_framework.list.ArrayListAdapter;
import com.tongdaxing.xchat_framework.list.ListItem;

import java.util.List;

/**
 * Created by chenran on 2017/8/9.
 */

public class AuctionListAdapter extends ArrayListAdapter{
    public static final int VIEW_TYPE_COUNT = 5;//列表有4种类型

    public static final int VIEW_TYPE_WEEK_ITEM = 0;//周榜
    public static final int VIEW_TYPE_TOTAL_ITEM = 1;//总榜
    public static final int VIEW_TYPE_EMPTY = 2;//空内容
    public static final int VIEW_TYPE_WEEK_HEADER = 3;//周榜header
    public static final int VIEW_TYPE_TOTAL_HEADER = 4;//总榜header


    private List<ListItem> items = getItems();

    public void clear(int except) {
        if (except >= 0 && except < items.size()) {
            ListItem item = items.get(except);
            items.clear();
            items.add(item);
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
