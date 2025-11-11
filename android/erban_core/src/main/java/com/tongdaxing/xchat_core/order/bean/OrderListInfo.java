package com.tongdaxing.xchat_core.order.bean;

/**
 * Created by zhouxiangfeng on 2017/6/15.
 */

public class OrderListInfo {

    /*
    "orderId": 1,
      "uid": 900089,
      "prodUid": 900100,
      "objId": "6fdfc372e03f46dd8fb1755a80ed971d",
      "curStatus": 1
    "totalMoney": 4000
    "servDura": 30
    “userImg”:xx 用户头像
    “prodImg”:xx 声优头像
    “prodName”:xx声优名字
    “userName”:xx用户名字
    “remainDay”:xx 剩余天数
     */

    private long uid;
    private long prodUid;
    private int orderId;
    //竞拍单号
    private String objId;
    private String sName;
    private String cName;
    private String userImg;
    private String prodImg;
    private String prodName;
    private String userName;
    private long remainDay;
    private int servDura;

    /**
     * 1订单生成（未处理），2订单处理中（服务进行中），3订单处理完成（服务完成），4订单状态异常
     */
    private int curStatus;
    private int totalMoney;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getProdUid() {
        return prodUid;
    }

    public void setProdUid(long prodUid) {
        this.prodUid = prodUid;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getProdImg() {
        return prodImg;
    }

    public void setProdImg(String prodImg) {
        this.prodImg = prodImg;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getRemainDay() {
        return remainDay;
    }

    public void setRemainDay(int remainDay) {
        this.remainDay = remainDay;
    }

    public int getServDura() {
        return servDura;
    }

    public void setServDura(int servDura) {
        this.servDura = servDura;
    }
}
