package com.tongdaxing.xchat_core.bills;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.bills.bean.ExpendListInfo;
import com.tongdaxing.xchat_core.bills.bean.IncomeListInfo;
import com.tongdaxing.xchat_core.bills.bean.RedBagListInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by Seven on 2017/9/9.
 */

public interface IBillsCoreClient extends ICoreClient {


    public static final String METHOD_GET_INCOME_BILLS = "onGetIncomeBills";
    public static final String METHOD_GET_INCOME_BILLS_ERROR = "onGetIncomeBillsError";

    public static final String METHOD_GET_EXPEND_BILLS = "onGetExpendBills";
    public static final String METHOD_GET_EXPEND_BILLS_ERROR = "onGetExpendBillsError";


    public static final String METHOD_GET_CHARGE_BILLS = "onGetChargeBills";
    public static final String METHOD_GET_CHARGE_BILLS_ERROR = "onGetChargeBillsError";

    public static final String METHOD_GET_RED_BAG_BILLS = "onGetRedBagBills";
    public static final String METHOD_GET_RED_BAG_BILLS_ERROR = "onGetRedBagBillsError";

    public static final String METHOD_GET_WIHTDRAW_BILLS = "onGetWithdrawBills";
    public static final String METHOD_GET_WIHTDRAW_BILLS_ERROR = "onGetWithdrawBillsError";

    public static final String METHOD_GET_ORDER_INCOME_BILLS = "onGetOrderIncomeBills";
    public static final String METHOD_GET_ORDER_INCOME_BILLS_ERROR = "onGetOrderIncomeBillsError";

    public static final String METHOD_GET_ORDER_EXPEND_BILLS = "onGetOrderExpendBills";
    public static final String METHOD_GET_ORDER_EXPEND_BILLS_ERROR = "onGetOrderExpendBillsError";

    //账单红包查询
    String METHOD_GET_WITHDRAW_RED_BILLS = "onGetWithdrawRedBills";
    String METHOD_GET_WITHDRAW_RED_BILLS_ERROR = "onGetWithdrawRedBillsError";

    void onGetIncomeBills(IncomeListInfo data);

    void onGetIncomeBillsError(String error);

    void onGetExpendBills(ExpendListInfo data);

    void onGetExpendBillsError(String error);

    void onGetChargeBills(ExpendListInfo data);

    void onGetChargeBillsError(String error);

    void onGetRedBagBills(RedBagListInfo data);

    void onGetRedBagBillsError(String error);

    void onGetWithdrawBills(IncomeListInfo data);

    void onGetWithdrawBillsError(String error);

    void onGetOrderIncomeBills(IncomeListInfo data);

    void onGetOrderIncomeBillsError(String error);

    void onGetOrderExpendBills(JSONObject data);

    void onGetOrderExpendBillsError(String error);
}
