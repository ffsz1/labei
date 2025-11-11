package com.erban.web.controller.advertise;

import com.erban.main.base.BeanMapper;
import com.erban.main.model.Advertise;
import com.erban.main.service.AppVersionService;
import com.erban.main.service.advertise.AdvertiseService;
import com.erban.main.vo.AdvertiseVo;
import com.erban.web.common.BaseController;
import com.google.common.collect.Lists;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/advertise")
public class AdvertiseController extends BaseController {
    @Autowired
    AdvertiseService advertiseService;
    @Autowired
    AppVersionService appVersionService;

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getList(String os, String appVersion, HttpServletRequest request) {
        try {
            // IOS新版本在审核期内的首页数据要做特殊处理
            if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion, request)) {
                List<AdvertiseVo> result = Lists.newArrayList();
                AdvertiseVo advertiseVo = new AdvertiseVo();
                advertiseVo.setAdvName("拉贝使用手册");
                advertiseVo.setAdvIcon("http://res.91fb.com/FmcE2uTbOdnyAiSoiT-RNVYx2Uwl?imageslim");
                advertiseVo.setSkipType(new Byte("3"));
                advertiseVo.setSkipUri("https://mp.weixin.qq.com/s/5XM873ZRbLzI65DEsrxxGg");
                result.add(advertiseVo);
                return new BusiResult(BusiStatus.SUCCESS, result);
            }
            List<Advertise> list = advertiseService.list();
            List<AdvertiseVo> result = BeanMapper.mapList(list, Advertise.class, AdvertiseVo.class);
            return new BusiResult(BusiStatus.SUCCESS, result);
        } catch(Exception e) {
            logger.error("Failed to advertise getList. Cause by {}", e.getMessage());
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

}
