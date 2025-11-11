package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.erban.main.param.neteasepush.Body;
import com.erban.main.param.neteasepush.NeteaseSendMsgBatchParam;
import com.erban.main.param.neteasepush.Payload;
import com.erban.main.vo.OrderServVo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private SendSysMsgService sendSysMsgService;

    private Gson gson = new Gson();

    public BusiResult getTest() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        List<String> list = Lists.newArrayList();
        list.add("90012");
        list.add("90021");
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(Constant.official.uid.toString());
        neteaseSendMsgBatchParam.setToAccids(list);
        neteaseSendMsgBatchParam.setType(100);
        neteaseSendMsgBatchParam.setPushcontent("你已成功拍下宝贝，请速速密聊去吧~~");
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.DealFinish);
        body.setSecond(Constant.DefMsgType.DealFinishNotice);
        Map<String, String> data = Maps.newHashMap();
        OrderServVo orderServVo = new OrderServVo();
        orderServVo.setCreateTime(new Date());
        orderServVo.setFinishTime(new Date());
        orderServVo.setObjId("1");
        orderServVo.setOrderType((byte) 1);
        orderServVo.setProdImg("1");
        orderServVo.setProdName("1");
        orderServVo.setProdUid(1L);
        orderServVo.setRemainDay(0L);
        orderServVo.setUserImg("1");
        orderServVo.setUserName("1");
        orderServVo.setTotalMoney(1L);
        String orderSerStr = gson.toJson(orderServVo);
        data.put("orderserv", orderSerStr);
        body.setData(data);
        neteaseSendMsgBatchParam.setBody(body);
        data.put("skiproute", String.valueOf(Constant.PayloadRoute.order));
        Payload payload = new Payload();
        payload.setSkiptype(Constant.PayloadSkiptype.apppage);
        payload.setData(data);
        neteaseSendMsgBatchParam.setPayload(payload);
        logger.info("发送订单拍卖成功通知：fromAccid:" + 1 + ",toaccids:" + orderServVo.getUid() + "," + orderServVo.getProdUid());
        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
        return busiResult;
    }
}
