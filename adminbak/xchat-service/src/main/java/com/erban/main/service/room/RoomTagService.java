package com.erban.main.service.room;

import com.erban.main.model.Room;
import com.erban.main.model.RoomTag;
import com.erban.main.model.RoomTagExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.RoomMapperExpand;
import com.erban.main.mybatismapper.RoomTagMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.RoomVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoomTagService extends BaseService {
    @Autowired
    private RoomTagMapper roomTagMapper;
    @Autowired
    private RoomMapperExpand roomMapperExpand;
    @Autowired
    private UsersService usersService;

    /**
     * 获取顶部标签，cache -> db -> cache
     *
     * @return
     */
    public List<RoomTag> getTopTagList() {
        String liststr = jedisService.get(RedisKey.room_tag_top.getKey());
        List<RoomTag> list;
        if (BlankUtil.isBlank(liststr)) {
            list = getAppTopTagsFromDB();
            jedisService.set(RedisKey.room_tag_top.getKey(), gson.toJson(list));
        } else {
            list = gson.fromJson(liststr, new TypeToken<ArrayList<RoomTag>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 获取分类标签，cache -> db -> cache
     *
     * @return
     */
    public List<RoomTag> getSearchTags() {
        String liststr = jedisService.get(RedisKey.room_tag_search.getKey());
        List<RoomTag> list;
        if (BlankUtil.isBlank(liststr)) {
            list = getAppSearchTagsFromDB();
            jedisService.set(RedisKey.room_tag_search.getKey(), gson.toJson(list));
        } else {
            list = gson.fromJson(liststr, new TypeToken<ArrayList<RoomTag>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 获取全部标签的列表，cache -> db -> cache
     *
     * @return
     */
    public List<RoomTag> getAllTagList() {
        String liststr = jedisService.get(RedisKey.room_tag_room.getKey());
        List<RoomTag> list;
        if (BlankUtil.isBlank(liststr)) {
            list = getAllRoomTagsFromDB();
            jedisService.set(RedisKey.room_tag_room.getKey(), gson.toJson(list));
        } else {
            list = gson.fromJson(liststr, new TypeToken<ArrayList<RoomTag>>() {
            }.getType());
        }
        return list;
    }

    private static final String NEW_ROOM_SQL = "select * from room where DATE_SUB(CURDATE(),INTERVAL 30 day) <= DATE(create_time)";

    /**
     * 获取30天内新开的房间数据
     *
     * @return
     */
    public List<RoomVo> getNewRoomList(Integer day,List<Long> hideUids, Integer pageNum, Integer pageSize) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("day", day);
        param.put("skip", (pageNum - 1) * pageSize);
        param.put("size", pageSize);
        if(!BlankUtil.isBlank(hideUids)) {
            param.put("hideUids", hideUids);
        }
        List<Room> list = roomMapperExpand.selectNewRooms(param);
        return converToRoomVo(list);
    }

    /**
     * 获取轻聊房数据（电台标签）
     *
     * @return
     */
    public List<RoomVo> getRadioRoomList(List<Long> hideUids, Integer pageNum, Integer pageSize) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("skip", (pageNum - 1) * pageSize);
        param.put("size", pageSize);
        if(!BlankUtil.isBlank(hideUids)) {
            param.put("hideUids", hideUids);
        }
        List<Room> list = roomMapperExpand.selectRadioRooms(param);
        return converToRoomVo(list);
    }

    private List<RoomVo> converToRoomVo(List<Room> list) {
        List<RoomVo> rooms = Lists.newArrayList();
        for (Room period : list) {
            RoomVo roomVo = new RoomVo();
            Users users = usersService.getUsersByUid(period.getUid());
            if (users != null) {
                roomVo.setNick(users.getNick());
                roomVo.setAvatar(users.getAvatar());
                roomVo.setGender(users.getGender());
            }
            roomVo.setRoomPwd(period.getRoomPwd());
            roomVo.setOnlineNum(period.getOnlineNum());
            roomVo.setUid(period.getUid());
            roomVo.setBackPic(period.getBackPic());
            roomVo.setRoomId(period.getRoomId());
            roomVo.setRoomDesc(period.getRoomDesc());
            roomVo.setRoomTag(period.getRoomTag());
            roomVo.setTagId(period.getTagId());
            roomVo.setTagPict(period.getTagPict());
            roomVo.setTitle(period.getTitle());
            roomVo.setType(period.getType());
            roomVo.setOfficeUser((byte) 1);
            roomVo.setOperatorStatus(period.getOperatorStatus());
            roomVo.setIsPermitRoom(period.getIsPermitRoom());
            roomVo.setOfficeUser(period.getOfficialRoom());
            roomVo.setBadge(period.getBadge());
            rooms.add(roomVo);
        }
        return rooms;
    }

    /**
     * 获取房间标签信息，cache -> db -> cache
     *
     * @param tagId
     * @return
     */
    public RoomTag getRoomTagById(Integer tagId) {
        String json = jedisService.hget(RedisKey.room_tag_list.getKey(), tagId.toString());
        if (BlankUtil.isBlank(json)) {
            RoomTag roomTag = roomTagMapper.selectByPrimaryKey(tagId);
            if (roomTag != null) {
                jedisService.hwrite(RedisKey.room_tag_list.getKey(), tagId.toString(), gson.toJson(roomTag));
            }
            return roomTag;
        }
        return gson.fromJson(json, new TypeToken<RoomTag>() {
        }.getType());
    }

    public RoomTag getRoomTagByName(String name) {
        if (BlankUtil.isBlank(name)) {
            return null;
        }
        RoomTagExample example = new RoomTagExample();
        example.createCriteria().andNameEqualTo(name);
        List<RoomTag> list = roomTagMapper.selectByExample(example);
        if (!BlankUtil.isBlank(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 从DB获取所有的标签数据,除去萌新
     *
     * @return
     */
    public List<RoomTag> getAllRoomTagsFromDB() {
        RoomTagExample example = new RoomTagExample();
        example.createCriteria().andStatusEqualTo(true).andTypeEqualTo(1);
        // 按指定的序号来排序
        example.setOrderByClause("seq desc");
        return roomTagMapper.selectByExample(example);
    }

    /**
     * 从DB获取顶部标签的数据
     *
     * @return
     */
    public List<RoomTag> getAppTopTagsFromDB() {
        RoomTagExample example = new RoomTagExample();
        example.createCriteria().andIstopEqualTo(true).andStatusEqualTo(true);
        // 按指定的序号来排序
        example.setOrderByClause("seq desc");
        return roomTagMapper.selectByExample(example);
    }

    // 使用了tmpint字段
    public List<RoomTag> getAppSearchTagsFromDB() {
        RoomTagExample example = new RoomTagExample();
        example.createCriteria().andTmpintEqualTo(1).andStatusEqualTo(true);
        // 按指定的序号来排序
        example.setOrderByClause("seq desc");
        return roomTagMapper.selectByExample(example);
    }

}
