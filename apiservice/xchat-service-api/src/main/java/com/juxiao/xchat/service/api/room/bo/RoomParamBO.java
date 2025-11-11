package com.juxiao.xchat.service.api.room.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("RoomParam")
public class RoomParamBO {
    @ApiModelProperty(name = "uid", value = "用户ID")
    private Long uid;
    @ApiModelProperty(name = "roomId", value = "房间ID")
    private Long roomId;
    @ApiModelProperty(name = "title", value = "房间标题")
    private String title;
    @ApiModelProperty(name = "type", value = "房间类型，目前只有3")
    private Byte type;
    @ApiModelProperty(name = "operatorStatus", value = "房主在线状态：1，不在线；2，在线")
    private Byte operatorStatus;
    @ApiModelProperty(name = "avatar", value = "房间头像")
    private String avatar;
    @ApiModelProperty(name = "roomDesc", value = "房间话题")
    private String roomDesc;
    @ApiModelProperty(name = "roomNotice", value = "房间公告")
    private String roomNotice;
    @ApiModelProperty(name = "backPic", value = "房间背景图")
    private String backPic;
    @ApiModelProperty(name = "roomPwd", value = "进房间密码")
    private String roomPwd;
    @ApiModelProperty(name = "roomTag", value = "房间标签")
    private String roomTag;
    @ApiModelProperty(name = "tagId", value = "标签id")
    private Integer tagId;
    @ApiModelProperty(name = "giftEffectSwitch", value = "礼物特效，默认关闭过滤（0关1开）")
    private Integer giftEffectSwitch;
    @ApiModelProperty(name = "publicChatSwitch", value = "公聊默认关闭过滤（0关1开）")
    private Integer publicChatSwitch;
    @ApiModelProperty(name = "playInfo", value = "玩法介绍")
    private String playInfo;
    @ApiModelProperty(name = "giftCardSwitch", value = "座驾特效，默认关闭过滤（0关1开）")
    private Integer giftCardSwitch;

}
