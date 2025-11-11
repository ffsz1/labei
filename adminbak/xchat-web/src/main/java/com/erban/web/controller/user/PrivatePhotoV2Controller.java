package com.erban.web.controller.user;

import com.erban.main.service.user.PrivatePhotoV2Service;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 个人照片管理
 */
@Controller
@RequestMapping("/photo/v2")
public class PrivatePhotoV2Controller {
    @Autowired
    private PrivatePhotoV2Service privatePhotoV2Service;

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
    public BusiResult uploadV2(Long uid, String photoStr) {
        if (uid == null || photoStr == null || photoStr.equals("")) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        String[] picStrs = photoStr.split(",");
        if (picStrs.length > 8 || picStrs.length == 0) {
            return new BusiResult(BusiStatus.PRIVATEPHOTOSUPMAX, BusiStatus.PRIVATEPHOTOSUPMAX.value());
        }
        BusiResult result = privatePhotoV2Service.upload(uid, photoStr);
//        try {
//            List<PrivatePhoto> list = privatePhotoService.getPrivatePhoto(uid);
//            if (list.size() >= 5) {
//                dutyService.updateFreshDuty(uid, DutyType.private_photo.getDutyId());
//            }
//        } catch (Exception e) {
//        }

        return result;
    }

}
