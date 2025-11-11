package com.juxiao.xchat.dao.mcoin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 萌币PK信息表
 * @author clear.
 * @date 2019/03/05 18:54
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class McoinPkInfoDO {

    private Long id;
    private int term;//期数
    private String title;//题目
    private String redAnswer;//红方答案
    private String blueAnswer;//蓝方答案
    private String redPic;//红方头像
    private String bluePic;//蓝方头像
    private int redPolls;//红方票数
    private int bluePolls;//蓝方票数
    private Date lotteryTime;//本期开奖时间
    private int carveUpMcoinNum;//瓜分萌币数
    private byte pkStatus;//状态(0、无效 1、正在进行 2、已经结束)
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
}
