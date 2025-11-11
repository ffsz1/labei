package com.juxiao.xchat.dao.guild.dto;

import lombok.Data;

import java.util.Date;

/**
 * 描述：
 *
 * @创建时间： 2020/10/14 11:42
 * @作者： carl
 */
@Data
public class GuildHallApplyRecordDTO {
    private Long id;
    private Long uid;
    private String nick;
    private String erbanNo;
    private String reason;
    private Date createTime;

    private int type;     // 申请类型：0（申请加入），1（申请退出）
    private int status;   // 审核状态：0，审核中；1，审核拒绝；2，审核通过；3，已加入；4，已失效

    private String hallId;
    private String hallTitle;
    private String approverUid;  //审核人uid,
    private String approverNick;   //审核用户昵称
    private Date approverTime;  //审核时间

    private Long guildId;
}
