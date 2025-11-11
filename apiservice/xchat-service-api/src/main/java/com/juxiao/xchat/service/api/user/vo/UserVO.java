package com.juxiao.xchat.service.api.user.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.user.dto.PrivatePhotoDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {
    private Long uid;
    private Long erbanNo;
    private String phone;
    private Date birth;
    private Byte star;
    private String nick;
    private String signture;
    private String userVoice;
    private Integer voiceDura;
    private Byte defUser;
    private Long fortune;
    private Byte gender;
    private String avatar;
    private String region;
    private String userDesc;
    private Integer followNum;
    private Integer fansNum;
    private Long goldNum;
    private Boolean hasPrettyErbanNo;
    private Byte operType;//用户新增或更新操作时，返回操作类型，1是更新用户资料，2是第一次注册补全用户资料
    private Boolean hasRegPacket;
    private Integer experLevel; //等级值
    private Integer charmLevel; //魅力值
    private String carUrl; //座驾
    private String carName; //座驾
    private String headwearUrl; //头饰
    private String headwearName; //头饰
    private Date createTime;
    private List<PrivatePhotoDTO> privatePhoto;
    private Byte findNewUsers;
    /** 私聊权限 */
    private Integer chatPermission;
    /**
     * 活跃度
     */
    private Integer liveness;
    /**
     * 是否已关注
     */
    private Boolean isFan;
    private Long shareCode;
    private Boolean hasWx;
    private Boolean hasQq;
    private Integer mcoinNum;
    @ApiModelProperty(value = "0:新晋 1：萌新 9：无")
    private String userShowState;

    private String qqNick;
    private String weixinNick;
    private String appleUserName;
    private Boolean hasApple;
//    public String getPhone() {
//        if (StringUtils.isBlank(phone) || phone.equals(String.valueOf(erbanNo))) {
//            return phone;
//        }
//
//        String desensitize = StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(phone, 4), StringUtils.length(phone), "*"), "***");
//        return StringUtils.left(phone, 3).concat(desensitize);
//    }
}
