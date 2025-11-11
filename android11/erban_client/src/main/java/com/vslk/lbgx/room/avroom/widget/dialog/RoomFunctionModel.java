package com.vslk.lbgx.room.avroom.widget.dialog;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.bean.RoomFunctionBean;
import com.tongdaxing.xchat_core.room.bean.RoomFunctionEnum;

public class RoomFunctionModel {
    private int publicChatSwitch;
    private int vehicleSwitch;
    private int giftSwitch;

    /**
     * @param roleType         -1是普通成员功能，0是房主功能，1是管理员功能
     * @param publicChatSwitch 0是关闭，1是开启
     * @param vehicleSwitch    0展示坐骑礼物，1关闭坐骑礼物
     * @param giftSwitch       0展示小礼物，1关闭小礼物
     * @param isMusic          true 显示音乐
     * @return
     */
    public RoomFunctionEnum[] getRoomFunctionByType(int roleType, int publicChatSwitch, int vehicleSwitch, int giftSwitch, boolean isMusic) {
        this.publicChatSwitch = publicChatSwitch;
        this.vehicleSwitch = vehicleSwitch;
        this.giftSwitch = giftSwitch;
        switch (roleType) {
            case 0://房主 房间设置 关闭公屏 关闭座驾 关闭礼物 进房提示 管理员 最小化  退出
                return new RoomFunctionEnum[]{RoomFunctionEnum.ROOM_MUSIC, RoomFunctionEnum.ROOM_SETTING,
                        publicChatSwitch == 0 ? RoomFunctionEnum.ROOM_PUBLIC_CLOSE : RoomFunctionEnum.ROOM_PUBLIC_OPEN,
                        vehicleSwitch == 0 ? RoomFunctionEnum.ROOM_VEHICLE_CLOSE : RoomFunctionEnum.ROOM_VEHICLE_OPEN,
                        giftSwitch == 0 ? RoomFunctionEnum.ROOM_GIFT_CLOSE : RoomFunctionEnum.ROOM_GIFT_OPEN,
                        RoomFunctionEnum.ROOM_ENTER_HINT, /*RoomFunctionEnum.ROOM_ANMIN_MANAGER,*/
                        RoomFunctionEnum.ROOM_MINIMIZE, RoomFunctionEnum.ROOM_QUIT};

            case 1://房主 房间设置 打开公屏 打开座驾 打开礼物 进房提示 管理员 最小化  退出
                return new RoomFunctionEnum[]{RoomFunctionEnum.ROOM_MUSIC, RoomFunctionEnum.ROOM_SETTING,
                        publicChatSwitch == 0 ? RoomFunctionEnum.ROOM_PUBLIC_CLOSE : RoomFunctionEnum.ROOM_PUBLIC_OPEN,
                        vehicleSwitch == 0 ? RoomFunctionEnum.ROOM_VEHICLE_CLOSE : RoomFunctionEnum.ROOM_VEHICLE_OPEN,
                        giftSwitch == 0 ? RoomFunctionEnum.ROOM_GIFT_CLOSE : RoomFunctionEnum.ROOM_GIFT_OPEN,
                        RoomFunctionEnum.ROOM_ENTER_HINT, RoomFunctionEnum.ROOM_MINIMIZE,
                        RoomFunctionEnum.ROOM_QUIT};

            default://举报房间  最小化  退出 -- 默认
                if (isMusic) {
                    return new RoomFunctionEnum[]{RoomFunctionEnum.ROOM_MUSIC, RoomFunctionEnum.ROOM_REPORT, RoomFunctionEnum.ROOM_QUIT};
                } else {
                    return new RoomFunctionEnum[]{RoomFunctionEnum.ROOM_REPORT, RoomFunctionEnum.ROOM_QUIT};
                }
        }
    }


    public RoomFunctionBean getFunctionBean(RoomFunctionEnum type) {
        RoomFunctionBean functionBean = new RoomFunctionBean();
        functionBean.setPublicChatSwitch(publicChatSwitch);
        functionBean.setVehicleSwitch(vehicleSwitch);
        functionBean.setGiftSwitch(giftSwitch);
        functionBean.setFunctionType(type);
        switch (type) {
            case ROOM_MUSIC:
                functionBean.setImgRes(R.mipmap.ic_room_music);
                functionBean.setTitle("音乐");
                break;
            case ROOM_REPORT:
                functionBean.setImgRes(R.mipmap.ic_room_report);
                functionBean.setTitle("举报");
                break;
            case ROOM_MINIMIZE:
                functionBean.setImgRes(R.mipmap.ic_room_minimize);
                functionBean.setTitle("魅力值清零");
                break;
            case ROOM_SETTING:
                functionBean.setImgRes(R.mipmap.ic_room_setting);
                functionBean.setTitle("房间设置");
                break;
            case ROOM_PUBLIC_CLOSE:
                functionBean.setImgRes(R.mipmap.ic_public_screen_close);
                functionBean.setTitle("关闭公屏");
                break;
            case ROOM_PUBLIC_OPEN:
                functionBean.setImgRes(R.mipmap.ic_public_screen_open);
                functionBean.setTitle("打开公屏");
                break;
            case ROOM_VEHICLE_CLOSE:
                functionBean.setImgRes(R.mipmap.ic_vehicle_close);
                functionBean.setTitle("关闭座驾");
                break;
            case ROOM_VEHICLE_OPEN:
                functionBean.setImgRes(R.mipmap.ic_vehicle_open);
                functionBean.setTitle("打开座驾");
                break;
            case ROOM_GIFT_CLOSE:
                functionBean.setImgRes(R.mipmap.ic_gift_close);
                functionBean.setTitle("关闭礼物");
                break;
            case ROOM_GIFT_OPEN:
                functionBean.setImgRes(R.mipmap.ic_gift_open);
                functionBean.setTitle("打开礼物");
                break;
            case ROOM_ANMIN_MANAGER:
                functionBean.setImgRes(R.mipmap.ic_admin_manager);
                functionBean.setTitle("管理员");
                break;
            case ROOM_ENTER_HINT:
                functionBean.setImgRes(R.mipmap.ic_enter_room_hint);
                functionBean.setTitle("进房提示");
                break;
            default:
                functionBean.setImgRes(R.mipmap.ic_quit_room);
                functionBean.setTitle("退出");
                break;
        }
        return functionBean;
    }
}
