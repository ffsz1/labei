package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.utils.XmlUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.UserInfo;
import com.juxiao.xchat.manager.common.user.vo.WXUserInfoVO;
import com.juxiao.xchat.manager.external.weixin.WeixinAuthManager;
import com.juxiao.xchat.manager.external.weixin.bo.WeixinReceiverBO;
import com.juxiao.xchat.manager.external.weixin.conf.WeixinConf;
import com.juxiao.xchat.manager.external.weixin.ret.SnsapiBaseinfoRet;
import com.juxiao.xchat.manager.external.weixin.vo.WeixinReturnCodeVO;
import com.juxiao.xchat.manager.external.weixin.vo.WxappRequestPaymentVO;
import com.juxiao.xchat.service.api.charge.WeixinChargeService;
import com.juxiao.xchat.service.api.charge.vo.WeixinUnifiedOrderVO;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微信公众号充值
 *
 * @class: WxChargeController.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Controller
@RequestMapping("/wx")
@Api(tags = "充值接口")
public class WxChargeController {
    private final Logger logger = LoggerFactory.getLogger(WxChargeController.class);
    @Autowired
    private WeixinConf weixinConf;
    @Autowired
    private WeixinAuthManager authManager;
    @Autowired
    private WeixinChargeService chargeService;

    @Autowired
    private SystemConf systemConf;

    @ApiOperation(value = "微信公众号获取openId回调接口")
    @RequestMapping(value = "/snsapi/baseinfo/get", method = RequestMethod.GET)
    public void getAccess(HttpServletResponse response, @RequestParam("code") String code,
                          @RequestParam(value = "state", required = false) String state) {
        try {
           String openId = authManager.getOpenId(weixinConf.getAppid(), weixinConf.getSecret(), code);
            if (StringUtils.isNotBlank(openId)) {
                response.sendRedirect("http://域名/front/wxpay/index.html?openId=" + openId);
            }
        } catch (Exception e) {
            logger.error("[ 微信openid接口 ]请求异常：", e);
        }
    }

    @ApiOperation(value = "微信公众号获取openId")
    @ResponseBody
    @RequestMapping(value = "/snsapi/baseinfo/scan/get", method = RequestMethod.GET)
    public WebServiceMessage getOpenId(HttpServletResponse response, @RequestParam("code") String code,
                                       @RequestParam(value = "state", required = false) String state) {
        WXUserInfoVO userInfo = null;
        try {
            String openId = authManager.getOpenId(weixinConf.getAppid(), weixinConf.getSecret(), code);
            String accessToken = authManager.getAccessToken();
            if (openId != null && !StringUtils.isBlank(openId) && !StringUtils.isBlank(accessToken)) {
                userInfo = authManager.getUserInfo(openId, accessToken);
            }
        } catch (Exception e) {
            logger.error("[ 微信详情接口 ]请求异常：", e);
            return WebServiceMessage.failure("微信详情接口异常!");
        }

        return WebServiceMessage.success(userInfo);
    }



    @ApiOperation(value = "微信公众号获取openId回调接口")
    @RequestMapping(value = "/snsapi/baseinfo/getOpenId", method = RequestMethod.GET)
    public void getGzhOpenId(HttpServletResponse response, @RequestParam("code") String code,
                          @RequestParam(value = "state", required = false) String state) {
        try {
            String openId = authManager.getOpenId(weixinConf.getAppid(), weixinConf.getSecret(), code);
            logger.info("[汇聚公众号支付] 获取微信openId:{}",openId);
            if (StringUtils.isNotBlank(openId)) {
                if("prod".equalsIgnoreCase(systemConf.getEnv())){
                    response.sendRedirect("http://域名/front/pay_huiju/index.html?openId=" + openId);
                }else{
//                    response.sendRedirect("http:// .com/front/pay_huiju/index.html?openId=" + openId);
                    response.sendRedirect("http://域名/front/pay_huiju/index.html?openId=" + openId);
                }
            }
        } catch (Exception e) {
            logger.error("[ 微信openid接口 ]请求异常：", e);
        }
    }

    @ApiOperation(value = "微信异步会通知接口", hidden = true)
    @ResponseBody
    @RequestMapping(value = "/payCallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String notify(HttpServletRequest request) {
        String xml = null;
        try {
            xml = HttpServletUtils.readString(request);
        } catch (IOException e) {
            logger.info("[ 微信支付 ]读取请求内容异常：", e);
        }

        if (StringUtils.isEmpty(xml)) {
            String result = new WeixinReturnCodeVO("SUCCESS", "OK").toString();
            logger.info("[ 微信支付 ]订单回调，返回:>{}，耗时:>{}", result.replaceAll("\n", ""));
            return result;
        }

        String result;
        String outTradeNo = "";
        try {
            WeixinReceiverBO receiverBo = XmlUtils.fromXml(xml, WeixinReceiverBO.class);
            outTradeNo = receiverBo.getOut_trade_no();
            result = chargeService.receive(receiverBo).toString();
            logger.info("[ 微信支付 {} ]订单回调，请求:{}，返回:>{}", outTradeNo, xml.replaceAll("\n", ""), result.replaceAll("\n", ""));
        } catch (Exception e) {
            logger.error("[ 微信支付 {} ]订单回调，请求:{}，异常信息：", outTradeNo, xml.replaceAll("\n", ""), e);
            result = new WeixinReturnCodeVO("FAIL", "Error, please try again.").toString();
        }


        return result;

    }

    @ApiOperation(value = "微信公众号支付下单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "erban_no", value = "官方号", dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "chargeProdId", value = "充值购买产品ID", dataType = "string"),
            @ApiImplicitParam(name = "openId", value = "用户对应的openId", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WeixinUnifiedOrderVO.class),
            @ApiResponse(code = 1444, message = "非法的参数")
    })
    @ResponseBody
    @RequestMapping(value = "/submitPay", method = RequestMethod.POST)
    public WebServiceMessage createPaySign(HttpServletRequest request,
                                           @RequestParam(value = "erban_no", required = false) Long erbanNo,
                                           @RequestParam(value = "phone", required = false) String phone,
                                           @RequestParam(value = "chargeProdId") String chargeProdId,
                                           @RequestParam(value = "openId") String openId) {
        WebServiceMessage message;
        String outTradeNo = "";
        long startTime = System.currentTimeMillis();
        String ip = HttpServletUtils.getRemoteIpV4(request);
        try {
            WeixinUnifiedOrderVO orderVo = chargeService.createPaySign(ip, erbanNo, phone, chargeProdId, openId);
            outTradeNo = orderVo.takeOutTradeNo();
            message = WebServiceMessage.success(orderVo);
        } catch (WebServiceException e) {
            logger.info("[ 微信支付{} ] WebServiceException", e);
            message = WebServiceMessage.failure(e);
        } catch (Exception e) {
            logger.info("[ 微信支付{} ] Exception", e);
            message = WebServiceMessage.failure(WebServiceCode.SERVER_ERROR);
        }

        long time = System.currentTimeMillis() - startTime;
        logger.info("[ 微信支付{} ]生成订单，请求:>erban_no={}&phone={}&chargeProdId={},openId={}，返回:>{}，耗时:>{}", outTradeNo, erbanNo, phone, chargeProdId, openId, message, time);
        return message;
    }

    @ApiOperation(value = "微信小程序支付下单接口", tags = {"小程序接口", "充值接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long"),
            @ApiImplicitParam(name = "openId", value = "用户的openId", dataType = "string"),
            @ApiImplicitParam(name = "chargeProdId", value = "充值产品ID", dataType = "string"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WxappRequestPaymentVO.class)
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @ResponseBody
    @RequestMapping(value = "wxapp/pay", method = RequestMethod.POST)
    public WebServiceMessage applyCharge(HttpServletRequest request,
                                         @RequestParam(value = "uid") Long uid,
                                         @RequestParam(value = "openId") String openId,
                                         @RequestParam(value = "chargeProdId") String chargeProdId) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        WxappRequestPaymentVO payTokenVo = chargeService.createWxappPaySign(uid, openId, chargeProdId, ip);
        return WebServiceMessage.success(payTokenVo);
    }
}



