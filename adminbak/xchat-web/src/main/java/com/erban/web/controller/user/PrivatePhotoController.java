package com.erban.web.controller.user;

import com.erban.main.service.user.PrivatePhotoService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yanghaoyu 个人照片管理
 */

@Controller
@RequestMapping("/photo")
public class PrivatePhotoController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PrivatePhotoController.class);
    @Autowired
    private PrivatePhotoService privatePhotoService;


    /**
     * 上传个人照片，迁移原网易云上传的图片到七牛
     *
     * @param uid
     * @param photoStr
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult upload(Long uid, String photoStr) {
        if (uid == null || photoStr == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        String[] picStrs = photoStr.split(",");
        if (picStrs.length > 8 || picStrs.length == 0) {
            logger.info("用户上传头像错误,uid：" + uid + ",photoStr:" + photoStr);
            return new BusiResult(BusiStatus.PRIVATEPHOTOSUPMAX, BusiStatus.PRIVATEPHOTOSUPMAX.value());
        }

        BusiResult result = privatePhotoService.upload(uid, photoStr);
//        try {
//            List<PrivatePhoto> list = privatePhotoService.getPrivatePhoto(uid);
//            if (list.size() >= 5) {
//                dutyService.updateFreshDuty(uid, DutyType.private_photo.getDutyId());
//            }
//        } catch (Exception e) {
//            logger.error("[ 更新新手任务 ]异常，", e);
//        }

        return result;
    }

    @RequestMapping(value = "/delPhoto", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult delPhoto(Long uid, Long pid) {
        return privatePhotoService.delPhoto(uid, pid);
    }
}
