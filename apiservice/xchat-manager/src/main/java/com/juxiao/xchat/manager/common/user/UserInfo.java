package com.juxiao.xchat.manager.common.user;


import java.util.Arrays;

/**
 * 微信用户信息
 */
public class UserInfo {

    private String openid;
    private String nickname;
    private Byte sex;
    private Integer[] privilege;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;


    public String getOpenid ( ) {
        return openid;
    }

    public void setOpenid ( String openid ) {
        this.openid = openid;
    }

    public String getNickname ( ) {
        return nickname;
    }

    public void setNickname ( String nickname ) {
        this.nickname = nickname;
    }

    public Byte getSex ( ) {
        return sex;
    }

    public void setSex ( Byte sex ) {
        this.sex = sex;
    }

    public Integer[] getPrivilege ( ) {
        return privilege;
    }

    public void setPrivilege ( Integer[] privilege ) {
        this.privilege = privilege;
    }

    public String getCity ( ) {
        return city;
    }

    public void setCity ( String city ) {
        this.city = city;
    }

    public String getProvince ( ) {
        return province;
    }

    public void setProvince ( String province ) {
        this.province = province;
    }

    public String getCountry ( ) {
        return country;
    }

    public void setCountry ( String country ) {
        this.country = country;
    }

    public String getHeadimgurl ( ) {
        return headimgurl;
    }

    public void setHeadimgurl ( String headimgurl ) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid ( ) {
        return unionid;
    }

    public void setUnionid ( String unionid ) {
        this.unionid = unionid;
    }

    @Override
    public String toString ( ) {
        return  "openid=" + openid +
                "&nickName=" + nickname +
                "&sex=" + sex +
                "&city=" + city +
                "&province=" + province +
                "&country=" + country +
                "&headimgurl=" + headimgurl +
                "&privilege=" + Arrays.toString ( privilege ) +
                "&unionid=" + unionid ;
    }
}
