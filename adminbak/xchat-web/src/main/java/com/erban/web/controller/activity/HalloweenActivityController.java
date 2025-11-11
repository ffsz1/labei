package com.erban.web.controller.activity;

import com.erban.main.service.activity.HalloweenActivityService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/10/28.
 */
@Controller
@RequestMapping("/halloween")
public class HalloweenActivityController {

    @Autowired
    private HalloweenActivityService halloweenActivityService;

    /**
     * 万圣节活动排行榜
     *
     * @param type 1是查富豪排行榜，2是查明星排行榜，3是房间榜
     * @param uid
     * @return
     */
    @RequestMapping(value = "rank",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult queryHalloweenActRank(int type,Long uid){
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult=halloweenActivityService.queryHalloweenActRank(uid,type);

        return busiResult;
    }

    @RequestMapping(value = "refresh",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult doHalloweenActRank(){
        halloweenActivityService.doHalloweenActRank();
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        return busiResult;
    }
}
