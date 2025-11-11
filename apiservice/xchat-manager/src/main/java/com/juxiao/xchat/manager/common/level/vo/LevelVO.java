package com.juxiao.xchat.manager.common.level.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LevelVO {
    private Long uid;
    private String avatar;
    private Integer level; //等级
    private String levelName;//等级
    private Double levelPercent;//金币等级百分比
    private Long leftGoldNum;//升到下一级所需的金币数量
}
