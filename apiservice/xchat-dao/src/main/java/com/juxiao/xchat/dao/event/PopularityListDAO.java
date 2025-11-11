package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.event.dto.PopularityListDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人气榜DAO接口
 */
public interface PopularityListDAO {
    /**
     * 查询本周人气榜单前二十名
     *
     * @param giftId 礼物ID
     * @param gender 性别
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<PopularityListDTO> queryTop20List(@Param("giftId") Integer giftId, @Param("gender") Integer gender);

    /**
     * 查询上周人气榜单女神男神前三名
     *
     * @param giftId 礼物ID
     * @param gender 性别
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<PopularityListDTO> queryLastWeekRank(@Param("giftId") Integer giftId, @Param("gender") Integer gender);

    /**
     * 查询上周某个用户的星推官 (即本周送给该用户人气票的前三名)
     *
     * @param giftId    礼物ID
     * @param receiveId 收到人气票对应的用户ID
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<PopularityListDTO> queryUserRecommendLastWeek(@Param("giftId") Integer giftId, @Param("receiveId") Long receiveId);

    /**
     * 查询某个用户的星推官 (即本周送给该用户人气票的前三名)
     *
     * @param giftId    礼物ID
     * @param receiveId 收到人气票对应的用户ID
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<PopularityListDTO> queryUserRecommend(@Param("giftId") Integer giftId, @Param("receiveId") Long receiveId);

    /**
     * 查询某个用户的人气榜排名和收到的人气票 (本周)
     *
     * @param giftId    礼物ID
     * @param gender    性别
     * @param receiveId 收到人气票对应的用户ID
     * @return
     */
    @TargetDataSource(name = "ds2")
    PopularityListDTO queryMyRank(@Param("giftId") Integer giftId, @Param("gender") Integer gender, @Param(
            "receiveId") Long receiveId);

    /**
     * 查询某个用户送出的人气票数 (本周)
     *
     * @param giftId 礼物ID
     * @param uid    该用户送出人气票的ID
     * @return
     */
    @TargetDataSource(name = "ds2")
    int querySendVotes(@Param("giftId") Integer giftId, @Param("uid") Long uid);
}
