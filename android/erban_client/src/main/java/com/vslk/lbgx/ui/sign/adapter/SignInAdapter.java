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
 * Author: Edward on 2019/5/24
 */
public class SignInAdapter extends BaseQuickAdapter<MengCoinBean, BaseViewHolder> {
    public SignInAdapter(int layoutResId, @Nullable List<MengCoinBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MengCoinBean item) {
        int position = helper.getLayoutPosition();
        ImageView imageView = helper.getView(R.id.iv);
        TextView textView = helper.getView(R.id.tv);
        View view = helper.getView(R.id.view);
        view.setVisibility(View.VISIBLE);
        if (item.getMissionStatus() == 3) {//已签到
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            if (position == 6) {//第7天
                view.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                ImageLoadUtils.loadImage(mContext, item.getPicUrl(), imageView, R.drawable.ic_give_gift);
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(String.valueOf(position + 1));
                textView.setBackgroundResource(R.drawable.ic_sign_in);
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
