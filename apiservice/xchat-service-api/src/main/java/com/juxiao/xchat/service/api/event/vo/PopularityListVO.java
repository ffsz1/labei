package com.juxiao.xchat.service.api.event.vo;

import lombok.Data;

@Data
public class PopularityListVO {
    private Long uid;   // 送出人气票的ID
    private Long receiptId; // 收到人气票的ID
    private Integer receiptVotes;   // 收到的人气票数量
    private Integer sendVotes;  // 送出的人气票数量
    private String avatar;  // 头像
    private String nick;    // 昵称
    private String sendNick; // 送出人气票的昵称
    private Integer rank;   // 当前排名
}
