package com.juxiao.xchat.service.api.event.vo;

import lombok.Data;

import java.util.List;

@Data
public class ChipGiveParamVo {
    private Long sendUid;
    private String phone;
    private List<Long> chipList;
    private String chipId;
}
