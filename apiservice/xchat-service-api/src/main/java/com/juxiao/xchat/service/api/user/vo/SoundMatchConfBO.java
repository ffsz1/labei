package com.juxiao.xchat.service.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/12/6 17:32
 */
@Data
public class SoundMatchConfBO {

    @ApiModelProperty(value = "UID")
    private Long uid;
    @ApiModelProperty(value = "性别过滤 0:不过滤, 1:过滤男(显示女) 2:女")
    private Integer filterGender;
    @ApiModelProperty(value = "是否隐藏声音")
    private Boolean voiceHide;
}
