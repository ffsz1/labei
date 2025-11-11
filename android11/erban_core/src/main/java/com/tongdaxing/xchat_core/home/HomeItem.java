package com.tongdaxing.xchat_core.home;


import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import lombok.Data;

/**
 * <p> 首页热门实体u </p>
 *
 * @author Administrator
 * @date 2017/11/16
 */

@Data
public class HomeItem implements MultiItemEntity {

    public static final int BANNER = 1;
    public static final int RANKING = 2;
    public static final int RECOMMEND = 3;
    public static final int NORMAL = 4;
    public static final int HOT_INDEX_BANNER = 5;

    private int itemType;

    //正常数据
    public List<HomeRoom> recomList;
    public List<HomeRoom> homeItemRoomList;
    public List<BannerInfo> bannerInfoList;
    public RankingInfo mRankingInfo;
}
