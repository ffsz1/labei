package com.juxiao.xchat.service.api.event.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-20
 * @time 16:33
 */
@Data
public class WeekStarVO {

    /**本周周星礼物*/
    private List<WeekStarGiftVO> weekStarGiftVO;

    /**礼物预告*/
    private List<WeekStarGiftNoticeVO> weekStarGiftNoticeVO;
    /**周星奖励*/
    private List<WeekStarItemRewardVO> weekStarItemRewardVO;

    /**上周星礼物*/
    private List<WeekStarGiftVO> lastWeekStarGiftVO;

}
