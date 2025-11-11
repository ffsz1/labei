package com.juxiao.xchat.service.api.room.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.IOSData;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.RoomAttentionDao;
import com.juxiao.xchat.dao.room.RoomDao;
import com.juxiao.xchat.dao.room.RoomInfoDao;
import com.juxiao.xchat.dao.room.domain.RoomBgDO;
import com.juxiao.xchat.dao.room.domain.RoomDO;
import com.juxiao.xchat.dao.room.domain.RoomInfoDO;
import com.juxiao.xchat.dao.room.dto.*;
import com.juxiao.xchat.dao.room.enumeration.RoomOptStatus;
import com.juxiao.xchat.dao.room.enumeration.RoomUserAtt;
import com.juxiao.xchat.dao.sysconf.NetEaseChatroomDao;
import com.juxiao.xchat.dao.user.FansDao;
import com.juxiao.xchat.dao.user.domain.AccountBlock;
import com.juxiao.xchat.dao.user.dto.FansFollowDTO;
import com.juxiao.xchat.dao.user.dto.UserSettingDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.user.query.UserFansQuery;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.RoomTagManager;
import com.juxiao.xchat.manager.common.room.vo.RoomRecommendVo;
import com.juxiao.xchat.manager.common.room.vo.RunningRoomVo;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.sysconf.GeneralManager;
import com.juxiao.xchat.manager.common.user.UserRealNameManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Body;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.Payload;
import com.juxiao.xchat.manager.external.netease.conf.RoomMic;
import com.juxiao.xchat.manager.external.qiniu.QiniuManager;
import com.juxiao.xchat.service.api.room.RoomBgService;
import com.juxiao.xchat.service.api.room.RoomMicService;
import com.juxiao.xchat.service.api.room.RoomService;
import com.juxiao.xchat.service.api.room.bo.RoomAdminParamBO;
import com.juxiao.xchat.service.api.room.bo.RoomParamBO;
import com.juxiao.xchat.service.api.room.vo.ChatInfoVO;
import com.juxiao.xchat.service.api.room.vo.RoomNotifyVo;
import com.juxiao.xchat.service.api.sysconf.SensitiveWordService;
import com.juxiao.xchat.service.api.sysconf.enumeration.SensitiveWordEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class RoomServiceImpl implements RoomService {
    private static final String DEFAULT_HEAD = "/default_head.png";

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final Random random = new Random();

    private final List<RoomLinkVo> defaultRoomLinks = Lists.newArrayList();
    /**
     * 1秒1500并发
     */
    private final RateLimiter ROOM_SEARCH_LIMITER = RateLimiter.create(1500.0);

    @Value("common.system.jpegPrefix")
    private String jpegPrefix;
    @Autowired
    private Gson gson;
    @Autowired
    private SystemConf systemConf;

    @Autowired
    private FansDao fansDao;
    @Autowired
    private NetEaseChatroomDao netEaseChatroomDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomAttentionDao roomAttentionDao;
    @Autowired
    private RoomInfoDao roomInfoDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;

    @Autowired
    private ImRoomManager imroomManager;

    @Autowired
    private QiniuManager qiniuManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RoomTagManager roomTagManager;
    @Autowired
    private RoomMicService roomMicService;
    @Autowired
    private RoomBgService roomBgService;
    @Autowired
    private SensitiveWordService sensitiveWordService;
    @Value("${common.system.officialRoomId}")
    private Long officialRoomId;

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private UserRealNameManager userRealNameManager;

    @Autowired
    private AppVersionManager appVersionManager;

    @Override
    public void roomMicDown(Long roomId, Long uid) throws WebServiceException {

    }

    @Override
    public WebServiceMessage openRoom(RoomParamBO room) throws Exception {
        if (room == null || room.getUid() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDTO = usersManager.getUser(room.getUid());
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        userRealNameManager.verifyUserRealName(room.getUid(), "openroom");

        // 图片迁移到七牛空间
        room = moveToQinniu(room);
        // 查询是否存在该房间
        RoomDTO roomDto = this.getRoomByUid(room.getUid());
        if (roomDto == null) {
            // 第一次开房, 如果开房失败会抛异常
            roomDto = createRoom(room);

        } else {
            // 房间已经存在,判断房间是否有效
            if (roomDto.getValid() != null && roomDto.getValid()) {
                return new WebServiceMessage(WebServiceCode.ROOM_RUNNING.getValue(), convertRoomToVo(roomDto), "房间在运行中");
            }
            // 重新开房--更新房间状态
            roomDto = reopenRoom(roomDto);
        }
        // 写入正在运行的房间的缓存
        writerRunningRoomVo(roomDto, 1);
        return WebServiceMessage.success(convertRoomToVo(roomDto));
    }

    @Override
    public RoomDTO getRoomByUid(Long uid) {
        if (uid == null) {
            return null;
        }
        return roomManager.getUserRoom(uid);
    }

    /**
     * 添加房间背景
     *
     * @param roomDto
     */
    private void addRoomBg(RoomDTO roomDto) throws Exception {
        if (roomDto != null && StringUtils.isNotBlank(roomDto.getBackPic()) && StringUtils.isNumeric(roomDto.getBackPic())) {
            RoomBgDO roomBgDO = roomBgService.getById(roomDto.getBackPic(), roomDto.getRoomId());
            if (roomBgDO == null) {
                roomDto.setBackPicUrl("");
                roomDto.setBackPic("0");
                updateRoom(roomDto);
            } else {
                roomDto.setBackPicUrl(roomBgDO.getPicUrl());
                // 判断是否过期
                if ("2".equals(roomBgDO.getStatus()) && roomBgDO.getBeginDate() != null && roomBgDO.getEndDate() != null) {
                    long time = System.currentTimeMillis();
                    if (time > roomBgDO.getEndDate().getTime() || time < roomBgDO.getBeginDate().getTime()) {
                        logger.info("[房间背景] 背景过期,backPic:{},name:{}", roomBgDO.getId(), roomBgDO.getName());
                        redisManager.hdel(RedisKey.room_background.getKey(), roomDto.getBackPic());
                        roomDto.setBackPicUrl("");
                        roomDto.setBackPic("0");
                        updateRoom(roomDto);
                    }
                }
            }
        }
    }

    private String getRoomBgUrl(String bgId, Long roomId) {
        if (StringUtils.isEmpty(bgId)) {
            return "";
        }
        RoomBgDO roomBgDO = roomBgService.getById(bgId, roomId);
        if (roomBgDO == null) {
            return "";
        } else {
            // 判断是否过期
            if ("2".equals(roomBgDO.getStatus()) && roomBgDO.getBeginDate() != null && roomBgDO.getEndDate() != null) {
                long time = System.currentTimeMillis();
                if (time > roomBgDO.getEndDate().getTime() || time < roomBgDO.getBeginDate().getTime()) {
                    redisManager.hdel(RedisKey.room_background.getKey(), bgId);
                    return "";
                }
            }
            return roomBgDO.getPicUrl();
        }


    }

    /**
     * 更新房间信息
     *
     * @param roomDto
     */
    public void updateRoom(RoomDTO roomDto) throws Exception {
        RoomDO roomDO = new RoomDO();
        BeanUtils.copyProperties(roomDto, roomDO);
        // 保存信息
        roomDto = roomManager.updateRoomInfo(roomDO);
        UsersDTO usersDto = usersManager.getUser(roomDto.getUid());
        roomDto.setErbanNo(usersDto == null ? null : usersDto.getErbanNo());
        roomDto.setGiftDrawEnable(1);
        roomDto.setFaceType(roomDto.getFaceType());
        roomDto.setHideFace(getHideFace(roomDto.getFaceType()));
        if (systemConf.getAuditAccountList().contains(roomDto.getUid())) {
            roomDto.setGiftDrawEnable(2);
        }
        roomDto.setGiftCardSwitch(roomDto.getGiftCardSwitch());
        roomDto.setBackPic(roomDto.getBackPic());
        roomDto.setBackPicUrl(getRoomBgUrl(roomDto.getBackPic(), roomDto.getRoomId()));
        logger.info("updateRoom -> pic:{},roomId:{}", roomDto.getBackPicUrl(), roomDto.getRoomId());
        imroomManager.updateRoom(roomDto);
    }

    @Override
    public RoomUserinDTO getRoom(Long uid, Long visitorUid, String os, String app, String appVersion, String ip) throws Exception {
        if (uid == null) {
            return null;
        }

        if (uid.equals(visitorUid)) {
            userRealNameManager.verifyUserRealName(uid, "openroom");
        }

        // 判断进入房间的用户是否补全资料
        if (visitorUid != null) {
            UsersDTO usersDTO = usersManager.getUser(visitorUid);
            if (usersDTO == null) {
                throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
            }
            // 查询账号是否被封禁
            String cache = redisManager.hget(RedisKey.block_account.getKey(), usersDTO.getErbanNo().toString());
            if (cache != null) {
                AccountBlock accountBlock = gson.fromJson(cache, AccountBlock.class);
                if (usersDTO.getErbanNo().toString().equals(accountBlock.getErbanNo().toString())) {
                    String status = accountBlock.getBlockStatus().toString();
                    Date currentTime = new Date();
                    boolean betweenDate = DateTimeUtils.isBetweenDate(currentTime, accountBlock.getBlockStartTime(), accountBlock.getBlockEndTime());
                    if (betweenDate && "1".equals(status)) {
                        throw new WebServiceException(WebServiceCode.SIGN_AUTHORITY);
                    }
                }
            }
        }
        RoomDTO roomDTO = getRoomByUid(uid);
        if (roomDTO == null) {
            return null;
        }
        addRoomBg(roomDTO);
        RoomUserinDTO roomUserinDTO = convertRoomToVo(roomDTO);
        if (roomUserinDTO.getAudioLevel() == null) {
            roomUserinDTO.setAudioLevel(0);
        }
        try {
            UsersDTO usersDTO = usersManager.getUser(uid);
            roomDTO.setErbanNo(usersDTO.getErbanNo());
            updateRoom(roomDTO);
        } catch (Exception e) {
            logger.error("执行getRoom操作更新IM中房间信息出现异常,异常信息:{}", e);
        }
        logger.info("getRoom -> roomUserinDTO:{}", gson.toJson(roomUserinDTO));
        return roomUserinDTO;
    }

    @Override
    public RoomDTO getRoomByRoomId(Long roomId) throws WebServiceException {
        if (roomId == null && roomId == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO roomDto = roomManager.getRoom(roomId);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        RoomBgDO roomBgDO = roomBgService.getById(roomDto.getBackPic(), roomDto.getRoomId());
        roomDto.setBackPicUrl(roomBgDO == null ? "" : roomBgDO.getPicUrl());
        roomDto.setHideFace(getHideFace(roomDto.getFaceType()));
        return roomDto;
    }

    @Override
    public RoomUserinDTO updateRoomByRunning(RoomParamBO paramBO) throws Exception {
        if (paramBO == null || paramBO.getUid() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        RoomDTO roomDto = getRoomByUid(paramBO.getUid());
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        if (hasSensitiveWords(paramBO.getRoomDesc()) || hasSensitiveWords(paramBO.getRoomNotice()) || hasSensitiveWords(paramBO.getTitle())) {
            throw new WebServiceException(WebServiceCode.ROOM_WORDS);
        }
        if (hasSensitiveByPlayInfo(paramBO.getPlayInfo())) {
            // 检查房间玩法介绍是否包含敏感词
            throw new WebServiceException(WebServiceCode.ROOM_WORDS);
        }

        if (!imroomManager.isRoomManager(roomDto.getRoomId(), roomDto.getUid())) {
            // 管理员信息验证
            throw new WebServiceException(WebServiceCode.ROOM_NO_AUTHORITY);
        }

        if (StringUtils.isNotBlank(paramBO.getTitle())) {
            String title = URLDecoder.decode(paramBO.getTitle(), "utf-8");
            if (!roomDto.getTitle().equalsIgnoreCase(title)) {
                if (generalManager.checkProhibitModification()) {
                    throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                }
            }
        }

        if (StringUtils.isNotBlank(paramBO.getRoomNotice())) {
            String notice = URLDecoder.decode(paramBO.getRoomNotice(), "utf-8");
            if (StringUtils.isNotBlank(roomDto.getRoomNotice())) {
                if (!roomDto.getRoomNotice().equalsIgnoreCase(notice)) {
                    if (generalManager.checkProhibitModification()) {
                        throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(paramBO.getRoomDesc())) {
            String desc = URLDecoder.decode(paramBO.getRoomDesc(), "utf-8");
            if (StringUtils.isNotBlank(roomDto.getRoomDesc())) {
                if (!roomDto.getRoomDesc().equalsIgnoreCase(desc)) {
                    if (generalManager.checkProhibitModification()) {
                        throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                    }
                }
            }
        }


        RoomDO roomDo = new RoomDO();
        roomDo.setUid(paramBO.getUid());
        BeanUtils.copyProperties(paramBO, roomDo);
        roomDo.setUpdateTime(new Date());
        roomDo.setValid(null);    // 不能更新该字段
        roomDo.setOperatorStatus(null);
        roomDo.setType(null);
        roomDo.setBadge(null);
        if ((roomDto.getTagId() == null || roomDto.getTagId() <= 0 || roomDto.getTagId() == 8) && StringUtils.isBlank(roomDto.getRoomTag())) {
            logger.error("tagNull updateRoomByRunning>:{} before", gson.toJson(roomDto));
        }

        if (paramBO.getTagId() == null) {
            roomDo.setTagId(roomDto.getTagId());
            roomDo.setTagPict(roomDto.getTagPict());
            roomDo.setRoomTag(roomDto.getRoomTag());
        }
        if ((roomDto.getTagId() == null || roomDto.getTagId() <= 0 || roomDto.getTagId() == 8) && StringUtils.isBlank(roomDto.getRoomTag())) {
            logger.error("tagNull updateRoomByRunning>:{} after", gson.toJson(roomDto));
        }

        // 填充标签
        this.fillTagInfo(roomDo);
        saveRoomPlay(roomDo.getUid(), paramBO.getPlayInfo());
        // 保存信息
        roomDo.setHideFace(getHideFace(roomDto.getFaceType()));
        roomDo.setGiftCardSwitch(paramBO.getGiftCardSwitch());
        roomDto = roomManager.updateRoomInfo(roomDo);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        RoomBgDO roomBgDO = roomBgService.getById(roomDto.getBackPic(), roomDto.getRoomId());
        roomDto.setBackPicUrl(roomBgDO == null ? "" : roomBgDO.getPicUrl());

        RoomNotifyVo roomNotifyVo = new RoomNotifyVo();
        roomNotifyVo.setRoomInfo(gson.toJson(roomDto));
        roomNotifyVo.setType(RoomMic.ROOM_NOTIFY_TYPE_ROOM);

        UsersDTO usersDto = usersManager.getUser(roomDto.getUid());
        roomDto.setErbanNo(usersDto == null ? null : usersDto.getErbanNo());
        if (systemConf.getAuditAccountList().contains(roomDto.getUid())) {
            roomDto.setGiftDrawEnable(2);
        } else {
            roomDto.setGiftDrawEnable(1);
        }
        roomDto.setGiftCardSwitch(roomDto.getGiftCardSwitch());
        imroomManager.updateRoom(roomDto);
        RoomUserinDTO dto = convertRoomToVo(roomDto);
        return dto;
    }

    /**
     * 检查玩法介绍是否包含敏感词
     *
     * @param playInfo
     */
    public boolean hasSensitiveByPlayInfo(String playInfo) {
        if (StringUtils.isBlank(playInfo)) {
            return false;
        }
        // 查询敏感词
        String sensitiveWords = sensitiveWordService.getWords(SensitiveWordEnum.room_play_info);
        if (StringUtils.isBlank(sensitiveWords)) {
            return false;
        }
        return Pattern.compile(".*(" + sensitiveWords + ").*").matcher(playInfo).matches();
    }

    /**
     * 更新房间玩法信息
     *
     * @param roomUid  房主ID
     * @param playInfo 玩法信息
     */
    public void saveRoomPlay(Long roomUid, String playInfo) throws WebServiceException {
        RoomInfoDO roomInfoDO = roomInfoDao.selectByRoomUid(roomUid);
        if (StringUtils.isNotBlank(playInfo)) {
            if (roomInfoDO != null) {
                if (!playInfo.equalsIgnoreCase(roomInfoDO.getPlayInfo())) {
                    if (generalManager.checkProhibitModification()) {
                        throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                    }
                }
            } else {
                if (generalManager.checkProhibitModification()) {
                    throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                }
            }

        }

        if (playInfo != null) {
            // 更新房间玩法信息
            RoomInfoDO infoDO = new RoomInfoDO();
            infoDO.setRoomUid(roomUid);
            infoDO.setPlayInfo(playInfo);
            roomInfoDao.saveOrUpdate(infoDO);
        }
    }

    @Override
    public String replaceSensitiveWords(String str, String replace) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String sensitiveWords = sensitiveWordService.getWords(SensitiveWordEnum.room_title);
        if (StringUtils.isBlank(sensitiveWords)) {
            return str;
        }
        if (StringUtils.isBlank(replace)) {
            replace = "*";
        }
        return str.replaceAll("(?:" + sensitiveWords + ")", replace);
    }

    @Override
    public boolean hasSensitiveWords(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        String sensitiveWords = sensitiveWordService.getWords(SensitiveWordEnum.room_title);
        if (StringUtils.isBlank(sensitiveWords)) {
            return false;
        }
        return Pattern.compile(".*(" + sensitiveWords + ").*").matcher(str).matches();
    }

    @Override
    public RoomUserinDTO updateRoomByAdmin(RoomAdminParamBO paramBO) throws Exception {
        if (paramBO == null || paramBO.getRoomUid() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        // 权限校验
        RoomDTO roomDto = getRoomByUid(paramBO.getRoomUid());
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        if (hasSensitiveWords(paramBO.getRoomDesc()) || hasSensitiveWords(paramBO.getRoomNotice()) || hasSensitiveWords(paramBO.getTitle())) {
            throw new WebServiceException(WebServiceCode.ROOM_WORDS);
        }
        if (hasSensitiveByPlayInfo(paramBO.getPlayInfo())) {
            // 检查房间玩法介绍是否包含敏感词
            throw new WebServiceException(WebServiceCode.ROOM_WORDS);
        }
        if (!imroomManager.isRoomManager(roomDto.getRoomId(), roomDto.getUid())) {
            // 管理员信息验证
            throw new WebServiceException(WebServiceCode.ROOM_NO_AUTHORITY);
        }

        if (StringUtils.isNotBlank(paramBO.getTitle())) {
            String title = URLDecoder.decode(paramBO.getTitle(), "utf-8");
            if (!roomDto.getTitle().equalsIgnoreCase(title)) {
                if (generalManager.checkProhibitModification()) {
                    throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                }
            }
        }

        if (StringUtils.isNotBlank(paramBO.getRoomNotice())) {
            String notice = URLDecoder.decode(paramBO.getRoomNotice(), "utf-8");
            if (StringUtils.isNotBlank(roomDto.getRoomNotice())) {
                if (!roomDto.getRoomNotice().equalsIgnoreCase(notice)) {
                    if (generalManager.checkProhibitModification()) {
                        throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(paramBO.getRoomDesc())) {
            String desc = URLDecoder.decode(paramBO.getRoomDesc(), "utf-8");
            if (StringUtils.isNotBlank(roomDto.getRoomDesc())) {
                if (!roomDto.getRoomDesc().equalsIgnoreCase(desc)) {
                    if (generalManager.checkProhibitModification()) {
                        throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
                    }
                }
            }
        }


        RoomDO roomDo = new RoomDO();
        BeanUtils.copyProperties(paramBO, roomDo);
        roomDo.setUid(paramBO.getRoomUid());
        // 不能更新该字段
        roomDo.setValid(null);
        roomDo.setOperatorStatus(null);
        roomDo.setType(null);
        roomDo.setBadge(null);
        roomDo.setUpdateTime(new Date());

        // 客户端现在不开放修改房间标签功能
        // IOS 关闭礼物特效和房间座驾 传null导致房间标签修改
        roomDo.setTagId(roomDto.getTagId());

        if ((roomDo.getTagId() == null || roomDo.getTagId() <= 0 || roomDo.getTagId() == 8) && StringUtils.isBlank(roomDo.getRoomTag())) {
            logger.error("tagNull updateRoomByAdmin>:{}",gson.toJson(roomDo));
        }

        // 填充标签
        this.fillTagInfo(roomDo);

        saveRoomPlay(roomDo.getUid(), paramBO.getPlayInfo());
        // 保存信息
        roomDo.setHideFace(getHideFace(roomDto.getFaceType()));
        roomDo.setGiftCardSwitch(paramBO.getGiftCardSwitch());
        roomDto = roomManager.updateRoomInfo(roomDo);
        if (roomDto != null) {
            RoomBgDO roomBgDO = roomBgService.getById(roomDto.getBackPic(), roomDto.getRoomId());
            roomDto.setBackPicUrl(roomBgDO == null ? "" : roomBgDO.getPicUrl());
        }
        RoomNotifyVo roomNotifyVo = new RoomNotifyVo();
        roomNotifyVo.setRoomInfo(gson.toJson(roomDto));
        roomNotifyVo.setType(RoomMic.ROOM_NOTIFY_TYPE_ROOM);

        // 更新云信消息
        UsersDTO usersDto = usersManager.getUser(roomDto.getUid());
        roomDto.setErbanNo(usersDto == null ? null : usersDto.getErbanNo());
        roomDto.setGiftDrawEnable(1);
        if (systemConf.getAuditAccountList().contains(usersDto.getUid())) {
            roomDto.setGiftDrawEnable(2);
        } else {
            roomDto.setGiftDrawEnable(1);
        }
        roomDto.setGiftCardSwitch(roomDto.getGiftCardSwitch());
        imroomManager.updateRoom(roomDto);
        return convertRoomToVo(roomDto);
    }

    @Override
    public List<RoomSearchDTO> search(String key, List<Long> uids) throws WebServiceException {
        if (!ROOM_SEARCH_LIMITER.tryAcquire(5, TimeUnit.SECONDS)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        return roomDao.listSearchRoom(key, uids);
    }

    /**
     * 房间信息已存在, 更新房间状态
     *
     * @param roomDTO 房间信息
     * @return 房间信息
     */
    private RoomDTO reopenRoom(RoomDTO roomDTO) throws WebServiceException {
        if ((roomDTO.getTagId() == null || roomDTO.getTagId() <= 0 || roomDTO.getTagId() == 8) && StringUtils.isBlank(roomDTO.getRoomTag())) {
            logger.error("tagNull reopenRoom>:{}", gson.toJson(roomDTO));
        }

        // 查询用户信息
        UsersDTO usersDTO = usersManager.getUser(roomDTO.getUid());
        Date date = new Date();
        // 切换云信房间的状态,
        roomDTO.setValid(true);
        roomDTO.setAbChannelType(usersDTO.getChannelType());
        roomDTO.setAvatar(usersDTO.getAvatar());
        roomDTO.setOperatorStatus(RoomOptStatus.in.getStatus());
        roomDTO.setUpdateTime(date);
        roomDTO.setOpenTime(date);
        roomDTO.setMeetingName(UUIDUtils.get());
        roomDTO.setTagId(roomDTO.getTagId());
        roomDTO.setRoomTag(roomDTO.getRoomTag());
        roomDTO.setErbanNo(usersDTO.getErbanNo());
        RoomDO roomDo = new RoomDO();
        BeanUtils.copyProperties(roomDTO, roomDo);
        roomDo.setHideFace(getHideFace(roomDTO.getFaceType()));
        roomDTO = roomManager.updateRoomInfo(roomDo);
        return roomDTO;
    }


    /**
     * 写到缓存中
     */
    public void writerRunningRoomVo(RoomDTO roomDto, int onlineNum) {
        RunningRoomVo runningRoomVo = new RunningRoomVo();
        runningRoomVo.setOnlineNum(onlineNum);
        runningRoomVo.setUid(roomDto.getUid());
        runningRoomVo.setType(roomDto.getType());
        runningRoomVo.setRoomId(roomDto.getRoomId());
        redisManager.hset(RedisKey.room_running.getKey(), roomDto.getUid().toString(), gson.toJson(runningRoomVo));
        redisManager.hdel(RedisKey.room_permit_hide.getKey(), roomDto.getUid().toString());
    }

    /**
     * 在开通房间的时候, 给关注这个用户的的用户发送消息
     *
     * @param usersDto 开通房间的人
     */
    @Async
    @Override
    public void sendOpenRoomNoticeToFollowers(UsersDTO usersDto) {
        // 查询粉丝
        UserFansQuery query = new UserFansQuery();
        query.setLikedUid(usersDto.getUid());
        query.setPage(1, 500);

        List<FansFollowDTO> fans = fansDao.listFollowOrFans(query);
        List<String> fansList = new ArrayList<>(500);

        fans.forEach(followDto -> {
            UserSettingDTO settingDto = usersManager.getUserSetting(followDto.getUid());
            if (settingDto != null && settingDto.getLikedSend() == 2) {
                return;
            }
            fansList.add(String.valueOf(followDto.getUid()));
        });

        if (fansList.size() == 0) {
            return;
        }

        Map<String, Object> data = Maps.newHashMap();
        data.put("uid", usersDto.getUid().toString());
        data.put("userVo", usersDto);

        NeteaseBatchMsgBO msgBO = new NeteaseBatchMsgBO();
        msgBO.setFromAccid(systemConf.getLikeMsgUid());
        msgBO.setToAccids(fansList);
        msgBO.setType(100);
        msgBO.setPushcontent(usersDto.getNick() + "上线啦，TA在喊你来围观~");
        msgBO.setBody(new Body(DefMsgType.RoomOpen, DefMsgType.RoomOpenNotice, data));
        msgBO.setPayload(new Payload(Payload.SkipType.ROOM, usersDto));
        neteaseMsgManager.sendBatchMsg(msgBO);
    }

    /**
     * 房间相关的所有图片都迁移到七牛服务器
     *
     * @param room 转移图片之前的房间信息
     * @return 图片转移到七牛以后房间的信息
     */
    private RoomParamBO moveToQinniu(RoomParamBO room) {
        if (StringUtils.isNotBlank(room.getBackPic()) && !room.getBackPic().contains(jpegPrefix)) {
            if (room.getBackPic().length() < 50) {
                room.setBackPic(jpegPrefix + DEFAULT_HEAD);
            } else {
                try {
                    String fileName = qiniuManager.uploadByUrl(room.getBackPic());
                    // 图片迁移到七牛
                    String backPic = qiniuManager.mergeUrlAndSlim(fileName);
                    room.setBackPic(backPic);
                } catch (Exception e) {
                    logger.error("uploadByUrl error, roomId: " + room.getRoomId() + ", backPic: " + room.getBackPic(), e);
                }
            }
        }
        if (StringUtils.isNotBlank(room.getAvatar()) && !room.getAvatar().contains(jpegPrefix)) {
            if (room.getAvatar().length() < 50) {
                room.setBackPic(jpegPrefix + DEFAULT_HEAD);
            } else {
                try {
                    String fileName = qiniuManager.uploadByUrl(room.getAvatar());
                    // 图片迁移到七牛
                    String avatar = qiniuManager.mergeUrlAndSlim(fileName);
                    room.setAvatar(avatar);
                } catch (Exception e) {
                    logger.error("uploadByUrl error, roomId: " + room.getRoomId() + ", backPic: " + room.getBackPic(), e);
                }
            }
        }
        return room;
    }

    /**
     * 填充标签的信息，如ID,图片
     *
     * @param room 房间信息
     * @return
     */
    private void fillTagInfo(RoomDO room) {
        // 若不传入标签，默认为tagId=8（聊天）
        if ((room.getTagId() == null || room.getTagId() <= 0 || room.getTagId() == 8) && StringUtils.isBlank(room.getRoomTag())) {
            logger.error("tagNull tagWrong>:{}", gson.toJson(room));
            room.setTagId(8);
        }

        if (room.getTagId() != null) {
            // 根据获取标签信息
            RoomTagDTO roomTag = roomTagManager.getById((long) room.getTagId());
            logger.info("fillTagInfo -> roomTag:{}", gson.toJson(roomTag));
            logger.info("fillTagInfo -> tagId:{}", room.getTagId());
            if (roomTag != null) {
                room.setTagId(roomTag.getId());
                room.setTagPict(roomTag.getPict());
                room.setRoomTag(roomTag.getName());
            }
            return;
        }

        RoomTagDTO roomTag = roomTagManager.getByName(room.getRoomTag());
        if (roomTag != null) {
            room.setTagId(roomTag.getId());
            room.setTagPict(roomTag.getPict());
            room.setRoomTag(roomTag.getName());
        }
    }


    /**
     * 第一次创建聊天室，通知云信并且保存数据到DB
     *
     * @param room 开通房间的基本参数
     * @return 保存过的房间信息
     * @throws Exception 开通房间失败抛出异常
     */
    private RoomDTO createRoom(RoomParamBO room) throws Exception {
        UsersDTO users = usersManager.getUser(room.getUid());
        if (users == null) {
            throw new WebServiceException("房间开通失败,用户信息异常");
        }
        logger.info("createRoom start, room:{}", room);
        Date date = new Date();
        RoomDO roomDo = new RoomDO();
        roomDo.setValid(true);
        roomDo.setUid(room.getUid());
        roomDo.setIsPermitRoom((byte) 2); // 非牌照房
        roomDo.setMeetingName(UUIDUtils.get());
        roomDo.setOperatorStatus(RoomOptStatus.in.getStatus());
        roomDo.setOpenTime(date);
        roomDo.setCreateTime(date);
        roomDo.setUpdateTime(date);
        roomDo.setIsExceptionClose(false);
        roomDo.setAvatar(users.getAvatar());
        roomDo.setTitle(room.getTitle());
        roomDo.setTagId(room.getTagId());
        roomDo.setRoomTag(room.getRoomTag());
        roomDo.setBackPic(room.getBackPic());
        roomDo.setUid(room.getUid());
        roomDo.setRoomDesc(room.getRoomDesc());
        roomDo.setRoomNotice(room.getRoomNotice());
        roomDo.setOnlineNum(1);
        roomDo.setType((byte) 3);
        RoomDTO roomDto = new RoomDTO();
        BeanUtils.copyProperties(roomDo, roomDto);
        roomDto.setErbanNo(users.getErbanNo());
        Long roomId = imroomManager.createRoom(roomDto);
        logger.info("createRoom -> roomId:{}", roomId);

        // 通知云信服务器创建聊天室
//        RoomResult roomResult = netEaseManager.openRoom(room.getUid().toString(), room.getTitle(), buildRoomExtendInfo(roomDto));
//        if (roomResult.getCode() != 200) {
//            logger.info("createRoom error, code= {} uid= {} desc= {}", roomResult.getCode(), room.getUid(), roomResult.getDesc());
//            throw new WebServiceException("房间开通失败");
//        }
//        Map<String, Object> chatRoom = roomResult.getChatroom();
//        Long roomId = Long.parseLong(String.valueOf(chatRoom.get("roomid")));
        roomDo.setRoomId(roomId);
        if (StringUtils.isBlank(roomDo.getBackPic())) {
            roomDo.setBackPic(roomDo.getDefBackpic());
        }

        if ((roomDo.getTagId() == null || roomDo.getTagId() <= 0 || roomDo.getTagId() == 8) && StringUtils.isBlank(roomDo.getRoomTag())) {
            logger.error("tagNull createRoom>:{}", gson.toJson(roomDo));
        }

        // 填充默认的标签信息
        fillTagInfo(roomDo);
        // 保存房间信息
        roomDao.save(roomDo);

        roomDto = roomDao.getUserRoom(room.getUid());
//        BeanUtils.copyProperties(roomDo, roomDto);
        roomDto.setFaceType(roomDto.getFaceType());
        redisManager.hset(RedisKey.room.getKey(), room.getUid().toString(), gson.toJson(roomDto));
        return roomDto;
    }

    /**
     * 构建云信上聊天室的扩展字段信息
     *
     * @param roomDTO 房间信息
     * @return 扩展字段信息
     */
    private String buildRoomExtendInfo(RoomDTO roomDTO) {
        Map<String, String> roomMicMap = roomMicService.getRoomMicByUid(roomDTO.getUid());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RoomMic.ROOM_INFO, gson.toJson(roomDTO));
        jsonObject.put(RoomMic.MIC_QUEUE, gson.toJson(roomMicMap));
        return jsonObject.toJSONString();
    }

    private RoomUserinDTO convertRoomToVo(RoomDTO room) {
        if ((room.getTagId() == null || room.getTagId() <= 0 || room.getTagId() == 8) && StringUtils.isBlank(room.getRoomTag())) {
            logger.error("tagNull convertRoomToVoWrong>:{}", gson.toJson(room));
        }

        RoomUserinDTO roomVo = new RoomUserinDTO();
        roomVo.setRoomId(room.getRoomId());
        roomVo.setUid(room.getUid());
        roomVo.setMeetingName(room.getMeetingName());
        roomVo.setType(room.getType());
        roomVo.setOperatorStatus(room.getOperatorStatus());
        roomVo.setBackPic(room.getBackPic() == null ? "" : room.getBackPic());
        roomVo.setBadge("".equals(room.getBadge()) ? null : room.getBadge());
        roomVo.setValid(room.getValid());
        roomVo.setRoomDesc(room.getRoomDesc());
        roomVo.setRoomNotice(room.getRoomNotice());
        roomVo.setTitle(room.getTitle());
        roomVo.setOpenTime(room.getOpenTime());
        roomVo.setOfficeUser(new Byte("1"));
        roomVo.setIsExceptionClose(room.getIsExceptionClose());
        roomVo.setExceptionCloseTime(room.getExceptionCloseTime());
        roomVo.setRoomPwd(room.getRoomPwd());
        roomVo.setOfficialRoom(room.getOfficialRoom());
        roomVo.setRoomTag(room.getRoomTag());
        roomVo.setTagId(room.getTagId());
        roomVo.setTagPict(room.getTagPict());
        roomVo.setIsPermitRoom(room.getIsPermitRoom());
        roomVo.setFactor(roomManager.getNeedAddNum(room.getUid(), room.getOnlineNum()));
        roomVo.setCharmOpen(room.getCharmOpen());
        roomVo.setHideFace(getHideFace(room.getFaceType()));
        roomVo.setGiftEffectSwitch(room.getGiftEffectSwitch());
        roomVo.setPublicChatSwitch(room.getPublicChatSwitch());
        Byte bytes = room.getAbChannelType();
        roomVo.setPlayInfo(room.getPlayInfo());
        roomVo.setBackPicUrl(room.getBackPicUrl());
        roomVo.setAudioLevel(room.getAudioLevel());
        if (room.getGiftCardSwitch() == null) {
            roomVo.setGiftCardSwitch(0);
        } else {
            roomVo.setGiftCardSwitch(room.getGiftCardSwitch());
        }
        UsersDTO usersDTO = usersManager.getUser(room.getUid());
        roomVo.setOnlineNum(room.getOnlineNum());
        roomVo.setErbanNo(usersDTO.getErbanNo());
        if (systemConf.getAuditAccountList().contains(room.getUid())) {
            roomVo.setGiftDrawEnable(2);
        } else {
            roomVo.setGiftDrawEnable(1);
        }
        if (bytes != null) {
            roomVo.setAbChannelType(bytes);
            return roomVo;
        }
        roomVo.setAbChannelType(RoomUserAtt.A.getAtt());

        return roomVo;
    }

    private List<Integer> getHideFace(Integer type) {
        List<Integer> list = new ArrayList<>();
        if (type == null || type == 0) {
            list.add(18);
        } else if (type == 2) {
            list.add(18);
        }
        return list;
    }

    @Override
    public String selectChatInfo() {
        try {
            return gson.toJson(netEaseChatroomDao.findList());
        } catch (Exception e) {
            logger.error("查询公聊信息错误" + e.getMessage());
        }
        return null;
    }

    @Override
    public Object selectMalice() throws WebServiceException {
        try {
            return redisManager.hgetAll(RedisKey.malice_user.getKey());
        } catch (Exception e) {
            throw new WebServiceException(WebServiceCode.SERVER_ERROR);
        }
    }

    @Override
    public List<RoomLinkVo> listAuditLinkPool(Long uid) throws WebServiceException {
        UsersDTO usersDto;
        RoomDTO roomDto;
        RoomLinkVo linkVo;
        List<RoomLinkVo> list = Lists.newArrayList();
        for (Long roomUid : IOSData.AUDIT_ALIST) {
            usersDto = usersManager.getUser(roomUid);
            if (usersDto == null) {
                continue;
            }

            roomDto = roomManager.getUserRoom(roomUid);
            if (roomDto == null) {
                continue;
            }
            linkVo = new RoomLinkVo();
            linkVo.setUid(roomUid);
            linkVo.setAvatar(usersDto.getAvatar());
            linkVo.setErbanNo(usersDto.getErbanNo());
            linkVo.setBirth(usersDto.getBirth());
            linkVo.setStar(usersDto.getStar());
            linkVo.setNick(usersDto.getNick());
            linkVo.setEmail(usersDto.getEmail());
            linkVo.setSignture(usersDto.getSignture());
            linkVo.setUserVoice(usersDto.getUserVoice());
            linkVo.setVoiceDura(usersDto.getVoiceDura());
            linkVo.setRoomId(roomDto.getRoomId());
            linkVo.setOnlineNum(roomDto.getOnlineNum());
            linkVo.setRoomAvatar(roomDto.getAvatar());
            int likeStatus = fansDao.countLikeBetween(uid, roomUid);
            linkVo.setIsLike(likeStatus > 0);
            linkVo.setLinkNum(IOSData.AUDIT_ALIST.size() * 5 + random.nextInt(100));
            list.add(linkVo);
        }
        Collections.shuffle(list);// 随机打乱
        return list.subList(0, 6);
    }

    @Override
    public List<RoomLinkVo> linkPool(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        List<RoomLinkVo> list = roomDao.getLinkRoom(usersDTO.getGender());
        if (list == null) {
            list = Lists.newArrayList();
        }
        if (list.size() < 9) {
            list.addAll(listDefaultRoomLinks());
        }

        Collections.shuffle(list);// 随机打乱

        list.get(0).setLinkNum(list.size() * 5 + random.nextInt(100));
        return list.subList(0, 9);
    }


    private List<RoomLinkVo> listDefaultRoomLinks() {
        if (defaultRoomLinks.size() == 0) {
            RoomLinkVo roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(1L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_1.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_1.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(2L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_2.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_2.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(3L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_3.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_3.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(4L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_4.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_4.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(5L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_5.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender1_5.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(6L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_1.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_1.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(7L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_2.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_2.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(8L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_3.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_3.jpg");
            defaultRoomLinks.add(roomLinkVo);
            roomLinkVo = new RoomLinkVo();
            roomLinkVo.setUid(9L);
            roomLinkVo.setAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_4.jpg");
            roomLinkVo.setRoomAvatar("https://pic.tiantianyuyin.com/auto_gen_robot_gender2_4.jpg");
            defaultRoomLinks.add(roomLinkVo);
        }

        return defaultRoomLinks;
    }

    @Override
    public RoomLinkVo link(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        RoomLinkVo roomLinkVo = getLink(uid, roomDao.getLinkRoom(usersDTO.getGender()));
        if (roomLinkVo != null) {
            int likeStatus = fansDao.countLikeBetween(uid, roomLinkVo.getUid());

            roomLinkVo.setIsLike(likeStatus > 0);
        }
        return roomLinkVo;
    }

    @Override
    public RoomLinkVo getAuditLinkRoom(Long uid) throws WebServiceException {
        Long roomUid = IOSData.AUDIT_ALIST.get(random.nextInt(IOSData.AUDIT_ALIST.size()));
        UsersDTO usersDto = usersManager.getUser(roomUid);
        RoomLinkVo linkVo = new RoomLinkVo();
        if (usersDto != null) {
            linkVo.setUid(roomUid);
            linkVo.setAvatar(usersDto.getAvatar());
            linkVo.setErbanNo(usersDto.getErbanNo());
            linkVo.setBirth(usersDto.getBirth());
            linkVo.setStar(usersDto.getStar());
            linkVo.setNick(usersDto.getNick());
            linkVo.setEmail(usersDto.getEmail());
            linkVo.setSignture(usersDto.getSignture());
            linkVo.setUserVoice(usersDto.getUserVoice());
            linkVo.setVoiceDura(usersDto.getVoiceDura());
        }

        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto != null) {
            linkVo.setRoomId(roomDto.getRoomId());
            linkVo.setOnlineNum(roomDto.getOnlineNum());
            linkVo.setRoomAvatar(roomDto.getAvatar());
            linkVo.setLinkNum(IOSData.AUDIT_ALIST.size() * 5 + random.nextInt(10));
        }

        int likeStatus = fansDao.countLikeBetween(uid, roomUid);
        linkVo.setIsLike(likeStatus > 0);
        return linkVo;
    }

    private RoomLinkVo getLink(Long uid, List<RoomLinkVo> list) {
        for (RoomLinkVo roomLinkVo : list) {
            if (uid.intValue() == roomLinkVo.getUid().intValue()) {
                continue;
            }
            if (roomLinkVo.getOnlineNum() > roomManager.getRobotNum(roomLinkVo.getUid())) {
                String lock = redisManager.get(RedisKey.room_link_lock.getKey(roomLinkVo.getUid().toString()));// 每个人6秒没只能被一个人选中
                if (StringUtils.isBlank(lock)) {
                    String lock2 = redisManager.get(RedisKey.room_link_lock.getKey(uid + "_" + roomLinkVo.getUid()));// 一分钟内不能选择同一个人
                    if (StringUtils.isBlank(lock2)) {
                        redisManager.set(RedisKey.room_link_lock.getKey(roomLinkVo.getUid().toString()), "1");
                        redisManager.expire(RedisKey.room_link_lock.getKey(roomLinkVo.getUid().toString()), 6, TimeUnit.SECONDS);
                        redisManager.set(RedisKey.room_link_lock.getKey(uid + "_" + roomLinkVo.getUid()), "1");
                        redisManager.expire(RedisKey.room_link_lock.getKey(uid + "_" + roomLinkVo.getUid()), 1, TimeUnit.MINUTES);
                        return roomLinkVo;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<RoomRecommendVo> getRoomRecommendList(Long uid, String os, String appVersion, String app, Integer pageNum, Integer pageSize) {
        String resultJson = redisManager.get(RedisKey.room_recommend_list.getKey());
        if (StringUtils.isEmpty(resultJson)) {
            return Lists.newArrayList();
        }
        if (StringUtils.isNotBlank(resultJson) || !"[]".equals(resultJson)) {
            List<RoomRecommendVo> resultRooms = gson.fromJson(resultJson, new TypeToken<List<RoomRecommendVo>>() {
            }.getType());
            resultRooms.stream().forEach(item -> {
                UsersRoomAttentionDTO usersRoomAttentionDTO = roomAttentionDao.selectRoomAttentionByUidAndRoomId(uid, item.getRoomId());
                if (usersRoomAttentionDTO != null) {
                    item.setStatus(1);
                } else {
                    item.setStatus(0);
                }
            });
            if (pageNum == null || pageSize == null) {
                return resultRooms;
            }
            Integer size = resultRooms.size();
            Integer skip = (pageNum - 1) * pageSize;
            if (skip >= size) {
                return Lists.newArrayList();
            }
            if (skip + pageSize > size) {
                return resultRooms.subList(skip, resultRooms.size());
            }
            return resultRooms.subList(skip, skip + pageSize);
        }

        return Lists.newArrayList();
    }

    @Override
    public List<ChatInfoVO> getLobbyChatInfo(Long uid, String os, String appVersion, String app, String ip) throws WebServiceException {

        List<ChatInfoVO> list = Lists.newArrayList();
        if (appVersionManager.checkAuditingVersion(os, app, appVersion, ip, uid)) {
            ChatInfoVO c1 = new ChatInfoVO();
            c1.setUid(0L);
            c1.setNick("ak110");
            c1.setAvatar("https://pic.hnyueqiang.com/FolYc-I2IZyyGwJ3Mn_kHTzjPDfU?imageslim");
            c1.setContent("遇见心中那个TA");
            list.add(c1);

            ChatInfoVO c2 = new ChatInfoVO();
            c2.setUid(0L);
            c2.setNick("ak120");
            c2.setAvatar("https://pic.hnyueqiang.com/FswRpHyb2t4FI5oPZZf1a2pnLtBh?imageslim");
            c2.setContent("用声音在一起");
            list.add(c2);
            return list;
        } else {

            Integer pageNum = 1;
            Integer pageSize = 20;
            Integer skip = (pageNum - 1) * pageSize;
            if (systemConf.getEnv().equalsIgnoreCase("prod") || systemConf.getEnv().equalsIgnoreCase("release")) {
                String redisKey = "imxinpi_public_room_history_zset_key_" + 4;
                Set<String> stringSet = redisManager.reverseZsetRange(redisKey, skip, skip + pageSize - 1);
                if (stringSet.size() == 0) {
                    return list;
                }
                stringSet.forEach(item -> {
                    ChatInfoVO chatInfoVO = new ChatInfoVO();
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(item).getAsJsonObject();
                    JsonObject jsonData = jsonObject.get("data").getAsJsonObject();
                    chatInfoVO.setContent(jsonData.get("msg").getAsString());
                    ChatInfoVO record = gson.fromJson(jsonData.get("params"), ChatInfoVO.class);
                    chatInfoVO.setAvatar(record.getAvatar());
                    chatInfoVO.setNick(record.getNick());
                    chatInfoVO.setUid(record.getUid());
                    list.add(chatInfoVO);
                });

            } else {
                String redisKey = "imxinpi_public_room_history_zset_key_" + 2;
                Set<String> stringSet = redisManager.reverseZsetRange(redisKey, skip, skip + pageSize - 1);
                if (stringSet.size() == 0) {
                    return list;
                }
                stringSet.forEach(item -> {
                    ChatInfoVO chatInfoVO = new ChatInfoVO();
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(item).getAsJsonObject();
                    JsonObject jsonData = jsonObject.get("data").getAsJsonObject();
                    chatInfoVO.setContent(jsonData.get("msg").getAsString());
                    ChatInfoVO record = gson.fromJson(jsonData.get("params"), ChatInfoVO.class);
                    chatInfoVO.setAvatar(record.getAvatar());
                    chatInfoVO.setNick(record.getNick());
                    chatInfoVO.setUid(record.getUid());
                    list.add(chatInfoVO);
                });
            }

            return list;
        }
    }
}
