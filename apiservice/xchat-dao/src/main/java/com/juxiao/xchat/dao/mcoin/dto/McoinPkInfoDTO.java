package com.juxiao.xchat.dao.mcoin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 萌币PK信息表
 * @author clear.
 * @date 2019/03/05 18:54
 */
@Getter
@Setter
public class McoinPkInfoDTO {

    @ApiModelProperty(name = "id", value = "萌币Pk信息ID")
    private Long id;
    @ApiModelProperty(name = "term", value = "萌币Pk活动期号")
    private int term;//期数
    @ApiModelProperty(name = "title", value = "萌币Pk活动该期题目")
    private String title;//题目
    @ApiModelProperty(name = "redAnswer", value = "红方答案")
    private String redAnswer;//红方答案
    @ApiModelProperty(name = "blueAnswer", value = "蓝方答案")
    private String blueAnswer;//蓝方答案
    @ApiModelProperty(name = "redPic", value = "红方头像")
    private String redPic;//红方头像
    @ApiModelProperty(name = "bluePic", value = "蓝方头像")
    private String bluePic;//蓝方头像
    @ApiModelProperty(name = "redPolls", value = "红方票数")
    private int redPolls;//红方票数
    @ApiModelProperty(name = "bluePolls", value = "蓝方票数")
    private int bluePolls;//蓝方票数
    @ApiModelProperty(name = "lotteryTime", value = "本期开奖时间")
    private Date lotteryTime;//本期开奖时间
    @ApiModelProperty(name = "carveUpMcoinNum", value = "可瓜分的萌币数量")
    private int carveUpMcoinNum;//瓜分萌币数
    @ApiModelProperty(name = "pkStatus", value = "活动状态(0、无效 1、正在进行 2、已经结束)",hidden = true)
    private byte pkStatus;//状态(0、无效 1、正在进行 2、已经结束)
    @ApiModelProperty(name = "pushMsgStatus", value = "是否推送活动开始全服（0、未推送 1、已推送）",hidden = true)
    private byte pushMsgStatus;//是否推送活动开始全服（0、未推送 1、已推送）
    @ApiModelProperty(name = "createTime", value = "创建时间",hidden = true)
    private Date createTime;//创建时间
    @ApiModelProperty(name = "updateTime", value = "更新时间",hidden = true)
    private Date updateTime;//更新时间
}
