package com.erban.main.service.job;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.OrderServ;
import com.erban.main.param.neteasepush.Body;
import com.erban.main.param.neteasepush.NeteaseSendMsgBatchParam;
import com.erban.main.param.neteasepush.Payload;
import com.erban.main.service.OrderServService;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.vo.OrderServVo;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 密聊即将过期发送系统推送定时任务
 *
 * @author yanghaoyu
 */
public class ChannelTimeOutJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ChannelTimeOutJob.class);

    @Autowired
    private OrderServService orderServService;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    private Gson gson = new Gson();

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("正在执行密聊过期任务");
        List<OrderServ> orderServList = orderServService.getAllUnfinishOrder();
        if (orderServList == null) {
            return;
        }
        for (OrderServ orderServ : orderServList) {
            Date orderTime = orderServ.getCreateTime();
            if (System.currentTimeMillis() - orderTime.getTime() > 43200000 && System.currentTimeMillis() - orderTime.getTime() < 46800000) {
                logger.info("当前订单时间超过12小时,用户uid" + orderServ.getUid());
                sendMsgByOrderTime(orderServ, Constant.OrderTimeOutType.oneTimeOut);
            } else if (System.currentTimeMillis() - orderTime.getTime() > 82800000 && System.currentTimeMillis() - orderTime.getTime() < 86400000) {
                logger.info("当前订单时间超过23小时,用户uid" + orderServ.getUid());
                sendMsgByOrderTime(orderServ, Constant.OrderTimeOutType.towTimeOut);
            }
        }
    }

    private void sendMsgByOrderTime(OrderServ orderServ, int type) {
        OrderServVo orderServVo = orderServService.convertOrderServToVo(orderServ);
        Long remainDay = orderServVo.getCreateTime().getTime() + 86400000 - System.currentTimeMillis();
        if (remainDay > 0) {
            orderServVo.setRemainDay(remainDay);
        } else {
            orderServVo.setRemainDay(0L);
        }
        List<String> list = Lists.newArrayList();
        list.add(orderServ.getUid().toString());
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(Constant.official.uid.toString());
        neteaseSendMsgBatchParam.setToAccids(list);
        neteaseSendMsgBatchParam.setType(100);
        if (type == Constant.OrderTimeOutType.oneTimeOut) {
            neteaseSendMsgBatchParam.setPushcontent("您的密聊时间只剩最后12小时，快去撩吧~");
        } else {
            neteaseSendMsgBatchParam.setPushcontent("您的密聊时间只剩最后1小时，快去撩吧!");
        }
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.OrderReTime);
        body.setSecond(Constant.DefMsgType.OrderReTimeNotice);
        Map<String, String> data = com.beust.jcommander.internal.Maps.newHashMap();
        String orderSerStr = gson.toJson(orderServVo);
        data.put("orderser", orderSerStr);
        body.setData(data);
        neteaseSendMsgBatchParam.setBody(body);
        data.put("skiproute", String.valueOf(Constant.PayloadRoute.call));
        Payload payload = new Payload();
        payload.setSkiptype(Constant.PayloadSkiptype.apppage);
        payload.setData(data);
        neteaseSendMsgBatchParam.setPayload(payload);
        logger.info("发送密聊过期通知：fromAccid:" + orderServ.getRoomOwnerUid() + ",toaccids:" + orderServ.getUid() + "," + orderServ.getProdUid());
//        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
    }
}
