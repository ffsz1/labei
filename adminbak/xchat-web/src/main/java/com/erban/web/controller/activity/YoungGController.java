//package com.erban.web.controller.activity;
//
//import com.erban.main.service.activity.YoungGService;
//import com.xchat.common.annotation.Authorization;
//import com.xchat.common.result.BusiResult;
//import com.xchat.common.status.BusiStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * 养鸡活动
// */
//@Controller
//@RequestMapping("/activity/youngg")
//public class YoungGController {
//    @Autowired
//    private YoungGService youngGService;
//
//    /**
//     * 用户抽奖接口
//     *
//     * @param uid
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/draw", method = RequestMethod.POST)
//    public BusiResult draw(@RequestParam("uid") Long uid) {
//        return youngGService.draw(uid);
//    }
//
//    /**
//     * 获取用户抽奖次数
//     *
//     * @param uid
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/count", method = RequestMethod.GET)
//    public BusiResult<Integer> countDailyTask(@RequestParam("uid") Long uid) {
//        if (uid == null) {
//            return new BusiResult<>(BusiStatus.PARAMERROR);
//        }
//        int drawCount = youngGService.countDailyTask(uid);
//        return new BusiResult<>(BusiStatus.SUCCESS, drawCount);
//    }
//}
