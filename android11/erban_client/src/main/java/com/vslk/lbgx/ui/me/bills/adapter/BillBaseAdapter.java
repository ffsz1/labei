package com.vslk.lbgx.ui.me.bills.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * <p>   账单adapter base</p>
 * Created by Administrator on 2017/11/6.
 */
public abstract class BillBaseAdapter extends BaseMultiItemQuickAdapter<BillItemEntity, BaseViewHolder> {

    public BillBaseAdapter(List<BillItemEntity> billItemEntityList) {
        super(billItemEntityList);
        addItemType(BillItemEntity.ITEM_DATE, R.layout.list_income_gift_title);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity) {
        if (billItemEntity == null) {
            return;
        }
        switch (billItemEntity.getItemType()) {
            case BillItemEntity.ITEM_DATE:
                baseViewHolder.setText(R.id.tv_date,
                        TimeUtils.getDateTimeString(Long.parseLong(billItemEntity.time), "yyyy-MM-dd"));
                break;
            case BillItemEntity.ITEM_NORMAL:
                convertNormal(baseViewHolder, billItemEntity);
                break;
            default:
        }
    }

    public abstract void convertNormal(BaseViewHolder baseViewHolder, BillItemEntity billItemEntity);
}
