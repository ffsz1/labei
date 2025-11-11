package com.tongdaxing.xchat_core.home;


import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import lombok.Data;

/**
 * <p> 首页热门实体u </p>
 *
 * @author Administrator
 * @date 2017/11/16
 */
@Data
public class HomeHotItem implements MultiItemEntity {
    public static final int NORMAL = 1;
    public static final int HOT_INDEX_BANNER = 2;

    private int itemType = 1;

    //正常数据
    HomeRoom hotRoom;
}
