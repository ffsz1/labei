package com.tongdaxing.xchat_core.bills;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by Seven on 2017/9/9.
 */

public interface IBillsCore extends IBaseCore {

    /*传12356*/
//    void getExpendBills(int pageNo,int pageSize,long time);

    void getGiftIncomeBills(int pageNo, int pageSize, long time);

    void getGiftExpendBills(int pageNo, int pageSize, long time);

    void getWithdrawBills(int pageNo, int pageSize, long time);

    /** 获取账单红包 */
    void getWithdrawRedBills(int pageNo, int pageSize, long time);

    void getChatBills(int pageNo, int pageSize, long time);

    //    void getOrderExpendBills(int pageNo,int pageSize,long time);
    void getChargeBills(int pageNo, int pageSize, long time);

    /** 获取红包记录列表 */
    void getRedBagBills(int pageNo, int pageSize, long time);

//    void getIncomBills(int pageNo,int pageSize);
}
