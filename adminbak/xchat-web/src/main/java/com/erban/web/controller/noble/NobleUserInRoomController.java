package com.erban.web.controller.noble;

import com.erban.main.service.noble.NobleUserInRoomService;
import com.erban.main.vo.noble.NobleUserVo;
import com.erban.web.common.BaseController;
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

import java.util.List;

/**
 *  贵族进入房间列表
 *
 */
@Controller
@RequestMapping("/noble/room/user")
public class NobleUserInRoomController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(NobleUserInRoomController.class);

    @Autowired
    private NobleUserInRoomService nobleUserInRoomService;

    /**
     *  获取房间中的贵族列表
     *  @param roomUid
     *  @return
     */
    @RequestMapping( value = "/get", method = RequestMethod.GET)
    @ResponseBody
    @Authorization
    public BusiResult getNobleUserInRoomList(Long roomUid){
        if(roomUid == null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try{
            List<NobleUserVo> userList = nobleUserInRoomService.getNobleUserInRoomList(roomUid);
            return new BusiResult(BusiStatus.SUCCESS, userList);
        }catch(Exception e){
            logger.error("getNobleUserInRoomList error",e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }
}
