package com.juxiao.xchat.dao.guild.domain;

import lombok.Data;

import java.util.Date;

@Data
public class GuildHallApplyRecordDO {
    private Long id;

    private Long hallId;

    private Long uid;

    private Integer type;   //类型：0（申请加入），1（申请退出）

    private Integer status;   //审核状态：0（待审核），1（不通过），2（通过），3（已完成），4（已失效）

    private Long approverUid;

    private Date createTime;

    private Date updateTime;

    private Date approveTime;

    private String reason;

    private String remark;
}