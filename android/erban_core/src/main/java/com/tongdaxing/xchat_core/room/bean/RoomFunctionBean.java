package com.tongdaxing.xchat_core.room.bean;

public class RoomFunctionBean {
    private RoomFunctionEnum functionType = RoomFunctionEnum.ROOM_QUIT;
    private String title;
    private int imgRes;
    private String imgUrl;
    private int publicChatSwitch;
    private int vehicleSwitch;
    private int giftSwitch;

    public int getPublicChatSwitch() {
        return publicChatSwitch;
    }

    public void setPublicChatSwitch(int publicChatSwitch) {
        this.publicChatSwitch = publicChatSwitch;
    }

    public int getVehicleSwitch() {
        return vehicleSwitch;
    }

    public void setVehicleSwitch(int vehicleSwitch) {
        this.vehicleSwitch = vehicleSwitch;
    }

    public int getGiftSwitch() {
        return giftSwitch;
    }

    public void setGiftSwitch(int giftSwitch) {
        this.giftSwitch = giftSwitch;
    }

    public RoomFunctionEnum getFunctionType() {
        return functionType;
    }

    public void setFunctionType(RoomFunctionEnum functionType) {
        this.functionType = functionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "RoomFunctionBean{" +
                "functionType=" + functionType +
                ", title='" + title + '\'' +
                ", imgRes=" + imgRes +
                ", imgUrl='" + imgUrl + '\'' +
                ", publicChatSwitch=" + publicChatSwitch +
                ", vehicleSwitch=" + vehicleSwitch +
                ", giftSwitch=" + giftSwitch +
                '}';
    }
}
