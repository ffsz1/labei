package com.juxiao.xchat.service.api.sysconf;

import java.util.Map;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;


/**
 * 国庆活动
@author:tp
@date:2020年9月22日
*/
public interface NationalDayService {

	/**
	 * 国庆活动页列表
	 * @param uid
	 * @param os
	 * @param appid
	 * @return
	 */
	public Map<String, Object> loadNationalDayActivityData(Long uid,String os,String appid);
	
	/**
	 * 国庆活动页分享操作
	 * @param uid
	 * @param shareType
	 */
	public void NationalDayPageShare(Long uid,String shareType) throws WebServiceException ;
	
	/**
	 * 国庆活动页关注操作
	 * @param uid
	 * @param likedUid
	 */
	public void NationalDayFansLike(Long uid,Long likedUid) throws WebServiceException ;
	
	/**
	 * 国庆活动页取消关注操作
	 * @param uid
	 * @param likedUid
	 */
	public void NationalDayCancelFansLike(Long uid,Long likedUid);
	
	/**
	 * 国庆活动页充值操作
	 * @param uid
	 * @param before 充值前
	 * @param after 充值后
	 */
	public void NationalDayRecharge(Long uid,Integer before,Integer after);
	
	/**
	 * 国庆活动页送礼操作
	 * @param uid
	 * @param roomUid
	 * @param giftNum
	 */
	public void NationalDayGiveGifts(Long uid,Long roomUid,int giftNum)  throws WebServiceException ;
	
	/**
	 * 国庆活动页房间停留积分处理
	 * @param uid
	 * @return
	 */
	public Integer NationalDayRoomStay(Long uid)  throws WebServiceException ;
	
	/**
	 * 国庆活动页积分兑换
	 * @param uid
	 * @param optionId
	 * @return
	 */
	public WebServiceMessage exchange(Long uid,Integer optionId) throws WebServiceException;
	
}
