package com.vslk.lbgx.room.avroom.other;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * Created by Administrator on 2018/3/23.
 */

public class BgTypeHelper {

    public static int getBgId(String type) {
        int id = R.mipmap.ic_room_bg_default;
        if (StringUtils.isEmpty(type)) {
            return id;
        }
        return id;
    }

    public static String getBgName(String type) {
        String name = "默认";
        switch (type) {
            case "1":
                name = "告白气球";
                break;
            case "2":
                name = "海洋";
                break;
            case "3":
                name = "流星";
                break;
            case "4":
                name = "梦幻";
                break;
            case "5":
                name = "晚霞";
                break;
            case "6":
                name = "相约";
                break;
            case "7":
                name = "夜曲";
                break;
            case "8":
                name = "音乐";
                break;
            case "9":
                name = "月亮";
                break;
            case "10":
                name = "经典";
                break;
            default:

        }
        return name;
    }

    public static int getDefaultBackRes() {
        return R.mipmap.ic_room_bg_default;
    }
}
