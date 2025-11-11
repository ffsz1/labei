package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @date 2019-07-03
 */
@Data
public class UsersPwdTeensMode {

    private Integer id;

    private Long uid;

    private String pwd;

    private Date createDate;
}
