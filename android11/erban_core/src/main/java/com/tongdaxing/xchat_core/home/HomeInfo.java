package com.tongdaxing.xchat_core.home;

import java.util.List;

/**
 * <p> 首页数据 </p>
 *
 * @author Administrator
 * @date 2017/11/22
 */
public class HomeInfo {

    public List<BannerInfo> banners;
    /**
     * 排行数据
     */
    public RankingInfo rankHome;
    /**
     * 热门推荐
     */
    public List<HomeRoom> hotRooms;
    /**
     * 房间推荐
     */
    public List<HomeRoom> listRoom;
    /**
     * 首页新推荐房
     */
    public List<HomeRoom> agreeRecommendRooms;

    public List<HomeIcon> homeIcons;

    public List<HomeRoom> listGreenRoom;

    public List<HomeRoom> recommendRooms;



    //首页显示view的类型（0为正常显示，1为简单显示）
    public int viewType;

    @Override
    public String toString() {
        return "HomeInfo{" +
                "banners=" + banners +
                ", rankHome=" + rankHome +
                ", hotRooms=" + hotRooms +
                ", listRoom=" + listRoom +
                ", homeIcons=" + homeIcons +
                ", listGreenRoom=" + listGreenRoom +
                ", recommendRooms=" + recommendRooms +
                ", viewType=" + viewType +
                '}';
    }
}
