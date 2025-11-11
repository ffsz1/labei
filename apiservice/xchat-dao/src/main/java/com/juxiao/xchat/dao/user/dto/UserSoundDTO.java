package com.juxiao.xchat.dao.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/11/26 19:18
 */
@Data
public class UserSoundDTO {

    @ApiModelProperty(value = "UID")
    private Long uid;
    @ApiModelProperty(value = "喵喵号")
    private Long erbanNo;
    @ApiModelProperty(value = "昵称")
    private String nick;
    @ApiModelProperty(value = "性别")
    private Byte gender;
    @ApiModelProperty(value = "生日")
    private Date birth;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "粉丝数量")
    private Integer fansNum;
    @ApiModelProperty(value = "魅力值等级")
    private Integer charmLevel;
    @ApiModelProperty(value = "在房间状态，0不在线；1在线")
    private Byte operatorStatus;
    @ApiModelProperty(value = "用户签名")
    private String userDesc;
    @ApiModelProperty(value = "用户声鉴卡")
    private String userVoice;
    @ApiModelProperty(value = "声鉴卡时长")
    private Integer voiceDura;

}
