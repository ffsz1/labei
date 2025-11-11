package com.erban.main.service.room;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.KeepAliveRoom;
import com.erban.main.model.KeepAliveRoomExample;
import com.erban.main.model.RoomHot;
import com.erban.main.model.RoomHotExample;
import com.erban.main.mybatismapper.KeepAliveRoomMapper;
import com.erban.main.mybatismapper.RoomHotMapper;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.RoomHotVo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RoomHotService {

    @Autowired
    private RoomHotMapper roomHotMapper;

    @Autowired
    private KeepAliveRoomMapper keepAliveRoomMapper;

    @Autowired
    private JedisService jedisService;

    private Gson gson = new Gson();

    /* 查询热门房间是否开播 */
    public List<RoomHotVo> getRoomHotList() {
        List<RoomHotVo> roomList = new ArrayList<>();
        Map<String, String> map = jedisService.hgetAllBykey(RedisKey.room_hot.getKey());
        if (map == null || map.size() == 0) {
            RoomHotExample example = new RoomHotExample();
            example.createCriteria().andUidIsNotNull();
            List<RoomHot> roomHotList = roomHotMapper.selectByExample(example);
            for (RoomHot roomHot : roomHotList) {
                String json = gson.toJson(roomHot);
                jedisService.hwrite(RedisKey.room_hot.getKey(), roomHot.getUid().toString(), json);
            }
        } else {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String value = entry.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    RoomHotVo roomHotVo = gson.fromJson(value, RoomHotVo.class);
                    roomList.add(roomHotVo);
                }
            }
        }
        return roomList;
    }

    public boolean getRoomVo(Long uid) {
        RoomHot roomHot = null;
        String roomHotStr = jedisService.hget(RedisKey.room_hot.getKey(), uid.toString());
        roomHot = gson.fromJson(roomHotStr, RoomHot.class);
        if (roomHot == null) {
            roomHot = roomHotMapper.selectByPrimaryKey(uid);
            if (roomHot == null) {
                return false;
            }
            String json = gson.toJson(roomHot);
            jedisService.hwrite(RedisKey.room_hot.getKey(), roomHot.getUid().toString(), json);
        }
        long startTime = formateDate(roomHot.getStartLiveTime());
        long endTime = formateDate(roomHot.getEndLiveTime());
        long nowTime = formateDate(new Date());
        if (roomHot == null || startTime > nowTime || endTime < nowTime) {
            return false;
        } else {
            return true;
        }
    }

    private List<RoomHotVo> convertRoomListToVoList(List<RoomHot> roomHotList) {
        List<RoomHotVo> roomVoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(roomHotList)) {
            return roomVoList;
        }
        for (RoomHot roomHot : roomHotList) {
            RoomHotVo roomHotVo = convertRoomToVo(roomHot);
            roomVoList.add(roomHotVo);
        }
        return roomVoList;
    }

    private RoomHotVo convertRoomToVo(RoomHot roomHot) {
        RoomHotVo roomHotVo = new RoomHotVo();
        roomHotVo.setUid(roomHot.getUid());
        roomHotVo.setRoomSeq(roomHot.getRoomSeq());
        roomHotVo.setStartLiveTime(roomHot.getStartLiveTime());
        roomHotVo.setEndLiveTime(roomHot.getEndLiveTime());
        return roomHotVo;
    }

    public long formateDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String format = sdf.format(date);
        try {
            return sdf.parse(format).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void addHotRoom(Long uid, Date startDate, Date endDate, Integer seqNo) {
        RoomHot roomHot = new RoomHot();
        roomHot.setUid(uid);
        roomHot.setStartLiveTime(startDate);
        roomHot.setEndLiveTime(endDate);
        roomHot.setRoomSeq(seqNo);
        saveOrUpdateHotRoom(roomHot);
    }

    private void saveOrUpdateHotRoom(RoomHot roomHot) {
        RoomHot roomHot1 = roomHotMapper.selectByPrimaryKey(roomHot.getUid());
        if (roomHot1 == null) {
            roomHotMapper.insertSelective(roomHot);
            return;
        }
        roomHotMapper.updateByPrimaryKeySelective(roomHot);
    }

    public List<Long> getVipRoomListByCheckRoom() throws Exception {
        List<KeepAliveRoom> keepAliveRooms = null;
        List<Long> keepAliveRoomUids = Lists.newArrayList();
        String listStr = jedisService.read(RedisKey.room_vip.getKey());
        if (!StringUtils.isEmpty(listStr)) {
            Type type = new TypeToken<List<Long>>() {
            }.getType();
            keepAliveRoomUids = gson.fromJson(listStr, type);
        } else {
            KeepAliveRoomExample example = new KeepAliveRoomExample();
            example.createCriteria().andKeepIdIsNotNull();
            keepAliveRooms = keepAliveRoomMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(keepAliveRooms)) {
                for (KeepAliveRoom keepAliveRoom : keepAliveRooms) {
                    keepAliveRoomUids.add(keepAliveRoom.getUid());
                }
            }
            String keepUidsStr = gson.toJson(keepAliveRoomUids);
            jedisService.set(RedisKey.room_vip.getKey(), keepUidsStr);
        }
        return keepAliveRoomUids;
    }
}
