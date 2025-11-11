package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.charge.ChargeService;
import com.juxiao.xchat.service.api.charge.vo.EcpssAlipayVO;
import com.pingplusplus.model.Charge;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/charge")
@Api(description = "充值接口", tags = "充值接口")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    @ApiOperation(value = "发起充值接口", notes = "com.juxiao.xchat.api.controller.charge.ChargeController")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "payChannel", value = "充值渠道", dataType = "string", required = true),
            @ApiImplicitParam(name = "successUrl", value = "充值成功回调页面", dataType = "string"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Charge.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "apply", method = RequestMethod.POST)
    public WebServiceMessage applyCharge(HttpServletRequest request,
                                         @RequestParam(value = "uid", required = false) Long uid,
                                         @RequestParam(value = "chargeProdId", required = false) String chargeProdId,
                                         @RequestParam(value = "payChannel", required = false) String payChannel,
                                         @RequestParam(value = "successUrl", required = false) String successUrl) throws Exception {
        String clientIp = HttpServletUtils.getRemoteIpV4(request);
        return WebServiceMessage.success(chargeService.applyCharge(uid, chargeProdId, payChannel, clientIp, successUrl));
    }


    @ApiOperation(value = "检验用户是否存在", notes = "返回用户昵称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "用户号", dataType = "Long", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "checkUser", method = RequestMethod.GET)
    public WebServiceMessage checkUser(@RequestParam(value = "userNo", required = false) Long userNo, @RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        return WebServiceMessage.success(chargeService.checkUser(userNo, uid));
    }

    @ApiOperation(value = "官网发起充值接口", notes = "用户在进行充值之前，在数据库中创建订单ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "用户号", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "payChannel", value = "充值渠道", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Charge.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "webApply", method = RequestMethod.POST)
    public WebServiceMessage webApply(HttpServletRequest request,
                                      @RequestParam(value = "userNo") Long userNo,
                                      @RequestParam(value = "chargeProdId") String chargeProdId,
                                      @RequestParam(value = "payChannel") String payChannel,
                                      @RequestParam(value = "successUrl", required = false) String successUrl) throws Exception {
        return WebServiceMessage.success(chargeService.webApply(userNo, chargeProdId, payChannel, HttpServletUtils.getRemoteIpV4(request), successUrl));
    }

    @ApiOperation(value = "官网充值查询是否已经支付完成接口", notes = "官网充值查询是否已经支付完成接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chargeRecordId", value = "订单号", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Boolean.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "checkApply", method = RequestMethod.GET)
    public WebServiceMessage checkApply(@RequestParam(value = "chargeRecordId") String chargeRecordId) {
        return WebServiceMessage.success(chargeService.checkApply(chargeRecordId));
    }


    @ApiOperation(value = "汇聚支付--发起充值接口", notes = "用户在进行充值之前，在数据库中创建订单ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "payChannel", value = "充值渠道", dataType = "string", required = true),
            @ApiImplicitParam(name = "successUrl", value = "充值成功回调页面", dataType = "string"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Charge.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @PostMapping(value = "/joinpay/apply")
    public WebServiceMessage joinpayCharge(HttpServletRequest request,
                                           @RequestParam(value = "uid") Long uid,
                                           @RequestParam(value = "chargeProdId") String chargeProdId,
                                           @RequestParam(value = "payChannel") String payChannel,
                                           @RequestParam(value = "successUrl", required = false) String successUrl) throws Exception {
        return WebServiceMessage.success(chargeService.joinpayCharge(uid, chargeProdId, payChannel, HttpServletUtils.getRemoteIpV4(request), successUrl, ""));
    }

    @ApiOperation(value = "汇聚支付--官网发起充值接口", notes = "用户在进行充值之前，在数据库中创建订单ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "用户号", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "payChannel", value = "充值渠道", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Charge.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/joinpay/webApply", method = RequestMethod.POST)
    public WebServiceMessage joinpayWebApply(HttpServletRequest request,
                                             @RequestParam(value = "userNo") Long userNo,
                                             @RequestParam(value = "chargeProdId") String chargeProdId,
                                             @RequestParam(value = "payChannel") String payChannel,
                                             @RequestParam(value = "openId", required = false) String openId,
                                             @RequestParam(value = "successUrl", required = false) String successUrl) throws Exception {
        return WebServiceMessage.success(chargeService.joinpayWebApply(userNo, chargeProdId, payChannel, HttpServletUtils.getRemoteIpV4(request), successUrl, openId));
    }


    @ApiOperation(value = "汇潮支付--支付宝充值(APP)", notes = "返回一个支付请求URL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = EcpssAlipayVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/ecpss/alipay/apply", method = RequestMethod.POST)
    public WebServiceMessage ecpssApply(HttpServletRequest request,
                                        @RequestParam(value = "uid") Long uid,
                                        @RequestParam(value = "ticket") String ticket,
                                        @RequestParam(value = "appid") String appid,
                                        @RequestParam(value = "chargeProdId") String chargeProdId) throws Exception {
        return WebServiceMessage.success(chargeService.ecpssApply(uid, chargeProdId, appid, HttpServletUtils.getRemoteIpV4(request)));
    }

    @ApiOperation(value = "汇潮支付--支付宝充值(H5)", notes = "返回一个支付请求URL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "用户喵喵号", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = EcpssAlipayVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/ecpss/alipay/webApply", method = RequestMethod.POST)
    public WebServiceMessage ecpssWebApply(HttpServletRequest request,
                                           @RequestParam(value = "userNo") Long userNo,
                                           @RequestParam(value = "chargeProdId") String chargeProdId) throws Exception {
        return WebServiceMessage.success(chargeService.ecpssWebApply(userNo, chargeProdId, HttpServletUtils.getRemoteIpV4(request)));
    }

    @ApiOperation(value = "用户当前充值金额", notes = "返回用户当前充值金额与礼盒次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户uid", dataType = "Long", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "userCharge", method = RequestMethod.GET)
    public WebServiceMessage getUserCharge(@RequestParam(value = "uid", required = true) Long uid) throws WebServiceException {
        return WebServiceMessage.success(chargeService.getUserCharge(uid));
    }

    @ApiOperation(value = "充值活动测试接口", notes = "返回一个支付请求URL")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = EcpssAlipayVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/testCharge", method = RequestMethod.POST)
    public WebServiceMessage testCharge(@RequestParam(value = "uid") Long uid, @RequestParam(value = "chargeProdId") String chargeProdId) throws Exception {
        chargeService.testCharge(uid, chargeProdId);
        return WebServiceMessage.success("充值成功");
    }

    @ApiOperation(value = "衫德支付--支付宝充值(H5)", notes = "返回一个支付请求URL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "用户喵喵号", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = EcpssAlipayVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/sand/alipay/webApply", method = RequestMethod.POST)
    public WebServiceMessage sandWebApply(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestParam(value = "userNo") Long userNo,
                                          @RequestParam(value = "chargeProdId") String chargeProdId,
                                          @RequestParam(value = "payChannel", required = false) String payChannel,
                                          @RequestParam(value = "successUrl", required = false) String successUrl) throws Exception {
        return WebServiceMessage.success(chargeService.sandWebApply(userNo, chargeProdId, HttpServletUtils.getRemoteIpV4(request), payChannel, successUrl));
    }

}
