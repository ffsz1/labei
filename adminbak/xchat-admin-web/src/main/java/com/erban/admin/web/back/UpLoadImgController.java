package com.erban.admin.web.back;


import com.erban.admin.main.service.UpLoadImgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;

@RequestMapping(value = "/img")
@Controller
public class UpLoadImgController {
    private static final Logger logger = LoggerFactory.getLogger(UpLoadImgController.class);
    @Autowired
    private UpLoadImgService upLoadImgService;

    @RequestMapping(value = "/addbanner", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult uploadBanner(MultipartFile file, String title, Byte type, Integer seqNo) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (file == null || title == null || type == null || seqNo == null) {
            return new BusiResult<>(BusiStatus.BUSIERROR);
        }
        try {
            upLoadImgService.addBanner(file, title, type, seqNo);
        } catch (Exception e) {
            logger.error("uploadBanner error..file=" + file, e.getMessage());
            return new BusiResult<>(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping(value = "updategift", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult updateGift(Integer giftId, MultipartFile file) {
        BusiResult busiResult = null;
        if (giftId == null || file == null) {
            return new BusiResult<>(BusiStatus.BUSIERROR);
        }
        try {
            upLoadImgService.updateGift(giftId, file);
        } catch (Exception e) {
            logger.error("updategift error..file=" + file, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }
}
