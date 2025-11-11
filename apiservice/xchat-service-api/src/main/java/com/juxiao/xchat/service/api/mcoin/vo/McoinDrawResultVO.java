package com.juxiao.xchat.service.api.mcoin.vo;

import com.juxiao.xchat.manager.common.mcoin.dto.McoinDrawWiningDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class McoinDrawResultVO {
    /**
     * 参与记录ID，int
     */
    @ApiModelProperty(name = "recordId", value = "参与记录ID，int")
    private Long recordId;
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

    @ApiModelProperty(name = "itemTypeName", value = "抽奖类型名称，string")
    private String itemTypeName;
    /**
     * 抽奖商品名称，string
     */
    @ApiModelProperty(name = "itemName", value = "抽奖商品名称，string")
    private String itemName;
    /**
     * 参与时间，string
     */
    @ApiModelProperty(name = "createTime", value = "参与时间，string")
    private Date createTime;
    /**
     * 开奖时间，string
     */
    @ApiModelProperty(name = "drawTime", value = "开奖时间，string")
    private Date drawTime;
    /**
     * 中奖用户，object
     */
    @ApiModelProperty(name = "winUser", value = "中奖用户，object")
    private McoinDrawWiningDTO winUser;

}
