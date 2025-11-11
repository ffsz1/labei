package com.juxiao.xchat.service.api.event.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.user.UserDrawManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.service.api.event.PublicChargeActivityService;
import com.juxiao.xchat.service.api.event.dto.NoviceFirstChargeCheckInDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 公用充值活动service实现
 * @author chris
 */
@Slf4j
@Service
public class PublicChargeActivityServiceImpl implements PublicChargeActivityService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    @Autowired
    private UserDrawManager userDrawManager;

    @Autowired
    private UserPurseManager userPurseManager;

    @Autowired
    private GiftCarManager giftCarManager;

    @Autowired
    private HeadwearManager headwearManager;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    /**
     * 新手首充签到大礼包
     * @param uid uid
     */
    @Override
    public void noviceFirstChargeCheckInActivity(Long uid,int amount,String chargeId) throws Exception{
        List<Map<String,Object>> maps = jdbcTemplate.queryForList("select t.uid from (select * from charge_record where uid = ? and charge_status = 2 and channel  <> 'exchange' and to_days(update_time) = to_days(now()) order by update_time desc limit 2) as t group by t.charge_record_id", uid);
        if(maps.size() == 1) {
            String cacheResult = redisManager.hget(RedisKey.novice_first_charge_checkin.getKey(), uid.toString());
            List<NoviceFirstChargeCheckInDTO> checkInDTOS = Lists.newArrayList();
            NoviceFirstChargeCheckInDTO checkInDTO = new NoviceFirstChargeCheckInDTO();
            if (StringUtils.isNotBlank(cacheResult)) {
                checkInDTOS = gson.fromJson(cacheResult, new TypeToken<List<NoviceFirstChargeCheckInDTO>>() {}.getType());
                checkInDTO.setNum(1);
                checkInDTO.setTime(DateUtils.dateToStr(new Date()));
                checkInDTO.setUid(uid);
                checkInDTOS.add(checkInDTO);
            }else{
                checkInDTO.setNum(1);
                checkInDTO.setTime(DateUtils.dateToStr(new Date()));
                checkInDTO.setUid(uid);
                checkInDTOS.add(checkInDTO);
            }
            redisManager.hset(RedisKey.novice_first_charge_checkin.getKey(), uid.toString(), gson.toJson(checkInDTOS));
            if(checkInDTOS.size() == 1){
                redisManager.hset(RedisKey.novice_first_charge_checkin_record.getKey("1"), uid.toString(),"2");
                Map<String,Object> dataMap = Maps.newHashMap();
                dataMap.put("amount",amount);
                dataMap.put("pingxxChargeId",chargeId);
                redisManager.hset(RedisKey.novice_first_charge_checkin_prod.getKey(),uid.toString(),gson.toJson(dataMap));
//                userDrawManager.saveFirstChargeDraw(uid,amount,chargeId);

            }
            if(checkInDTOS.size() == 2){
                int days = DateUtils.checkPeriodContinue(checkInDTOS.get(0).getTime(),checkInDTOS.get(1).getTime());
                if(days == 1) {
                    redisManager.hset(RedisKey.novice_first_charge_checkin_record.getKey("2"), uid.toString(), "2");
                    HeadwearDTO headwearDTO = headwearManager.getHeadwear(22);
                    headwearManager.saveUserHeadwear(uid, headwearDTO.getHeadwearId(), 7, 7, "恭喜您获得价值"+headwearDTO.getGoldPrice()+"金币头饰"+headwearDTO.getHeadwearName()+"*7天,快点去搭配吧！");
                }
            }
            if(checkInDTOS.size() == 3){
                int oldDays = DateUtils.checkPeriodContinue(checkInDTOS.get(0).getTime(),checkInDTOS.get(1).getTime());
                if(oldDays == 1) {
                    int days = DateUtils.checkPeriodContinue(checkInDTOS.get(1).getTime(), checkInDTOS.get(2).getTime());
                    if (days == 1) {
                        redisManager.hset(RedisKey.novice_first_charge_checkin_record.getKey("3"), uid.toString(), "2");
                        GiftCarDTO giftCarDTO = giftCarManager.getGiftCar(16);
                        giftCarManager.saveUserCar(uid, giftCarDTO.getCarId(), 7, 7, "恭喜您获得价值"+giftCarDTO.getGoldPrice()+"金币座驾"+giftCarDTO.getCarName()+"*7天,快点去搭配吧！");
                    }
                }
            }
        }
    }

    @Override
    public WebServiceMessage getNoviceFirstChargeRecord(Long uid)  {
        Map<String,Object> dataMap = Maps.newHashMap();
        String str = redisManager.hget(RedisKey.novice_first_charge_checkin_record.getKey("1"), uid.toString());
        //第一天 金币加大转盘
        if(StringUtils.isNotBlank(str)){
            dataMap.put("box1",str);
        }else{
            dataMap.put("box1", "0");
        }

        //第二天头饰
        str = redisManager.hget(RedisKey.novice_first_charge_checkin_record.getKey("2"), uid.toString());
        if(StringUtils.isNotBlank(str)) {
            dataMap.put("box2", str);
        }else{
            dataMap.put("box2", "0");
        }

        //第三天座驾
        str = redisManager.hget(RedisKey.novice_first_charge_checkin_record.getKey("3"), uid.toString());
        if(StringUtils.isNotBlank(str)) {
            dataMap.put("box3", str);
        }else{
            dataMap.put("box3", "0");
        }
        return WebServiceMessage.success(dataMap);
    }

    /**
     * 领取礼物项
     *
     * @param uid
     * @return
     */
    @Override
    public WebServiceMessage receiveActivityItem(Long uid,Integer itemId) {
        if(!itemId.equals(1) && !itemId.equals(2) && !itemId.equals(3)){
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        //金币
        if(itemId == 1){
            String str = redisManager.hget(RedisKey.novice_first_charge_checkin_record.getKey("1"), uid.toString());
            if(StringUtils.isNotBlank(str)){
                if("1".equals(str)){
                    try {
                        String cache = redisManager.hget(RedisKey.novice_first_charge_checkin_prod.getKey(),uid.toString());
                        Map<String,Object> stringObjectMap = gson.fromJson(cache, new TypeToken<Map<String,Object>>() {}.getType());
                        userPurseManager.updateAddGold(uid, 20L, false,true,"首充礼包", null, null);
                        Double result = Double.valueOf(stringObjectMap.get("amount").toString());
                        int amount = result.intValue() * 100;
//                        userDrawManager.saveFirstChargeDraw(uid,amount,stringObjectMap.get("pingxxChargeId").toString());
//                        asyncNetEaseTrigger.sendMsg(String.valueOf(uid),"恭喜你获得金币*20加额外大转盘抽奖机会1次");
                        redisManager.hset(RedisKey.novice_first_charge_checkin_record.getKey("1"), uid.toString(),"2");
                        return WebServiceMessage.success("领取成功!");
                    } catch (Exception e) {
                        redisManager.hset(RedisKey.novice_first_charge_checkin_record.getKey("1"), uid.toString(),"2");
                        log.error("receiveActivityItem  error while saveFirstChargeDraw,still continue save user...uid=" + uid, e.getMessage());
                    }
                }else{
                    return WebServiceMessage.failure("你已领取过了!");
                }
            }else{
                return WebServiceMessage.failure("未达到领取资格!");
            }
        }
        //头饰
        else if(itemId == 2){
            String str = redisManager.hget(RedisKey.novice_first_charge_checkin_record.getKey("2"), uid.toString());
            if(StringUtils.isNotBlank(str)){
                if("1".equals(str)){
                    try {
                        HeadwearDTO headwearDTO = headwearManager.getHeadwear(22);
                        headwearManager.saveUserHeadwear(uid, headwearDTO.getHeadwearId(), 7, 7, "恭喜您获得价值"+headwearDTO.getGoldPrice()+"金币头饰"+headwearDTO.getHeadwearName()+"*7天,快点去搭配吧！");
                        redisManager.hset(RedisKey.novice_first_charge_checkin_record.getKey("2"), uid.toString(),"2");
                        return WebServiceMessage.success("领取成功!");
                    } catch (Exception e) {
                        log.error("receiveActivityItem  error while saveUserHeadwear,still continue save user...uid=" + uid, e.getMessage());
                    }
                }else{
                    return WebServiceMessage.failure("你已领取过了!");
                }
            }else{
                return WebServiceMessage.failure("未达到领取资格!");
            }
        }
        //座驾
        else if(itemId == 3){
            String str = redisManager.hget(RedisKey.novice_first_charge_checkin_record.getKey("3"), uid.toString());
            if(StringUtils.isNotBlank(str)){
                if("1".equals(str)){
                    try {
                        GiftCarDTO giftCarDTO = giftCarManager.getGiftCar(16);
                        giftCarManager.saveUserCar(uid, giftCarDTO.getCarId(), 7, 7, "恭喜您获得价值"+giftCarDTO.getGoldPrice()+"金币座驾"+giftCarDTO.getCarName()+"*7天,快点去搭配吧！");
                        redisManager.hset(RedisKey.novice_first_charge_checkin_record.getKey("3"), uid.toString(),"2");
                        return WebServiceMessage.success("领取成功!");
                    } catch (Exception e) {
                        log.error("receiveActivityItem  error while saveUserCar,still continue save user...uid=" + uid, e.getMessage());
                    }
                }else{
                    return WebServiceMessage.failure("你已领取过了!");
                }
            }else{
                return WebServiceMessage.failure("未达到领取资格!");
            }
        }
        return null;
    }

}
