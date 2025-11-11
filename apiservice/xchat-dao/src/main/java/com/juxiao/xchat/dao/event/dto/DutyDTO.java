package com.juxiao.xchat.dao.event.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @class: DutyDTO.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
@Getter
@Setter
public class DutyDTO {
    private Integer id;
    private String dutyName;
    private Integer goldAmount;
    private Byte dutyType;
    private Integer dutyStatus;
}