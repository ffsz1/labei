package com.juxiao.xchat.service.api.play.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-06-01
 * @time 22:18
 */
@Data
public class MoraInfoVO {

    private Integer num;

    private List<GiftInfoVO> giftInfoVOList;

    private Integer moraTime;


}
