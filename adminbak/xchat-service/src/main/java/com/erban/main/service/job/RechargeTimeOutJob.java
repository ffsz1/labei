package com.erban.main.service.job;

import com.erban.main.model.ChargeRecord;
import com.erban.main.service.ChargeRecordService;
import com.erban.main.service.ChargeService;
import com.erban.main.service.wx.WxService;
import com.xchat.common.constant.Constant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yanghaoyu 充值金币超时检查
 */
public class RechargeTimeOutJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RechargeTimeOutJob.class);

    @Autowired
    private ChargeRecordService chargeRecordService;

    @Autowired
    private ChargeService chargeService;
    @Autowired
    private WxService wxService;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("正在检查充值是否超时");
        List<ChargeRecord> allUnRecharges = chargeRecordService.getAllUnRecharge();
        for (ChargeRecord chargeRecord : allUnRecharges) {
            long createTime = chargeRecord.getCreateTime().getTime();
            long nowTime = System.currentTimeMillis();
            boolean falg = nowTime - createTime > 120000 ? true : false;
            try {
                if (falg) {
                    if (chargeRecord.getChannel().equals("wx_pub")) {
                        // 微信判断是否超时回调
                        //1.判断订单是不是处于未支付状态
                        if (chargeRecord.getChargeStatus().equals(Constant.ChargeRecordStatus.create)) {
                            boolean chargeFalg = wxService.getRecharge(chargeRecord);

                            if (!chargeFalg) {
                                logger.info("----------微信公众号支付有误或者长时间为支付----------");
                                chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.error);
                                chargeRecordService.UpdateChargeRecord(chargeRecord);
                            }
                        }
                    } else if (chargeRecord.getChannel().equals("alipay")) {
                        // 支付宝回调
                        boolean chargeFalg = chargeService.getRechargeByAliId(chargeRecord.getChargeRecordId(),
                                chargeRecord.getPingxxChargeId());
                        if (!chargeFalg) {
                            chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.error);
                            chargeRecordService.UpdateChargeRecord(chargeRecord);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("获取用户充值信息失败,充值用户uid=" + chargeRecord.getUid() + ",chargeRecordId="
                        + chargeRecord.getChargeRecordId() + ",错误信息：" + e.getMessage());
            }
        }
    }

}
