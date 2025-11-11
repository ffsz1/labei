package com.erban.main.vo;

import com.erban.main.model.RoomTag;

import java.util.List;

public class HomeV2Vo {

    private List<BannerVo> banners; // Banner列表
    private List<IconVo> homeIcons;  // 热门下的房间列表
    private RankHomeVo rankHome;    // 排行榜列表
    private List<RoomVo> hotRooms;  // 热门房间列表
    private List<RoomVo> listRoom;  // 热门下的房间列表
    private List<RoomVo> listGreenRoom;  // 热门下的绿色房间列表
    private List<RoomTag> roomTagList;

    public List<RoomTag> getRoomTagList() {
        return roomTagList;
    }

    public void setRoomTagList(List<RoomTag> roomTagList) {
        this.roomTagList = roomTagList;
    }

    public List<BannerVo> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerVo> banners) {
        this.banners = banners;
    }

    public List<IconVo> getHomeIcons() {
        return homeIcons;
    }

    public void setHomeIcons(List<IconVo> homeIcons) {
        this.homeIcons = homeIcons;
    }

    public RankHomeVo getRankHome() {
        return rankHome;
    }

    public void setRankHome(RankHomeVo rankHome) {
        this.rankHome = rankHome;
    }

    public List<RoomVo> getHotRooms() {
        return hotRooms;
    }

    public void setHotRooms(List<RoomVo> hotRooms) {
        this.hotRooms = hotRooms;
    }

    public List<RoomVo> getListRoom() {
        return listRoom;
    }

    public void setListRoom(List<RoomVo> listRoom) {
        this.listRoom = listRoom;
    }

    public List<RoomVo> getListGreenRoom() {
        return listGreenRoom;
    }

    public void setListGreenRoom(List<RoomVo> listGreenRoom) {
        this.listGreenRoom = listGreenRoom;
    }
}
