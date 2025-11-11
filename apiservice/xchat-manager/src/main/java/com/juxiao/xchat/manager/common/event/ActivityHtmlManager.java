package com.juxiao.xchat.manager.common.event;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.domain.RankActDO;
import com.juxiao.xchat.manager.common.event.vo.ActivityHtmlVO;
import com.juxiao.xchat.manager.common.event.vo.RankActVo;

import java.util.List;

public interface ActivityHtmlManager {
    /**
     * 根据礼物类型--查询收到此礼物的排名
     *
     * @return
     */
    List<RankActDO> queryList(Integer type) throws WebServiceException;

    /**
     * 房间分类日排行
     *
     * @return
     */
    List<RankActVo> getRoomDayRank(Integer type);

    void refreshLast();

    void refreshOneWeek();

    void refreshAll();

    void refreshRoomRank();

    /**
     * 获取活动页信息
     *
     * @param activityId 活动ID
     * @return
     */
    ActivityHtmlVO getActivityInfo(String activityId);
}
