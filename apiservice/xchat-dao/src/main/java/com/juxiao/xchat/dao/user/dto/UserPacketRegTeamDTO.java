package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPacketRegTeamDTO {

    private Long uid;

    private String nick;

    private String avatar;

    private String createTime;

    private Integer invitationNum;
}
