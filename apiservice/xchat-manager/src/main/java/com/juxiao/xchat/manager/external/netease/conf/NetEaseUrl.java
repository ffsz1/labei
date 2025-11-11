package com.juxiao.xchat.manager.external.netease.conf;

/**
 * 云信URL配置
 */
public class NetEaseUrl {

    /**
     * URL 前缀
     */
    public static final String URL_PREFIX = "https://api.netease.im";

    /**
     * 创建聊天室
     */
    public static final String ROOM_CREATE = "/nimserver/chatroom/create.action";

    public static final String ROOM_GET = "/nimserver/chatroom/get.action";

    /**
     * 切换聊天室开/关状态
     */
    public static final String ROOM_TOGGLE_STAT = "/nimserver/chatroom/toggleCloseStat.action";

    /**
     * 列出队列中的所有元素
     */
    public static final String ROOM_QUEUE_LIST = "/nimserver/chatroom/queueList.action";

    /**
     * 从队列中取出元素弹出元素
     */
    public static final String ROOM_QUEUE_POLL = "/nimserver/chatroom/queuePoll.action";

    /**
     *
     */
    public static final String ROOM_SEND_MSG = "/nimserver/chatroom/sendMsg.action";

    /**
     * 更新聊天室信息
     */
    public static final String ROOM_UPDATE = "/nimserver/chatroom/update.action";

    /**
     * 删除清理整个队列
     */
    public static final String ROOM_QUEUE_DROP = "/nimserver/chatroom/queueDrop.action";

    /**
     * 往聊天室有序队列中新加或更新元素
     */
    public static final String ROOM_QUEUE_OFFER = "/nimserver/chatroom/queueOffer.action";

    /**
     * 设置临时禁言状态
     */
    public static final String ROOM_TEMPORARY_MUTE = "/nimserver/chatroom/temporaryMute.action";

    public static final String ADD_ROBOT = "/nimserver/chatroom/addRobot.action";

    public static final String REMOVE_ROBOT = "/nimserver/chatroom/removeRobot.action";

    public static final String CREATE_USER = "/user/create.action";

    /**
     * 批量获取在线成员信息
     */
    public static final String ROOM_QUERY_MEMBERS = "/nimserver/chatroom/queryMembers.action";

}
