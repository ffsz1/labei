package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.DrawProd;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DataUtils;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.BillGoldDrawDao;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.domain.BillGoldDrawDO;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.item.enumeration.BoxDrawType;
import com.juxiao.xchat.dao.item.enumeration.CarGetType;
import com.juxiao.xchat.dao.item.enumeration.HeadwearGetType;
import com.juxiao.xchat.dao.user.UserBoxDrawDao;
import com.juxiao.xchat.dao.user.UserDrawGiftRecordDao;
import com.juxiao.xchat.dao.user.UserDrawPrettyErbanNoDao;
import com.juxiao.xchat.dao.user.UserDrawRecordDao;
import com.juxiao.xchat.dao.user.domain.UserBoxDrawRecordDO;
import com.juxiao.xchat.dao.user.domain.UserDrawDO;
import com.juxiao.xchat.dao.user.domain.UserDrawRecordDO;
import com.juxiao.xchat.dao.user.domain.UserWordDrawOverviewDO;
import com.juxiao.xchat.dao.user.dto.UserDrawDTO;
import com.juxiao.xchat.dao.user.dto.UserDrawRecordDTO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawOverviewDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.user.enumeration.UserDrawRecordStatus;
import com.juxiao.xchat.dao.user.enumeration.UserDrawRecordType;
import com.juxiao.xchat.dao.user.enumeration.UserWordDrawActivityType;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.user.*;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.conf.DingTalkConf;
import com.juxiao.xchat.service.api.charge.ChargeService;
import com.juxiao.xchat.service.api.user.UserDrawService;
import com.juxiao.xchat.service.api.user.UserWordDrawService;
import com.juxiao.xchat.service.api.user.vo.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @class: UserDrawServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
@Service
public class UserDrawServiceImpl implements UserDrawService {

    private static final Logger logger = LoggerFactory.getLogger(UserDrawServiceImpl.class);

    @Autowired
    private BillGoldDrawDao goldDrawDao;
    @Autowired
    private BillRecordDao billRecordDao;
    @Autowired
    private UserDrawPrettyErbanNoDao prettyErbanNoDao;
    @Autowired
    private UserDrawRecordDao drawRecordDao;
    @Autowired
    private DingTalkConf dingTalkConf;
    @Autowired
    private DingtalkChatbotManager dingtalkChatbotManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UserDrawManager userDrawManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private UserWordDrawService userWordDrawService;
    @Autowired
    private UserWordDrawManager userWordDrawManager;
    @Autowired
    private HeadwearManager headwearManager;
    @Autowired
    private GiftCarManager giftCarManager;

    @Autowired
    private UserDrawGiftRecordDao giftRecordDao;
    @Autowired
    private UserGiftPurseManager userGiftPurseManager;
    @Autowired
    private GiftManager giftManager;

    @Autowired
    private UserBoxDrawDao userBoxDrawDao;

    @Autowired
    private ChargeService chargeService;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private Gson gson;

    @Override
    public UserDrawVO getUserDraw(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UserDrawDTO drawDto = userDrawManager.getUserDraw(uid);
        UserDrawVO drawVo = new UserDrawVO();
        if (drawDto == null) {
            return drawVo;
        }

        BeanUtils.copyProperties(drawDto, drawVo);
        return drawVo;
    }

    @Override
    public UserDrawResultVO draw(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UserDrawDTO drawDto = userDrawManager.getUserDraw(uid);
        if (drawDto.getLeftDrawNum() == null || drawDto.getLeftDrawNum() <= 0) {
            throw new WebServiceException(WebServiceCode.NO_MORE_CHANCE);
        }

        // 给抽奖用户添加一个锁
        String lockValue = redisManager.lock(RedisKey.user_draw_lock.getKey(uid.toString()), 5 * 1000);
        if (StringUtils.isBlank(lockValue)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            List<Byte> types = new ArrayList<>();
            types.add(UserDrawRecordType.CHARGE.getValue());
            types.add(UserDrawRecordType.SHARE.getValue());
            //选择充值/分享的抽奖记录
            UserDrawRecordDTO drawRecordDto = drawRecordDao.getUserCreateDrawRecord(uid, types);
            if (drawRecordDto == null) {
                throw new WebServiceException(WebServiceCode.NO_MORE_CHANCE);
            }
            UserDrawRecordDO drawRecordDo = null;

            int amount = drawRecordDto.getSrcObjAmount().intValue();
            UserDrawPrize prizeProd = UserDrawPrize.amountOf(amount);

            drawRecordDo = prizeProd.createDrawPrize(drawRecordDto);

            UserDrawDO drawDo = new UserDrawDO();
            drawDo.setUid(drawDto.getUid());
            drawDo.setLeftDrawNum(drawDto.getLeftDrawNum() - 1);
            drawDo.setTotalWinDrawNum(drawDto.getTotalWinDrawNum());
            drawDo.setUpdateTime(new Date());

            UserWordDrawResultVO wordDrawResultVO = null;
            if (UserDrawRecordStatus.HAS_PRIZE.statusEquals(drawRecordDo.getDrawStatus())) {// 判断有没有中奖，如果有中奖，判断是加金币还是靓号
                drawDo.setTotalWinDrawNum(drawDto.getTotalWinDrawNum() + 1);
                if (drawRecordDo.getDrawPrizeId() <= DrawProd.gold8888) {// 金币充值，增加金币账单

                    //字体抽奖,
                    Integer activityType = UserWordDrawActivityType.NIU_DAN.getType();
                    UserWordDrawOverviewDO overviewDO = new UserWordDrawOverviewDO();
                    UserWordDrawOverviewDTO overviewDTO = userWordDrawManager.getUserWordDrawOverview(drawDto.getUid(), activityType);

                    BeanUtils.copyProperties(overviewDTO, overviewDO);
                    overviewDO.setLeftDrawNum(overviewDTO.getLeftDrawNum() + 1);
                    overviewDO.setTotalDrawNum(overviewDTO.getTotalDrawNum() + 1);
                    overviewDO.setUpdateTime(new Date());
                    //添加总览
                    userWordDrawManager.saveUserWordDrawOverview(overviewDO);
                    wordDrawResultVO = userWordDrawService.drawWord(uid, activityType);
                    wordDrawResultVO.setDrawStatus(null);
                    wordDrawResultVO.setLeftDrawNum(null);
                    wordDrawResultVO.setTotalDrawNum(null);
                    //字体抽奖over

                    userPurseManager.updateAddGold(uid, drawRecordDo.getDrawPrizeId().longValue(), false, true, "大转盘抽奖", null, null);

                    BillGoldDrawDO goldDrawDo = new BillGoldDrawDO();
                    goldDrawDo.setRecordId(drawRecordDo.getRecordId().longValue());
                    goldDrawDo.setUid(drawRecordDto.getUid());
                    goldDrawDo.setGoldAmount(drawRecordDo.getDrawPrizeId());
                    goldDrawDo.setCreateTime(new Date());
                    goldDrawDao.save(goldDrawDo);


                    // FIXME: 兼容管理后台
                    this.insertBillRecord(uid, String.valueOf(drawRecordDo.getRecordId()), BillRecordType.draw, drawRecordDo.getDrawPrizeId());
                } else if (drawRecordDo.getDrawPrizeId() <= DrawProd.prettySeven) {// 修改抽奖靓号的状态
                    prettyErbanNoDao.updateUsedPrettyErbanNo(DataUtils.getNumer(drawRecordDo.getDrawPrizeName()));
                    drawRecordDo.setDrawPrizeName("七位靓号" + drawRecordDo.getDrawPrizeName());
                }
            }

            userDrawManager.updateUserDraw(drawDo);
            drawRecordDao.update(drawRecordDo);
            UserDrawResultVO resultVo = new UserDrawResultVO();
            resultVo.setTotalWinDrawNum(drawDo.getTotalWinDrawNum());
            resultVo.setTotalDrawNum(drawDto.getTotalDrawNum());
            resultVo.setLeftDrawNum(drawDo.getLeftDrawNum());

            resultVo.setDrawStatus(drawRecordDo.getDrawStatus());
            resultVo.setDrawPrizeName(drawRecordDo.getDrawPrizeName());
            resultVo.setSrcObjName(drawRecordDo.getSrcObjName());
            resultVo.setDrawPrizeId(drawRecordDo.getDrawPrizeId());
            resultVo.setWordDraw(wordDrawResultVO);
            return resultVo;
        } finally {
            redisManager.unlock(RedisKey.user_draw_lock.getKey(uid.toString()), lockValue);
        }
    }

    @Override
    public List<UserDrawWinRecordVO> listUserDrawWinRecord() {
        String recordStr = redisManager.get(RedisKey.draw_win_records.getKey());
        List<UserDrawRecordDTO> list = null;
        if (StringUtils.isNotBlank(recordStr)) {
            list = gson.fromJson(recordStr, new TypeToken<List<UserDrawRecordDTO>>() {
            }.getType());
        }

        if (list == null) {
            list = drawRecordDao.listUserDrawWinRecord();
            redisManager.set(RedisKey.draw_win_records.getKey(), gson.toJson(list));
            redisManager.expire(RedisKey.draw_win_records.getKey(), 1, TimeUnit.HOURS);
        }

        List<UserDrawWinRecordVO> winRecords = Lists.newArrayList();
        UserDrawWinRecordVO recordVo;
        for (UserDrawRecordDTO recordDto : list) {
            recordVo = new UserDrawWinRecordVO();
            recordVo.setUid(recordDto.getUid());
            recordVo.setDrawStatus(recordDto.getDrawStatus());
            recordVo.setType(recordDto.getType());
            recordVo.setSrcObjName(recordDto.getSrcObjName());
            recordVo.setDrawPrizeName(recordDto.getDrawPrizeName());
            winRecords.add(recordVo);
        }

        return winRecords;
    }

    @Deprecated
    @Override
    public void insertBillRecord(Long uid, String objId, BillRecordType objType, Integer goldNum) {
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(UUIDUtils.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(null);
        billRecord.setObjId(objId);
        billRecord.setObjType(objType);
        billRecord.setDiamondNum(null);
        billRecord.setGoldNum(goldNum.longValue());
        billRecord.setMoney(0L);
        billRecord.setCreateTime(date);
        billRecordDao.save(billRecord);
    }

    @Override
    public BoxDrawVO doBoxDraw(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO user = usersManager.getUser(uid);
        if (user == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        // 查询当前抽奖次数
        String strBoxNum = redisManager.hget(RedisKey.day_first_charge.getKey(), uid.toString());
        if (StringUtils.isBlank(strBoxNum)) {
            throw new WebServiceException(WebServiceCode.BOX_NUM_NOT_ENOUGH);
        }

        Integer beforeBoxNum = Integer.parseInt(strBoxNum);
        if (beforeBoxNum <= 0) {
            throw new WebServiceException(WebServiceCode.BOX_NUM_NOT_ENOUGH);
        }
        Integer afterBoxNum = beforeBoxNum - 1;

        int boxBigPrizeNum = 0;// 当日已中大奖数量
        String strBoxBigPrize = redisManager.get(RedisKey.box_big_prize.getKey());
        if (StringUtils.isNotBlank(strBoxBigPrize)) {
            boxBigPrizeNum = Integer.parseInt(strBoxBigPrize);
        }

        // 给抽奖用户添加一个锁
        String lockValue = redisManager.lock(RedisKey.user_draw_lock.getKey(uid.toString()), 5 * 1000);
        if (StringUtils.isBlank(lockValue)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        try {

            //概率
            double[] probability;
            if (boxBigPrizeNum > 10) {// 当天已中大奖数量
                probability = new double[]{0.32, 0.65, 1};
            } else {
                probability = new double[]{0.31, 0.64, 0.99, 1};
            }

            // 每个线程有自己的Random对象
            double randomNumber = RandomUtils.threadLocalRandomDouble();
            int length = probability.length;
            int index = 0;
            for (int i = 0; i < length; i++) {
                if (randomNumber <= probability[i]) {
                    index = i;
                    break;
                }
            }
            UserBoxDrawRecordDO boxRecord = new UserBoxDrawRecordDO();
            int headwearId = 72;
            int carId = 67;
            int giftId = 460;
            Integer prizeDate = 1;
            String prizeName = null;
            HeadwearDTO headwearDTO = null;
            GiftCarDTO giftCarDTO = null;
            GiftDTO giftDTO = null;
            String prizePic = null;
            switch (index) {
                case 0:// 幸运草头饰     id : 72
                    headwearId = 72;
                    headwearDTO = boxDrawHeadwear(uid, headwearId, prizeDate);
                    prizeName = headwearDTO != null ? headwearDTO.getHeadwearName() : null;
                    prizePic = headwearDTO != null ? headwearDTO.getPicUrl() : null;
                    boxRecord.setPrizeName(prizeName);
                    boxRecord.setPrizeId(headwearId);
                    boxRecord.setType(BoxDrawType.headwear.getValue());
                    boxRecord.setDesc(prizeName + prizeDate + "天");
                    break;
                case 1:// 许愿星头饰     id : 73
                    headwearId = 73;
                    headwearDTO = boxDrawHeadwear(uid, headwearId, prizeDate);
                    prizeName = headwearDTO != null ? headwearDTO.getHeadwearName() : null;
                    prizePic = headwearDTO != null ? headwearDTO.getPicUrl() : null;
                    boxRecord.setPrizeName(prizeName);
                    boxRecord.setPrizeId(headwearId);
                    boxRecord.setType(BoxDrawType.headwear.getValue());
                    boxRecord.setDesc(prizeName + prizeDate + "天");
                    break;
                case 2:// 炫酷超跑座驾    id : 67
                    giftCarDTO = boxDrawCar(uid, carId, prizeDate);
                    prizeName = giftCarDTO != null ? giftCarDTO.getCarName() : null;
                    prizePic = giftCarDTO != null ? giftCarDTO.getPicUrl() : null;
                    boxRecord.setPrizeName(prizeName);
                    boxRecord.setPrizeId(carId);
                    boxRecord.setType(BoxDrawType.car.getValue());
                    boxRecord.setDesc(prizeName + prizeDate + "天");
                    break;
                case 3: // 礼盒抽奖只有4个礼物 最后一个为特殊礼物 水果礼物 id：460
                    redisManager.incr(RedisKey.box_big_prize.getKey());
                    try {
                        userGiftPurseManager.updateUserGiftPurse(uid, giftId, 1);
                    } catch (WebServiceException e) {
                        logger.error("[ 礼盒抽奖 ]uid:>{}, giftId:>{}, 放入礼物背包异常：", uid, giftId, e);
                    }
                    //todo 应该不用插到这个表
//                UserDrawGiftRecordDO recordDO = new UserDrawGiftRecordDO(uid, 0L, giftId, 1, 1, 7, new Date());
//                giftRecordDao.save(recordDO);
                    giftDTO = giftManager.getGiftById(giftId);
                    prizeName = giftDTO != null ? giftDTO.getGiftName() : null;
                    prizePic = giftDTO != null ? giftDTO.getPicUrl() : null;
                    boxRecord.setPrizeName(prizeName);
                    boxRecord.setPrizeId(giftId);
                    boxRecord.setType(BoxDrawType.gift.getValue());
                    boxRecord.setDesc(prizeName + prizeDate + "个");
                    break;
                default:// 幸运草
                    headwearId = 72;
                    headwearDTO = boxDrawHeadwear(uid, headwearId, prizeDate);
                    prizeName = headwearDTO != null ? headwearDTO.getHeadwearName() : null;
                    prizePic = headwearDTO != null ? headwearDTO.getPicUrl() : null;
                    boxRecord.setPrizeName(prizeName);
                    boxRecord.setPrizeId(headwearId);
                    boxRecord.setType(BoxDrawType.headwear.getValue());
                    boxRecord.setDesc("幸运草" + prizeDate + "天");
                    break;
            }

            boxRecord.setNum(1);
            boxRecord.setUid(uid);
            boxRecord.setCreateTime(new Date());
            userBoxDrawDao.save(boxRecord);
            logger.info("[ 每日首充礼盒 ] uid:{} prizeId:{} prizeName:{} prizeType:{}",
                    uid, boxRecord.getPrizeId(), boxRecord.getPrizeName(), boxRecord.getType());

            redisManager.hset(RedisKey.day_first_charge.getKey(), uid.toString(), afterBoxNum.toString());// 每日一次

            return new BoxDrawVO(user.getNick(), boxRecord.getType(), boxRecord.getPrizeName(), prizePic,
                    prizeDate, boxRecord.getNum(), afterBoxNum, boxRecord.getDesc(), boxRecord.getCreateTime());
        } finally {
            redisManager.unlock(RedisKey.user_draw_lock.getKey(uid.toString()), lockValue);
        }
    }

    @Override
    public List<UserBoxDrawRecordDO> listBoxDrawRecord(Long uid) {
        return userBoxDrawDao.listUserBoxDrawRecord(uid);
    }

    @Override
    public UserBoxVO getUserBox(Long uid) throws WebServiceException {
        UserBoxVO userBoxVO = new UserBoxVO();
        userBoxVO.setUserCharge(chargeService.getUserCharge(uid));
        String str = redisManager.hget(RedisKey.day_first_charge.getKey(), uid.toString());
        userBoxVO.setBoxNum(StringUtils.isBlank(str) ? 0 : Integer.valueOf(str));
        return userBoxVO;
    }

    /**
     * @param headwearId
     * @param date
     */
    private HeadwearDTO boxDrawHeadwear(Long uid, int headwearId, int date) {
        HeadwearDTO headwearDto = headwearManager.getHeadwear(headwearId);
        if (headwearDto != null) {
            List<String> headwearIds = headwearManager.listUserHeadwearid(uid);
            if (headwearIds.contains(headwearId + "")) {
                headwearIds.add(headwearId + "");
            }
            redisManager.hset(RedisKey.headwear_purse_list.getKey(), uid.toString(), StringUtils.join(headwearIds, ","));
            headwearManager.saveUserHeadwear(uid, headwearId, date, HeadwearGetType.box_draw.getValue(), null);
            return headwearDto;
        } else {
            return null;
        }
    }

    /**
     * @param carId
     * @param date
     */
    private GiftCarDTO boxDrawCar(Long uid, int carId, int date) {
        GiftCarDTO carDTO = giftCarManager.getGiftCar(carId);
        if (carDTO != null) {
            List<String> carids = giftCarManager.listUserCarids(uid);
            if (!carids.contains(carId + "")) {
                carids.add(carId + "");
            }
            redisManager.hset(RedisKey.gift_car_purse_list.getKey(), uid.toString(), StringUtils.join(carids, ","));
            // 保存用户座驾信息
            giftCarManager.saveUserCar(uid, carDTO.getCarId(), date, CarGetType.box_draw.getValue(), null);
            return carDTO;
        } else {
            return null;
        }
    }

}
