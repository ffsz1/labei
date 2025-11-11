package com.juxiao.xchat.dao.charge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeChannelProdDTO implements Comparable<ChargeChannelProdDTO> {
    private String chargeProdId;
    private String prodName;
    private String prodDesc;
    private Integer money;
    private Integer giftGoldNum;
    private Byte channel;
    private Byte seqNo;

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(ChargeChannelProdDTO chargeProd) {
        if (chargeProd.seqNo > this.seqNo) {
            return -1;
        } else if (chargeProd.seqNo < this.seqNo) {
            return 1;
        } else {
            return 0;
        }
    }
}
