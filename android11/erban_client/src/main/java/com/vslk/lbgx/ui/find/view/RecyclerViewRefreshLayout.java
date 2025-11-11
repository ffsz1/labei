package com.vslk.lbgx.ui.find.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * 创建者      Created by dell
 * 创建时间    2018/9/6
 * 描述        解决SwipeRefreshLayout与NestedScrollView嵌套时滑动异常的问题
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class RecyclerViewRefreshLayout extends SwipeRefreshLayout {

    public RecyclerViewRefreshLayout(Context context) {
        this(context, null);
    }

    public RecyclerViewRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        CoreManager.addClient(this);
    }


    @Override
    public boolean canChildScrollUp() {
        return canChildScrollUp(this);
    }

    public boolean canChildScrollUp(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View temp = v.getChildAt(i);
            if (temp instanceof RecyclerView) {
                if (canRecycleViewScroll((RecyclerView) temp)) {
                    return true;
                }
            } else if (temp instanceof AbsListView) {
                if (ViewCompat.canScrollVertically(temp, -1)) {
                    return true;
                }
            } else if (temp instanceof ViewGroup) {
                if (canChildScrollUp((ViewGroup) temp)) {
                    return true;
                }
            } else if (ViewCompat.canScrollVertically(temp, -1)) {
                return true;
            }
        }
        return false;
    }

    public boolean canRecycleViewScroll(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() != 0;
    }
}
