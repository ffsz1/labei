package com.erban.web.controller.user;

import com.erban.main.service.user.SensitiveWordService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 敏感词
 */
@RestController
@RequestMapping("sensitiveWord")
public class SensitiveWordController {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    /**
     * 过滤铭感词的正则
     * @return
     */
    @RequestMapping("regex")
    public BusiResult regex() {
        String regex = sensitiveWordService.regex();
        return new BusiResult(BusiStatus.SUCCESS, regex);
    }
}
