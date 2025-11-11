package com.juxiao.xchat.service.api.room.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.vo.RoomMicVO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import com.juxiao.xchat.manager.external.netease.conf.DefineProtocol;
import com.juxiao.xchat.manager.external.netease.conf.RoomMic;
import com.juxiao.xchat.manager.external.netease.ret.MicUserResult;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import com.juxiao.xchat.manager.external.netease.vo.MicUserVo;
import com.juxiao.xchat.service.api.room.RoomMicService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class RoomMicServiceImpl implements RoomMicService {
    @Autowired
    private Gson gson;
    @Autowired
    private ImRoomManager imroomManager;
    @Autowired
    private NetEaseRoomManager netEaseRoomManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private UsersManager usersManager;


    @Override
    public int upMic(Long uid, Long roomId, Integer position, Long operator) throws WebServiceException {
        if (roomId == null || position == null || uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO roomDTO = roomManager.getUserRoom(uid);
        if (roomDTO == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        // 校验用户信息
        UsersDTO user = usersManager.getUser(uid);
        if (user == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        // TODO 对同一房间同一麦序加分布式锁，防止多个用户同时抢麦

        // 获取该房间的麦上用户--查本地缓存???
//        MicUserResult micUserResult = netEaseRoomManager.queueList(roomId);
        MicUserResult micUserResult = null;
        if (micUserResult.getCode() != 200) {
            // 查询房间队列信息失败
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        // 判断该麦位置是否有人
        List<MicUserVo> list = micUserResult.getDesc().getList();
        MicUserVo userVO = list.get(position);
        if (userVO != null && userVO.getUid() != null) {
            // 该麦上现在有人
            throw new WebServiceException(WebServiceCode.ROOM_MICRO_SOMEONE);
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            if (uid.equals(list.get(i).getUid())) {
                // 该用户已经在麦上的话需要先下麦
                NetEaseRet ret = netEaseRoomManager.queuePoll(roomId, String.valueOf(i));
                if (ret.getCode() != 200) {
                    // 下麦失败
                    throw new WebServiceException(WebServiceCode.SERVER_BUSY);
                }
            }
        }
        // 执行上麦操作
        MicUserVo micUserVo = new MicUserVo();
        micUserVo.setUid(user.getUid());
        micUserVo.setNick(user.getNick());
        micUserVo.setAvatar(user.getAvatar());
        micUserVo.setGender(user.getGender() == null ? 0 : user.getGender());
        NetEaseRet ret = netEaseRoomManager.queueOffer(roomId, position.toString(), gson.toJson(micUserVo), uid.toString(), true);
        if (ret.getCode() != 200) {
            // 上麦失败
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        return ret.getCode();
    }

    @Override
    public int downMic(Long micUid, Long roomId, Integer position) throws WebServiceException {
        // 更新缓存???
//        MicUserResult result = netEaseRoomManager.queueList(roomId);
        MicUserResult result = null;
        MicUserVo userVo = result.getDesc().getList().get(position);
        NetEaseRet ret;
        if (userVo != null && userVo.getUid() == micUid) {
            // 下麦时弹出队列中该UID对应的信息
            ret = netEaseRoomManager.queuePoll(roomId, position.toString());
            if (ret.getCode() != 200) {
                // 接口调用失败
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
        } else {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        return ret.getCode();
    }

    @Override
    public int lockMic(Long roomUid, Long uid, Integer position, Integer state) throws Exception {
        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        // TODO: 检验uid的身份权限
        String fieldKey = String.valueOf(position);
        String result = redisManager.hget(RedisKey.room_mic_list.getKey(String.valueOf(roomUid)), fieldKey);
        if (StringUtils.isEmpty(result)) {
            Map<String, String> micRet = this.initRoomMic(roomUid);
            result = micRet.get(fieldKey);
            if (StringUtils.isEmpty(result)) {
                throw new WebServiceException(WebServiceCode.PARAM_ERROR);
            }
        }

        RoomMicVO micVo = gson.fromJson(result, RoomMicVO.class);
        micVo.setMicState(state);
        redisManager.hset(RedisKey.room_mic_list.getKey(String.valueOf(roomDto.getUid())), position.toString(), gson.toJson(micVo));
        imroomManager.pushRoomMicUpdateNotice(roomDto.getRoomId(), 1, micVo);
        // 更新坑位列表的缓存并通知云信更新聊天室的信息
        return 200;
    }

    @Override
    public int sendInviteMicMessage(Long micUid, Long roomUid, Long roomId, Integer position) throws WebServiceException {
        // TODO 参数验证,房间信息验证, 管理员信息验证, 房间麦序验证
        if (!imroomManager.isRoomManager(roomId, roomUid)) {
            // 管理员信息验证
            throw new WebServiceException(WebServiceCode.ROOM_NO_AUTHORITY);
        }
        Attach attach = new Attach();
        attach.setFirst(DefineProtocol.CUSTOM_MESS_HEAD_QUEUE);
        attach.setSecond(DefineProtocol.CUSTOM_MESS_SUB_INVITE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RoomMic.KEY_INVITEUID, micUid);
        jsonObject.put(RoomMic.KEY_POSITION, position);
        attach.setData(jsonObject);
        NetEaseRet ret = netEaseRoomManager.sendChatRoomMsg(roomId, UUIDUtils.get(), roomUid.toString(), DefineProtocol.CUSTOM_MESS_DEFINE, gson.toJson(attach));
        return ret.getCode();
    }

    @Override
    public int kickMic(Long micUid, Long roomUid, Long roomId, Integer position) throws WebServiceException {
        // TODO:增加管理员信息的验证
//        if (!roomManager.isManager(roomUid, roomId)) {
//            // 管理员信息验证
//            throw new WebServiceException(WebServiceCode.ROOM_NO_AUTHORITY);
//        }


        // 用户下麦
        downMic(micUid, roomId, position);
        Attach attach = new Attach();
        attach.setFirst(DefineProtocol.CUSTOM_MESS_HEAD_QUEUE);
        attach.setSecond(DefineProtocol.CUSTOM_MESS_SUB_KICKIT);
        attach.setData("你已被房主或管理员踢下麦");
        // 发送被踢下麦的消息
        NetEaseRet ret = netEaseRoomManager.sendChatRoomMsg(roomId, UUIDUtils.get(), roomUid.toString(),
                DefineProtocol.CUSTOM_MESS_DEFINE, gson.toJson(attach));
        return ret.getCode();
    }

    @Override
    public int lockPos(Long roomUid, Long uid, Integer position, Integer state) throws Exception {
        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        // TODO:校验用户的信息
        String fieldKey = String.valueOf(position);
        String json = redisManager.hget(RedisKey.room_mic_list.getKey(roomUid.toString()), fieldKey);
        if (StringUtils.isBlank(json)) {
            Map<String, String> micRet = this.initRoomMic(roomUid);
            json = micRet.get(fieldKey);
            if (StringUtils.isEmpty(json)) {
                throw new WebServiceException(WebServiceCode.PARAM_ERROR);
            }
        }

        RoomMicVO micVo = gson.fromJson(json, RoomMicVO.class);
        micVo.setPosState(state);
        redisManager.hset(RedisKey.room_mic_list.getKey(String.valueOf(roomDto.getUid())), position.toString(), gson.toJson(micVo));
        imroomManager.pushRoomMicUpdateNotice(roomDto.getRoomId(), 1, micVo);
        // 更新坑位列表的缓存并通知云信更新聊天室的信息
        return 200;
    }

    @Override
    public Map<String, String> getRoomMicByUid(Long uid) {
        Map<String, String> map = redisManager.hgetAll(RedisKey.room_mic_list.getKey(uid.toString()));
        if (map.isEmpty()) {
            map = this.initRoomMic(uid);
        }
        return map;
    }

//    public int updateRoomMicAndCache(RoomMicVo roomMicVo, RoomDTO roomDTO, Integer position) throws WebServiceException {
//        RoomNotifyVo roomNotifyVo = new RoomNotifyVo();
//        String roomMicStr = gson.toJson(roomMicVo);
//        roomNotifyVo.setType(RoomMicConf.ROOM_NOTIFY_TYPE_MIC);
//        roomNotifyVo.setMicInfo(roomMicStr);
//        // 更新缓存
//        redisManager.hset(RedisKey.room_mic_list.getKey(roomDTO.getUid().toString()), position.toString(), roomMicStr);
//        // 更新聊天室缓存
//        RoomDO roomDo = new RoomDO();
//        BeanUtils.copyProperties(roomDTO, roomDo);
//        roomManager.updateRoomInfo(roomDo);
//        // 构建拓展字段
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(RoomMic.ROOM_INFO, gson.toJson(roomDTO));
//        jsonObject.put(RoomMic.MIC_QUEUE, gson.toJson(getRoomMicByUid(roomDTO.getUid())));
//        // 通知云信更新聊天室
//        NetEaseRet ret = netEaseRoomManager.updateRoomInfo(roomDTO.getRoomId(), roomDTO.getTitle(), jsonObject.toJSONString(), gson.toJson(roomNotifyVo));
//        return ret.getCode();
//    }

    /**
     * 初始化房间的麦序
     *
     * @return
     */
    private Map<String, String> initRoomMic(Long roomUid) {
        Map<String, String> map = Maps.newLinkedHashMap();
        String json;
        for (int i = -1; i < 8; i++) {
            json = gson.toJson(new RoomMicVO(i, 0, 0));
            redisManager.hset(RedisKey.room_mic_list.getKey(String.valueOf(roomUid)), String.valueOf(i), json);
            map.put(String.valueOf(i), json);
        }
        return map;
    }

}
