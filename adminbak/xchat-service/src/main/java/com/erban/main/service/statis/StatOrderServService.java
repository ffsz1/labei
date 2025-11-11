package com.erban.main.service.statis;

import com.erban.main.model.OrderServ;
import com.erban.main.model.StatOrderServ;
import com.erban.main.model.StatOrderServExample;
import com.erban.main.mybatismapper.StatOrderServMapper;
import com.erban.main.vo.StatOrderServVo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class StatOrderServService {
    private static final Logger logger = LoggerFactory.getLogger(StatOrderServService.class);

    @Autowired
    private StatOrderServMapper statOrderServMapper;


    public void recordOrderServ(OrderServ orderServ) {
        try {
            if (orderServ == null) {
                logger.info("订单记录异常.");
            }
            StatOrderServ statOrderServ = statOrderServMapper.selectByPrimaryKey(orderServ.getOrderId());
            if (statOrderServ == null) {
                statOrderServ = new StatOrderServ();
                statOrderServ.setOrderId(orderServ.getOrderId());
                statOrderServ.setChatNum(1);
                statOrderServ.setProdId(orderServ.getProdUid());
                statOrderServ.setUid(orderServ.getUid());
                statOrderServ.setCreateTime(new Date());
                statOrderServMapper.insertSelective(statOrderServ);
            } else {
                statOrderServ.setChatNum(statOrderServ.getChatNum() + 1);
                statOrderServ.setCreateTime(new Date());
                statOrderServMapper.updateByPrimaryKeySelective(statOrderServ);
            }
        } catch (Exception e) {
            logger.info("记录基础订单数据失败orderId:" + orderServ.getOrderId());
        }
    }

    public void updateStatOrderServ(OrderServ orderServ) {
        try {
            StatOrderServ statOrderServ = statOrderServMapper.selectByPrimaryKey(orderServ.getOrderId());
            if (statOrderServ == null) {
                logger.info("记录基础订单数据失败orderId:" + orderServ.getOrderId());
            }
            Long chatTime = statOrderServ.getChatTime();
            if (chatTime == null) {
                chatTime = System.currentTimeMillis() - statOrderServ.getCreateTime().getTime();
            } else {
                chatTime = (System.currentTimeMillis() - statOrderServ.getCreateTime().getTime()) + chatTime;
            }
            statOrderServ.setChatTime(chatTime);
        } catch (Exception e) {
            logger.info("记录基础订单数据失败orderId:" + orderServ.getOrderId());
        }
    }

    public BusiResult getAvgChatTime() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatOrderServExample example = new StatOrderServExample();
        example.createCriteria().andChatTimeIsNotNull();
        List<StatOrderServ> statOrderServList = statOrderServMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statOrderServList)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        int chatNum = 0;
        long chatTime = 0;
        for (StatOrderServ statOrderServ : statOrderServList) {
            chatNum += statOrderServ.getChatNum();
            chatTime += statOrderServ.getChatTime();
        }
        long avgChatTime = (chatTime / chatNum) / 60000;
        StatOrderServVo statOrderServVo = new StatOrderServVo();
        statOrderServVo.setAvgChatTime(avgChatTime);
        busiResult.setData(statOrderServVo);
        return busiResult;
    }

    public BusiResult getChatTime(Long orderId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatOrderServ statOrderServ = statOrderServMapper.selectByPrimaryKey(orderId);
        if (statOrderServ == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        StatOrderServVo statOrderServVo = new StatOrderServVo();
        long chatTime = statOrderServ.getChatTime().longValue() / 60000;
        statOrderServVo.setChatTime(chatTime);
        statOrderServVo.setChatNum(statOrderServ.getChatNum());
        busiResult.setData(statOrderServVo);
        return busiResult;
    }

    public BusiResult getOrderProp() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatOrderServExample example = new StatOrderServExample();
        example.createCriteria().andChatNumIsNotNull();
        List<StatOrderServ> statOrderServList = statOrderServMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statOrderServList)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        int oneChat = 0;
        int moreChat = 0;
        for (StatOrderServ statOrderServ : statOrderServList) {
            if (statOrderServ.getChatNum() > 1) {
                moreChat += 1;
            } else {
                oneChat += 1;
            }
        }
        StatOrderServVo statOrderServVo = new StatOrderServVo();
        statOrderServVo.setOneChat(oneChat);
        statOrderServVo.setMoreChat(moreChat);
        busiResult.setData(statOrderServVo);
        return busiResult;
    }
}
