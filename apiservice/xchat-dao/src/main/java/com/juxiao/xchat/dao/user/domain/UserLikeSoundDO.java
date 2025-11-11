package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/11/26 18:28
 */
@Data
public class UserLikeSoundDO {

    private Long uid;
    private Long likeUid;
    private Date createDate;
}
