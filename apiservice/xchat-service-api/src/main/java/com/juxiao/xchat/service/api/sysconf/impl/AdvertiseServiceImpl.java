package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.AdvertiseDao;
import com.juxiao.xchat.dao.sysconf.dto.AdvertiseDTO;
import com.juxiao.xchat.manager.common.constant.AppClient;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.sysconf.AdvertiseService;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    @Autowired
    private AppVersionManager appVersionService;
    @Autowired
    private AdvertiseDao advertiseDao;

    @Deprecated
    @Override
    public List<AdvertiseDTO> list(String os, String app, String appVersion, String clientIp,Long uid) throws WebServiceException {
        if (appVersionService.checkAuditingVersion(os, app, appVersion, clientIp,uid)) {
            List<AdvertiseDTO> result = Lists.newArrayList();
            AdvertiseDTO advertiseVo = new AdvertiseDTO();
            advertiseVo.setAdvName("官方使用手册");
            advertiseVo.setAdvIcon("https://pic.chaoxuntech.com/FmcE2uTbOdnyAiSoiT-RNVYx2Uwl?imageslim");
            advertiseVo.setSkipType(new Byte("3"));
            advertiseVo.setSkipUri("https://mp.weixin.qq.com/s/5XM873ZRbLzI65DEsrxxGg");
            result.add(advertiseVo);
            return result;
        }
        return advertiseDao.list();
    }
}
