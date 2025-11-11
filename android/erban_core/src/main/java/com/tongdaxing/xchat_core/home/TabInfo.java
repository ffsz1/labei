package com.tongdaxing.xchat_core.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> tab 标签数据 </p>
 *
 * @author Administrator
 * @date 2017/11/21
 */
public class TabInfo implements Parcelable {

    /**
     * id : 8
     * name : 聊天
     * pict : https://img.erbanyy.com/tag%E8%81%8A%E5%A4%A9.png
     * seq : 3
     * type : 1
     * status : true
     * istop : true
     * createTime : 1511155717000
     */

    private int id;
    private String name;
    private String pict;
    private int seq;
    private int type;
    private boolean status;
    private boolean istop;
    private long createTime;

    public TabInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }


    protected TabInfo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        pict = in.readString();
        seq = in.readInt();
        type = in.readInt();
        status = in.readByte() != 0;
        istop = in.readByte() != 0;
        createTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(pict);
        dest.writeInt(seq);
        dest.writeInt(type);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeByte((byte) (istop ? 1 : 0));
        dest.writeLong(createTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TabInfo> CREATOR = new Creator<TabInfo>() {
        @Override
        public TabInfo createFromParcel(Parcel in) {
            return new TabInfo(in);
        }

        @Override
        public TabInfo[] newArray(int size) {
            return new TabInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isIstop() {
        return istop;
    }

    public void setIstop(boolean istop) {
        this.istop = istop;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TabInfo{" +
                "name='" + name + '\'' +
                '}';
    }
}
