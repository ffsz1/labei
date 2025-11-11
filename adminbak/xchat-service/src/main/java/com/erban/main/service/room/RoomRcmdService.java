package com.erban.main.service.room;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.mybatismapper.RoomRcmdMapper;
import com.erban.main.mybatismapper.RoomRcmdPoolMapper;
import com.erban.main.param.room.RoomRcmdParam;
import com.erban.main.service.SysConfService;
import com.erban.main.service.room.vo.RoomRcmdVo;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.RoomVo;
import com.erban.main.wechat.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountLoginRecordMapper;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class RoomRcmdService {

    private static final String RCMD_CONF_ID = "rcmd_room_option";

    private final Gson gson = new Gson();

    private final Random random = new Random();
    @Autowired
    private AccountLoginRecordMapper loginRecordMapper;
    @Autowired
    private RoomRcmdMapper rcmdMapper;
    @Autowired
    private RoomRcmdPoolMapper rcmdPoolMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private SysConfService confService;


    public BusiResult save(RoomRcmdParam param) {
        if (param == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        if (param.getRcmdType() == null || StringUtils.isBlank(param.getStartDate()) || StringUtils.isBlank(param.getEndDate())) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        if (param.getRcmdType() != 1 && param.getRcmdType() != 2) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (param.getRcmdType() == 1 && (param.getRcmdRoomTags() == null || param.getRcmdRoomTags().size() == 0)) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        if (param.getRcmdType() == 2 && StringUtils.isBlank(param.getRcmdRooms())) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        Date startDate;
        try {
            startDate = DateUtil.str2Date(param.getStartDate(), DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS);
        } catch (ParseException e) {
            return new BusiResult(BusiStatus.ROOM_RCMD_TIME_FORMAT_ERROR);
        }

        Date endDate;
        try {
            endDate = DateUtil.str2Date(param.getEndDate(), DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS);
        } catch (ParseException e) {
            return new BusiResult(BusiStatus.ROOM_RCMD_TIME_FORMAT_ERROR);
        }

        if (endDate.getTime() <= new Date().getTime()) {
            return new BusiResult(BusiStatus.ROOM_RCMD_TIME_INVALID);
        }

        if (startDate.getTime() >= endDate.getTime()) {
            return new BusiResult(BusiStatus.ROOM_RCMD_TIME_INVALID);
        }

        int conflictRcmdCount = rcmdMapper.countConflictRcmd(startDate, endDate, param.getRcmdId());
        if (conflictRcmdCount > 0) {
            return new BusiResult(BusiStatus.ROOM_RCMD_TIME_CONFLICT);
        }

        RoomRcmd rcmd = new RoomRcmd();
        rcmd.setRcmdId(param.getRcmdId());
        rcmd.setMinOnline(param.getMinOnline());
        rcmd.setStartDate(startDate);
        rcmd.setEndDate(endDate);
        rcmd.setRcmdType(param.getRcmdType());
        if (param.getRcmdId() == null) {
            rcmdMapper.insert(rcmd);
        } else {
            rcmdMapper.updateByPrimaryKey(rcmd);
        }

        RoomRcmdPoolExample poolExample = new RoomRcmdPoolExample();
        poolExample.createCriteria().andRcmdIdEqualTo(rcmd.getRcmdId());
        rcmdPoolMapper.deleteByExample(poolExample);
        // 1，根据房间类型推荐；
        List<RoomRcmdPool> list = new ArrayList<>();
        if (param.getRcmdType() == 1) {
            for (Integer tagId : param.getRcmdRoomTags()) {
                list.add(new RoomRcmdPool(rcmd.getRcmdId(), tagId.longValue()));
            }
        } else if (param.getRcmdType() == 2) { // 2根据房间ID推荐
            String[] roomUIds = param.getRcmdRooms().split("@");
            for (String roomUid : roomUIds) {
                list.add(new RoomRcmdPool(rcmd.getRcmdId(), Long.valueOf(roomUid)));
            }
        }

        if (list.size() > 0) {
            rcmdPoolMapper.inserts(list);
        }
        this.cacheRcmdRoom();
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public void delete(Integer rcmdId) {
        if (rcmdId == null) {
            return;
        }
        rcmdMapper.deleteByPrimaryKey(rcmdId);
        RoomRcmdPoolExample poolExample = new RoomRcmdPoolExample();
        poolExample.createCriteria().andRcmdIdEqualTo(rcmdId);
        rcmdPoolMapper.deleteByExample(poolExample);
        this.cacheRcmdRoom();
    }

    /**
     * 获取推荐房间
     *
     * @param uid
     * @return
     */
    public BusiResult<RoomVo> getUserRcmdRoom(Long uid) {
        if (uid == null) {
            return new BusiResult<>(BusiStatus.PARAMERROR);
        }

        SysConf conf = confService.getSysConfById(RCMD_CONF_ID);
        if (conf == null || !Boolean.valueOf(conf.getConfigValue())) {
            return new BusiResult<>(BusiStatus.ROOM_RCMD_OPTION_CLOSE);
        }

        // 判断是否是新用户
        int loginCount = loginRecordMapper.countUserLogin(uid);
        if (loginCount > 5) {
            return new BusiResult<>(BusiStatus.ROOM_RCMD_NOT_NEW);
        }

        // 查询当前是否存在有推荐的时间段
        long poolSize = jedisService.size(RedisKey.rcmd_room_pool.getKey());
        if (poolSize == 0) {
            return new BusiResult<>(BusiStatus.ROOM_RCMD_POOL_EMPTY);
        }

        int index = random.nextInt((int) poolSize);
        List<String> list = jedisService.lrange(RedisKey.rcmd_room_pool.getKey(), index, index);
        if (list == null || list.size() == 0 || StringUtils.isBlank(list.get(0))) {
            return new BusiResult<>(BusiStatus.ROOM_RCMD_NO_INFO);
        }

        RoomVo roomVo = gson.fromJson(list.get(0), RoomVo.class);
        return new BusiResult<>(BusiStatus.SUCCESS, roomVo);
    }

    public PageInfo<RoomRcmd> listRcmdRooms(int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        RoomRcmdExample example = new RoomRcmdExample();
        example.setOrderByClause("start_date ");
        List<RoomRcmd> list = rcmdMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    public BusiResult<RoomRcmdVo> getRcmdRoom(Integer rcmdId) {
        if (rcmdId == null) {
            return new BusiResult<>(BusiStatus.PARAMERROR);
        }

        RoomRcmd rcmd = rcmdMapper.selectByPrimaryKey(rcmdId);
        if (rcmd == null) {
            return new BusiResult<>(BusiStatus.PARAMETERILLEGAL);
        }

        RoomRcmdVo rcmdVo = new RoomRcmdVo();
        rcmdVo.setRcmdId(rcmdId);
        rcmdVo.setMinOnline(rcmd.getMinOnline());
        rcmdVo.setRcmdType(rcmd.getRcmdType());
        rcmdVo.setStartDate(rcmd.getStartDate());
        rcmdVo.setEndDate(rcmd.getEndDate());

        RoomRcmdPoolExample example = new RoomRcmdPoolExample();
        example.createCriteria().andRcmdIdEqualTo(rcmdId);
        List<RoomRcmdPool> list = rcmdPoolMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            return new BusiResult<>(BusiStatus.SUCCESS, rcmdVo);
        }

        if (rcmd.getRcmdType() == 1) {
            List<Integer> rcmdRoomTags = Lists.newArrayList();
            for (RoomRcmdPool pool : list) {
                rcmdRoomTags.add(pool.getRoomFkId().intValue());
            }
            rcmdVo.setRcmdRoomTags(rcmdRoomTags);
        } else if (rcmd.getRcmdType() == 2) {
            StringBuilder builder = new StringBuilder();
            for (RoomRcmdPool pool : list) {
                builder.append(pool.getRoomFkId()).append("@");
            }
            rcmdVo.setRcmdRooms(builder.substring(0, builder.length() - 1));
        }
        return new BusiResult<>(BusiStatus.SUCCESS, rcmdVo);
    }

    public BusiResult updateRcmdOption(Boolean isOpen) {
        SysConf sysConf = confService.getSysConfById(RCMD_CONF_ID);
        if (sysConf == null) {
            sysConf = new SysConf();
            sysConf.setConfigId(RCMD_CONF_ID);
            sysConf.setConfigName("新用户房间推荐开关");
            sysConf.setConfigValue(isOpen.toString());
            sysConf.setNameSpace("room");
            sysConf.setConfigStatus((byte) 1);
            confService.addSysConf(sysConf);
        } else {
            confService.setConfValueById(RCMD_CONF_ID, isOpen.toString());
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public void cacheRcmdRoom() {
        jedisService.del(RedisKey.rcmd_room_pool.getKey());

        Date now = new Date();
        RoomRcmdExample example = new RoomRcmdExample();
        RoomRcmdExample.Criteria criteria = example.createCriteria();
        criteria.andEndDateGreaterThanOrEqualTo(now);
        criteria.andStartDateLessThanOrEqualTo(now);
        List<RoomRcmd> list = rcmdMapper.selectByExample(example);
        if (list == null || list.size() == 0 || list.get(0) == null) {
            return;
        }

        RoomRcmd rcmd = list.get(0);
        RoomRcmdPoolExample poolExample = new RoomRcmdPoolExample();
        poolExample.createCriteria().andRcmdIdEqualTo(rcmd.getRcmdId());
        List<Long> pools = rcmdPoolMapper.listRoomFkId(rcmd.getRcmdId());
        if (pools == null || pools.size() == 0) {
            return;
        }

        if (rcmd.getRcmdType() == 2) {
            Room room;
            for (Long roomUid : pools) {
                room = roomService.getRoomByUid(roomUid);
                if (room != null) {
                    this.push(room);
                }
            }
            return;
        }

        // 查询推荐的房间
        RoomExample roomExample = new RoomExample();
        List<Integer> tagIdPools = new ArrayList<>();
        for (Long id : pools) {
            tagIdPools.add(id.intValue());
        }

        roomExample.createCriteria().andTagIdIn(tagIdPools).andOnlineNumGreaterThanOrEqualTo(rcmd.getMinOnline());
        List<Room> rooms = roomMapper.selectByExample(roomExample);
        if (rooms == null || rooms.size() == 0) {
            return;
        }

        for (Room room : rooms) {
            this.push(room);
        }
    }

    private void push(Room room) {
        RoomVo roomVo = new RoomVo();
        roomVo.setRoomId(room.getRoomId());
        roomVo.setUid(room.getUid());
        roomVo.setTitle(room.getTitle());
        roomVo.setAvatar(room.getAvatar());
        jedisService.lpush(RedisKey.rcmd_room_pool.getKey(), gson.toJson(roomVo));
    }
}
