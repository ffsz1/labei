package com.erban.web.controller.record;

import com.erban.main.service.user.ExpressWallService;
import com.xchat.common.result.BusiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 表白墙
 */
@Controller
@RequestMapping("expressWall")
public class ExpressWallController {

    @Autowired
    private ExpressWallService expressWallService;

//    @RequestMapping("getTop")
//    @ResponseBody
//    public BusiResult getTop() {
//        return expressWallService.getTop();
//    }

    @RequestMapping("list")
    @ResponseBody
    public BusiResult list(Integer pageNo, Integer pageSize) {

        return expressWallService.findByPage(pageNo, pageSize);
    }
}
