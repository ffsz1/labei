package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FansDTO {
    private Long fanId;
    private Long likeUid;
    private Long likedUid;
    private Date createTime;
}