package com.erban.main.service.room;

import com.erban.main.model.Room;
import com.erban.main.model.RoomGameRecord;
import com.erban.main.model.RoomGameRecordVo;
import com.erban.main.model.UserConfigure;
import com.erban.main.mybatismapper.RoomGameRecordMapper;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.user.UserConfigureService;
import com.erban.main.util.StringUtils;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomGameService extends CacheBaseService<RoomGameRecord, RoomGameRecord> {
    @Autowired
    private RoomGameRecordMapper roomGameRecordMapper;
    @Autowired
    private UserConfigureService userConfigureService;
    @Autowired
    private RoomService roomService;

    @Override
    public RoomGameRecord getOneByJedisId(String jedisId) {
        return null;
    }

    public RoomGameRecord getOneByJedisId(Long uid, Long roomId) {
        return getOne(RedisKey.room_game_record.getKey(), uid + "_" + roomId, "SELECT * from room_game_record where uid = ? and room_id = ? and game_status = 0", uid, roomId);
    }

    @Override
    public RoomGameRecord entityToCache(RoomGameRecord entity) {
        return entity;
    }

    public BusiResult getState(Long uid, Long roomId) {
        if (uid == null || roomId == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Room room = roomService.getRoomByRoomId(roomId);
        if (room == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        RoomGameRecordVo roomGameRecordVo = new RoomGameRecordVo();
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(room.getUid().toString());
        if (userConfigure == null || userConfigure.getGameRoom().intValue() == 0) {
            roomGameRecordVo.setStatus(-1);
        } else {
            RoomGameRecord roomGameRecord = getOneByJedisId(uid, roomId);
            if(roomGameRecord == null) {
                roomGameRecordVo.setStatus(0);
            } else {
                roomGameRecordVo.setStatus(roomGameRecord.getType().intValue());
                roomGameRecordVo.setResult(roomGameRecord.getResult());
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, roomGameRecordVo);
    }

    public BusiResult choose(Long uid, Long roomId, Integer type, String result) {
        if (uid == null || roomId == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (type == null || (type != 1 && type != 2)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (type == 2 && StringUtils.isBlank(result)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Room room = roomService.getRoomByRoomId(roomId);
        if (room == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(room.getUid().toString());
        if (userConfigure == null || userConfigure.getGameRoom().intValue() == 0) {
            return new BusiResult(BusiStatus.NOAUTHORITY, "房间活动已经结束", "");
        }
        RoomGameRecord roomGameRecord = getOneByJedisId(uid, roomId);
        if (roomGameRecord != null) {
            return new BusiResult(BusiStatus.NOAUTHORITY, "数据库已经存在选择", "");
        }
        if (type == 1) {
            for(int i = 0;i < 3;i++) {
                if (i == 0) {
                    result = "" + (int)(Math.random()*6+1);
                } else {
                    result += "," + (int)(Math.random()*6+1);
                }
            }
        }
        roomGameRecord = new RoomGameRecord();
        roomGameRecord.setUid(uid);
        roomGameRecord.setRoomId(roomId);
        roomGameRecord.setType(type.byteValue());
        roomGameRecord.setResult(result);
        roomGameRecord.setGameStatus(new Byte("0"));
        roomGameRecord.setCreateTime(new Date());
        roomGameRecordMapper.insert(roomGameRecord);
        saveOneCache(roomGameRecord, RedisKey.room_game_record.getKey(), uid + "_" + roomId);
        return new BusiResult(BusiStatus.SUCCESS, result);
    }

    public BusiResult confirm(Long uid, Long roomId, Integer type, String result) {
        if (uid == null || roomId == null || StringUtils.isBlank(result)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (type == null || (type != 1 && type != 2)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Room room = roomService.getRoomByRoomId(roomId);
        if (room == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(room.getUid().toString());
        if (userConfigure == null || userConfigure.getGameRoom().intValue() == 0) {
            return new BusiResult(BusiStatus.NOAUTHORITY, "房间活动已经结束", "");
        }
        RoomGameRecord roomGameRecord = getOneByJedisId(uid, roomId);
        if (roomGameRecord == null) {
            return new BusiResult(BusiStatus.NOTEXISTS, "数据库不存在选择", "");
        }
        if (roomGameRecord.getType().intValue() != type || !result.equals(roomGameRecord.getResult())) {
            return new BusiResult(BusiStatus.NOTEXISTS, "客户端和数据库选择不一致", "");
        }
        roomGameRecord.setGameStatus(new Byte("1"));
        roomGameRecordMapper.updateByPrimaryKey(roomGameRecord);
        jedisService.hdel(RedisKey.room_game_record.getKey(), uid + "_" + roomId);
        return new BusiResult(BusiStatus.SUCCESS, result);
    }

}
