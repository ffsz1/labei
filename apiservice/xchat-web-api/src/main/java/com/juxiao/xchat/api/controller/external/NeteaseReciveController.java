package com.juxiao.xchat.api.controller.external;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.service.api.room.NetEaseReciveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/recive")
@Api(description = "其他接口", tags = "其他")
public class NeteaseReciveController {
    private final Logger logger = LoggerFactory.getLogger(NeteaseReciveController.class);
    @Autowired
    private NetEaseReciveService reciveService;

    @ApiOperation(value = "云信回调接口", hidden = true)
    @RequestMapping(value = "msg", method = RequestMethod.POST)
    public Map<String, Object> mockClient(HttpServletRequest request,
                                          @RequestHeader(value = "AppKey", required = false) String appKey,
                                          @RequestHeader("CurTime") String curTime,
                                          @RequestHeader("MD5") String md5,
                                          @RequestHeader("CheckSum") String checkSum) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            // 获取请求体
            String requestBody = new String(HttpServletUtils.readString(request).getBytes(), "utf-8");
            int code = reciveService.reciveNetEaseMsg(curTime, md5, checkSum, requestBody);
            result.put("code", code);
            logger.info("[ 接收云信信息 ] 请求:>AppKey={}&CurTime={}&MD5={}CheckSum={}，requestBody:>{},返回:>{}", appKey, curTime, md5, checkSum, requestBody, JSON.toJSON(result));
        } catch (Exception e) {
            result.put("code", 444);
            logger.error("[ 接收云信信息 ] 处理接收消息异常，请求:>AppKey={}&CurTime={}&MD5={}CheckSum={}，异常信息：", appKey, curTime, md5, checkSum, e);
        }

        return result;
    }

}
