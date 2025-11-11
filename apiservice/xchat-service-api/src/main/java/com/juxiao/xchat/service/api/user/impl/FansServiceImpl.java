package com.juxiao.xchat.service.api.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.dao.user.FansDao;
import com.juxiao.xchat.dao.user.domain.FansDO;
import com.juxiao.xchat.dao.user.dto.FansDTO;
import com.juxiao.xchat.dao.user.dto.FansFollowDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.user.query.UserFansQuery;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.UserRoomManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.netease.NetEaseManager;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.bo.LikeSomeBodyMessageBO;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.user.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @class: FansServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
@Service
public class FansServiceImpl implements FansService {
    @Autowired
    private FansDao fansDao;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private NetEaseManager neteaseManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private UserRoomManager userRoomManager;
    @Autowired
    private UsersManager usersManager;

    @Autowired
    private ActiveMqManager activeMqManager;

    @Autowired
    private Gson gson;
    @Autowired
    private RedisManager redisManager;


    /**
     * @see com.juxiao.xchat.service.api.user.FansService#likeSomeBody(Long, Long)
     */
    @Override
    public void likeSomeBody(Long likeUid, Long likedUid) throws Exception {
        validateLikeSomeBody(likeUid, likedUid);
        FansDTO fans = fansDao.getUserLike(likeUid, likedUid);
        // 喜欢关系已经存在,不处理
        if (fans != null) {
            return;
        }
        // 是否已经被喜欢（相互喜欢）
        int likeBetweenCount = fansDao.countLikeBetween(likedUid, likeUid);
        NetEaseRet ret;
        if (likeBetweenCount > 0) {
            // 相互喜欢，双方成为好友
            ret = neteaseManager.addFriends(likeUid, likedUid, (byte) 1, "已经是好友了");
        } else {
            ret = neteaseManager.addFriends(likeUid, likedUid, (byte) 2, "喜欢了你");
        }
        if (ret.getCode() != 200) {
            throw new WebServiceException("添加好友操作异常：" + ret.getDesc());
        }
        // 喜欢关系不存在,新增一条喜欢关系
        FansDO fansDo = new FansDO();
        fansDo.setLikeUid(likeUid);
        fansDo.setLikedUid(likedUid);
        fansDo.setCreateTime(new Date());
        fansDao.save(fansDo);
        usersManager.updateFollowNum(likeUid);
        usersManager.updateFansNum(likedUid);

        LikeSomeBodyMessageBO messageBo = new LikeSomeBodyMessageBO();
        messageBo.setLikeUid(likeUid);
        messageBo.setLikedUid(likedUid);
        messageBo.setCreateDate(new Date());
        activeMqManager.sendQueueMessage(MqDestinationKey.LIKE_SOME_BODY_QUEUE, gson.toJson(messageBo));
    }

    /**
     * @see com.juxiao.xchat.service.api.user.FansService#cancelLikeSomeBody(Long, Long)
     */
    @Override
    public void cancelLikeSomeBody(Long likeUid, Long cancelLikeUid) throws Exception {
        validateLikeSomeBody(likeUid, cancelLikeUid);

        FansDTO fansDto = fansDao.getUserLike(likeUid, cancelLikeUid);
        // 喜欢关系不存在,不处理
        if (fansDto == null) {
            return;
        }

        // 删除喜欢关系数据
        fansDao.delete(fansDto.getFanId());
        // 更新取消喜欢人的关注数量
        usersManager.updateFollowNum(likeUid);
        // 更新被取消被喜欢人的粉丝数量
        usersManager.updateFansNum(cancelLikeUid);
        neteaseManager.deleteFriends(likeUid, cancelLikeUid);
    }

    /**
     * @see com.juxiao.xchat.service.api.user.FansService#checkUserLike(Long, Long)
     */
    @Override
    public boolean checkUserLike(Long uid, Long isLikeUid) throws WebServiceException {
        if (uid == null || uid == 0L || isLikeUid == null || isLikeUid == 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        FansDTO fansDto = fansDao.getUserLike(uid, isLikeUid);
        return fansDto != null;
    }

    /**
     * @see com.juxiao.xchat.service.api.user.FansService#listFollowing(Long, Integer, Integer)
     */
    @Override
    public List<FansFollowDTO> listFollowing(Long uid, Integer pageNo, Integer pageSize) throws WebServiceException {
        if (uid == null || uid == 0L) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UserFansQuery query = new UserFansQuery();
        query.setLikeUid(uid);
        query.setPage(pageNo, pageSize);

        List<FansFollowDTO> list = fansDao.listFollowOrFans(query);

        UsersDTO usersDto;
        RoomDTO roomDto;

        for (FansFollowDTO followDto : list) {
            usersDto = usersManager.getUser(followDto.getUid());
            if (usersDto == null) {
                continue;
            }

            followDto.setUid(followDto.getUid());
            followDto.setAvatar(usersDto.getAvatar());
            followDto.setFansNum(usersDto.getFansNum());
            followDto.setNick(usersDto.getNick());
            followDto.setGender(usersDto.getGender());
            followDto.setErbanNo(usersDto.getErbanNo());
            followDto.setUserVoice(usersDto.getUserVoice());
            followDto.setVoiceDuration(usersDto.getVoiceDura());
            followDto.setExperLevel(levelManager.getUserExperienceLevelSeq(followDto.getUid()));
            followDto.setCharmLevel(levelManager.getUserCharmLevelSeq(followDto.getUid()));
            
            String roomStateCountKey=RedisKey.imxinpi_uid_map_roomid_key.name()+"_" +followDto.getUid();
        	Long roomStateCount=redisManager.zcount(roomStateCountKey, 0, Integer.MAX_VALUE);
        	if(roomStateCount>0) {
        		followDto.setRoomState(1);
        	}else {
        		followDto.setRoomState(0);
        	}

            RoomUserinDTO roomUserinDTO = userRoomManager.getUserInRoom(followDto.getUid());
            if (roomUserinDTO != null) {
                roomDto = roomManager.getUserRoom(roomUserinDTO.getUid());
                if (roomDto != null && roomDto.getValid() && roomDto.getOnlineNum() != null && roomDto.getOnlineNum().intValue() > 0 && !roomDto.getOnlineNum().equals(roomManager.getRobotNum(roomUserinDTO.getUid()))) {
                    roomUserinDTO.setOnlineNum(roomDto.getOnlineNum());
                    followDto.setUserInRoom(roomUserinDTO);
                }
            }

            roomDto = roomManager.getUserRoom(followDto.getUid());
            if (roomDto != null) {
                followDto.setRoomBack(roomDto.getBackPic());
                followDto.setType(roomDto.getType());
                followDto.setValid(roomDto.getValid());
                followDto.setOperatorStatus(roomDto.getOperatorStatus());
                followDto.setTitle(roomDto.getTitle());
            }
        }
        
        Collections.sort(list, (dto1, dto2) -> {
            int v1 = dto1.getUserInRoom() == null ? 0 : 1;
            int v2 = dto2.getUserInRoom() == null ? 0 : 1;
            return Integer.compare(v2, v1);
        });
        return list;
    }

    @Override
    public Map<String, Object> listFans(Long uid, Integer pageNo, Integer pageSize) throws WebServiceException {
        if (uid == null || uid == 0L) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UserFansQuery query = new UserFansQuery();
        query.setLikedUid(uid);

        int recordCount = fansDao.coutFollowOrFans(query);
        int pageCount = (recordCount + pageSize - 1) / pageSize;

        Map<String, Object> result = new HashMap<>();
        result.put("pageCount", pageCount);

        query.setPage(pageNo, pageSize);
        List<FansFollowDTO> list = fansDao.listFollowOrFans(query);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        RoomDTO roomDto;
        UsersDTO user;
        for (FansFollowDTO followDto : list) {
            user = usersManager.getUser(followDto.getUid());
            if (user == null) {
                continue;
            }
            followDto.setAvatar(user.getAvatar());
            followDto.setNick(user.getNick());
            followDto.setExperLevel(levelManager.getUserExperienceLevelSeq(followDto.getUid()));
            followDto.setErbanNo(user.getErbanNo());
            followDto.setCharmLevel(levelManager.getUserCharmLevelSeq(followDto.getUid()));
            RoomUserinDTO roomUserinDTO = userRoomManager.getUserInRoom(followDto.getUid());
            if (roomUserinDTO != null) {
                roomDto = roomManager.getUserRoom(roomUserinDTO.getUid());
                if (roomDto != null && roomDto.getValid() && roomDto.getOnlineNum() != null && roomDto.getOnlineNum().intValue() > 0 && !roomDto.getOnlineNum().equals(roomManager.getRobotNum(roomUserinDTO.getUid()))) {
                    roomUserinDTO.setOnlineNum(roomDto.getOnlineNum());
                    followDto.setUserInRoom(roomUserinDTO);
                }
            }

            roomDto = roomManager.getUserRoom(followDto.getUid());
            followDto.setValid(roomDto == null ? false : roomDto.getValid());
            if (roomDto != null) {
                followDto.setType(roomDto.getType());
                followDto.setValid(roomDto.getValid());
                followDto.setOperatorStatus(roomDto.getOperatorStatus());
                followDto.setTitle(roomDto.getTitle());
                followDto.setRoomBack(roomDto.getBackPic());
            }
        }
        Collections.sort(list, (dto1, dto2) -> {
            int v1 = dto1.getUserInRoom() == null ? 0 : 1;
            int v2 = dto2.getUserInRoom() == null ? 0 : 1;
            return Integer.compare(v2, v1);
        });

        result.put("fansList", list);

        return result;
    }

    @Override
    public List<FansFollowDTO> listFans(Long uid) throws WebServiceException {
        if (uid == null || uid == 0L) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UserFansQuery query = new UserFansQuery();
        query.setLikedUid(uid);
        return fansDao.listFollowOrFans(query);
    }


    /**
     * 关注动作入参验证
     *
     * @param uid
     * @param likedUid
     * @throws WebServiceException
     */
    private void validateLikeSomeBody(Long uid, Long likedUid) throws WebServiceException {
        if (uid == null || likedUid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        UsersDTO likedUsersDto = usersManager.getUser(uid);
        if (likedUsersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        if (uid.equals(likedUid)) {
            throw new WebServiceException(WebServiceCode.NOT_LIKE_ONESELF);
        }
    }
}
