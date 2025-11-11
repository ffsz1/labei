package com.juxiao.xchat.dao.room.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 个人游戏配置
 */

@Data
@AllArgsConstructor
public class RoomGameDTO {
    @ApiModelProperty(value = "玩法类型", name = "1：海螺 ")
    private int type; // 类型
    private int isOpen;// 底部工具栏是否显示 isopen=1表示开启、0表示关闭
}
