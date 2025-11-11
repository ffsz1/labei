package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.charge.dto.RedPacketCashProdDTO;
import com.juxiao.xchat.dao.charge.dto.UserPacketRecordDTO;
import com.juxiao.xchat.service.api.charge.vo.WithdrawRedPacketRecordVO;
import com.juxiao.xchat.service.api.charge.vo.WithdrawRedPacketVO;

import java.util.List;

/**
 * 红包
 *
 * @class: RedPacketWithDrawService.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface RedPacketWithDrawService {

    /**
     * 获取有效的的红包产品
     *
     * @return
     */
    List<RedPacketCashProdDTO> listPacketCashProd();

    /**
     * 所有用户红包提现数据展示
     *
     * @return
     */
    List<UserPacketRecordDTO> listUserPacketRecord();

    /**
     * 用户红包提现
     *
     * @param uid
     * @param packetId
     * @return
     */
    WithdrawRedPacketVO withDraw(Long uid, Integer packetId,int type) throws WebServiceException;


    WithdrawRedPacketVO getUserRedPacket(Long uid);

    /**
     * 红包提现记录
     * @param uid
     * @return
     */
    List<WithdrawRedPacketRecordVO> listWithdrawRedPacketRecord(Long uid);

    /**
     * 红包提现
     * @param uid uid
     * @param packetId packetId
     * @param type type
     * @param openId openId
     * @return WithdrawRedPacketVO
     */
    WithdrawRedPacketVO withReadPackageDraw(Long uid, Integer packetId, int type, String openId)throws WebServiceException;
}
