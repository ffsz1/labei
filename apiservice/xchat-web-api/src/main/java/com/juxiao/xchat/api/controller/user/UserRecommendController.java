package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.sysconf.vo.HomeV2Vo;
import com.juxiao.xchat.service.api.user.UsersRecommendService;
import com.juxiao.xchat.service.api.user.UsersService;
import com.juxiao.xchat.service.api.user.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "用户推荐接口", description = "用户推荐接口")
@RequestMapping("/user")
@RestController
public class UserRecommendController {
    @Autowired
    private UsersRecommendService usersRecommendService;

    @ApiOperation(value = "获取推荐用户列表", tags = {"用户推荐接口"})
    @ApiResponse(response = UserVO.class, code = 200, message = "success")
    @GetMapping("/getRecommendUsers")
    public WebServiceMessage getRecommendUsers(@ApiParam(value = "用户ID") @RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        List<UserVO> list = usersRecommendService.getRecommendUsers(uid,6);
        return WebServiceMessage.success(list);
    }
}
