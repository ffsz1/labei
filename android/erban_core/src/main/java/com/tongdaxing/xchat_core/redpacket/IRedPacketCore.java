package com.tongdaxing.xchat_core.redpacket;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by ${Seven} on 2017/9/20.
 */

public interface IRedPacketCore extends IBaseCore {
    //获取红包页面信息
    void getRedPacketInfo();

    //进入主界面有红包后设置弹窗类型 1，首页  2直播间
    void getActionDialog(int type);

    //获取红包提现列表
    void getRedList();

    //发起红包提现
    void getRedWithdraw(long uid, int packetId);

    //获取红包提现列表
    void getRedDrawList();

    //发起红包提现v2
    void getRedWithdraw(long uid, int packetId, String openId);

    //获取红包金额
}


