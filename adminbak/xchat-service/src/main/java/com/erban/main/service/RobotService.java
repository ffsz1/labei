package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.RoomRobotGroupMapper;
import com.erban.main.mybatismapper.RoomRobotGroupRelaMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.im.ImRoomManager;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class RobotService extends BaseService {
    @Autowired
    private RoomRobotGroupMapper roomRobotGroupMapper;
    @Autowired
    private RoomRobotGroupRelaMapper roomRobotGroupRelaMapper;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private ImRoomManager imRoomManager;

    public Integer getRobotNum(Long uid){
        String str = jedisService.hget(RedisKey.robot_num.getKey(), uid.toString());
        if(StringUtils.isBlank(str)){
            Integer num = jdbcTemplate.queryForObject("select COUNT(1) from room_robot_group r INNER JOIN users u on r.group_no = u.erban_no where u.uid = ?", Integer.class, uid);
            jedisService.hset(RedisKey.robot_num.getKey(), uid.toString(), num.toString());
            return num;
        }
        return Integer.valueOf(str);
    }

    @Async
    public BusiResult addRobotByErbanNo(Long erbanNo) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = usersService.getUsresByErbanNo(erbanNo);
        if (users == null) {
            busiResult.setCode(BusiStatus.USERNOTEXISTS.value());
            busiResult.setMessage(GlobalConfig.appName + "号不存在");
            return busiResult;
        }
        Room room = roomService.getRoomByUid(users.getUid());
        if (room == null) {
            busiResult.setMessage("房间不存在");
            busiResult.setCode(BusiStatus.USERNOTEXISTS.value());
            return busiResult;
        }
        RoomRobotGroupRela roomRobotGroupRela = getRobotGroupRelaByUid(room.getUid());
        int groupNo = roomRobotGroupRela.getGroupNo();
        List<RoomRobotGroup> roomRobotGroupList = getPermitRoomGropuList(groupNo);
        Long roomId = roomRobotGroupRela.getRoomId();
        for (RoomRobotGroup roomRobotGroup : roomRobotGroupList) {
            Long robotUid = roomRobotGroup.getRobotUid();
            List<String> rotbotAccidList = Lists.newArrayList();
            rotbotAccidList.add(robotUid.toString());

            String robotAccidsListStr = gson.toJson(rotbotAccidList);
            erBanNetEaseService.addRobotToRoom(roomId, robotAccidsListStr);
            Thread.sleep(15 * 1000);
        }
        return busiResult;
    }

    public BusiResult removeRobotByErbanNo(Long erbanNo) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = usersService.getUsresByErbanNo(erbanNo);
        if (users == null) {
            busiResult.setCode(BusiStatus.USERNOTEXISTS.value());
            busiResult.setMessage(GlobalConfig.appName + "号不存在");
            return busiResult;
        }
        Room room = roomService.getRoomByUid(users.getUid());
        if (room == null) {
            busiResult.setMessage("房间不存在");
            busiResult.setCode(BusiStatus.USERNOTEXISTS.value());
            return busiResult;
        }
        removeRobotBy(room);
        return busiResult;
    }

    private void removeRobotBy(Room room) throws Exception {
        RoomRobotGroupRela roomRobotGroupRela = getRobotGroupRelaByUid(room.getUid());
        if (roomRobotGroupRela == null) {
            return;
        }
        int groupNo = roomRobotGroupRela.getGroupNo();
        List<RoomRobotGroup> roomRobotGroupList = getPermitRoomGropuList(groupNo);
        String accounts = "";
        for (RoomRobotGroup roomRobotGroup : roomRobotGroupList) {
            if (StringUtils.isNotBlank(accounts)) {
                accounts += ","+roomRobotGroup.getRobotUid();
            } else {
                accounts = roomRobotGroup.getRobotUid().toString();
            }
        }
        Long roomId = roomRobotGroupRela.getRoomId();
        imRoomManager.deleteRobotToRoom(roomId, accounts);
    }

    public void addRobotToPermitRoom() throws Exception {
        logger.info("addRobotToPermitRoom 开始添加牌照房机器人.....");
        List<RoomRobotGroupRela> roomRobotGroupRelaList = getRoomRobotGroupRelaList();
        for (RoomRobotGroupRela roomRobotGroupRela : roomRobotGroupRelaList) {
            Integer groupNo = roomRobotGroupRela.getGroupNo();
            List<RoomRobotGroup> roomRobotGroupList = getPermitRoomGropuList(groupNo);
            if (CollectionUtils.isEmpty(roomRobotGroupList)) {
                continue;
            }
            Long roomId = roomRobotGroupRela.getRoomId();
            for (RoomRobotGroup roomRobotGroup : roomRobotGroupList) {
                Long robotUid = roomRobotGroup.getRobotUid();
                List<String> rotbotAccidList = Lists.newArrayList();
                rotbotAccidList.add(robotUid.toString());
                String robotAccidsListStr = gson.toJson(rotbotAccidList);
                erBanNetEaseService.deleteRobotToRoom(roomId, robotAccidsListStr);
                erBanNetEaseService.addRobotToRoom(roomId, robotAccidsListStr);
            }
            logger.info("addRobotToPermitRoom 添加牌照房机器人uid=" + roomRobotGroupRela.getUid() + "&roomId=" + roomId + "&机器人数量=" + roomRobotGroupList.size());
        }
    }

    private RoomRobotGroupRela getRobotGroupRelaByUid(Long uid) {
        RoomRobotGroupRelaExample roomRobotGroupRelaExample = new RoomRobotGroupRelaExample();
        roomRobotGroupRelaExample.createCriteria().andUidEqualTo(uid);
        List<RoomRobotGroupRela> roomRobotGroupRelaList = roomRobotGroupRelaMapper.selectByExample(roomRobotGroupRelaExample);
        if (CollectionUtils.isEmpty(roomRobotGroupRelaList)) {
            return null;
        } else {
            return roomRobotGroupRelaList.get(0);
        }
    }

    private List<RoomRobotGroup> getPermitRoomGropuList(Integer groupNo) {
        RoomRobotGroupExample roomRobotGroupExample = new RoomRobotGroupExample();
        roomRobotGroupExample.createCriteria().andGroupNoEqualTo(groupNo);
        List<RoomRobotGroup> roomRobotGroups = roomRobotGroupMapper.selectByExample(roomRobotGroupExample);
        return roomRobotGroups;
    }

    private List<RoomRobotGroupRela> getRoomRobotGroupRelaList() {
        RoomRobotGroupRelaExample roomRobotGroupRelaExample = new RoomRobotGroupRelaExample();
        roomRobotGroupRelaExample.createCriteria().andStatusEqualTo(new Byte("1"));
        List<RoomRobotGroupRela> roomRobotGroupRelaList = roomRobotGroupRelaMapper.selectByExample(roomRobotGroupRelaExample);
        return roomRobotGroupRelaList;
    }

    private List<RoomRobotGroupRela> getAllRoomRobotGroupRelaList() {
        RoomRobotGroupRelaExample roomRobotGroupRelaExample = new RoomRobotGroupRelaExample();
        List<RoomRobotGroupRela> roomRobotGroupRelaList = roomRobotGroupRelaMapper.selectByExample(roomRobotGroupRelaExample);
        return roomRobotGroupRelaList;
    }

    public void deleteRobotRelaByRemovePermitRoom(Long uid) {
        RoomRobotGroupRelaExample roomRobotGroupRelaExample = new RoomRobotGroupRelaExample();
        roomRobotGroupRelaExample.createCriteria().andUidEqualTo(uid);
        roomRobotGroupRelaMapper.deleteByExample(roomRobotGroupRelaExample);
    }

    public boolean addRobotRelaByAddPermitRoom(Long uid, Long roomId) {
        RoomRobotGroupRelaExample roomRobotGroupRelaExample = new RoomRobotGroupRelaExample();
        roomRobotGroupRelaExample.createCriteria().andUidEqualTo(uid);
        List<RoomRobotGroupRela> roomRobotGroupRelaList = roomRobotGroupRelaMapper.selectByExample(roomRobotGroupRelaExample);
        if (CollectionUtils.isEmpty(roomRobotGroupRelaList)) {
            RoomRobotGroup roomRobotGroup = getNotInUsedRobotGroup();
            if (roomRobotGroup == null) {
                return false;
            }
            RoomRobotGroupRela roomRobotGroupRela = new RoomRobotGroupRela();
            roomRobotGroupRela.setUid(uid);
            roomRobotGroupRela.setRoomId(roomId);
            roomRobotGroupRela.setStatus(new Byte("1"));
            roomRobotGroupRela.setGroupNo(roomRobotGroup.getGroupNo());
            roomRobotGroupRelaMapper.insert(roomRobotGroupRela);
        }
        return true;
    }

    private RoomRobotGroup getNotInUsedRobotGroup() {
        List<RoomRobotGroupRela> roomRobotGroupRelaListAll = getAllRoomRobotGroupRelaList();
        List<Integer> roomRobotGroupRelaNotInList = Lists.newArrayList();
        for (RoomRobotGroupRela roomRobotGroupRelaAll : roomRobotGroupRelaListAll) {
            roomRobotGroupRelaNotInList.add(roomRobotGroupRelaAll.getGroupNo());
        }
        RoomRobotGroupExample roomRobotGroupExample = new RoomRobotGroupExample();
        roomRobotGroupExample.setDistinct(true);
        roomRobotGroupExample.createCriteria().andGroupNoNotIn(roomRobotGroupRelaNotInList);
        List<RoomRobotGroup> roomRobotGroupRelas = roomRobotGroupMapper.selectByExample(roomRobotGroupExample);
        if (CollectionUtils.isEmpty(roomRobotGroupRelas)) {
            return null;
        } else {
            return roomRobotGroupRelas.get(0);
        }
    }

}
