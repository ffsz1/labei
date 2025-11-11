package com.juxiao.xchat.api.controller.sysconf;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.external.agora.AgoraService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agora")
@Api(tags = "客户端配置接口", description = "客户端配置接口")
public class AgoraController {
    @Autowired
    private AgoraService agoraService;

    @RequestMapping(value = "getKey", method = RequestMethod.POST)
    public WebServiceMessage queryActivityHomeList(String roomId, Long uid) {
        if (roomId == null || uid == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        return agoraService.getChannelKey(roomId, uid);
    }

}
