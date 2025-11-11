package com.erban.main.service;

import com.erban.main.model.AuctionCur;
import com.erban.main.model.AuctionCurExample;
import com.erban.main.model.Room;
import com.erban.main.mybatismapper.AuctionCurMapper;
import com.erban.main.param.AuctionCurParam;
import com.erban.main.service.record.AuctionRivalRecordService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.AuctionCurVo;
import com.erban.main.vo.AuctionRivalRecordVo;
import com.google.gson.Gson;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/5/25.
 */
@Service
public class AuctionCurService {
    private static final Logger logger = LoggerFactory.getLogger(AuctionCurService.class);

    @Autowired
    private AuctionCurMapper auctionCurMapper;

    @Autowired
    private AuctionRivalRecordService auctionRivalRecordService;
    @Autowired
    private SendChatRoomMsgService sendChatRoomMsgService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AuctionDealService auctionDealService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JmsTemplate jmsTemplate;

    private Gson gson = new Gson();

    @Transactional(rollbackFor = Exception.class)
    public BusiResult saveStartAuctionCur(AuctionCurParam auctionCurParam) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Long uid = auctionCurParam.getUid();
        // 检查当前房主的房间是否还有未结束（拍卖中1）的拍卖单
        AuctionCur auctionCurDb0 = getAuctionCurByUid(uid);
        if (auctionCurDb0 != null) {
            busiResult.setCode(BusiStatus.AUCTCURDOING.value());
            return busiResult;
        }

        Room room = roomService.getRoomByUid(uid);
        if (room == null) {
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            return busiResult;
        }

        Long auctUid = auctionCurParam.getAuctUid();
        Long auctMoney = auctionCurParam.getAuctMoney();
        Integer servDura = auctionCurParam.getServDura();
        Long minRaiseMoney = auctionCurParam.getMinRaiseMoney();
        String auctDesc = auctionCurParam.getAuctDesc();
        Date date = new Date();
        AuctionCur auctionCur = new AuctionCur();
        auctionCur.setUid(uid);
        auctionCur.setAuctUid(auctUid);
        auctionCur.setServDura(servDura);
        auctionCur.setAuctMoney(auctMoney);
        auctionCur.setMinRaiseMoney(minRaiseMoney);
        auctionCur.setCurStatus(Constant.AuctCurStatus.ing);
        auctionCur.setAuctDesc(auctDesc);
        auctionCur.setCreateTime(date);
        String auctId = UUIDUitl.get();
        auctionCur.setAuctId(auctId);

        // 发送聊天室消息
        AuctionCurVo auctionCurVo = convertAuctionCurVo(auctionCur);
        sendChatRoomMsg(room.getRoomId(), auctUid.toString(), Constant.DefMsgType.Auction,
                Constant.DefMsgType.AuctionStart, auctionCurVo);
        // 保存数据库
        insertAuctionCur(auctionCur);
        //用于对象锁
        AuctLockData.putLockData(auctId);
        return busiResult;
    }

    public void updateAuctionBy(AuctionCur auctionCur) {
        auctionCurMapper.updateByPrimaryKeySelective(auctionCur);
    }

    private void sendChatRoomMsg(Long roomId, String fromAccid, int first, int second, Object data) throws Exception {
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId, fromAccid, first, second, data);
    }

    public void removeAuctionByRoomOwnerLeft(Long uid) {
        AuctionCur auctionCur = getAuctionCurByUid(uid);
        if (auctionCur == null) {
            return;
        }
        deleteAuctionCur(auctionCur.getAuctId());
    }

    @Transactional(rollbackFor = Exception.class)
    public BusiResult finishAuction(Long uid) throws Exception {
        AuctionCur auctionCur = getAuctionCurByUid(uid);
        BusiResult<AuctionCurVo> busiResult = new BusiResult<AuctionCurVo>(BusiStatus.SUCCESS);
        if (auctionCur == null) {
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            return busiResult;
        }
        Room room = roomService.getRoomByUid(uid);
        if (room == null) {
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            return busiResult;
        }
        if (auctionCur.getCurMaxUid() != null && auctionCur.getAuctUid() != null) {// 有人出价
            auctionDealService.saveAuctionDeal(auctionCur);// 有人出价，生成订单
            sendMessage(auctionCur.getUid(), auctionCur.getAuctId(), Constant.ActiveMq.sum_list);
        }
        AuctionCurVo auctionCurVo = getCurrentAuction2ByUid(uid);
        sendChatRoomMsgService.sendSendChatRoomMsg(room.getRoomId(), uid.toString(), Constant.DefMsgType.Auction, Constant.DefMsgType.AuctionFinish, auctionCurVo);
        deleteAuctionCur(auctionCur.getAuctId());
        AuctLockData.removeLockData(auctionCur.getAuctId());
        return busiResult;
    }

    private void deleteAuctionCur(String auctId) {
        auctionCurMapper.deleteByPrimaryKey(auctId);
        deleteAuctionCurCache(auctId);
    }

    private void deleteAuctionCurCache(String auctId) {

    }

    public BusiResult<AuctionCurVo> getCurrentAuctionByUid(Long uid) throws Exception {
        BusiResult<AuctionCurVo> busiResult = new BusiResult<AuctionCurVo>(BusiStatus.SUCCESS);
        AuctionCur auctionCur = getAuctionCurByUid(uid);
        if (auctionCur == null) {
            busiResult.setData(new AuctionCurVo());
            return busiResult;
        }
        AuctionCurVo auctionCurVo = convertAuctionCurVo(auctionCur);
        List<AuctionRivalRecordVo> auctionRivalRecordVoList = auctionRivalRecordService.getRivalRecordListVoByUid(uid);
        auctionCurVo.setRivals(auctionRivalRecordVoList);
        busiResult.setData(auctionCurVo);
        return busiResult;
    }

    public AuctionCurVo getCurrentAuction2ByUid(Long uid) throws Exception {
        AuctionCur auctionCur = getAuctionCurByUid(uid);
        if (auctionCur == null) {
            return null;
        }
        AuctionCurVo auctionCurVo = convertAuctionCurVo(auctionCur);
        List<AuctionRivalRecordVo> auctionRivalRecordVoList = auctionRivalRecordService.getRivalRecordListVoByUid(uid);
        auctionCurVo.setRivals(auctionRivalRecordVoList);
        return auctionCurVo;
    }

    private void insertAuctionCur(AuctionCur auctionCur) {
        auctionCurMapper.insert(auctionCur);
    }

    private void saveAuctionCurCache(AuctionCur auctionCur) {
        String auctionCurStr = gson.toJson(auctionCur);
        jedisService.hwrite(RedisKey.auct_cur.getKey(), auctionCur.getUid().toString(), auctionCurStr);
    }

    private AuctionCur getAuctionCurById(String auctId) {
        AuctionCur auctionCur = auctionCurMapper.selectByPrimaryKey(auctId);
        return auctionCur;
    }

    public AuctionCur getAuctionCurByUid(Long uid) {
        AuctionCurExample example = new AuctionCurExample();
        example.createCriteria().andUidEqualTo(uid).andCurStatusEqualTo(Constant.AuctCurStatus.ing);
        List<AuctionCur> auctionCurList = auctionCurMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(auctionCurList)) {
            return null;
        } else {
            return auctionCurList.get(0);
        }
    }

    private AuctionCurVo convertAuctionCurVo(AuctionCur auctionCur) {
        AuctionCurVo auctionCurVo = new AuctionCurVo();
        auctionCurVo.setAuctId(auctionCur.getAuctId());
        auctionCurVo.setUid(auctionCur.getUid());

        auctionCurVo.setUserVo(usersService.getUserVoByUid(auctionCur.getUid()));
        auctionCurVo.setAuctUid(auctionCur.getAuctUid());
        if(auctionCur.getAuctUid()!=null){
            auctionCurVo.setAuctUserVo(usersService.getUserVoByUid(auctionCur.getAuctUid()));
        }
        auctionCurVo.setUserVo(usersService.getUserVoByUid(auctionCur.getAuctUid()));
        auctionCurVo.setAuctMoney(auctionCur.getAuctMoney());
        auctionCurVo.setServDura(auctionCur.getServDura());
        auctionCurVo.setMinRaiseMoney(auctionCur.getMinRaiseMoney());
        auctionCurVo.setCurMaxUid(auctionCur.getCurMaxUid());
        if(auctionCur.getCurMaxUid()!=null){
            auctionCurVo.setCurMaxUserVo(usersService.getUserVoByUid(auctionCur.getCurMaxUid()));
        }
        auctionCurVo.setCurMaxMoney(auctionCur.getAuctMoney());
        auctionCurVo.setAuctDesc(auctionCur.getAuctDesc());
        auctionCurVo.setCurStatus(auctionCur.getCurStatus());
        auctionCurVo.setCreateTime(auctionCur.getCreateTime());
        auctionCurVo.setDealTime(auctionCurVo.getDealTime());
        return auctionCurVo;
    }

    private void sendMessage(final Long roomUid, final String auctId, final String title) {
        jmsTemplate.send(new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = new ActiveMQMapMessage();
                mapMessage.setObject("roomUid", roomUid);
                mapMessage.setObject("auctId", auctId);
                mapMessage.setObject("title", title);
                return mapMessage;
            }
        });
    }

}
