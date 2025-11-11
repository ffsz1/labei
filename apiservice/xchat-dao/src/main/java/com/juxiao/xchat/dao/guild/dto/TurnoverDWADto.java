package com.juxiao.xchat.dao.guild.dto;

import lombok.Data;

/**
 * 描述：日、周、汇总的流水金币数据
 *
 * @创建时间： 2020/10/10 14:18
 * @作者： carl
 */
@Data
public class TurnoverDWADto {
    private Long gold_day;
    private Long gold_week;
    private Long gold_all;
}
