package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.LevelService;
import com.juxiao.xchat.manager.common.level.vo.LevelVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户经验值
 */
@RestController
@RequestMapping("level")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @RequestMapping("/exeperience/get")
    public WebServiceMessage getExeperience(@RequestParam("uid") Long uid) throws WebServiceException {
        LevelVO vo = levelService.getLevelExperience(uid);
        return WebServiceMessage.success(vo);
    }

    /**
     * 获取用户魅力级别
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "/charm/get", method = RequestMethod.GET)
    public WebServiceMessage getCharm(@RequestParam("uid") Long uid) throws WebServiceException {
        LevelVO vo = levelService.getLevelCharm(uid);
        return WebServiceMessage.success(vo);
    }
}
