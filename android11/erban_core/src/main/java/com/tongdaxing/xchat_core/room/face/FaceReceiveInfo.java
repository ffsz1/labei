package com.tongdaxing.xchat_core.room.face;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenran
 * @date 2017/9/12
 * 房间里面其中一个人发运气表情，其他人通过云信收到对应的运气表情的java bean
 */

public class FaceReceiveInfo implements Serializable {
    private long uid;
    private String nick;
    private int faceId;
    private List<Integer> resultIndexes;


    public List<Integer> getResultIndexes() {
        return resultIndexes;
    }

    public void setResultIndexes(List<Integer> resultIndexes) {
        this.resultIndexes = resultIndexes;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "FaceReceiveInfo{" +
                "uid=" + uid +
                ", nick='" + nick + '\'' +
                ", faceId=" + faceId +
                ", resultIndexes=" + resultIndexes +
                '}';
    }
}
