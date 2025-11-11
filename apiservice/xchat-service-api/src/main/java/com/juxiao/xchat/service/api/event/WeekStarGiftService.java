package com.juxiao.xchat.service.api.event;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftDTO;
import com.juxiao.xchat.service.api.event.vo.*;

import java.util.List;

/**
 * @author chris
 * @Title: 周星榜service接口
 * @date 2019-05-20
 * @time 10:31
 */
public interface WeekStarGiftService {


    /**
     * 获取本周礼物,周星奖励,礼物预告
     * @return WeekStarVO
     */
    WeekStarVO getWeekStarGift();


    /**
     * 获取上周明星
     * @param giftId giftId
     * @return list
     */
    List<WeekStartRankVO> getLastWeekStarList(Integer giftId)throws WebServiceException;


    /**
     * 获取自己周星排名
     * @param uid uid
     * @param giftId 礼物ID
     * @return  WeekStartRankVO
     */
    WeekStartRankVO getMyWeekStar(Long uid,Integer giftId)throws WebServiceException;

    /**
     * 获取本周明星排名
     * @param giftId  giftId
     * @return list
     */
    List<WeekStartRankVO> getWeekStartRank(Integer giftId) throws WebServiceException;

    /**
     * 获取周星礼物
     * @return List<WeekStarGiftDTO>
     */
    List<WeekStarGiftDTO> getWeekStartGift();


}
