package com.juxiao.xchat.service.api.user.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/12/6 15:06
 */
@Data
public class SoundMatchBO {
    @ApiModelProperty(value = "UID")
    private Long uid;
    @ApiModelProperty(value = "喜欢的人的UID")
    private Long likeUid;
    @ApiModelProperty(value = "ticket")
    private String ticket;
}
