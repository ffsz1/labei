package com.xchat.oauth2.service.sso.vo;

import com.alibaba.fastjson.JSONObject;
import com.xchat.oauth2.service.http.HttpUtils;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/8 19:21
 */
public class QQUnioidVO {

    private String client_id;
    private String openid;
    private String unionid;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "QQUnioidVO{" +
                "client_id='" + client_id + '\'' +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }

//    public static void main(String[] args) throws Exception {
//        String s = " {\"client_id\":\"1106890679\",\"openid\":\"E763A7DB4A6231FADE30BF6C7CE1A98A\",\"unionid\":\"UID_FB90ACDF6711BA3BAECC900554796922\"} ";
//        //
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("access_token=").append("AF3A78E68C66C3E67757269BA2BD179D");
//        sb.append("&unionid=1");
//        String result = HttpUtils.get("https://graph.qq.com/oauth2.0/me", sb.toString());
//        String[] ar = result.split("\\(");
//        ar = ar[1].split("\\)");
//        result = ar[0];
//        QQUnioidVO vo = JSONObject.parseObject(result, QQUnioidVO.class);
//        System.out.println(vo);
//    }
}
