package com.juxiao.xchat.service.api.guild.bo;

import lombok.Data;

/**
 * 描述：审核加入/退出申请的所需参数
 *
 * @创建时间： 2020/10/15 9:49
 * @作者： carl
 */
@Data
public class VerifyParamBo {

    private Long uid;    //审核人的uid

    private Long applyId;    //申请记录id

    private Boolean isApproved;   //审核结果
}
