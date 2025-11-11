package com.erban.web.controller.activity;


import com.erban.main.model.GiftSendRecordVo2;
import com.erban.main.service.activity.FeedbackActivityService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.erban.web.common.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by yuanyi on 2018/1/8.
 */

@Controller
@RequestMapping("/feedbackActivity")
public class FeedbackActivityController extends BaseController{
    @Autowired
    private FeedbackActivityService feedbackActivityService;

    @Autowired
    private UsersService usersService;

//    @RequestMapping("/testUpdate")
//    @ResponseBody
//    public void testToGetList(){
//        try{
//            feedbackActivityService.updateUserGiftBonusPerPay();
//        } catch (Exception e){
//
//        }
//    }
//
//    @RequestMapping("/testCal")
//    @ResponseBody
//    public void testToCal(){
//        try{
//            feedbackActivityService.computeDiamond();
//        }catch (Exception e){
//
//        }
//    }

    /**
     * 获取用户每天的礼物分成（如缓存无，则查数据库）
     * @param uid
     * @return
     */
    @RequestMapping("/getMessage")
    @ResponseBody
    public BusiResult getMessage(Long uid){
        return feedbackActivityService.getData(uid);
    }

    /**
     * 获取用户信息(包括用户对应的贵族信息)（如缓存无，则查数据库）
     * @param uid
     * @return
     */
    @RequestMapping("/getPerson")
    @ResponseBody
    public BusiResult getPerson(Long uid){
        BusiResult busiResult = null;
        UserVo user = usersService.getUserVoByUid(uid);
        if(user == null){
            busiResult = new BusiResult(BusiStatus.USERNOTEXISTS);
            busiResult.setMessage("用户不存在");
        }else{
            busiResult = new BusiResult(BusiStatus.SUCCESS);
            busiResult.setData(user);
        }
        return busiResult;
    }
}
