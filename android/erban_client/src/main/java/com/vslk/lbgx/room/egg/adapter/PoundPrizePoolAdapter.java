package com.vslk.lbgx.room.egg.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.GiftInfo;

/**
 * Function:
 * Author: Edward on 2019/5/8
 */
public class PoundPrizePoolAdapter extends BaseQuickAdapter<GiftInfo, BaseViewHolder> {

    public PoundPrizePoolAdapter() {
        super(R.layout.item_pound_egg_prize_pool);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GiftInfo info) {
        ImageView ivImg = baseViewHolder.getView(R.id.iv_img);
        ImageLoadUtils.loadImage(mContext, info.getGiftUrl(), ivImg, R.drawable.content_empty);
        baseViewHolder.setText(R.id.tv_gift_name, info.getGiftName());
        baseViewHolder.setText(R.id.tv_money, " x"+info.getGoldPrice());
    }
}
