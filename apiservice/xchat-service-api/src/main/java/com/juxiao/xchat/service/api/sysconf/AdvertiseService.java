package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.AdvertiseDTO;

import java.util.List;

public interface AdvertiseService {

    List<AdvertiseDTO> list(String os, String app, String appVersion, String clientIp,Long uid) throws WebServiceException;

}
