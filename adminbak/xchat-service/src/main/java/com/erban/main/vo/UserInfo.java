package com.erban.main.vo;


import java.util.Arrays;

/**
 * 微信用户信息
 */
/*
{
   "subscribe": 1,
   "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M",
   "nickname": "Band",
   "sex": 1,
   "language": "zh_CN",
   "city": "广州",
   "province": "广东",
   "country": "中国",
   "headimgurl":  "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4
eMsv84eavHiaiceqxibJxCfHe/0",
  "subscribe_time": 1382694957,
  "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
  "remark": "",
  "groupid": 0,
  "tagid_list":[128,2]
}

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
