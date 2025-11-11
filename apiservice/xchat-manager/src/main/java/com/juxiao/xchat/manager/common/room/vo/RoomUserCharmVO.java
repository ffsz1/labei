package com.juxiao.xchat.manager.common.room.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomUserCharmVO {

    /**
     * 魅力值
     */
    private Integer value;
    /**
     * 是否带帽
     */
    private Boolean withHat;
}
