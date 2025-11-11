package com.juxiao.xchat.dao.user.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFansQuery {
    private Long likeUid;
    private Long likedUid;
    private Integer offset;
    private Integer pageSize;

    public void setPage(Integer pageNo, Integer pageSize) {
        this.pageSize = (pageSize == null ? 10 : pageSize);
        if (this.pageSize > 50) {
            this.pageSize = 50;
        }
        if (pageNo <= 0) {
            this.offset = 0;
        } else {
            this.offset = (pageNo - 1) * this.pageSize;
        }

    }
}
