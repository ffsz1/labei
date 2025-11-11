package com.vslk.lbgx.ui.me.bills.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.ExpendInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * ExpendInfo
 * Created by Seven on 2017/9/10.
 */
public class GiftExpendAdapter extends BillBaseAdapter {

    public GiftExpendAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_NORMAL, R.layout.list_expend_gift_item);
    }

    @Override
    public void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        ExpendInfo expendInfo = billItemEntity.mGiftExpendInfo;
        if (expendInfo == null) return;
        baseViewHolder.setText(R.id.tv_gift_income, expendInfo.getGoldNum())
                .setText(R.id.tv_send_name, "收礼人: " + expendInfo.getTargetNick())
                .setText(R.id.tv_user_name, expendInfo.getGiftName())
                .setText(R.id.gift_date, TimeUtils.getDateTimeString(expendInfo.getRecordTime(), "yyyy-MM-dd HH:mm:ss"))
                .setImageResource(R.id.image, R.mipmap.ic_ranking_gold);

        ImageView img_avatar = baseViewHolder.getView(R.id.img_avatar);
        ImageLoadUtils.loadImage(mContext, expendInfo.getGiftPic(), img_avatar);
    }
}
