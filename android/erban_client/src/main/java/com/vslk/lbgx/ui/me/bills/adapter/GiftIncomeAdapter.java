package com.vslk.lbgx.ui.me.bills.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.IncomeInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * Created by Seven on 2017/9/11.
 */

public class GiftIncomeAdapter extends BillBaseAdapter {

    public GiftIncomeAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_NORMAL, R.layout.list_expend_gift_item);
    }

    @Override
    public void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        IncomeInfo incomeInfo = billItemEntity.mGiftInComeInfo;
        if (incomeInfo == null) {
            return;
        }
        baseViewHolder.setText(R.id.tv_gift_income, "+" + incomeInfo.getDiamondNum())
                .setText(R.id.tv_send_name, "送礼人: " + incomeInfo.getTargetNick())
                .setText(R.id.tv_user_name, incomeInfo.getGiftName())
                .setText(R.id.gift_date, TimeUtils.getDateTimeString(incomeInfo.getRecordTime(), "yyyy-MM-dd HH:mm:ss"))
                .setTextColor(R.id.tv_gift_income, Color.parseColor("#FF18B0FE"))
                .setImageResource(R.id.image, R.mipmap.ic_wallet_diamond);
        ImageView avatar = baseViewHolder.getView(R.id.img_avatar);
        ImageLoadUtils.loadImage(mContext, incomeInfo.getGiftPic(), avatar);
    }
}
