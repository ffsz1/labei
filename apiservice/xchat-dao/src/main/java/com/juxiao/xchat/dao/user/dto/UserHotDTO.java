package com.juxiao.xchat.dao.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(value = "热门用户类")
public class UserHotDTO {
  @ApiModelProperty(value = "UID")
  private Long uid;
  @ApiModelProperty(value = "昵称")
  private String nick;
  @ApiModelProperty(value = "个性签名")
  private String userDescription;
  @ApiModelProperty(value = "签名")
  private String signature;
  @ApiModelProperty(value = "用户声音")
  private String userVoice;
  @ApiModelProperty(value = "声音时长")
  private Integer voiceDuration;
  @ApiModelProperty(value = "性别, 1.男; 2.女")
  private Byte gender;
  @ApiModelProperty(value = "头像")
  private String avatar;
  @ApiModelProperty(value = "注册时间")
  private Date signTime;
  @ApiModelProperty(value = "魅力值")
  private Integer glamour;
  @ApiModelProperty(value = "false:充值;true:未充值")
  private Boolean isFirstCharge;
  @ApiModelProperty(value = "最后一次登陆时间")
  private Date lastLoginTime;
  @ApiModelProperty(value = "等级值")
  private Integer experLevel;
  @ApiModelProperty(value = "海角号")
  private Long erbanNo;
  @ApiModelProperty(value = "房间在线状态,1.在线 0.不在线")
  private Integer roomState;
}
