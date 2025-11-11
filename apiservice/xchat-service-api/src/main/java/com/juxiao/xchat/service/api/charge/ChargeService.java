package com.juxiao.xchat.service.api.charge;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.external.ecpss.bo.OrderCallBackBO;
import com.juxiao.xchat.manager.external.joinpay.vo.JoinpayReciver;
import com.juxiao.xchat.manager.external.sand.vo.SandpayReciver;
import com.juxiao.xchat.service.api.charge.vo.EcpssAlipayVO;
import com.pingplusplus.model.Charge;

/**
 * 充值服务接口
 *
 * @class: ChargeService.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface ChargeService {

    /**
     * 调用ping++充值下单接口
     *
     * @param uid
     * @param chargeProdId
     * @param payChannel
     * @param clientIp
     * @param successUrl
     * @return
     * @throws Exception
     */
    Charge applyCharge(Long uid, String chargeProdId, String payChannel, String clientIp, String successUrl) throws Exception;

    /**
     * 处理ping++的回调订单
     *
     * @param charge
     */
    void reciveCharge(Charge charge) throws Exception;

    /**
     * 首冲大礼包, 方法内会判断是不是首冲
     *
     * @param uid 充值用户
     */
    void firstChargeBag(Long uid);

    void chargeActivity(Long uid, int amount, String chargeId);

    UsersDTO checkUser(Long userNo, Long uid) throws WebServiceException;

    // void initFirstChargeGift(Long uid, int amount, String chargeId);

    Charge webApply(Long userNo, String chargeProdId, String payChannel, String clientIp, String successUrl) throws Exception;

    boolean checkApply(String chargeRecordId);


    Object joinpayCharge(Long uid, String chargeProdId, String payChannel, String clientIp, String successUrl, String openId) throws Exception;

    void reciveJoinpayCharge(JoinpayReciver joinpayReciver) throws Exception;

    Object joinpayWebApply(Long userNo, String chargeProdId, String payChannel, String clientIp, String successUrl, String openId) throws Exception;


    /**
     * 衫德支付-回调
     *
     * @param sandpayReciver
     * @return
     * @throws WebServiceException
     */
    void reciveSandCharge(SandpayReciver sandpayReciver) throws WebServiceException;

    String sandpayCharge(Long uid, String chargeProdId, String clientIp, String payChannel, String successUrl) throws Exception;

    /**
     * 衫德支付-H5支付
     *
     * @param userNo
     * @param chargeProdId
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    String sandWebApply(Long userNo, String chargeProdId, String clientIp, String payChannel, String successUrl) throws Exception;


    /**
     * 汇潮支付-APP支付(都是跳H5支付)
     *
     * @param uid
     * @param chargeProdId
     * @param app
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    EcpssAlipayVO ecpssApply(Long uid, String chargeProdId, String app, String clientIp) throws WebServiceException;

    /**
     * 汇潮支付-H5支付
     *
     * @param userNo
     * @param chargeProdId
     * @param clientIp
     * @return
     * @throws WebServiceException
     */
    EcpssAlipayVO ecpssWebApply(Long userNo, String chargeProdId, String clientIp) throws WebServiceException;

    /**
     * 汇潮支付-回调
     *
     * @param callBackBO
     * @return
     * @throws WebServiceException
     */
    String reciveEcpssCharge(OrderCallBackBO callBackBO) throws WebServiceException;


    /**
     * 用户当前充值金额
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    Integer getUserCharge(Long uid) throws WebServiceException;

    /**
     * 模拟充值接口
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    void testCharge(Long uid, String chargeProdId) throws WebServiceException;
}
