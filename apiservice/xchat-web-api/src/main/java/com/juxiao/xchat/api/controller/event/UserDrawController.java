package com.juxiao.xchat.api.controller.event;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.domain.UserBoxDrawRecordDO;
import com.juxiao.xchat.dao.user.enumeration.UserWordDrawActivityType;
import com.juxiao.xchat.service.api.user.UserDrawService;
import com.juxiao.xchat.service.api.user.UserWordDrawService;
import com.juxiao.xchat.service.api.user.UsersService;
import com.juxiao.xchat.service.api.user.vo.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/draw")
@Api(tags = "抽奖接口")
public class UserDrawController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserDrawService userDrawService;
    @Autowired
    private UserWordDrawService userWordDrawService;
    /**
     * 获取我的抽奖信息
     *
     * @param uid
     **/
    @Authorization
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public WebServiceMessage getUserDrawInfo(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        return WebServiceMessage.success(userDrawService.getUserDraw(uid));
    }

    /**
     * 获取我的抽奖信息
     *
     * @param uid
     **/
    @RequestMapping(value = "/getWord", method = RequestMethod.GET)
    @ApiOperation(value = "获取文字抽奖信息", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true)
    })
    @Authorization
    public WebServiceMessage getUserWordDraw(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        return WebServiceMessage.success(userWordDrawService.getUserWordDraw(uid, UserWordDrawActivityType.NIU_DAN.getType()));
    }

    @ApiOperation(value = "发起一次抽奖", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/do", method = RequestMethod.GET)
    @Authorization
    public WebServiceMessage doUserDraw(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        UserDrawResultVO resultVo = userDrawService.draw(uid);
        UserVO userVo = usersService.getUser(uid,null);
        resultVo.setUserVo(userVo);
        return WebServiceMessage.success(resultVo);
    }
    

    @ApiOperation(value = "兑换文字抽奖奖品", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/wordPrize", method = RequestMethod.GET)
    @Authorization
    public WebServiceMessage doWordPrize(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        UserDrawResultVO resultVo = userWordDrawService.draw(uid, UserWordDrawActivityType.NIU_DAN.getType());
        UserVO userVo = usersService.getUser(uid,null);
        resultVo.setUserVo(userVo);
        return WebServiceMessage.success(resultVo);
    }


    /**
     * 获取已经中奖的记录，用于滚屏
     **/
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Authorization
    public WebServiceMessage listUserDrawWinRecord() {
        List<UserDrawWinRecordVO> records = userDrawService.listUserDrawWinRecord();
        for (UserDrawWinRecordVO recordVo : records) {
            try {
                recordVo.setUserVo(usersService.getUser(recordVo.getUid(),null));
            } catch (WebServiceException e) {
                continue;
            }
        }
        return WebServiceMessage.success(records);
    }

    @ApiOperation(value = "发起一次礼盒抽奖", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/doBoxDraw", method = RequestMethod.POST)
    @Authorization
    public WebServiceMessage doBoxDraw(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        BoxDrawVO resultVo = userDrawService.doBoxDraw(uid);
        return WebServiceMessage.success(resultVo);
    }

    /**
     * 获取指定用户的中奖纪录
     **/
    @RequestMapping(value = "/boxDrawList", method = RequestMethod.GET)
    @Authorization
    public WebServiceMessage listUserBoxDrawRecord(@RequestParam(value = "uid", required = true) Long uid) {
        List<UserBoxDrawRecordDO> records = userDrawService.listBoxDrawRecord(uid);
        return WebServiceMessage.success(records);
    }

    /**
     * 获取指定用户的礼盒信息
     **/
    @ApiOperation(value = "获取指定用户的礼盒信息", notes = "需要登录ticket和加密")
    @RequestMapping(value = "/userBoxInfo", method = RequestMethod.GET)
    @Authorization
    public WebServiceMessage userBoxInfo(@RequestParam(value = "uid", required = true) Long uid) throws WebServiceException {
        return WebServiceMessage.success(userDrawService.getUserBox(uid));
    }

}
