package com.juxiao.xchat.service.api.room.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomNotifyVo implements Serializable{

    private static final long serialVersionUID = -2138667061485792919L;
    private int type;  // 1：房间消息，2：坑位消息
    private String roomInfo;
    private String micInfo;
}
