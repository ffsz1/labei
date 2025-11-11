package com.juxiao.xchat.service.api.guild.bo;

import lombok.Data;

/**
 * 描述：申请加入厅的业务类
 *
 * @创建时间： 2020/10/13 21:45
 * @作者： carl
 */
@Data
public class ApplyHallParamBo {
    private Long uid;
    private Long hallId;
    private String reason;
}
