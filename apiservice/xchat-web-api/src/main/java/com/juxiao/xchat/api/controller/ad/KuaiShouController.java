package com.juxiao.xchat.api.controller.ad;

import com.google.common.collect.Maps;
import com.juxiao.xchat.dao.ad.domain.AdKuaiShouRecordDO;
import com.juxiao.xchat.service.api.ad.AdKuaiShouService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author chris
 * @date 2019-06-20
 */
@Slf4j
@RestController
@RequestMapping(value = "/kuaishou")
@Api(description = "快手接口", tags = "快手接口")
public class KuaiShouController {
    @Autowired
    private AdKuaiShouService adKuaiShouService;

    @ApiOperation(value = "接收快手广告激活数据接口", notes = "接收快手广告激活数据接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Integer.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "receiveKSAd", method = RequestMethod.GET)
    public Map<String, Object> receiveKSAd(AdKuaiShouRecordDO adKuaiShouRecordDO) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            int code = adKuaiShouService.reciveKuaiShouMsg(adKuaiShouRecordDO);
            result.put("code", code);
        } catch (Exception e) {
            result.put("code", 410);
            log.error("[ 接收快手信息 ] 处理接收消息异常，异常信息：",e);
        }
        return result;
    }


}
