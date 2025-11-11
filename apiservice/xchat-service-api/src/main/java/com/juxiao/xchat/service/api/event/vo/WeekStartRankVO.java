package com.juxiao.xchat.service.api.event.vo;

import lombok.Data;

/**
 * @author chris
 * @Title: 自己周星排名
 * @date 2019-05-20
 * @time 10:44
 */
@Data
public class WeekStartRankVO {

    private Long uid;

    private Long erbanNo;

    private String avatar;

    private String nick;

    private Long rank;

    private Byte gender;

    private Double total;
}
