package com.erban.main.service.user;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.FansMapper;
import com.erban.main.mybatismapper.FansMapperExpand;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.room.RoomService;
import com.erban.main.vo.FansFollowVo;
import com.erban.main.vo.RoomVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by liuguofu on 2017/5/10.
 */
@Service
public class FansService {
    private static final Logger logger = LoggerFactory.getLogger(FansService.class);
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private FansMapper fansMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserInRoomService userInRoomService;
    @Autowired
    private FansMapperExpand fansMapperExpand;
    @Autowired
    private NobleUsersService nobleUsersService;

    /**
     * @param likeUid
     * @param likedUid
     * @param type     1喜欢某人 2取消喜欢某人
     * @return
     * @throws Exception
     */
    public BusiResult likeSomeBody(Long likeUid, Long likedUid, String type) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = usersService.getUsersByUid(likedUid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if (type.equals("1")) {
            busiResult = likeSomeBody(likeUid, likedUid);
        } else if (type.equals("2")) {
            cancelLikeSomeBody(likeUid, likedUid);
        } else {
            return new BusiResult(BusiStatus.BUSIERROR);
        }

//        try {
//            dutyService.updateFreshDuty(likeUid, DutyType.like.getDutyId());
//        } catch (Exception e) {
//        }

        return busiResult;
    }

    private BusiResult likeSomeBody(Long likeUid, Long likedUid) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (likeUid.equals(likedUid)) {
            return new BusiResult(BusiStatus.NOTLIKEONESELF);
        }
        Fans fans = getLikeRela(likeUid, likedUid);
        if (fans != null) {// 喜欢关系已经存在,不处理
            return busiResult;
        }
        int increaseNum = 1;
        // 喜欢关系不存在,新增一条喜欢关系
        fans = insertLike(likeUid, likedUid);
        // 更新喜欢人的关注数量
        usersService.updateFollowNum(likeUid, increaseNum);
        // 更新被喜欢人的粉丝数量
        usersService.updateFansNum(likedUid, increaseNum);
        // 发送喜欢请求（好友请求）
        String likeUidStr = likeUid.toString();
        String likedUidStr = likedUid.toString();
        String type = "";// 请求加好友
        String msg = "";
        if (likedExists(likeUid, likedUid)) {// 是否已经被喜欢（相互喜欢）
            // 相互喜欢，双方成为好友
            type = "1";
            msg = "已经是好友了";
        } else {
            type = "2";
            msg = "喜欢了你";
        }
//        BaseNetEaseRet baseNetEaseRet = erBanNetEaseService.addFriends(likeUidStr, likedUidStr, type, msg);
//        if (baseNetEaseRet.getCode() != 200) {
//            throw new Exception("addFriends exception " + baseNetEaseRet.getCode() + baseNetEaseRet.getDesc());
//        }
        return busiResult;

    }

    private BusiResult cancelLikeSomeBody(Long likeUid, Long likedUid) throws Exception {

        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Fans fans = getLikeRela(likeUid, likedUid);
        if (fans == null) {// 喜欢关系不存在,不处理
            return busiResult;
        }
        int increaseNum = -1;
        // 删除喜欢关系数据
        deleteFansByFanId(fans.getFanId());
        // 更新取消喜欢人的关注数量
        usersService.updateFollowNum(likeUid, increaseNum);
        // 更新被取消被喜欢人的粉丝数量
        usersService.updateFansNum(likedUid, increaseNum);
        deleteFriend(likeUid, likedUid);
        return busiResult;

    }

    public BusiResult deleteFriend(Long likeUid, Long likedUid) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        String likeUidStr = likeUid.toString();
        String likedUidStr = likedUid.toString();
//        BaseNetEaseRet baseNetEaseRet = erBanNetEaseService.deleteFriends(likeUidStr, likedUidStr);
        return busiResult;
    }

    public BusiResult<List<FansFollowVo>> getFollowingList(Long uid, int pageSize, int pageNo) throws Exception {
        BusiResult<List<FansFollowVo>> busiResult = new BusiResult<List<FansFollowVo>>(BusiStatus.SUCCESS);
        List<Fans> fansList = getFollowingListByUid(uid, pageSize, pageNo);
        if (CollectionUtils.isEmpty(fansList)) {
            busiResult.setData(Lists.<FansFollowVo>newArrayList());
            return busiResult;
        }
        List<Long> uids = Lists.newArrayList();
        String uidArray[] = new String[fansList.size()];
        for (int i = 0; i < fansList.size(); i++) {
            uidArray[i] = fansList.get(i).getLikedUid().toString();
            uids.add(fansList.get(i).getLikedUid());
        }
        List<Room> roomList = roomService.queryRoomBeanListByUids(uidArray);
        Map<Long, Room> roomMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(roomList)) {
            for (Room room : roomList) {
                roomMap.put(room.getUid(), room);
            }
        }
        Map<Long, Users> userMap = usersService.getUsersMapBatch(uidArray);
        Map<Long, NobleUsers> nobleMap = nobleUsersService.getNobleUserMap(uidArray);
        Map<Long, RoomVo> userInRoomVoMap = userInRoomService.getUserInRoomBatch(uidArray);
        List<FansFollowVo> fansFollowVoList = Lists.newArrayList();
        for (int i = 0; i < fansList.size(); i++) {
            Fans fans = fansList.get(i);
            Long likedUid = fans.getLikedUid();
            Users users = userMap.get(likedUid);
            if (users == null) continue;

            FansFollowVo fansFollowVo = new FansFollowVo();
            fansFollowVo.setUid(likedUid);
            fansFollowVo.setAvatar(users.getAvatar());
            fansFollowVo.setFansNum(users.getFansNum());
            fansFollowVo.setNick(users.getNick());
            fansFollowVo.setGender(users.getGender());
            Room room = roomMap.get(likedUid);
            if (room != null) {
                fansFollowVo.setRoomBack(room.getBackPic());
                fansFollowVo.setType(room.getType());
                fansFollowVo.setValid(room.getValid());
                fansFollowVo.setOperatorStatus(room.getOperatorStatus());
                fansFollowVo.setTitle(room.getTitle());
            }
            RoomVo userInRoomVo = userInRoomVoMap.get(likedUid);
            if (userInRoomVo != null) {
                fansFollowVo.setUserInRoom(userInRoomVo);
            }
            fansFollowVo.setNobleUsers(nobleMap.get(likedUid));
            fansFollowVoList.add(fansFollowVo);
        }
//        Collections.sort(fansFollowVoList);
        Collections.sort(fansFollowVoList, new Comparator<FansFollowVo>() {
            public int compare(FansFollowVo a, FansFollowVo b) {
                Integer v1 = a.getUserInRoom() == null ? 0 : 1;
                Integer v2 = b.getUserInRoom() == null ? 0 : 1;
                return Integer.compare(v2, v1);
            }
        });
        busiResult.setData(fansFollowVoList);
        return busiResult;
    }

    //获取当前用户的所有关注数
    private Integer getFollowingCount(Long uid) {
        Integer recordCount = fansMapperExpand.getFollowingCount(uid);
        return recordCount;
    }

    /**
     * 查询uid是否喜欢isLIkeUid
     *
     * @param uid
     * @param isLikeUid
     * @return
     */
    public BusiResult<Boolean> checkUserIsLike(Long uid, Long isLikeUid) {
        BusiResult<Boolean> busiResult = new BusiResult<Boolean>(BusiStatus.SUCCESS);
        Fans fans = getLikeRela(uid, isLikeUid);
        if (fans != null) {
            busiResult.setData(true);
        } else {
            busiResult.setData(false);
        }
        return busiResult;
    }

    public List<Fans> getFollowingListByUid(Long uid, int pageSize, int pageNo) throws Exception {
        int offset = (pageNo - 1) * pageSize;
        List<Fans> fansList = fansMapperExpand.getFollowingListByUidPage(uid, offset, pageSize);
        return fansList;
    }

    /**
     * 获取喜欢（点赞关系）关系
     *
     * @param likeUid
     * @param likedUid
     * @return
     */
    private Fans getLikeRela(Long likeUid, Long likedUid) {
        FansExample fansExample = new FansExample();
        FansExample.Criteria criteria = fansExample.createCriteria();
        criteria.andLikeUidEqualTo(likeUid);
        criteria.andLikedUidEqualTo(likedUid);
        List<Fans> fansList = fansMapper.selectByExample(fansExample);
        if (CollectionUtils.isEmpty(fansList)) {
            return null;
        } else {
            return fansList.get(0);
        }

    }

    private void deleteFansByFanId(Long fannsId) {
        fansMapper.deleteByPrimaryKey(fannsId);
    }

    private Fans insertLike(Long likeUid, Long likedUid) {
        Fans fans = new Fans();
        Date date = new Date();
        fans.setLikeUid(likeUid);
        fans.setLikedUid(likedUid);
        fans.setCreateTime(date);
        UUIDUitl.get();
        fansMapper.insert(fans);
        return fans;
    }

    private boolean likeExists(Long likeUid, Long likedUid) {
        Fans fans = getLikeRela(likeUid, likedUid);
        if (fans == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean likedExists(Long likeUid, Long likedUid) {
        FansExample fansExample = new FansExample();
        FansExample.Criteria criteria = fansExample.createCriteria();
        criteria.andLikedUidEqualTo(likeUid);
        criteria.andLikeUidEqualTo(likedUid);
        List<Fans> fansList = fansMapper.selectByExample(fansExample);
        if (CollectionUtils.isEmpty(fansList)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkLikeEachOther(Long likeUid, Long likedUid) {
        if (likeExists(likeUid, likedUid) && likedExists(likeUid, likedUid)) {
            return true;
        } else {
            return false;
        }
    }

    public BusiResult getFansList(Long uid, Integer pageNo, Integer pageSize) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Map<String, Object> fansMap = Maps.newHashMap();
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 50;
        }
        pageNo = (pageNo - 1) * pageSize;
        Integer recordCount = fansMapperExpand.getFansCount(uid);
        Integer pageCount = (recordCount + pageSize - 1) / pageSize;
        fansMap.put("pageCount", pageCount);
        List<Fans> fansList = fansMapperExpand.getFansListByUidPage(uid, pageNo, pageSize);
        if (CollectionUtils.isEmpty(fansList)) {
            busiResult.setData(fansMap);
            return busiResult;
        }
        List<FansFollowVo> FansFollowVoList = convertFansListToVoList(fansList);
        Collections.sort(FansFollowVoList, new Comparator<FansFollowVo>() {
            public int compare(FansFollowVo a, FansFollowVo b) {
                Integer v1 = a.getUserInRoom() == null ? 0 : 1;
                Integer v2 = b.getUserInRoom() == null ? 0 : 1;
                return Integer.compare(v2, v1);
            }
        });
        fansMap.put("fansList", FansFollowVoList);
        busiResult.setData(fansMap);
        return busiResult;
    }

    public List<Fans> getFansListToList(Long uid) {
        FansExample example = new FansExample();
        example.createCriteria().andLikedUidEqualTo(uid);
        List<Fans> fansList = fansMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(fansList)) {
            return null;
        }
        return fansList;
    }

    private List<FansFollowVo> convertFansListToVoList(List<Fans> fansList) {
        List<FansFollowVo> FansFollowVoList = Lists.newArrayList();
        String uidArray[] = new String[fansList.size()];
        for (int i = 0; i < fansList.size(); i++) {
            uidArray[i] = fansList.get(i).getLikeUid().toString();
        }
        List<Room> roomList = roomService.queryRoomBeanListByUids(uidArray);
        Map<Long, Room> roomMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(roomList)) {
            for (Room room : roomList) {
                roomMap.put(room.getUid(), room);
            }
        }
        if (!CollectionUtils.isEmpty(roomList)) {
            for (Room room : roomList) {
                roomMap.put(room.getUid(), room);
            }
        }
        Map<Long, RoomVo> userInRoomVoMap = userInRoomService.getUserInRoomBatch(uidArray);
        Map<Long, NobleUsers> nobleMap = nobleUsersService.getNobleUserMap(uidArray);

        for (Fans fans : fansList) {
            FansFollowVo fansFollowVo = convertFansToVo(fans);
            if (fansFollowVo == null) {
                continue;
            }
            boolean valid = roomMap.get(fansFollowVo.getUid()) == null ? false : roomMap.get(fansFollowVo.getUid()).getValid();
            fansFollowVo.setValid(valid);
            RoomVo userInRoomVo = userInRoomVoMap.get(fansFollowVo.getUid());
            if (userInRoomVo != null) {
                fansFollowVo.setUserInRoom(userInRoomVo);
            }
            fansFollowVo.setNobleUsers(nobleMap.get(fansFollowVo.getUid()));
            FansFollowVoList.add(fansFollowVo);
        }
        return FansFollowVoList;
    }

    private FansFollowVo convertFansToVo(Fans fans) {
        FansFollowVo FansFollowVo = new FansFollowVo();
        FansFollowVo.setUid(fans.getLikeUid());
        Users user = usersService.getUsersByUid(fans.getLikeUid());
        if (user == null) {
            return null;
        }
        FansFollowVo.setAvatar(user.getAvatar());
        FansFollowVo.setNick(user.getNick());
        return FansFollowVo;
    }

}
