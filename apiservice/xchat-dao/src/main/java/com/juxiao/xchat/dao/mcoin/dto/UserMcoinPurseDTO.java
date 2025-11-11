package com.juxiao.xchat.dao.mcoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMcoinPurseDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(name = "timestamp", value = "用户ID")
    private Long uid;
    /**
     * 萌币数量
     */
    @ApiModelProperty(name = "timestamp", value = "萌币数量")
    private Integer mcoinNum;
    /**
     * 用户钱包状态：1，正常；2，冻结
     */
    @ApiModelProperty(name = "timestamp", value = "用户钱包状态：1，正常；2，冻结", hidden = true)
    private Byte purseStatus;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "timestamp", value = "创建时间", hidden = true)
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "timestamp", value = "更新时间", hidden = true)
    private Date updateTime;
}
