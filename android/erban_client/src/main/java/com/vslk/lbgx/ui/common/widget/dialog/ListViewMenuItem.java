package com.vslk.lbgx.ui.common.widget.dialog;

/**
 * Creator： Chanry
 * Date：2016/7/20
 * Time: 17:31
 * <p/>
 * Description: 列表长按出现的菜单
 */
public class ListViewMenuItem {

    public String mTitle;
    public OnClickListener mClickListener;

    public interface OnClickListener {
        void onClick();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mClickListener = listener;
    }

    public ListViewMenuItem(String mTitle, OnClickListener mClickListener) {
        this.mTitle = mTitle;
        this.mClickListener = mClickListener;
    }
}
