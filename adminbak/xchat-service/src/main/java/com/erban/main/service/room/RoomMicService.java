package com.erban.main.service.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.model.redis.RoomMic;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.MicUserVo;
import com.erban.main.vo.RoomNotifyVo;
import com.google.common.collect.Maps;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.neteaseacc.result.RubbishRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RoomMicService extends BaseService {

    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomService roomService;

    /**
     * 上麦操作，测试时需要考虑上麦用户意外掉线时队列是否更新
     *
     * @param micUid   上麦用户
     * @param roomId   上麦房间
     * @param position 麦序位置
     * @return
     */
    public int upMic(Long micUid, Long roomId, Integer position) throws Exception {
        Users users = usersService.getUsersByUid(micUid);
        if (users == null) {
            return 404;     // 用户不存在
        }
        // 获取云信上房间的有序队列
        Map<String, MicUserVo> map = erBanNetEaseService.queueList(roomId);
        if (map.get(position.toString()) != null) {
            return 302; // 坑位已有人
        }
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            MicUserVo micUserVo = map.get(key);
            if (micUserVo.getUid().equals(micUid)) {
                // 若已经在麦上，先下麦
                int code = erBanNetEaseService.queuePoll(roomId, key);
                if (code == 200) {
                    break;
                } else {
                    return 500; // 下麦失败
                }
            }
        }
        MicUserVo micUserVo = new MicUserVo();
        micUserVo.setUid(users.getUid());
        micUserVo.setNick(users.getNick());
        micUserVo.setAvatar(users.getAvatar());
        micUserVo.setGender(users.getGender() == null ? 0 : users.getGender());
        return erBanNetEaseService.queueOffer(roomId, position.toString(), gson.toJson(micUserVo)
                , micUid.toString(), "true");
    }

    /**
     * 下麦操作，测试时需要考虑上麦用户意外掉线时队列是否更新
     *
     * @param micUid   下麦用户
     * @param roomId   下麦房间
     * @param position 麦序位置
     * @return
     * @throws Exception
     */
    public int downMic(Long micUid, Long roomId, Integer position) throws Exception {
        Map<String, MicUserVo> map = erBanNetEaseService.queueList(roomId);
        MicUserVo micUserVo = map.get(position.toString());
        if (micUserVo.getUid().equals(micUid)) {
            // 下麦时弹出队列中该UID对应的信息
            return erBanNetEaseService.queuePoll(roomId, position.toString());
        }
        return -1;
    }


    /**
     * 锁麦/开麦
     *
     * @param roomUid
     * @param position
     * @param state
     * @return
     */
    public int lockMic(Long roomUid, Integer position, Integer state) throws Exception {
        String json = jedisService.hget(RedisKey.room_mic_list.getKey(roomUid.toString()), position.toString());
        if (BlankUtil.isBlank(json)) {
            return -1;
        }
        RoomMic roomMic = gson.fromJson(json, RoomMic.class);
        roomMic.setMicState(state);
        // 更新坑位列表的缓存并通知云信更新聊天室的信息
        return updateRoomMicAndCache(roomMic, roomUid, position);
    }

    /**
     * 锁坑位/取消锁
     *
     * @param roomUid
     * @param position
     * @param state
     * @return
     */
    public int lockPos(Long roomUid, Integer position, Integer state) throws Exception {
        String json = jedisService.hget(RedisKey.room_mic_list.getKey(roomUid.toString()), position.toString());
        if (BlankUtil.isBlank(json)) {
            return -1;
        }
        RoomMic roomMic = gson.fromJson(json, RoomMic.class);
        roomMic.setPosState(state);
        // 更新坑位列表的缓存并通知云信更新聊天室的信息
        return updateRoomMicAndCache(roomMic, roomUid, position);
    }

    /**
     * 获取房间的坑位列表信息
     *
     * @param uid
     * @return
     */
    public Map<String, String> getRoomMicByUid(Long uid) {
        Map<String, String> map = jedisService.hgetAll(RedisKey.room_mic_list.getKey(uid.toString()));
        if (BlankUtil.isBlank(map)) {
            map = initMicForRoom(uid, 9);
        }
        return map;
    }

    /**
     * 删除房间的麦序列表（房间关闭时删除）
     *
     * @param uid
     */
    public void delRoomMicByUid(Long uid) {
        jedisService.del(RedisKey.room_mic_list.getKey(uid.toString()));
    }

    /**
     * 邀请上麦，通知被邀请用户
     *
     * @param micUid   上麦用户
     * @param roomUid  房主或管理员UID
     * @param roomId   上麦房间
     * @param position 麦序位置
     * @return
     */
    public int sendInviteMicMessage(Long micUid, Long roomUid, Long roomId, Integer position) throws Exception {
        Attach attach = new Attach();
        attach.setFirst(Constant.DefineProtocol.CUSTOM_MESS_HEAD_QUEUE);
        attach.setSecond(Constant.DefineProtocol.CUSTOM_MESS_SUB_INVITE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.RoomMic.KEY_INVITEUID, micUid);
        jsonObject.put(Constant.RoomMic.KEY_POSITION, position);
        attach.setData(jsonObject);
        return erBanNetEaseService.sendChatRoomMsg(roomId, UUIDUitl.get(), roomUid.toString()
                , Constant.DefineProtocol.CUSTOM_MESS_DEFINE, gson.toJson(attach)).getCode();
    }

    /**
     * 发送被踢下麦时的提示消息
     *
     * @param roomId
     * @param roomUid
     */
    public int sendKickTipMessage(Long roomId, Long roomUid) throws Exception {
        Attach attach = new Attach();
        attach.setFirst(Constant.DefineProtocol.CUSTOM_MESS_HEAD_QUEUE);
        attach.setSecond(Constant.DefineProtocol.CUSTOM_MESS_SUB_KICKIT);
        attach.setData("你已被房主或管理员踢下麦");
        return erBanNetEaseService.sendChatRoomMsg(roomId, UUIDUitl.get(), roomUid.toString()
                , Constant.DefineProtocol.CUSTOM_MESS_DEFINE, gson.toJson(attach)).getCode();
    }

    /**
     * 初始化房间的麦序
     *
     * @return
     */
    private Map<String, String> initMicForRoom(Long roomUid, int max) {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (Integer i = -1; i < max - 1; i++) {
            RoomMic roomMic = new RoomMic();
            roomMic.setPosition(i);
            roomMic.setMicState(0);
            roomMic.setPosState(0);
            String json = gson.toJson(roomMic);
            jedisService.hset(RedisKey.room_mic_list.getKey(roomUid.toString()), i.toString(), json);
            map.put(i.toString(), json);
        }
        return map;
    }

    /**
     * 更新缓存并通知云信更新聊天室信息
     *
     * @param roomMic
     * @param roomUid
     * @param position
     * @return
     * @throws Exception
     */
    private int updateRoomMicAndCache(RoomMic roomMic, Long roomUid, Integer position) throws Exception {
        RoomNotifyVo roomNotifyVo = new RoomNotifyVo();
        String roomMicStr = gsonDefine.toJson(roomMic);
        roomNotifyVo.setMicInfo(roomMicStr);
        roomNotifyVo.setType(Constant.RoomMic.ROOM_NOTIFY_TYPE_MIC);
        // 更新聊天室的麦序信息
        jedisService.hset(RedisKey.room_mic_list.getKey(roomUid.toString()), position.toString(), roomMicStr);
        Room room = roomService.getRoomByUid(roomUid);
        // 通知云信更新聊天室的信息
        return 1;
    }
}
