package com.tongdaxing.xchat_core.home;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouxiangfeng
 * @date 2017/5/17
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class HomeRoom extends RoomInfo implements MultiItemEntity {
    //性别 1:男 2：女 0 ：未知
    private int gender;
    //头像
    private String avatar;

    private String nick;

    private int seqNo;

    public int showLine = 0;

    public int itemType = 0;
    private int status = 0;

    private List<HomeRoom> recommendList;

    public PeiPeiBean peiPeiBean;

    //角标相关的
    public String badge;
}
