package com.juxiao.xchat.dao.mcoin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class McoinUserPkInfoDO {

    private Long id;
    private Long uid;
    private int term;//期数
    private int redPolls;//红方票数
    private int bluePolls;//蓝方票数
    private int payMcoinNumber;//支付萌币数
    private int carveUpMcoinNum;//瓜分萌币数
    private byte arrivalMcoin;//萌币是否到账（0未参加 1到账 2未到账）
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
}
