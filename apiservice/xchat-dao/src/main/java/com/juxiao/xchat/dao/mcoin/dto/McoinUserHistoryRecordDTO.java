package com.juxiao.xchat.dao.mcoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McoinUserHistoryRecordDTO {
    @ApiModelProperty(name = "issueId", value = "抽奖期号，int")
    private Long issueId;
    @ApiModelProperty(name = "uid", value = "抽奖商品显示图片，string")
    private Long uid;
    @ApiModelProperty(name = "nick", value = "昵称，string")
    private String nick;
    @ApiModelProperty(name = "avatar", value = "头像，string")
    private String avatar;
}
