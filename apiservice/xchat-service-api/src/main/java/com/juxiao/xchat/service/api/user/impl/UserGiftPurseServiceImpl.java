package com.juxiao.xchat.service.api.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.spring.SpringAppContext;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.utils.UUIDUtil;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.BillGiftDrawDao;
import com.juxiao.xchat.dao.bill.domain.BillGiftDrawDO;
import com.juxiao.xchat.dao.bill.dto.BillGiftDrawDTO;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.UserDrawGiftRecordDao;
import com.juxiao.xchat.dao.user.UserPurseDao;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftRankDO;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftRecordDO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.draw.GiftDrawManager;
import com.juxiao.xchat.manager.common.draw.conf.DrawTypeConf;
import com.juxiao.xchat.manager.common.draw.conf.GiftDrawConf;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.item.constant.GiftType;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UserGiftPurseManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.item.mq.GiftDrawMessage;
import com.juxiao.xchat.service.api.item.vo.GiftVO;
import com.juxiao.xchat.service.api.user.UserGiftPurseService;
import com.juxiao.xchat.service.api.user.draw.impl.GiftDrawKey;
import com.juxiao.xchat.service.api.user.vo.DrawConchVO;
import com.juxiao.xchat.service.api.user.vo.DrawGiftVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @class: UserGiftPurseServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Slf4j
@Service
public class UserGiftPurseServiceImpl implements UserGiftPurseService {
    @Autowired
    private BillGiftDrawDao giftDrawDao;
    @Autowired
    private UserDrawGiftRecordDao giftRecordDao;
    @Autowired
    private ImRoomManager imroomManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private SysConfManager sysconfManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private UserPurseDao userPurseDao;
    @Autowired
    private UserGiftPurseManager userGiftPurseManager;
    @Autowired
    private GiftManager giftManager;
    @Autowired
    private Gson gson;
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    protected GiftDrawConf giftDrawConf;
    @Autowired
    private SystemConf systemConf;

    @Autowired
    private LevelManager levelManager;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    private List<GiftDrawManager> drawManagers = Lists.newArrayList();

    @Override
    public DrawConchVO hdDraw(Long uid, Integer type, Long roomId) throws WebServiceException {
        if (uid == null || type == null || roomId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        SysConfDTO lotteryBoxOption = sysconfManager.getSysConf(SysConfigId.lottery_box_option);
        if (lotteryBoxOption == null || "0".equals(lotteryBoxOption.getConfigValue())) {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        RoomDTO roomDto = roomManager.getRoom(roomId);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        RoomConfDTO roomConfDto = null;
        String json = redisManager.hget(RedisKey.room_conf.getKey(String.valueOf(roomDto.getType())), String.valueOf(roomDto.getUid()));
        if (StringUtils.isNotBlank(json)) {
            roomConfDto = gson.fromJson(json, RoomConfDTO.class);
        }

        int drawNum = this.getDrawNum(type);
        int totalDrawNum = drawNum;
        String lockVal = redisManager.lock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), 10000);
        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.OPE_FREQUENT);
        }

        Map<Integer, Integer> result = new HashMap<>(drawNum);

        int Min = drawNum;
        int Max = drawNum * 3;
        int ticketNum = Min + (int) (Math.random() * ((Max - Min) + 1));// ticketNum范围 Min~Max

        Integer ticketGiftId = giftDrawConf.getTicketGiftId();
        log.info("[ 捡海螺人气票 ] uid:>{}, 抽取次数:>{}, 获得票数:>{}, ", uid, drawNum, ticketNum);
        result.put(ticketGiftId, ticketNum);

        int[] giftIdList = giftDrawConf.getHdDrawGifts();
        int[] giftPriceList = giftDrawConf.getGiftPrices();

        Map<Integer, Integer> giftMap = new HashMap<>(giftIdList.length);
        for (int j = 0; j < giftIdList.length; j++) {
            giftMap.put(giftIdList[j], giftPriceList[j]);
        }

        int[] tmpGiftIdList = giftDrawConf.getTmpDrawGifts();
        int[] tmpGiftPriceList = giftDrawConf.getTmpDrawGiftsPrice();
        Map<Integer, Integer> tmpGiftMap = new HashMap<>(giftIdList.length);
        for (int j = 0; j < tmpGiftIdList.length; j++) {
            tmpGiftMap.put(tmpGiftIdList[j], tmpGiftPriceList[j]);
        }

        double totalInput = 0, totalOutput = 0, tempTotalInput = 0, tempTotalOutput = 0;
        Double personInput = 0.0, tempPersonInput = 0.0;
        Double personOutput = 0.0, tempPersonOutput = 0.0;

        String totalInputStr = redisManager.hget(RedisKey.hdgift_draw_input.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalInputStr)) totalInput = Double.parseDouble(totalInputStr);

        String totalOutputStr = redisManager.hget(RedisKey.hdgift_draw_output.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalOutputStr)) totalOutput = Double.parseDouble(totalOutputStr);

        Double personInputDouble = redisManager.zscore(RedisKey.hduser_gift_draw_input.getKey(), uid.toString());
        if (personInputDouble != null) personInput = personInputDouble;

        Double personOutPutDouble = redisManager.zscore(RedisKey.hduser_gift_draw_output.getKey(), uid.toString());
        if (personOutPutDouble != null) personOutput = personOutPutDouble;

        String isBai = redisManager.hget(RedisKey.is_bai.getKey(), uid.toString());

        log.info("[ hd概率全局 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, totalInput, totalOutput, totalOutput / totalInput);
        log.info("[ hd概率个人 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, personInput, personOutput, personOutput / personInput);

        Map<String, Long> num = new HashMap<>();
        try {
            Integer giftId;
            userPurseManager.updateReduceTryCoin(uid, drawNum, false, "体验币", num);
            // 捡海螺礼物ID必须要与人气票礼物ID不一致
            for (GiftDrawManager drawManager : this.getDrawManagers()) {
                if (!drawManager.check(uid, roomConfDto, totalDrawNum, true, false)) {
                    continue;
                }

                for (; drawNum > 0; drawNum--) {
                    giftId = drawManager.draw(uid, roomDto.getUid(), totalDrawNum, true, false);
                    if (giftId == -1) {
                        break;
                    }

                    Integer preGiftPrice;
                    preGiftPrice = giftMap.get(giftId);
                    if (preGiftPrice == null) {
                        preGiftPrice = tmpGiftMap.get(giftId);
                    }

                    totalInput += 20;
                    personInput += 20;
                    tempTotalInput += 20;
                    tempPersonInput += 20;

                    double preWant = 1.161;

                    log.info("[ 概率对象 ] drawname >:{}, isbai >: {}", drawManager.getClass().getName(), isBai);
                    if (!drawManager.getClass().getName().contains("MustWinningDrawManagerImpl")) {
                        if (!StringUtils.isBlank(isBai) && totalInput > 3000 && personInput > 100 && ((personOutput + preGiftPrice) / personInput) < (preWant - 0.000011)) {// 下限
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.6) {
                                start = 5;
                            } else {
                                start = 6;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {//
//                                    if (giftPriceList[jj] <= preGiftPrice) break;
                                if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                    continue;
                                log.info("[ 命中白名单下限 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        } else if (totalInput > 1500 && personInput > 1000 && (personOutput + preGiftPrice) / personInput > (preWant - 0.04)) {// 上限
                            if (preGiftPrice >= 66) {
                                double randomNumber = RandomUtils.threadLocalRandomDouble();
                                if ((randomNumber > 0.97 && (personOutput + preGiftPrice) / personInput > (preWant - 0.04)) ||
                                        (randomNumber > 0.95 && (personOutput + preGiftPrice) / personInput > (preWant - 0.03)) ||
                                        (randomNumber > 0.90 && (personOutput + preGiftPrice) / personInput > (preWant - 0.02)) ||
                                        (randomNumber > 0.85 && (personOutput + preGiftPrice) / personInput > (preWant - 0.01)) ||
                                        (randomNumber > 0.80 && (personOutput + preGiftPrice) / personInput > (preWant - 0.00)) ||
                                        (randomNumber > 0.75 && (personOutput + preGiftPrice) / personInput > (preWant + 0.01)) ||
                                        (randomNumber > 0.70 && (personOutput + preGiftPrice) / personInput > (preWant + 0.02)) ||
                                        (randomNumber > 0.65 && (personOutput + preGiftPrice) / personInput > (preWant + 0.03)) ||
                                        (randomNumber > 0.60 && (personOutput + preGiftPrice) / personInput > (preWant + 0.04)) ||
                                        (randomNumber > 0.50 && (personOutput + preGiftPrice) / personInput > (preWant + 0.05)) ||
                                        (randomNumber > 0.40 && (personOutput + preGiftPrice) / personInput > (preWant + 0.07)) ||
                                        (randomNumber > 0.30 && (personOutput + preGiftPrice) / personInput > (preWant + 0.12)) ||
                                        (randomNumber > 0.20 && (personOutput + preGiftPrice) / personInput > (preWant + 0.16)) ||
                                        (randomNumber > 0.10 && (personOutput + preGiftPrice) / personInput > (preWant + 0.23)) ||
                                        (randomNumber > 0.00 && (personOutput + preGiftPrice) / personInput > (preWant + 0.30))
                                ) {
                                    // 降一位
                                    for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                        if (giftPriceList[jj] >= preGiftPrice) continue;
                                        log.info("[ 命中上限2 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                        giftId = giftIdList[jj];
                                        break;
                                    }
                                } else {
                                    // 消掉5200以上
                                    for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                        if (giftPriceList[jj] > preGiftPrice) continue;
                                        if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                            continue;
                                        log.info("[ 命中上限3 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                        giftId = giftIdList[jj];
                                        break;
                                    }
                                }
                            }
                        } else if (totalInput > 5000 && personInput < 10000 && ((personOutput + preGiftPrice) / personInput) < (preWant - 0.04)) {// < 10000
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.5) {
                                start = 5;
                            } else if (randomNumber < 0.6) {
                                start = 6;
                            } else if (randomNumber < 0.7) {
                                start = 7;
                            } else if (randomNumber < 0.8) {
                                start = 8;
                            } else {
                                start = 8;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {// -2 是为了在5200之内做补偿 从上往下补
                                if (giftPriceList[jj] <= preGiftPrice) break;
                                if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2074) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                    continue;
                                log.info("[ 命中下限4 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        } else if (totalInput > 5000 && personInput > 10 &&
                                (((personOutput + preGiftPrice) / personInput) < (preWant - 0.07) ||
                                        (totalOutput + preGiftPrice) / totalInput < (preWant - 0.02))) {// 下限
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.5) {
                                start = 5;
                            } else if (randomNumber < 0.6) {
                                start = 6;
                            } else if (randomNumber < 0.7) {
                                start = 7;
                            } else if (randomNumber < 0.8) {
                                start = 8;
                            } else {
                                start = 8;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {// -2 是为了在5200之内做补偿 从上往下补
                                if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2474) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                    continue;
                                log.info("[ 命中下限6 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        }


                    }

                    Integer realGiftPrice = giftMap.get(giftId);
                    if (realGiftPrice == null) {
                        realGiftPrice = tmpGiftMap.get(giftId);
                    }

                    totalOutput += realGiftPrice;
                    personOutput += realGiftPrice;
                    tempTotalOutput += realGiftPrice;
                    tempPersonOutput += realGiftPrice;

                    if (result.get(giftId) == null) {
                        result.put(giftId, 1);
                    } else {
                        result.put(giftId, result.get(giftId) + 1);
                    }
                }

                // 抽完 num 次数即退出
                if (drawNum < 1) {
                    break;
                }
            }

//            redisManager.hincrBy(RedisKey.user_gift_first.getKey(), uid.toString(), 1L);
        } finally {
            redisManager.unlock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), lockVal);
        }

        if (result.size() == 0) {
            return new DrawConchVO();
        }

        result.forEach((giftId, giftNum) -> {
            try {
                userGiftPurseManager.addGiftPurseCache(uid, giftId, giftNum);
            } catch (WebServiceException e) {
                log.info("[ draw 刷新礼物缓存失败  ] uid :>{}  giftId >:{}, giftNum >:{}", uid, giftId, giftNum);
            }
        });

        //总投入
        redisManager.hincrBy(RedisKey.hdgift_draw_input.getKey(), GiftDrawKey.total.toString(), (long) tempTotalInput);
        //个人总投入
        redisManager.zincrby(RedisKey.hduser_gift_draw_input.getKey(), uid.toString(), tempPersonInput);
        //总产出
        redisManager.hincrBy(RedisKey.hdgift_draw_output.getKey(), GiftDrawKey.total.toString(), (long) tempTotalOutput);
        //个人总产出
        redisManager.zincrby(RedisKey.hduser_gift_draw_output.getKey(), uid.toString(), tempPersonOutput);

        List<DrawGiftVO> list = handleResult(result);
        String messageId = UUIDUtil.uuid();
        GiftDrawMessage message = new GiftDrawMessage(messageId, System.currentTimeMillis(), roomId, uid, type, result);
        String msg = gson.toJson(message);
        redisManager.hset(RedisKey.mq_gift_draw_message_status.getKey(), messageId, msg);
        activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_TRYDRAW_QUEUE, msg);

        return new DrawConchVO(list, ticketGiftId,
                num == null ? null : (num.get("reduceAfterConchNum") == null ? 0L : num.get("reduceAfterConchNum")),
                num == null ? null : (num.get("reduceAfterTryCoinNum") == null ? 0L : num.get("reduceAfterTryCoinNum")));
    }

    @Override
    public DrawConchVO xqDraw(Long uid, Integer type, Long roomId) throws WebServiceException {
        if (uid == null || type == null || roomId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        SysConfDTO lotteryBoxOption = sysconfManager.getSysConf(SysConfigId.lottery_box_option);
        if (lotteryBoxOption == null || "0".equals(lotteryBoxOption.getConfigValue())) {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        RoomDTO roomDto = roomManager.getRoom(roomId);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        RoomConfDTO roomConfDto = null;
        String json = redisManager.hget(RedisKey.room_conf.getKey(String.valueOf(roomDto.getType())), String.valueOf(roomDto.getUid()));
        if (StringUtils.isNotBlank(json)) {
            roomConfDto = gson.fromJson(json, RoomConfDTO.class);
        }

        int drawNum = this.getDrawNum(type);
        int totalDrawNum = drawNum;
        String lockVal = redisManager.lock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), 10000);
        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.OPE_FREQUENT);
        }

        Map<Integer, Integer> result = new HashMap<>(drawNum);

        int Min = drawNum;
        int Max = drawNum * 3;
        int ticketNum = Min + (int) (Math.random() * ((Max - Min) + 1));// ticketNum范围 Min~Max

        Integer ticketGiftId = giftDrawConf.getTicketGiftId();
        log.info("[ 捡海螺人气票 ] uid:>{}, 抽取次数:>{}, 获得票数:>{}, ", uid, drawNum, ticketNum);
        result.put(ticketGiftId, ticketNum);

        int[] giftIdList = giftDrawConf.getXqDrawGifts();
        int[] giftPriceList = giftDrawConf.getGiftPrices();

        Map<Integer, Integer> giftMap = new HashMap<>(giftIdList.length);
        for (int j = 0; j < giftIdList.length; j++) {
            giftMap.put(giftIdList[j], giftPriceList[j]);
        }

        int[] tmpGiftIdList = giftDrawConf.getTmpDrawGifts();
        int[] tmpGiftPriceList = giftDrawConf.getTmpDrawGiftsPrice();
        Map<Integer, Integer> tmpGiftMap = new HashMap<>(giftIdList.length);
        for (int j = 0; j < tmpGiftIdList.length; j++) {
            tmpGiftMap.put(tmpGiftIdList[j], tmpGiftPriceList[j]);
        }

        double totalInput = 0, totalOutput = 0, tempTotalInput = 0, tempTotalOutput = 0;
        Double personInput = 0.0, tempPersonInput = 0.0;
        Double personOutput = 0.0, tempPersonOutput = 0.0;

        String totalInputStr = redisManager.hget(RedisKey.xqgift_draw_input.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalInputStr)) totalInput = Double.parseDouble(totalInputStr);

        String totalOutputStr = redisManager.hget(RedisKey.xqgift_draw_output.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalOutputStr)) totalOutput = Double.parseDouble(totalOutputStr);

        Double personInputDouble = redisManager.zscore(RedisKey.xquser_gift_draw_input.getKey(), uid.toString());
        if (personInputDouble != null) personInput = personInputDouble;

        Double personOutPutDouble = redisManager.zscore(RedisKey.xquser_gift_draw_output.getKey(), uid.toString());
        if (personOutPutDouble != null) personOutput = personOutPutDouble;

        String isBai = redisManager.hget(RedisKey.is_bai.getKey(), uid.toString());

        log.info("[ xq概率全局 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, totalInput, totalOutput, totalOutput / totalInput);
        log.info("[ xq概率个人 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, personInput, personOutput, personOutput / personInput);

        Map<String, Long> num = new HashMap<>();
        try {
            Integer giftId;
            userPurseManager.updateReduceConch(uid, drawNum, false, "捡海螺", num);
            // 捡海螺礼物ID必须要与人气票礼物ID不一致
            for (GiftDrawManager drawManager : this.getDrawManagers()) {
                if (!drawManager.check(uid, roomConfDto, totalDrawNum, true, false)) {
                    continue;
                }

                for (; drawNum > 0; drawNum--) {
                    giftId = drawManager.draw(uid, roomDto.getUid(), totalDrawNum, true, false);
                    if (giftId == -1) {
                        break;
                    }

                    Integer preGiftPrice;
                    preGiftPrice = giftMap.get(giftId);
                    if (preGiftPrice == null) {
                        preGiftPrice = tmpGiftMap.get(giftId);
                    }

                    totalInput += 20;
                    personInput += 20;
                    tempTotalInput += 20;
                    tempPersonInput += 20;

                    double preWant = 1.161;

                    log.info("[ 概率对象 ] drawname >:{}, isbai >: {}", drawManager.getClass().getName(), isBai);
                    if (!drawManager.getClass().getName().contains("MustWinningDrawManagerImpl")) {
                        if (!StringUtils.isBlank(isBai) && totalInput > 3000 && personInput > 100 && ((personOutput + preGiftPrice) / personInput) < (preWant - 0.000011)) {// 下限
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.6) {
                                start = 5;
                            } else {
                                start = 6;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {//
//                                    if (giftPriceList[jj] <= preGiftPrice) break;
                                if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                    continue;
                                log.info("[ 命中白名单下限 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        } else if (totalInput > 1500 && personInput > 1000 && (personOutput + preGiftPrice) / personInput > (preWant - 0.04)) {// 上限
                            if (preGiftPrice >= 66) {
                                double randomNumber = RandomUtils.threadLocalRandomDouble();
                                if ((randomNumber > 0.97 && (personOutput + preGiftPrice) / personInput > (preWant - 0.04)) ||
                                        (randomNumber > 0.95 && (personOutput + preGiftPrice) / personInput > (preWant - 0.03)) ||
                                        (randomNumber > 0.90 && (personOutput + preGiftPrice) / personInput > (preWant - 0.02)) ||
                                        (randomNumber > 0.85 && (personOutput + preGiftPrice) / personInput > (preWant - 0.01)) ||
                                        (randomNumber > 0.80 && (personOutput + preGiftPrice) / personInput > (preWant - 0.00)) ||
                                        (randomNumber > 0.75 && (personOutput + preGiftPrice) / personInput > (preWant + 0.01)) ||
                                        (randomNumber > 0.70 && (personOutput + preGiftPrice) / personInput > (preWant + 0.02)) ||
                                        (randomNumber > 0.65 && (personOutput + preGiftPrice) / personInput > (preWant + 0.03)) ||
                                        (randomNumber > 0.60 && (personOutput + preGiftPrice) / personInput > (preWant + 0.04)) ||
                                        (randomNumber > 0.50 && (personOutput + preGiftPrice) / personInput > (preWant + 0.05)) ||
                                        (randomNumber > 0.40 && (personOutput + preGiftPrice) / personInput > (preWant + 0.07)) ||
                                        (randomNumber > 0.30 && (personOutput + preGiftPrice) / personInput > (preWant + 0.12)) ||
                                        (randomNumber > 0.20 && (personOutput + preGiftPrice) / personInput > (preWant + 0.16)) ||
                                        (randomNumber > 0.10 && (personOutput + preGiftPrice) / personInput > (preWant + 0.23)) ||
                                        (randomNumber > 0.00 && (personOutput + preGiftPrice) / personInput > (preWant + 0.30))
                                ) {
                                    // 降一位
                                    for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                        if (giftPriceList[jj] >= preGiftPrice) continue;
                                        log.info("[ 命中上限2 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                        giftId = giftIdList[jj];
                                        break;
                                    }
                                } else {
                                    // 消掉5200以上
                                    for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                        if (giftPriceList[jj] > preGiftPrice) continue;
                                        if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                            continue;
                                        log.info("[ 命中上限3 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                        giftId = giftIdList[jj];
                                        break;
                                    }
                                }
                            }
                        } else if (totalInput > 5000 && personInput < 10000 && ((personOutput + preGiftPrice) / personInput) < (preWant - 0.04)) {// < 10000
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.5) {
                                start = 5;
                            } else if (randomNumber < 0.6) {
                                start = 6;
                            } else if (randomNumber < 0.7) {
                                start = 7;
                            } else if (randomNumber < 0.8) {
                                start = 8;
                            } else {
                                start = 8;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {// -2 是为了在5200之内做补偿 从上往下补
                                if (giftPriceList[jj] <= preGiftPrice) break;
                                if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2074) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                    continue;
                                log.info("[ 命中下限4 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        } else if (totalInput > 5000 && personInput > 10 &&
                                (((personOutput + preGiftPrice) / personInput) < (preWant - 0.17) ||
                                        (totalOutput + preGiftPrice) / totalInput < (preWant - 0.02))) {// 下限
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.5) {
                                start = 5;
                            } else if (randomNumber < 0.6) {
                                start = 6;
                            } else if (randomNumber < 0.7) {
                                start = 7;
                            } else if (randomNumber < 0.8) {
                                start = 8;
                            } else {
                                start = 8;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {// -2 是为了在5200之内做补偿 从上往下补
                                if ((personOutput + giftPriceList[jj]) / personInput > (preWant + 0.2474) || (totalOutput + giftPriceList[jj]) / totalInput > (preWant - 0.000011))
                                    continue;
                                log.info("[ 命中下限6 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        }


                    }

                    Integer realGiftPrice = giftMap.get(giftId);
                    if (realGiftPrice == null) {
                        realGiftPrice = tmpGiftMap.get(giftId);
                    }

                    totalOutput += realGiftPrice;
                    personOutput += realGiftPrice;
                    tempTotalOutput += realGiftPrice;
                    tempPersonOutput += realGiftPrice;

                    if (result.get(giftId) == null) {
                        result.put(giftId, 1);
                    } else {
                        result.put(giftId, result.get(giftId) + 1);
                    }
                }

                // 抽完 num 次数即退出
                if (drawNum < 1) {
                    break;
                }
            }

            redisManager.hincrBy(RedisKey.user_gift_first.getKey(), uid.toString(), 1L);
        } finally {
            redisManager.unlock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), lockVal);
        }

        if (result.size() == 0) {
            return new DrawConchVO();
        }

        result.forEach((giftId, giftNum) -> {
            try {
                userGiftPurseManager.addGiftPurseCache(uid, giftId, giftNum);
            } catch (WebServiceException e) {
                log.info("[ draw 刷新礼物缓存失败  ] uid :>{}  giftId >:{}, giftNum >:{}", uid, giftId, giftNum);
            }
        });

        //总投入
        redisManager.hincrBy(RedisKey.xqgift_draw_input.getKey(), GiftDrawKey.total.toString(), (long) tempTotalInput);
        //个人总投入
        redisManager.zincrby(RedisKey.xquser_gift_draw_input.getKey(), uid.toString(), tempPersonInput);
        //总产出
        redisManager.hincrBy(RedisKey.xqgift_draw_output.getKey(), GiftDrawKey.total.toString(), (long) tempTotalOutput);
        //个人总产出
        redisManager.zincrby(RedisKey.xquser_gift_draw_output.getKey(), uid.toString(), tempPersonOutput);

        List<DrawGiftVO> list = handleResult(result);
        String messageId = UUIDUtil.uuid();
        GiftDrawMessage message = new GiftDrawMessage(messageId, System.currentTimeMillis(), roomId, uid, type, result);
        String msg = gson.toJson(message);
        redisManager.hset(RedisKey.mq_gift_draw_message_status.getKey(), messageId, msg);
        activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_DRAW_QUEUE, msg);

        return new DrawConchVO(list, ticketGiftId,
                num == null ? null : (num.get("reduceAfterConchNum") == null ? 0L : num.get("reduceAfterConchNum")),
                num == null ? null : (num.get("reduceAfterTryCoinNum") == null ? 0L : num.get("reduceAfterTryCoinNum")));
    }


    @Override
    public DrawConchVO doDraw(Long uid, Integer type, Long roomId) throws WebServiceException {
        if (uid == null || type == null || roomId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        SysConfDTO lotteryBoxOption = sysconfManager.getSysConf(SysConfigId.lottery_box_option);
        if (lotteryBoxOption == null || "0".equals(lotteryBoxOption.getConfigValue())) {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        RoomDTO roomDto = roomManager.getRoom(roomId);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (roomDto.getTagId().equals(systemConf.getXiangQinTagId())) {// 是相亲房
            throw new WebServiceException(WebServiceCode.XQ_GIFT_TYPE_ERROR);
        }

        RoomConfDTO roomConfDto = null;
        String json = redisManager.hget(RedisKey.room_conf.getKey(String.valueOf(roomDto.getType())), String.valueOf(roomDto.getUid()));
        if (StringUtils.isNotBlank(json)) {
            roomConfDto = gson.fromJson(json, RoomConfDTO.class);
        }

        int drawNum = this.getDrawNum(type);
        int totalDrawNum = drawNum;
        String lockVal = redisManager.lock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), 10000);
        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.OPE_FREQUENT);
        }

        Map<Integer, Integer> result = new HashMap<>(drawNum);

        int Min = drawNum;
        int Max = drawNum * 3;
        int ticketNum = Min + (int) (Math.random() * ((Max - Min) + 1));// ticketNum范围 Min~Max

        Integer ticketGiftId = giftDrawConf.getTicketGiftId();
        log.info("[ 捡海螺人气票 ] uid:>{}, 抽取次数:>{}, 获得票数:>{}, ", uid, drawNum, ticketNum);
        result.put(ticketGiftId, ticketNum);

        int[] giftIdList = giftDrawConf.getDrawGifts();
        int[] giftPriceList = giftDrawConf.getGiftPrices();

        Map<Integer, Integer> giftMap = new HashMap<>(giftIdList.length);
        for (int j = 0; j < giftIdList.length; j++) {
            giftMap.put(giftIdList[j], giftPriceList[j]);
        }

        int[] tmpGiftIdList = giftDrawConf.getTmpDrawGifts();
        int[] tmpGiftPriceList = giftDrawConf.getTmpDrawGiftsPrice();
        Map<Integer, Integer> tmpGiftMap = new HashMap<>(giftIdList.length);
        for (int j = 0; j < tmpGiftIdList.length; j++) {
            tmpGiftMap.put(tmpGiftIdList[j], tmpGiftPriceList[j]);
        }

        double totalInput = 0, totalOutput = 0, tempTotalInput = 0, tempTotalOutput = 0;
        Double personInput = 0.0, tempPersonInput = 0.0;
        Double personOutput = 0.0, tempPersonOutput = 0.0;

        String totalInputStr = redisManager.hget(RedisKey.gift_draw_input.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalInputStr)) totalInput = Double.parseDouble(totalInputStr);

        String totalOutputStr = redisManager.hget(RedisKey.gift_draw_output.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalOutputStr)) totalOutput = Double.parseDouble(totalOutputStr);

        Double personInputDouble = redisManager.zscore(RedisKey.user_gift_draw_input.getKey(), uid.toString());
        if (personInputDouble != null) personInput = personInputDouble;

        Double personOutPutDouble = redisManager.zscore(RedisKey.user_gift_draw_output.getKey(), uid.toString());
        if (personOutPutDouble != null) personOutput = personOutPutDouble;

        String isBai = redisManager.hget(RedisKey.is_bai.getKey(), uid.toString());

        log.info("[ 概率全局 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, totalInput, totalOutput, totalOutput / totalInput);
        log.info("[ 概率个人 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, personInput, personOutput, personOutput / personInput);

        Map<String, Long> num = new HashMap<>();
        try {
            Integer giftId;
            userPurseManager.updateReduceConch(uid, drawNum, false, "捡海螺", num);
            // 捡海螺礼物ID必须要与人气票礼物ID不一致
            for (GiftDrawManager drawManager : this.getDrawManagers()) {
                if (!drawManager.check(uid, roomConfDto, totalDrawNum, false, false)) {
                    continue;
                }

                for (; drawNum > 0; drawNum--) {
                    giftId = drawManager.draw(uid, roomDto.getUid(), totalDrawNum, false, false);
                    if (giftId == -1) {
                        break;
                    }

                    Integer preGiftPrice;
                    preGiftPrice = giftMap.get(giftId);
                    if (preGiftPrice == null) {
                        preGiftPrice = tmpGiftMap.get(giftId);
                    }

                    totalInput += 20;
                    personInput += 20;
                    tempTotalInput += 20;
                    tempPersonInput += 20;


                    log.info("[ 概率对象 ] drawname >:{}, isbai >: {}", drawManager.getClass().getName(), isBai);
                    if (!drawManager.getClass().getName().contains("MustWinningDrawManagerImpl")) {
                        if (!StringUtils.isBlank(isBai) && totalInput > 3000 && personInput > 100 && ((personOutput + preGiftPrice) / personInput) < (1.03011 - 0.02011)) {// 下限
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.6) {
                                start = 5;
                            } else {
                                start = 6;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {//
//                                    if (giftPriceList[jj] <= preGiftPrice) break;
                                if ((personOutput + giftPriceList[jj]) / personInput > (1.03011 + 0.1274) || (totalOutput + giftPriceList[jj]) / totalInput > (1.03011 - 0.00011))
                                    continue;
                                log.info("[ 命中白名单下限 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        }
                        /*else if (personInput > 1000 && ((personOutput + 10) / personInput) > (1.03011 + 0.095)) {
                                    for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                        if (giftPriceList[jj] >= preGiftPrice) continue;
                                        if (giftPriceList[jj] > 1888) continue;
                                        if ((personOutput + giftPriceList[jj]) / personInput > (1.03011 + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (1.03011 - 0.00011)) continue;
                                        log.info("[ 命中上限1 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                        giftId = giftIdList[jj];
                                        break;
                                    }
                                    //for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                    //    if (giftPriceList[jj] > 1000) continue;
                                    //    if (giftPriceList[jj] > preGiftPrice) continue;
                                    //    log.info("[ 命中上限1 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                    //    giftId = giftIdList[jj];
                                    //    break;
                                    //}
                        }*/

                        else if (totalInput > 1500 && personInput > 1000 && (personOutput + preGiftPrice) / personInput > (1.03011 - 0.03)) {// 上限
                            if (preGiftPrice > 66) {
                                double randomNumber = RandomUtils.threadLocalRandomDouble();
                                if ((randomNumber > 0.97 && (personOutput + preGiftPrice) / personInput > (1.03011 - 0.03)) ||
                                        (randomNumber > 0.90 && (personOutput + preGiftPrice) / personInput > (1.03011 - 0.02)) ||
                                        (randomNumber > 0.80 && (personOutput + preGiftPrice) / personInput > (1.03011 - 0.01)) ||
                                        (randomNumber > 0.70 && (personOutput + preGiftPrice) / personInput > (1.03011 - 0.00)) ||
                                        (randomNumber > 0.60 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.01)) ||
                                        (randomNumber > 0.55 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.02)) ||
                                        (randomNumber > 0.50 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.03)) ||
                                        (randomNumber > 0.45 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.04)) ||
                                        (randomNumber > 0.40 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.05)) ||
                                        (randomNumber > 0.35 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.07)) ||
                                        (randomNumber > 0.30 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.12)) ||
                                        (randomNumber > 0.20 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.16)) ||
                                        (randomNumber > 0.10 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.23)) ||
                                        (randomNumber > 0.00 && (personOutput + preGiftPrice) / personInput > (1.03011 + 0.30))
                                ) {
                                    // 降一位
                                    for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                        if (giftPriceList[jj] >= preGiftPrice) continue;
                                        //if ((personOutput + giftPriceList[jj]) / personInput > (1.03011 + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (1.03011 - 0.00011)) continue;
                                        log.info("[ 命中上限2 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                        giftId = giftIdList[jj];
                                        break;
                                    }
                                } else {
                                    // 消掉5200以上
                                    for (int jj = giftPriceList.length - 1; jj > 0; jj--) {//  从上往下补
                                        if (giftPriceList[jj] > preGiftPrice) continue;
                                        if ((personOutput + giftPriceList[jj]) / personInput > (1.03011 + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (1.03011 - 0.00011))
                                            continue;
                                        log.info("[ 命中上限3 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                        giftId = giftIdList[jj];
                                        break;
                                    }
                                }
                            }
                        } else if (totalInput > 5000 && personInput < 5000 && ((personOutput + preGiftPrice) / personInput) < (1.03011 - 0.0726)) {// < 10000
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.6) {
                                start = 5;
                            } else {
                                start = 6;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {// -2 是为了在5200之内做补偿 从上往下补
                                if (giftPriceList[jj] <= preGiftPrice) break;
                                if ((personOutput + giftPriceList[jj]) / personInput > (1.03011 + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (1.03011 - 0.00011))
                                    continue;
                                log.info("[ 命中下限4 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        }
//                        else if (totalInput > 5000 && (personInput % 2000) < 1000 && ((personOutput + preGiftPrice) / personInput) < (1.03011 - 0.0826)) {// %1000
//                            double randomNumber = RandomUtils.threadLocalRandomDouble();
//                            int start = 2;
//                            if (randomNumber < 0.06) {start = 2;}
//                            else if (randomNumber < 0.12) {start = 3;}
//                            else if (randomNumber < 0.25) {start = 4;}
//                            else if (randomNumber < 0.6){start = 5;}
//                            else {start = 6;}
//                                for (int jj = giftPriceList.length - 1; jj > start; jj--) {// -2 是为了在5200之内做补偿 从上往下补
//                                    if (giftPriceList[jj] <= preGiftPrice) break;
//                                    if ((personOutput + giftPriceList[jj]) / personInput > (1.03011 + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (1.03011 - 0.00011) ) continue;
//                                    log.info("[ 命中下限5 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
//                                    giftId = giftIdList[jj];
//                                    break;
//                                }
//                        }
                        else if (totalInput > 5000 && personInput > 10 && (totalOutput + preGiftPrice) / totalInput < 0.78) {// 下限
                            double randomNumber = RandomUtils.threadLocalRandomDouble();
                            int start = 2;
                            if (randomNumber < 0.06) {
                                start = 2;
                            } else if (randomNumber < 0.12) {
                                start = 3;
                            } else if (randomNumber < 0.25) {
                                start = 4;
                            } else if (randomNumber < 0.6) {
                                start = 5;
                            } else {
                                start = 6;
                            }
                            for (int jj = giftPriceList.length - 1; jj > start; jj--) {// -2 是为了在5200之内做补偿 从上往下补
                                if (giftPriceList[jj] <= preGiftPrice) break;
                                if ((personOutput + giftPriceList[jj]) / personInput > (1.03011 + 0.2174) || (totalOutput + giftPriceList[jj]) / totalInput > (1.03011 - 0.00011))
                                    continue;
                                log.info("[ 命中下限6 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftIdList[jj]);
                                giftId = giftIdList[jj];
                                break;
                            }
                        }


                    }

                    Integer realGiftPrice = giftMap.get(giftId);
                    if (realGiftPrice == null) {
                        realGiftPrice = tmpGiftMap.get(giftId);
                    }

                    totalOutput += realGiftPrice;
                    personOutput += realGiftPrice;
                    tempTotalOutput += realGiftPrice;
                    tempPersonOutput += realGiftPrice;

                    if (result.get(giftId) == null) {
                        result.put(giftId, 1);
                    } else {
                        result.put(giftId, result.get(giftId) + 1);
                    }
                }

                // 抽完 num 次数即退出
                if (drawNum < 1) {
                    break;
                }
            }

            redisManager.hincrBy(RedisKey.user_gift_first.getKey(), uid.toString(), 1L);
        } finally {
            redisManager.unlock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), lockVal);
        }

        if (result.size() == 0) {
            return new DrawConchVO();
        }

        result.forEach((giftId, giftNum) -> {
            try {
                userGiftPurseManager.addGiftPurseCache(uid, giftId, giftNum);
            } catch (WebServiceException e) {
                log.info("[ draw 刷新礼物缓存失败  ] uid :>{}  giftId >:{}, giftNum >:{}", uid, giftId, giftNum);
            }
        });

        //总投入
        redisManager.hincrBy(RedisKey.gift_draw_input.getKey(), GiftDrawKey.total.toString(), (long) tempTotalInput);
        //个人总投入
        redisManager.zincrby(RedisKey.user_gift_draw_input.getKey(), uid.toString(), tempPersonInput);
        //总产出
        redisManager.hincrBy(RedisKey.gift_draw_output.getKey(), GiftDrawKey.total.toString(), (long) tempTotalOutput);
        //个人总产出
        redisManager.zincrby(RedisKey.user_gift_draw_output.getKey(), uid.toString(), tempPersonOutput);

        List<DrawGiftVO> list = handleResult(result);
        String messageId = UUIDUtil.uuid();
        GiftDrawMessage message = new GiftDrawMessage(messageId, System.currentTimeMillis(), roomId, uid, type, result);
        String msg = gson.toJson(message);
        redisManager.hset(RedisKey.mq_gift_draw_message_status.getKey(), messageId, msg);
        activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_DRAW_QUEUE, msg);

        return new DrawConchVO(list, ticketGiftId,
                num == null ? null : (num.get("reduceAfterConchNum") == null ? 0L : num.get("reduceAfterConchNum")),
                num == null ? null : (num.get("reduceAfterTryCoinNum") == null ? 0L : num.get("reduceAfterTryCoinNum")));
    }


    @Override
    public void handleDrawMessage(GiftDrawMessage message) {
        if (message.getUid() == null || message.getCreateTime() == null || message.getRoomId() == null || message.getDrawType() == null) {
            return;
        }

        UsersDTO usersDto = usersManager.getUser(message.getUid());
        List<UserDrawGiftRecordDO> records = Lists.newArrayList();
        List<BillGiftDrawDO> billGiftDrawDOList = Lists.newArrayList();
        Date now = new Date(message.getCreateTime());
        int drawNum = 0;
        try {
            drawNum = this.getDrawNum(message.getDrawType());
        } catch (WebServiceException e) {
            e.printStackTrace();
        }
        long goldPrice = drawNum * 20;

        if (drawNum > 0) {
            // 更新打call用户信息  减少sql的海螺次数
            userPurseDao.updateConchCost(usersDto.getUid(), (long) drawNum);
        } else {
            log.error("[ 捡海螺记录 mq] 参数异常, uid:>{}, conchNum:>{}", usersDto.getUid(), drawNum);
        }


        message.getResult().forEach((giftId, giftNum) -> {
            // 全服通知
            GiftDTO giftDto = giftManager.getValidGiftById(giftId);
            if (giftDto == null) {
                return;
            }

            try {
                userGiftPurseManager.updateUserGiftPurse(message.getUid(), giftDto.getGiftId(), giftNum);
            } catch (WebServiceException e) {
                log.error("[ 捡海螺 ]uid:>{}, giftId:>{}, 放入礼物背包异常：", message.getUid(), giftDto.getGiftId(), e);
            }

            if (giftDto.getGiftId() == giftDrawConf.getTicketGiftId()) {
                return;
            }

            long outputGold = giftDto.getGoldPrice() * giftNum;
            //总产出
//            redisManager.hincrBy(RedisKey.gift_draw_output.getKey(), GiftDrawKey.total.toString(), outputGold);
            //个人总产出
//            redisManager.zincrby(RedisKey.user_gift_draw_output.getKey(), message.getUid().toString(), Double.valueOf(outputGold));

            // 产出金币要求实时
            if (giftDto.getGiftId().equals(giftDrawConf.getFullGiftId())) {
                // 全服
                redisManager.hincrBy(RedisKey.gift_draw_output.getKey(), GiftDrawKey.full_gift.toString(), giftNum.longValue());
            }

            records.add(new UserDrawGiftRecordDO(message.getUid(), message.getRoomId(), giftId, giftNum, message.getDrawType(), now));
            if (message.getDrawType() == GiftType.DRAW || message.getDrawType() == GiftType.XIANGQIN) {
                billGiftDrawDOList.add(new BillGiftDrawDO(message.getUid(), giftId, giftNum, 0, now));
            } else {
                billGiftDrawDOList.add(new BillGiftDrawDO(message.getUid(), giftId, giftNum, 20 * giftNum, now));
            }

            // 发房间全服
            if (giftDto.getGoldPrice() >= 1000) {
                this.sendGiftAllRoom(message.getRoomId(), usersDto, giftDto, giftNum);
            }
        });

        if (records.size() > 0) {
            giftRecordDao.saveMany(records);
        }

        if (billGiftDrawDOList.size() > 0) {
            giftDrawDao.saveMany(billGiftDrawDOList);
        }

        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_gift_draw_message_status.getKey(), message.getMessageId());
        redisManager.hdel(RedisKey.gift_draw_record.getKey(), message.getUid() + "_" + GiftType.DRAW);
        redisManager.hdel(RedisKey.gift_draw_record.getKey(), message.getUid() + "_" + GiftType.XIANGQIN);
        redisManager.hdel(RedisKey.gift_draw_record.getKey(), message.getUid() + "_" + GiftType.ACTIVITY);

    }

    @Override
    public void handleTryDrawMessage(GiftDrawMessage message) {
        if (message.getUid() == null || message.getCreateTime() == null || message.getRoomId() == null || message.getDrawType() == null) {
            return;
        }

        UsersDTO usersDto = usersManager.getUser(message.getUid());
        List<UserDrawGiftRecordDO> records = Lists.newArrayList();
        List<BillGiftDrawDO> billGiftDrawDOList = Lists.newArrayList();
        Date now = new Date(message.getCreateTime());
        int drawNum = 0;
        try {
            drawNum = this.getDrawNum(message.getDrawType());
        } catch (WebServiceException e) {
            e.printStackTrace();
        }
        long goldPrice = drawNum * 20;

        if (drawNum > 0) {
            // 更新打call用户信息  减少sql的海螺次数
            userPurseDao.updateTryCoinCost(usersDto.getUid(), (long) drawNum);
        } else {
            log.error("[ 体验记录 mq] 参数异常, uid:>{}, conchNum:>{}", usersDto.getUid(), drawNum);
        }


        message.getResult().forEach((giftId, giftNum) -> {
            // 全服通知
            GiftDTO giftDto = giftManager.getValidGiftById(giftId);
            if (giftDto == null) {
                return;
            }

            try {
                userGiftPurseManager.updateUserGiftPurse(message.getUid(), giftDto.getGiftId(), giftNum);
            } catch (WebServiceException e) {
                log.error("[ 捡海螺 ]uid:>{}, giftId:>{}, 放入礼物背包异常：", message.getUid(), giftDto.getGiftId(), e);
            }

            if (giftDto.getGiftId() == giftDrawConf.getTicketGiftId()) {
                return;
            }

            long outputGold = giftDto.getGoldPrice() * giftNum;
            //总产出
//            redisManager.hincrBy(RedisKey.gift_draw_output.getKey(), GiftDrawKey.total.toString(), outputGold);
            //个人总产出
//            redisManager.zincrby(RedisKey.user_gift_draw_output.getKey(), message.getUid().toString(), Double.valueOf(outputGold));

            // 产出金币要求实时

            records.add(new UserDrawGiftRecordDO(message.getUid(), message.getRoomId(), giftId, giftNum, message.getDrawType(), now));
            if (message.getDrawType() == 3) {
                billGiftDrawDOList.add(new BillGiftDrawDO(message.getUid(), giftId, giftNum, 0, now));
            } else {
                billGiftDrawDOList.add(new BillGiftDrawDO(message.getUid(), giftId, giftNum, 20 * giftNum, now));
            }

            // 发房间全服
            if (giftDto.getGoldPrice() >= 520) {
                this.sendGiftAllRoom(message.getRoomId(), usersDto, giftDto, giftNum);
            }
        });

//        if (records.size() > 0) {
//            giftRecordDao.saveMany(records);
//        }
//
//        if (billGiftDrawDOList.size() > 0) {
//            giftDrawDao.saveMany(billGiftDrawDOList);
//        }

        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_gift_draw_message_status.getKey(), message.getMessageId());
        redisManager.hdel(RedisKey.gift_draw_record.getKey(), message.getUid() + "_" + GiftType.DRAW);
        redisManager.hdel(RedisKey.gift_draw_record.getKey(), message.getUid() + "_" + GiftType.XIANGQIN);
        redisManager.hdel(RedisKey.gift_draw_record.getKey(), message.getUid() + "_" + GiftType.ACTIVITY);
    }

    @Override
    public List<DrawGiftVO> dyDraw(Long uid, Integer type, Long roomId) throws WebServiceException {
        if (uid == null || type == null || roomId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        SysConfDTO lotteryBoxOption = sysconfManager.getSysConf(SysConfigId.lottery_box_option);
        if (lotteryBoxOption == null || "0".equals(lotteryBoxOption.getConfigValue())) {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        RoomDTO roomDto = roomManager.getRoom(roomId);
        if (roomDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        RoomConfDTO roomConfDto = null;
        String json = redisManager.hget(RedisKey.room_conf.getKey(String.valueOf(roomDto.getType())), String.valueOf(roomDto.getUid()));
        if (StringUtils.isNotBlank(json)) {
            roomConfDto = gson.fromJson(json, RoomConfDTO.class);
        }

        int drawNum = this.getDrawNum(type);
        int totalDrawNum = drawNum;
        int goldPrice = drawNum * 20;
        String lockVal = redisManager.lock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), 10000);
        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.OPE_FREQUENT);
        }

        int[] giftIdList = giftDrawConf.getDrawGifts();
        int[] giftPriceList = giftDrawConf.getGiftPrices();

        Map<Integer, Integer> giftMap = new HashMap<>(giftIdList.length);
        for (int j = 0; j < giftIdList.length; j++) {
            giftMap.put(giftIdList[j], giftPriceList[j]);
        }

        double totalInput = 0, totalOutput = 0, tempTotalInput = 0, tempTotalOutput = 0;
        Double personInput = 0.0, tempPersonInput = 0.0;
        Double personOutput = 0.0, tempPersonOutput = 0.0;

        String totalInputStr = redisManager.hget(RedisKey.gift_draw_input.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalInputStr)) totalInput = Double.parseDouble(totalInputStr);

        String totalOutputStr = redisManager.hget(RedisKey.gift_draw_output.getKey(), GiftDrawKey.total.toString());
        if (!StringUtils.isBlank(totalOutputStr)) totalOutput = Double.parseDouble(totalOutputStr);

        Double personInputDouble = redisManager.zscore(RedisKey.user_gift_draw_input.getKey(), uid.toString());
        if (personInputDouble != null) personInput = personInputDouble;

        Double personOutPutDouble = redisManager.zscore(RedisKey.user_gift_draw_output.getKey(), uid.toString());
        if (personOutPutDouble != null) personOutput = personOutPutDouble;

        log.info("[ 概率全局 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, totalInput, totalOutput, totalOutput / totalInput);
        log.info("[ 概率个人 ] uid :>{}  投入 >:{}, 产出 >:{}, 比率 >:{}", uid, personInput, personOutput, personOutput / personInput);

        Map<Integer, Integer> result = new HashMap<>(drawNum);
        try {
            Integer giftId;
            userPurseManager.updateReduceGold(uid, goldPrice, false, "捡海螺", null);
            for (GiftDrawManager drawManager : this.getDrawManagers()) {
                if (!drawManager.check(uid, roomConfDto, totalDrawNum, false, false)) {
                    continue;
                }

                for (; drawNum > 0; drawNum--) {
                    giftId = drawManager.draw(uid, roomDto.getUid(), totalDrawNum, false, false);
                    if (giftId == -1) {
                        break;
                    }

                    Integer preGiftPrice = giftMap.get(giftId);
                    totalInput += 20;
                    personInput += 20;
                    tempTotalInput += 20;
                    tempPersonInput += 20;

                    log.info("[ 概率对象 ] drawname >:{}", drawManager.getClass().getName());
                    if (!drawManager.getClass().getName().contains("MustWinningDrawManagerImpl")) {
                        if (totalInput > 1000 && ((totalOutput + preGiftPrice) / totalInput) > 1.16) {// 全服上限
                            if (preGiftPrice >= 500) {
                                log.info("[ 命中上限 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, giftId, giftDrawConf.getDefaultGiftId());
                                giftId = giftDrawConf.getDefaultGiftId();
                            } else {
                                log.info("[ 命中上限 ] uid :>{}  替换前 >:{}, 但不替换", uid, giftId);
                            }
                        }
//                        else if ((personInput > 500)&&((personOutput + preGiftPrice) / personInput < 0.75)) {// 个人下限 包括500往下补 补了之后超过116 继续往下
//                            int preGiftId = giftId;
//                            int indexMin = 2;
//                            int indexMax = 3;
//                            int index = indexMin + (int) (Math.random() * ((indexMax - indexMin) + 1));// giftIdList 索引2到3
//                            giftId = giftIdList[index];
//                            log.info("[ 命中下限 ] uid :>{}  替换前 >:{}, 替换后 >:{}", uid, preGiftId, giftId);
//                        }
                    }
                    Integer realGiftPrice = giftMap.get(giftId);
                    totalOutput += realGiftPrice;
                    personOutput += realGiftPrice;
                    tempTotalOutput += realGiftPrice;
                    tempPersonOutput += realGiftPrice;


                    if (result.get(giftId) == null) {
                        result.put(giftId, 1);
                    } else {
                        result.put(giftId, result.get(giftId) + 1);
                    }
                }

                // 抽完 num 次数即退出
                if (drawNum < 1) {
                    break;
                }
            }

            redisManager.hincrBy(RedisKey.user_gift_first.getKey(), uid.toString(), 1L);
        } finally {
            redisManager.unlock(RedisKey.gift_draw_user_lock.getKey(uid.toString()), lockVal);
        }

        if (result.size() == 0) {
            return Lists.newArrayList();
        }

        result.forEach((giftId, giftNum) -> {
            try {
                userGiftPurseManager.addGiftPurseCache(uid, giftId, giftNum);
            } catch (WebServiceException e) {
                log.info("[ draw 刷新礼物缓存失败  ] uid :>{}  giftId >:{}, giftNum >:{}", uid, giftId, giftNum);
            }
        });

        log.info("[ draw消息生产 ] (long)totalInput :>{}, totalOutput :>{}, personInput :>{}, personOutput :>{}", (long) totalInput, (long) totalOutput, personInput, personOutput);

        //总投入
        redisManager.hincrBy(RedisKey.gift_draw_input.getKey(), GiftDrawKey.total.toString(), (long) tempTotalInput);
        //个人总投入
        redisManager.zincrby(RedisKey.user_gift_draw_input.getKey(), uid.toString(), tempPersonInput);
        //总产出
        redisManager.hincrBy(RedisKey.gift_draw_output.getKey(), GiftDrawKey.total.toString(), (long) tempTotalOutput);
        //个人总产出
        redisManager.zincrby(RedisKey.user_gift_draw_output.getKey(), uid.toString(), tempPersonOutput);

        List<DrawGiftVO> list = handleResult(result);
        String messageId = UUIDUtil.uuid();
        GiftDrawMessage message = new GiftDrawMessage(messageId, System.currentTimeMillis(), roomId, uid, type, result);
        log.info("[ draw消息生产 ] messageId :>{}  createTime >:{}", messageId, message.getCreateTime());
        String msg = gson.toJson(message);
        redisManager.hset(RedisKey.mq_gift_draw_message_status.getKey(), messageId, msg);
        activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_DY_DRAW_QUEUE, msg);
        return list;
    }

    @Override
    public void handleDyDrawMessage(GiftDrawMessage message) {
        if (message.getUid() == null || message.getCreateTime() == null || message.getRoomId() == null || message.getDrawType() == null) {
            return;
        }
        UsersDTO usersDto = usersManager.getUser(message.getUid());
        List<UserDrawGiftRecordDO> records = Lists.newArrayList();
        List<BillGiftDrawDO> billGiftDrawDOList = Lists.newArrayList();
        Date now = new Date(message.getCreateTime());
        int drawNum = 0;
        try {
            drawNum = this.getDrawNum(message.getDrawType());
        } catch (WebServiceException e) {
            e.printStackTrace();
        }
        long goldPrice = drawNum * 20;

//        if (!StringUtils.isBlank(message.getNoticeMsg())) {
//            this.sendMsgRoom(message.getRoomId(), usersDto, message.getNoticeMsg());
//        }

        GiftDTO maxGiftDto = null;
        Integer maxGiftNum = 0;

        for (Integer key : message.getResult().keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
            Integer giftId = key;
            Integer giftNum = message.getResult().get(key);

            // 全服通知
            GiftDTO giftDto = giftManager.getValidGiftById(giftId);
            if (giftDto == null) {
                return;
            }

            try {
                userGiftPurseManager.updateUserGiftPurse(message.getUid(), giftDto.getGiftId(), giftNum);
            } catch (WebServiceException e) {
                log.error("[ 捡海螺 ]uid:>{}, giftId:>{}, 放入礼物背包异常：", message.getUid(), giftDto.getGiftId(), e);
            }

            long outputGold = giftDto.getGoldPrice() * giftNum;
//            //总产出
//            redisManager.hincrBy(RedisKey.gift_draw_output.getKey(), GiftDrawKey.total.toString(), outputGold);
//            //个人总产出
//            redisManager.zincrby(RedisKey.user_gift_draw_output.getKey(), message.getUid().toString(), Double.valueOf(outputGold));

            // 产出金币要求实时
            if (giftDto.getGiftId().equals(giftDrawConf.getFullGiftId())) {
                // 全服
                redisManager.hincrBy(RedisKey.gift_draw_output.getKey(), GiftDrawKey.full_gift.toString(), giftNum.longValue());
            }

            records.add(new UserDrawGiftRecordDO(message.getUid(), message.getRoomId(), giftId, giftNum, message.getDrawType(), now));
            if (message.getDrawType() == 3) {
                billGiftDrawDOList.add(new BillGiftDrawDO(message.getUid(), giftId, giftNum, 0, now));
            } else {
                billGiftDrawDOList.add(new BillGiftDrawDO(message.getUid(), giftId, giftNum, 20 * giftNum, now));
            }

            if (maxGiftDto == null || giftDto.getGoldPrice() > maxGiftDto.getGoldPrice()) {
                maxGiftDto = giftDto;
                maxGiftNum = giftNum;
            }
        }

        if (maxGiftDto != null && maxGiftDto.getGoldPrice() >= 500) {
            // 发房间全服
            this.sendGiftAllRoom(message.getRoomId(), usersDto, maxGiftDto, maxGiftNum);
            // 发送全服小秘书通知
            asyncNetEaseTrigger.sendWordMsg(null, 1, 0, "哇塞了，" + usersDto.getNick() + "获得了" + maxGiftDto.getGiftName() + " x " + maxGiftNum);
        }

        if (records.size() > 0) {
            giftRecordDao.saveMany(records);
        }

        if (billGiftDrawDOList.size() > 0) {
            giftDrawDao.saveMany(billGiftDrawDOList);
        }

        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_gift_draw_message_status.getKey(), message.getMessageId());
        redisManager.hdel(RedisKey.gift_draw_record.getKey(), String.valueOf(message.getUid()));
    }

    @Override
    public List<?> getRank(Integer type, Integer giftType) {
        if (giftType == null) {// 兼容旧版
            String result = redisManager.hget(RedisKey.gift_draw_rank.getKey(), type.toString());
            if (StringUtils.isNotBlank(result)) {
                return gson.fromJson(result, new TypeToken<List<?>>() {
                }.getType());
            }
        } else {
            if (giftType.equals(GiftType.DRAW)) {
                String result = redisManager.hget(RedisKey.gift_draw_rank.getKey(), type + "_" + GiftType.DRAW);
                if (StringUtils.isNotBlank(result)) {
                    return gson.fromJson(result, new TypeToken<List<UserDrawGiftRankDO>>() {
                    }.getType());
                }
            } else if (giftType.equals(GiftType.XIANGQIN)) {
                String result = redisManager.hget(RedisKey.gift_draw_rank.getKey(), type + "_" + GiftType.XIANGQIN);
                if (StringUtils.isNotBlank(result)) {
                    return gson.fromJson(result, new TypeToken<List<UserDrawGiftRankDO>>() {
                    }.getType());
                }
            }
        }

        return Lists.newArrayList();
    }

    @Override
    public List<BillGiftDrawDTO> record(Long uid, Integer giftType, Integer pageNum, Integer pageSize) {
        UsersDTO user = usersManager.getUser(uid);
        if (user == null) {
            return Lists.newArrayList();
        }

        Integer resultType = giftType == null ? GiftType.DRAW : giftType;// 旧版本兼容
        String fieldKey = uid + "_" + giftType;
        String value = redisManager.hget(RedisKey.gift_draw_record.getKey(), fieldKey);
        List<BillGiftDrawDTO> list;
        if (StringUtils.isBlank(value)) {
            // 没有用户的记录, 去数据库查询 缓存 100 条
            list = giftDrawDao.listByUid(uid, resultType, 0, 100);
            if (list != null && !list.isEmpty()) {
                redisManager.hset(RedisKey.gift_draw_record.getKey(), fieldKey, gson.toJson(list));
            } else {
                return Lists.newArrayList();
            }
        } else {
            list = gson.fromJson(value, new TypeToken<List<BillGiftDrawDTO>>() {
            }.getType());
        }

//        List<BillGiftDrawDTO> resultList = new ArrayList<>();
//        for (BillGiftDrawDTO drawDTO : list) {
//            if (drawDTO.getGiftType().equals(resultType)){
//                resultList.add(drawDTO);
//            }
//        }

        pageNum = pageNum == null || pageNum == 0 ? 1 : pageNum;
        pageSize = pageSize == null ? 20 : pageSize;
        int begin = (pageNum - 1) * pageSize;
        if (begin > list.size()) {
            return Lists.newArrayList();
        }
        int end = pageNum * pageSize > list.size() ? list.size() : pageNum * pageSize;
        return list.subList(begin, end);
    }

    /**
     * 获取捡海螺奖池礼物
     *
     * @param uid uid
     * @return GiftVO
     */
    @Override
    public List<GiftVO> getPrizePoolGift(Long uid, Integer giftType) {
        List<GiftVO> giftVOList = Lists.newArrayList();
        int[] drawGifts = null;
        if (giftType == null) {
            drawGifts = giftDrawConf.getDrawGifts();
        } else if (giftType == GiftType.DRAW) {
            drawGifts = giftDrawConf.getDrawGifts();
        } else if (giftType == GiftType.XIANGQIN) {
            drawGifts = giftDrawConf.getXqDrawGifts();
        } else if (giftType == GiftType.ACTIVITY) {
            drawGifts = giftDrawConf.getHdDrawGifts();
        } else {
            drawGifts = giftDrawConf.getDrawGifts();
        }
        for (int giftId : drawGifts) {
            GiftDTO giftDto = giftManager.getValidGiftById(giftId);
            giftVOList.add(toVo(giftDto));
        }
        return giftVOList;
    }


    // ============================================= 私有方法 =============================================

    /**
     * 根据类型获取捡海螺次数
     *
     * @param type 捡海螺类型
     * @return
     * @throws WebServiceException
     */
    private int getDrawNum(int type) throws WebServiceException {
        if (giftDrawConf.getDrawTypes() == null) {
            throw new WebServiceException(WebServiceCode.NOT_EXISTS);
        }

        DrawTypeConf typeConf = giftDrawConf.getDrawTypes().get(type);
        if (typeConf == null) {
            throw new WebServiceException(WebServiceCode.NOT_EXISTS);
        }

        Integer drawNum = typeConf.getDrawCount();
        if (drawNum == null) {
            throw new WebServiceException(WebServiceCode.NOT_EXISTS);
        }
        return drawNum;
    }

    /**
     * @return
     */
    private List<GiftDrawManager> getDrawManagers() {
        if (drawManagers.size() == 0) {
            drawManagers.add(SpringAppContext.getBean("MustWinningDrawManager", GiftDrawManager.class));
            drawManagers.add(SpringAppContext.getBean("FirstGiftDrawManager", GiftDrawManager.class));
            drawManagers.add(SpringAppContext.getBean("LowGiftDrawManager", GiftDrawManager.class));
            drawManagers.add(SpringAppContext.getBean("HighGiftDrawManager", GiftDrawManager.class));
        }
        return drawManagers;
    }

    /**
     * 组装处理结果
     *
     * @param result 捡海螺结果
     * @return
     */
    private List<DrawGiftVO> handleResult(Map<Integer, Integer> result) {
        List<GiftDTO> gifts = giftManager.listGift(null);
        Integer giftNum;
        DrawGiftVO giftVo;
        List<DrawGiftVO> list = Lists.newArrayList();
        for (GiftDTO giftDto : gifts) {
            if (giftDto == null || giftDto.getGiftId() == null) {
                continue;
            }
            giftNum = result.get(giftDto.getGiftId());
            if (giftNum == null || giftNum == 0) {
                continue;
            }

            giftVo = new DrawGiftVO();
            BeanUtils.copyProperties(giftDto, giftVo);
            giftVo.setGiftNum(giftNum);
            list.add(giftVo);
        }
        return list;
    }

    /**
     * 发送房间礼物
     *
     * @param roomDto
     * @param sendUser
     * @param recvUser
     * @param giftDTO
     * @param giftNum
     */
    public void sendGiftOneRoom(RoomDTO roomDto, UsersDTO sendUser, UsersDTO recvUser, GiftDTO giftDTO, Integer
            giftNum) {
        if (roomDto == null || sendUser == null || recvUser == null || giftDTO == null) return;

        Map<String, Object> data = new HashMap<>(1);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", sendUser.getUid());
        map.put("targetUid", recvUser.getUid());
        map.put("giftId", giftDTO.getGiftId());
        map.put("giftNum", giftNum);
        map.put("targetNick", recvUser.getNick());
        map.put("targetAvatar", recvUser.getAvatar());
        map.put("nick", sendUser.getAvatar());
        map.put("avatar", sendUser.getAvatar());
        map.put("personCount", null);
        map.put("roomIdList", null);
        map.put("roomUid", roomDto.getUid());
        map.put("userNo", sendUser.getErbanNo());
        map.put("userGiftPurseNum", null);
        map.put("useGiftPurseGold", null);
        map.put("giftSendTime", System.currentTimeMillis());
        map.put("experLevel", levelManager.getUserExperienceLevelSeq(sendUser.getUid()));
        map.put("defUser", sendUser.getDefUser());

        data.put("params", map);

        Attach attach = new Attach();
        attach.setFirst(DefMsgType.roomMessage);
        attach.setSecond(DefMsgType.roomMessage);
        attach.setData(data);

        JSONObject object = new JSONObject();
        object.put("custom", attach);
        List<Long> list = Lists.newArrayList();
        try {
            imroomManager.pushAllRoomMsg(object, list);
        } catch (Exception e) {
            log.error("发送房间全服礼物消息失败,异常信息:{}", e);
        }
    }

    /**
     * 发送全服礼物
     *
     * @param users   抽奖用户
     * @param roomId  抽奖房间
     * @param giftDTO 礼物
     * @param giftNum 礼物数量
     */
    private void sendGiftAllRoom(Long roomId, UsersDTO users, GiftDTO giftDTO, Integer giftNum) {
        Map<String, Object> data = new HashMap<>(1);
        Map<String, Object> map = new HashMap<>();
        map.put("giftName", "【全服】" + giftDTO.getGiftName());
        map.put("giftType", giftDTO.getGiftType());
        map.put("giftUrl", giftDTO.getPicUrl());
        map.put("count", giftNum);
        map.put("nick", users.getNick());
        map.put("avatar", users.getAvatar());
        map.put("uid", users.getUid());
        map.put("isFull", true);
        map.put("goldPrice", giftDTO.getGoldPrice());
        data.put("params", map);

        Attach attach = new Attach();
        attach.setFirst(DefMsgType.roomMessage);
        attach.setSecond(DefMsgType.roomMessage);
        attach.setData(data);

        JSONObject object = new JSONObject();
        object.put("custom", attach);
        List<Long> list = Lists.newArrayList();
        try {
            imroomManager.pushAllRoomMsg(object, list);
        } catch (Exception e) {
            log.error("发送房间全服礼物消息失败,异常信息:{}", e);
        }
    }

    private GiftVO toVo(GiftDTO giftDTO) {
        GiftVO giftVo = new GiftVO();
        BeanUtils.copyProperties(giftDTO, giftVo);
        giftVo.setHasLatest(giftDTO.getIsLatest());
        giftVo.setGiftUrl(giftDTO.getPicUrl());
        giftVo.setHasTimeLimit(giftDTO.getIsTimeLimit());
        return giftVo;
    }
}
