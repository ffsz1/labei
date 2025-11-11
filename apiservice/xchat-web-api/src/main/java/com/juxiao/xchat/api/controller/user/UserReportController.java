package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.UserReportService;
import com.juxiao.xchat.service.api.user.bo.UserItemReportBO;
import com.juxiao.xchat.service.api.user.bo.UserReportBO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户举报
 */
@RestController
@RequestMapping("/user/report")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class UserReportController {

    @Autowired
    private UserReportService reportService;

    /**
     * 保存用户举报信息
     *
     * @param request
     * @param reportBo
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public WebServiceMessage save(HttpServletRequest request, UserReportBO reportBo) throws WebServiceException {
        reportBo.setIp(HttpServletUtils.getRealIp(request));
        reportService.save(reportBo);
        return WebServiceMessage.success(null);
    }


    /**
     * 保存用户头像举报信息
     *
     * @param request request
     * @param reportBo reportBo
     * @return WebServiceMessage
     */
    @SignVerification
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public WebServiceMessage saveAvatar(HttpServletRequest request, UserItemReportBO reportBo) throws WebServiceException {
        reportBo.setIp(HttpServletUtils.getRealIp(request));
        reportService.saveAvatar(reportBo);
        return WebServiceMessage.success(null);
    }


    /**
     * 保存用户相册举报信息
     *
     * @param request request
     * @param reportBo reportBo
     * @return WebServiceMessage
     */
    @SignVerification
    @RequestMapping(value = "/album", method = RequestMethod.POST)
    public WebServiceMessage saveAlbum(HttpServletRequest request, UserItemReportBO reportBo) throws WebServiceException {
        reportBo.setIp(HttpServletUtils.getRealIp(request));
        reportService.saveAlbum(reportBo);
        return WebServiceMessage.success(null);
    }



}
