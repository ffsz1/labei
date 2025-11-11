package com.juxiao.xchat.api.controller.monitor;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/monitor")
@Api(tags = "监控接口")
public class MonitorController {

    @ApiOperation(value = "私聊记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sendUid", value = "发送方", dataType = "long", required = true),
            @ApiImplicitParam(name = "recvUid", value = "接收方", dataType = "long", required = true),
            @ApiImplicitParam(name = "type", value = "消息类型(10.文本 20.图片)", dataType = "long", required = true),
            @ApiImplicitParam(name = "content", value = "内容", dataType = "String", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @RequestMapping(value = "chat/private", method = RequestMethod.POST)
    public WebServiceMessage chatPrivate(HttpServletRequest request,
                                         @RequestParam("sendUid") Long sendUid,
                                         @RequestParam("recvUid") Long recvUid,
                                         @RequestParam("type") Integer type,
                                         @RequestParam("content") String content) throws WebServiceException {
        log.info(" [ 私聊记录 ] sendUid:>{}, recvUid:>{}, type:>{}, content:>{}", sendUid, recvUid, type, content);
        // todo 保存记录 插表
        return WebServiceMessage.success(null);
    }
}
