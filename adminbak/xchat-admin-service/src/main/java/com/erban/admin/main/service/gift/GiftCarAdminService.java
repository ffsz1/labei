package com.erban.admin.main.service.gift;

import com.erban.admin.main.base.RedisSupportService;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.GiftCarRecordDTO;
import com.erban.admin.main.dto.HeadwearRecordDTO;
import com.erban.admin.main.mapper.GiveGiftcarMapperMgr;
import com.erban.admin.main.vo.GiveGiftcarVo;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import com.erban.main.base.BaseMapper;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.GiftCarGetRecordMapper;
import com.erban.main.mybatismapper.GiftCarMapper;
import com.erban.main.mybatismapper.GiftCarPurseRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.gift.GiftCarPurseService;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GiftCarAdminService extends RedisSupportService<GiftCar, GiftCarExample> {
    @Value("${giftCarSecret}")
    private String giftCarSecret;

    @Autowired
    private GiftCarMapper giftCarMapper;

    @Autowired
    private GiveGiftcarMapperMgr giveGiftcarMapperMgr;

    @Autowired
    private UsersService usersService;

    @Autowired
    private GiftCarService giftCarService;

    @Autowired
    private GiftCarPurseService giftCarPurseService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private GiftCarPurseRecordMapper giftCarPurseRecordMapper;

    @Autowired
    private GiftCarGetRecordMapper giftCarGetRecordMapper;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    private Gson gson = new Gson();

    protected final Logger logger = LoggerFactory.getLogger(GiftCarAdminService.class);

    /**
     * 获取所有有效的礼物列表
     *
     * @return
     */
    public List<GiftCar> getAllGift() {
        GiftCarExample example = new GiftCarExample();
        example.createCriteria().andCarStatusEqualTo((byte) 1);
        return giftCarMapper.selectByExample(example);
    }

    /**
     * 获取所有礼物列表
     *
     * @return
     */
    public List<GiftCar> getAllGiftCar() {
        GiftCarExample example = new GiftCarExample();
        return giftCarMapper.selectByExample(example);
    }

    /**
     * 获取官方赠送座驾记录
     *
     * @return
     */
    public PageInfo<GiftCarGetRecord> getGiftCarRecord(int pageNum, int pageSize, String erbanNo, String startDate, String endDate) {
        // 设置查询条件
        GiftCarGetRecordExample example = new GiftCarGetRecordExample();
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
        // 查询官方赠送座驾记录
        List<GiftCarGetRecord> resultList = giftCarGetRecordMapper.selectRecord(uids, startDate, endDate);
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
    public PageInfo<GiftCar> getGiftByPage(byte status, String giftCarName, int page, int size) {
        PageHelper.startPage(page, size);
        GiftCarExample example = new GiftCarExample();
        // 判断是否需要增加状态条件
        if (status != 0) {
            example.createCriteria().andCarStatusEqualTo(status);
        }
        if (StringUtils.isNotBlank(giftCarName)) {
            example.setLikeName(giftCarName);
        }
        example.setOrderByClause(" seq_no asc");
        return new PageInfo(giftCarMapper.selectByExample(example));
    }

    public PageInfo<GiveGiftcarVo> getList(Long erbanNo, int page, int size) {
        PageHelper.startPage(page, size);
        RoomRobotGroupParam roomRobotGroupParam = new RoomRobotGroupParam();
        roomRobotGroupParam.setErbanNo(erbanNo);
        List<GiveGiftcarVo> giveGiftcarVoList = giveGiftcarMapperMgr.selectByParam(roomRobotGroupParam);
        return new PageInfo(giveGiftcarVoList);
    }

    public BusiResult give(String carUid, Integer date) {
        String[] list = carUid.split(",");
        if (list.length < 2) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Users users = usersService.getUsersByUid(Long.valueOf(list[0]));
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        GiftCar giftCar = giftCarService.getOneByJedisId(list[1]);
        if (giftCar == null) {
            return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
        }
        String purse = giftCarPurseService.getPurse(Long.valueOf(list[0]));
        if (StringUtils.isNotBlank(purse)) {
            if (!com.erban.main.util.StringUtils.splitToList(purse, ",").contains(list[1])) {
                purse += "," + list[1];
            }
        } else {
            purse = "" + list[1];
        }
        jedisService.hwrite(RedisKey.gift_car_purse_list.getKey(), list[0], purse);
        GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(list[0], list[1]);
        if (giftCarPurseRecord == null) {
            giftCarPurseRecord = new GiftCarPurseRecord();
            giftCarPurseRecord.setUid(Long.valueOf(list[0]));
            giftCarPurseRecord.setCarId(Long.valueOf(list[1]));
            giftCarPurseRecord.setTotalGoldNum(0L);
            giftCarPurseRecord.setCarDate(date);
            giftCarPurseRecord.setIsUse(new Byte("0"));
            giftCarPurseRecord.setCreateTime(new Date());
            giftCarPurseRecordMapper.insertSelective(giftCarPurseRecord);
        } else {
            giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() + date);
            giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
        }
        jedisService.hset(RedisKey.gift_car_purse.getKey(), list[0] + "_" + list[1],
                gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
        GiftCarGetRecord giftCarGetRecord = new GiftCarGetRecord();
        giftCarGetRecord.setUid(Long.valueOf(list[0]));
        giftCarGetRecord.setCarId(Long.valueOf(list[1]));
        giftCarGetRecord.setCarDate(date);
        giftCarGetRecord.setType(new Byte("2"));
        giftCarGetRecord.setCreateTime(new Date());
        giftCarGetRecordMapper.insert(giftCarGetRecord);
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(list[0]);
        neteaseSendMsgParam.setBody("恭喜您，获得官方赠送座驾【" + giftCar.getCarName() + "】,快点坐上去游玩吧！");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult giveList(String carId, String uids, Integer date) {
        // 分隔拉贝号，多个拉贝号用换行分隔
        String[] arr = uids.split("\n");
        for (String uid : arr) {
            // Users users = usersService.getUsersByUid(Long.valueOf(uid));
            Users users = usersService.getUsresByErbanNo(Long.valueOf(uid));
            if (users == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }

            GiftCar giftCar = giftCarService.getOneByJedisId(carId);
            if (giftCar == null) {
                return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
            }

            // String purse = headwearPurseService.getPurse(Long.valueOf(uid));
            String purse = giftCarPurseService.getPurse(users.getUid());
            if (StringUtils.isNotBlank(purse)) {
                if (!com.erban.main.util.StringUtils.splitToList(purse, ",").contains(carId)) {
                    purse += "," + carId;
                }
            } else {
                purse = "" + carId;
            }

            // jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), uid, purse);
            jedisService.hwrite(RedisKey.gift_car_purse_list.getKey(), String.valueOf(users.getUid()), purse);
            // HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(uid, headwearId);
            GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(String.valueOf(users.getUid()), carId);
            if (giftCarPurseRecord == null) {
                giftCarPurseRecord = new GiftCarPurseRecord();
                // headwearPurseRecord.setUid(Long.valueOf(uid));
                giftCarPurseRecord.setUid(users.getUid());
                giftCarPurseRecord.setCarId(Long.valueOf(carId));
                giftCarPurseRecord.setTotalGoldNum(0L);
                giftCarPurseRecord.setCarDate(date);
                giftCarPurseRecord.setIsUse(new Byte("0"));
                giftCarPurseRecord.setCreateTime(new Date());
                giftCarPurseRecordMapper.insertSelective(giftCarPurseRecord);
            } else {
                giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() + date);
                giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
            }

            jedisService.hset(RedisKey.gift_car_purse.getKey(), users.getUid() + "_" + carId, gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
            GiftCarGetRecord giftCarGetRecord = new GiftCarGetRecord();
            // headwearGetRecord.setUid(Long.valueOf(uid));
            giftCarGetRecord.setUid(users.getUid());
            giftCarGetRecord.setCarId(Long.valueOf(carId));
            giftCarGetRecord.setCarDate(date);
            giftCarGetRecord.setType(new Byte("2"));
            giftCarGetRecord.setCreateTime(new Date());
            giftCarGetRecordMapper.insert(giftCarGetRecord);
            // 发送消息给用户
            NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
            neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
            neteaseSendMsgParam.setOpe(0);
            neteaseSendMsgParam.setType(0);
            // neteaseSendMsgParam.setTo(uid);
            neteaseSendMsgParam.setTo(String.valueOf(users.getUid()));
            neteaseSendMsgParam.setBody("恭喜您，获得官方赠送座驾【" + giftCar.getCarName() + "】,快点装饰吧！");
            sendSysMsgService.sendMsg(neteaseSendMsgParam);

            // try {
            //     Thread.sleep(500);   // 休眠500毫秒
            // } catch (Exception e) {
            //     return new BusiResult(BusiStatus.SERVERERROR, "赠送座驾出错");
            // }
        }

        return new BusiResult(BusiStatus.SUCCESS);
    }

    public GiftCar getOne(Integer id) throws Exception {
        GiftCar giftCar = get(id);
        if (giftCar != null) {//base64解密
            if (!StringUtils.isBlank(giftCar.getVggUrl())) {
                giftCar.setVggUrl(DESUtils.DESAndBase64Decrypt(giftCar.getVggUrl().toString(), giftCarSecret));
            }
        }
        return giftCar;
    }

    public int saveCar(GiftCar entity) {
        if (entity == null) {
            return 0;
        }
        //base64加密
        try {
            if (!StringUtils.isBlank(entity.getVggUrl())) {
                entity.setVggUrl(DESUtils.DESAndBase64Encrypt(entity.getVggUrl().toString(), giftCarSecret));
            }
        } catch (Exception e) {
            logger.error("uirl加密失败:{}", e);
            return 0;
        }
        //是否有时效限制
        if (entity.getEffectiveTime().longValue() > 0) {
            entity.setIsTimeLimit(true);
        } else {
            entity.setIsTimeLimit(false);
        }
        entity.setCreateTime(new Date());
        if (entity.getCarId() != null) {
            //
            return giftCarMapper.updateByPrimaryKey(entity);
        } else {
            // 如果是第一次保存.则判断是否礼物名称是否重复
            GiftCarExample example = new GiftCarExample();
            example.createCriteria().andCarNameEqualTo(entity.getCarName().trim());
            int result = countByExample(example);
            if (result > 0) {
                throw new BusinessException("该礼物名称已重复");
            }
            return giftCarMapper.insertSelective(entity);
        }
    }

    @Override
    public boolean beforeInsert(GiftCar entity) {
        entity.setCreateTime(new Date());
        return super.beforeInsert(entity);
    }

    @Override
    protected String getRedisKey() {
        return RedisKey.gift_car.getKey();
    }

    @Override
    public Object getId(GiftCar entity) {
        return entity.getCarId();
    }

    @Override
    protected BaseMapper<GiftCar, GiftCarExample> getMapper() {
        return giftCarMapper;
    }

    public List<GiftCarRecordDTO> getExportList(String startDate, String endDate, String erbanNo) {
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

        return giveGiftcarMapperMgr.selectRecord(uids, startDate, endDate);
    }
}
