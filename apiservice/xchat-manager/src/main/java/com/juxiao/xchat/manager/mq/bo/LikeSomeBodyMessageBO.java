package com.juxiao.xchat.manager.mq.bo;

import lombok.Data;

import java.util.Date;

@Data
public class LikeSomeBodyMessageBO {

    private Long id;
    private Long likedUid;
    private Long likeUid;
    private Date createDate;
}
