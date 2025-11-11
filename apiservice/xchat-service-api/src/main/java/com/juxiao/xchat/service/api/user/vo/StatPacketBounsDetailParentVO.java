package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.user.dto.StatPacketBounsDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatPacketBounsDetailParentVO {
    private Double todayBouns;
    private Double totalBouns;
    private List<StatPacketBounsDTO> bounsList;
}
