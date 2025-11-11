package com.vslk.lbgx.ui.me.bills.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.RedBagInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * 红包提现账单adapter RedBagInfo
 * Created by Seven on 2017/9/17.
 */
public class WithdrawRedBillsAdapter extends BillBaseAdapter {


    public WithdrawRedBillsAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_NORMAL, R.layout.list_withdraw_bills_item);
    }

    @Override
    public void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        RedBagInfo redBagInfo = billItemEntity.mRedBagInfo;
        if (redBagInfo == null) return;
        baseViewHolder.setText(R.id.tv_diamondNum, "提现" + redBagInfo.getPacketNum() + "开心")
                .setText(R.id.tv_date, TimeUtils.getYearMonthDayHourMinuteSecond(redBagInfo.getCreateTime()))
//                .setText(R.id.tv_money, "+" + redBagInfo.getMoney() + "元")
        ;
    }
}
