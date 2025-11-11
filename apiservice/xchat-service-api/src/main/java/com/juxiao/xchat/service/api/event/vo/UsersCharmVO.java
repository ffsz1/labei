package com.juxiao.xchat.service.api.event.vo;

import lombok.Data;

/**
 * @author chris
 * @date 2019-07-10
 */
@Data
public class UsersCharmVO {

    private Long uid;

    private Long erbanNo;

    private String nick;

    private String avatar;

    private Integer rank;

    private Integer totalNum;
}
