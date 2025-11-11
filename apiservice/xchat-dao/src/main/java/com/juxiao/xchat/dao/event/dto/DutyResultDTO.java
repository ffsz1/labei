package com.juxiao.xchat.dao.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DutyResultDTO {

    private Integer dutyId;

    private String dutyName;

    private Integer goldAmount;

    private Byte udStatus;

    public Byte getUdStatus() {
        return udStatus == null ? 1 : udStatus;
    }
}
