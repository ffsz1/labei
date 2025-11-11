package com.erban.web.controller.room;

import com.erban.main.service.room.RoomRewardService;
import com.erban.main.vo.RoomRewardVo;
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

/**
 * Created by liuguofu on 2017/5/26.
 */
@Controller
@RequestMapping("/reward")
public class RoomRewardController {
    private static final Logger logger = LoggerFactory.getLogger(RoomRewardController.class);
    @Autowired
    private RoomRewardService roomRewardService;

    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult saveRoomReward(Long uid,Long rewardMoney,int servDura,String ticket){

        if(uid==null||rewardMoney==null||rewardMoney<1){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        BusiResult<RoomRewardVo> result=new BusiResult<RoomRewardVo>(BusiStatus.SUCCESS);
        try {
            result= roomRewardService.saveRoomReward(uid,rewardMoney,servDura);
        } catch (Exception e) {
            logger.error("saveRoomReward error..uid="+uid,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return result;
    }


}
