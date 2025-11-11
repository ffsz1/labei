package com.vslk.lbgx.base.bindadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tongdaxing.erban.R;


/**
 * Created by fwhm on 2017/7/5.
 */

public class BaseAdapter<T> extends BaseQuickAdapter<T,BindingViewHolder> {

    private int brid;

    public BaseAdapter(@LayoutRes int layoutResId, int brid) {
        super(layoutResId);
        this.brid = brid;
    }

    @Override
    protected void convert(BindingViewHolder helper, T item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(brid, item);
        binding.executePendingBindings();
    }
    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }
}
