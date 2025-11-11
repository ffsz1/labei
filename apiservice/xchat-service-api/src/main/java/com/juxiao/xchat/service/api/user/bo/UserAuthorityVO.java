package com.juxiao.xchat.service.api.user.bo;

import lombok.Data;

@Data
public class UserAuthorityVO {
    private Long uid;
    private Long erbanNo;
    private String nick;
    private Boolean authority;// true:有权限, false:无权限
}
