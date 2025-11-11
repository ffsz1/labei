package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.soundcard.SoundCardService;
import com.juxiao.xchat.service.api.soundcard.vo.SoundCardResultVO;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 声鉴卡接口
 *
 * @class: SoundCardController.java
 * @author: chenjunsheng
 * @date 2018/5/24
 */
@Api(tags = "声鉴卡接口", description = "声鉴卡接口")
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "soundcard")
public class SoundCardController {
    @Autowired
    private SoundCardService soundcardService;

    /**
     * 获取随机文本
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/5/28
     */
    @ApiOperation(value = "获取随机文本", notes = "需要登录和加密")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "uid", required = true, dataTypeClass = Long.class)
    })
    @RequestMapping(value = "/text", method = {RequestMethod.GET, RequestMethod.POST})
    @Authorization
    @SignVerification
    public WebServiceMessage getText(@RequestParam(value = "uid") Long uid) throws WebServiceException {
        String text = soundcardService.getText(uid);
        return WebServiceMessage.success(text);
    }

    /**
     * 分析鉴别用户的声音
     *
     * @param uid
     * @param sound 上传的声音文件
     * @return
     * @author: chenjunsheng
     * @date 2018/5/24
     */
    @ApiOperation(value = "分析鉴别用户的声音", notes = "需要登录")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "uid", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(value = "声音文件", name = "sound", required = true, dataTypeClass = MultipartFile.class)
    })
    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    @Authorization
    public WebServiceMessage analysis(@RequestParam(value = "uid") Long uid, @RequestParam(value = "sound", required = false) MultipartFile sound) throws WebServiceException {
        SoundCardResultVO vo = soundcardService.analysis(uid, sound);
        return WebServiceMessage.success(vo);
    }
}