package com.juxiao.xchat.dao.mcoin.domain;

import lombok.Data;

import java.util.Date;

@Data
public class McoinLevelRecordDO {

    private Long id;
    private Long uid;
    private Integer level;
    private Integer mcoinNum;
    private Date createTime;
    private Date updateTime;

    public McoinLevelRecordDO() {
    }

    public McoinLevelRecordDO(Long uid, Integer level, Integer mcoinNum) {
        this.uid = uid;
        this.level = level;
        this.mcoinNum = mcoinNum;
    }
}
