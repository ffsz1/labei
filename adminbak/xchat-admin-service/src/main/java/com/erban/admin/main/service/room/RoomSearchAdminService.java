package com.erban.admin.main.service.room;

import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.utils.JavaNetURLRestfulApiClient;
import com.erban.admin.main.vo.AgoraVo;
import com.erban.admin.main.vo.RoomVo;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomSearchAdminService extends BaseService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomTagService roomTagService;
    @Autowired
    private JedisService jedisService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersMapper usersMapper;

    Gson gson = new Gson();

    public PageInfo<Room> getAllRoomList(Integer pageNumber, Integer pageSize, String erbanNo, Byte type, Byte isPermitRoom) {
        RoomExample roomExample = new RoomExample();
        roomExample.setOrderByClause("uid");
        RoomExample.Criteria criteria = roomExample.createCriteria();
        if (type != null) {
            criteria.andCanShowEqualTo(type);
        }
        if (isPermitRoom != null) {
            criteria.andIsPermitRoomEqualTo(isPermitRoom);
        }
        if (!BlankUtil.isBlank(erbanNo) && erbanNo != "") {
            Account account = accountService.getAccountByErBanNo(Long.valueOf(erbanNo));
            if (account == null) {
                return new PageInfo(new ArrayList());
            } else {
                criteria.andUidEqualTo(account.getUid());
            }
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Room> roomList = roomMapper.selectByExample(roomExample);
        PageInfo pageInfo = new PageInfo(roomList);
//        convertToRoomVoList(pageInfo.getList());

//        return new PageInfo<>(newList);
//        return new PageInfo<>(roomList);
        return pageInfo;
    }

    public BusiResult setRoomStatus(Long uid) {
        Room room = roomMapper.selectByPrimaryKey(uid);
        if (room != null) {
            if (room.getCanShow().toString().equals("1")) {//不展示
                room.setCanShow((byte) 0);
            } else {
                room.setCanShow((byte) 1);
            }
            roomMapper.updateByPrimaryKeySelective(room);
            saveRoomCache(room);
            return new BusiResult(BusiStatus.SUCCESS, room);
        } else {
            return new BusiResult(BusiStatus.ADMIN_ROOM_NOTEXIT);
        }
    }

    public List<RoomTag> getRoomTag(String name) {
        return roomTagService.getRoomTags(name);
    }

    public Room saveRoom(Integer uid, Integer tagId, String title, String badge, String backPic, Byte isPermitRoom, Long rewardMoney, Integer audioLevel, Integer charmOpen, Integer faceType) throws Exception {
        Room room = roomMapper.selectByPrimaryKey(Long.valueOf(uid));
        if (room != null) {
            RoomTag roomTag = roomTagService.getRoomTagById(tagId);
            if (roomTag == null) {
                throw new RuntimeException("标签信息异常");
            }
            room.setRoomTag(roomTag.getName());
            room.setTagPict(roomTag.getPict());
            room.setTagId(tagId);
            room.setTitle(title);
            room.setBadge(StringUtils.isBlank(badge) ? "" : badge);
//            room.setBackPic(backPic);
//            room.setDefBackpic(backPic);
            room.setFaceType(faceType);
            room.setIsPermitRoom(isPermitRoom);
            room.setRewardMoney(rewardMoney);
            room.setAudioLevel(audioLevel);
            room.setCharmOpen(charmOpen);
            roomService.updateOpenRoom(room);
//            saveRoomCache(room);
        }
        return room;
    }


    private void saveRoomCache(Room room) {
        if (room == null) {
            return;
        }
        String roomJson = gson.toJson(room);
        jedisService.hwrite(RedisKey.room.getKey(), room.getUid().toString(), roomJson);
    }

    public List<RoomVo> convertToRoomVoList(List<Room> oldList) {
        List<RoomVo> newList = new ArrayList<>(oldList.size());
        for (Room myRoom : oldList) {
            RoomVo roomVo = convertToRoomVo(myRoom);
            newList.add(roomVo);
        }
        return newList;
    }

    private RoomVo convertToRoomVo(Room room) {
        RoomVo roomVo = new RoomVo();
        roomVo.setRoom(room);
        Long uid = room.getUid();
        Account myAccount = accountService.getAccountByUid(uid);
        if (myAccount != null) {
            Long myErbanNo = myAccount.getErbanNo();
            roomVo.setErbanNo(myErbanNo);
        }
        return roomVo;
    }

    public BusiResult getRoomMicList(Long erbanNo) {
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Room room = roomService.getRoomByUid(users.getUid());
        if (room == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        try {
            AgoraVo agoraVo = JavaNetURLRestfulApiClient.getRoomMic(room.getRoomId());
            if (agoraVo == null) {
                return new BusiResult(BusiStatus.NOTEXISTS);
            }
            if (agoraVo.getSuccess()) {
                if (agoraVo.getData().getBroadcasters() != null && agoraVo.getData().getBroadcasters().length > 0) {
                    UsersExample usersExample = new UsersExample();
                    List<Long> longList = new ArrayList<>(agoraVo.getData().getBroadcasters().length);
                    for (Long l : agoraVo.getData().getBroadcasters()) {
                        longList.add(l);
                    }
                    usersExample.createCriteria().andUidIn(longList);
                    return new BusiResult(BusiStatus.SUCCESS, usersMapper.selectByExample(usersExample));
                }
                return new BusiResult(BusiStatus.SUCCESS);
            } else {
                return new BusiResult(BusiStatus.NOTEXISTS);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
    }
}
