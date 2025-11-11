package com.erban.main.service.record;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.AuctionRivalRecordMapper;
import com.erban.main.service.AuctionCurService;
import com.erban.main.service.DepositService;
import com.erban.main.service.SendChatRoomMsgService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.AuctionCurVo;
import com.erban.main.vo.AuctionRivalRecordVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/5/25.
 */
@Service
public class AuctionRivalRecordService {
    private static final Logger logger = LoggerFactory.getLogger(AuctionRivalRecordService.class);

    @Autowired
    private AuctionRivalRecordMapper auctionRivalRecordMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AuctionCurService auctionCurService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private SendChatRoomMsgService sendChatRoomMsgService;

    @Autowired
    private JedisService jedisService;
    @Autowired
    private UsersService usersService;


    @Transactional(rollbackFor = Exception.class)
    public synchronized BusiResult saveOrUpdateAuctionRivalRecord(Long uid, Long roomUid, String auctId, Long auctMoney) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Room room = roomService.getRoomByUid(roomUid);
        if (room == null) {
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            return busiResult;
        }
        AuctionCur auctionCur = auctionCurService.getAuctionCurByUid(roomUid);
        String indentifier = jedisService.lockWithTimeout(auctionCur.getUid().longValue() + "auctionUp", 5000, 1000);
        if (auctionCur == null || !auctionCur.getAuctId().equals(auctId)) {
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            return busiResult;
        }
        if (auctionCur.getAuctUid().equals(uid)) {
            busiResult.setCode(BusiStatus.AUCTCURYOURSELFERROR.value());
            return busiResult;
        }
        Long curMaxMoney = auctionCur.getCurMaxMoney();
        if (curMaxMoney == null) {
            curMaxMoney = auctionCur.getAuctMoney() - 1;
        }
        if (curMaxMoney >= auctMoney) {
            busiResult.setCode(BusiStatus.AUCTCURLESSTHANMAXMONEY.value());
            busiResult.setMessage("低于最高价格");
            return busiResult;
        }
        //判断账户余额是否足够同时扣入预扣款（押金）账户
        boolean depositResult = depositService.updateOrInsertDeposit(uid, auctionCur.getAuctId(), Constant.DepositUseType.auct, auctMoney);
        if (!depositResult) {
            busiResult.setCode(BusiStatus.PURSEMONEYNOTENOUGH.value());
            busiResult.setMessage("账户余额不足，请充值");
            return busiResult;
        }
        AuctionRivalRecord auctionRivalRecord = getAuctionRivalRecordByUid(auctId, uid);
        if (null == auctionRivalRecord) {
            String rivalId = UUIDUitl.get();
            auctionRivalRecord = new AuctionRivalRecord();
            auctionRivalRecord.setRivalId(rivalId);
            auctionRivalRecord.setUid(uid);
            auctionRivalRecord.setAuctId(auctId);
            auctionRivalRecord.setCreateTime(new Date());
            auctionRivalRecord.setAuctMoney(auctMoney);
            insertAuctionRivalRecord(auctionRivalRecord);
        } else {
            auctionRivalRecord.setAuctMoney(auctMoney);
            updateAuctionRivalRecord(auctionRivalRecord);
        }
        auctionCur.setAuctId(auctId);
        auctionCur.setCurMaxMoney(auctMoney);
        auctionCur.setCurMaxUid(uid);
        auctionCurService.updateAuctionBy(auctionCur);
        AuctionCurVo auctionCurVo = auctionCurService.getCurrentAuction2ByUid(roomUid);
        sendChatRoomMsg(room.getRoomId(), uid.toString(), Constant.DefMsgType.Auction, Constant.DefMsgType.AuctionUpdate, auctionCurVo);
        jedisService.releaseLock(auctionCur.getUid().longValue() + "auctionUp", indentifier);
        return busiResult;
    }

    private void sendChatRoomMsg(Long roomId, String fromAccid, int first, int second, Object data) throws Exception {
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId, fromAccid, first, second, data);
    }

    public BusiResult<List<AuctionRivalRecordVo>> getRivalRecordListByUid(Long uid) throws Exception {
        BusiResult<List<AuctionRivalRecordVo>> busiResult = new BusiResult(BusiStatus.SUCCESS);
        List<AuctionRivalRecordVo> auctionRivalRecordVoList = getRivalRecordListVoByUid(uid);
        busiResult.setData(auctionRivalRecordVoList);
        return busiResult;

    }

    public List<AuctionRivalRecordVo> getRivalRecordListVoByUid(Long uid) throws Exception {
        List<AuctionRivalRecordVo> auctionRivalRecordVoList = Lists.newArrayList();

        List<AuctionRivalRecord> auctionRivalRecordList = getRivalRecordList(uid);

        if (CollectionUtils.isEmpty(auctionRivalRecordList)) {
            return auctionRivalRecordVoList;
        }
        for (AuctionRivalRecord auctionRivalRecord : auctionRivalRecordList) {
            AuctionRivalRecordVo auctionRivalRecordVo = convertToAuctionRivalRecordVo(auctionRivalRecord);
            auctionRivalRecordVoList.add(auctionRivalRecordVo);
        }

        return auctionRivalRecordVoList;
    }

    public List<AuctionRivalRecord> getRivalRecordList(Long uid) throws Exception {
        AuctionCur auctionCur = auctionCurService.getAuctionCurByUid(uid);
        if (auctionCur == null || auctionCur.getCurMaxUid() == null) {
            return Lists.newArrayList();
        }

        String auctId = auctionCur.getAuctId();
        AuctionRivalRecordExample auctionRivalRecordExample = new AuctionRivalRecordExample();
        auctionRivalRecordExample.setOrderByClause(" auct_money desc");
        auctionRivalRecordExample.createCriteria().andAuctIdEqualTo(auctId);
        List<AuctionRivalRecord> auctionRivalRecordList = auctionRivalRecordMapper.selectByExample(auctionRivalRecordExample);
        return auctionRivalRecordList;

    }

    public List<AuctionRivalRecord> getRivalRecordList(AuctionCur auctionCur) throws Exception {
        if (auctionCur == null || auctionCur.getCurMaxUid() == null) {
            return Lists.newArrayList();
        }
        String auctId = auctionCur.getAuctId();
        AuctionRivalRecordExample auctionRivalRecordExample = new AuctionRivalRecordExample();
        auctionRivalRecordExample.setOrderByClause(" auct_money desc");
        auctionRivalRecordExample.createCriteria().andAuctIdEqualTo(auctId);
        List<AuctionRivalRecord> auctionRivalRecordList = auctionRivalRecordMapper.selectByExample(auctionRivalRecordExample);
        return auctionRivalRecordList;

    }

    private AuctionRivalRecordVo convertToAuctionRivalRecordVo(AuctionRivalRecord auctionRivalRecord) {
        AuctionRivalRecordVo auctionRivalRecordVo = new AuctionRivalRecordVo();
        auctionRivalRecordVo.setRivalId(auctionRivalRecord.getRivalId());
        auctionRivalRecordVo.setAuctMoney(auctionRivalRecord.getAuctMoney());
        auctionRivalRecordVo.setUid(auctionRivalRecord.getUid());
        auctionRivalRecordVo.setUserVo(usersService.getUserVoByUid(auctionRivalRecord.getUid()));
        auctionRivalRecordVo.setAuctId(auctionRivalRecord.getAuctId());
        return auctionRivalRecordVo;
    }

    private AuctionRivalRecord getAuctionRivalRecordByUid(String auctId, Long uid) {
        AuctionRivalRecordExample auctionRivalRecordExample = new AuctionRivalRecordExample();
        AuctionRivalRecordExample.Criteria criteria = auctionRivalRecordExample.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andAuctIdEqualTo(auctId);
        List<AuctionRivalRecord> auctionRivalRecordList = auctionRivalRecordMapper.selectByExample(auctionRivalRecordExample);
        if (CollectionUtils.isEmpty(auctionRivalRecordList)) {
            return null;
        } else {
            return auctionRivalRecordList.get(0);
        }
    }

    private void insertAuctionRivalRecord(AuctionRivalRecord auctionRivalRecord) {
        auctionRivalRecordMapper.insert(auctionRivalRecord);
    }

    private void updateAuctionRivalRecord(AuctionRivalRecord auctionRivalRecord) {
        auctionRivalRecordMapper.updateByPrimaryKeySelective(auctionRivalRecord);
    }


}
