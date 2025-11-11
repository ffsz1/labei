package com.erban.main.service.room;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.erban.main.config.SystemConfig;
import com.erban.main.dto.RoomBgDTO;
import com.erban.main.dto.UsersDTO;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.mybatismapper.RoomOpenHistMapper;
import com.erban.main.param.LeftChatRoomParam;
import com.erban.main.param.neteasepush.Body;
import com.erban.main.param.neteasepush.NeteaseSendMsgBatchParam;
import com.erban.main.param.neteasepush.Payload;
import com.erban.main.service.*;
import com.erban.main.service.api.QiniuService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.im.ImRoomManager;
import com.erban.main.service.im.dto.RoomDTO;
import com.erban.main.service.user.FansService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.RoomVo;
import com.erban.main.vo.RunningRoomVo;
import com.google.common.collect.Maps;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.neteaseacc.result.RoomRet;
import com.xchat.common.netease.neteaseacc.result.RoomUserListRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class RoomService extends BaseService {
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private FansService fansService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomOpenHistMapper roomOpenHistMapper;
    @Autowired
    private AuctionCurService auctionCurService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private AuctionDealService auctionDealService;
    @Autowired
    private RoomHotService roomHotService;
    @Autowired
    private RoomQueueService roomQueueService;
    @Autowired
    private QiniuService qiniuService;
    @Autowired
    private RoomTagService roomTagService;
    @Autowired
    private RoomMicService roomMicService;
    @Autowired
    private RoomOnlineNumService roomOnlineNumService;

    @Autowired
    private ImRoomManager imRoomManager;

    @Autowired
    private RoomBgPicService roomBgPicService;


    /**
     * 开房间 type 1竞拍房 2悬赏房
     *
     * @param room
     * @return
     * @throws Exception
     */
    public BusiResult<RoomVo> openRoom(Room room) throws Exception {
        room = moveToQinniu(room); // 图片迁移到七牛空间
        room = fillTagInfo(room);  // 填充默认的标签信息

        Room roomDb = getRoomByUid(room.getUid());
        if (null == roomDb) {
            room = createRoom(room);    // 第一次开房间
            if (room == null) {
                return new BusiResult(BusiStatus.USERINFONOTEXISTS);
            }
        } else {
            if (roomDb.getValid()) {
                return new BusiResult(BusiStatus.ROOMRUNNING, "聊天室已经在运行中，请在我的聊天室中直接进入！"
                        , convertRoomToVo(room));
            }
            Users users = usersService.getUsersByUid(room.getUid());
            room.setValid(true);
            room.setAbChannelType(users.getChannelType());
            room.setAvatar(users.getAvatar());
            room.setRoomId(roomDb.getRoomId());
            room.setOperatorStatus(Constant.RoomOptStatus.in);
            room.setDefBackpic(roomDb.getDefBackpic());
            updateOpenRoom(room);   // 重新开房
        }
        // 写入正在运行的房间的缓存
        writerRunningRoomVo(room, roomDb);
        // TODO: 2017/7/13 待完善， 应该分页获取关注列表并且异步发送通知
        sendOpenRoomNoticeToFollowers(room.getUid());
        RoomVo roomVo = convertRoomToVo(getRoomByUid(room.getUid()));
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(roomVo);
        return busiResult;
    }

    /**
     * 写入正在运行的房间缓存
     *
     * @param room
     * @param roomDb
     */
    public void writerRunningRoomVo(Room room, Room roomDb) {
        RunningRoomVo runningRoomVo = new RunningRoomVo();
        runningRoomVo.setOnlineNum(1);
        runningRoomVo.setUid(room.getUid());
        runningRoomVo.setType(room.getType());
        if (roomDb != null) {
            runningRoomVo.setRoomId(roomDb.getRoomId());
        } else {
            runningRoomVo.setRoomId(room.getRoomId());
        }
        String runningRoomStr = gson.toJson(runningRoomVo);
        jedisService.hwrite(RedisKey.room_running.getKey(), room.getUid().toString(), runningRoomStr);
        jedisService.hdel(RedisKey.room_permit_hide.getKey(), room.getUid().toString());
    }


    public void sendOpenRoomNoticeToFollowers(Long uid) throws Exception {
        List<Fans> followLsit = fansService.getFansListToList(uid);
        if (CollectionUtils.isEmpty(followLsit)) {
            return;
        }
        if (followLsit.size() > 500) {
            followLsit = followLsit.subList(0, 499);
        }
        List<String> toAccids = Lists.newArrayList();
        for (Fans fans : followLsit) {
            String accid = fans.getLikeUid().toString();
            toAccids.add(accid);
        }
        Users user = usersService.getUsersByUid(uid);
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
        neteaseSendMsgBatchParam.setToAccids(toAccids);
        neteaseSendMsgBatchParam.setType(100);
        String nick = user.getNick();
        neteaseSendMsgBatchParam.setPushcontent(nick + "上线啦，TA在喊你来围观~");
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.RoomOpen);
        body.setSecond(Constant.DefMsgType.RoomOpenNotice);
        Map<String, Object> data = Maps.newHashMap();
        data.put("uid", uid.toString());
        data.put("userVo", usersService.getUserVoByUid(uid));
        body.setData(data);
        neteaseSendMsgBatchParam.setBody(body);
        Payload payload = new Payload();
        payload.setSkiptype(Constant.PayloadSkiptype.room);
        payload.setData(uid);
        neteaseSendMsgBatchParam.setPayload(payload);
        logger.info("uid=" + uid + "发送开播通知，当前关注人数=" + followLsit.size());
        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);

    }

    /**
     * 根据uid获取房主房间信息
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public BusiResult<RoomVo> queryRoomByUid(Long uid, String os, String appVersion, HttpServletRequest request) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Room room = getRoomByUid(uid);
        RoomVo roomVo;
        if (room == null) {
            busiResult.setMessage("还没有开过房间，快去开房嗨起来吧~");
            roomVo = new RoomVo();
        } else {
            roomVo = convertRoomToVo(room);
        }
        // IOS新版本在审核期内的首页数据要做特殊处理
//        if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion, request)) {
//            List<Integer> list = new ArrayList<>();
//            list.add(17);
//            list.add(18);
//            list.add(20);
//            list.add(21);
//            list.add(22);
//            list.add(23);
//            roomVo.setHideFace(list);
//        }

        int version = 0;
        if (com.xchat.common.utils.StringUtils.isNotBlank(appVersion)) {
            version = Integer.valueOf(appVersion.replaceAll("\\.", ""));
        }
        if (version < 106) {
            roomVo.setBackPic("0");
        }
        busiResult.setData(roomVo);
        return busiResult;
    }

    public BusiResult<List<RoomVo>> queryRoomListByUids(String uids) {
        String[] uidArray = uids.split(",");
        if (uidArray == null || uidArray.length < 1 || uidArray.length > 200) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        List<RoomVo> roomVoList = Lists.newArrayList();
        List<Room> roomList = queryRoomBeanListByUids(uidArray);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (CollectionUtils.isEmpty(roomList)) {
            busiResult.setData(roomVoList);
            return busiResult;
        }
        roomVoList = convertRoomListToVoList(roomList);
        busiResult.setData(roomVoList);
        return busiResult;
    }

    /**
     * 根据roomId来获取房主的uid
     * cache -> db -> cache
     *
     * @param roomId
     * @return
     */
    public Long getroomUidByRoomId(Long roomId) {
        Long uid = getUidByRoomIdCache(roomId);
        if (uid == null) {
            RoomExample example = new RoomExample();
            example.createCriteria().andRoomIdEqualTo(roomId);
            List<Room> roomList = roomMapper.selectByExample(example);
            uid = roomList.get(0).getUid();
            setUidRoomIdCache(roomId, uid);
        }
        return uid;
    }

    /**
     * 房主关闭房间
     *
     * @param uid
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult closeRoom(Long uid) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Room room = getRoomByUid(uid);
        if (room == null) {
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        room.setValid(false);
        room.setOperatorStatus(Constant.RoomOptStatus.out);
        room.setUid(uid);
        Long roomId = room.getRoomId();
        room.setRoomId(roomId);

        closeRoom(room);
        saveRoomOpenHist(room);
        if (Constant.RoomType.auct.equals(room.getType())) {//竞拍房，如果有未完成的竞拍单，则结束竞拍
            fininsAuctByCloseRoom(uid);
        }
        return busiResult;
    }

    public void fininsAuctByCloseRoom(Long uid) throws Exception {
        auctionDealService.closeRoomReturnBackDeposit(uid);
        auctionCurService.removeAuctionByRoomOwnerLeft(uid);
    }

    public List<Room> queryRoomBeanListByUids(String uidArray[]) {
        List<Room> roomList = getRoomListCache(uidArray);
        if (roomList == null) {
            RoomExample example = new RoomExample();
            List<Long> uids = Lists.newArrayList();
            for (String str : uidArray) {
                uids.add(Long.valueOf(str));
            }
            example.createCriteria().andUidIn(uids);
            roomList = roomMapper.selectByExample(example);
            for (Room room : roomList) {
                saveRoomCacheByRoom(room);
            }
        }
        return roomList;
    }

    public BusiResult updateRunningRoom(Room room) throws Exception {
        Room roomDb = getRoomByUid(room.getUid());
        if (roomDb == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        if (hasSensitiveWords(room.getRoomDesc()) || hasSensitiveWords(room.getRoomNotice()) || hasSensitiveWords(room.getTitle())) {
            return new BusiResult(BusiStatus.ROOM_WORDS);
        }
        Date date = new Date();
        roomDb.setOpenTime(date);
        roomDb.setUpdateTime(date);
        room = fillTagInfo(room);
        room.setUpdateTime(date);
        room.setOpenTime(date);
        room.setValid(null);    // 不能更新该字段
        room.setOperatorStatus(null);
        room.setType(null);
        room.setBadge(null);
        room.setRoomId(roomDb.getRoomId());
        // 更新DB和云信的聊天室消息
        updateRoomNeteaseAndDB(room);
        RoomVo roomVo = convertRoomToVo(getRoomByUid(room.getUid()));
        return new BusiResult(BusiStatus.SUCCESS, roomVo);

    }

    public BusiResult updateRunningRoomByAdmin(Room room, Long adminUid) throws Exception {
        //TODO 管理员权限校验
        BusiResult busiResult = updateRunningRoom(room);
        return busiResult;
    }

    public List<RoomVo> getHomeRunningRoomList() {
        List<RoomVo> roomVoList = new ArrayList<>();
        Map<String, String> map = jedisService.hgetAll(RedisKey.room_running.getKey());
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String value = entry.getKey();
                RunningRoomVo runningRoomVo = gson.fromJson(entry.getValue(), RunningRoomVo.class);
                RoomVo roomVo = getRoomVoByUid(Long.parseLong(value));
                if (roomVo == null) {
                    continue;
                }
                if (roomVo.getValid()) {
                    // 判断是否是热门房间
                    if (roomHotService.getRoomVo(roomVo.getUid())) {
                        String roomHotStr = jedisService.hget(RedisKey.room_hot.getKey(), roomVo.getUid().toString());
                        RoomHot roomHot = gson.fromJson(roomHotStr, RoomHot.class);
                        roomVo.setSeqNo(roomHot.getRoomSeq());
                    }
                    roomVo.setCount(runningRoomVo.getCount());
                    roomVoList.add(roomVo);
                }
            }
        } else {
            RoomExample roomExample = new RoomExample();
            roomExample.createCriteria().andValidEqualTo(true);
            List<Room> roomList = roomMapper.selectByExample(roomExample);
            roomVoList = convertRoomListToVoList(roomList);
            for (RoomVo roomVo : roomVoList) {
                // 判断是否是热门房间
                if (roomHotService.getRoomVo(roomVo.getUid())) {
                    String roomHotStr = jedisService.hget(RedisKey.room_hot.getKey(), roomVo.getUid().toString());
                    RoomHot roomHot = gson.fromJson(roomHotStr, RoomHot.class);
                    roomVo.setSeqNo(roomHot.getRoomSeq());
                }
                String json = gson.toJson(roomVo);
                RunningRoomVo runningRoomVo = new RunningRoomVo();
                runningRoomVo.setRoomId(roomVo.getRoomId());
                runningRoomVo.setUid(roomVo.getUid());
                runningRoomVo.setOnlineNum(1);
                String runningRoomStr = gson.toJson(runningRoomVo);
                jedisService.hwrite(RedisKey.room.getKey(), roomVo.getUid().toString(), json);
                jedisService.hwrite(RedisKey.room_running.getKey(), roomVo.getUid().toString(), runningRoomStr);
            }
        }
        return roomVoList;
    }

    public int ownerOrSeqUserLeftRoom(LeftChatRoomParam leftChatRoomParam) throws Exception {
        Long roomId = leftChatRoomParam.getRoomId();
        Room room = getRoomByRoomId(roomId);
        if (room == null) {
            return 200;
        }
        Long uid = Long.valueOf(leftChatRoomParam.getAccid());
        Long roomUid = room.getUid();
        if (uid.equals(roomUid)) {// 房主
            logger.info("房主离开并且关闭房间uid=" + uid + "&roomUid=" + roomUid + "&roomId=" + room.getRoomId());
            /* 判断房间是否是正常关闭  游戏房间不关闭*/
            if (room.getValid()) {
                if (room.getType().equals(Constant.RoomType.radio) || room.getType().equals(Constant.RoomType.auct)) {
                    /* 不正常关闭 */
                    logger.info("不正常关闭房间,uid:" + roomUid);
                    room.setIsExceptionClose(true);
                    room.setExceptionCloseTime(new Date());
                    room.setOperatorStatus(Constant.RoomOptStatus.out);
                    updateRunningRoom(room);
                } else {
                    room.setOperatorStatus(Constant.RoomOptStatus.out);
                    room.setIsExceptionClose(false);
                    updateRunningRoom(room);
                }

            }
        }
        return 0;
    }

    public int ownerInRoom(LeftChatRoomParam leftChatRoomParam) throws Exception {
        Long roomId = leftChatRoomParam.getRoomId();
        Room room = getRoomByRoomId(roomId);
        if (room == null) {
            return 200;
        }
        Long uid = Long.valueOf(leftChatRoomParam.getAccid());
        Long roomUid = room.getUid();
        if (uid.equals(roomUid)) {// 房主
            if (Constant.RoomOptStatus.out == room.getOperatorStatus()) {
                room.setOperatorStatus(Constant.RoomOptStatus.in);
                updateRunningRoom(room);
            }
        }
        return 0;
    }

    /* 批量获取在线成员信息 */
    public RoomUserListRet getRoomUserListInfo(long roomId, long uid) throws Exception {
        RoomUserListRet roomMemberListInfo = erBanNetEaseService.getRoomMemberListInfo(roomId, uid);
        return roomMemberListInfo;
    }

    public Room getRoomByRoomId(Long roomId) {
        Long uid = getUidByRoomIdCache(roomId);
        if (uid == null) {
            RoomExample roomExample = new RoomExample();
            roomExample.createCriteria().andRoomIdEqualTo(roomId);
            List<Room> roomList = roomMapper.selectByExample(roomExample);
            if (CollectionUtils.isEmpty(roomList)) {
                return null;
            } else {
                Room room = roomList.get(0);
                setUidRoomIdCache(roomId, room.getUid());
                return room;
            }
        } else {
            return getRoomByUid(uid);
        }

    }

    private Long getUidByRoomIdCache(Long roomId) {
        String uidStr = jedisService.hget(RedisKey.room_roomId_uid.getKey(), roomId.toString());
        if (StringUtils.isEmpty(uidStr)) {
            return null;
        } else {
            Long uid = Long.valueOf(uidStr);
            return uid;
        }
    }

    private void setUidRoomIdCache(Long roomId, Long uid) {
        jedisService.hwrite(RedisKey.room_roomId_uid.getKey(), roomId.toString(), uid.toString());
    }

    private void saveRoomOpenHist(Room room) throws Exception {
        logger.info("将当前房间的开播时长写入数据库中，roomUid:" + room.getUid());
        Date date = new Date();
        Date openTime = room.getOpenTime();
        RoomOpenHist roomOpenHist = new RoomOpenHist();
        roomOpenHist.setHistId(UUIDUitl.get());
        roomOpenHist.setMeetingName(room.getMeetingName());
        roomOpenHist.setRoomId(room.getUid());
        roomOpenHist.setUid(room.getUid());
        roomOpenHist.setType(room.getType());
        roomOpenHist.setCloseTime(date);
        roomOpenHist.setDura(new Double(date.getTime() - openTime.getTime()));
        roomOpenHistMapper.insert(roomOpenHist);
    }

    public void updateOpenRoom(Room room) throws Exception {
        Date date = new Date();
        room.setUpdateTime(date);
        room.setOpenTime(date);
        room.setMeetingName(UUIDUitl.get());
//        setRoomOpenOrClose(room.getRoomId(), room.getUid().toString(), true);
        // 更新DB和云信的聊天室消息
        updateRoomNeteaseAndDB(room);
    }

    public void closeRoom(Room room) throws Exception {
        Long uid = room.getUid();
        Long roomId = room.getRoomId();
        room.setValid(false);
        updateCloseRoomDbInfo(room);
        // 通知云信清空聊天室的有序队列
        roomQueueService.dropRoomQueueByExceptionRoom(roomId);
        // 删除房间的麦序列表信息
        roomMicService.delRoomMicByUid(room.getUid());
        logger.info("清理正在运行房间缓存.");
        jedisService.hdelete(RedisKey.room_running.getKey(), room.getUid().toString(), null);
        // 通知云信设置聊天室状态为关闭
        setRoomOpenOrClose(roomId, uid.toString(), false);
    }

    /**
     * 更新DB和云信聊天室的信息
     *
     * @param room
     * @throws Exception
     */
    public void updateRoomNeteaseAndDB(Room room) throws Exception {
        // 更新DB的房间信息
        updateOpenRoomDbInfo(room);// 通知云信更新房间的信息
        RoomDTO roomDTO = new RoomDTO();
        BeanUtils.copyProperties(room,roomDTO);
        roomDTO.setValid(true);
        roomDTO.setHideFace(getHideFace(room.getFaceType()));
        roomDTO.setGiftDrawEnable((byte)1);
        roomDTO.setGiftEffectSwitch(room.getGiftCardSwitch());
        String cacheUser = jedisService.hget(RedisKey.user.getKey(),room.getUid().toString());
        UsersDTO usersDTO = gson.fromJson(cacheUser, UsersDTO.class);
        roomDTO.setErbanNo(usersDTO.getErbanNo());
        if(room.getBackPic() != null && !"null".equalsIgnoreCase(room.getBackPic()) && !StringUtils.isBlank(room.getBackPic())){
            RoomBgDTO roomBgDTO = roomBgPicService.getById(Integer.valueOf(room.getBackPic()),room.getRoomId());
            if(roomBgDTO != null){
                roomDTO.setBackPicUrl(roomBgDTO.getPicUrl());
            }
        }
        imRoomManager.updateRoom(roomDTO);
//        Room newRoom = getRoomByUid(room.getUid());
//        newRoom.setFactor(roomOnlineNumService.getNeedAddNum(newRoom.getUid().toString(), newRoom.getOnlineNum()));
//        RoomNotifyVo roomNotifyVo = new RoomNotifyVo();
//        roomNotifyVo.setRoomInfo(gsonDefine.toJson(newRoom));
//        roomNotifyVo.setType(Constant.RoomMic.ROOM_NOTIFY_TYPE_ROOM);
//        updateNeteaseRoomInfo(newRoom, gsonDefine.toJson(roomNotifyVo));
    }

    /**
     * 隐藏表情包
     * @param type
     * @return
     */
    private List<Integer> getHideFace(Integer type) {
        List<Integer> list = new ArrayList<>();
        if (type == null || type == 0) {
            list.add(18);
            list.add(19);
        } else if (type == 1) {
//            list.add(19);
        }
        return list;
    }

    /**
     * 通知云信更新聊天室的信息
     *
     * @throws Exception
     */
    private RoomRet createNeteaseRoomInfo(Room room) throws Exception {
        return erBanNetEaseService.openRoom(room.getUid().toString(), room.getTitle(), buildRoomInfoExtend(room));
    }

    /**
     * 通知云信更新聊天室的信息
     *
     * @throws Exception
     */
    public RoomRet updateNeteaseRoomInfo(Room room, String notifyExt) throws Exception {
        return erBanNetEaseService.updateRoomInfo(room.getRoomId(), room.getTitle(), buildRoomInfoExtend(room), notifyExt);
    }

    /**
     * 构建云信上聊天室的扩展字段信息
     *
     * @param room
     * @return
     */
    private String buildRoomInfoExtend(Room room) {
        Map<String, String> roomMicMap = roomMicService.getRoomMicByUid(room.getUid());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.RoomMic.ROOM_INFO, gsonDefine.toJson(room));
        jsonObject.put(Constant.RoomMic.MIC_QUEUE, gsonDefine.toJson(roomMicMap));
        return jsonObject.toJSONString();
    }

    private void updateOpenRoomDbInfo(Room room) {
        // TODO 加入标签信息，历史原因
        room = fillTagInfo(room);
        if (BlankUtil.isBlank(room.getBackPic())) {
            if (!BlankUtil.isBlank(room.getDefBackpic())) {
                room.setBackPic(room.getDefBackpic());
            }
        }
        roomMapper.updateByPrimaryKeySelective(room);
        saveRoomCacheByUid(room.getUid());
    }

    private void updateCloseRoomDbInfo(Room room) throws Exception {
        roomMapper.updateByPrimaryKeySelective(room);
        saveRoomCacheByUid(room.getUid());
    }

    private void setRoomOpenOrClose(Long roomId, String uid, boolean valid) throws Exception {
//        CloseRoomRet closeRoomRet = erBanNetEaseService.toggleCloseStat(roomId, uid, valid);
//        if (!(closeRoomRet.getCode() == 200 || closeRoomRet.getCode() == 417)) {
//            logger.error("setRoomOpenOrClose error, roomId=" + roomId + "&uid=" + uid + ", valid=" + valid
//                    + ", code=" + closeRoomRet.getCode());
//        }
    }

    public Room getRoomByDB(Long uid) {
        return roomMapper.selectByPrimaryKey(uid);
    }

    public Room getRoomByUid(Long uid) {
        Room room = getRoomCacheByUid(uid);
        if (room == null) {
            room = roomMapper.selectByPrimaryKey(uid);
            if (room != null) {
                saveRoomCacheByRoom(room);
            }
        }
        return room;
    }

    public RoomVo getRoomVoByUid(Long uid) {
        Room room = getRoomByUid(uid);
        if (room == null) {
            return null;
        }
        RoomVo roomVo = convertRoomToVo(room);
        return roomVo;
    }

    public RoomVo getRoomVoWithUsersByUid(Long uid) {
        Room room = getRoomByUid(uid);
        if (room == null) {
            return null;
        }
        RoomVo roomVo = convertRoomToVo(room);
        Users users = usersService.getUsersByUid(room.getUid());
        if (users != null) {
            roomVo.setErbanNo(users.getErbanNo());
            roomVo.setAvatar(users.getAvatar());
            roomVo.setNick(users.getNick());
            roomVo.setGender(users.getGender());
        }
        return roomVo;
    }

    public BusiResult<RoomVo> getWholeRoomVoWithUsersByUid(Long uid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        RoomVo roomVo = getRoomVoWithUsersByUid(uid);
        busiResult.setData(roomVo);
        return busiResult;
    }

    private List<Room> getRoomListCache(String[] uids) {
        List<String> roomListStr = jedisService.hmread(RedisKey.room.getKey(), uids);
        if (CollectionUtils.isEmpty(roomListStr)) {
            return null;
        }
        List<Room> roomList = Lists.newArrayList();
        for (String roomStr : roomListStr) {
            if (StringUtils.isEmpty(roomStr)) {
                continue;
            }
            Room room = gson.fromJson(roomStr, Room.class);
            roomList.add(room);
        }
        return roomList;

    }

    public void insertRoom(Room room) {
        roomMapper.insertSelective(room);
        saveRoomCacheByRoom(room);
    }

    /**
     * 第一次创建聊天室，通知云信并且保存数据到DB
     *
     * @param room
     * @return
     * @throws Exception
     */
    public Room createRoom(Room room) throws Exception {
        Users users = usersService.getUsersByUid(room.getUid());
        if (users == null) {
            return null;
        }
        logger.info("createRoom start, room:{}", room);
        Date date = new Date();
        room.setValid(true);
        room.setIsPermitRoom((byte) 2); // 非牌照房
        room.setMeetingName(UUIDUitl.get());
        room.setOperatorStatus(Constant.RoomOptStatus.in);
        room.setOpenTime(date);
        room.setCreateTime(date);
        room.setUpdateTime(date);
        room.setIsExceptionClose(false);
        room.setAvatar(users.getAvatar());
        // 通知云信服务器创建聊天室
//        RoomRet roomRet = createNeteaseRoomInfo(room);
//        if (roomRet.getCode() != 200) {
//            throw new Exception("createRoom error, code=" + roomRet.getCode() + "&uid=" + room.getUid() + "&desc=" + roomRet.getDesc());
//        }
//        Map<String, Object> chatRoom = roomRet.getChatroom();
//        Long roomId = (Long) chatRoom.get("roomid");
//        room.setRoomId(roomId);
        if (BlankUtil.isBlank(room.getBackPic())) {
            room.setBackPic(room.getDefBackpic());
        }
        insertRoom(room);
        return room;
    }

    public void saveRoomCacheByUid(Long uid) {
        Room room = roomMapper.selectByPrimaryKey(uid);
        if (room == null) {
            return;
        }
        String roomStr = gson.toJson(room);
        logger.info("saveRoomCacheByUid roomStr=" + roomStr);
        jedisService.hwrite(RedisKey.room.getKey(), uid.toString(), roomStr);
    }

    public void saveRoomCacheByRoom(Room room) {
        String roomStr = gson.toJson(room);
        jedisService.hwrite(RedisKey.room.getKey(), room.getUid().toString(), roomStr);
    }

    public Room getRoomCacheByUid(Long uid) {
        String roomStr = jedisService.hget(RedisKey.room.getKey(), uid.toString());
        if (StringUtils.isEmpty(roomStr)) {
            return null;
        } else {
            Room room = gson.fromJson(roomStr, Room.class);
            return room;
        }
    }

    public void updateRoomOnlineNumMysql(Long uid, int onlineNum) {
        Room room = new Room();
        room.setUid(uid);
        room.setOnlineNum(onlineNum);
        roomMapper.updateByPrimaryKeySelective(room);
//        logger.info("updateRoomOnlineNumMysql uid="+uid,"&onlineNum="+onlineNum);
    }

    public RoomVo convertRoomToVo(Room room) {
        RoomVo roomVo = new RoomVo();
        roomVo.setRoomId(room.getRoomId());
        roomVo.setUid(room.getUid());
        roomVo.setMeetingName(room.getMeetingName());
        roomVo.setType(room.getType());
        roomVo.setOperatorStatus(room.getOperatorStatus());
        roomVo.setBackPic(room.getBackPic() == null ? "" : room.getBackPic());
        roomVo.setBadge("".equals(room.getBadge()) ? null : room.getBadge());
        roomVo.setValid(room.getValid());
        roomVo.setRoomDesc(replaceSensitiveWords(room.getRoomDesc(), null));
        roomVo.setRoomNotice(replaceSensitiveWords(room.getRoomNotice(), null));
        roomVo.setTitle(replaceSensitiveWords(room.getTitle(), null));
        roomVo.setOpenTime(room.getOpenTime());
        roomVo.setOfficeUser(new Byte("1"));
        roomVo.setIsExceptionClose(room.getIsExceptionClose());
        roomVo.setExceptionCloseTime(room.getExceptionCloseTime());
        roomVo.setRoomPwd(room.getRoomPwd());
        roomVo.setOfficialRoom(room.getOfficialRoom());
//        roomVo.setRoomPwd(StringUtils.isEmpty(room.getRoomPwd())?null: MD5Utils.getMD5String(room.getRoomPwd()));
        roomVo.setRoomTag(room.getRoomTag());
        roomVo.setTagId(room.getTagId());
        roomVo.setTagPict(room.getTagPict());
        roomVo.setIsPermitRoom(room.getIsPermitRoom());
        roomVo.setFactor(roomOnlineNumService.getNeedAddNum(room.getUid().toString(), room.getOnlineNum()));
        Byte bytes = room.getAbChannelType();
        if (bytes != null) {
            roomVo.setAbChannelType(bytes);
            return roomVo;
        }
        roomVo.setAbChannelType((Constant.UsersAtt.A));
        return roomVo;
    }

    public List<RoomVo> convertRoomListToVoList(List<Room> roomList) {
        List<RoomVo> roomVoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(roomList)) {
            return roomVoList;
        }
        for (Room room : roomList) {
            RoomVo roomVo = convertRoomToVo(room);
            roomVoList.add(roomVo);
        }
        return roomVoList;
    }

    public List<Room> getRoomListByUids(List<Long> uids) {

        RoomExample roomExample = new RoomExample();
        List<Long> list = com.google.common.collect.Lists.newArrayList();
        for (int i = 0; i < uids.size(); i++) {
            list.add(uids.get(i));
        }
        roomExample.createCriteria().andUidIn(uids);
        List<Room> roomList = roomMapper.selectByExample(roomExample);
        return roomList;
    }


    public BusiResult addVipRoom(Long erbanNo) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = usersService.getUsersByErBanNo(erbanNo);
        Room room = getRoomByUid(users.getUid());
        String roomStr = gson.toJson(room);
        jedisService.hwrite(RedisKey.room_vip.getKey(), room.getUid().toString(), roomStr);
        return busiResult;
    }

    public BusiResult getRoomOnlines(String uids) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        String[] uidsStrs = uids.split(",");
        List<Room> rooms = queryRoomBeanListByUids(uidsStrs);
        Map<Long, Object> map = Maps.newHashMap();
        for (Room room : rooms) {
            RoomRet roomRet = erBanNetEaseService.getRoomMessage(room.getRoomId());
            map.put(room.getUid(), roomRet.getChatroom().get("onlineusercount"));
        }
        busiResult.setData(map);
        return busiResult;
    }

    /**
     * 房间相关的所有图片都迁移到七牛服务器
     *
     * @param room
     * @return
     */
    private Room moveToQinniu(Room room) {
        try {
            if (!BlankUtil.isBlank(room.getBackPic()) && !room.getBackPic().contains("res.91fb.com")) {
                if (room.getBackPic().length() < 50) {
                    room.setBackPic(Constant.DEFAULT_HEAD);
                } else {
                    String fileName = qiniuService.uploadByUrl(room.getBackPic());
                    String backPic = qiniuService.mergeUrlAndSlim(fileName); // 图片迁移到七牛
                    room.setBackPic(backPic);
                }
            }
            if (!BlankUtil.isBlank(room.getAvatar()) && !room.getAvatar().contains("res.91fb.com")) {
                if (room.getAvatar().length() < 50) {
                    room.setAvatar(Constant.DEFAULT_HEAD);
                } else {
                    String fileName = qiniuService.uploadByUrl(room.getAvatar());
                    String avatar = qiniuService.mergeUrlAndSlim(fileName); // 图片迁移到七牛
                    room.setAvatar(avatar);
                }
            }
        } catch (Exception e) {
            logger.error("uploadByUrl error, roomId: " + room.getRoomId() + ", backPic: " + room.getBackPic(), e);
        }

        return room;
    }

    /**
     * 填充标签的信息，如ID,图片
     *
     * @param room
     * @return
     */
    private Room fillTagInfo(Room room) {
        // 轻聊房强制加上电台标签
        if (Constant.RoomType.radio.equals(room.getType())) {
            return fillRadioTag(room);
        }
        // 若不传入标签，默认为tagId=8（聊天）
        if ((room.getTagId() == null || room.getTagId() <= 0) && BlankUtil.isBlank(room.getRoomTag())) {
            room.setTagId(8);
        }
        if (room.getTagId() != null) {
            RoomTag roomTag = roomTagService.getRoomTagById(room.getTagId());
            if (roomTag != null) {
                room.setTagId(roomTag.getId());
                room.setTagPict(roomTag.getPict());
                room.setRoomTag(roomTag.getName());
            }
        } else {
            RoomTag roomTag = roomTagService.getRoomTagByName(room.getRoomTag());
            if (roomTag != null) {
                room.setTagId(roomTag.getId());
                room.setTagPict(roomTag.getPict());
                room.setRoomTag(roomTag.getName());
            }
        }
        return room;
    }

    public Room fillRadioTag(Room room) {
        RoomTag roomTag = roomTagService.getRoomTagById(5);
        if (roomTag != null) {
            room.setTagId(roomTag.getId());
            room.setTagPict(roomTag.getPict());
            room.setRoomTag(roomTag.getName());
        }
        return room;
    }

    public boolean hasSensitiveWords(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        String sensitiveWords = getSensitiveWords();
        if (StringUtils.isBlank(sensitiveWords)) {
            return false;
        }
        return Pattern.compile(".*(" + sensitiveWords + ").*").matcher(str).matches();
    }

    public String replaceSensitiveWords(String str, String replace) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String sensitiveWords = getSensitiveWords();
        if (StringUtils.isBlank(sensitiveWords)) {
            return str;
        }
        if (StringUtils.isBlank(replace)) {
            replace = "*";
        }
        return str.replaceAll("(?:" + sensitiveWords + ")", replace);
    }

    public String getSensitiveWords() {
        String result = jedisService.get(RedisKey.room_sensitive_words.getKey());
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        StringBuffer sensitiveWords = new StringBuffer();
        List<RoomSensitiveWords> sensitiveWordsList = jdbcTemplate.query("SELECT * from room_sensitive_words where type = 1", new BeanPropertyRowMapper<>(RoomSensitiveWords.class));
        for (RoomSensitiveWords words : sensitiveWordsList) {
            if (sensitiveWords.length() == 0) {
                sensitiveWords.append(words.getSensitiveWords());
            } else {
                sensitiveWords.append("|").append(words.getSensitiveWords());
            }
        }
        if (sensitiveWords.length() > 0) {
            jedisService.set(RedisKey.room_sensitive_words.getKey(), sensitiveWords.toString());
        }
        return sensitiveWords.toString();
    }

}
