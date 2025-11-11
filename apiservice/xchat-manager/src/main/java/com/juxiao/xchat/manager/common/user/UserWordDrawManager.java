package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.domain.UserWordDrawOverviewDO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawDTO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawOverviewDTO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawPrizeConfDTO;
import com.juxiao.xchat.dao.user.enumeration.UserWordDrawWordType;

import java.util.List;

public interface UserWordDrawManager {

    /***
     * 获取用户没有被使用的抽奖字体
     * @param userId
     * @param activityType
     * @return
     */
    List<UserWordDrawDTO> getUnUsedWordList(Long userId, Integer activityType);

    /**
     * 获取奖品设置列表
     * @param activityType
     * @return
     */
    List<UserWordDrawPrizeConfDTO> getPrizeConfList(Integer activityType);

    /**
     * 获取用户的字体抽奖信息
     * @param userId
     * @param activityType
     * @return
     */
    UserWordDrawOverviewDTO getUserWordDrawOverview(Long userId, Integer activityType);

    /**
     * 保存用户的抽奖信息
     * @param overviewDO
     */
    void saveUserWordDrawOverview(UserWordDrawOverviewDO overviewDO);

    /**
     * 保存获取的字体
     * @param uid
     * @param wordType
     */
    void saveDrawWord(Long uid, UserWordDrawWordType wordType);

    /**
     * 更改用户获取的字体的所有状态
     * @param uid
     * @param activityType
     * @param isUse
     */
    void updateUserWordStatus(Long uid, Integer activityType, Boolean isUse);
}
