package com.erban.web.controller.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.erban.main.config.WxConfig;
import com.erban.main.model.ChargeRecord;
import com.erban.main.model.Users;
import com.erban.main.service.WXPushService;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.DutyType;
import com.erban.main.service.user.UsersService;
import com.erban.main.service.wx.WxService;
import com.erban.main.util.HttpUtil;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserHasPhoneVo;
import com.erban.main.vo.UserVo;
import com.erban.web.wechat.service.WXPubService;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.wx.PayUtil;
import com.xchat.common.wx.Util;
import com.xchat.common.wx.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 北岭山下 on 2017/7/22.
 */
/*
                                微信公众号支付
                                实现功能：
                                        1、商户server调用统一下单接口请求订单，api参见公共api【统一下单API】
                                        2、商户server接收支付通知，api参见公共api【支付结果通知API】
                                        3、商户server查询支付结果，api参见公共api【查询订单API】
                                 微信支付流程
                                                1）获取code
                                                        我们还提供了第二种获取code的方式，支持网站将微信登录二维码内嵌到自己页面中，
                                                        用户使用微信扫码授权后通过JS将code返回给网站。
                                                        (H5提供out_trade_no 此处指订单ID和微信返回的code)

                                                        接口
                                                        code：微信返回的code
                                                        outTradeNo:订单ID


                                              //==========================服务器内容=====================//
                                                2）通过code获取openid
                                                                返回结果：
                                                                {
                                                                        "access_token":"ACCESS_TOKEN",
                                                                        "expires_in":7200,
                                                                        "refresh_token":"REFRESH_TOKEN",
                                                                        "openid":"OPENID",
                                                                        "scope":"SCOPE",
                                                                        "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
                                                                      }
                                                3）调用统一下单接口
                                                4）通过微信JSAPI中的getBrandWCPayRequest调用支付
                                                5）支付回调修改订单状态


                                                 //code 微信返回的code
        //out_trade_no 此处指订单ID
        //total_fee 订单需要支付的金额
        //appid、secret 参考文档
        /*
                【appid】公众账号ID:微信支付分配的公众账号ID（企业号corpid即为此appId）
                【secret】应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
         */






@Controller
@RequestMapping("/wx")
public class JSAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxService.class);
    private static final String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
    @Autowired
    private DutyService dutyService;
    @Autowired
    WxService wxService;
    @Autowired
    UsersService usersService;
    @Autowired
    WXPubService wxPubService;
    @Autowired
    WXPushService wxPushService;

    /**
     * 获取微信openid接口
     *
     * @param code
     * @param state
     * @return
     * @author: chenjunsheng
     * @date 2018/6/1
     */
    @RequestMapping(value = "/snsapi/baseinfo/get", method = RequestMethod.GET)
    public ModelAndView getAccess(@RequestParam("code") String code,
                                  @RequestParam(value = "state", required = false) String state) {

        StringBuilder params = new StringBuilder();
        params.append("appid=").append(WxConfig.appId);
        params.append("&secret=").append(WxConfig.appSecret);
        params.append("&code=").append(code);
        params.append("&grant_type=authorization_code");
        String data;
        try {
            data = HttpUtil.get(url, params.toString());
        } catch (Exception e) {
            ModelAndView view = new ModelAndView(new MappingJackson2JsonView());
            view.addObject("code", "500");
            view.addObject("message", "网络繁忙，请稍后重试");
            return view;
        }

        LOGGER.info("[ getSnsapiBaseinfo ] 请求:{}?{},返回:>{}", url, params, data);
        JSONObject object = JSON.parseObject(data);
        if (object.containsKey("errcode")) {
            ModelAndView view = new ModelAndView(new MappingJackson2JsonView());
            view.addObject("code", "500");
            view.addObject("message", "微信响应：" + object.getString("errcode") + ":" + object.getString("errmsg"));
            return view;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(state)) {
            ModelAndView view = new ModelAndView(new MappingJackson2JsonView());
            view.addObject("code", "404");
            view.addObject("message", "您要打开的页面上火星了！");
            return view;
        }

        String url = "redirect:" + state + (state.endsWith(".html") ? "?" : "&") + "openId=" + object.getString("openid");
        return new ModelAndView(url);
    }

    //支付回调接口（微信异步会通知）【notify_url 配置的值】
    @ResponseBody
    @RequestMapping(value = "/payCallback", method = RequestMethod.POST)
    public void payCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String retStr = new String(Util.readInput(request.getInputStream()), "utf-8");
        LOGGER.info("=======微信支付回调的结果为：" + retStr);
        //返回的数据
        Map map = XMLParser.getMapFromXML(retStr);
        String return_code = "";
        String return_msg = "";
        String xml = "<xml>";
        String openId = (String) map.get("openid");
//                String openId = ( String ) request.getSession ( ).getAttribute ( "openId" );
        LOGGER.info("======payCallback 获取openId=" + openId + "=======");


        /*回调接口处理*/
        //1.签名验证
        String recSign = (String) map.get("sign");
        LOGGER.info("传入的签名：" + recSign);
        map.remove("sign");
        String sign = PayUtil.getPaySign(map, WxConfig.key);
        LOGGER.info("校验的签名：" + sign);
        /**
         * 签名验证( 验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。)
         */
        if (!sign.equals(recSign)) {
            //验证失败
            LOGGER.error("=================验证签名失败======================");
            xml += "<return_code><![CDATA[FAIL]]></return_code>";
            xml += "<return_msg><![CDATA[签名失败]]></return_msg></xml>";
            response.setContentType("text/xml");
            response.getWriter().print(xml);
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        //2.结果判断
        if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
            LOGGER.info("================支付回调接口通知：支付成功，进行订单操作=========================");
            //6.返回信息
            try {
                BusiResult busiResult = wxService.getPayResult((Map<String, String>) map);
                ChargeRecord chargeRecord = (ChargeRecord) busiResult.getData();
                Users users = usersService.getUsersByUid(chargeRecord.getUid());
                Long erbanNo = users.getErbanNo();

                LOGGER.info("busiResult.code={}", busiResult.getCode());
                if (busiResult.getCode() == 200) {
                    LOGGER.info("================验证成功=========================");
                    return_code += "<return_code><![CDATA[SUCCESS]]></return_code>";
                    return_msg += "<return_msg><![CDATA[OK]]></return_msg>";
                    /* 发送订单消息给用户 */
                    wxPubService.sendModelMsg(erbanNo, openId, String.valueOf(chargeRecord.getAmount() / 10), "支付成功");
                    dutyService.updateDailyDuty(users.getUid(), DutyType.charge.getDutyId());
                } else {
                    wxPubService.sendModelMsg(erbanNo, openId, "--", "支付失败");
                    LOGGER.info("================验证失败：" + busiResult.getMessage() + "=========================");
                    return_code += "<return_code><![CDATA[FAIl]]></return_code>";
                    return_msg += "<return_msg><![CDATA[" + busiResult.getMessage() + "]]></return_msg>";
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("================验证失败=========================");
                return_code += "<return_code><![CDATA[FAIl]]></return_code>";
                return_msg += "<return_msg><![CDATA[ERROR]]></return_msg>";
                response.setContentType("text/xml");
                xml += return_code + return_msg + "</xml>";
                response.getWriter().print(xml);
                response.getWriter().flush();
                response.getWriter().close();
            }
        }

        response.setContentType("text/xml");
        xml += return_code + return_msg + "</xml>";
        response.getWriter().print(xml);
        response.getWriter().flush();
        response.getWriter().close();

    }


    /**
     * @param erban_no
     * @param chargeProdId 订单类型
     * @throws UnrecoverableKeyException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    /**
     * 支付成功之后 微信会进行异步回调的地址
     *
     * @param erban_no     用户ID
     * @param phone        用户绑定电话
     * @param chargeProdId 订单类型
     * @param openId       微信的openid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitPay", method = RequestMethod.POST)
    public BusiResult submitPayWx(Long erban_no, String phone, String chargeProdId, String openId) {
        String nick = "";
        Long myUid = 0L;
        Long myErBanNo = 0L;

        if (StringUtils.isEmpty(chargeProdId) && StringUtils.isEmpty(openId)) {
            LOGGER.error("============erban_no=" + erban_no + "=======微信公众号下单参数异常========");
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (erban_no == null && phone == null) {
            LOGGER.error("============erban_no=" + erban_no + "=======微信公众号下单参数异常========");
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        //获取openId
//        String openId = ( String ) session.getAttribute ( "openId" );
        LOGGER.info("===========获取openIdInfo===========" + openId);
        try {
            if (erban_no != null) {
                BusiResult<List<UserVo>> list1 = usersService.getUsersByErbanNo(erban_no);
                if (list1.getData() == null) {
                    LOGGER.info("=========" + GlobalConfig.appName + "号：" + erban_no + "=====不存在================");
                    return new BusiResult(BusiStatus.USERNOTEXISTS);
                }
                nick = list1.getData().get(0).getNick();
                myUid = list1.getData().get(0).getUid();
                myErBanNo = erban_no;
                LOGGER.info("=========" + GlobalConfig.appName + "号：" + erban_no + "=====存在================");
            } else if (phone != null) {
                List<String> list = new ArrayList<>();
                list.add(phone);
                List<UserHasPhoneVo> list1 = usersService.getByPhone(list);
                if (list1 == null) {
                    LOGGER.info("=========手机号：" + phone + "=====不存在================");
                    return new BusiResult(BusiStatus.PHONEINVALID);
                }
                nick = list1.get(0).getNick();
                myUid = list1.get(0).getUid();
                myErBanNo = list1.get(0).getErbanNo();
                LOGGER.info("=========手机号：" + phone + "=====存在================");
            }
            //uid nick erban_no
            //或uid  nick   erban_no(phone)
            return wxService.submitPayWx(myUid, chargeProdId, nick, myErBanNo, openId);


        } catch (Exception e) {
            LOGGER.error("============uid=" + myUid + "=======微信公众号下单错误========");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    //获取openID回调接口
    //获取code并请求openID
    @RequestMapping(value = "/codeCallback")
    @ResponseBody
    public void codeCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String code = request.getParameter("code");
        LOGGER.info("=========获取code：" + code + "  =============");
        try {
            String openId = wxPubService.getOpenId(code);
            LOGGER.info("=========获取openId：" + openId + "=============");
            LOGGER.info("====PAY_URL" + WxConfig.payUrl);
            response.sendRedirect(WxConfig.payUrl + "?openId=" + openId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {

        return "test";

    }

    @RequestMapping("/deployTest")
    @ResponseBody
    public String deployTest() {

        return "微信test";

    }
}



