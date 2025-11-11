package com.vslk.lbgx.ui.me.bills.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.ExpendInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * 金币收入 ExpendInfo
 * Created by ${Seven} on 2017/9/15.
 *
 * @author dell
 */
public class ChargeBillsAdapter extends BillBaseAdapter {

    public ChargeBillsAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_NORMAL, R.layout.list_charge_bills_item);
    }

    @Override
    public void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        ExpendInfo expendInfo = billItemEntity.mChargeExpendInfo;
        if (expendInfo == null) {
            return;
        }
        baseViewHolder.setText(R.id.tv_money, expendInfo.getShowStr())
                .setText(R.id.tv_gold, "+" + expendInfo.getGoldNum() + "金币")
                .setText(R.id.tv_charge_time,
                        TimeUtils.getDateTimeString(expendInfo.getRecordTime(), "yyyy-MM-dd HH:mm:ss"));
    }
}
