package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.ExpressWallDTO;
import com.juxiao.xchat.manager.common.user.ExpressWallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(tags = "用户信息接口", description = "用户信息接口")
@Controller
@RequestMapping("/user/expressWall")
public class ExpressWallController {

    @Autowired
    private ExpressWallService expressWallService;

//    @RequestMapping("getTop")
//    @ResponseBody
//    public BusiResult getTop() {
//        return expressWallService.getTop();
//    }

    @ApiOperation(value = "表白墙列表")
    @GetMapping("list")
    @ResponseBody
    public WebServiceMessage list(@RequestParam(value = "pageNo") @ApiParam(value = "页码") Integer pageNo,
                                  @RequestParam(value = "pageSize") @ApiParam(value = "每页记录数") Integer pageSize) {
        List<ExpressWallDTO> list = expressWallService.findByPage(pageNo, pageSize);
        return WebServiceMessage.success(list);
    }
}
