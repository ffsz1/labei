package com.juxiao.xchat.api.controller.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.room.RoomVsService;
import com.juxiao.xchat.service.api.room.bo.RoomVsParamBO;

/**
 * @title:房间PK
 * @author:tp
 * @date:2020年10月28日
 */
@RestController
@RequestMapping("/roomVs")
public class RoomVsController {
	@Autowired
	private RoomVsService roomVsService;

	/**
	  *  发起PK
	 * @param paramBO
	 * @return
	 */
	@RequestMapping(value = "/initiatePK", method = RequestMethod.POST)
	public WebServiceMessage initiatePK(RoomVsParamBO paramBO) throws Exception {
		return roomVsService.initiatePK(paramBO);
	}
}
