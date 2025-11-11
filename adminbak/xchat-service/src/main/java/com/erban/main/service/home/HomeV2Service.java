package com.erban.main.service.home;

import com.erban.main.model.*;
import com.erban.main.model.beanmap.HomeRoomFlowPeriod;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.room.RoomOnlineNumService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.room.RoomTagService;
import com.erban.main.service.statis.StatRoomCtrbsumPeriodService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.RoomVo;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeV2Service extends BaseService {
    @Autowired
    private RoomTagService roomTagService;
    @Autowired
    private StatRoomCtrbsumPeriodService statRoomCtrbsumPeriodService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomOnlineNumService roomOnlineNumService;

    /**
     * 获取首页热门下的房间列表，（过滤已经在热门推荐位上的房间）
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<RoomVo> getHomeRoomList(Long uid, Integer pageNum, Integer pageSize) {
        String json = jedisService.get(RedisKey.home_room_list.getKey());
        List<RoomVo> roomVoList = getRoomVoFromCache(json, pageNum, pageSize);
        return hideGivenRoom(uid, roomVoList);
    }

    /**
     * 获取新秀列表
     *
     * @return
     */
    public List<RoomVo> getNewRoomList(Long uid, Integer pageNum, Integer pageSize) {
        String json = jedisService.get(RedisKey.room_home_new.getKey());
        List<RoomVo> roomVoList = getRoomVoFromCache(json, pageNum, pageSize);
        return hideGivenRoom(uid, roomVoList);
    }

    /**
     * 获取指定标签类型的房间，（除萌新标签外）
     *
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<RoomVo> getRoomByTag(Long uid, Integer tagId, Integer pageNum, Integer pageSize) {
        String json = jedisService.get(RedisKey.room_tag_index.getKey(tagId.toString()));
        List<RoomVo> roomVoList = getRoomVoFromCache(json, pageNum, pageSize);
        return hideGivenRoom(uid, roomVoList);
    }

    /**
     * 获取热门推荐的列表
     *
     * @return
     */
    public List<RoomVo> getHomeHotRoom(Long uid, Integer pageNum, Integer pageSize) {
        String json = jedisService.get(RedisKey.home_hot_recom.getKey());
        List<RoomVo> roomVoList = getRoomVoFromCache(json, pageNum, pageSize);
        return hideGivenRoom(uid, roomVoList);
    }

    /**
     * 获取绿色厅的列表
     *
     * @return
     */
    public List<RoomVo> getGreenRoom() {
        String json = jedisService.get(RedisKey.green_room_list.getKey());
        if (!BlankUtil.isBlank(json)) {
            if ("[]".equals(json)) {
                return null;
            }
            return gson.fromJson(json, new TypeToken<List<RoomVo>>() {
            }.getType());
        }
        return new ArrayList<>();
    }

    /**
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<RoomVo> getRoomByTagFromDB(Integer tagId, List<Long> recomUids, Integer pageNum, Integer pageSize, List<Integer> isPermitRoom) {
        if (tagId == null) {
            return convertToRoom(statRoomCtrbsumPeriodService.getRoomFlowOnlinePeriodFromDB(recomUids, null, pageNum, pageSize, isPermitRoom));
        }
        RoomTag roomTag = roomTagService.getRoomTagById(tagId);
        List<Integer> tagIds = Lists.newArrayList();
        String[] arrId = roomTag.getChildren().split(",");
        for (String tagIdstr : arrId) {
            tagIds.add(Integer.valueOf(tagIdstr));
        }
        return convertToRoom(statRoomCtrbsumPeriodService.getRoomFlowOnlinePeriodFromDB(recomUids, tagIds, pageNum, pageSize, isPermitRoom));
    }

    public List<RoomVo> getRoomForFirstCharge(List<Long> recomUids, Integer pageNum, Integer pageSize) {
        return convertToRoom(statRoomCtrbsumPeriodService.getRoomForFirstCharge(recomUids, pageNum, pageSize));
    }

    public List<RoomVo> getGreenHome(List<Long> recomUids, Integer pageNum, Integer pageSize) {
        return convertToRoom(statRoomCtrbsumPeriodService.getGreenHome(recomUids, pageNum, pageSize));
    }

    public List<RoomVo> getRoomVoFromCache(String json, Integer pageNum, Integer pageSize) {
        if (!BlankUtil.isBlank(json) && !"[]".equals(json)) {
            List<RoomVo> list = gson.fromJson(json, new TypeToken<List<RoomVo>>() {
            }.getType());
            Integer size = list.size();
            Integer skip = (pageNum - 1) * pageSize;
            if (skip >= size) {
                return null;
            }
            if (skip + pageSize > size) {
                return list.subList(skip, list.size());
            }
            return list.subList(skip, skip + pageSize);
        }
        return null;
    }


    public List<RoomVo> getHomeRoomToRoomVo(List<HomeRoomFlowPeriod> roomFlowlist) {
        List<RoomVo> roomVoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(roomFlowlist)) {
            return Lists.newArrayList();
        }

        for (HomeRoomFlowPeriod homeRoomFlow : roomFlowlist) {
            Long uid = homeRoomFlow.getUid();
            Room room = roomService.getRoomByDB(uid);
            // 若配置了不展示，需要过滤
            if (room == null || room.getCanShow().intValue() != 1) {
                continue;
            }
            RoomVo roomVo = roomService.convertRoomToVo(room);
            Double flowSum = new Double(homeRoomFlow.getPersonFlowSumSeqNoValue());
            roomVo.setCalcSumDataIndex(flowSum.intValue());
            roomVo.setSeqNo(homeRoomFlow.getSeqNo());
            int onlineNum = homeRoomFlow.getOnlineNum();
            if (onlineNum == 0) {
                onlineNum = 1;
            }
            roomVo.setOnlineNum(onlineNum + roomOnlineNumService.getNeedAddNum(uid.toString(), onlineNum));
            Users users = usersService.getUsersByUid(uid);
            if (users != null) {
                roomVo.setAvatar(users.getAvatar());
                roomVo.setNick(users.getNick());
                roomVo.setGender(users.getGender());
                roomVo.setErbanNo(users.getErbanNo());
            }
            roomVoList.add(roomVo);
        }
        return roomVoList;

    }

    public List<RoomVo> convertToRoom(List<StatRoomFlowOnlinePeriod> list) {
        List<RoomVo> rooms = Lists.newArrayList();
        Users users;
        for (StatRoomFlowOnlinePeriod period : list) {
            RoomVo roomVo = new RoomVo();
            roomVo.setOnlineNum(period.getOnlineNum() + roomOnlineNumService.getNeedAddNum(period.getUid().toString(), period.getOnlineNum()));
            roomVo.setUid(period.getUid());
            users = usersService.getUsersByUid(period.getUid());
            roomVo.setErbanNo(users == null ? 1987456L : users.getErbanNo());
            roomVo.setNick(period.getNick());
            roomVo.setAvatar(period.getAvatar());
            roomVo.setGender(period.getGender());
            roomVo.setBackPic(period.getBackPic() == null ? "" : period.getBackPic());
            roomVo.setRoomId(period.getRoomId());
            roomVo.setRoomDesc(roomService.replaceSensitiveWords(period.getRoomDesc(), null));
            roomVo.setTitle(roomService.replaceSensitiveWords(period.getTitle(), null));
            roomVo.setRoomTag(period.getRoomTag());
            roomVo.setTagId(period.getTagId());
            roomVo.setTagPict(period.getTagPict());
            roomVo.setRecomSeq(period.getRecomSeq());
            roomVo.setType(period.getType());
            roomVo.setOfficeUser((byte) 1);
            roomVo.setOperatorStatus(period.getOperatorStatus());
            roomVo.setIsPermitRoom(period.getIsPermitRoom());
            roomVo.setOfficeUser(period.getOfficialRoom());
            roomVo.setBadge("".equals(period.getBadge()) ? null : period.getBadge());
            roomVo.setScore(period.getScore());
            rooms.add(roomVo);
        }
        return rooms;
    }

    public List<RoomVo> hideGivenRoom(Long uid, List<RoomVo> roomVoList) {
        if (uid != null && uid.intValue() == 1000819 && roomVoList != null) {
            List<RoomVo> list = Lists.newArrayList();
            for (RoomVo roomVo : roomVoList) {
                if (roomVo.getTitle() == null ||
                    (roomVo.getTitle().indexOf("游戏") == -1 && roomVo.getTitle().indexOf("相亲") == -1
                     && roomVo.getTitle().indexOf("100") == -1 && roomVo.getTitle().indexOf("200") == -1
                     && roomVo.getTitle().indexOf("艹") == -1 && roomVo.getTitle().indexOf("诱色") == -1)) {
                    list.add(roomVo);
                }
            }
            return list;
        }
        return roomVoList;
    }

}
