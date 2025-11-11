package com.erban.web.controller;

import com.erban.main.model.ChargeAppleRecord;
import com.erban.main.model.ChargeAppleRecordExample;
import com.erban.main.mybatismapper.ChargeAppleRecordMapper;
import com.erban.main.service.ChargeRecordUpdateService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.ReceiptVo;
import com.google.gson.Gson;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.wx.MD5;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by Lily on 2017/7/10.
 */
@Controller
@RequestMapping("/verify")
public class IOSVerifyController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IOSVerifyController.class);

    //购买凭证验证地址
    private static final String certificateUrl = "https://buy.itunes.apple.com/verifyReceipt";

    //测试的购买凭证验证地址
    private static final String certificateUrlTest = "https://sandbox.itunes.apple.com/verifyReceipt";

    @Autowired
    private ChargeRecordUpdateService chargeRecordUpdateService;
    @Autowired
    private ChargeAppleRecordMapper chargeAppleRecordMapper;


    /**
     * 接收iOS端发过来的购买凭证
     *
     * @param uid
     * @param receipt   （base64在客户端已进行编码操作，不需要再进行base64编码）
     * @param chooseEnv true - 购买状态（正式），false-沙盒
     */
    @ResponseBody
    @Authorization
    @RequestMapping(value = "/setiap", method = RequestMethod.POST)
    public BusiResult setIapCertificate(String uid, String chargeRecordId, String chooseEnv, String receipt) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        LOGGER.debug("uid = " + uid + " 二次验证的订单号为 = " + chargeRecordId
                + " receipt = " + receipt);
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(receipt)) {
            LOGGER.debug("输入参数有误");
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        // 校验唯一receipt
        String receiptmd5 = MD5.MD5Encode(receipt);
        ChargeAppleRecordExample chargeAppleRecordExample = new ChargeAppleRecordExample();
        chargeAppleRecordExample.createCriteria().andReceipEqualTo(receiptmd5);
        List<ChargeAppleRecord> chargeAppleRecordList = chargeAppleRecordMapper.selectByExample(chargeAppleRecordExample);
        if(chargeAppleRecordList!=null&&chargeAppleRecordList.size()>0){
            return new BusiResult(BusiStatus.INVALID_SERVICE);
        }

        //环境测试处理
        //TODO 正式环境失败后还要进行沙盒环境（增加）
        String url = chooseEnv.equals("true") ? certificateUrl : certificateUrlTest;
        if("1000004".equals(uid)){
            url = certificateUrlTest;
        }
        LOGGER.info("选择测试环境，对应URL为：" + url);
                /* String receiptData ="{\n" +
                        "\t\"receipt-data\":\""+certificateCode +"\"}" ;
                String reData = HttpUtils.sendPost ( url, receiptData );*/
//                String code ="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";
        String data = chargeRecordUpdateService.sendHttpsCoon(url, receipt);
        LOGGER.info("接收apple服务器：" + data);
        Gson gson = new Gson();
        ReceiptVo receiptVo = gson.fromJson(data, ReceiptVo.class);
        if(!"com.yingtao.ios".equals(receiptVo.getReceipt().get("bundle_id"))){
            return new BusiResult(BusiStatus.INVALID_SERVICE);
        }
        if (chooseEnv.equals("true")) {
            //正式环境测试失败
            if (receiptVo.getStatus() != 0) {
                //进行测试环境
                LOGGER.info("正式环境下验证失败，将进行沙盒环境下的验证");
                String reData2 = chargeRecordUpdateService.sendHttpsCoon(certificateUrlTest, receipt);
                busiResult = chargeRecordUpdateService.chargeRecordUpdate(uid, chargeRecordId, reData2, receiptmd5);
                return busiResult;
            }
        }
        LOGGER.info("已沙盒环境下进行验证");
        busiResult = chargeRecordUpdateService.chargeRecordUpdate(uid, chargeRecordId, data, receiptmd5);

        return busiResult;
    }

}
