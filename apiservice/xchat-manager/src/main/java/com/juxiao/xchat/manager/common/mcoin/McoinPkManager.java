package com.juxiao.xchat.manager.common.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.web.WebServiceException;

public interface McoinPkManager {

    /**
     * 获取正在进行的萌币PK活动信息
     * @return
     * @throws WebServiceException
     */
    JSONObject getPkInfo(Long uid) throws WebServiceException;

    /**
     * 点击支持Pk
     * @param uid
     * @param supportType
     * @throws WebServiceException
     */
    void supportPk(Long uid, Integer supportType) throws WebServiceException;

    /**
     * PK活动开奖
     * @throws WebServiceException
     */
     void lottery(int term, Long endTime) throws WebServiceException;

    /**
     * pk排行榜
     * @param uid
     * @return
     */
    JSONObject rankingList(Long uid)throws WebServiceException;

    /**
     * PK往期回顾
     * @param uid
     * @return
     * @throws WebServiceException
     */
    JSONObject pastPeriod(Long uid)throws WebServiceException;

    /**
     * 新一期活动正式开始
     * @param item
     */
    void pkBeginning(int item);

    /**
     * 发送延迟消息
     */
    void eventSendDelayQueueMessage();
}
