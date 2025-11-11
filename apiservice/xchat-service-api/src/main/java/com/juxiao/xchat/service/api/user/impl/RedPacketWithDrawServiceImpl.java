package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.charge.PacketWithDrawRecordDao;
import com.juxiao.xchat.dao.charge.WithDrawPacketCashProdDao;
import com.juxiao.xchat.dao.charge.domain.PacketWithDrawRecordDO;
import com.juxiao.xchat.dao.charge.dto.RedPacketCashProdDTO;
import com.juxiao.xchat.dao.charge.dto.UserPacketRecordDTO;
import com.juxiao.xchat.dao.charge.dto.WithDrawPacketCashProdDTO;
import com.juxiao.xchat.dao.charge.dto.WithdrawRedPacketRecordDTO;
import com.juxiao.xchat.dao.charge.enumeration.PacketRecordStatus;
import com.juxiao.xchat.dao.user.UserPacketDao;
import com.juxiao.xchat.dao.user.UserPacketRecordDao;
import com.juxiao.xchat.dao.user.domain.UserPacketDO;
import com.juxiao.xchat.dao.user.domain.UserPacketRecordDO;
import com.juxiao.xchat.dao.user.dto.UserPacketDTO;
import com.juxiao.xchat.dao.user.enumeration.UserPacketRecordTypeEnum;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.RedPacketWithDrawManager;
import com.juxiao.xchat.service.api.charge.vo.WithdrawRedPacketRecordVO;
import com.juxiao.xchat.service.api.charge.vo.WithdrawRedPacketVO;
import com.juxiao.xchat.service.api.user.RedPacketWithDrawService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 红包提现service
 */
@Service
public class RedPacketWithDrawServiceImpl implements RedPacketWithDrawService {
    @Autowired
    private PacketWithDrawRecordDao withdrawRecordDao;
    @Autowired
    private UserPacketDao userPacketDao;
    @Autowired
    private UserPacketRecordDao packetRecordDao;
    @Autowired
    private WithDrawPacketCashProdDao packetCashProdDao;
    @Autowired
    private RedPacketWithDrawManager redpacketManager;
    @Autowired
    private RedisManager redisManager;


    @Override
    public List<RedPacketCashProdDTO> listPacketCashProd() {
        return redpacketManager.listUsingPacketCashProd();
    }

    @Override
    public List<UserPacketRecordDTO> listUserPacketRecord() {
        return withdrawRecordDao.listSuccessPacketRecord(PacketRecordStatus.SUCCESS.getValue());
    }

    @Override
    public WithdrawRedPacketVO withDraw(Long uid, Integer packetId,int type) throws WebServiceException {
        if (uid == null || packetId == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        // 加锁
        String lock = redisManager.lock(RedisKey.luck_money_with_draw.getKey(uid.toString()), 5 * 1000);
        if (StringUtils.isBlank(lock)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        Double remainNum;
        try {
            //1.获取用户现有红包
            UserPacketDTO userPacketDto = userPacketDao.getUserPacket(uid);
            if (userPacketDto == null || userPacketDto.getPacketNum() == null || userPacketDto.getPacketNum() == 0) {
                throw new WebServiceException(WebServiceCode.REDPACKET_NUM_NOT_ENOUGH);
            }

            WithDrawPacketCashProdDTO packetCashProdDto = packetCashProdDao.getPacketCashProd(packetId);
            if (packetCashProdDto == null || packetCashProdDto.getPacketNum() == null) {
                throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
            }

            //2.用户拥有红包金额是否大于提现红包金额
            if (packetCashProdDto.getPacketNum() > userPacketDto.getPacketNum()) {
                throw new WebServiceException(WebServiceCode.REDPACKET_NUM_NOT_ENOUGH);
            }
            remainNum = userPacketDto.getPacketNum() - packetCashProdDto.getPacketNum();
            if (remainNum < 0) {
                throw new WebServiceException(WebServiceCode.REDPACKET_NUM_NOT_ENOUGH);
            }

            //3.更新用户钱包--生成userpacketrecord记录---生成packetwithdrawrecord记录
            UserPacketDO packetDo = new UserPacketDO();
            packetDo.setUid(uid);
            packetDo.setPacketNum(remainNum);
            packetDo.setUpdateTime(new Date());
            userPacketDao.update(packetDo);

            UserPacketRecordDO packetRecordDo = new UserPacketRecordDO();
            packetRecordDo.setPacketId(UUIDUtils.get());
            packetRecordDo.setCreateTime(new Date());
            packetRecordDo.setPacketNum(packetCashProdDto.getPacketNum());
            packetRecordDo.setPacketStatus(PacketRecordStatus.CREATE.getValue());
            packetRecordDo.setType(UserPacketRecordTypeEnum.PACKET_FOR_DEPOSITS.getValue());
            packetRecordDo.setUid(uid);
            packetRecordDao.save(packetRecordDo);

            PacketWithDrawRecordDO withdrawRecordDo = new PacketWithDrawRecordDO();
            withdrawRecordDo.setRecordId(UUIDUtils.get());
            withdrawRecordDo.setCreateTime(new Date());
            withdrawRecordDo.setUid(uid);
            withdrawRecordDo.setTranType((byte) type);
            withdrawRecordDo.setPacketProdCashId(packetId);
            withdrawRecordDo.setPacketNum(packetCashProdDto.getPacketNum());
            withdrawRecordDo.setRecordStatus(PacketRecordStatus.CREATE.getValue());
            withdrawRecordDao.save(withdrawRecordDo);
        } finally {
            redisManager.unlock(RedisKey.luck_money_with_draw.getKey(uid.toString()), lock);
        }
        return new WithdrawRedPacketVO(uid, remainNum);
    }

    @Override
    public WithdrawRedPacketVO getUserRedPacket(Long uid) {
        UserPacketDTO userPacketDto = userPacketDao.getUserPacket(uid);
        return new WithdrawRedPacketVO(uid, userPacketDto.getPacketNum());
    }

    /**
     * 红包提现记录
     *
     * @param uid
     * @return
     */
    @Override
    public List<WithdrawRedPacketRecordVO> listWithdrawRedPacketRecord(Long uid) {
        List<WithdrawRedPacketRecordVO> withdrawRedPacketRecordVOList = Lists.newLinkedList();
        List<WithdrawRedPacketRecordDTO> withdrawRedPacketRecordDTOS = withdrawRecordDao.selectWithdrawRedPacketRecordByList(uid);
        if(withdrawRedPacketRecordDTOS.size() > 0){
            withdrawRedPacketRecordDTOS.forEach(item ->{
                withdrawRedPacketRecordVOList.add(new WithdrawRedPacketRecordVO(item.getUid(),"提取邀请好友收益",item.getPacketNum(),item.getCreateTime()));
            });
        }
        return withdrawRedPacketRecordVOList;
    }

    /**
     * 红包提现
     *
     * @param uid      uid
     * @param packetId packetId
     * @param type     type
     * @param openId   openId
     * @return WithdrawRedPacketVO
     */
    @Override
    public WithdrawRedPacketVO withReadPackageDraw(Long uid, Integer packetId, int type, String openId) throws WebServiceException{
        if (uid == null || packetId == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        // 加锁
        String lock = redisManager.lock(RedisKey.luck_money_with_draw.getKey(uid.toString()), 5 * 1000);
        if (StringUtils.isBlank(lock)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        Double remainNum;
        try {
            //1.获取用户现有红包
            UserPacketDTO userPacketDto = userPacketDao.getUserPacket(uid);
            if (userPacketDto == null || userPacketDto.getPacketNum() == null || userPacketDto.getPacketNum() == 0) {
                throw new WebServiceException(WebServiceCode.REDPACKET_NUM_NOT_ENOUGH);
            }

            WithDrawPacketCashProdDTO packetCashProdDto = packetCashProdDao.getPacketCashProd(packetId);
            if (packetCashProdDto == null || packetCashProdDto.getPacketNum() == null) {
                throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
            }

            //2.用户拥有红包金额是否大于提现红包金额
            if (packetCashProdDto.getPacketNum() > userPacketDto.getPacketNum()) {
                throw new WebServiceException(WebServiceCode.REDPACKET_NUM_NOT_ENOUGH);
            }
            remainNum = userPacketDto.getPacketNum() - packetCashProdDto.getPacketNum();
            if (remainNum < 0) {
                throw new WebServiceException(WebServiceCode.REDPACKET_NUM_NOT_ENOUGH);
            }

            //3.更新用户钱包--生成userpacketrecord记录---生成packetwithdrawrecord记录
            UserPacketDO packetDo = new UserPacketDO();
            packetDo.setUid(uid);
            packetDo.setPacketNum(remainNum);
            packetDo.setUpdateTime(new Date());
            userPacketDao.update(packetDo);

            UserPacketRecordDO packetRecordDo = new UserPacketRecordDO();
            packetRecordDo.setPacketId(UUIDUtils.get());
            packetRecordDo.setCreateTime(new Date());
            packetRecordDo.setPacketNum(packetCashProdDto.getPacketNum());
            packetRecordDo.setPacketStatus(PacketRecordStatus.CREATE.getValue());
            packetRecordDo.setType(UserPacketRecordTypeEnum.PACKET_FOR_DEPOSITS.getValue());
            packetRecordDo.setUid(uid);
            packetRecordDao.save(packetRecordDo);

            PacketWithDrawRecordDO withdrawRecordDo = new PacketWithDrawRecordDO();
            withdrawRecordDo.setRecordId(UUIDUtils.get());
            withdrawRecordDo.setCreateTime(new Date());
            withdrawRecordDo.setUid(uid);
            withdrawRecordDo.setTranType((byte) type);
            withdrawRecordDo.setPacketProdCashId(packetId);
            withdrawRecordDo.setPacketNum(packetCashProdDto.getPacketNum());
            withdrawRecordDo.setRecordStatus(PacketRecordStatus.CREATE.getValue());
            withdrawRecordDo.setWxOpenId(openId);
            withdrawRecordDao.insert(withdrawRecordDo);
        } finally {
            redisManager.unlock(RedisKey.luck_money_with_draw.getKey(uid.toString()), lock);
        }
        return new WithdrawRedPacketVO(uid, remainNum);
    }
}
