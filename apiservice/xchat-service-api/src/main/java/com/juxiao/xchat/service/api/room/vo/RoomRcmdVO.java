package com.juxiao.xchat.service.api.room.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RoomRcmd")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomRcmdVO {
    @ApiModelProperty(value = "推荐房主ID", name = "uid")
    private Integer uid;
    @ApiModelProperty(value = "推荐房间ID", name = "roomId")
    private Long roomId;
    @ApiModelProperty(value = "推荐房间标题", name = "title")
    private String title;
    @ApiModelProperty(value = "推荐房间头像", name = "avatar")
    private String avatar;
}
