package com.vslk.lbgx.ui.sign.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.mengcoin.MengCoinBean;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/5/23
 */
public class TaskCenterSignInAdapter extends BaseQuickAdapter<MengCoinBean, BaseViewHolder> {
    public TaskCenterSignInAdapter(int layoutResId, @Nullable List<MengCoinBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MengCoinBean item) {
        int position = helper.getLayoutPosition();
        helper.setText(R.id.tv_day, "第" + (position + 1) + "天");
        ImageView imageView = helper.getView(R.id.iv);
        ImageView ivGift = helper.getView(R.id.iv_gift);
        TextView textView = helper.getView(R.id.tv);
        if (item.getMissionStatus() == 3) {//已签到
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            ivGift.setVisibility(View.GONE);
        } else {
            if (position == 6) {//第7天
                ivGift.setVisibility(View.VISIBLE);
                ImageLoadUtils.loadImage(mContext, item.getPicUrl(), ivGift, R.drawable.ic_give_gift);
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setBackgroundResource(R.drawable.shape_f5f5f5_5dp);
                textView.setText("");
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText("+" + (int) item.getMcoinAmount());
                textView.setBackgroundResource(R.drawable.shape_f5f5f5_5dp);
                ivGift.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
