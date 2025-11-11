package com.tongdaxing.xchat_framework.list;

import java.util.List;

/**
 * Created by lijun on 2014/11/7.
 */
public interface GroupItem extends ListItem {

    public List<ListItem> getChildItems();
}
