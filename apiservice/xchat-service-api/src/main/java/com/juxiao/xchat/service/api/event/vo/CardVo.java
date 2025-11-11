package com.juxiao.xchat.service.api.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CardVo {

    private String name;
    private String giftPic;
    private String giftName;
    private List<CardChipVo> chipList;
}
