package com.vslk.lbgx.ui.home.adpater;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.xchat_core.home.HomeInfo;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/6/15
 */
public class NewHomeHotAdapter extends BaseQuickAdapter<HomeInfo, BaseViewHolder> {
    public NewHomeHotAdapter(int layoutResId, @Nullable List<HomeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeInfo item) {

    }
}
