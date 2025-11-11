package com.juxiao.xchat.service.api.mcoin.vo;

import com.juxiao.xchat.manager.common.mcoin.impl.IssueItemTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class McoinDrawIssueDetailVO {
    /**
     * 抽奖期号，int
     */
    @ApiModelProperty(name = "issueId", value = "抽奖期号，int")
    private Long issueId;
    /**
     * 抽奖商品显示图片，string
     */
    @ApiModelProperty(name = "issueUrl", value = "抽奖商品显示图片，string")
    private String issueUrl;
    /**
     * 抽奖状态，1：可购买；2：等待开奖/开奖中；3：已开奖
     */
    @ApiModelProperty(name = "issueStatus", value = "抽奖状态，1：可购买；2：等待开奖/开奖中；3：已开奖")
    private Byte issueStatus;


    @ApiModelProperty(name = "itemTypeName", value = "抽奖类型名称，string")
    private String itemTypeName;

    /**
     * 抽奖商品名称，string
     */
    @ApiModelProperty(name = "itemName", value = "抽奖商品名称，string")
    private String itemName;
    /**
     * 抽奖商品单次所需萌币，int
     */
    @ApiModelProperty(name = "price", value = "抽奖商品单次所需萌币，int")
    private Integer price;
    /**
     * 剩余购买人次数量
     */
    @ApiModelProperty(name = "leftCount", value = "剩余购买人次数量")
    private Integer leftCount;
    /**
     * 总共可购买的人次数量，int
     */
    @ApiModelProperty(name = "totalCount", value = "总共可购买的人次数量，int")
    private Integer totalCount;

    //    /**
    //     * 参与记录，list
    //     */
    //    @ApiModelProperty(name = "issueId", value = "参与记录，list")
    //    private List<McoinDrawInvolvedUserDTO> involvedUsers;
    
    public void setItemTypeName(Byte itemType) {
        IssueItemTypeEnum typeName = IssueItemTypeEnum.itemTypeOf(itemType);
        if (typeName == null) {
            return;
        }
        this.itemTypeName = typeName.getItemTypeName();
    }
}
