package com.juxiao.xchat.service.api.event;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.event.vo.DutiesVO;

/**
 * 用户任务
 *
 * @class: DutyService.java
 * @author: chenjunsheng
 * @date 2018/8/16
 */
public interface DutyService {

    DutiesVO getUserDuties(Long uid) throws WebServiceException;

    /**
     * 奖励达成接口
     *
     * @param uid
     * @return
     */
    void achieve(Integer dutyId, Long uid) throws WebServiceException;

    /**
     * 大厅发言保存
     *
     * @param uid
     */
    void speakPublic(Long uid) throws WebServiceException;
}
