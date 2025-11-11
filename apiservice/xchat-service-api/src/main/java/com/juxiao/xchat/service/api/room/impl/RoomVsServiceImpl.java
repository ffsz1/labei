package com.juxiao.xchat.service.api.room.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.RoomVsDao;
import com.juxiao.xchat.dao.room.RoomVsTeamDao;
import com.juxiao.xchat.dao.room.RoomVsTeamUserDao;
import com.juxiao.xchat.dao.room.domain.RoomVsDO;
import com.juxiao.xchat.dao.room.domain.RoomVsTeamDO;
import com.juxiao.xchat.dao.room.domain.RoomVsTeamUserDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.enumeration.RoomVsStatus;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.im.bo.Custom;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMessage;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.manager.external.netease.ret.MicUserResult;
import com.juxiao.xchat.manager.external.netease.vo.MicUserVo;
import com.juxiao.xchat.service.api.room.RoomService;
import com.juxiao.xchat.service.api.room.RoomVsService;
import com.juxiao.xchat.service.api.room.bo.InitiatePKMsg;
import com.juxiao.xchat.service.api.room.bo.RoomVsParamBO;
import com.juxiao.xchat.service.api.room.bo.RoomVsTeamParam;

/**
 * @author:tp
 * @date:2020年10月28日
 */
@Service
public class RoomVsServiceImpl implements RoomVsService {

	private static final Logger log = LoggerFactory.getLogger(RoomVsServiceImpl.class);

	@Autowired
	private RedisManager redisManager;

	@Autowired
	private Gson gson;

	@Autowired
	private UsersManager usersManager;
	@Autowired
	private RoomService roomService;

	@Autowired
	private RoomVsDao roomVsDao;
	@Autowired
	private RoomVsTeamDao roomVsTeamDao;
	@Autowired
	private RoomVsTeamUserDao roomVsTeamUserDao;
	@Autowired
	private RoomManager roomManager;
	@Autowired
	private ImRoomManager imroomManager;
	@Autowired
	private NetEaseRoomManager netEaseRoomManager;

	@Override
	public WebServiceMessage initiatePK(RoomVsParamBO paramBO) throws Exception {
		if (paramBO == null || paramBO.getTeams() == null || paramBO.getRoomUid() == null
				|| paramBO.getStartUser() == null||paramBO.getMinutes()==null) {
			throw new WebServiceException(WebServiceCode.PARAM_ERROR);
		}
		Long roomUid=paramBO.getRoomUid();
		Long startUser=paramBO.getStartUser();
		RoomDTO roomDto = roomManager.getUserRoom(roomUid);
		if (roomDto == null) {
			throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
		}
		if (!imroomManager.isRoomManager(roomDto.getRoomId(), startUser)) {// 只有房主和管理员才有发起pk的权限
			// 管理员信息验证
			throw new WebServiceException(WebServiceCode.ROOM_NO_AUTHORITY);
		}
		List<RoomVsTeamParam> teams = paramBO.getTeams();
		MicUserResult micUserResult = netEaseRoomManager.queueList(roomDto.getRoomId());
		List<MicUserVo> list = micUserResult.getDesc().getList();

		for (RoomVsTeamParam t : teams) {
			for (long uid : t.getUids()) {
				boolean isContains = list.stream().filter(m -> m.getUid().compareTo(uid) == 0).findAny().isPresent();
				if (!isContains) {
					log.warn("pk选中的uid:>{}不是房间的麦上用户",uid);
					throw new WebServiceException(WebServiceCode.ROOM_NO_MICUSER);
				}
			}
		}
		RoomVsDO roomVsDO=new  RoomVsDO();
		roomVsDO.setRoomUid(roomUid);
		Integer seconds=paramBO.getMinutes()*60;
		roomVsDO.setSeconds(seconds);
		Date date=new Date();
		Date endTime=DateTimeUtils.addSecond(date, seconds);
		roomVsDO.setStartTime(date);
		roomVsDO.setEndTime(endTime);
		roomVsDO.setStatus(RoomVsStatus.ONGOING.getValue());
		roomVsDO.setStartUser(startUser);
		roomVsDO.setCreateTime(date);
		roomVsDO.setScoreType(paramBO.getScoreType());
		roomVsDao.insert(roomVsDO);
		Long vsId=roomVsDO.getId();
		for (RoomVsTeamParam t : teams) {
			RoomVsTeamDO teamDO=new RoomVsTeamDO();
			teamDO.setVsId(vsId);
			teamDO.setTeamIndex(t.getTeamIndex());
			teamDO.setCreateTime(date);
			teamDO.setScore((long) 0);
			roomVsTeamDao.insert(teamDO);
			for (long uid : t.getUids()) {
				RoomVsTeamUserDO teamUser=new RoomVsTeamUserDO();
				teamUser.setTeamId(teamDO.getId());
				teamUser.setUid(uid);
				teamUser.setCreateTime(date);
				teamUser.setScore((long) 0);
				roomVsTeamUserDao.insert(teamUser);
			}
		}
		
		Map<String, Object> data =  Maps.newHashMap();
		data.put("roomId", roomDto.getRoomId());
        data.put("timestamps", System.currentTimeMillis());
        InitiatePKMsg pkMsg=new InitiatePKMsg();
        data.put("initiatePKMsg", pkMsg);

		pushRoomMsg(roomDto.getRoomId(), new Custom(DefMsgType.RoomVs, DefMsgType.RoomVsStart, data));
		return WebServiceMessage.success("成功发起PK");
	}
	
	public void pushRoomMsg(Long roomId,Custom custom) {
		ImRoomMessage msginfo = new ImRoomMessage();
		msginfo.setRoomId(String.valueOf(roomId));
		msginfo.setCustom(custom);
		try {
		    imroomManager.pushRoomCustomMsg(msginfo);
		} catch (Exception e) {
		    log.error("发送消息异常，消息:>{}，异常信息：", msginfo, e);
		}

	}

}
