package com.juxiao.xchat.manager.common.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QQUserInfoVO {
    private String city;
    private String constellation;
    private String figureurl;
    private String figureurl_1;
    private String figureurl_2;
    private String figureurl_qq_1;
    private String figureurl_qq_2;
    private String gender;
    private String is_lost;
    private String is_yellow_vip;
    private String is_yellow_year_vip;
    private String level;
    private String msg;
    private String nickname;
    private String province;
    private String ret;
    private String vip;
    private String year;
    private String yellow_vip_level;

    @Override
    public String toString() {
        return "QQUserInfoVO{" +
                "city='" + city + '\'' +
                ", constellation='" + constellation + '\'' +
                ", figureurl='" + figureurl + '\'' +
                ", figureurl_1='" + figureurl_1 + '\'' +
                ", figureurl_2='" + figureurl_2 + '\'' +
                ", figureurl_qq_1='" + figureurl_qq_1 + '\'' +
                ", figureurl_qq_2='" + figureurl_qq_2 + '\'' +
                ", gender='" + gender + '\'' +
                ", is_lost='" + is_lost + '\'' +
                ", is_yellow_vip='" + is_yellow_vip + '\'' +
                ", is_yellow_year_vip='" + is_yellow_year_vip + '\'' +
                ", level='" + level + '\'' +
                ", msg='" + msg + '\'' +
                ", nickname='" + nickname + '\'' +
                ", province='" + province + '\'' +
                ", ret='" + ret + '\'' +
                ", vip='" + vip + '\'' +
                ", year='" + year + '\'' +
                ", yellow_vip_level='" + yellow_vip_level + '\'' +
                '}';
    }
}
