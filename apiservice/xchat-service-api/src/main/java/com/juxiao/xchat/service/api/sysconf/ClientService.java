package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.domain.ClientSecurityInfoDO;
import com.juxiao.xchat.service.api.sysconf.vo.AppInitVo;
import com.juxiao.xchat.service.api.sysconf.vo.ClientConfigVo;
import com.juxiao.xchat.service.api.user.bo.DeviceInfoBO;
import com.juxiao.xchat.service.common.bo.BaseParamBO;

/**
 * 客户端处理逻辑
 */
public interface ClientService {

    AppInitVo init(String os, String app, String appVersion, String ip,Long uid) throws WebServiceException;

    ClientConfigVo getConfig(String idfa, String imei, DeviceInfoBO deviceInfo, String ip, String app);

    /**
     * 保存安全上报的信息
     * @param clientSecurityInfoDO
     * @return
     */
    int saveSecurityInfo(ClientSecurityInfoDO clientSecurityInfoDO);

    /**
     * 保存客户端上传的日志
     *
     * @param paramBO
     * @param url
     * @param ip
     * @return
     */
    boolean saveLogInfo(BaseParamBO paramBO, String url, String ip);
}
