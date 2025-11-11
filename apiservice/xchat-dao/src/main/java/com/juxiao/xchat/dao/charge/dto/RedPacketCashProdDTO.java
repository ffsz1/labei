package com.juxiao.xchat.dao.charge.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @class: RedPacketCashProdDTO.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
@Getter
@Setter
public class RedPacketCashProdDTO implements Comparable<RedPacketCashProdDTO> {

    private Integer packetId;

    private Double packetNum;

    private Byte prodStauts;

    private Integer seqNo;

    @Override
    public int compareTo(RedPacketCashProdDTO prodDto) {
        if (prodDto.seqNo < this.seqNo) {
            return -1;
        }

        if (prodDto.seqNo > this.seqNo) {
            return 1;
        }
        return 0;
    }
}
