package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.PacketWithDrawRecordMapper;
import com.erban.main.mybatismapper.WithDrawPacketCashProdMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UserPacketRecordService;
import com.erban.main.service.user.UserPacketService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.PacketWithDrawRecordVo;
import com.erban.main.vo.RedPacketVo;
import com.erban.main.vo.UserVo;
import com.erban.main.vo.WithDrawPacketCashProdVo;
import com.google.common.reflect.TypeToken;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.*;

/**
 * 红包提现service
 */
@Service
public class RedPacketWithDrawService extends BaseService {

    @Autowired
    private WithDrawPacketCashProdMapper withDrawPacketCashProdMapper;
    @Autowired
    private PacketWithDrawRecordMapper packetWithDrawRecordMapper;
    @Autowired
    private UserPacketRecordService userPacketRecordService;
    @Autowired
    private UserPacketService userPacketService;
    @Autowired
    private PacketWithDrawRecordService packetWithDrawRecordService;
    @Autowired
    private UsersService usersService;

    public BusiResult findWithDrawList() throws Exception {
        List<WithDrawPacketCashProd> list = new ArrayList<>();
        List<WithDrawPacketCashProdVo> voList = new ArrayList<>();
        String ListStr = jedisService.read(RedisKey.packet_withdraw_cash_list.getKey());
        if (!StringUtils.isEmpty(ListStr)) {
            Type type = new TypeToken<List<WithDrawPacketCashProdVo>>() {
            }.getType();
            voList = gson.fromJson(ListStr, type);
        } else {
            list = findList();
            /* 将查询到的提现列表存入redis中 */
            voList = convertWithDrawPacketCashProdListToVoList(list);
            Collections.sort(voList);
            String voListStr = gson.toJson(voList);
            jedisService.set(RedisKey.packet_withdraw_cash_list.getKey(), voListStr);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(voList);
        busiResult.setCode(200);
        return busiResult;
    }

    private List<WithDrawPacketCashProdVo> convertWithDrawPacketCashProdListToVoList(List<WithDrawPacketCashProd> list) {
        List<WithDrawPacketCashProdVo> voList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return voList;
        }
        for (WithDrawPacketCashProd withDrawPacketCashProd : list) {
            WithDrawPacketCashProdVo withDrawPacketCashProdVo = convertWithDrawPacketCashProdToVo(withDrawPacketCashProd);
            voList.add(withDrawPacketCashProdVo);
        }
        return voList;
    }

    private WithDrawPacketCashProdVo convertWithDrawPacketCashProdToVo(WithDrawPacketCashProd withDrawPacketCashProd) {
        WithDrawPacketCashProdVo withDrawPacketCashProdVo = new WithDrawPacketCashProdVo();
        withDrawPacketCashProdVo.setPacketId(withDrawPacketCashProd.getPacketProdCashId());
        withDrawPacketCashProdVo.setPacketNum(withDrawPacketCashProd.getPacketNum());
        withDrawPacketCashProdVo.setProdStauts(withDrawPacketCashProd.getProdStauts());
        withDrawPacketCashProdVo.setSeqNo(withDrawPacketCashProd.getSeqNo());
        return withDrawPacketCashProdVo;
    }

    private List<WithDrawPacketCashProd> findList() {
        WithDrawPacketCashProdExample example = new WithDrawPacketCashProdExample();
        example.createCriteria().andProdStautsEqualTo(Constant.PacketProStauts.using);
        List<WithDrawPacketCashProd> withDrawPacketCashProdList = withDrawPacketCashProdMapper.selectByExample(example);
        return withDrawPacketCashProdList;
    }

    /**
     * 红包提现
     * @param uid
     * @param packetId
     * @return
     * @throws Exception
     */
    public BusiResult withDraw(Long uid, Integer packetId) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        //1.获取用户现有红包
        UserPacket userPacket = userPacketService.getUserPacketByUid(uid);
        Double money = getProdNum(packetId);//提现金额
        Double remainNum = userPacket.getPacketNum();//用户现有红包
        //2.用户拥有红包金额是否大于提现红包金额
        if (money < remainNum) {
            remainNum = remainNum - money;
            if (remainNum < 0) {
                busiResult.setCode(BusiStatus.REDPACKETNUMNOTENOUGH.value());
                busiResult.setMessage("红包余额不足!");
            } else {
                //3.更新用户钱包--生成userpacketrecord记录---生成packetwithdrawrecord记录
                userPacket.setPacketNum(remainNum);
                userPacketService.updateUserPacket(userPacket);
                userPacketRecordService.insertInPacketRecord(uid, money, Constant.RedPacket.withDraw, Constant.PacketStatus.create);
                packetWithDrawRecordService.insertInPacketWithDrawRecord(uid, packetId, money, Constant.PacketStatus.create);
                RedPacketVo redPacketVo = new RedPacketVo();
                redPacketVo.setUid(uid);
                redPacketVo.setPacketNum(remainNum);
                busiResult.setData(redPacketVo);
            }
        } else {
            busiResult.setCode(BusiStatus.REDPACKETNUMNOTENOUGH.value());
            busiResult.setMessage("红包余额不足!");
        }
        return busiResult;
    }

    private Double getProdNum(Integer packetId) {
        WithDrawPacketCashProd withDrawPacketCashProd = withDrawPacketCashProdMapper.selectByPrimaryKey(packetId);
        return withDrawPacketCashProd.getPacketNum();
    }

    public BusiResult getUserWithDrawList() {
        List<PacketWithDrawRecord> packetWithDrawRecordList=getPacketWithdrawRecord();
        List<PacketWithDrawRecordVo> voList=convertPacketWithDrawRecordToVo(packetWithDrawRecordList);
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(voList);
        return busiResult;
    }
    private List<PacketWithDrawRecord> getPacketWithdrawRecord(){
        PacketWithDrawRecordExample packetWithDrawRecordExample=new PacketWithDrawRecordExample();
        packetWithDrawRecordExample.createCriteria().andRecordStatusEqualTo(Constant.PacketStatus.suc);
        List<PacketWithDrawRecord> packetWithDrawRecordList= packetWithDrawRecordMapper.selectByExample(packetWithDrawRecordExample);
        return packetWithDrawRecordList;
    }
    private List<PacketWithDrawRecordVo> convertPacketWithDrawRecordToVo(List<PacketWithDrawRecord> packetWithDrawRecordList){
        List<PacketWithDrawRecordVo> voList=Lists.newArrayList();
        if(CollectionUtils.isEmpty(packetWithDrawRecordList)){
            return voList;
        }
        for(PacketWithDrawRecord packetWithDrawRecord:packetWithDrawRecordList){
            PacketWithDrawRecordVo packetWithDrawRecordVo=new PacketWithDrawRecordVo();
            packetWithDrawRecordVo.setCreateTime(packetWithDrawRecord.getCreateTime());
            packetWithDrawRecordVo.setPacketNum(packetWithDrawRecord.getPacketNum());
            packetWithDrawRecordVo.setPacketProdCashId(packetWithDrawRecord.getPacketProdCashId());
            packetWithDrawRecordVo.setRecordId(packetWithDrawRecord.getRecordId());
            Long uid=packetWithDrawRecord.getUid();
            UserVo userVo=usersService.getUserVoByUid(uid);
            packetWithDrawRecordVo.setUid(userVo.getUid());
            packetWithDrawRecordVo.setNick(userVo.getNick());
            packetWithDrawRecordVo.setAvatar(userVo.getAvatar());
            voList.add(packetWithDrawRecordVo);
        }
        return voList;
    }
}
