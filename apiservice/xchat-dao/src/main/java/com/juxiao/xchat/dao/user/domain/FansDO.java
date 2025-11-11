package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FansDO {
    private Long fanId;
    private Long likeUid;
    private Long likedUid;
    private Date createTime;
}