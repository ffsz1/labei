package com.erban.web.controller;

/**
 * Created by liuguofu on 2017/5/26.
 */

import com.erban.main.model.AuctionCur;
import com.erban.main.model.Room;
import com.erban.main.param.AuctionCurParam;
import com.erban.main.param.RoomParam;
import com.erban.main.service.AuctionCurService;
import com.erban.main.vo.AuctionCurVo;
import com.erban.main.vo.RoomVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auction")
public class AuctionCurController {

	private static final Logger logger = LoggerFactory.getLogger(AuctionCurController.class);
	@Autowired
	private AuctionCurService auctionCurService;

	/**
	 * 房间主人发起竞拍
	 * 
	 * @param auctionCurParam
	 * @param ticket
	 * @return
	 */
	@RequestMapping(value = "start", method = RequestMethod.POST)
	@ResponseBody
	@Authorization
	public BusiResult startAuction(AuctionCurParam auctionCurParam, String ticket) {
		if (auctionCurParam == null || auctionCurParam.getUid() == null || auctionCurParam.getAuctUid() == null
				|| auctionCurParam.getAuctMoney() == null || auctionCurParam.getAuctMoney() < 1) {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
		}
		BusiResult<AuctionCurVo> startAuctionResult = null;
		try {
			startAuctionResult = auctionCurService.saveStartAuctionCur(auctionCurParam);
		} catch (Exception e) {
			logger.error("startAuction error..uid=" + auctionCurParam.getUid(), e);
			return new BusiResult(BusiStatus.BUSIERROR);
		}
		return startAuctionResult;
	}

	@RequestMapping(value = "finish", method = RequestMethod.POST)
	@ResponseBody
	@Authorization
	public BusiResult finishAuction(String auctId, Long uid, String ticket) {
		if (auctId == null || uid == null) {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
		}
		BusiResult<AuctionCurVo> startAuctionResult = null;
		try {
			startAuctionResult = auctionCurService.finishAuction(uid);
		} catch (Exception e) {
			logger.error("finishAuction error..uid=" + uid, e);
			return new BusiResult(BusiStatus.BUSIERROR);
		}
		return startAuctionResult;
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	@ResponseBody
	public BusiResult getCurentAuction(Long uid) {
		if (uid == null) {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
		}
		BusiResult<AuctionCurVo> startAuctionResult = null;
		try {
			startAuctionResult = auctionCurService.getCurrentAuctionByUid(uid);
		} catch (Exception e) {
			logger.error("getCurrentAuctionByUid error..uid=" + uid, e);
			return new BusiResult(BusiStatus.BUSIERROR);
		}
		return startAuctionResult;
	}


}
