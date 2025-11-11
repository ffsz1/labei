package com.juxiao.xchat.dao.mcoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McoinUserDrawRecordDTO {
    @ApiModelProperty(name = "recordId", value = "参与记录ID，int")
    private Long recordId;
    @ApiModelProperty(name = "issueId", value = "抽奖期号，int")
    private Long issueId;
    @ApiModelProperty(name = "issueUrl", value = "抽奖商品显示图片，string")
    private String issueUrl;
    @ApiModelProperty(name = "itemTypeName", value = "抽奖商品类型，string")
    private String itemTypeName;
    @ApiModelProperty(name = "itemName", value = "抽奖商品名称，string")
    private String itemName;
    @ApiModelProperty(name = "leftCount", value = "剩余购买人次数量，int")
    private Integer leftCount;
    @ApiModelProperty(name = "drawCount", value = "用户参与次数，int")
    private Integer drawCount;
    @ApiModelProperty(name = "drawStatus", value = "中奖状态：1，已中奖；2，没有中奖")
    private Byte drawStatus;
    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    public void setItemType(Byte itemType) {
        if (itemType == 1) {
            this.itemTypeName = "靓号";
        } else if (itemType == 2) {
            this.itemTypeName = "座驾";
        } else if (itemType == 3) {
            this.itemTypeName = "头饰";
        }

    }
}
