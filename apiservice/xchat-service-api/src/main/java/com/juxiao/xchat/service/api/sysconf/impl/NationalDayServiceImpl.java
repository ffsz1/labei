package com.juxiao.xchat.service.api.sysconf.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.Utils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.item.enumeration.CarGetType;
import com.juxiao.xchat.dao.item.enumeration.HeadwearGetType;
import com.juxiao.xchat.dao.sysconf.dto.IntegralDetailsDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.NationalDayConf;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.item.constant.GiftType;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.service.api.sysconf.NationalDayService;

/**
 * @author:tp
 * @date:2020年9月22日
 */
@Service
public class NationalDayServiceImpl implements NationalDayService {

	private static final Logger logger = LoggerFactory.getLogger(NationalDayServiceImpl.class);
	@Autowired
	private GiftManager giftManager;
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Gson gson;
	@Autowired
	private NationalDayConf nationalDayConf;
	@Autowired
	private UsersManager usersManager;
	@Autowired
	private HeadwearManager headwearManager;
	@Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;
	@Autowired
	private UserPurseManager userPurseManager;
	
	@Autowired
    private GiftCarManager giftCarManager;

	/**
	 * 国庆活动页列表
	 * 
	 * @param uid
	 * @param os
	 * @param appid
	 * @return
	 */
	@Override
	public Map<String, Object> loadNationalDayActivityData(Long uid, String os, String appid) {
		// 获取所有普通礼物
		List<GiftDTO> gifts = giftManager.listGift(GiftType.NORMAL);
		// 根据礼物状态和限时, 过滤礼物
		gifts = gifts.stream().filter((GiftDTO gift) -> gift.getGiftStatus() == 1 && gift.getIsTimeLimit() == true)
				.collect(Collectors.toList());
		gifts = gifts == null ? Lists.newArrayList() : gifts;
		Map<String, Object> nationalDayData = new HashMap<String, Object>();
		nationalDayData.put("gifs", gifts);

		String taskDay = DateTimeUtils.getTodayStr();
		// 分享
		String task1 = redisManager.hget(RedisKey.daily_task.name(),uid + "_" + taskDay + "_" + "share");
		Integer task1ProgressValue = Utils.formatInt(task1);
		nationalDayData.put("task1", task1ProgressValue.intValue() >= 3 ? 3:task1ProgressValue);
		nationalDayData.put("task1Status", task1ProgressValue.intValue() >= 3 ? true : false);
		// 关注
		String task2 = redisManager.hget(RedisKey.daily_task.name(),uid + "_" + taskDay + "_" + "fans_like");
		Integer task2ProgressValue = Utils.formatInt(task2);
		nationalDayData.put("task2", task2ProgressValue.intValue() >= 3 ? 3:task2ProgressValue);
		nationalDayData.put("task2Status", task2ProgressValue.intValue() >= 3 ? true : false);
		// 充值
		String task3 = redisManager.hget(RedisKey.daily_task.name(),uid + "_" + taskDay + "_" + "recharge");
		Integer task3ProgressValue = Utils.formatInt(task3);
		nationalDayData.put("task3", task3ProgressValue.intValue() >= 3 ? 3:task3ProgressValue);
		nationalDayData.put("task3Status", task3ProgressValue.intValue() >= 3 ? true : false);
		// 送礼
		String task4 = redisManager.hget(RedisKey.daily_task.name(),uid + "_" + taskDay + "_" + "give_gifts");
		Integer task4ProgressValue = Utils.formatInt(task4);
		nationalDayData.put("task4", task4ProgressValue.intValue() >= 3 ?3:task4ProgressValue);
		nationalDayData.put("task4Status", task4ProgressValue.intValue() >= 3 ? true : false);
		// 房间停留
		Integer count = NationalDayRoomStay(uid);
		nationalDayData.put("task5", count.intValue() > 0 ? 10 : 0);
		nationalDayData.put("task5Status", count.intValue() > 0 ? true : false);
		
		String detailsStr = redisManager.hget(RedisKey.integral_details.name(),uid.toString());
		List<IntegralDetailsDTO> detailsList = new ArrayList<IntegralDetailsDTO>();
		Integer integralTotal = 0;
		if (StringUtils.isNotBlank(detailsStr)) {
			detailsList = gson.fromJson(detailsStr, new TypeToken<List<IntegralDetailsDTO>>() {
			}.getType());
			for (IntegralDetailsDTO details : detailsList) {
				integralTotal = integralTotal + details.getIntegralValue();
			}
		}
		nationalDayData.put("integralDetails", detailsList);// 积分明细
		nationalDayData.put("integralTotal", integralTotal);// 总积分
		redisManager.hset(RedisKey.integral_total.name(),uid.toString(), integralTotal.toString());// 总积分
		return nationalDayData;
	}

	/**
	 * 国庆活动页分享操作
	 * 
	 * @param uid
	 * @param shareType
	 */
	@Override
	public void NationalDayPageShare(Long uid, String shareType)  throws WebServiceException {

		if (uid == null) {
			throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
		}

		UsersDTO usersDto = usersManager.getUser(uid);
		if (usersDto == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}
		String taskDay = DateTimeUtils.getTodayStr();
		String progressValStr = redisManager.hget(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "share");// 当天分享次数

		Integer progressValue = Utils.formatInt(progressValStr) + 1;
		redisManager.hset(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "share", progressValue.toString());
		
		logger.info("国庆活动期间,用户uid:{} 分享活动页至朋友圈/群", uid);
		if (progressValue.intValue() <= 3) {
			String detailsStr = redisManager.hget(RedisKey.integral_details.name(), uid.toString());
			List<IntegralDetailsDTO> detailsList = new ArrayList<IntegralDetailsDTO>();
			if (StringUtils.isNotBlank(detailsStr)) {
				detailsList = gson.fromJson(detailsStr, new TypeToken<List<IntegralDetailsDTO>>() {
				}.getType());
			}
			IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
			detailsDto.setUid(uid);
			detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
			detailsDto.setDetailType("share");
			detailsDto.setIntegralValue(30);
			detailsDto.setRemark("分享朋友圈/群 +30");

			detailsList.add(detailsDto);
			redisManager.hset(RedisKey.integral_details.name(), uid.toString(), gson.toJson(detailsList));
			logger.info("国庆活动期间,用户uid:{}分享活动页至朋友圈/群一次获得:{} 积分", uid, 30);
			Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
			integralTotal = integralTotal+detailsDto.getIntegralValue();
			redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());// 总积分
		}

	}

	/**
	 * 国庆活动页关注操作
	 * 
	 * @param uid
	 * @param likedUid
	 */
	@Override
	public void NationalDayFansLike(Long uid, Long likedUid)  throws WebServiceException {
		if (uid == null) {
			throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
		}

		UsersDTO usersDto = usersManager.getUser(uid);
		if (usersDto == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}
		String taskDay = DateTimeUtils.getTodayStr();
		String progressValStr = redisManager.hget(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "fans_like");// 当天关注次数
		List<String> likedUids = new ArrayList<String>();
		String fanslikeStr = redisManager.hget(RedisKey.daily_fans_like.name(), uid.toString());
		if (StringUtils.isNotBlank(fanslikeStr)) {
			likedUids = gson.fromJson(fanslikeStr, new TypeToken<List<String>>() {
			}.getType());
		}
		Integer progressValue = Utils.formatInt(progressValStr);
		if (!likedUids.contains(likedUid.toString())) {
			likedUids.add(likedUid.toString());
			progressValue =  progressValue+ 1;
			redisManager.hset(RedisKey.daily_fans_like.name(), uid.toString(), gson.toJson(likedUids));
			redisManager.hset(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "fans_like", progressValue.toString());

			if (progressValue.intValue() <= 3) {
				String detailsStr = redisManager.hget(RedisKey.integral_details.name(), uid.toString());
				List<IntegralDetailsDTO> detailsList = new ArrayList<IntegralDetailsDTO>();
				if (StringUtils.isNotBlank(detailsStr)) {
					detailsList = gson.fromJson(detailsStr, new TypeToken<List<IntegralDetailsDTO>>() {
					}.getType());
				}
				IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
				detailsDto.setUid(uid);
				detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
				detailsDto.setDetailType("fans_like");
				detailsDto.setIntegralValue(10);
				detailsDto.setRemark("关注1个用户 +10");

				detailsList.add(detailsDto);
				redisManager.hset(RedisKey.integral_details.name(), uid.toString(), gson.toJson(detailsList));
				logger.info("国庆活动期间,日期：>{} 用户uid:{} 关注了1个用户 +10 积分 ", taskDay, uid);
				Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
				integralTotal = integralTotal+detailsDto.getIntegralValue();
				redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());// 总积分
			}
		} else {
			logger.info("日期：>{} 用户uid:{}曾经关注过用户likedUid:{}", taskDay, uid, likedUid);
		}
	}

	/**
	 * 国庆活动页取消关注操作
	 * 
	 * @param uid
	 * @param likedUid
	 */
	@Override
	public void NationalDayCancelFansLike(Long uid, Long likedUid) {
		List<String> likedUids = new ArrayList<String>();
		String cancelFansLikeListStr=redisManager.hget(RedisKey.cancel_fans_like.name(), uid.toString());
		if(StringUtils.isNotBlank(cancelFansLikeListStr)) {
			likedUids =gson.fromJson(cancelFansLikeListStr, new TypeToken<List<String>>() {
			}.getType());
		}
		if(!likedUids.contains(likedUid.toString())) {
			likedUids.add(likedUid.toString());
		}
		redisManager.hset(RedisKey.cancel_fans_like.name(), uid.toString(), gson.toJson(likedUids));
	}

	/**
	 * 国庆活动页充值操作
	 * 
	 * @param uid
	 * @param before 充值前
	 * @param after  充值后
	 */
	@Override
	public void NationalDayRecharge(Long uid, Integer before, Integer after) {
		String taskDay = DateTimeUtils.getTodayStr();
		String progressValStr = redisManager.hget(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "recharge");// 当天充值次数

		Integer progressValue = Utils.formatInt(progressValStr) + 1;
		redisManager.hset(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "recharge", progressValue.toString());
		logger.info("国庆活动期间,日期：>{},用户uid:{} 充值前:>{},充值后:>{}", taskDay, uid, before, after);
		if (progressValue.intValue() <= 3) {
			String detailsStr = redisManager.hget(RedisKey.integral_details.name(), uid.toString());
			List<IntegralDetailsDTO> detailsList = new ArrayList<IntegralDetailsDTO>();
			if (StringUtils.isNotBlank(detailsStr)) {
				detailsList = gson.fromJson(detailsStr, new TypeToken<List<IntegralDetailsDTO>>() {
				}.getType());
			}
			IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
			detailsDto.setUid(uid);
			detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
			detailsDto.setDetailType("recharge");
			detailsDto.setIntegralValue(30);
			detailsDto.setRemark("充值1次 +30");

			detailsList.add(detailsDto);
			redisManager.hset(RedisKey.integral_details.name(), uid.toString(), gson.toJson(detailsList));
			logger.info("国庆活动期间,用户uid:{}充值1次获得:{} 积分", uid, 30);
			Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
			integralTotal = integralTotal+detailsDto.getIntegralValue();
			redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());// 总积分
		}

	}

	/**
	 * 国庆活动页送礼操作
	 * 
	 * @param uid
	 * @param roomUid
	 * @param giftNum
	 */
	@Override
	public void NationalDayGiveGifts(Long uid, Long roomUid, int giftNum)  throws WebServiceException {
		if (uid == null) {
			throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
		}

		UsersDTO usersDto = usersManager.getUser(uid);
		if (usersDto == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}
		String taskDay = DateTimeUtils.getTodayStr();
		String detailsStr = redisManager.hget(RedisKey.integral_details.name(), uid.toString());
		List<IntegralDetailsDTO> detailsList = new ArrayList<IntegralDetailsDTO>();
		if (StringUtils.isNotBlank(detailsStr)) {
			detailsList = gson.fromJson(detailsStr, new TypeToken<List<IntegralDetailsDTO>>() {
			}.getType());
		}

		logger.info("国庆活动期间,日期：>{},用户uid:{} 一次性送了用户roomUid:>{}, {}个礼物", taskDay, uid, roomUid, giftNum);
		String progressValStr = redisManager.hget(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "give_gifts");
		Integer progressValue = Utils.formatInt(progressValStr);// 当天送礼累积数
		if (StringUtils.isBlank(progressValStr)) {// 当天首次送礼
			IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
			detailsDto.setUid(uid);
			detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
			detailsDto.setDetailType("give_gifts");
			if (giftNum >= 3) {
				detailsDto.setIntegralValue(90);
				detailsDto.setRemark("送出了3个付费礼物  +90");
				
			} else if (giftNum == 2) {
				detailsDto.setIntegralValue(60);
				detailsDto.setRemark("送出了2个付费礼物  +60");
			} else {
				detailsDto.setIntegralValue(30);
				detailsDto.setRemark("送出了1个付费礼物  +30");
			}
			detailsList.add(detailsDto);
			Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
			integralTotal = integralTotal+detailsDto.getIntegralValue();
			redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());// 总积分
			progressValue = progressValue + giftNum;
			
		} else {
			if (progressValue.intValue() < 3) {
				if (progressValue.intValue()==1&&giftNum >= 2) {// 满足三个付费礼物(首次送了一个，第二次送了两个或两个以上)
					IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
					detailsDto.setUid(uid);
					detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
					detailsDto.setDetailType("give_gifts");
					detailsDto.setIntegralValue(60);
					detailsDto.setRemark("送出了2个付费礼物  +60");
					detailsList.add(detailsDto);
					Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
					integralTotal = integralTotal+detailsDto.getIntegralValue();
					redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());// 总积分
				} else  {//首次送了两个或者 首次和本次 各送了一个
					IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
					detailsDto.setUid(uid);
					detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
					detailsDto.setDetailType("give_gifts");
					detailsDto.setIntegralValue(30);
					detailsDto.setRemark("送出了1个付费礼物  +30");
					detailsList.add(detailsDto);
					Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
					integralTotal = integralTotal+detailsDto.getIntegralValue();
					redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());// 总积分
				} 
			}
			progressValue = progressValue + giftNum;
		}
		redisManager.hset(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "give_gifts", progressValue.toString());
		redisManager.hset(RedisKey.integral_details.name(), uid.toString(), gson.toJson(detailsList));
	}

	/**
	 * 国庆活动页房间停留积分处理
	 * 
	 * @param uid
	 * @return
	 */
	@Override
	public Integer NationalDayRoomStay(Long uid) {
		String taskDay = DateTimeUtils.getTodayStr();
		Integer maxNum = maxMinuteRoom(uid, taskDay);//取停留最长时间
		String countStr = redisManager.hget(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "room_stay_count");
		Integer count = Utils.formatInt(countStr);

		if (maxNum.intValue() >= 10) {
			if (StringUtils.isBlank(countStr)) {//当天任务完成还没有累积积分
				String detailsStr = redisManager.hget(RedisKey.integral_details.name(), uid.toString());
				List<IntegralDetailsDTO> detailsList = new ArrayList<IntegralDetailsDTO>();
				if (StringUtils.isNotBlank(detailsStr)) {
					detailsList = gson.fromJson(detailsStr, new TypeToken<List<IntegralDetailsDTO>>() {
					}.getType());
				}
				IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
				detailsDto.setUid(uid);
				detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
				detailsDto.setDetailType("room_stay");
				detailsDto.setIntegralValue(10);
				detailsDto.setRemark("在单个房间停留达10分钟   +10");
				detailsList.add(detailsDto);
				count++;
				redisManager.hset(RedisKey.integral_details.name(), uid.toString(), gson.toJson(detailsList));
				redisManager.hset(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "room_stay_count", count.toString());
				Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
				integralTotal = integralTotal+detailsDto.getIntegralValue();
				redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());// 总积分
			}
		}
		return count;
	}

	/**
	 * 获取用户某天停留最长时长（分钟）
	 * 
	 * @param uid
	 * @param taskDay
	 * @return
	 */
	public Integer maxMinuteRoom(Long uid, String taskDay) {
		String task5 = redisManager.hget(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "room_stay");
		if (StringUtils.isNotBlank(task5)) {
			List<Map<String, Object>> maplist = gson.fromJson(task5, new TypeToken<List<Map<String, Object>>>() {
			}.getType());
			List<Integer> minutelist = new ArrayList<Integer>();
			minutelist.add(0);
			for (Map<String, Object> map : maplist) {
				Long oldUid= Long.parseLong(map.get("times").toString());
				minutelist.add(oldUid.intValue());
			}
			return Collections.max(minutelist);
		} else {
			return 0;
		}
	}

	/**
	 * 国庆活动页积分兑换
	 * @param uid
	 * @param optionId
	 * @return
	 */
	@Override
	public WebServiceMessage exchange(Long uid, Integer optionId) throws WebServiceException {
		Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
		if (optionId.intValue() == 1) {// 限时头饰
			if (integralTotal.intValue() < 100) {
				throw new WebServiceException(WebServiceCode.LACK_OF_INTEGRAL);
			}
			exchangeHeadwear(uid, nationalDayConf.getHeadwearLimitTimeId(), 100);
			return WebServiceMessage.success("兑换限时头饰成功");
		} else if (optionId.intValue() == 2) {// 限时座驾
			if (integralTotal.intValue() < 200) {
				throw new WebServiceException(WebServiceCode.LACK_OF_INTEGRAL);
			}
			exchangeGiftCar(uid, nationalDayConf.getGifCarLimitTimeId(), 200);
			return WebServiceMessage.success("兑换限时座驾成功");
		} else if (optionId.intValue() == 3) {// 50金币
			if (integralTotal.intValue() < 500) {
				throw new WebServiceException(WebServiceCode.LACK_OF_INTEGRAL);
			}
			Integer exchangeGlodCount=Utils.formatInt(redisManager.hget(RedisKey.exchange_glodcount.name(), uid.toString()));
			if(exchangeGlodCount.intValue()>=1) {//每个用户只能兑换1次
				throw new WebServiceException(WebServiceCode.REWARD_FINISHED);
			}
			Long goldAmount=Long.parseLong(nationalDayConf.getGold().toString());
			exchangeGlod(uid, goldAmount, 500);
			exchangeGlodCount++;
			redisManager.hset(RedisKey.exchange_glodcount.name(), uid.toString(), exchangeGlodCount.toString());
			return WebServiceMessage.success("兑换金币成功");
		} else if (optionId.intValue() == 4) {// 商城头饰
			if (integralTotal.intValue() < 80) {
				throw new WebServiceException(WebServiceCode.LACK_OF_INTEGRAL);
			}
			exchangeHeadwear(uid, nationalDayConf.getMallHeadwearId(), 80);
			return WebServiceMessage.success("兑换商城头饰成功");
		} else if (optionId.intValue() == 5) {// 商城座驾
			if (integralTotal.intValue() < 120) {
				throw new WebServiceException(WebServiceCode.LACK_OF_INTEGRAL);
			}
			exchangeGiftCar(uid, nationalDayConf.getMallGifCarId(), 120);
			return WebServiceMessage.success("兑换商城座驾成功");
		} else if (optionId.intValue() == 6) {// 30天靓号
			return WebServiceMessage.failure(WebServiceCode.REWARD_FINISHED);
//			if (integralTotal.intValue() < 2000) {
//				throw new WebServiceException(WebServiceCode.LACK_OF_INTEGRAL);
//			}
//			Integer exchangeErbanoCount=Utils.formatInt(redisManager.hget(RedisKey.exchange_erbano_count.name(), uid.toString()));
//			if(exchangeErbanoCount.intValue()>=2) {//每个用户只能兑换2个靓号
//				throw new WebServiceException(WebServiceCode.REWARD_FINISHED);
//			}
//			exchangeErbano(uid, 2000);
//			return WebServiceMessage.success("兑换靓号成功");
		}else {
			return WebServiceMessage.failure("积分所要兑换的商品不存在");
		}
	}

	/**
	 * 积分兑换核销
	 * 
	 * @param uid
	 * @param remark
	 * @param integral
	 */
	public void subtractIntegral(Long uid, String remark, Integer integral) {

		String detailsStr = redisManager.hget(RedisKey.integral_details.name(), uid.toString());// 积分明细
		List<IntegralDetailsDTO> detailsList = new ArrayList<IntegralDetailsDTO>();
		if (StringUtils.isNotBlank(detailsStr)) {
			detailsList = gson.fromJson(detailsStr, new TypeToken<List<IntegralDetailsDTO>>() {
			}.getType());
		}
		IntegralDetailsDTO detailsDto = new IntegralDetailsDTO();
		detailsDto.setUid(uid);
		detailsDto.setDetailDt(DateTimeUtils.convertDate(new Date()));
		detailsDto.setDetailType("exchange");
		detailsDto.setIntegralValue(-integral);
		detailsDto.setRemark(remark);
		detailsList.add(detailsDto);
		redisManager.hset(RedisKey.integral_details.name(), uid.toString(), gson.toJson(detailsList));
		Integer integralTotal = Utils.formatInt(redisManager.hget(RedisKey.integral_total.name(), uid.toString()));// 总积分
		integralTotal = integralTotal - integral;
		redisManager.hset(RedisKey.integral_total.name(), uid.toString(), integralTotal.toString());;// 总积分
		logger.info("用户uid:>{}"+remark+",剩余积分：{}", uid,integralTotal);
	}

	/**
	 * 兑换头饰
	 * 
	 * @param uid
	 * @param headwearId
	 * @param integral
	 * @throws WebServiceException
	 */
	public void exchangeHeadwear(Long uid, Integer headwearId, Integer integral) throws WebServiceException {
		if (uid == null || headwearId == null) {
			throw new WebServiceException(WebServiceCode.PARAM_ERROR);
		}
		UsersDTO users = usersManager.getUser(uid);
		if (users == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}
		HeadwearDTO headwear = headwearManager.getHeadwear(headwearId);
		if (headwear == null) {
			throw new WebServiceException(WebServiceCode.NOT_HAVING_LIST);
		}

		headwearManager.saveUserHeadwear(uid, headwearId, headwear.getEffectiveTime().intValue(), HeadwearGetType.integral_exchange.getValue(),
				"您使用" + integral + "积分兑换了【" + headwear.getHeadwearName() + "】头饰已发放，请查收！");
		subtractIntegral(uid, "兑换了【" + headwear.getHeadwearName() + "】头饰  -" + integral, integral);
	}

	/**
	 *  兑换座驾
	 * @param uid
	 * @param carId
	 * @param integral
	 * @throws WebServiceException
	 */
	public void exchangeGiftCar(Long uid, Integer carId, Integer integral) throws WebServiceException {
		if (uid == null || carId == null) {
			throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
		}

		UsersDTO usersDto = usersManager.getUser(uid);
		if (usersDto == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}

		GiftCarDTO carDto = giftCarManager.getGiftCar(carId);
		if (carDto == null) {
			throw new WebServiceException(WebServiceCode.GIFT_CAR_NOT_EXISTS);
		}
		
		giftCarManager.saveUserCar(uid, carId, carDto.getEffectiveTime().intValue(), CarGetType.integral_exchange.getValue(),
				"您使用" + integral + "积分兑换了【" + carDto.getCarName() + "】座驾已发放，请查收！");
		subtractIntegral(uid, "兑换了【" + carDto.getCarName() + "】座驾  -" + integral, integral);
	}
	
	/**
	  * 兑换金币
	 * @param uid
	 * @param goldAmount
	 * @param integral
	 * @throws WebServiceException
	 */
	public void exchangeGlod(Long uid, Long goldAmount, Integer integral) throws WebServiceException {
		if (uid == null) {
			throw new WebServiceException(WebServiceCode.PARAM_ERROR);
		}
		UsersDTO users = usersManager.getUser(uid);
		if (users == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}
		userPurseManager.updateAddGold(uid,goldAmount , false, false, "国庆活动使用" + integral + "积分兑换"+goldAmount+"金币", null, null);
		asyncNetEaseTrigger.sendMsg(String.valueOf(uid),"您使用" + integral + "积分兑换了" + goldAmount + " 金币已发放，请查收！");
		subtractIntegral(uid, "兑换了【" + goldAmount + "】金币  -" + integral, integral);
	}
	
	
	/**
	 *  靓号兑换
	 * @param uid
	 * @param integral
	 * @throws WebServiceException
	 */
	public void exchangeErbano(Long uid, Integer integral) throws WebServiceException {
		if (uid == null) {
			throw new WebServiceException(WebServiceCode.PARAM_ERROR);
		}
		UsersDTO users = usersManager.getUser(uid);
		if (users == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}
		
		String rcStr = redisManager.get(RedisKey.daily_user_erbano_exchange_rc.getKey());
		Long erbanNo=users.getErbanNo();
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(rcStr)) {
			maplist = gson.fromJson(rcStr, new TypeToken<List<Map<String, Object>>>() {
			}.getType());
		}
		
		Map<String,Object> userExchangeRC=new HashMap<String,Object>();
		userExchangeRC.put("uid", uid.toString());
		userExchangeRC.put("erbanNo", erbanNo.toString());
		userExchangeRC.put("detailDt", DateTimeUtils.convertDate(new Date()));
		maplist.add(userExchangeRC);
		redisManager.set(RedisKey.daily_user_erbano_exchange_rc.getKey(),gson.toJson(maplist));
		subtractIntegral(uid, "兑换了ABABABA样式靓号   -" + integral, integral);
		asyncNetEaseTrigger.sendMsg(String.valueOf(uid),"您已使用2000积分兑换ABABABA样式靓号30天，请联系官方活动（haijiaoxingqiu3）进行领取，靓号奖励将在10月9日统一发放。");
		Integer exchangeErbanoCount=Utils.formatInt(redisManager.hget(RedisKey.exchange_erbano_count.name(), uid.toString()));
		exchangeErbanoCount++;
		redisManager.hset(RedisKey.exchange_erbano_count.name(),uid.toString(),exchangeErbanoCount.toString());
	}


}
