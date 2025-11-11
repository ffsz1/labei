package com.erban.admin.main.service.room.enumeration;

/**
 * @Description: 推荐位的显示类型
 * @Author: alwyn
 * @Date: 2018/11/23 11:18
 */
public enum ViewTypeEnum {

    /** 首页热门推荐 */
    home_index(1),
    /** 活位推荐 */
    advert(2);

    ViewTypeEnum(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }
}
