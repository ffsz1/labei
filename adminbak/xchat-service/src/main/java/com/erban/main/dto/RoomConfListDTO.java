package com.erban.main.dto;


public class RoomConfListDTO {

    private Long roomUid;

    private Long roomId;

    private String title;

    private Byte roomType;

    private Byte drawType;
    /**
     * 魅力值开关：1，关闭；2开启
     */
    private Byte charmEnable;

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getRoomType() {
        return roomType;
    }

    public void setRoomType(Byte roomType) {
        this.roomType = roomType;
    }

    public Byte getDrawType() {
        return drawType;
    }

    public void setDrawType(Byte drawType) {
        this.drawType = drawType;
    }

    public Byte getCharmEnable() {
        return charmEnable;
    }

    public void setCharmEnable(Byte charmEnable) {
        this.charmEnable = charmEnable;
    }
}
