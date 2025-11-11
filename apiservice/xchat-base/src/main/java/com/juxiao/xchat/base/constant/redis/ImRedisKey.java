package com.juxiao.xchat.base.constant.redis;

import org.apache.commons.lang.StringUtils;

public enum ImRedisKey {
    /**
     * uid和socket对应列表
     */
    user_map_socket_key,
    /**
     * 存房间对应成员信息
     */
    room_map_queue_mem_key,
    /**
     * 房间对应用户key
     */
    roomid_map_uid_key,
    /**
     * 用户socketid对应房间ID
     */
    socket_map_room_key,
    /**
     * 房间信息
     */
    room_info
    ;


    public String getKey() {
        return ("imxinpi_" + name()).toLowerCase();
    }

    public String getKey(String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            return getKey();
        } else {
            return getKey() + "_" + suffix;
        }
    }
}
