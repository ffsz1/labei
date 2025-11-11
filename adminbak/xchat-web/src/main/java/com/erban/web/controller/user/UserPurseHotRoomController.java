package com.erban.web.controller.user;

import com.erban.main.service.user.UserPurseHotRoomService;
import com.erban.web.common.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping("/purseHotRoom")
public class UserPurseHotRoomController extends BaseController {
    @Autowired
    private UserPurseHotRoomService userPurseHotRoomService;

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public BusiResult list(Long uid){
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            String str = userPurseHotRoomService.getStrList(uid.toString());
            return new BusiResult(BusiStatus.SUCCESS, (str==null?new ArrayList<>():userPurseHotRoomService.getList(str)));
        }catch (Exception e){
            logger.error("purseHotRoom/list error: ", e);
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
    }

    @ResponseBody
    @RequestMapping(value = "purse",method = RequestMethod.GET)
    public BusiResult purse(Long uid, Long erbanNo, String date, String hour){
        if(uid==null||erbanNo==null||date==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return userPurseHotRoomService.purse(uid, erbanNo, date, hour);
        }catch (Exception e){
            logger.error("purseHotRoom/purse error: ", e);
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
    }

}
