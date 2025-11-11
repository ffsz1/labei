package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.domain.UserDrawRecordDO;
import com.juxiao.xchat.dao.user.dto.UserDrawRecordDTO;
import com.juxiao.xchat.service.api.user.vo.UserDrawResultVO;
import com.juxiao.xchat.service.api.user.vo.UserWordDrawResultVO;

public interface UserWordDrawService {

    /**
     * 抽取字体
     * @param uid
     * @return
     */
    UserWordDrawResultVO drawWord(Long uid, Integer activityType) throws WebServiceException;

    /**
     * 创建抽奖奖品
     * @param recordDto
     * @return
     * @throws WebServiceException
     */
    UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) throws WebServiceException;

    /**
     * 抽奖
     * @param uid
     * @param activity
     * @return
     * @throws WebServiceException
     */
    UserDrawResultVO draw(Long uid, Integer activity) throws WebServiceException;

    /**
     * 获取抽奖奖品
     * @param uid
     * @param activityType
     * @return
     */
    UserWordDrawResultVO getUserWordDraw(Long uid, Integer activityType)throws WebServiceException;
}
