package com.juxiao.xchat.api.controller.external;


import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.manager.external.weixin.WeixinCustomManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 消息接收
 *
 * @class: MessageController.java
 * @author: chenjunsheng
 * @date 2018/5/8
 */
@RestController
@RequestMapping(value = "weixin/custom")
@Api(description = "其他接口", tags = "其他")
public class WeixinCustomController {
    private final Logger logger = LoggerFactory.getLogger(WeixinCustomController.class);

    @Autowired
    private WeixinCustomManager customManager;

    @ApiOperation(value = "接收微信客服消息接口", hidden = true)
    @RequestMapping(value = "/v1/receiver", method = {RequestMethod.POST, RequestMethod.GET})
    public String receiveWeixinCustom(HttpServletRequest request,
                                      @RequestParam(value = "signature", required = false) String signature,
                                      @RequestParam(value = "timestamp", required = false) String timestamp,
                                      @RequestParam(value = "nonce", required = false) String nonce,
                                      @RequestParam(value = "echostr", required = false) String echostr) throws IOException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return customManager.checkWeixinSignature(signature, timestamp, nonce, echostr);
        }

        String text = HttpServletUtils.readString(request);
        logger.info("[ 接收微信客服信息 ] 请求:>{}", text);
        JSONObject object = JSONObject.parseObject(text);
        customManager.receiveWeixinMessage(object.getString("Encrypt"));
        return "success";
    }
}
