package com.juxiao.xchat.dao.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: alwyn
 * @Description: 渠道管理用户展示信息
 * @Date: 2018/11/5 14:51
 */
@ApiModel("用户基本信息")
@Data
public class ChannelUserDTO {

    private Long erbanNo;
    private String nick;
    private String avatar;
    private Date createTime;
}
