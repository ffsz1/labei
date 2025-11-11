package com.vslk.lbgx.ui.me.bills.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.IncomeInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * IncomeInfo
 * Created by Seven on 2017/9/17.
 */

public class WithdrawBillsAdapter extends BillBaseAdapter {

    public WithdrawBillsAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_NORMAL, R.layout.list_withdraw_bills_item);
    }

    @Override
    public void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        IncomeInfo incomeInfo = billItemEntity.mWithdrawInfo;
        if (incomeInfo == null) return;
        baseViewHolder.setText(R.id.tv_date, TimeUtils.getDateTimeString(incomeInfo.getRecordTime(), "yyyy-MM-dd HH:mm:ss"))
                .setText(R.id.tv_diamondNum, "提现" + incomeInfo.getDiamondNum() + "钻石")
                .setText(R.id.tv_money, "+" + incomeInfo.getMoney() + "元");
    }
}
