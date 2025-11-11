package com.juxiao.xchat.dao.room.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 房间背景实体
 * @Date: 2018/10/9 11:12
 */
@Data
public class RoomBgDO implements Comparable<RoomBgDO> {

    private Integer id;
    private String picUrl;
    private Date beginDate;
    private Date endDate;
    private Date createDate;
    private Integer sortNo;
    private String status;
    private Integer type;
    private String name;
    private List<Long> uids;
    private List<Integer> tagIds;

    @Override
    public int compareTo(RoomBgDO o) {
        return this.sortNo - o.sortNo;
    }
}
