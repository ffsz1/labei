package com.juxiao.xchat.dao.user.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDrawDO {
    private Long uid;
    private Integer leftDrawNum;
    private Integer totalDrawNum;
    private Integer totalWinDrawNum;
    private Boolean isFirstShare;
    private Date createTime;
    private Date updateTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}