package com.erban.main.message;


import java.io.Serializable;

/**
 * 房间公屏消息
 */
public class RoomMessage implements Serializable {
    private String messId;    // 消息唯一标识
    private Long messTime;    // 消息创建时间
    private Long uid;
    private Long roomId;
    private Object data;

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public Long getMessTime() {
        return messTime;
    }

    public void setMessTime(Long messTime) {
        this.messTime = messTime;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RoomMessage{" +
                "messId='" + messId + '\'' +
                ", messTime=" + messTime +
                ", uid=" + uid +
                ", roomId=" + roomId +
                ", data=" + data +
                '}';
    }
}
