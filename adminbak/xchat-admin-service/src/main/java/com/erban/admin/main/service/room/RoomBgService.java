package com.erban.admin.main.service.room;

import com.erban.main.dto.RoomBgDTO;
import com.erban.main.model.Room;
import com.erban.main.model.RoomBg;
import com.erban.main.model.RoomBgExample;
import com.erban.main.mybatismapper.RoomBgMapper;
import com.erban.main.service.room.RoomService;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: alwyn
 * @Description: 房间背景
 * @Date: 2018/10/10 16:43
 */
@Service
public class RoomBgService {

    @Autowired
    private RoomBgMapper roomBgMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private RoomService roomService;
    private Gson gson = new Gson();

    public PageInfo<RoomBg> list(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        RoomBgExample example = new RoomBgExample();
        example.setOrderByClause("sort_no asc");
        List<RoomBg> list =  roomBgMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    public RoomBg get(Integer id) {
        //
        return roomBgMapper.selectByPrimaryKey(id);
    }

    public BusiResult delete(Integer id) {
        if (id == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        int count = roomBgMapper.deleteByPrimaryKey(id);
        deleteJoin(id);
        jedisService.hdel(RedisKey.room_background.getKey(), id.toString());
        return new BusiResult(count > 0 ? BusiStatus.SUCCESS : BusiStatus.SERVERERROR);
    }

    public BusiResult save(RoomBg roomBg) {
        if (roomBg == null) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        int count;
        if (roomBg.getId() == null) {
            roomBg.setCreateDate(new Date());
            count = roomBgMapper.insert(roomBg);
        } else {
            count = roomBgMapper.updateByPrimaryKeySelective(roomBg);
            deleteJoin(roomBg.getId());
        }
        switch (roomBg.getType()) {
            case 2:
                String tagIds = roomBg.getTagIds();
                if (StringUtils.isNotBlank(tagIds)) {
                    String[] tagArr = tagIds.split(",");
                    List<Integer> list = Lists.newArrayList();
                    for (int i = 0; i < tagArr.length; i ++) {
                        list.add(Integer.valueOf(tagArr[i]));
                    }
                    roomBgMapper.addRoomBgTag(roomBg.getId(), list);
                }
                break;
            case 3:
                String uids = roomBg.getUids();
                if (StringUtils.isNotBlank(uids)) {
                    String[] uidArr = uids.split(",");
                    List<Integer> list = Lists.newArrayList();
                    for (int i = 0; i < uidArr.length; i ++) {
                        list.add(Integer.valueOf(uidArr[i]));
                    }
                    roomBgMapper.addRoomBgTagUser(roomBg.getId(), list);
                }
                break;
        }
        jedisService.hdel(RedisKey.room_background.getKey(), roomBg.getId().toString());
        return new BusiResult(count > 0 ? BusiStatus.SUCCESS : BusiStatus.SERVERERROR);
    }

    public void updateRoom(RoomBg roomBg) {
        String result = jedisService.hget(RedisKey.room_background_user.getKey(), roomBg.getId().toString());
        Set<Long> set = gson.fromJson(result, new TypeToken<HashSet<Long>>(){}.getType());
        if (set != null && !set.isEmpty()) {

        }
    }

    /**
     * 删除关联表信息
     * @param id 背景ID
     */
    public void deleteJoin(Integer id) {
        roomBgMapper.deleteTagByBgId(id);
        roomBgMapper.deleteUserByBgId(id);
    }




}
