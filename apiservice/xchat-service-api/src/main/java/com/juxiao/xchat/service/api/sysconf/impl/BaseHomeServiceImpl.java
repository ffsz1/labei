package com.juxiao.xchat.service.api.sysconf.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.IOSData;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.Utils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.RoomAttentionDao;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomTagDTO;
import com.juxiao.xchat.dao.room.dto.UsersRoomAttentionDTO;
import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import com.juxiao.xchat.dao.sysconf.dto.GeneralReviewWhitelist;
import com.juxiao.xchat.dao.sysconf.dto.IconDTO;
import com.juxiao.xchat.dao.sysconf.dto.ReviewConfigDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.dto.TabBannerDTO;
import com.juxiao.xchat.dao.sysconf.dto.TopTabDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfNameSpace;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.dto.UserHotDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.constant.AppClient;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.RoomTagManager;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.RankService;
import com.juxiao.xchat.service.api.room.RoomService;
import com.juxiao.xchat.service.api.sysconf.BannerService;
import com.juxiao.xchat.service.api.sysconf.HomeV2Service;
import com.juxiao.xchat.service.api.sysconf.IconService;
import com.juxiao.xchat.service.api.sysconf.ReviewConfigService;
import com.juxiao.xchat.service.api.sysconf.vo.HomeV2Vo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public abstract class BaseHomeServiceImpl implements HomeV2Service {
    private static final Logger logger = LoggerFactory.getLogger(HomeV2ServiceImpl.class);

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private AppVersionManager appVersionService;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private IconService iconService;

    @Autowired
    private RankService rankService;

    @Autowired
    private RoomTagManager roomTagManager;

    @Autowired
    private Gson gson;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private SystemConf systemConf;

    @Autowired
    private ReviewConfigService reviewConfigService;

    @Autowired
    private RoomAttentionDao roomAttentionDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private LevelManager levelManager;

    @Autowired
    private SysConfManager sysConfManager;

    @Override
    public abstract HomeV2Vo index(String channel, IndexParam indexParam, String clientIp) throws WebServiceException;

    @Override
    public abstract List<RoomVo> findRoomByTag(String channel, Long uid, Long tagId, Integer pageNum,
                                               Integer pageSize, String os, String app, String appVersion,
                                               String clientIp) throws WebServiceException;

    @Override
    public abstract List<RoomVo> getHomeHotRoom(String channel, Long uid, String appVersion, Integer pageNum,
                                                Integer pageSize, String appid);

    @Override
    public HomeV2Vo index(IndexParam indexParam, String clientIp) throws WebServiceException {
        HomeV2Vo homeV2Vo = new HomeV2Vo();
        String appVersion = indexParam.getAppVersion();

        /** 推荐和热门全在task完成判断 **/
        if (indexParam.getPageNum() == 1) {
            // 首页中间根据viewType获取banner
            // banner
            homeV2Vo.setBanners(bannerService.findBannerListByTagId(indexParam.getUid(), indexParam.getOs(), 3L));
            // icon
            homeV2Vo.setHomeIcons(iconService.findIconList(false, indexParam.getAppid(), appVersion, indexParam.getOs()));
            // 排行榜
            // homeV2Vo.setRankHome(rankService.getRankHomeByCache());
            // 绿色厅列表
            // homeV2Vo.setListGreenRoom(getGreenRoom());
            // 房间分类列表(标签列表)
            homeV2Vo.setRoomTagList(roomTagManager.getSearchTags(true));
            List<RoomVo> hotRoom = findHotRoom(indexParam.getUid());
            // 隐藏指定房间内标题包含敏感字的房间 在task进行判断
            // hotRoom = hideGivenRoom(indexParam.getUid(), hotRoom);
            // 设置优质推荐房间
            homeV2Vo.setHotRooms(hotRoom);
            // 设置指定UID的优质推荐房间
            homeV2Vo.setAgreeRecommendRooms(hotRoom);
        }

        List<RoomVo> roomList = Lists.newArrayList();
        List<RoomVo> homeRoomList = findHomeRoomList(indexParam.getUid(), indexParam.getPageNum(),
                indexParam.getPageSize());
        if (homeRoomList != null) {
            roomList.addAll(homeRoomList);
        }
        // 设置热门房间
        homeV2Vo.setListRoom(roomList);

        return homeV2Vo;
    }

    @Override
    public List<RoomVo> listHotRoom(IndexParam indexParam, String clientIp) throws WebServiceException {
        if (indexParam == null) {
            indexParam = new IndexParam();
        }
        if (appVersionService.checkAuditingVersion(indexParam.getOs(), indexParam.getAppid(),
                indexParam.getAppVersion(), clientIp, indexParam.getUid())) {
            // 只显示牌照房
            return listRoomByUid(IOSData.AUDIT_ALIST)
                    .stream().filter((RoomVo vo) -> vo.getIsPermitRoom() != null && vo.getIsPermitRoom() == 1)
                    .collect(Collectors.toList());
        } else {
            String json = redisManager.get(RedisKey.home_room_list.getKey());
            return getRoomVoFromCache(indexParam.getUid(), json, indexParam.getPageNum(), indexParam.getPageSize());
        }
    }

    @Override
    public HomeV2Vo getHotHomeV3(IndexParam param, String clientIp) throws WebServiceException {
        if (!AppClient.WXAPP.equalsIgnoreCase(param.getOs())) {
            return null;
        }
        HomeV2Vo homeV2Vo = new HomeV2Vo();
        if (appVersionService.checkAuditingVersion(param.getOs(), param.getAppid(), param.getAppVersion(), clientIp,
                param.getUid())) {
            homeV2Vo.setRoomTagList(Lists.newArrayList(roomTagManager.getById(4L)));
        } else {
            homeV2Vo.setRoomTagList(roomTagManager.listWxappTags());// 房间分类列表
        }

        return homeV2Vo;
    }

    private HomeV2Vo getIOSData(String app, String appVersion, String os) {
        HomeV2Vo homeVo = new HomeV2Vo();
        homeVo.setListRoom(listRoomByUid(IOSData.AUDIT_ALIST));
        RoomTagDTO tagDto = roomTagManager.getById(4L);
        homeVo.setRoomTagList(Lists.newArrayList(tagDto));
        homeVo.setHomeIcons(iconService.findIconList(true, app, appVersion, os));
        // 获取 banner 列表
        homeVo.setBanners(listBanner(app));
        homeVo.setHotRooms(Lists.newArrayList());
        // homeVo.setListNewRoom(newRooms);
        return homeVo;
    }

    /**
     * 列出各个用户ID对应的房间号
     *
     * @param uidList UID列表
     * @return
     */
    public List<RoomVo> listRoomByUid(List<Long> uidList) {
        List<RoomVo> list = Lists.newArrayList();
        if (uidList == null) {
            return list;
        }

        RoomDTO roomDTO;
        RoomVo roomVo;
        UsersDTO usersDTO;
        for (Long uid : uidList) {
            roomDTO = roomService.getRoomByUid(uid);
            if (roomDTO != null && roomDTO.getValid()) {
                roomVo = roomManager.convertRoomToVo(roomDTO);
                if (roomVo == null) {
                    continue;
                }

                usersDTO = usersManager.getUser(uid);
                if (usersDTO != null) {
                    fillUserToRoom(roomVo, usersDTO);
                }

                roomVo.setOnlineNum((roomVo.getOnlineNum() == null ? 1 : roomVo.getOnlineNum()) + (roomVo.getFactor() == null ? 0 : roomVo.getFactor()));
                list.add(roomVo);
            }
        }
        return list;
    }

    public List<BannerDTO> listBanner(String app) {
        List<BannerDTO> bannerList = Lists.newArrayList();
        BannerDTO bannerVo = new BannerDTO();
        bannerVo.setBannerId(1);
        bannerVo.setBannerName("喵喵手册");
        bannerVo.setBannerPic("https://pic.chaoxuntech.com/ios_banner.png");
        bannerVo.setSkipUri("http://域名/front/question/question.html");
        bannerVo.setSkipType(new Byte("3"));
        bannerVo.setSeqNo(1);
        bannerVo.setIsNewUser(new Byte("0"));
        bannerList.add(bannerVo);
        return bannerList;
    }

    /**
     * 房间信息添加房主基本信息
     *
     * @param roomVo
     * @param usersDTO
     */
    public void fillUserToRoom(RoomVo roomVo, UsersDTO usersDTO) {
        roomVo.setErbanNo(usersDTO.getErbanNo());
        // roomVo.setAvatar(usersDTO.getAvatar());
        roomVo.setUserDescription(usersDTO.getUserDesc());
        roomVo.setNick(usersDTO.getNick());
        roomVo.setGender(usersDTO.getGender());
    }

    /**
     * 获取[优质推荐]房间列表
     *
     * @param uid UID
     * @return
     */
    public List<RoomVo> findHotRoom(Long uid) {
        String json = redisManager.get(RedisKey.home_hot_recom.getKey());
        return getRoomVoFromCache(uid, json, null, null);
    }

    /**
     * 获取绿色厅的列表
     */
    public List<RoomVo> getListGreenRoom(Long uid) {
        String json = redisManager.get(RedisKey.green_room_list.getKey());
        return getRoomVoFromCache(uid, json, null, null);
    }

    public List<RoomVo> getGreenRoom() {
        String json = redisManager.get(RedisKey.green_room_list.getKey());
        return gson.fromJson(json, new TypeToken<List<RoomVo>>() {
        }.getType());
    }

    /**
     * 获取首页[热门房间]列表(过滤已经在优质推荐的房间)
     *
     * @param uid      UID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return
     */
    public List<RoomVo> findHomeRoomList(Long uid, Integer pageNum, Integer pageSize) {
        String json = redisManager.get(RedisKey.home_room_list.getKey());
        return getRoomVoFromCache(uid, json, pageNum, pageSize);
    }

    /**
     * 获取首页新秀列表
     */
    public List<RoomVo> getListNewRoom(Long uid, Integer pageNum, Integer pageSize) {
        String json = redisManager.get(RedisKey.room_home_new.getKey());
        return getRoomVoFromCache(uid, json, pageNum, pageSize);
    }

    /**
     * 获取分类下热门列表
     */
    public List<RoomVo> getTagHotList(Long uid, Integer pageNum, Integer pageSize) {
        String json = redisManager.get(RedisKey.room_tag_hot.getKey());
        return getRoomVoFromCache(uid, json, pageNum, pageSize);
    }

    /**
     * 从缓存里获取房间信息
     *
     * @param uid
     * @param json
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<RoomVo> getRoomVoFromCache(Long uid, String json, Integer pageNum, Integer pageSize) {
        if (StringUtils.isNotBlank(json) && !"[]".equals(json)) {
            List<RoomVo> result;
            List<RoomVo> list = gson.fromJson(json, new TypeToken<List<RoomVo>>() {
            }.getType());
            if (uid != null && (uid.intValue() == 187707 || uid.intValue() == 103763) && list != null) {
                result = Lists.newArrayList();
                for (RoomVo roomVo : list) {
                    if (roomVo.getTitle() == null ||
                            (roomVo.getTitle().indexOf("游戏") == -1 && roomVo.getTitle().indexOf("相亲") == -1
                                    && roomVo.getTitle().indexOf("100") == -1 && roomVo.getTitle().indexOf("200") == -1
                                    && roomVo.getTitle().indexOf("艹") == -1 && roomVo.getTitle().indexOf("诱色") == -1)) {
                        result.add(roomVo);
                    }
                }
            } else {
                result = list;
            }
            if (pageNum == null || pageSize == null) {
                return result;
            }
            Integer size = result.size();
            Integer skip = (pageNum - 1) * pageSize;
            if (skip >= size) {
                return Lists.newArrayList();
            }
            if (skip + pageSize > size) {
                return result.subList(skip, result.size());
            }
            return result.subList(skip, skip + pageSize);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<RoomVo> findRoomByTag(Long uid, Long tagId, Integer pageNum, Integer pageSize, String os, String app,
                                      String appVersion, String clientIp, String channel) throws WebServiceException {
        List<RoomVo> list;
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        if (tagId == null || tagId < 1) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomTagDTO tagDTO = roomTagManager.getById(tagId);
        if (tagDTO == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (tagId == 9) {
            list = getListNewRoom(uid, pageNum, pageSize);
        } else {
            list = getRoomByTag(uid, tagId, pageNum, pageSize);
        }
        if (uid != null) {
            List<GeneralReviewWhitelist> generalReviewWhitelists =
                    reviewConfigService.getGeneralReviewWhitelistByList();
            if (generalReviewWhitelists.size() > 0) {
                //获取到需要过滤的用户uid
                boolean flag = generalReviewWhitelists.stream().anyMatch(item -> item.getUid().equals(uid));
                if (flag) {
                    return list;
                }
            }
            List<ReviewConfigDTO> reviewConfigDTOS = reviewConfigService.selectByCacheList();
            if (reviewConfigDTOS.size() > 0) {
                Set<String> removeTag = reviewConfigService.getRemoveTags(channel, uid, os, appVersion);
                if (removeTag != null && removeTag.size() > 0) {
                    list = list.stream().filter(item -> !removeTag.contains(item.getRoomTag())).collect(Collectors.toList());
                }
            }
        }
        logger.info("[reviewConfig] /home/v2/tagindex:{}", gson.toJson(list));
        return list;
    }

    @Override
    public List<RoomVo> listRoomByTagV3(Long uid, Long tagId, Integer pageNum, Integer pageSize, String os,
                                        String app, String appVersion, String clientIp) throws WebServiceException {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        if (tagId == null || tagId < 1) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (appVersionService.checkAuditingVersion(os, app, appVersion, clientIp, uid)) {
            // 只返回听歌的
            return getRoomByTag(uid, 4L, pageNum, pageSize);
        }

        RoomTagDTO tagDTO = roomTagManager.getById(tagId);
        if (tagDTO == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        List<RoomVo> list;
        if (tagId == 9) {
            list = getListNewRoom(uid, pageNum, pageSize);
        } else {
            list = getRoomByTag(uid, tagId, pageNum, pageSize);
        }
        return list;
    }

    /**
     * 根据tagId查询房间列表
     *
     * @param uid
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    private List<RoomVo> getRoomByTag(Long uid, Long tagId, Integer pageNum, Integer pageSize) throws WebServiceException {
        String result = redisManager.get(RedisKey.room_tag_index.getKey(tagId.toString()));
        return getRoomVoFromCache(uid, result, pageNum, pageSize);
    }

    // @Override
    // public List<RoomVo> getHot(Long uid, Integer pageNum, Integer pageSize, String os, String appVersion, String
    //         clientIp) throws WebServiceException {
    //     if (pageNum == null || pageNum < 1) pageNum = 1;
    //     if (pageSize == null || pageSize < 1 || pageSize > 50) pageSize = 10;
    //     // IOS新版本在审核期内的首页数据要做特殊处理
    //     if (appVersionService.checkAuditingVersion(os, appVersion, clientIp)) {
    //        /*if (pageNum > 1) {
    //            return Lists.newArrayList();
    //        }
    //        return getIOSHot();*/
    //         return Lists.newArrayList();
    //     }
    //     return getTagHotList(uid, pageNum, pageSize);
    // }

    @Override
    public List getOppositeSex(Long uid, String os, String app, String appVersion, String clientIp) throws WebServiceException {
        if (appVersionService.checkAuditingVersion(os, app, appVersion, clientIp, uid)) {
            // IOS新版本在审核期内的首页数据要做特殊处理
            return Lists.newArrayList();
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        String str = redisManager.hget(RedisKey.opposite_sex.getKey(), usersDTO == null ? "1" :
                usersDTO.getGender().toString());
        List<JSONObject> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            List<JSONObject> map = JSONObject.parseArray(str, JSONObject.class);
            Random random = new Random();
            int i = 0;
            while (list.size() < 14) {
                if (map.size() == 1) {
                    list.add(map.get(0));
                } else {
                    i = random.nextInt(map.size());
                    list.add(map.get(i));
                    map.remove(i);
                }
            }
        }
        return list;
    }

    @Override
    public List<RoomVo> getHomeHotRoom(Long uid, String appVersion, Integer pageNum, Integer pageSize, String appid) {
        String json = redisManager.get(RedisKey.home_hot_recom.getKey());
        List<RoomVo> list = getRoomVoFromCache(json, pageNum, pageSize);
        list = hideGivenRoom(uid, list);
        if (list == null || list.isEmpty()) {
            // TODO 添加默认的我要上推荐的H5链接
            return list;
        }
        list.stream().forEach(item -> {
            UsersRoomAttentionDTO usersRoomAttentionDTO = roomAttentionDao.selectRoomAttentionByUidAndRoomId(uid,
                    item.getRoomId());
            if (usersRoomAttentionDTO != null) {
                item.setStatus(1);
            } else {
                item.setStatus(0);
            }
        });
        return list;
    }

    /**
     * 隐藏指定房间内标题包含敏感字的房间
     *
     * @param uid        UID
     * @param roomVoList 房间列表
     * @return
     */
    public List<RoomVo> hideGivenRoom(Long uid, List<RoomVo> roomVoList) {
        if (uid != null && systemConf.getAuditAccountList().contains(uid.toString()) && roomVoList != null) {
            List<RoomVo> list = Lists.newArrayList();
            for (RoomVo roomVo : roomVoList) {
                // 判断房间标签是否包含"游戏", "相亲", "100", "200", "艹", "诱色"相关字符
                if (roomVo.getTitle() == null ||
                        (roomVo.getTitle().indexOf("游戏") == -1 && roomVo.getTitle().indexOf("相亲") == -1
                                && roomVo.getTitle().indexOf("100") == -1 && roomVo.getTitle().indexOf("200") == -1
                                && roomVo.getTitle().indexOf("艹") == -1 && roomVo.getTitle().indexOf("诱色") == -1)) {
                    list.add(roomVo);
                }
            }
            return list;
        }
        return roomVoList;
    }

    public List<RoomVo> getRoomVoFromCache(String json, Integer pageNum, Integer pageSize) {
        if (!StringUtils.isBlank(json) && !"[]".equals(json)) {
            List<RoomVo> list = gson.fromJson(json, new TypeToken<List<RoomVo>>() {
            }.getType());
            Integer size = list.size();
            Integer skip = (pageNum - 1) * pageSize;
            if (skip >= size) {
                return null;
            }
            if (skip + pageSize > size) {
                return list.subList(skip, list.size());
            }
            return list.subList(skip, skip + pageSize);
        }
        return null;
    }

    @Override
    public List<RoomVo> makeFriends(Long uid, Integer pageNum, Integer pageSize, String os, String app,
                                    String appVersion, String clientIp) throws WebServiceException {
        return Lists.newArrayList();
    }

    @Override
    public List<BannerDTO> getIndexTopBanner(Long uid, String os, String app) {
        return bannerService.findBannerListByTagId(uid, os, 1L);
    }

    @Override
    public List<BannerDTO> getDiscoverItem(Long uid, String os, String app) {
        return bannerService.findDiscoverItemList(uid, os, 2L);
    }

    @Override
    public List<IconDTO> getIndexHomeIcon(IndexParam indexParam, String clientIp) throws WebServiceException {
        boolean isCheck = false;
        if (appVersionService.checkAuditingVersion(indexParam.getOs(), indexParam.getAppid(),
                indexParam.getAppVersion(), clientIp, indexParam.getUid())) {
            isCheck = true;
        }
        return iconService.findIconList(isCheck, indexParam.getAppid(), indexParam.getAppVersion(), indexParam.getOs());
    }

    @Override
    public List<UserHotDTO> getBestCompanies(IndexParam indexParam, String clientIp) throws WebServiceException {
        String bestCompanies =
                redisManager.get(RedisKey.user_charm_best_companies.getKey() + "_" + indexParam.getPageNum() + "_"
                        + indexParam.getPageSize());
        if (StringUtils.isNotBlank(bestCompanies)) {
            List<UserHotDTO> list = gson.fromJson(bestCompanies, new TypeToken<List<UserHotDTO>>() {
            }.getType());
            for (UserHotDTO dto : list) {
                String roomStateCountKey = RedisKey.imxinpi_uid_map_roomid_key.name() + "_" + dto.getUid();
                Long roomStateCount = redisManager.zcount(roomStateCountKey, 0, Integer.MAX_VALUE);
                if (roomStateCount > 0) {
                    dto.setRoomState(1);
                } else {
                    dto.setRoomState(0);
                }
            }
            return list;
        } else {
            int pageNum = indexParam.getPageNum() > 0 ? (indexParam.getPageNum() - 1) * indexParam.getPageSize() : 0;
            Integer glamour = 0;//魅力值
            SysConfDTO sysConfDTO = sysConfManager.getSysConf(SysConfigId.define_glamour);
            if (sysConfDTO != null) {
                glamour = sysConfDTO.getConfigStatus() == 1 ? Utils.formatInt(sysConfDTO.getConfigValue()) : 0;
            }
            List<UserHotDTO> usersHotList = usersDao.queryCharmCompanies(glamour, indexParam.getGender(), pageNum,
                    indexParam.getPageSize());
            for (UserHotDTO dto : usersHotList) {
                String roomStateCountKey = RedisKey.imxinpi_uid_map_roomid_key.name() + "_" + dto.getUid();
                Long roomStateCount = redisManager.zcount(roomStateCountKey, 0, Integer.MAX_VALUE);
                if (roomStateCount > 0) {
                    dto.setRoomState(1);
                } else {
                    dto.setRoomState(0);
                }
            }
            redisManager.set(RedisKey.user_charm_best_companies.getKey() + "_" + indexParam.getPageSize(),
                    gson.toJson(usersHotList), 120, TimeUnit.SECONDS);
            return usersHotList;
        }
    }

    @Override
    public List<UserHotDTO> getNewUsers(IndexParam indexParam, String clientIp) throws WebServiceException {
        String newUsers = redisManager.get(RedisKey.user_charm_new_user.getKey() + "_" + indexParam.getPageNum() + "_"
                + indexParam.getPageSize());
        if (StringUtils.isNotBlank(newUsers)) {
            List<UserHotDTO> list = gson.fromJson(newUsers, new TypeToken<List<UserHotDTO>>() {
            }.getType());
            for (UserHotDTO userHotDTO : list) {
                int experLevel = levelManager.getUserExperienceLevelSeq(userHotDTO.getUid());
                userHotDTO.setExperLevel(experLevel);//财富等级值
                String roomStateCountKey = RedisKey.imxinpi_uid_map_roomid_key.name() + "_" + userHotDTO.getUid();
                Long roomStateCount = redisManager.zcount(roomStateCountKey, 0, Integer.MAX_VALUE);
                if (roomStateCount > 0) {
                    userHotDTO.setRoomState(1);
                } else {
                    userHotDTO.setRoomState(0);
                }
            }
            return list;
        } else {
            int pageNum = indexParam.getPageNum() > 0 ? (indexParam.getPageNum() - 1) * indexParam.getPageSize() : 0;
            List<UserHotDTO> usersHotList = usersDao.queryNewUsers(pageNum, indexParam.getPageSize());
            for (UserHotDTO userHotDTO : usersHotList) {
                int experLevel = levelManager.getUserExperienceLevelSeq(userHotDTO.getUid());
                userHotDTO.setExperLevel(experLevel);//财富等级值
                String roomStateCountKey = RedisKey.imxinpi_uid_map_roomid_key.name() + "_" + userHotDTO.getUid();
                Long roomStateCount = redisManager.zcount(roomStateCountKey, 0, Integer.MAX_VALUE);
                if (roomStateCount > 0) {
                    userHotDTO.setRoomState(1);
                } else {
                    userHotDTO.setRoomState(0);
                }
            }
            redisManager.set(RedisKey.user_charm_new_user.getKey(), gson.toJson(usersHotList), 120, TimeUnit.SECONDS);
            return usersHotList;
        }
    }

    @Override
    public TabBannerDTO getIndexTopTabBanner(Long uid, String os, String app) {
        TabBannerDTO tabBannerDTO = new TabBannerDTO();
        UsersDTO users = usersManager.getUser(uid);
        if (users == null || users.getCreateTime() == null) {
            return tabBannerDTO;
        }
        List<TopTabDTO> tabs = sysConfManager.getSysConfigByNameSpace(SysConfNameSpace.cust_home_tab.name(), 1);
        List<BannerDTO> bannerVos = bannerService.refreshValidateBannerList(os);
        boolean isNew = false;
        Date date = DateTimeUtils.getLastDay(new Date(), 3);
        if (users.getCreateTime().getTime() - date.getTime() > 0) {
            isNew = true;
        }
        for (TopTabDTO tab : tabs) {
            List<BannerDTO> banners = Lists.newArrayList();
            for (BannerDTO bannerVo : bannerVos) {
                if (StringUtils.equals(tab.getTabValue(), bannerVo.getViewType().toString())) {
                    if (isNew && (bannerVo.getIsNewUser().intValue() == 0 || bannerVo.getIsNewUser().intValue() == 1)) {
                        banners.add(bannerVo);
                    } else if (!isNew && (bannerVo.getIsNewUser().intValue() == 0 || bannerVo.getIsNewUser().intValue() == 2)) {
                        banners.add(bannerVo);
                    }
                }
            }
            tab.setBanners(banners);
        }
        SysConfDTO iconConf = sysConfManager.getSysConf(SysConfigId.open_room_icon_status);
        if (iconConf != null) {
            tabBannerDTO.setIconStatus(iconConf.getConfigStatus().intValue());
        } else {
            tabBannerDTO.setIconStatus(2);
        }
        tabBannerDTO.setTabs(tabs);
        return tabBannerDTO;
    }
}
