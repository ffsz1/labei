package com.erban.admin.main.service;

import com.erban.main.mybatismapper.GiftMapper;
import com.erban.main.service.BannerService;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.gift.GiftService;
import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.result.FileUploadRet;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UpLoadImgService {

    @Autowired
    private BannerService bannerService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private GiftMapper giftMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private GiftService giftService;

    private Gson gson = new Gson();

    public void addBanner(MultipartFile file, String title, Byte type, Integer seqNo) throws Exception {
        FileUploadRet fileUploadRet = erBanNetEaseService.uploadMultipartFile(file);
        bannerService.addBanner(fileUploadRet, title, type, seqNo);
    }

    public BusiResult updateGift(Integer giftId, MultipartFile file) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        FileUploadRet fileUploadRet = erBanNetEaseService.uploadMultipartFile(file);
        busiResult = giftService.updateGift(fileUploadRet, giftId);
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
