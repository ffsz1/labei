package com.juxiao.xchat.api.controller.sysconf;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.sysconf.SensitiveWordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sensitiveWord")
@Api(tags = "客户端配置接口", description = "客户端配置接口")
public class SensitiveWordController {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    /**
     * 过滤铭感词的正则
     *
     * @return
     */
    @RequestMapping("regex")
    public WebServiceMessage regex() {
        String regex = sensitiveWordService.regex();
        return WebServiceMessage.success(regex);
    }

    /**
     * 敏感词列表
     *
     * @return
     */
    @RequestMapping("list")
    public WebServiceMessage list() {
        List<String> list = sensitiveWordService.list();
        return WebServiceMessage.success(list);
    }
}
