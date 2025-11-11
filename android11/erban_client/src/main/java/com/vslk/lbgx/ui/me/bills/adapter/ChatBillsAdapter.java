package com.vslk.lbgx.ui.me.bills.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.IncomeInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * IncomeInfo
 * Created by Seven on 2017/9/18.
 */
public class ChatBillsAdapter extends BillBaseAdapter {

    public ChatBillsAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_NORMAL, R.layout.list_order_bills_item);
    }

    @Override
    public void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        IncomeInfo incomeInfo = billItemEntity.mChatInComeInfo;
        if (incomeInfo == null) return;
        baseViewHolder.setVisible(R.id.rly_gold, true)
                .setText(R.id.tv_user_pro, incomeInfo.getTargetNick() + "&" + incomeInfo.getUserNick())
                .setText(R.id.tv_date, TimeUtils.getYearMonthDayHourMinuteSecond(incomeInfo.getRecordTime()))
                .setText(R.id.tv_gold, incomeInfo.getGoldNum() != 0
                        ? String.valueOf(incomeInfo.getGoldNum()) : "+" + incomeInfo.getDiamondNum())
                .setText(R.id.tv_bill_type, incomeInfo.getGoldNum() != 0
                        ? mContext.getString(R.string.gift_expend_gold) : mContext.getString(R.string.gift_income_gold));

        CircleImageView userAvatar = baseViewHolder.getView(R.id.user_avatar);
        CircleImageView proAvatar = baseViewHolder.getView(R.id.pro_avatar);
        ImageLoadUtils.loadAvatar(mContext, incomeInfo.getUserAvatar(), userAvatar);
        ImageLoadUtils.loadAvatar(mContext, incomeInfo.getTargetAvatar(), proAvatar);
    }
}
