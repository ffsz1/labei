package com.erban.admin.web.controller.drawmoney;


import com.erban.admin.main.service.drawmoney.DiamondWithDrawAdminService;
import com.erban.admin.main.service.drawmoney.RedPacketWithDrawAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.vo.admin.JoinpayRet;
import com.erban.main.vo.admin.JoinpayTransferCallback;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.pingplusplus.model.Transfer;
import com.xchat.common.result.BusiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台管理-提现controller
 */
@Controller
@RequestMapping("/yingtao/withdraw")
public class TransferCallBackController extends BaseController {
    @Autowired
    private DiamondWithDrawAdminService diamondWithDrawAdminService;

    @Autowired
    private RedPacketWithDrawAdminService redPacketWithDrawAdminService;
    private Gson gson=new Gson();

    /**
     * 转账成功回调
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/transferCallback/success", method = {RequestMethod.POST})
    @ResponseBody
    public BusiResult transferSuccess(HttpServletRequest request)throws Exception {
        logger.info("---------------transfer success CallBack begin-------------------");
        BusiResult busiResult = null;
        try {
            // 获取请求体
            byte[] body = readBody(request);
            if (body == null) {
                logger.error("转账成功回调,入参为null");
                throw  new RuntimeException("转账成功回调,入参为null");
            }
            //成功回调
            busiResult = diamondWithDrawAdminService.updateTransfer(getTransfer(body), 1);
            logger.info("---------------transfer success CallBack end-------------------");
            return busiResult;
        } catch (Exception ex) {
            logger.error("转账成功回调异常，报错：{}", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * 转账失败回调
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/transferCallback/fail", method = {RequestMethod.POST})
    @ResponseBody
    public BusiResult transferFail(HttpServletRequest request)throws Exception {
        logger.info("---------------transfer fail CallBack begin-------------------");
        BusiResult busiResult = null;
        try {
            // 获取请求体
            byte[] body = readBody(request);
            if (body == null) {
                logger.error("转账失败回调,入参为null");
                throw  new RuntimeException("转账失败回调,入参为null");
            }
            //失败回调
            busiResult = diamondWithDrawAdminService.updateTransfer(getTransfer(body), 0);
            logger.info("---------------transfer fail CallBack end-------------------");
            return busiResult;
        } catch (Exception ex) {
            logger.error("转账失败回调异常，报错：{}", ex);
            throw new RuntimeException(ex);
        }
    }

    private Transfer getTransfer(byte[] body) throws Exception {
        String requestBody = new String(body, "utf-8");
        Map<String, Object> requstMap = gson.fromJson(requestBody, HashMap.class);
        LinkedTreeMap data = (LinkedTreeMap) requstMap.get("data");
        LinkedTreeMap dataMap = (LinkedTreeMap) data.get("object");

        Transfer transfer=new Transfer();
        String id=(String)dataMap.get("id");
        String object=(String)dataMap.get("object");
        boolean livemode=dataMap.get("livemode")==null?false:(boolean)dataMap.get("livemode");
        String app=(String)dataMap.get("app");
        String channel=(String)dataMap.get("channel");
        String order_no=(String)dataMap.get("order_no");
        Double amount=(Double)dataMap.getOrDefault("amount", 0.0);
        Double amount_settle=(Double)dataMap.getOrDefault("amount_settle", 0.0);
        String currency=(String)dataMap.get("currency");
        String type=(String)dataMap.get("type");
        String status=(String)dataMap.get("status");
        transfer.setId(id);
        transfer.setObject(object);
        transfer.setType(type);
        transfer.setLivemode(livemode);
        transfer.setApp(app);
        transfer.setChannel(channel);
        transfer.setOrderNo(order_no);
        transfer.setAmount(amount.intValue());
        transfer.setAmountSettle(amount_settle.intValue());
        transfer.setCurrency(currency);
        transfer.setStatus(status);
        return transfer;
    }

    /**
     * 汇聚转账回调
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/joinpay/transferCallback", method = {RequestMethod.POST})
    @ResponseBody
    public JoinpayRet transferCallback(HttpServletRequest request)throws Exception {
        try {
            diamondWithDrawAdminService.updateJoinpayTransfer(gson.fromJson(new String(readBody(request), "utf-8"), JoinpayTransferCallback.class));
            JoinpayRet joinpayRet = new JoinpayRet();
            joinpayRet.setStatusCode("2001");
            joinpayRet.setMessage("成功");
            return joinpayRet;
        } catch (Exception ex) {
            logger.error("转账成功回调异常，报错：{}", ex);
            throw new RuntimeException(ex);
        }
    }


    /**
     * 汇聚转账回调
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/joinpay/transferRedPackageCallback", method = {RequestMethod.POST})
    @ResponseBody
    public JoinpayRet transferRedPackageCallback(HttpServletRequest request)throws Exception {
        try {
            redPacketWithDrawAdminService.updateJoinpayTransfer(gson.fromJson(new String(readBody(request), "utf-8"), JoinpayTransferCallback.class));
            JoinpayRet joinpayRet = new JoinpayRet();
            joinpayRet.setStatusCode("2001");
            joinpayRet.setMessage("成功");
            return joinpayRet;
        } catch (Exception ex) {
            logger.error("转账成功回调异常，报错：{}", ex);
            throw new RuntimeException(ex);
        }
    }
}
