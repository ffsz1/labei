package com.tongdaxing.xchat_core.withdraw;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by Administrator on 2017/7/24.
 */

public interface IWithdrawCore  extends IBaseCore{
//        获取提现列表
        void getWithdrawList();
//      获取提现信息
        void getWithdrawUserInfo();
//        兑换接口,发起兑换
        void requestExchange(long uid,int pid,int type);
        //获取手机验证码
        void getSmsCode(long uid);
        //绑定支付宝
        void binderAlipay(String aliPayAccount,String aliPayAccountName,String code);
}
