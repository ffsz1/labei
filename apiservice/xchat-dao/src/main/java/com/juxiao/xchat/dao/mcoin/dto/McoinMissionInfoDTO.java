package com.juxiao.xchat.dao.mcoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "McoinMissionInfo", description = "萌币任务列表信息")
public class McoinMissionInfoDTO {

    @ApiModelProperty(name = "missionId", value = "任务ID")
    private Integer missionId;

    @ApiModelProperty(name = "missionName", value = "任务名称")
    private String missionName;

    @ApiModelProperty(name = "mcoinAmount", value = "任务奖励")
    private Integer mcoinAmount;

    @ApiModelProperty(name = "freebiesType", value = "赠品类型")
    private Byte freebiesType;

    @ApiModelProperty(name = "freebiesId", value = "赠品ID")
    private Integer freebiesId;

    @ApiModelProperty(name = "picUrl", value = "图片url")
    private String picUrl;

    @ApiModelProperty(name = "completeDate", value = "完成时间")
    private Date completeDate;

    @ApiModelProperty(name = "scheme", value = "客户端预留跳转页面字段")
    private String scheme = "";

    @ApiModelProperty(name = "missionType", hidden = true)
    private Byte missionType;

    @ApiModelProperty(name = "missionStatus", value = "任务状态，1，未完成；2，已完成；3，已领取")
    private Byte missionStatus;

    public Byte getMissionStatus() {
        return missionStatus == null ? 1 : missionStatus;
    }
}
