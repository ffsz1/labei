package com.tongdaxing.xchat_core.bills.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * <p>  </p>
 * Created by Administrator on 2017/11/17.
 */
public class BillItemEntity implements MultiItemEntity {

    public static final int ITEM_DATE = 1;
    public static final int ITEM_NORMAL = 2;

    private int itemType;

    /** 一天时间日期 */
    public String time;

    /** 礼物收入 */
    public IncomeInfo mGiftInComeInfo;
    /** 密聊实体 */
    public IncomeInfo mChatInComeInfo;
    /** 提现记录 */
    public IncomeInfo mWithdrawInfo;
    /** 充值记录信息 */
    public ExpendInfo mChargeExpendInfo;
    /** 礼物支出 */
    public ExpendInfo mGiftExpendInfo;
    /** 红包记录 +账单红包记录 */
    public RedBagInfo mRedBagInfo;

    public BillItemEntity(int itemType) {
        this.itemType = itemType;
    }

    public BillItemEntity(int itemType, String time) {
        this.itemType = itemType;
        this.time = time;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
