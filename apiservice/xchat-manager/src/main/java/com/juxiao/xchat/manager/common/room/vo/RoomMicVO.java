package com.juxiao.xchat.manager.common.room.vo;//package com.juxiao.xchat.service.api.room.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomMicVO implements Serializable {
    private static final long serialVersionUID = 3157545571794952935L;
    /**
     * 麦所在位置
     */
    private int position;
    /**
     * 坑位的状态，是否被锁，1：锁坑，0：开放
     */
    private int posState;
    /**
     * 麦序的状态，是否被禁，1：禁麦，0：开放
     */
    private int micState;
}
