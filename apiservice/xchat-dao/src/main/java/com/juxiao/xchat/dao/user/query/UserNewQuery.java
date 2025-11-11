package com.juxiao.xchat.dao.user.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNewQuery {
    private Long uid;
    private Byte gender;
    private Integer startRecord;
    private Integer pageSize;

    public UserNewQuery(Long uid, Byte gender, Integer pageNum, Integer pageSize) {
        this.uid = uid;
        this.gender = gender;
        this.pageSize = pageSize;
        this.startRecord = (pageNum - 1) * pageSize;
    }
}
