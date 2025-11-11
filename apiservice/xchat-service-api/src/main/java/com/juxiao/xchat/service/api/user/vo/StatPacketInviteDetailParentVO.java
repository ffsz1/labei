package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.user.dto.UserPacketInviteRecordDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @class: StatPacketInviteDetailParentVO.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatPacketInviteDetailParentVO {
    private StatPacketInviteDetailVO inviteDetail;

    private List<UserPacketInviteRecordDTO> inviteList;
}
