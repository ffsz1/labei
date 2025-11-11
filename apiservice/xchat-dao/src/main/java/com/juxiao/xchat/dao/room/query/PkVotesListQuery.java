package com.juxiao.xchat.dao.room.query;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PkVotesListQuery {
    private Long roomId;
    private Integer startRecord;
    private Integer pageSize;

    public PkVotesListQuery(Long roomId, Integer pageNum, Integer pageSize) {
        this.roomId = roomId;
        this.pageSize = pageSize == null ? 20 : pageSize;
        this.startRecord = ((pageNum == null || pageNum <= 0 ? 1 : pageNum) - 1) * pageSize;
    }
}
