package com.juxiao.xchat.service.api.user.vo;

import com.juxiao.xchat.dao.user.dto.UserTodayShareDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("WxappShareInfo")
public class WxappShareInfoVO {
    @ApiModelProperty(name = "drawCount", value = "免费捡海螺次数")
    private Integer drawCount;

    @ApiModelProperty(name = "shareedUsers", value = "邀请用户列表")
    private List<UserTodayShareDTO> shareedUsers;

}
