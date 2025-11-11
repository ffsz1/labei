package com.juxiao.xchat.service.api.user.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 举报类型枚举
 * @author chris
 * @date 2019-06-30
 */
@Getter
@AllArgsConstructor
public enum  UsersReportType {

    /**
     *  政治敏感
     */
    POLITICALLY_SENSITIVE(1,"政治敏感"),
    /**
     * 色情低俗
     */
    EROTIC_VULGARITY(2,"色情低俗"),
    /**
     * 广告骚扰
     */
    ADVERTISING_HARASSMENT(3,"广告骚扰"),
    /**
     * 人身攻击
     */
    PERSONAL_ATTACKS(4,"人身攻击"),
    /**
     * 其他
     */
    OTHER(5,"其他"),

    /**
     * 昵称
     */
    PERSONAL_NICK(6,"昵称"),


    /**
     * 房间标题
     */
    PERSONAL_ROOM_TITLE(7,"房间标题");

    private int type;
    private String desc;


    /**
     * 根据类型获取举报名称
     * @param type
     * @return
     */
    public static UsersReportType of(final int type){
        UsersReportType[] usersReportTypes = UsersReportType.values();
        for(UsersReportType usersReportType : usersReportTypes){
            if(usersReportType.type == type){
                return usersReportType;
            }
        }
        return UsersReportType.OTHER;
    }

}
