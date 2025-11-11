package com.juxiao.xchat.dao.mcoin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class McoinDrawPrizeDTO {
    private Long issueId;
    private Long uid;
    private Long ticketId;
    private Date createTime;
}
