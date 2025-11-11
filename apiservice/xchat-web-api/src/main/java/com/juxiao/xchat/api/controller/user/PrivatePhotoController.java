package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.sysconf.GeneralManager;
import com.juxiao.xchat.service.api.user.PrivatePhotoService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 个人照片管理
 */
@RestController
@RequestMapping("/photo")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class PrivatePhotoController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PrivatePhotoController.class);
    @Autowired
    private PrivatePhotoService photoService;

    @Autowired
    private GeneralManager generalManager;

    /**
     * 上传个人照片，迁移原网易云上传的图片到七牛
     *
     * @param uid
     * @param photoStr
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public WebServiceMessage upload(@RequestParam("uid") Long uid, @RequestParam("photoStr") String photoStr) throws WebServiceException {
       if(StringUtils.isNotBlank(photoStr)){
           if(generalManager.checkProhibitModification()){
               throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
           }
       }
        photoService.uploadV2(uid, photoStr);
        return WebServiceMessage.success(null);
    }

    /**
     * 上传个人照片，迁移原网易云上传的图片到七牛
     *
     * @param uid
     * @param photoStr
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/v2/upload", method = RequestMethod.POST)
    public WebServiceMessage uploadV2(@RequestParam("uid") Long uid, @RequestParam("photoStr") String photoStr) throws WebServiceException {
        if(StringUtils.isNotBlank(photoStr)){
            if(generalManager.checkProhibitModification()){
                throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
            }
        }
        photoService.uploadV2(uid, photoStr);
        return WebServiceMessage.success(null);
    }

    /**
     * 删除图片接口
     *
     * @param uid
     * @param pid
     * @return
     * @throws WebServiceException
     */
    @Authorization
    @RequestMapping(value = "/delPhoto", method = RequestMethod.POST)
    public WebServiceMessage delPhoto(@RequestParam(value = "uid", required = false) Long uid,
                                      @RequestParam(value = "pid", required = false) Long pid) throws WebServiceException {
        photoService.delete(uid, pid);
        return WebServiceMessage.success(null);
    }

}
