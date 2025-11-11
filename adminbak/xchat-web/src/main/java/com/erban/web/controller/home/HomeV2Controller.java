package com.erban.web.controller.home;

import com.alibaba.fastjson.JSON;
import com.erban.main.model.Room;
import com.erban.main.model.RoomTag;
import com.erban.main.model.Users;
import com.erban.main.service.*;
import com.erban.main.service.home.HomeV2Service;
import com.erban.main.service.icon.IconService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.room.RoomTagService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.*;
import com.erban.web.common.BaseController;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/home/v2")
public class HomeV2Controller extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(HomeV2Controller.class);
    @Autowired
    private UsersService usersService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private BannerService bannerService;
    @Autowired
    private RankService rankService;
    @Autowired
    private IconService iconService;
    @Autowired
    private RoomTagService roomTagService;
    @Autowired
    private HomeV2Service homeV2Service;
    @Autowired
    private JedisService jedisService;
    @Autowired
    AppVersionService appVersionService;
    @Autowired
    private RoomService roomService;
    private Gson gson = new Gson();


    /**
     * 获取首页的数据
     * 首页数据包括：banner、排行榜、房间列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotindex")
    public BusiResult getHotHome(Long uid, String os, String appVersion, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        logger.info("调用接口(/home/v2/hotindex),首页数据-热门，请求入参：os:{},appVersion:{},pageNum:{},pageSize:{}", os, appVersion, pageNum, pageSize);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            // IOS新版本在审核期内的首页数据要做特殊处理
            if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion, request)) {
                if (pageNum == null || pageNum > 1) {
                    return new BusiResult(BusiStatus.SUCCESS);
                }
                return homeService.getIOSAuditHomeDataV3(appVersion);
            }

            if (pageNum == null || pageNum < 1) pageNum = 1;
            if (pageSize == null || pageSize < 1) pageSize = Constant.HOME_PAGE_SIZE;

            HomeV2Vo homeV2Vo = new HomeV2Vo();
            List<RoomVo> listRoom = new ArrayList<>();
            List<RoomVo> roomVoList;
            // 只有请求第一页的数据才返回banner、icon和排行榜数据
            if (pageNum == 1) {
                List<BannerVo> bannerList = bannerService.getBannerList(uid, os);
                if (bannerList == null) bannerList = Lists.newArrayList();

                List<IconVo> iconVoList = iconService.getIcon(false, appVersion);
                if (iconVoList == null) iconVoList = Lists.newArrayList();

                RankHomeVo rankHomeVo = rankService.getRankHomeDataOnlyCache();
                if (rankHomeVo == null) {
                    rankHomeVo = new RankHomeVo();
                }
                homeV2Vo.setBanners(bannerList);
                homeV2Vo.setHomeIcons(iconVoList);
                homeV2Vo.setRankHome(rankHomeVo);
                homeV2Vo.setListGreenRoom(homeV2Service.getGreenRoom());
                homeV2Vo.setRoomTagList(roomTagService.getSearchTags());
                Integer version = 103;
                if (StringUtils.isNotBlank(appVersion)) {
                    version = Integer.valueOf(appVersion.replaceAll("\\.", ""));
                }
                if (version < 103) {
                    roomVoList = homeV2Service.hideGivenRoom(uid, homeV2Service.getHomeHotRoom(uid, 1, 100));
                    if ("ios".equalsIgnoreCase(os)) {
                        homeV2Vo.setHotRooms(roomVoList);
                    } else {
                        if (roomVoList != null) {
                            listRoom.addAll(roomVoList);
                        }
                    }
                }
            }
            roomVoList = homeV2Service.getHomeRoomList(uid, pageNum, pageSize);
            if (roomVoList != null) {
                listRoom.addAll(roomVoList);
            }
            homeV2Vo.setListRoom(listRoom);
            busiResult.setData(homeV2Vo);
        } catch (Exception e) {
            logger.error("getHotHome error, os:" + os + ",appVersion:" + appVersion + ",pageNum:" + pageNum + ",pageSize:" + pageSize, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("首页数据-热门(/home/v2/hotindex),接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 获取标签页内的房间数据
     *
     * @param tagId    标签ID
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/tagindex")
    public BusiResult getTagHome(Long uid, Integer tagId, Integer pageNum, Integer pageSize, String os, String appVersion, HttpServletRequest request) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            if (pageNum == null || pageNum < 1) pageNum = 1;
            if (pageSize == null || pageSize < 1) pageSize = Constant.ROOM_PAGE_SIZE;
            // IOS新版本在审核期内的首页数据要做特殊处理
            if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion, request)) {
                if (pageNum != 1) {
                    return busiResult;
                }
                List<Long> auditAccountListUid = Constant.IOSAuditAccount.auditAccountList;
                List<RoomVo> list = com.beust.jcommander.internal.Lists.newArrayList();
                Room room;
                for (Long auditUid : auditAccountListUid) {
                    RoomVo roomVo = roomService.getRoomVoWithUsersByUid(auditUid);
                    if (roomVo != null && roomVo.getValid()) {
                        room = roomService.getRoomByUid(roomVo.getUid());
                        roomVo.setOnlineNum((room.getOnlineNum() == null ? 1 : room.getOnlineNum()) + (roomVo.getFactor() == null ? 0 : roomVo.getFactor()));
                        list.add(roomVo);
                        if (list.size() == 2) break;
                    }
                }
                busiResult.setData(list);
                return busiResult;
            }
            if (tagId == null || tagId < 1) {
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            RoomTag roomTag = roomTagService.getRoomTagById(tagId);
            if (roomTag == null) {
                return new BusiResult(BusiStatus.NOTEXISTS);
            }
            if (tagId == 9) {
                busiResult.setData(homeV2Service.getNewRoomList(uid, pageNum, pageSize));
            } else {
                busiResult.setData(homeV2Service.getRoomByTag(uid, tagId, pageNum, pageSize));
            }
        } catch (Exception e) {
            logger.error("getTagHome error ,tagId:" + tagId + ", pageNum:" + pageNum + ",pageSize:" + pageSize, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return busiResult;
    }

    /**
     * 手工刷新流水，用于测试
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/refreshflow")
    public BusiResult refreshFlow() {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        homeService.genPeriodData();
        return result;
    }

    /**
     * 首页热门推荐
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getindex")
    public BusiResult getList(Long uid, Integer pageNum, Integer pageSize, String os, String appVersion, HttpServletRequest request) {
        // IOS新版本在审核期内的首页数据要做特殊处理
        if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion, request)) {
            return new BusiResult(BusiStatus.SUCCESS, Lists.newArrayList());
        }
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        return new BusiResult(BusiStatus.SUCCESS, homeV2Service.getHomeHotRoom(uid, pageNum, pageSize));
    }

    /**
     * 首页获取最近1周异性列表
     */
    @RequestMapping("/getOppositeSex")
    @ResponseBody
    public BusiResult getOppositeSex(Long uid, String os, String appVersion, HttpServletRequest request) {
        // IOS新版本在审核期内的首页数据要做特殊处理
        if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion, request)) {
            return new BusiResult(BusiStatus.SUCCESS, new ArrayList<>());
        }
        Users users = usersService.getUsersByUid(uid);
        String str = jedisService.hget(RedisKey.opposite_sex.getKey(), users == null ? "1" : users.getGender().toString());
        List<Map<String, Object>> list = new ArrayList<>();
        if (StringUtils.isNotBlank(str)) {
            List<Map<String, Object>> map = gson.fromJson(str, list.getClass());
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
        return new BusiResult(BusiStatus.SUCCESS, list);
    }

}
