package com.xchat.common.redis;

import com.xchat.common.utils.StringUtils;

public enum RedisKey {
    app_sign_key,
    icon,
    icon_index,
    icon_index_v,
    client_ip,
    bonus_level,
    user_configure,
    user_purse_hot_room_record,
    user_purse_hot_room_record_list,
    uid_access_token,
    uid_ticket,
    room,
    room_background,
    room_background_user,
    room_online_num,// 房间在线人数显示加上这个配置的数量
    room_hot,
    room_running,
    room_vip,
    room_permit_hide,// 需要在展示时隐藏的牌照房（在线人数为0或者只有机器人）
    room_tag_room,   // 房间设置标签列表
    room_tag_top,    // APP顶部标签列表
    room_tag_search,    // APP分类标签列表
    room_tag_list,   // 单个标签数据
    room_tag_new,    // 拉贝新标签页的第一页
    room_tag_radio,  // 电台标签页的第一页
    room_tag_index,  // 每个标签页的第一页
    room_mic_list,   // 房间麦序列表
    room_pos_lock,   // 房间麦序占位临时的KEY，防多人并发抢
    room_sensitive_words,
    sensitive_words_chat,
    sensitive_words_nick,
    sensitive_words_room_play_info,
    home_hot_recom,  // 首页热门推荐
    home_room_list,  // 首页热门推荐下的房间列表
    room_home_new,  // 首页新秀列表
    green_room_list,  // 首页热门推荐下的绿色房间列表
    home_room_random,// 首页热门推荐下的房间列表，随机出现
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
    valentine,
    valentine_total,
    valentine_no,
    valentine_list,
    private_photo,
    auct_cur,
    sms,
    idfa,
    first_charge,
    charge_prod,
    charge_prod_list,
    user_purse,
    author_give_gold,   // 金币转账权限
    user_purse_gold,
    user_purse_diamond,
    user_purse_deposit,
    user_gift_first,//用户第一次抽礼物
    user_gift_purse,//用户抽奖礼物
    lock_user_gift, // 礼物分布式锁
    user_blacklist,//黑名单
    user_blacklist_list,//黑名单
    wx_token,
    wx_ticket,
    wx_access_oken,
    auditing_iosversion,
    forceupdate_iosversion,
    gift,
    /** 神秘礼物 */
    gift_mystic,
    gift_point,
    gift_activity,
    gift_all,
    gift_car,//座驾
    gift_car_mall,//座驾
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
    sys_conf,
    sys_conf_splash,   // 闪屏
    banner,
    banner_list,
    gift_draw_rank,
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
    face,
    face_json,
    act,
    room_ctrb_sum,
    room_game_record,
    home_hot,
    home_game,
    home_radio,
    user_in_room,
    app_version,
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
    lock_user_purse,    // 金币分布式锁
    lock_user_diamond, // 钻石分布式锁
    app_version_update,
    mq_gift_status,    // 礼物消息的状态
    mq_big_gift_status,    // 大额礼物消息的状态
    mq_noble_status,   // 贵族消息的状态
    mq_room_message_status,
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
    account_login_record,
    opposite_sex,
    clock_attend,
    clock_result,
    clock_result_record,
    clock_user_attend,
    clock_user_result,
    clock_status,
    full_gift_wall_user,
    erban_no,     //拉贝号
    pretty_number,//靓号
    feedback_activity,
    /** 表白墙,置顶记录 */
    express_wall_top,
    /** 表白墙列表 */
    express_wall_list,

    /**
     * 房间周的流水比率
     */
    room_flow_proportion,
    /**
     * 根据标签分类--房间日排行
     */
    room_day_rank,
    /** 用户猜名字的记录 */
    user_guess_name_record,
    /**apple用户充值记录*/
    user_apple_charge_record,
    /**apple用户消息发送记录*/
    user_send_message_record,

    /** 敏感词 */
    sensitive_words,

    daily_room_time,
    duty_fresh_record,
    duty_daily_record,
    duty_dailytime_record,
    duty_record,
    duty_lock,

    /** 推送消息标记 */
    push_message_flag,

    rcmd_room_pool,

    youngg_gift_pic,
    youngg_draw_lock,
    youngg_task_lock,
    youngg_room_time,

    /** 用户设备注册数量 */
    register_num,

    /** 用户禁言记录 */
    account_banned_record,

    room_pk_task_lock,
    room_pk_vote,
    room_pk_vote_set,
    room_pk_vote_count,

    /** 拉贝活动礼物送出的数量 */
    gift_draw_event_num,
    /** 拉贝次数统计 */
    gift_draw_num,
    /** 拉贝产出 */
    gift_draw_output,

    /** 国庆任务-任务完成情况 */
    national_day_task,
    /** 国庆礼物送出数量 */
    national_gift_num,

    wxapp_draw_count,
    /**
     * 微信小程序每天邀请用户列表
     */
    wxapp_share_day_list,
    sms_code_number_verify,
    goldcoin_give,

    /** 渠道审核 */
    channel_audit,
    /** 审核渠道房间 */
    channel_audit_room,
    channel_audit_icon,
    channel_audit_banner,
    channel_audit_users,
    channel_audit_ip,
    /** 渠道(下载)信息 */
    home_channel,

    /** 用户必出全服 */
    users_mining_must,
    /** 魅力推荐用户 */
    charm_recom_user,
    /** 魅力推荐用户的魅力值 */
    charm_recom_source,
    family_flow_proportion,
    review_whitelist,
    general_review_whitelist,
    review_config,
    users_withdraw_whitelist,
    admin_login_error_count,
    room_game_config,
    room_conf,
    user_real_name,

    week_star_gift,
    week_star_gift_notice,
    week_star_item_reward,

    mcoin_pk_info_list_string,
    mcoin_draw_issue_list_string,
    mcoin_draw_issue_string,
    mcoin_pk_support_red_polls,
    mcoin_pk_support_blue_polls,
    mcoin_pk_send_mq_message_status,
    mora_config,
    mora_award,
    sms_mobile_code_string,
    
    /*后台管理 发送验证码次数*/
    admin_sendSmsCode_count,
    admin_acount_mobile_sendSmsCode,
    home_hot_recommend_rule, // 首页推荐规则
    eff_banner,
    sys_namespace,
    daily_user_erbano_exchange_rc,//兑换靓号用户集
    guild_hallcount,//公会厅数量分布式锁
    guild_membercount,//公会成员数量分布式锁
    guild_hall_membercount,//公会厅成员数量分布式锁
    /**
     * 公会
     */
    guild,  // 公会信息
    guild_list,  //公会信息列表
    guild_recommend,  // 推荐公会的分页信息
    guild_hall,  //单个厅信息
    guild_hall_list,  //厅列表信息
    guild_member,  //成员的公会相关信息，有流水数据
    guild_member_in_hall,  // 成员所在的厅信息
    guild_member_list, //公会成员列表
    guild_hall_member_list, //厅成员列表
    guild_member_exist, //用户是否已加入公会
    guild_apply_record_list,//会长查看：申请加入或退出的分页信息
    guild_hall_apply_record_list;//厅主查看：申请加入或退出的分页信息
    

    public String getKey() {
        return ("yingtao_" + name()).toLowerCase();
    }

    public String getKey(String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            return getKey();
        } else {
            return getKey() + "_" + suffix;
        }

    }
}
