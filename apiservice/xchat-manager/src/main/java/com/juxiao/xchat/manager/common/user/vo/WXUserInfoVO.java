package com.juxiao.xchat.manager.common.user.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WXUserInfoVO {
    private String city;
    private String country;
    private String headimgurl;
    private String nickname;
    private String openid;
    private List<String> privilege;
    private String province;
    private String sex;
    private String unionid;

    @Override
    public String toString() {
        return "WXUserInfoVO{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", openid='" + openid + '\'' +
                ", privilege=" + privilege +
                ", province='" + province + '\'' +
                ", sex='" + sex + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
