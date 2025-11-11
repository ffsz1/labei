package com.juxiao.xchat.dao.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FansFollowDTO {
    @ApiModelProperty(value = "关注用户ID", name = "uid")
    private Long uid;
    @ApiModelProperty(value = "官方号", name = "erbanNo")
    private Long erbanNo;
    @ApiModelProperty(value = "关注用户昵称", name = "nick")
    private String nick;
    @ApiModelProperty(value = "关注用户头像", name = "avatar")
    private String avatar;
    @ApiModelProperty(value = "关注用户粉丝数量", name = "fansNum")
    private Integer fansNum;
    @ApiModelProperty(value = "关注用户在自己的房间开闭状态，1为开，0为闭", name = "valid")
    private boolean valid;
    @ApiModelProperty(value = "关注用户房间背景", name = "roomBack")
    private String roomBack;
    @ApiModelProperty(value = "关注用户房间类型", name = "type")
    private Byte type;
    @ApiModelProperty(value = "关注用户房间在线人数", name = "onlineNum")
    private Integer onlineNum;
    @ApiModelProperty(value = "关注用户性别", name = "gender")
    private Byte gender;
    @ApiModelProperty(value = "房主离开或者在房间状态，1不在线；2在线", name = "operatorStatus")
    private Byte operatorStatus;
    @ApiModelProperty(value = "关注用户房间标题", name = "title")
    private String title;
    @ApiModelProperty(value = "用户声音", name = "userVoice")
    private String userVoice;
    @ApiModelProperty(value = "声音时长", name = "voiceDuration")
    private Integer voiceDuration;
    @ApiModelProperty(value = "关注用户所在房间信息，用于踩人", name = "userInRoom")
    private RoomUserinDTO userInRoom;
    @ApiModelProperty(value = "关注用户等级", name = "experLevel")
    private Integer experLevel;
    @ApiModelProperty(value = "魅力等级", name = "charmLevel")
    private Integer charmLevel;
    @ApiModelProperty(value = "房间在线状态,1.在线 0.不在线")
    private Integer roomState;
}
