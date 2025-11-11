package com.vslk.lbgx.ui.me.bills.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.RedBagInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * 红包记录 RedBagInfo
 * Created by ${Seven} on 2017/9/25.
 */
public class RedBagBillsAdapter extends BillBaseAdapter {
    public RedBagBillsAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_NORMAL, R.layout.list_charge_bills_item);
    }

    @Override
    public void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        RedBagInfo redBagInfo = billItemEntity.mRedBagInfo;
        if (redBagInfo == null) return;
        baseViewHolder.setText(R.id.tv_gold,"邀请奖励")
                .setText(R.id.tv_money, "+" + redBagInfo.getPacketNum() + "元")
                .setText(R.id.tv_charge_time, TimeUtils.getDateTimeString(redBagInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
    }
}
