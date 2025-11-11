package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.service.api.user.AutoGenRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gen")
public class AutoGenRobotController {
    @Autowired
    private AutoGenRobotService autoGenRobotService;

//    @RequestMapping(value = "acc", method = RequestMethod.GET)
//    public void batchGenRobAccount() throws Exception {
//        autoGenRobotService.batchGenRobAccount();
//    }

}
