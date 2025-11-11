package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/11/7.
 */
public class RankHomeVo {
    private List<RankVo> starList;
    private List<RankVo> nobleList;
    private List<RankVo> roomList;

    public List<RankVo> getStarList() {
        return starList;
    }

    public void setStarList(List<RankVo> starList) {
        this.starList = starList;
    }

    public List<RankVo> getNobleList() {
        return nobleList;
    }

    public void setNobleList(List<RankVo> nobleList) {
        this.nobleList = nobleList;
    }

    public List<RankVo> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RankVo> roomList) {
        this.roomList = roomList;
    }
}
