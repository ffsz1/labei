package com.juxiao.xchat.api.controller.charge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.charge.ChargeService;
import com.pingplusplus.model.Charge;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("pingxx")
@Api(description = "其他接口", tags = "其他")
public class PingxxReciverController {
    public static final Logger logger = LoggerFactory.getLogger(PingxxReciverController.class);
    @Autowired
    private ChargeService chargeService;

    @ApiOperation(value = "ping++支付回调", hidden = true, tags = "charge")
    @RequestMapping(value = "callback", method = {RequestMethod.POST})
    public WebServiceMessage pingxxCallBack(HttpServletRequest request, HttpServletResponse response) {
        String requestBody = null;
        WebServiceMessage message;
        try {
            // 获取请求体
            requestBody = HttpServletUtils.readString(request);
            JSONObject json = JSON.parseObject(requestBody);
            JSONObject data = json.getJSONObject("data");
            JSONObject object = data.getJSONObject("object");
            Charge charge = this.getCharge(object);
            chargeService.reciveCharge(charge);
            message = WebServiceMessage.success(WebServiceCode.SUCCESS);
        } catch (WebServiceException e) {
            response.setStatus(500);
            message = WebServiceMessage.failure(e);
        } catch (Exception e) {
            response.setStatus(500);
            message = WebServiceMessage.failure(WebServiceCode.SERVER_ERROR);
        }
        logger.info("[ ping++回调 ] 请求:>{}，返回:>{}", requestBody, message.getCode());
        return message;
    }

    private Charge getCharge(JSONObject object) {
        Charge charge = new Charge();
        charge.setId(object.getString("id"));
        charge.setObject(object.getString("object"));
        charge.setPaid(object.containsKey("paid") && object.getBooleanValue("paid"));
        charge.setLivemode(object.containsKey("livemode") && object.getBooleanValue("livemode"));
        charge.setRefunded(object.containsKey("refunded") && object.getBooleanValue("refunded"));
        charge.setReversed(object.containsKey("reversed") && object.getBooleanValue("reversed"));
        charge.setApp(object.getString("app"));
        charge.setChannel(object.getString("channel"));
        charge.setOrderNo(object.getString("order_no"));
        charge.setClientIp(object.getString("client_ip"));
        charge.setAmount((int) (object.containsKey("amount") ? object.getDoubleValue("amount") : 0.0));
        charge.setAmountSettle((int) (object.containsKey("amount_settle") ? object.getDoubleValue("amount_settle") : 0.0));
        charge.setCurrency(object.getString("currency"));
        charge.setSubject(object.getString("subject"));
        charge.setBody(object.getString("body"));
        charge.setTimePaid((long) (object.containsKey("time_paid") ? object.getDoubleValue("time_paid") : 0.0));
        charge.setTimeExpire((long) (object.containsKey("time_expire") ? object.getDoubleValue("time_expire") : 0.0));
        return charge;
    }
}
