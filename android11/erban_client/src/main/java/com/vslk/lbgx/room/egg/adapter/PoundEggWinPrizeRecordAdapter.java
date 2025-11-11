package com.vslk.lbgx.room.egg.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

/**
 * Function:
 * Author: Edward on 2019/5/8
 */
public class PoundEggWinPrizeRecordAdapter extends BaseQuickAdapter<EggGiftInfo, BaseViewHolder> {

    public PoundEggWinPrizeRecordAdapter() {
        super(R.layout.item_pound_egg_win_prize_record);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EggGiftInfo info) {
        ImageView ivImg = baseViewHolder.getView(R.id.iv_img);
        ImageLoadUtils.loadImage(mContext, info.getPicUrl(), ivImg,R.drawable.content_empty);
        baseViewHolder.setText(R.id.tv_gift_name, "你获得了价值");
        baseViewHolder.setText(R.id.tv_gift_price_tv, info.getGoldPrice() + " 的");
        baseViewHolder.setText(R.id.tv_gift_wood_tv, info.getGiftName() + " X " + info.getGiftNum());

        baseViewHolder.setText(R.id.tv_time, TimeUtils.getDateTimeString(info.getCreateTime(), "MM-dd HH:mm"));
    }
}