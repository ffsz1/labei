package com.juxiao.xchat.service.api.room.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.RoomPkVoteDao;
import com.juxiao.xchat.dao.room.domain.RoomPkVoteDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteHistroyDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteResultDTO;
import com.juxiao.xchat.dao.room.enumeration.RoomPkVoteType;
import com.juxiao.xchat.dao.room.query.PkVotesListQuery;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.RoomPkVoteManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import com.juxiao.xchat.service.api.item.params.Custom;
import com.juxiao.xchat.service.api.item.params.MsgInfo;
import com.juxiao.xchat.service.api.room.RoomPkVoteService;
import com.juxiao.xchat.service.api.room.bo.RoomPkVoteSaveBO;
import com.juxiao.xchat.service.api.room.vo.PkVoteCancelUserVO;
import com.juxiao.xchat.service.api.room.vo.PkVoteCancelVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomPkVoteServiceImpl implements RoomPkVoteService {
    private final Gson gson = new Gson();
    private final Logger logger = LoggerFactory.getLogger(RoomPkVoteService.class);
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private RoomPkVoteDao pkvoteDao;
    @Autowired
    private NetEaseRoomManager neteaseRoomManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RoomPkVoteManager pkvoteManager;
    @Autowired
    private UsersManager usersManager;

    @Autowired
    private ImRoomManager imRoomManager;

    @Override
    public Long save(RoomPkVoteSaveBO saveBo) throws WebServiceException {
        if (saveBo.getRoomId() == null || saveBo.getRoomId() == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (!RoomPkVoteType.isSupport(saveBo.getPkType())) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (saveBo.getOpUid() == null || saveBo.getOpUid() == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (saveBo.getUid() == null || saveBo.getUid() == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (saveBo.getPkUid() == null || saveBo.getPkUid() == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (saveBo.getUid().longValue() == saveBo.getPkUid().longValue()) {
            throw new WebServiceException("不能和本人PK");
        }

        if (saveBo.getExpireSeconds() == null || saveBo.getExpireSeconds() <= 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        RoomDTO roomDto = roomManager.getRoom(saveBo.getRoomId());
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        RoomPkVoteDTO voteDto = pkvoteManager.getRoomPkVote(saveBo.getRoomId());
        if (voteDto != null && new Date().before(DateTimeUtils.addSecond(voteDto.getCreateTime(), voteDto.getExpireSeconds()))) {
            throw new WebServiceException("当前房间已存在投票");
        }

        UsersDTO user = usersManager.getUser(saveBo.getUid());
        if (user == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        UsersDTO pkUser = usersManager.getUser(saveBo.getPkUid());
        if (pkUser == null) {
            throw new WebServiceException("要PK的用户不存在");
        }

        String lockVal = redisManager.lock(RedisKey.room_pk_save_lock.getKey(String.valueOf(saveBo.getRoomId())), 3000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException("休息一下，您操作得太频繁了");
        }

        int intervalPKCount = pkvoteDao.countIntervalPK(saveBo.getRoomId());
        if (intervalPKCount > 0) {
            throw new WebServiceException("两次PK间隔时间为15秒");
        }

        Date date = new Date();
        RoomPkVoteDO voteDo = new RoomPkVoteDO();
        voteDo.setRoomId(saveBo.getRoomId());
        voteDo.setPkType(saveBo.getPkType());
        voteDo.setUid(saveBo.getUid());
        voteDo.setVoteCount(0);
        voteDo.setPkUid(saveBo.getPkUid());
        voteDo.setPkVoteCount(0);
        voteDo.setExpireSeconds(saveBo.getExpireSeconds());
        voteDo.setCreateTime(date);
        voteDo.setUpdateTime(date);
        pkvoteDao.save(voteDo);

        voteDto = new RoomPkVoteDTO();
        BeanUtils.copyProperties(voteDo, voteDto);
        redisManager.hset(RedisKey.room_pk_vote.getKey(), String.valueOf(saveBo.getRoomId()), gson.toJson(voteDo));

        return voteDo.getId();
    }

    @Override
    public void cancel(Long roomId, Long uid) throws WebServiceException {
        if (roomId == null || roomId == 0 || uid == null || uid == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        RoomPkVoteDTO pkVote = pkvoteManager.getRoomPkVote(roomId);
        if (pkVote == null) {
            throw new WebServiceException("投票不存在，无法进行取消");
        }

        String lockVal = redisManager.lock(RedisKey.room_pk_save_lock.getKey(String.valueOf(roomId)), 3000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException("休息一下，您操作得太频繁了");
        }

        pkvoteDao.delete(pkVote.getId());
        redisManager.hdel(RedisKey.room_pk_vote.getKey(), String.valueOf(pkVote.getRoomId()));
        pkvoteManager.clearRoomVote(pkVote);

        PkVoteCancelVO cancelVo = new PkVoteCancelVO();
        UsersDTO opUsersDto = usersManager.getUser(uid);
        PkVoteCancelUserVO opUserVo = new PkVoteCancelUserVO(uid, opUsersDto);
        cancelVo.setOpUser(opUserVo);

        UsersDTO usersDto = usersManager.getUser(pkVote.getUid());
        PkVoteCancelUserVO userVo = new PkVoteCancelUserVO(pkVote.getUid(), usersDto);
        cancelVo.setUser(userVo);

        UsersDTO pkUsersDto = usersManager.getUser(pkVote.getPkUid());
        PkVoteCancelUserVO pkUserVo = new PkVoteCancelUserVO(pkVote.getPkUid(), pkUsersDto);
        cancelVo.setPkUser(pkUserVo);
        this.sendPkMsg(roomId, DefMsgType.RoomPkCancel, cancelVo);
    }

    @Override
    public RoomPkVoteResultDTO userVote(Long roomId, Long uid, Long voteUid) throws WebServiceException {
        if (roomId == null || roomId == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (uid == null || voteUid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (uid.intValue() == voteUid.intValue()) {
            throw new WebServiceException("不能给自己投票");
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        RoomPkVoteDTO voteDto = pkvoteManager.getRoomPkVote(roomId);
        if (voteDto == null) {
            throw new WebServiceException("当前房间还没有发起投票");
        }

        if (!RoomPkVoteType.USER_COUNT_PK.compareToValue(voteDto.getPkType())) {
            throw new WebServiceException("不支持的投票类型");
        }

        if (new Date().after(DateTimeUtils.addSecond(voteDto.getCreateTime(), voteDto.getExpireSeconds() - 1))) {
            throw new WebServiceException("当前房间投票已过期");
        }

        Set<Long> uidVoteSet = pkvoteManager.getRoomVoteSet(voteDto.getId(), voteDto.getUid());
        if (uidVoteSet.contains(uid)) {
            throw new WebServiceException("您已经投过票了");
        }

        Set<Long> pkUidVoteSet = pkvoteManager.getRoomVoteSet(voteDto.getId(), voteDto.getPkUid());
        if (pkUidVoteSet.contains(uid)) {
            throw new WebServiceException("您已经投过票了");
        }

        String lockKey = RedisKey.room_pk_lock.getKey(roomId + "");
        String lock = null;
        try {
            lock = redisManager.lock(lockKey, 10000);
            if (StringUtils.isEmpty(lock)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            Set<Long> voteSet = pkvoteManager.getRoomVoteSet(voteDto.getId(), voteUid);
            voteSet.add(uid);
            redisManager.hset(RedisKey.room_pk_vote_set.getKey(), voteDto.getId() + "_" + voteUid, gson.toJson(voteSet));

            RoomPkVoteDO voteDo = new RoomPkVoteDO();
            voteDo.setId(voteDto.getId());
            if (voteUid.intValue() == voteDto.getUid().intValue()) {
                voteDo.setVoteCount(voteSet.size());
                voteDto.setVoteCount(voteSet.size());
            } else if (voteUid.intValue() == voteDto.getPkUid().intValue()) {
                voteDo.setPkVoteCount(voteSet.size());
                voteDto.setPkVoteCount(voteSet.size());
            } else {
                throw new WebServiceException("不存在的投票对象");
            }
            voteDo.setId(voteDto.getId());
            redisManager.hset(RedisKey.room_pk_vote.getKey(), String.valueOf(roomId), gson.toJson(voteDto));
            pkvoteDao.update(voteDo);

            RoomPkVoteResultDTO resultDto = pkvoteManager.getPkVoteResult(voteDto);
            return resultDto;
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(lockKey, lock);
        }
    }

    @Override
    public RoomPkVoteResultDTO getPkVote(Long roomId) throws WebServiceException {
        if (roomId == null || roomId == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        RoomPkVoteDTO voteDto = pkvoteManager.getRoomPkVote(roomId);
        if (voteDto == null) {
            throw new WebServiceException("当前房间还没有发起投票或已过期");
        }

        RoomPkVoteResultDTO resultDto = pkvoteManager.getPkVoteResult(voteDto);
        return resultDto;
    }

    @Override
    public List<RoomPkVoteHistroyDTO> listPkVotes(Long roomId, String os, Integer pageNum, Integer pageSize) {
        // FIXME: iOS要求改成50条
        if ("iOS".equalsIgnoreCase(os)) {
            pageSize = 50;
        }
        List<RoomPkVoteHistroyDTO> list = pkvoteDao.listPkVotes(new PkVotesListQuery(roomId, pageNum, pageSize));
        UsersDTO users;
        for (RoomPkVoteHistroyDTO result : list) {
            users = usersManager.getUser(result.getUid());
            result.setNick(users == null ? "" : users.getNick());
            result.setAvatar(users == null ? "" : users.getAvatar());

            users = usersManager.getUser(result.getPkUid());
            result.setPkNick(users == null ? "" : users.getNick());
            result.setPkAvatar(users == null ? "" : users.getAvatar());
        }
        return list;
    }

    @Override
    public int checkFinish() {
        String lockVal = redisManager.lock(RedisKey.room_pk_task_lock.getKey(), 10000);
        if (StringUtils.isEmpty(lockVal)) {
            return 0;
        }

        try {
            Map<String, String> pkVotes = redisManager.hgetAll(RedisKey.room_pk_vote.getKey());
            if (pkVotes == null || pkVotes.size() == 0) {
                return 0;
            }

            int result = 0;
            Iterator<Map.Entry<String, String>> iterator = pkVotes.entrySet().iterator();
            RoomPkVoteDTO pkVote;
            Map.Entry<String, String> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                pkVote = gson.fromJson(entry.getValue(), RoomPkVoteDTO.class);
                if (new Date().before(DateTimeUtils.addSecond(pkVote.getCreateTime(), pkVote.getExpireSeconds()))) {
                    continue;
                }

                pkvoteManager.clearRoomVote(pkVote);

                Map<String, Object> data = new HashMap<>();
                data.put("voteId", pkVote.getId());
                this.sendPkMsg(pkVote.getRoomId(), DefMsgType.RoomPkResult, data);
                result++;
            }
            return result;
        } catch (Exception e) {
            logger.error("[ 房间PK ] 处理异常：", e);
            return 0;
        } finally {
            redisManager.unlock(RedisKey.room_pk_task_lock.getKey(), lockVal);
        }
    }

    private void sendPkMsg(Long roomId, int msgType, Object data) {
        try {
            JSONObject object = new JSONObject();
            MsgInfo msgInfo = new MsgInfo();
            Custom custom = new Custom();
            custom.setSecond(msgType);
            custom.setFirst(DefMsgType.RoomPk);
            custom.setData(data);
            msgInfo.setRoomId(roomId);
            msgInfo.setCustom(custom);
            object.put("msg_info",msgInfo);
            String body = object.toJSONString();
            imRoomManager.pushRoomMsg(body);
//            Attach attach = new Attach();
//            attach.setFirst(DefMsgType.RoomPk);
//            attach.setSecond(msgType);
//            attach.setData(data);
//            neteaseRoomManager.sendChatRoomMsg(roomId, UUIDUtils.get(), systemConf.getSecretaryUid(), 100, JSON.toJSONString(attach));
        } catch (Exception e) {
            logger.error("[ 房间PK ] 发送消息异常，消息类型:>{}，异常信息：", msgType, e);
        }
    }

}


