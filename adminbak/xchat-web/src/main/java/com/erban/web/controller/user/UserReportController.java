package com.erban.web.controller.user;

import com.erban.main.param.UserReportParam;
import com.erban.main.service.user.UserReportService;
import com.erban.main.util.IpUtil;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户举报
 */
@Controller
@RequestMapping("/user/report")
public class UserReportController {

    @Autowired
    private UserReportService reportService;
    // POST
    @RequestMapping(value = "/save")
    @ResponseBody
    @SignVerification
    public BusiResult save(UserReportParam reportParam, HttpServletRequest request) {
        //
        if (reportParam == null || reportParam.getReportUid() == null || reportParam.getUid() == null || reportParam.getDeviceId() == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        // 用户信息校验完成
        String ip = IpUtil.getRemoteIp(request);
        reportParam.setIp(ip);
        int count = reportService.save(reportParam);
        return new BusiResult(BusiStatus.SUCCESS, count);
    }



}
