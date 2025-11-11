package com.tongdaxing.xchat_core.room.queue.bean;

import android.support.annotation.IntDef;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 房间坑位状态信息
 *
 * @author chenran
 * @date 2017/9/5
 */
public class RoomQueueInfo implements Serializable {

    public static final int CLOSE = 1;
    public static final int OPEN = 0;

    @IntDef({CLOSE, OPEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface QueueLockStatus {
    }

    @IntDef({CLOSE, OPEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface QueueMuteStatus {
    }

    /** 当前坑位状态:是否开锁 0--开锁， 1---闭锁 */
    public int lockStatus;
    /** 当前坑位是否静音 0---开麦， 1---闭麦 */
    public int muteStatus;

    private String inviteUid;
    private String position;


    public static final int QUEUE_TYPE_FREE = 0;
    public static final int QUEUE_TYPE_LOCK = 1;
    public static final String KEY_INVITEUID = "inviteUid";
    public static final String KEY_POSITION = "position";

    /** 当前坑位状态 */
    private int queueType;
    /** 是否静音 */
    private boolean isMute;

    public RoomQueueInfo(int queueType) {
        this.queueType = queueType;
        this.isMute = false;
    }

    public int getQueueType() {
        return queueType;
    }

    public void setQueueType(int queueType) {
        this.queueType = queueType;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public String getInviteUid() {
        return inviteUid;
    }

    public void setInviteUid(String inviteUid) {
        this.inviteUid = inviteUid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String toJsonString() {
        JSONObject object = new JSONObject();
        object.put("queueType", queueType);
        object.put("isMute", isMute);
        return object.toJSONString();
    }
}
