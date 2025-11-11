package com.tongdaxing.xchat_core.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenran on 2017/10/4.
 */

public class HomeRoomList implements Serializable{
    private List<HomeRoom> recomRooms;
    private List<HomeRoom> gameRooms;
    private List<HomeRoom> chatRooms;

    public List<HomeRoom> getRecomRooms() {
        return recomRooms;
    }

    public void setRecomRooms(List<HomeRoom> recomRooms) {
        this.recomRooms = recomRooms;
    }

    public List<HomeRoom> getGameRooms() {
        return gameRooms;
    }

    public void setGameRooms(List<HomeRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }

    public List<HomeRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<HomeRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }
}
