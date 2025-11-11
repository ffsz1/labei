package com.vslk.lbgx.room.avroom.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.bean.ProbabilityInfo;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/6/21
 */
public class ProbabilityAdapter extends BaseQuickAdapter<ProbabilityInfo, BaseViewHolder> {
    public ProbabilityAdapter(int layoutResId, @Nullable List<ProbabilityInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProbabilityInfo item) {
        TextView tv = helper.getView(R.id.tv);
        ImageView iv = helper.getView(R.id.iv);
        tv.setText(item.getName());
        if (item.isSelected()) {
            tv.setTextColor(mContext.getResources().getColor(R.color.color_C280FF));
            iv.setVisibility(View.VISIBLE);
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.color_50ffffff));
            iv.setVisibility(View.GONE);
        }
    }
}
