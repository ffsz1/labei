package com.juxiao.xchat.dao.user.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDrawRecordDO {
    private Integer recordId;
    private Long uid;
    private Byte drawStatus;
    private Byte type;
    private String srcObjId;
    private String srcObjName;
    private Long srcObjAmount;
    private Integer drawPrizeId;
    private String drawPrizeName;
    private Byte drawPrizePutout;
    private String recordDesc;
    private Date createTime;
    private Date updateTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}