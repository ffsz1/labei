package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/9/26.
 */
public class HomeVo {
    private List<RoomVo> recomRooms;
    private List<RoomVo> gameRooms;
    private List<RoomVo> chatRooms;

    public List<RoomVo> getGameRooms() {
        return gameRooms;
    }

    public void setGameRooms(List<RoomVo> gameRooms) {
        this.gameRooms = gameRooms;
    }

    public List<RoomVo> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<RoomVo> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public List<RoomVo> getRecomRooms() {
        return recomRooms;
    }

    public void setRecomRooms(List<RoomVo> recomRooms) {
        this.recomRooms = recomRooms;
    }
}
