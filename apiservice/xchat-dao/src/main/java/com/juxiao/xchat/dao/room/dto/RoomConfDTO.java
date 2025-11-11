package com.juxiao.xchat.dao.room.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RoomConfDTO {
    /**
     * 房主UID
     */
    private Long roomUid;
    /**
     * 房间类型：1，捡海螺厅；2，普通房间；
     */
    private Byte roomType;
    /**
     * 房间捡海螺概率：1，低概率；2；高概率
     */
    private Byte drawType;

    /**
     * 魅力值开关：1，关闭；2开启
     */
    private Byte charmEnable;


}
