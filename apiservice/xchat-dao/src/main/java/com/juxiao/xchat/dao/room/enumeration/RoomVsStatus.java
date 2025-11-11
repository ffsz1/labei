package com.juxiao.xchat.dao.room.enumeration;

public enum RoomVsStatus {
    /**
     * 房间PK状态：0（进行中），1（倒计时结束），2（房主/管理员结束），3（PK用户掉线结束），4（用户下麦结束），5（用户退出房间结束）
     */
    ONGOING(0),
    END_BY_COUNTDOWN(1),
    END_BY_MANAGER(2),
    END_BY_OFFLINE(3),
    END_BY_LEAVEMIC(4),
    END_BY_QUIT(5);

    RoomVsStatus(int value){
        this.value = value;
    }
    private int value;

    public Integer getValue(){
        return value;
    }
}
