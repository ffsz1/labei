package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import com.juxiao.xchat.dao.sysconf.dto.IconDTO;
import com.juxiao.xchat.dao.sysconf.dto.TabBannerDTO;
import com.juxiao.xchat.dao.user.dto.UserHotDTO;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.service.api.sysconf.vo.HomeV2Vo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;

import java.util.List;

/**
 * 首页接口
 */
public interface HomeV2Service {
    /**
     * @param indexParam
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    HomeV2Vo index(IndexParam indexParam, String clientIp) throws WebServiceException;

    /**
     * @param channel
     * @param indexParam
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    HomeV2Vo index(String channel, IndexParam indexParam, String clientIp) throws WebServiceException;

    /**
     * 查询热门房间列表
     *
     * @param indexParam
     * @param clientIp
     * @return
     */
    List<RoomVo> listHotRoom(IndexParam indexParam, String clientIp) throws WebServiceException;

    /**
     * @param indexParam
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    HomeV2Vo getHotHomeV3(IndexParam indexParam, String clientIp) throws WebServiceException;

    /**
     * 根据标签获取首页的房间信息
     *
     * @param uid      用户ID
     * @param tagId    标签 ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return
     */
    List<RoomVo> findRoomByTag(Long uid, Long tagId, Integer pageNum, Integer pageSize, String os, String app,
                               String appVersion, String clientIp, String channel) throws WebServiceException;

    /**
     * @param channel
     * @param uid
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @param os
     * @param app
     * @param appVersion
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    List<RoomVo> findRoomByTag(String channel, Long uid, Long tagId, Integer pageNum, Integer pageSize, String os,
                               String app, String appVersion, String clientIp) throws WebServiceException;

    /**
     * 根据标签获取首页的房间信息（小程序专用）
     *
     * @param uid
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @param os
     * @param appVersion
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    List<RoomVo> listRoomByTagV3(Long uid, Long tagId, Integer pageNum, Integer pageSize, String os, String app,
                                 String appVersion, String clientIp) throws WebServiceException;

    /**
     * @param uid
     * @param os
     * @param app
     * @param appVersion
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    List getOppositeSex(Long uid, String os, String app, String appVersion, String clientIp) throws WebServiceException;

    /**
     * 获取热门推荐的列表
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<RoomVo> getHomeHotRoom(Long uid, String appVersion, Integer pageNum, Integer pageSize, String appid);

    /**
     * 获取热门推荐的列表
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<RoomVo> getHomeHotRoom(String channel, Long uid, String appVersion, Integer pageNum, Integer pageSize,
                                String appid);

    /**
     * 交友页数据
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @param os
     * @param app
     * @param appVersion
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    List<RoomVo> makeFriends(Long uid, Integer pageNum, Integer pageSize, String os, String app, String appVersion,
                             String clientIp) throws WebServiceException;

    /**
     * 获取首页顶部横幅
     *
     * @param uid
     * @param os
     * @param app
     * @return
     */
    List<BannerDTO> getIndexTopBanner(Long uid, String os, String app);

    /**
     * 获取发现页顶部Item
     *
     * @param uid
     * @param os
     * @param app
     * @return
     */
    List<BannerDTO> getDiscoverItem(Long uid, String os, String app);

    /**
     * @param indexParam
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    List<IconDTO> getIndexHomeIcon(IndexParam indexParam, String clientIp) throws WebServiceException;

    /**
     * 获取优质陪陪列表
     *
     * @param indexParam
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    List<UserHotDTO> getBestCompanies(IndexParam indexParam, String clientIp) throws WebServiceException;

    /**
     * 获取优质陪陪列表
     *
     * @param indexParam
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    List<UserHotDTO> getNewUsers(IndexParam indexParam, String clientIp) throws WebServiceException;

    /**
     * 获取首页顶部Tab和banner
     *
     * @param uid
     * @param os
     * @param app
     * @return
     */
    TabBannerDTO getIndexTopTabBanner(Long uid, String os, String app);
}
