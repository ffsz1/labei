package com.erban.admin.web.controller.room;

import com.erban.main.model.Users;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/10/31.
 */
@Controller
@RequestMapping("/roomman")
public class RoomManageController {
    private static final Logger logger = LoggerFactory.getLogger(RoomManageController.class);
    @Autowired
    private RoomService roomService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    @RequestMapping(value = "close",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult close(Long erbanNo){
        if(erbanNo==null||erbanNo<1L){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        Users users=usersService.getUsresByErbanNo(erbanNo);

        try {
            roomService.closeRoom(users.getUid());
        } catch (Exception e) {
            logger.error("close Exception erbanNo="+erbanNo,e);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }
    @RequestMapping(value = "block",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult block(Long erbanNo){
        if(erbanNo==null||erbanNo<1L){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        Users users=usersService.getUsresByErbanNo(erbanNo);

        try {
            erBanNetEaseService.block(users.getUid().toString(),"true");
        } catch (Exception e) {
            logger.error("close Exception erbanNo="+erbanNo,e);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    @RequestMapping(value = "unblock",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult unblock(Long erbanNo){
        if(erbanNo==null||erbanNo<1L){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        Users users=usersService.getUsresByErbanNo(erbanNo);

        try {
            erBanNetEaseService.unblock(users.getUid().toString());
        } catch (Exception e) {
            logger.error("close Exception erbanNo="+erbanNo,e);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    @RequestMapping(value = "cblock",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult cblock(Long erbanNo){
        if(erbanNo==null||erbanNo<1L){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        Users users=usersService.getUsresByErbanNo(erbanNo);

        try {
            roomService.closeRoom(users.getUid());
            erBanNetEaseService.block(users.getUid().toString(),"true");
        } catch (Exception e) {
            logger.error("close Exception erbanNo="+erbanNo,e);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }





}
