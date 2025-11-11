package com.erban.main.service.job;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.erban.main.model.OrderServ;
import com.erban.main.service.OrderServService;
import com.xchat.common.constant.Constant;

/**
 * @author 杨浩宇 关闭超时订单
 */

public class OrderTimeoutJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CheckRoomExceptionJob.class);

    @Autowired
    private OrderServService orderServService;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("正在查询过时订单信息");
        List<OrderServ> orderList = orderServService.getAllUnfinishOrder();
        if (orderList == null) {
            return;
        }
        for (OrderServ orderServ : orderList) {
            long createTime = orderServ.getCreateTime().getTime();
            long nowDate = System.currentTimeMillis();
            // 判断订单完成时间是否超过当前时间一天。
            boolean falg = nowDate - createTime > 86400000 ? true : false;
            if (falg) {
                logger.info("订单已过期:orderId=" + orderServ.getOrderId());
                orderServ.setCurStatus(Constant.OrderServStatus.finish);
                orderServ.setFinishTime(new Date());
                orderServService.updateOrderStatus(orderServ);
            }
        }
    }

}
