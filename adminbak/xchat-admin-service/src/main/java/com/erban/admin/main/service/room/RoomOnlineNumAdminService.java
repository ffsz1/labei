package com.erban.admin.main.service.room;

import com.erban.admin.main.mapper.RoomRobotGroupMapperMgr;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import com.erban.admin.main.vo.RoomRobotGroupVo;
import com.erban.main.model.Room;
import com.erban.main.model.RoomOnlineNum;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.RoomOnlineNumMapper;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomOnlineNumAdminService extends BaseService {
    @Autowired
    private RoomRobotGroupMapperMgr roomRobotGroupMapperMgr;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomOnlineNumMapper roomOnlineNumMapper;
    private Gson gson = new Gson();

    public PageInfo<RoomRobotGroupVo> getAll(Integer pageNumber, Integer pageSize, RoomRobotGroupParam roomRobotGroupParam){
        PageHelper.startPage(pageNumber,pageSize);
        List<RoomRobotGroupVo> roomRobotGroupList = roomRobotGroupMapperMgr.selectRoomOnlineNumByParam(roomRobotGroupParam);
        PageInfo pageInfo = new PageInfo(roomRobotGroupList);
        return pageInfo;
    }

    public BusiResult add(Long erbanNo, Integer robotNum) {
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Room room = roomService.getRoomByUid(users.getUid());
        if (room == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        RoomOnlineNum roomOnlineNum = roomOnlineNumMapper.selectByPrimaryKey(users.getUid());
        if(roomOnlineNum==null){
            roomOnlineNum= new RoomOnlineNum();
            roomOnlineNum.setUid(users.getUid());
            roomOnlineNum.setType(new Byte("1"));
            roomOnlineNum.setFactor(robotNum);
            roomOnlineNumMapper.insertSelective(roomOnlineNum);
        }else {
            roomOnlineNum.setType(new Byte("1"));
            roomOnlineNum.setFactor(robotNum);
            roomOnlineNumMapper.updateByPrimaryKey(roomOnlineNum);
        }
        jedisService.hset(RedisKey.room_online_num.getKey(), users.getUid().toString(), gson.toJson(roomOnlineNum));
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult remove(Long uid) {
        roomOnlineNumMapper.deleteByPrimaryKey(uid);
        jedisService.hdel(RedisKey.room_online_num.getKey(), uid.toString());
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
