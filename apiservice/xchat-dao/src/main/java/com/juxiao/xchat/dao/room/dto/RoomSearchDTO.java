package com.juxiao.xchat.dao.room.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomSearchDTO {
    @ApiModelProperty(value = "搜索到的用户ID", name = "uid")
    private Long uid;
    @ApiModelProperty(value = "用户所在房间ID", name = "roomId")
    private Long roomId;
    @ApiModelProperty(value = "房间标题", name = "title")
    private String title;
    @ApiModelProperty(value = "类型", name = "type")
    private Byte type;
    @ApiModelProperty(value = "搜索到的用户是否在线", name = "valid")
    private Boolean valid;//是否在线
    @ApiModelProperty(value = "房间头像图", name = "avatar")
    private String avatar;//
    @ApiModelProperty(value = "性别", name = "gender")
    private Byte gender;//
    @ApiModelProperty(value = "用户昵称", name = "nick")
    private String nick;
    @ApiModelProperty(value = "官方号", name = "erbanNo")
    private Long erbanNo;
    @ApiModelProperty(value = "是否靓号", name = "hasPrettyErbanNo")
    private boolean hasPrettyErbanNo;
    @ApiModelProperty(value = "粉丝数量", name = "fansNum")
    private Integer fansNum;
    @ApiModelProperty(value = "房间标签图片", name = "tagPict")
    private String tagPict;
}
