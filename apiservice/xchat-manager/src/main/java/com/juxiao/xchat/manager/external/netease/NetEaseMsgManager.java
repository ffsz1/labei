package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.NeteasePushBO;
import com.juxiao.xchat.manager.external.netease.ret.RubbishRet;

/**
 * @class: NetEaseMsgManager.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
public interface NetEaseMsgManager {

    /**
     * 发送单条消息
     *
     * @param msgBo
     * @return
     */
    RubbishRet sendMsg(NeteaseMsgBO msgBo);

    /**
     * 批量发送消息接口
     *
     * @param batchMsgBO
     * @return
     */
    RubbishRet sendBatchMsg(NeteaseBatchMsgBO batchMsgBO);

    /**
     * @return
     */
    RubbishRet sendAttachMsg(NeteasePushBO pushBo);
}
