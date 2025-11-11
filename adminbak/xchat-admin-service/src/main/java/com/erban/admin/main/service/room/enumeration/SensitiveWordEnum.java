package com.erban.admin.main.service.room.enumeration;

/**
 * @Author: alwyn
 * @Description: 敏感词类型
 * @Date: 2018/10/23 10:35
 */
public enum SensitiveWordEnum {

    /** 房间标题 */
    room_title(1),
    /** 聊天 */
    chat(2),
    /** 房间玩法 */
    room_play_info(3),
    /** 昵称 */
    nick(4)
    ;

    SensitiveWordEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private int type;
}
