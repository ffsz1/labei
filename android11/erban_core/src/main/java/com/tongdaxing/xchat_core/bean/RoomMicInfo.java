package com.tongdaxing.xchat_core.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p> 房间麦序（坑位）信息实体 </p>
 *
 * @author jiahui
 * @date 2017/12/13
 */
public class RoomMicInfo implements Parcelable {


    /** 当前坑位位置 */
    private int position;
    /** 当前坑位是否锁住，0：开锁，1：闭锁 */
    private int posState;
    /** 当前坑位是否开麦，0：开麦，1：闭麦 */
    private int micState;

    protected RoomMicInfo(Parcel in) {
        position = in.readInt();
        posState = in.readInt();
        micState = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeInt(posState);
        dest.writeInt(micState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RoomMicInfo> CREATOR = new Creator<RoomMicInfo>() {
        @Override
        public RoomMicInfo createFromParcel(Parcel in) {
            return new RoomMicInfo(in);
        }

        @Override
        public RoomMicInfo[] newArray(int size) {
            return new RoomMicInfo[size];
        }
    };


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

    /**
     * 坑位是否锁了
     *
     * @return true:锁了，false：没锁
     */
    public boolean isMicLock() {
        return 1 == posState;
    }


    /**
     * 坑位是否闭麦了
     *
     * @return true：闭麦，false：开麦
     */
    public boolean isMicMute() {
        return 1 == micState;
    }

    @Override
    public String toString() {
        return "RoomMicInfo{" +
                "position=" + position +
                ", posState=" + posState +
                ", micState=" + micState +
                '}';
    }

}
