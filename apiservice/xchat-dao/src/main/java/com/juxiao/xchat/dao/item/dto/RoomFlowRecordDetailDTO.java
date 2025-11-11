package com.juxiao.xchat.dao.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 房间礼物赠送明细
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomFlowRecordDetailDTO {
    private String sendNick;
    private String receiveNick;
    private Long totalGoldNum;
    private Integer giftNum;
    private String giftName;
    private String giftUrl;
    private String time;
    private Long senUid;
    private Long receiveUid;
    private Long roomUid;


}
