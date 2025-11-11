package com.juxiao.xchat.service.api.event.vo;

import com.juxiao.xchat.dao.event.dto.DutyResultDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DutiesVO {
    /**
     * 在线时长
     */
    private Integer roomTime;
    /**
     * 新手任务
     */
    private List<DutyResultDTO> fresh;
    /**
     * 每日任务
     */
    private List<DutyResultDTO> daily;
    /**
     * 每日房间时长任务
     */
    private List<DutyResultDTO> dailyTime;
}
