package com.juxiao.xchat.dao.sysconf.enumeration;

public enum SysConfigId {
    auditing_version,

    cur_gift_version,

    cur_gift_car_version,

    face_version,

    homerecomm_sort_type,

    draw_act_switch,

    splash_pict,

    splash_link,

    splash_type,

    newest_version,

    is_exchange_awards,

    lottery_box_option,

    wxapp_luxury_gift,

    timestamps,

    mic_in_list_option,

    green_room_index,

    kick_waiting,

    lottery_box_big_gift,

    two_week_start,

    two_week_end,
    /**
     * 敏感词
     */
    sensitive_word,

    hour_recom,
    /**
     * 推荐房间-开关
     */
    rcmd_room_option,
    /**
     * 小程序审核版本
     */
    wx_auditing_version,
    /**
     * 小程序顶部房间标签
     */
    wx_top_tag,
    /**
     * 用户抽iphone xs开关
     */
    user_draw_iponexs_switch,

    /** 捡海螺高概率开关 */
    gift_draw_pr_option,
    /** 捡海螺礼物数量限制 */
    gift_draw_max_num,
    /** 捡海螺iphone开关 */
    gift_draw_iphone_option,
    /** 捡海螺出现iphone的捡海螺次数 */
    gift_draw_iphone_draw_num,
    //
    /** 捡海螺必出全服的捡海螺数量 */
    gift_draw_be_full_num,
    /** 捡海螺必出全服的开关 */
    gift_draw_be_full_option,
    /** 捡海螺低概率开关 */
    gift_draw_left_pro_option,
    /** 捡海螺活动概率开关 */
    gift_draw_event_option,
    /** 捡海螺活动全服礼物 */
    gift_draw_event_gift,
    /** 捡海螺全服最小等级 */
    gift_draw_left_level_num,

    /**
     *  文字抽奖扭蛋是否生效开关
     */
    word_draw_niu_dan_enable_option,

    /** 批量发送消息的开关 */
    batch_msg_option,

    pay_channel,

    pay_money,

    /**
     * 实名验证开关，0为关闭，1为开启
     */
    real_name_option,
    /**
     * 创建房间验证开关，0为关闭，1为实名，2为绑定手机号
     */
    real_name_option_openroom,
    /**
     * 房间公屏发言开关，0为关闭，1为实名，2为绑定手机号
     */
    real_name_option_sendtext,
    /**
     * 公聊大厅发言开关，0为关闭，1为实名，2为绑定手机号
     */
    real_name_option_sendpublic,

    mcoin_switch,
    /** 禁止修改资料*/
    prohibit_modification,

    /** 公聊大厅倒计时 (S)*/
    public_chat_hall_time,
    /**发送图片等级*/
    send_pic_left_level,
    /**支付宝开关  1开 2 关*/
    alipay_switch,
    mora_timeout,
    gift_car_switch,
    ali_switch,
    /**陪陪列表魅力值限定*/
    define_glamour,
    /**开房icon 是否显示*/
    open_room_icon_status,
    /**相亲tagId */
    xq_tag_id
    ;
}
