package com.juxiao.xchat.service.api.mcoin.vo;

import com.juxiao.xchat.dao.mcoin.dto.McoinMissionInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "McoinMission", description = "萌币任务接口返回")
public class McoinMissionVO {
    /**
     * 萌币余额
     */
    @ApiModelProperty(name = "mcoinNum", value = "萌币余额")
    private Integer mcoinNum;

    /**
     * 每周签到任务
     */
    @ApiModelProperty(name = "weeklyMissions", value = "每周签到任务")
    private List<McoinMissionInfoDTO> weeklyMissions;

    /**
     * 新手任务
     */
    @ApiModelProperty(name = "beginnerMissions", value = "新手任务")
    private List<McoinMissionInfoDTO> beginnerMissions;

    /**
     * 每日任务
     */
    @ApiModelProperty(name = "dailyMissions", value = "每日任务")
    private List<McoinMissionInfoDTO> dailyMissions;

}
