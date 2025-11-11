package com.hncxco.library_ui.widget;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Function:
 * Author: Edward on 2019/1/9
 */
public class ViewHolder {
    private SparseArray<View> views;
    private View convertView;

    public View getConvertView() {
        return convertView;
    }

    private ViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }

    public static ViewHolder create(View view) {
        return new ViewHolder(view);
    }

    /**
     * 获取View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * 设置图片
     *
     * @param viewId
     * @param imgRes
     */
    public ViewHolder setImageView(int viewId, int imgRes) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(imgRes);
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param viewId
     * @param colorId
     */
    public ViewHolder setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param viewId
     * @param resId
     */
    public ViewHolder setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param colorId
     */
    public ViewHolder setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
