package com.erban.admin.main.service.gift;

import com.erban.admin.main.base.RedisSupportService;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.HeadwearRecordDTO;
import com.erban.admin.main.dto.TreasureBoxReportDTO;
import com.erban.admin.main.mapper.GiveHeadwearMapperMgr;
import com.erban.admin.main.vo.GiveHeadwearVo;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import com.erban.admin.main.vo.TreasureBoxVo;
import com.erban.main.base.BaseMapper;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.HeadwearGetRecordMapper;
import com.erban.main.mybatismapper.HeadwearMapper;
import com.erban.main.mybatismapper.HeadwearPurseRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.headwear.HeadwearPurseService;
import com.erban.main.service.headwear.HeadwearService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

@Service
public class HeadwearAdminService extends RedisSupportService<Headwear, HeadwearExample> {
    @Value("${giftCarSecret}")
    private String giftCarSecret;

    @Autowired
    private HeadwearMapper headwearMapper;

    @Autowired
    private HeadwearService headwearService;

    @Autowired
    private HeadwearPurseService headwearPurseService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    @Autowired
    private HeadwearPurseRecordMapper headwearPurseRecordMapper;

    @Autowired
    private HeadwearGetRecordMapper headwearGetRecordMapper;

    @Autowired
    private GiveHeadwearMapperMgr giveHeadwearMapperMgr;

    @Autowired
    private UsersService usersService;

    private Gson gson = new Gson();

    /**
     * 获取所有有效的头饰列表
     *
     * @return
     */
    public List<Headwear> getAllGift() {
        HeadwearExample example = new HeadwearExample();
        example.createCriteria().andHeadwearStatusEqualTo((byte) 1);
        return headwearMapper.selectByExample(example);
    }

    /**
     * 获取所有头饰列表
     *
     * @return
     */
    public List<Headwear> getAllHeadwear() {
        HeadwearExample example = new HeadwearExample();
        return headwearMapper.selectByExample(example);
    }

    /**
     * 获取官方赠送头饰记录
     *
     * @return
     */
    public PageInfo<HeadwearGetRecord> getHeadwearRecord(int pageNum, int pageSize, String erbanNo, String startDate, String endDate) {
        // 设置查询条件
        HeadwearGetRecordExample example = new HeadwearGetRecordExample();
        // 获取对应拉贝号的UID列表
        List<Long> uids = new ArrayList<>();
        if (!StringUtils.isBlank(erbanNo)) {
            String[] erbanNos = erbanNo.split("\n");
            for (int i = 0; i < erbanNos.length; i++) {
                Users users = usersService.getUsersByErBanNo(Long.valueOf(erbanNos[i]));
                uids.add(users.getUid());
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        // 查询官方赠送头饰记录
        List<HeadwearGetRecord> resultList = headwearGetRecordMapper.selectRecord(uids, startDate, endDate);
        return new PageInfo<>(resultList);
    }

    /**
     * 获取礼物列表
     *
     * @param status 礼物状态(1、有效 2、无效)
     * @param page
     * @param size
     * @return
     */
    public PageInfo<Headwear> getGiftByPage(byte status, String headwearName, int page, int size) {
        PageHelper.startPage(page, size);
        HeadwearExample example = new HeadwearExample();
        // 判断是否需要增加状态条件
        if (status != 0) {
            example.createCriteria().andHeadwearStatusEqualTo(status);
        }
        if (StringUtils.isNotBlank(headwearName)) {
            example.setLikeName(headwearName);
        }
        example.setOrderByClause(" seq_no asc");
        return new PageInfo(headwearMapper.selectByExample(example));
    }

    public PageInfo<GiveHeadwearVo> getList(Long erbanNo, int page, int size) {
        PageHelper.startPage(page, size);
        RoomRobotGroupParam roomRobotGroupParam = new RoomRobotGroupParam();
        roomRobotGroupParam.setErbanNo(erbanNo);
        List<GiveHeadwearVo> giveGiftcarVoList = giveHeadwearMapperMgr.selectByParam(roomRobotGroupParam);
        return new PageInfo(giveGiftcarVoList);
    }

    public BusiResult give(String headwearUid, Integer date) {
        String[] list = headwearUid.split(",");
        if (list.length < 2) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Users users = usersService.getUsersByUid(Long.valueOf(list[0]));
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Headwear headwear = headwearService.getOneByJedisId(list[1]);
        if (headwear == null) {
            return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
        }
        String purse = headwearPurseService.getPurse(Long.valueOf(list[0]));
        if (StringUtils.isNotBlank(purse)) {
            if (!com.erban.main.util.StringUtils.splitToList(purse, ",").contains(list[1])) {
                purse += "," + list[1];
            }
        } else {
            purse = "" + list[1];
        }
        jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), list[0], purse);
        HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(list[0], list[1]);
        if (headwearPurseRecord == null) {
            headwearPurseRecord = new HeadwearPurseRecord();
            headwearPurseRecord.setUid(Long.valueOf(list[0]));
            headwearPurseRecord.setHeadwearId(Long.valueOf(list[1]));
            headwearPurseRecord.setTotalGoldNum(0L);
            headwearPurseRecord.setHeadwearDate(date);
            headwearPurseRecord.setIsUse(new Byte("0"));
            headwearPurseRecord.setCreateTime(new Date());
            headwearPurseRecordMapper.insertSelective(headwearPurseRecord);
        } else {
            headwearPurseRecord.setHeadwearDate(headwearPurseRecord.getHeadwearDate() + date);
            headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
        }
        jedisService.hset(RedisKey.headwear_purse.getKey(), list[0] + "_" + list[1],
                gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
        HeadwearGetRecord headwearGetRecord = new HeadwearGetRecord();
        headwearGetRecord.setUid(Long.valueOf(list[0]));
        headwearGetRecord.setHeadwearId(Long.valueOf(list[1]));
        headwearGetRecord.setHeadwearDate(date);
        headwearGetRecord.setType(new Byte("2"));
        headwearGetRecord.setCreateTime(new Date());
        headwearGetRecordMapper.insert(headwearGetRecord);
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(list[0]);
        neteaseSendMsgParam.setBody("恭喜您，获得官方赠送头饰【" + headwear.getHeadwearName() + "】,快点装饰吧！");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult giveList(String headwearId, String uids, Integer date) {
        // 分隔拉贝号，多个拉贝号用换行分隔
        String[] arr = uids.split("\n");
        for (String uid : arr) {
            // Users users = usersService.getUsersByUid(Long.valueOf(uid));
            Users users = usersService.getUsresByErbanNo(Long.valueOf(uid));
            if (users == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }

            Headwear headwear = headwearService.getOneByJedisId(headwearId);
            if (headwear == null) {
                return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
            }

            // String purse = headwearPurseService.getPurse(Long.valueOf(uid));
            String purse = headwearPurseService.getPurse(users.getUid());
            if (StringUtils.isNotBlank(purse)) {
                if (!com.erban.main.util.StringUtils.splitToList(purse, ",").contains(headwearId)) {
                    purse += "," + headwearId;
                }
            } else {
                purse = "" + headwearId;
            }

            // jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), uid, purse);
            jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), String.valueOf(users.getUid()), purse);
            // HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(uid, headwearId);
            HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(String.valueOf(users.getUid()), headwearId);
            if (headwearPurseRecord == null) {
                headwearPurseRecord = new HeadwearPurseRecord();
                // headwearPurseRecord.setUid(Long.valueOf(uid));
                headwearPurseRecord.setUid(users.getUid());
                headwearPurseRecord.setHeadwearId(Long.valueOf(headwearId));
                headwearPurseRecord.setTotalGoldNum(0L);
                headwearPurseRecord.setHeadwearDate(date);
                headwearPurseRecord.setIsUse(new Byte("0"));
                headwearPurseRecord.setCreateTime(new Date());
                headwearPurseRecordMapper.insertSelective(headwearPurseRecord);
            } else {
                headwearPurseRecord.setHeadwearDate(headwearPurseRecord.getHeadwearDate() + date);
                headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
            }

            jedisService.hset(RedisKey.headwear_purse.getKey(), users.getUid() + "_" + headwearId, gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
            HeadwearGetRecord headwearGetRecord = new HeadwearGetRecord();
            // headwearGetRecord.setUid(Long.valueOf(uid));
            headwearGetRecord.setUid(users.getUid());
            headwearGetRecord.setHeadwearId(Long.valueOf(headwearId));
            headwearGetRecord.setHeadwearDate(date);
            headwearGetRecord.setType(new Byte("2"));
            headwearGetRecord.setCreateTime(new Date());
            headwearGetRecordMapper.insert(headwearGetRecord);
            // 发送消息给用户
            NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
            neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
            neteaseSendMsgParam.setOpe(0);
            neteaseSendMsgParam.setType(0);
            // neteaseSendMsgParam.setTo(uid);
            neteaseSendMsgParam.setTo(String.valueOf(users.getUid()));
            neteaseSendMsgParam.setBody("恭喜您，获得官方赠送头饰【" + headwear.getHeadwearName() + "】,快点装饰吧！");
            sendSysMsgService.sendMsg(neteaseSendMsgParam);

            // try {
            //     Thread.sleep(500);   // 休眠500毫秒
            // } catch (Exception e) {
            //     return new BusiResult(BusiStatus.SERVERERROR, "赠送头饰出错");
            // }
        }

        return new BusiResult(BusiStatus.SUCCESS);
    }

    public Headwear getOne(Integer id) throws Exception {
        Headwear headwear = get(id);
        if (headwear != null) {//base64解密
            if (!StringUtils.isBlank(headwear.getVggUrl())) {
                headwear.setVggUrl(DESUtils.DESAndBase64Decrypt(headwear.getVggUrl().toString(), giftCarSecret));
            }
        }
        return headwear;
    }

    public int saveHeadwear(Headwear entity) {
        if (entity == null) {
            return 0;
        }
        //base64加密
        try {
            if (!StringUtils.isBlank(entity.getVggUrl())) {
                entity.setVggUrl(DESUtils.DESAndBase64Encrypt(entity.getVggUrl().toString(), giftCarSecret));
            }
        } catch (Exception e) {
            return 0;
        }
        //是否有时效限制
        if (entity.getEffectiveTime().longValue() > 0) {
            entity.setIsTimeLimit(true);
        } else {
            entity.setIsTimeLimit(false);
        }
        entity.setCreateTime(new Date());
        //
        if (entity.getHeadwearId() != null) {
            return headwearMapper.updateByPrimaryKey(entity);
        } else {
            // 如果是第一次保存.则判断是否礼物名称是否重复
            HeadwearExample example = new HeadwearExample();
            example.createCriteria().andHeadwearNameEqualTo(entity.getHeadwearName().trim());
            int result = countByExample(example);
            if (result > 0) {
                throw new BusinessException("该名称已重复");
            }
            return headwearMapper.insertSelective(entity);
        }
    }

    @Override
    public boolean beforeInsert(Headwear entity) {
        entity.setCreateTime(new Date());
        return super.beforeInsert(entity);
    }

    @Override
    protected String getRedisKey() {
        return RedisKey.headwear.getKey();
    }

    @Override
    public Object getId(Headwear entity) {
        return entity.getHeadwearId();
    }

    @Override
    protected BaseMapper<Headwear, HeadwearExample> getMapper() {
        return headwearMapper;
    }

    public List<HeadwearRecordDTO> getExportList(String startDate, String endDate, String erbanNo) {
        // 获取对应拉贝号的UID列表
        List<Long> uids = new ArrayList<>();
        if (!StringUtils.isBlank(erbanNo)) {
            String[] erbanNos = erbanNo.split(",");
            for (int i = 0; i < erbanNos.length; i++) {
                Users users = usersService.getUsersByErBanNo(Long.valueOf(erbanNos[i]));
                uids.add(users.getUid());
            }
        }

        if (!StringUtils.isBlank(endDate)) {
            endDate = endDate + " 23:59:59";
        }

        return giveHeadwearMapperMgr.selectRecord(uids, startDate, endDate);
    }
}
