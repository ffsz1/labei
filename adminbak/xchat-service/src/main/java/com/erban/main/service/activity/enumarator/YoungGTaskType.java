package com.erban.main.service.activity.enumarator;

public enum YoungGTaskType {

    like_user(1, "关注一位小伙伴"),

    go_in_3_room(2, "逛3个房间"),

    share_type(3, "分享拉贝语音到朋友圈"),

    send_a_q_youngg(4, "给一位小伙伴送出Q版养鸡");
    /**
     * 任务ID
     */
    private int taskId;
    /**
     * 任务名称
     */
    private String taskName;

    YoungGTaskType(int taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }
}
