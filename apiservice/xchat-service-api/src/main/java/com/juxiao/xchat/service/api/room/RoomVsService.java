package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.room.bo.RoomVsParamBO;

/**
@author:tp
@date:2020年10月28日
*/
public interface RoomVsService {

	public WebServiceMessage initiatePK(RoomVsParamBO paramBO) throws Exception;
}
