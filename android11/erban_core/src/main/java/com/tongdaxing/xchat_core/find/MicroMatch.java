package com.tongdaxing.xchat_core.find;

import java.io.Serializable;

import lombok.Data;

/**
 * @author dell
 */
@Data
public class MicroMatch implements Serializable {

    private long uid;
    private long erbanNo;
    private String nick;
    private int gender;
    private long birth;
    private String avatar;
    private int fansNum;
    private int operatorStatus;
    private String userDesc;
    private String userVoice;
    private int voiceDura;
    //财富等级
    private int experLevel;
    //魅力等级
    private int charmLevel;

    private boolean isLike;

    @Override
    public String toString() {
        return "MicroMatch{" +
                "uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", gender=" + gender +
                ", birth=" + birth +
                ", avatar='" + avatar + '\'' +
                ", fansNum=" + fansNum +
                ", operatorStatus=" + operatorStatus +
                ", userDesc='" + userDesc + '\'' +
                ", userVoice='" + userVoice + '\'' +
                ", voiceDura=" + voiceDura +
                ", experLevel=" + experLevel +
                ", charmLevel=" + charmLevel +
                ", isLike=" + isLike +
                '}';
    }
}
