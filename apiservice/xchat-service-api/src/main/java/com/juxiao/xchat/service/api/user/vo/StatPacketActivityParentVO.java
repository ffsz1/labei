package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.user.dto.StatPacketActRankDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatPacketActivityParentVO {

    private StatPacketActRankDTO me;

    private List<StatPacketActRankDTO> rankList;

}
