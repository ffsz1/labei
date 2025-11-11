package com.juxiao.xchat.manager.common.room.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.RoomTagDao;
import com.juxiao.xchat.dao.room.dto.RoomTagDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomTagManager;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 房间标签-业务实现
 */
@Service
public class RoomTagManagerImpl implements RoomTagManager {
    @Autowired
    private Gson gson;
    @Autowired
    private RoomTagDao roomTagDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private SysConfManager confManager;


    @Override
    public List<RoomTagDTO> getTopTagList(String os, String appVersion) {
        String liststr = redisManager.get(RedisKey.room_tag_top.getKey());
        if (StringUtils.isNotBlank(liststr)) {
            return gson.fromJson(liststr, new TypeToken<List<RoomTagDTO>>() {
            }.getType());
        }

        List<RoomTagDTO> list = getTopTagsForDB();
        if (list != null) {
            redisManager.set(RedisKey.room_tag_top.getKey(), gson.toJson(list));
        }
        return list;
    }

    @Override
    public List<RoomTagDTO> getAllTag(Long uid) {
        String listStr = redisManager.get(RedisKey.room_tag_room.getKey());
        List<RoomTagDTO> list = null;
        if (StringUtils.isEmpty(listStr)) {
            list = getAllTagsForDB();
            redisManager.set(RedisKey.room_tag_room.getKey(), gson.toJson(list));
        } else {
            list = gson.fromJson(listStr, new TypeToken<List<RoomTagDTO>>() {
            }.getType());
        }
        list = list.stream().filter(item -> showTags(uid).contains(item.getName())).collect(Collectors.toList());
        return list;
    }

    private Set<String> showTags(Long uid){
        Set<String> showSets = new HashSet<>();
        showSets.add("电台");
        showSets.add("音乐");
        showSets.add("交友");
        showSets.add("遇见");
        showSets.add("开黑");
        if(uid != null && uid != 0){
            String tagCache = redisManager.hget(RedisKey.room_users_tag.getKey(),uid.toString());
            if(StringUtils.isNotBlank(tagCache)){
                String[] tags  = tagCache.split(",");
                Collections.addAll(showSets, tags);
            }else{
                List<String> stringList = roomTagDao.getRoomUsersTagByTagName(uid);
                if(stringList.size() > 0){
                    showSets.addAll(stringList);
                    redisManager.hset(RedisKey.room_users_tag.getKey(),uid.toString(), StringUtils.join(stringList, ","));
                }
            }
        }
        return showSets;
    }

    @Override
    public List<RoomTagDTO> getSearchTags() {
        String listStr = redisManager.get(RedisKey.room_tag_search.getKey());
        if (StringUtils.isNotBlank(listStr)) {
            return gson.fromJson(listStr, new TypeToken<List<RoomTagDTO>>() {
            }.getType());
        }

        List<RoomTagDTO> list = getSearchTagsForDB();
        if (list != null) {
            redisManager.set(RedisKey.room_tag_search.getKey(), gson.toJson(list));
        }
        return list;
    }


    @Override
    public List<RoomTagDTO> getSearchTags(boolean isFilter) {
        List<RoomTagDTO> list;
        String listStr = redisManager.get(RedisKey.room_tag_search.getKey());
        if (StringUtils.isNotBlank(listStr)) {
            list = gson.fromJson(listStr, new TypeToken<List<RoomTagDTO>>() {
            }.getType());
        } else {
            list = getSearchTagsForDB();
            if (list != null) {
                redisManager.set(RedisKey.room_tag_search.getKey(), gson.toJson(list));
            }
        }

        if (list == null) {
            list = Lists.newArrayList();
        }

        // 过滤没有房间的标签
        if (list.size() > 0 && isFilter) {
            Iterator<RoomTagDTO> iterator = list.iterator();
            Type type = new TypeToken<List<RoomTagDTO>>() {
            }.getType();
            String roomlist;
            RoomTagDTO tagDto;
            List<RoomVo> tagRoomList;
            while (iterator.hasNext()) {
                tagDto = iterator.next();
                if (tagDto == null || tagDto.getId() == null) {
                    iterator.remove();
                    continue;
                }

                if (tagDto.getId() == 9) {
                    continue;
                }

                roomlist = redisManager.get(RedisKey.room_tag_index.getKey(String.valueOf(tagDto.getId())));
                if (StringUtils.isBlank(roomlist)) {
                    iterator.remove();
                    continue;
                }

                tagRoomList = gson.fromJson(roomlist, type);
                if (tagRoomList == null || tagRoomList.size() == 0) {
                    iterator.remove();
                }
            }
        }

        return list;
    }

    @Override
    public List<RoomTagDTO> listWxappTags() {
        SysConfDTO confDto = confManager.getSysConf(SysConfigId.wx_top_tag);
        if (confDto == null || StringUtils.isBlank(confDto.getConfigValue())) {
            return this.getSearchTags();
        }

        List<String> tagids = Lists.newArrayList(confDto.getConfigValue().split("@"));
        List<RoomTagDTO> list = Lists.newArrayList();
        try {
            for (String tagId : tagids) {
                list.add(this.getById(Long.parseLong(tagId)));
            }
            return list;
        } catch (Exception e) {
            return this.getSearchTags();
        }
    }

    @Override
    public RoomTagDTO getById(Long id) {
        if (id == null) {
            return null;
        }
        String result = redisManager.hget(RedisKey.room_tag_list.getKey(), id.toString());
        RoomTagDTO tag;
        if (StringUtils.isBlank(result)) {
            tag = roomTagDao.getById(id);
            if (tag != null) {
                redisManager.hset(RedisKey.room_tag_list.getKey(), id.toString(), gson.toJson(tag));
            }
        } else {
            tag = gson.fromJson(result, RoomTagDTO.class);
        }
        return tag;
    }

    @Override
    public RoomTagDTO getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return roomTagDao.getByName(name);
    }

    /**
     * 获取分类标签
     *
     * @return
     */
    public List<RoomTagDTO> getSearchTagsForDB() {
        return roomTagDao.listAppSearchTags();
    }

    /**
     * 从数据库中查询顶部标签
     *
     * @return
     */
    public List<RoomTagDTO> getTopTagsForDB() {
        return roomTagDao.listAppTopTags();
    }

    /**
     * 查询全部标签
     *
     * @return
     */
    public List<RoomTagDTO> getAllTagsForDB() {
        return roomTagDao.listAppAllTags();
    }

}
