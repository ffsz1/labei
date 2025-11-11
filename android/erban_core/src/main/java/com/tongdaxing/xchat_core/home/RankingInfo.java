package com.tongdaxing.xchat_core.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * <p> 首页排行信息 </p>
 * Created by Administrator on 2017/11/8.
 */
public class RankingInfo implements Parcelable {

    public List<Ranking> starList;
    public List<Ranking> nobleList;
    public List<Ranking> roomList;

    @Override
    public String toString() {
        return "RankingInfo{" +
                "starList=" + starList +
                ", nobleList=" + nobleList +
                ", roomList=" + roomList +
                '}';
    }

    public static class Ranking implements Parcelable {
        @Override
        public String toString() {
            return "Ranking{" +
                    "erbanNo=" + erbanNo +
                    ", avatar='" + avatar + '\'' +
                    ", nick='" + nick + '\'' +
                    ", gender=" + gender +
                    ", totalNum=" + totalNum +
                    '}';
        }

        /**
         * erbanNo : 850379
         * avatar : https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=
         * nick : 阿拉善
         * gender : 1
         * totalNum : 9988923
         */

        private int erbanNo;
        private String avatar;
        private String nick;
        private int gender;
        private int totalNum;

        protected Ranking(Parcel in) {
            erbanNo = in.readInt();
            avatar = in.readString();
            nick = in.readString();
            gender = in.readInt();
            totalNum = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(erbanNo);
            dest.writeString(avatar);
            dest.writeString(nick);
            dest.writeInt(gender);
            dest.writeInt(totalNum);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Ranking> CREATOR = new Creator<Ranking>() {
            @Override
            public Ranking createFromParcel(Parcel in) {
                return new Ranking(in);
            }

            @Override
            public Ranking[] newArray(int size) {
                return new Ranking[size];
            }
        };

        public int getErbanNo() {
            return erbanNo;
        }

        public void setErbanNo(int erbanNo) {
            this.erbanNo = erbanNo;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }


    protected RankingInfo(Parcel in) {
        starList = in.createTypedArrayList(Ranking.CREATOR);
        nobleList = in.createTypedArrayList(Ranking.CREATOR);
        roomList = in.createTypedArrayList(Ranking.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(starList);
        dest.writeTypedList(nobleList);
        dest.writeTypedList(roomList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RankingInfo> CREATOR = new Creator<RankingInfo>() {
        @Override
        public RankingInfo createFromParcel(Parcel in) {
            return new RankingInfo(in);
        }

        @Override
        public RankingInfo[] newArray(int size) {
            return new RankingInfo[size];
        }
    };
}
