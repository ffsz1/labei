package com.juxiao.xchat.dao.mcoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McoinDrawIssuesDTO {
    /**
     * 抽奖期号
     */
    @ApiModelProperty(name = "issueId", value = "抽奖期号")
    private Long issueId;
    /**
     * 抽奖商品显示图片
     */
    @ApiModelProperty(name = "issueUrl", value = "抽奖商品显示图片")
    private String issueUrl;

    @ApiModelProperty(name = "itemType", value = "抽奖类型名称", hidden = true)
    private Byte itemType;

    @ApiModelProperty(name = "itemTypeName", value = "抽奖类型名称，string")
    private String itemTypeName;
    /**
     * 抽奖商品名称
     */
    @ApiModelProperty(name = "itemName", value = "抽奖商品名称")
    private String itemName;
    @ApiModelProperty(name = "drawNum", value = "总抽奖次数")
    private Integer drawNum;
    @ApiModelProperty(name = "drawCount", value = "已抽次数")
    private Integer drawCount;
    /**
     * 抽奖商品单次所需萌币
     */
    @ApiModelProperty(name = "price", value = "抽奖商品单次所需萌币")
    private Integer price;
    @ApiModelProperty(name = "pushMsgStatus", value = "是否推送全服（0：未推送 1：已推送）")
    private Byte pushMsgStatus;
    public Byte getItemType() {
        return null;
    }

    public void setItemType(Byte itemType) {
        switch (itemType) {
            case 1:
                this.itemTypeName = "靓号";
                break;
            case 2:
                this.itemTypeName = "座驾";
                break;
            case 3:
                this.itemTypeName = "头饰";
                break;
            default:
                this.itemTypeName = "";
                break;
        }
    }
}
