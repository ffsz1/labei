package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.charge.IOSChargeService;
import com.juxiao.xchat.service.api.charge.vo.IOSOrderPlaceVO;
import com.juxiao.xchat.service.api.charge.vo.ReceiptVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
@Api(description = "充值接口", tags = "充值接口")
public class IOSPayPlaController {
    @Autowired
    private IOSChargeService iosPayPlaService;

    @ApiOperation(value = "iOS下单接口", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "购买产品ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "isJailbroken", value = "客户端是否越狱：0，非越狱；1，越狱", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "应用版本号", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = ReceiptVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "place", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage placeOrder(HttpServletRequest request,
                                        @RequestParam(value = "uid", required = false) Long uid,
                                        @RequestParam(value = "chargeProdId", required = false) String chargeProdId,
                                        @RequestParam(value = "isJailbroken", required = false) Integer isJailbroken,
                                        @RequestParam(value = "os", required = false) String os) throws WebServiceException {
        String ip = HttpServletUtils.getRealIp(request);
        IOSOrderPlaceVO placeVo = iosPayPlaService.placeOrder(ip, uid, chargeProdId, isJailbroken, os);
        return WebServiceMessage.success(placeVo);
    }
}
