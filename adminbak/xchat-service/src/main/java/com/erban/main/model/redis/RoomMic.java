package com.erban.main.model.redis;

public class RoomMic {
    private int position;   // 麦所在位置
    private int posState;   // 坑位的状态，是否被锁，1：锁坑，0：开放
    private int micState;   // 麦序的状态，是否被禁，1：禁麦，0：开放

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosState() {
        return posState;
    }

    public void setPosState(int posState) {
        this.posState = posState;
    }

    public int getMicState() {
        return micState;
    }

    public void setMicState(int micState) {
        this.micState = micState;
    }
}
