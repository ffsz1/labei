package com.juxiao.xchat.dao.mcoin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户参与萌币PK信息表
 * @author clear
 * @date 2019-03-06 16:01
 */
@Getter
@Setter
public class McoinUserPkInfoDTO {

        @ApiModelProperty(name = "id", value = "用户参与萌币PK信息表ID")
        private Long id;
        @ApiModelProperty(name = "uid", value = "用户id")
        private Long uid;
        @ApiModelProperty(name = "term", value = "萌币Pk活动期号")
        private int term;//期数
        @ApiModelProperty(name = "redPolls", value = "支持红方票数")
        private int redPolls;//红方票数
        @ApiModelProperty(name = "bluePolls", value = "支持蓝方票数")
        private int bluePolls;//蓝方票数
        @ApiModelProperty(name = "payMcoinNumber", value = "支付萌币数")
        private int payMcoinNumber;//支付萌币数
        @ApiModelProperty(name = "carveUpMcoinNum", value = "可瓜分的萌币数量")
        private int carveUpMcoinNum;//瓜分萌币数
        @ApiModelProperty(name = "arrivalMcoin", value = "萌币是否到账（0未参加 1到账 2未到账）",hidden = true)
        private byte arrivalMcoin;//萌币是否到账（0未参加 1到账 2未到账）
        @ApiModelProperty(name = "createTime", value = "创建时间",hidden = true)
        private Date createTime;//创建时间
        @ApiModelProperty(name = "updateTime", value = "更新时间",hidden = true)
        private Date updateTime;//更新时间

}
