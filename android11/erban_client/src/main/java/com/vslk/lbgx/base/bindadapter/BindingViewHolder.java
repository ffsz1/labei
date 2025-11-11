package com.vslk.lbgx.base.bindadapter;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;

/**
 * Created by fwhm on 2017/3/28.
 */

public class BindingViewHolder extends BaseViewHolder {

    public BindingViewHolder(View view) {
        super(view);
    }

    public ViewDataBinding getBinding() {
        return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
    }
}
