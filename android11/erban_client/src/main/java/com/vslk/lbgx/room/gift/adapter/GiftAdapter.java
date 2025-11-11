package com.vslk.lbgx.room.gift.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/5/17
 */
public class GiftAdapter extends BaseQuickAdapter<GiftInfo, BaseViewHolder> {
    public GiftAdapter(int layoutResId, @Nullable List<GiftInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftInfo item) {
        ImageView giftImage = helper.getView(R.id.gift_image);
        RelativeLayout giftImageSelected = helper.getView(R.id.gift_image_selected);
        ImageView giftEffects = helper.getView(R.id.icon_gift_effect);
        ImageView giftLimitTime = helper.getView(R.id.icon_gift_limit_time);
        ImageView giftNew = helper.getView(R.id.icon_gift_new);
        TextView freeGiftCount = helper.getView(R.id.tv_free_gift_count);

        helper.setText(R.id.gift_name, item.getGiftName());
        helper.setText(R.id.gift_gold, item.getGoldPrice() + "");
        ImageLoadUtils.loadImage(BasicConfig.INSTANCE.getAppContext(), item.getGiftUrl(), giftImage);

        if (item.getUserGiftPurseNum() > 0) {
            freeGiftCount.setVisibility(View.VISIBLE);
            freeGiftCount.setText("X" + item.getUserGiftPurseNum());
        } else {
            freeGiftCount.setVisibility(View.GONE);
        }

        if (item.isSelected()) {
            giftImageSelected.setBackgroundResource(R.drawable.gift_selected);
        } else {
            giftImageSelected.setBackgroundResource(R.drawable.bg_gift_dialog_unselect);
        }

        if (item.isHasEffect()) {
            giftEffects.setVisibility(View.VISIBLE);
        } else {
            giftEffects.setVisibility(View.GONE);
        }

        if (item.isHasTimeLimit()) {
            giftLimitTime.setVisibility(View.VISIBLE);
        } else {
            giftLimitTime.setVisibility(View.GONE);
        }

        if (item.isHasLatest()) {
            giftNew.setVisibility(View.VISIBLE);
        } else {
            giftNew.setVisibility(View.GONE);
        }
    }
}
