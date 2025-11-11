package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.AuctionDeal;
import com.erban.main.model.OrderServ;
import com.erban.main.model.OrderServExample;
import com.erban.main.model.OrderServExample.Criteria;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.OrderServMapper;
import com.erban.main.param.EndPhoneCallParam;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.statis.StatOrderServService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.OrderServVo;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/6/9.
 */
@Service
public class OrderServService {
    public static final Logger logger = LoggerFactory.getLogger(OrderServService.class);

    @Autowired
    private OrderServMapper orderServMapper;

    @Autowired
    private UserPurseService userPurseService;

    @Autowired
    private AuctionDealService auctionDealService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private StatOrderServService statOrderServService;

    @Autowired
    private BillRecordService billRecordService;

    /**
     * 生成订单 生成订单需要处理押金数据
     *
     * @param auctionDeal
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderServ saveOrderServ(AuctionDeal auctionDeal) {
        OrderServ orderServ = new OrderServ();
        orderServ.setUid(auctionDeal.getDealUid());
        orderServ.setProdUid(auctionDeal.getAuctUid());
        orderServ.setRoomOwnerUid(auctionDeal.getUid());
        orderServ.setOrderType(Constant.OrderServType.auct);
        orderServ.setObjId(auctionDeal.getAuctId());
        orderServ.setCurStatus(Constant.OrderServStatus.create);
        orderServ.setObjId(auctionDeal.getAuctId());
        orderServ.setTotalMoney(auctionDeal.getDealMoney());
        orderServ.setCreateTime(auctionDeal.getDealTime());
        orderServMapper.insert(orderServ);
        return orderServ;
    }

    public BusiResult<OrderServVo> getOrderServListById(Long orderId) {
        OrderServ orderServ = getOrderServById(orderId);
        BusiResult<OrderServVo> busiResult = new BusiResult<OrderServVo>(BusiStatus.SUCCESS);
        if (orderServ == null) {
            busiResult.setData(new OrderServVo());
            return busiResult;
        }
        OrderServVo orderServVo = convertOrderServToVo(orderServ);
        Users prodUser = usersService.getUsersByUid(orderServ.getProdUid());
        Users users = usersService.getUsersByUid(orderServ.getUid());
        orderServVo.setProdImg(prodUser.getAvatar());
        orderServVo.setProdName(prodUser.getNick());
        orderServVo.setUserImg(users.getAvatar());
        orderServVo.setUserName(users.getNick());
        Long reTime = orderServVo.getCreateTime().getTime() - System.currentTimeMillis();
        if (reTime > 259200000) {
            orderServVo.setRemainDay(reTime);
        } else {
            orderServVo.setRemainDay(0L);
        }
        busiResult.setData(orderServVo);
        return busiResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public BusiResult doOrderServ(Long uid, Long orderId, String channelId) {
        OrderServ orderServ = getOrderServByUidOrProdUid(uid, orderId);
        if (orderServ == null) {
            return new BusiResult(BusiStatus.ORDERNOTEXISTS, "订单不存在！");
        }
        if (orderServ.getCurStatus().equals(Constant.OrderServStatus.finish)) {
            return new BusiResult(BusiStatus.ORDERFINISHED, "订单已经服务完成！");
        }
        Date date = new Date();
        orderServ.setCurStatus(Constant.OrderServStatus.ing);
        Date beginDate = orderServ.getBeginServTime();
        if (beginDate == null) {
            orderServ.setBeginServTime(date);
        }
        orderServ.setChannelId(channelId);
        updateOrderServ(orderServ);
        statOrderServService.recordOrderServ(orderServ);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(convertOrderServToVo(orderServ));
        return busiResult;
    }

    /**
     * 处理订单结束通话，网易云结束通话回调
     *
     * @param endPhoneCallParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int endPhoneCall(EndPhoneCallParam endPhoneCallParam) {
        String channelId = endPhoneCallParam.getChannelId();
        if (StringUtils.isEmpty(channelId)) {
            return 200;
        }
        String status = endPhoneCallParam.getStatus();
        String type = endPhoneCallParam.getType();
        Integer duration = endPhoneCallParam.getDuration();
        if (!type.equals("AUDIO")) {
            logger.info("channelId=" + channelId + "&type=" + type + "不处理，直接返回......");
            return 200;
        }
        if (!status.equals("SUCCESS")) {
            logger.info("channelId=" + channelId + "&status=" + status + "不处理，直接返回......");
            return 200;
        }
        if (duration < 1) {
            logger.info("channelId=" + channelId + "&duration=" + duration + "不处理，直接返回......");
            return 200;
        }
        OrderServ orderServ = getOrderServByChannelId(channelId);
        if (orderServ == null) {
            return 200;
        }
        orderServ.setCurStatus(Constant.OrderServStatus.ing);
        updateOrderServ(orderServ);
        statOrderServService.updateStatOrderServ(orderServ);
        logger.info("订单orderId=" + orderServ.getOrderId() + "&channelId=" + channelId + "结束通话处理成功....");
        return 200;
    }

    /**
     * 订单服务结束，更新相关账户金额信息 更新声优账户，更新房主佣金
     *
     * @param orderServ
     */
    public void updateUserPurseByOrderServ(OrderServ orderServ) {
        if (!Constant.OrderServStatus.finish.equals(orderServ.getCurStatus())) {
            return;
        }
        Long totalMoney = orderServ.getTotalMoney();
        Double prodUidDiamondDouble = SystemConfig.appAkira * totalMoney;

        Double roomOwnerDiamondDouble = SystemConfig.roomOwnerAuctOrder * totalMoney;
        getOrderServByObjId(orderServ.getObjId(), orderServ.getOrderType());
        Long prodUid = orderServ.getProdUid();
        AuctionDeal auctionDeal = auctionDealService.getAuctionDealById(orderServ.getObjId());
        if (auctionDeal == null) {
            return;
        }
        Long roomOwnerUid = auctionDeal.getUid();
        userPurseService.updateDiamondByOrderIncome(prodUid, prodUidDiamondDouble);
        billRecordService.insertBillRecord(prodUid, auctionDeal.getDealUid(), orderServ.getObjId(), Constant.BillType.orderIncome, prodUidDiamondDouble, null, null);
        userPurseService.updateDiamondByOrderIncome(roomOwnerUid, roomOwnerDiamondDouble);
        billRecordService.insertBillRecord(roomOwnerUid, auctionDeal.getDealUid(), orderServ.getObjId(), Constant.BillType.roomOwnerIncome, roomOwnerDiamondDouble, null, null);
    }

    public static void main(String args[]) {
        OrderServService orderServService = new OrderServService();
        OrderServ orderServ = orderServService.getOrderServByUid(900308L, 172L);
    }

    private OrderServ getOrderServByChannelId(String channelId) {

        OrderServExample orderServExample = new OrderServExample();
        OrderServExample.Criteria criteria = orderServExample.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        List<OrderServ> orderServList = orderServMapper.selectByExample(orderServExample);
        if (CollectionUtils.isEmpty(orderServList)) {
            return null;
        } else {
            return orderServList.get(0);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BusiResult finishOrderServ(Long uid, Long orderId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        OrderServ orderServ = getOrderServByUid(uid, orderId);
        if (orderServ == null) {
            busiResult.setCode(BusiStatus.ORDERNOTEXISTS.value());
            busiResult.setMessage("该订单不存在");
            return busiResult;
        }
        if (orderServ.getCurStatus() == Constant.OrderServStatus.finish) {
            busiResult.setCode(BusiStatus.ORDERFINISHED.value());
            busiResult.setMessage("该订单已经完成");
            return busiResult;
        }
        Date date = new Date();
        orderServ.setOrderId(orderId);
        orderServ.setFinishTime(date);
        orderServ.setCurStatus(Constant.OrderServStatus.finish);
        updateOrderServ(orderServ);
        return busiResult;
    }

    private void updateOrderServ(OrderServ orderServ) {
        orderServMapper.updateByPrimaryKey(orderServ);
        updateUserPurseByOrderServ(orderServ);
    }

    public OrderServ getOrderServByUid(Long uid, Long orderId) {
        OrderServExample orderServExample = new OrderServExample();
        OrderServExample.Criteria criteria = orderServExample.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andOrderIdEqualTo(orderId);
        List<OrderServ> orderServList = orderServMapper.selectByExample(orderServExample);
        if (CollectionUtils.isEmpty(orderServList)) {
            return null;
        } else {
            return orderServList.get(0);
        }

    }

    public OrderServ getOrderServByObjId(String objId, Byte orderServType) {
        OrderServExample orderServExample = new OrderServExample();
        OrderServExample.Criteria criteria = orderServExample.createCriteria();
        criteria.andObjIdEqualTo(objId);
        criteria.andOrderTypeEqualTo(orderServType);
        List<OrderServ> orderServList = orderServMapper.selectByExample(orderServExample);
        if (CollectionUtils.isEmpty(orderServList)) {
            return null;
        } else {
            return orderServList.get(0);
        }

    }

    public OrderServ getOrderServByUidOrProdUid(Long uid, Long orderId) {
        OrderServExample orderServExample = new OrderServExample();
        OrderServExample.Criteria criteria = orderServExample.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andOrderIdEqualTo(orderId);
        List<OrderServ> orderServList = orderServMapper.selectByExample(orderServExample);
        if (CollectionUtils.isEmpty(orderServList)) {
            OrderServExample orderServExample2 = new OrderServExample();
            OrderServExample.Criteria criteria2 = orderServExample2.createCriteria();
            criteria2.andProdUidEqualTo(uid);
            criteria2.andOrderIdEqualTo(orderId);
            List<OrderServ> orderServList2 = orderServMapper.selectByExample(orderServExample2);
            if (CollectionUtils.isEmpty(orderServList2)) {
                return null;
            } else {
                return orderServList2.get(0);
            }
        } else {
            return orderServList.get(0);
        }
    }

    private OrderServ getOrderServById(Long orderId) {
        OrderServ orderServ = orderServMapper.selectByPrimaryKey(orderId);
        return orderServ;
    }

    public BusiResult<List<OrderServVo>> getOrderServList(Long uid) {
        BusiResult<List<OrderServVo>> busiResult = new BusiResult<List<OrderServVo>>(BusiStatus.SUCCESS);
        List<OrderServVo> orderServVos = Lists.newArrayList();
        List<OrderServ> orderServList = getOrderServListByUid(uid);
        if (CollectionUtils.isEmpty(orderServList)) {
            busiResult.setData(orderServVos);
            return busiResult;
        }
        for (OrderServ orderServ : orderServList) {
            OrderServVo orderServVo = convertOrderServToVo(orderServ);
            Long remainDay = orderServVo.getCreateTime().getTime() + 86400000 - System.currentTimeMillis();
            if (remainDay > 0) {
                orderServVo.setRemainDay(remainDay);
            } else {
                orderServVo.setRemainDay(0L);
            }
            orderServVos.add(orderServVo);
        }
        busiResult.setData(orderServVos);
        return busiResult;
    }

    private List<OrderServ> getOrderServListByUid(Long uid) {
        OrderServExample orderServExample = new OrderServExample();
        OrderServExample.Criteria criteria = orderServExample.createCriteria();
        criteria.andUidEqualTo(uid);
        orderServExample.or().andProdUidEqualTo(uid);
        orderServExample.setOrderByClause(" cur_status asc, create_time desc");
        List<OrderServ> orderServList = orderServMapper.selectByExample(orderServExample);
        return orderServList;
    }

    public OrderServVo convertOrderServToVo(OrderServ orderServ) {
        OrderServVo orderServVo = new OrderServVo();
        orderServVo.setOrderId(orderServ.getOrderId());
        orderServVo.setUid(orderServ.getUid());
        orderServVo.setProdUid(orderServ.getProdUid());
        orderServVo.setObjId(orderServ.getObjId());
        orderServVo.setCurStatus(orderServ.getCurStatus());
        orderServVo.setTotalMoney(orderServ.getTotalMoney());
        orderServVo.setCreateTime(orderServ.getCreateTime());
        orderServVo.setFinishTime(orderServ.getFinishTime());
        Users prodUser = usersService.getUsersByUid(orderServ.getProdUid());
        orderServVo.setProdImg(prodUser.getAvatar());
        orderServVo.setProdName(prodUser.getNick());
        Users users = usersService.getUsersByUid(orderServ.getUid());
        orderServVo.setUserImg(users.getAvatar());
        orderServVo.setUserName(users.getNick());
        return orderServVo;
    }

    public BusiResult evalOrder(Long uid, Long orderId, Integer score, String desc) {
        OrderServ orderServ = getOrderServByUid(uid, orderId);
        BusiResult busiResult = new BusiResult<>(BusiStatus.SUCCESS);
        if (orderServ == null) {
            return new BusiResult(BusiStatus.ORDERNOTEXISTS, "订单不存在！");
        }
        orderServ.setServScore(score);
        orderServ.setServSocreDesc(desc);
        updateOrderEval(orderServ);
        return busiResult;
    }

    private void updateOrderEval(OrderServ orderServ) {
        orderServMapper.updateByPrimaryKey(orderServ);
    }

    // 查询所有的未完成订单。
    public List<OrderServ> getAllUnfinishOrder() {
        OrderServExample orderServExample = new OrderServExample();
        Criteria criteria = orderServExample.createCriteria();
        criteria.andCurStatusNotEqualTo(Constant.OrderServStatus.error).andCurStatusNotEqualTo(Constant.OrderServStatus.finish);
        List<OrderServ> orderServList = orderServMapper.selectByExample(orderServExample);
        if (CollectionUtils.isEmpty(orderServList)) {
            return null;
        }
        return orderServList;
    }

    private List<OrderServVo> convertOrderListToVoList(List<OrderServ> orderServList) {
        List<OrderServVo> orderServVoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(orderServList)) {
            return orderServVoList;
        }
        for (OrderServ orderServ : orderServList) {
            OrderServVo orderServVo = convertOrderServToVo(orderServ);
            orderServVoList.add(orderServVo);
        }
        return orderServVoList;
    }


    public void updateOrderStatus(OrderServ orderServ) {
        updateOrderServ(orderServ);
    }
}
