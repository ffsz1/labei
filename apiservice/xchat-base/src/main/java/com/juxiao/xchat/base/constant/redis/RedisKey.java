package com.juxiao.xchat.base.constant.redis;

import org.apache.commons.lang.StringUtils;

/**
 * redis 缓存key值常量
 */
public enum RedisKey {
    im_online_num,
    im_online_num_time,
    icon,
    icon_index,
    icon_index_v,
    icon_index_news,
    bonus_level,
    user_configure,
    user_purse_hot_room_record,
    user_purse_hot_room_record_list,
    /**
     * 用户喜欢的声音
     */
    user_like_sound,
    /**
     * 用户声音 (性别、年龄) 匹配
     */
    user_sound_gender_age_pool,
    /**
     * 用户声音匹配的配置
     */
    user_sound_config,
    /**
     * 用户喜欢声音的数量-天
     */
    user_like_num,
    user_word_draw_overview,
    user_word_draw_list,
    user_word_draw_conf,
    uid_access_token,
    uid_ticket,
    room,
    room_room_id,
    room_background,
    room_online_num,// 房间在线人数显示加上这个配置的数量
    room_hot,
    room_running,
    room_vip,
    room_permit_hide,// 需要在展示时隐藏的牌照房（在线人数为0或者只有机器人）
    room_pk_vote,
    room_pk_vote_count,
    room_pk_lock,
    room_pk_vote_set,
    room_pk_task_lock,
    room_pk_save_lock,
    room_tag_room,   // 房间设置标签列表
    room_tag_top,    // APP顶部标签列表
    room_tag_search,    // APP分类标签列表
    room_tag_list,   // 单个标签数据
    room_tag_new,    // 萌新标签页的第一页
    room_tag_radio,  // 电台标签页的第一页
    room_tag_index,  // 每个标签页的第一页
    //room_tag_first_charge,  // 没充值过的新秀列表
    room_tag_hot,   // 分类的热门列表
    room_home_new,  // 首页的新秀列表
    room_mic_list,   // 房间麦序列表
    room_pos_lock,   // 房间麦序占位临时的KEY，防多人并发抢
    home_hot_recom,  // 首页热门推荐
    home_room_list,  // 首页热门推荐下的房间列表
    green_room_list,  // 首页热门推荐下的绿色房间列表
    room_link_lock,// 一键连麦缓存
    idfa,
    sum_List,
    week_list,
    room_roomId_uid,
    add_woman,
    add_man,
    running_robot,
    not_running_robot,
    send_gift_weeklist,
    send_gift_sumlist,
    //麦序模块
    micro_app,
    micro_seq,
    microv2_list,
    user,
    chat_info,
    malice_user,
    phone_uid,
    erban_no_uid,
    valentine,
    valentine_total,
    valentine_no,
    valentine_list,
    private_photo,
    auct_cur,
    sms,
    first_charge,
    day_first_charge,// 每日首冲
    day_app_charge,// 每日app内充值
    box_big_prize,// 礼盒大奖中奖数
    charge_prod,
    charge_prod_list,
    user_purse,
    user_gift_first,//用户第一次抽礼物
    user_gift_purse,//用户抽奖礼物
    user_charge,
    user_phone_confirm,
    user_setting,
    user_extend,
    user_audit_speed_match_list,    // 速配审核用户
    user_charm_speed_match_list,    // 魅力速配用户
    user_charm_best_companies,      // 优质陪陪
    user_charm_new_user,            // 萌新
    user_charm_last_change_list,    // 魅力变化
    lock_user_gift, // 礼物分布式锁
    user_blacklist,//黑名单
    user_blacklist_list,//黑名单
    wx_token,
    wx_ticket,
    wx_access_oken,
    room_flow_proportion,
    auditing_iosversion,
    forceupdate_iosversion,

    gift_all,       // 全部礼物
    gift_car,//座驾
    gift_car_mall,//座驾商城
    gift_car_list,//座驾
    gift_car_purse,//座驾
    gift_car_purse_list,//座驾,
    headwear,//头饰
    headwear_mall,//头饰商城
    headwear_list,//头饰
    headwear_purse,//头饰
    headwear_purse_list,//头饰
    packet_withdraw_cash_list,
    withdraw_cash_list,
    withdraw_cash_limit,
    sys_conf,
    sys_conf_splash,   // 闪屏
    sys_notice_list,  //公告列表
    banner,
    banner_list,
    questionnaire_draw,
    april_fools_day,
    activity_html,
    wx_muscle_t_token,
    wx_muscle_t_ticket,
    wx_muscle_f_token,
    wx_muscle_f_ticket,
    wx_muscle_video_home,
    wx_users,
    wx_record,
    wx_record_list,
    wx_access_token,

    wxapp_draw_count,
    /**
     * 微信小程序每天邀请用户列表
     */
    wxapp_share_day_list,

    face,
    face_json,
    act,
    room_ctrb_sum,
    room_ctrb_list,
    home_hot,
    home_game,
    home_radio,
    user_in_room,
    app_version,
    app_sign_key,
    account_login_record,
    account_last_login_user_count,      // 最近七天登录的总数
    account_last_login_user_pagesize,   // 最近七天登录数据的总页数
    robot_num,
    check_excess,
    rank,
    rank_last,
    rank_home,     // 首页排行榜
    activity_rank, // 活动排行榜
    activity_conf, // 活动配置
    activity_win_list, // 活动结果
    redeem_code_input, // 兑换码输入错误次数
    redeem_code_uid,   // 兑换码接口请求UID
    redeem_code_ip,    // 兑换码接口请求次数
    redeem_code_lock,  // 兑换码分布式锁
    lock_user_purse,    // 钱包分布式锁
    app_version_update,
    mq_gift_status,    // 礼物消息的状态
    mq_gift_prop_status,    // 活动礼物消息的状态
    mq_call_status,    // 打call消息的状态
    mq_big_gift_status,    // 大额礼物消息的状态
    mq_room_message_status,   // 房间消息的状态
    mq_mystic_gift_status, // 神秘礼物消息状态

    config_home_random,// 首页列表用随机列表
    act_rank,
    block_ip, //被封禁ip
    block_account,//被封禁账户
    block_deivce,//被封禁设备
    acc_latest_login,//最后登录的uid
    noble_zip,
    noble_users,
    noble_right,
    noble_recom_notice, // 推荐等待通知的列表
    noble_user_in_room, // 房间里贵族的在线列表

    user_level_exper,   // 用户等级经验
    user_level_charm,   // 用户等级魅力
    level_exper_list,   //经验list
    level_charm_list,   //魅力list
    user_draw,

    clock_attend,
    clock_result,
    clock_result_record,
    clock_user_attend,
    clock_user_result,
    clock_status,
    full_gift_wall_user,
    erban_no,     //官方号
    opposite_sex,
    pretty_number,//靓号
    feedback_activity,
    draw_win_records,
    /**
     * 房间分类日排行
     */
    room_day_rank,
    /**
     * 用户签到一个周期内的记录
     */
    sign_in_by_period,
    /**
     * 用户签到的卡片
     */
    user_sign_card,
    /**
     * 用户兑换记录
     */
    user_card_exchange_record,
    /**
     * 用户赠送金币
     */
    author_give_gold,
    /**
     * 签到卡-卡片信息
     */
    sign_in_card_info,

    daily_room_time,
    duty_fresh_record,
    duty_daily_record,
    duty_dailytime_record,
    duty_record,
    duty_lock,

    /**
     * 聊天敏感词
     */
    sensitive_words,
    /**
     * 房间名 敏感词
     */
    room_sensitive_words,

    /**
     * 推荐房间
     */
    rcmd_room_pool,

    room_game_record,
    /**
     * 账号禁言
     */
    account_banned_record,
    /**
     * 捡海螺排行
     */
    gift_draw_rank,

    charge_lock,
    /**
     * 捡海螺记录
     */
    gift_draw_record,
    /**
     * 抽中iPhone xs的用户
     */
    user_draw_iphonexs,
    /**
     * 用户抽奖锁
     */
    user_draw_lock,
    /**
     * 用户文字抽奖锁
     */
    user_word_draw_lock,

    wxapp_draw_lock,
    /**
     * 捡海螺活动礼物送出的数量
     */
    gift_draw_event_num,
//    /**
//     * 捡海螺次数
//     */
//    gift_draw_num,

    /**
     * 表白墙,置顶记录
     */
    express_wall_top,
    /**
     * 表白墙列表
     */
    express_wall_list,
    client_ip,
    sms_code_number_verify,

    /**
     * 房间捡海螺流水
     */
    room_draw_gift_record,

    channel_audit,
    channel_audit_room,
    channel_audit_icon,
    channel_audit_banner,
    /**
     * 指定用户必出全服
     */
    users_mining_must,

    /**
     * 红包提现
     */
    luck_money_with_draw,
    /**
     * 渠道管理
     */
    home_channel,
    //扭蛋抽奖，
    niudan_word_draw,

    review_config,
    general_review_whitelist,
    room_recommend_list,
    accopany_manual_list,
    zego_access_token,

    user_reg_share_lock,

    with_draw_type,
    user_check_code,
    users_withdraw_whitelist,
    /**
     * 首次充值活动
     */
    novice_first_charge_checkin,
    novice_first_charge_checkin_record,
    novice_first_charge_checkin_prod,
    rank_auditing,
    room_game_config,
    user_sms_code_string,
    lock_exchange_gold,
    room_conf,
    gift_draw_user_lock,
    mq_gift_draw_message_status,
    gift_draw_input,
    gift_draw_output,
    user_gift_draw_output,
    user_gift_draw_input,

    xqgift_draw_input,
    xqgift_draw_output,
    xquser_gift_draw_output,
    xquser_gift_draw_input,

    hdgift_draw_input,
    hdgift_draw_output,
    hduser_gift_draw_output,
    hduser_gift_draw_input,

    is_bai,

    user_real_name,
    user_send_gift_sum,// 用户送礼总价值

    withdraw_token,

    check_audit_user_sound_gender_age_pool,

    /*周星*/
    week_star_gift,
    week_last_star,
    week_star_the_week,
    week_star_gift_notice,
    week_star_item_reward,
    week_last_star_gift,

    mcoin_draw_ticket_zset,
    mcoin_draw_issue_string,
    mcoin_draw_issue_list_string,
    mcoin_draw_lock,
    mcoin_draw_user_issue_hash,
    mcoin_mission_list,
    mcoin_mission_finish_hash,
    gift_draw_free_hash,
    gift_draw_free_limit_hash,
    user_home_never_notify_zset,
    level_user_charge,
    daily_online_lock,
    mcoin_mission_lock,
    mcoin_mission,
    mcoin_user_purse,
    mcoin_user_purse_lock,
    mcoin_user_level_lock,

    mcoin_pk_this_term,
    mcoin_pk_lottery_tag,
    mcoin_pk_support_red_polls,
    mcoin_pk_support_blue_polls,
    mcoin_pk_carve_up_number,
    mcoin_user_pk_support_red_polls,
    mcoin_user_pk_support_blue_polls,
    mcoin_pk_history_gain,
    mcoin_pk_history_times,
    mcoin_pk_history_win_times,
    mcoin_pk_history_win_rate,
    mcoin_pk_history_ranking_list,
    mcoin_pk_history_period_list,

    room_charm_lock,
    room_charm_add_zset,
    room_charm_zset,
    room_charm_mqlock_string,
    mcoin_pk_send_mq_message_status,


    /**
     * 猜拳
     */
    mora_award,
    mora_num,
    mora_config,
    mora_award_lock,
    mora_record_id_lock,
    mora_lave_num,

    report_kuaishou_device_lock,
    report_kuaishou_device_imei,
    report_kuaishou_device_idfa,
    report_kuaishou_imei,
    report_kuaishou_idfa,
    device_id,
    room_users_tag,
    users_charm_uid,

    /**
     * 测试东信
     */
    sms_mobile_code_string,
    // 人气榜Redis
    popularity_list_last_week, // 人气榜上周榜
    popularity_list, // 人气榜
    popularity_list_mine, // 人气榜我的信息
    popularity_list_user_recommend, // 人气榜用户星推官
    imxinpi_uid_map_roomid_key,
    eff_banner,
    sys_namespace,
    /**
	     * 国庆活动专用
	*/
	daily_task,//每日任务
	integral_details, //积分明细
	integral_total,//总积分
	daily_fans_like,//关注记录
	cancel_fans_like,//取消关注记录
	exchange_glodcount,//兑换金币次数
	exchange_erbano_count,//兑换靓号次数
	daily_user_erbano_exchange_rc,//兑换靓号用户集

    /**
     * 公会
     */
    guild,  // 公会信息
    guild_list,  //公会信息列表
    guild_recommend,  // 推荐公会的分页信息
    guild_hall,  //单个厅信息
//    guild_hall_master,  //厅主信息，有流水数据
    guild_hall_list,  //厅列表信息
    guild_member,  //成员的公会相关信息，有流水数据
    guild_member_in_hall,  // 成员所在的厅信息
    guild_member_list, //公会成员列表
    guild_hall_member_list, //厅成员列表
    guild_member_exist, //用户是否已加入公会
    guild_member_turnover,  //成员的日流水数据
    guild_apply_record_list,  //会长：申请加入或退出的分页信息
    guild_hall_apply_record_list,  //厅主：申请加入或退出的分页信息
    guild_apply_record_auditing_count,  //对应用户有待处理的申请记录数量
    guild_apply_record_verify,  //用于处理申请记录时加锁
    guild_period_turnover, // 公会的日、周、总流水
    guild_hall_period_turnover, // 厅的日、周、总流水
    guild_hall_member_period_turnover, // 成员的日、周、总流水
    guild_daily_turnover_report_lock, // 更新公会相关流水记录的锁


    /**
     * 房间PK
     */
    roomvs_lock,   // PK记录的锁
    ;
    /**
     * 获取Redis的键名
     *
     * @return
     */
    public String getKey() {
        return ("庫名_" + name()).toLowerCase();
    }

    /**
     * 通过Redis后缀获取键名
     *
     * @param suffix 后缀
     * @return
     */
    public String getKey(String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            return getKey();
        } else {
            return getKey() + "_" + suffix;
        }
    }
}
