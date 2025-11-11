package com.erban.main.service.room;

import com.erban.main.model.Room;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.base.BaseService;
import com.erban.main.vo.RunningRoomVo;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.neteaseacc.result.RoomUserListRet;
import com.xchat.common.netease.neteaseacc.result.RubbishRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 清除无效的房间, 无效房间的定义：
 * 1、普通房间，房主不在且在线人数等于0；
 * 2、牌照房间，房主不在且在线人数等于0或者在线用户只有机器人；
 * <p>
 * 对于情况1，关闭房间；对于情况2，该房间不在页面展示。
 */
@Service
public class RoomCleanService extends BaseService {

    private static final int ROOM_IDLE_COUNT = 300; // 房间连续没人在线允许的时长，min

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;


    /**
     * 清除无效的房间
     */
    public void cleanInvalidRoom() {
        Map<String, String> map = jedisService.hgetAll(RedisKey.room_running.getKey());
        if (BlankUtil.isBlank(map)) {
            return;
        }
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            try {
                Long uid = Long.valueOf(key);
                RunningRoomVo runningRoomVo = gson.fromJson(map.get(key), RunningRoomVo.class);
                Room room = roomMapper.selectByPrimaryKey(uid);
                if (room == null || !room.getValid()) {
                    closeRunningRoom(uid);
                    continue;
                }
                // 从云信上获取当前房间的房主信息
                RoomUserListRet roomUserListRet = erBanNetEaseService.getRoomMemberListInfo(room.getRoomId(), uid);
                if (roomUserListRet == null || roomUserListRet.getDesc() == null) {
                    closeRunningRoom(room.getUid());
                    continue;
                }

                Map<String, Object> userInfo = roomUserListRet.getDesc().get("data").get(0);
                // 获取是否在线的状态
                boolean onlineStat = (boolean) userInfo.get("onlineStat");
                if (onlineStat) {
                    jedisService.hdel(RedisKey.room_permit_hide.getKey(), room.getUid().toString());
                    continue;
                }

                // 判断是否为牌照房
                if (room.getIsPermitRoom() == 1) {
                    handlePermitRoom(room, runningRoomVo.getCount());    // 牌照房
                } else {
                    handleNotPermitRoom(room, runningRoomVo.getCount()); // 非牌照房
                }
            } catch (Exception e) {
                logger.error("cleanInvalidRoom error, room uid: " + key, e);
            }
        }
    }

    /**
     * 关闭运行中的房间
     *
     * @param uid
     * @throws Exception
     */
    public void closeRunningRoom(Long uid) throws Exception {
        jedisService.hdel(RedisKey.room_running.getKey(), uid.toString());
        roomService.closeRoom(uid);
        logger.info("close room uid: {}", uid);
    }

    /**
     * 牌照房，判断是否需要在展示上过滤
     *
     * @param room
     */
    public void handlePermitRoom(Room room, int count) {
        if (room.getOnlineNum() == null && room.getOnlineNum() == 0) {
            // 计数器的值大于59*5，表示开房5小时内一直没人在线（在线人数每1分钟更新一次）
            if (count > ROOM_IDLE_COUNT) {
                // 在线人数为0时，把牌照房加入过滤列表
                jedisService.hset(RedisKey.room_permit_hide.getKey(), room.getUid().toString(), "");
            }
            return;
        }

        // 判断在线用户是否全部是机器人
        if (count > ROOM_IDLE_COUNT && !hasRealUserInRoom(room.getRoomId())) {
            // 在线人数为0时，把牌照房加入过滤列表
            jedisService.hset(RedisKey.room_permit_hide.getKey(), room.getUid().toString(), room.getUid().toString());
            return;
        }
        //
        jedisService.hdel(RedisKey.room_permit_hide.getKey(), room.getUid().toString());
    }

    /**
     * 非牌照房，判断是否应该关闭
     *
     * @param room
     * @throws Exception
     */
    public void handleNotPermitRoom(Room room, int count) throws Exception {
        // 非牌照房都不应该加入过滤的列表
        jedisService.hdel(RedisKey.room_permit_hide.getKey(), room.getUid().toString());
        if (Constant.RoomType.game.equals(room.getType())) {
            if (room.getOnlineNum() != null && room.getOnlineNum() > 0) {
                return;
            }
        }
        // 计数器的值大于59*5，表示开房5小时内一直没人在线（在线人数每1分钟更新一次）
        if (count > ROOM_IDLE_COUNT) {
            // 关闭运行中的房间
            closeRunningRoom(room.getUid());
        }
    }

    /**
     * 判断是否有真实用户在房间内
     *
     * @param roomId
     * @return
     */
    public boolean hasRealUserInRoom(Long roomId) {
        long curTime = System.currentTimeMillis();
        try {
            // 获取所有在线的固定成员
            String result = erBanNetEaseService.getMembersByPage(roomId, 2, curTime, 100);
            RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
            if (rubbishRet.getCode() != 200) {
                return false;
            }
            RoomUserListRet roomUserListRet = gson.fromJson(result, RoomUserListRet.class);
            List<Map<String, Object>> list = roomUserListRet.getDesc().get("data");
            if (list == null) {
                return false;
            }
            for (Map<String, Object> userMap : list) {
                if (userMap.get("isRobot") != null) {
                    boolean isRobot = (boolean) userMap.get("isRobot");
                    if (!isRobot) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
            // 获取所有的非固定成员
            result = erBanNetEaseService.getMembersByPage(roomId, 1, curTime, 100);
            rubbishRet = gson.fromJson(result, RubbishRet.class);
            if (rubbishRet.getCode() != 200) {
                return false;
            }
            roomUserListRet = gson.fromJson(result, RoomUserListRet.class);
            list = roomUserListRet.getDesc().get("data");
            if (BlankUtil.isBlank(list)) {
                return false;
            }
            for (Map<String, Object> userMap : list) {
                if (userMap.get("isRobot") != null) {
                    boolean isRobot = (boolean) userMap.get("isRobot");
                    if (!isRobot) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("hasRealUserInRoom error, roomId: " + roomId, e);
            return false;
        }

        return false;
    }


}
