/**!
 * cnpmjs.org - config/index.js
 *
 * Copyright(c) cnpmjs.org and other contributors.
 * MIT Licensed
 *
 */

'use strict';

/**
 * Module dependencies.
 */
if (process.argv.length < 3) {
    console.log("ERROR: params not right, usage: pm2 start xxx.js port");
    process.exit();
}

//process.env.TZ = 'Asia/Shanghai';
//process.env.TZ = 'Europe/London';
//process.env.TZ = 'UTC';
process.env.TZ = 'Asia/Shanghai';

var path = require('path');
var os = require('os');
var _ = require('underscore');


var root = path.dirname(__dirname);

var redisGlobalPre = 'imxinpi_';
var javaRedisGlobalPre = 'yingtao_';

var allConfig = {
    aes_key: "09051825305fd819",
    aes_iv: "2dba43c93e7884b9",
    slow_log_delta: 30,
    testSpeedFile: 'data/tspeed/encode.js',
    waitForCon: true,
    redisPre: {
        uniid_svr_map_key: redisGlobalPre + 'uniid_svr_map_%d',//用户在哪一台机子 参数为userid，value为svr内部host
        pack_req_id_key: redisGlobalPre + 'pack_req_id_key', // request 唯一id
        sms_vercode_key: redisGlobalPre + 'sms_vercode_%s', // 短信验证码前缀，参数phone+vercode，value vercode
        sms_has_send_key: redisGlobalPre + 'sms_has_send_%s', // 短信验证码已生成标识， 参数phone， value vercode
        token_uid_map_key: redisGlobalPre + 'token_uid_map_%s', // token 映射uid, 参数 token，value uid
        uniid_realip_map_key: redisGlobalPre + 'uid_realip_map_%s', // uid 与登录host映射 参数 uid value host
        uniid_https_map_key: redisGlobalPre + 'uid_https_map_%s', // uid 与是否https协议映射 参数： uid，value：is_https
        roomid_uniid_map_key: redisGlobalPre + 'roomid_uniid_map_%s', // 主播间场次对应观众， 参数 场次id， value hset uniid score是时间
        uniid_roomid_map_key: redisGlobalPre + 'uniid_roomid_map_%s', // uid与主播间场次对应，参数 uniid，value：roomid
        uniid_roomid_prefix: redisGlobalPre + 'uniid_roomid_prefix_%s_%s', // 参数 uniid roomid， value: socket_id,con_time
        roomid_anchor_map_key: redisGlobalPre + 'roomid_anchor_map_%s', // 直播间场次与主播映射关系，参数：直播场次，value 主播id
        timer_id_prefix: redisGlobalPre + 'timer_id_%s', // 定时器前缀 参数：定时器id, value: 无
        userid_uni_map_key: redisGlobalPre + 'userid_uni_map_%s', // userid uni映射, 参数: userid value uniid列表
        subscribe_prefix: redisGlobalPre + 'subscribe_uniid_%s',//redis订阅key前缀
        timer_uniid_roomid_key: redisGlobalPre + 'timer_key_uniid_roomid_%s_%s',  // 根据uniid 房间ID设置定时器锁
        timer_video_uniid_roomid_key: redisGlobalPre + 'timer_video_key_uniid_roomid_%s_%s',  // 根据uniid 房间ID设置定时器-视频锁
        job_prefix: redisGlobalPre + 'job_prefix_%s:%s', // db 定时器前缀 参数：dbhost dbport
        job_stop_flag: redisGlobalPre + 'job_stop_flag', // db 定时器redis开关
        token_device_type_key: redisGlobalPre + 'token_device_type_map_%s', // token device_type
        token_sessid_key: redisGlobalPre + 'token_sessid_map_%s', // token sessid
        uniid_token_key: redisGlobalPre + 'uniid_token_map_%s', // uniid token
        send_req_cb_data: redisGlobalPre + 'send_req_cb_data_%s', // sequence id
        live_follow_key: redisGlobalPre + 'live_follow_%s_%s', // 直播间关注状态根据roomid和用户id来判断在该直播间是否关注过
        log_collect_next_id_key: redisGlobalPre + 'log_collect_next_id', // log_collect sn生成规则 自增ID
        mobile_access_date_key: redisGlobalPre + 'mobile_access_date_key_%s_%s', // 移动端访问日期key
        nickname_repleat_key: redisGlobalPre + 'nickname_repleat_key_%s', // 昵称重复锁 key
        nickname_change_key: redisGlobalPre + 'nickname_change_key_%s', // 昵称变更锁 key
        userid_sessionid_map_key: redisGlobalPre + 'userid_sessionid_map_key_%s', // userid sessionid映射 key
        login_reward_coupon_key: redisGlobalPre + 'login_reward_coupon_key_%s', // 登录送券锁 key
        haspop_key: redisGlobalPre + 'haspop_key_%s', // 记录用户pop 参数 userid
        rtmp_info_key: redisGlobalPre + 'rtmp_id_key_%s_%s', // 记录映射流媒体信息，参数1: stream, 参数2: rtmpid; value: token
        ga_user_key: redisGlobalPre + 'ga_user_key_%s_%s_%s_%s', // ga用户key 参数 userid platform type date
        page_socket_key: redisGlobalPre + 'page_socket_key_%s', // 用户page_socket redis key 参数 userid
        userid_uniid_map_key: redisGlobalPre + 'userid_uniid_map_key_%s', // userid uniid映射 key uniid
        hangout_create_room_user_key: redisGlobalPre + 'hangout_create_room_user_key_%s', // 多人直播间创建唯一键 参数 userid
        hangout_seat_room_list_key: redisGlobalPre + 'hangout_seat_room_key_%s', // 多人直播间位置list唯一键 参数 roomid
        hangout_timer_key: redisGlobalPre + 'hangout_timer_key_%s_%s_%s', // 多人视频定时扣费key room_id anchor_id sub_room_id
        userid_login_ip_info_key: redisGlobalPre + 'userid_login_ip_info_key_%s', // 用户登录记录IP信息 映射 key userid
        hangout_view_timer_key: redisGlobalPre + 'hangout_view_timer_key_%s', // 多人视频双向视频扣费key room_id
        live_show_timer_key: redisGlobalPre + 'live_show_timer_key_%s_%s', // 节目直播间扣费key 映射 key room_id userid
        anchor_enter_public_room_key: redisGlobalPre + 'anchor_enter_public_room_key_%s', // 主播进入公开直播间key 映射 key anchor_id
        next_expire_id_key: redisGlobalPre + 'next_expire_id_key_%s', // 生成会过期的唯一id 用于生成socketid用
        /***********以上redis为框架用**********/
        room_info_key: redisGlobalPre + 'room_info_%s', // 记录房间信息
        room_audio_channel_key: redisGlobalPre + 'room_audio_channel', // 记录房间信息
        default_audio_channel_key: redisGlobalPre + 'default_audio_channel', // 记录房间信息
        room_blacklist_key: redisGlobalPre + 'room_blacklist_%s', // 记录房间黑名单信息
        room_mute_key: redisGlobalPre + 'room_mute_%s', // 记录房间禁言信息
        room_manager_key: redisGlobalPre + 'room_manager_%s', // 记录房间角色信息
        user_info_key: redisGlobalPre + 'user_info_%s', // 记录用户信息
        user_map_socket_key: redisGlobalPre + 'user_map_socket_key_%s', //存用户对应所有socket
        roomid_map_uid_key: redisGlobalPre + 'roomid_map_uid_key_%s', // 存房间用户列表
        uid_map_roomid_key: redisGlobalPre + 'uid_map_roomid_key_%s', // 存用户房間列表
        roomid_map_socket_key: redisGlobalPre + 'roomid_map_socket_key_%s', // 存房间socket列表
        socket_map_room_key: redisGlobalPre + 'socket_map_room_key_%s', // 方向存socket在哪些房间
        uid_room_map_socket_key: redisGlobalPre + 'uid_room_map_socket_key_%s_%s', // 存某个用户在某个房间有哪些socket
        room_map_queue_mem_key: redisGlobalPre + 'room_map_queue_mem_key_%s', // 存房间对应成员信息
        room_live_sortedset_key: redisGlobalPre + 'room_live_zset_key', // 存所有有活人的房间，数据类型：sorted set
        public_roomid_map_socketid_zset_key: redisGlobalPre + 'public_roomid_map_socketid_zset_key_%s',
        public_roomid_map_uid_zset_key: redisGlobalPre + 'public_roomid_map_uid_zset_key_%s',
        public_socketid_map_roomid_zset_key: redisGlobalPre + 'public_socketid_map_roomid_zset_key_%s',
        public_room_history_zset_key: redisGlobalPre + 'public_room_history_zset_key_%s',
        stream_uid_map_streamid_key: redisGlobalPre + 'stream_uid_map_streamid_key_%s',
        stream_uid_map_streamsvrid_key: redisGlobalPre + 'stream_uid_map_streamsvrid_key_%s',
        account_banned_record_hash: javaRedisGlobalPre + 'account_banned_record',

        /********以下key为获取java服务器端设置的redis用********/
        uid_ticket: javaRedisGlobalPre + 'uid_ticket', // 存储 uid 和 ticket关系的key
        room_mic_list: javaRedisGlobalPre + 'room_mic_list_%s', // 存储坑位信息
        room_room_id: javaRedisGlobalPre + 'room_room_id',
        uid_type_map_room_info: javaRedisGlobalPre + 'room', // uid map room
        /*****banzhu用************/
        sms_mobile_code_string: javaRedisGlobalPre + "sms_mobile_code_string_%s", // 短信用 key: phone, value: smscode
        token_uid: javaRedisGlobalPre + "token_uid_%s", // 短信用 key: token, value: uid
        award_key: javaRedisGlobalPre + "award_key_%s", // 奖品hash key missionId drawName drawNum
        has_award_list_per: javaRedisGlobalPre + "has_award_list_per_%s_%s", // 个人中奖纪录 missionId uid
        has_award_list: javaRedisGlobalPre + "has_award_list_%s", // 总中奖纪录 missionId

    },
    anchor_instant_invite_num: { //主播立即邀请男士数量限制
        level1: 99999,
        level2: 99999,
        level3: 99999,
        level4: 99999,
        level5: 99999
    },
    anchor_scheduled_invite_num: { //主播预约邀请男士数量限制
        level1: 5,
        level2: 10,
        level3: 15,
        level4: 20,
        level5: 25
    },
    anchor_invite_user_num: 3, // 主播邀请同一个用户发送的最大次数
    anchor_invite_user_time: 600, // 主播邀请同一个用户发送邀请最低间隔时间 秒
    session: {
        key: 'xhlive_sess', // cookie 前缀
        prefix: 'xhlive_sess:', // session 前缀
        ttl: 28800, // session:28800 过期时间 8个小时，单位秒（登录会话）
        site_ttl: 5 * 60//统计网站信息会话
    },
    // cookie:{
    //     domain: 'charmdate.com'//'192.168.88.17',//
    //     //maxage: null
    // },
    tips: {
        '1': '你被管理员请出房间',
        '2': '拉黑名单踢出',
        '3': '网络异常，正在尝试重连',


        '1000001': '欢迎%s进入直播间',
        '1000002': 'Your scheduled show start in %smin, please get prepared.', // 节目开始前10分钟主播通知
        '1000003': 'Show Calendar are the calendars of specially scheduled broadcasts arranged by broadcasters.', //节目描述
        '1000004': 'You current broadcast will be ended in 30 seconds and your scheduled show "%s" is starting in 4 minutes. Please get prepared.', // 公开直播间因节目被迫关闭公告
        '1000005': 'Welcome to the show %s. This show started from %s minutes ago and the show duration is %s minutes. Enjoy!', // 男士节目入场公告
        '1000006': 'Your show "%s" just started and the show duration is %s minutes. Enjoy!', // 女士节目入场公告
        '1000007': 'The show will be ended in 30 seconds. Please be prepared.', // 节目结束公告
        '1000008': 'Your show is starting in %s minutes.Please click to start now.', // 女士 节目前3分钟对应通知
        '1000009': '%s\'s show is ready to start. Join the show now.', // 男士节目前 1分钟通知
        '1000010': 'Price: 0.3 credits per minute for each broadcaster participated.', // 多人直播间消费提示
        '1000011': 'Your show is starting in %s minutes.Please be prepared.', // 女士 节目前10分钟对应通知
        '1000012': 'Your show is starting in %s minute.Please click to start now.', // 女士 节目前1分钟对应通知
        '1000013': '%s is on show now.',
    },
    // 主分支 用户错误码12000 主播错误码16500
    // hangout分支 用户错误码11000 主播错误码16300
    // live_show分支 用户错误码13000 主播错误码16600
    // 框架错误 17000 ~ 18000
    // 18000-18100主播后台列表私信错误码
    err_msg: {
        "10001": "We could not complete your request. Error code: (10001).",
        "10002": "Please login to continue your action.",
        "10003": "An error occurs while connecting to the server",
        "10004": "Your authorization has expired. Please login again.",
        "10005": "We could not complete your request. Error code: (10005).",
        "10006": "Invalid authentication. Please login and try again.",
        "10007": "Could not view this page due to the permission limit. Code: (10007)",
        "10008": "Could not send message. Code: (10008)",
        "10009": "Failed to send virtual gift. Code: (10009)",
        "10010": "You don't have enough #credit_unit_preg#s to continue this action. Please add more #credit_unit_preg#s and try again.",
        "10011": "You has been kickoff.", // 已经不使用
        "10012": "setTimeout param is too large.",
        "10013": "You don't have enough #credit_unit_preg#s to continue this action. Please add more #credit_unit_preg#s and try again.",// 发送弹幕金币不足
        "10014": "setInterval param is too large.",
        "10015": "Broadcast room has ended. Please check back later.",
        "10017": "Failed to delete this image.",
        "10018": "Invalid authorization. Please login and try again.",
        "10019": "Your operation has been blocked due to too many invalid attempts.",
        "10020": "Incorrect broadcaster ID or password.",//主播登录，账号或密码错误
        "10021": "Broadcast has ended. Please check back later.", // 进入房间失败 找不到房间信息or房间关闭
        "10022": "Unknown error occurred. Please reload this page.", // 进入房间失败 数据库操作失败（添加记录or删除扣费记录）

        "10037": "你的账号在其他设备登录.",

        /**  [100100 - 100200)  login接口使用 **/
        "100100": "im登录鉴权失败",
        "100101": "获取用户信息失败",
        /** [100200 - 100300) enterChatRoom接口使用 **/
        "100200": "获取房间信息失败",
        "100201": "用户房间socketId不存在",
        "100202": "用户在房间黑名单中",
        "100203": "房间不存在",
        "100204": "声音渠道不存在",
        /** [100300 - 100400) queue接口使用 **/
        "100300": "队列位置权限控制",
        "100301": "只能操作比自己低权限的用户",
        "100302": "没有上麦不能推流",
        "100303": "当前麦位已经有人",
        /** [100400 - 100500) 房间用户角色和黑名单接口使用 **/
        "100400": "用户已在黑名单",
        "100401": "用户不在黑名单",
        "100402": "用户已是管理员",
        "100403": "用户不是管理员",
        "100404": "用户不存在",
        "100405": "操作权限不足",
        "100406": "用户已经禁言",
        /** [100500 - 100600) 踢出成员接口 **/
        "100500": "你被管理员请出房间",
        /** [100600 - 100700) 公屏接口返回吗 **/
        "100601": "错误的公聊大厅",
        "100407": "你已违反小伴点语音发言条例，将会进行%s禁言",
        "100408": "您已违反小伴点语音发言条例，请注意言行避免封号",

        /**101000 - 101010斑猪登录接口用**/
        "101000": "该手机已经被注册",
        "101001": "发送短信失败",
        "101002": "短信验证码校验失败",
        "101003": "您还没有登录",
        "101004": "找不到该用户信息",
        "101005": "需要审核通过才有权限发布，请联系管理员微信gzysh777开通。",
        /**101011 - 101020活动接口用**/
        "101011": "该活动已经被删除",
        "101012": "该悬赏已经被删除",
        "101013": "报名人数已经超过最大限制",
        "101014": "抽奖数量已经用完",
        "101015": "个人抽奖数量已经用完",
        "101016": "没有权限操作",
        "101017": "开始时间不能大于结束时间"
    },
    lock_key: {
        room_key: "room_",
        invitation_scheduled_key: "invitation_scheduled_userid_%s", // 预约邀请key 预约ID
        interim_consume_key: "interim_consume_%s_%s", // 临时扣费key userid roomid
        user_online_status_key: "user_online_status_%s", // 用户在线状态key userid
        report_speed_key: "report_speed_key_%s_%s", // 记录测速结果 参数1 svrid，参数2 userid
        reward_key: "reward_key_%s", // 活动送奖品 参数1 userid
        send_gift_key: "send_gift_key_%s_%s_%s", // 发生礼物 参数1 userid 参数2 礼物类型 参数3 anchor_id
        hangout_timer_consume_key: "hangout_timer_consume_key_%s_%s_%s", // 多人视频定时扣费 参数 room_id anchor_id sub_room_id
        hangout_view_consume_key: "hangout_view_consume_key_%s", // 多人视频双向视频扣费 参数 room_id
        //program_time:"program_%s", // 主播接受预约时间块锁
        program_time: "program", // 主播接受预约全局锁
        enter_show_lock_key: "enter_show_lock_key_%s", // 用户进入节目直播间锁 参数 room_id
        room_queue: "room_queue_", // 房间队列锁
        banzhu_award: "banzhu_award_", // banzhu 奖励锁
    },
    thumb_suffix: {
        high: "_960x720",
        middle: "_640x480",
        low: "_320x240",
    },
    SMS: {
        sms_url: 'http://101.227.68.49:7891/mt?',
        sms_user: '10690212',
        sms_pwd: 'qiYIg2016',
    },
    verCode_config: {
        "valid_time": 600,//验证码有效期
        "remake_interval": 60,//重新生成时间间隔
        "tpl": "【%s】thank you for you register,your identifying code is %s, duration of validity is %sseconds",
    },
    appName: "oklive",
    nickNamePrefix: "LIVE-",
    token_expire_time: 7200, // 7200秒
    RTMP_FLAG: '#RTMPToBeFitWithDomain#',
    //RTMP_HOST:'192.168.88.17',
    blurry_key: 'wpt76ke84ly8bj9e',
    rtmp_push_url: [
        "rtmp://#RTMPToBeFitWithDomain#:1936/speex/anchor_%s_%s"
    ],
    rtmp_pull_url: [
        "rtmp://#RTMPToBeFitWithDomain#:1936/speex/anchor_%s_%s"
    ],
    fansi_rtmp_push_url: [
        "rtmp://#RTMPToBeFitWithDomain#:1936/speex/fansi_%s_%s"
    ],
    fansi_rtmp_pull_url: [
        "rtmp://#RTMPToBeFitWithDomain#:1936/speex/fansi_%s_%s"
    ],
    rtmp_kickoff_api: "http://%s:%s/verify/v1/kickoff",
    pic_prefix: "#ToBeFittedWithDomain#",
    request_prefix: "#ToBeFromWithDomain#",
    is_open_sql_redis: true, //是否开启查看缓存

};


// 通过NODE_ENV来设置环境变量，如果没有指定则默认为生产环境
console.log('当前环境');
console.log(process.env.NODE_ENV);
var env = process.env.NODE_ENV || 'development';
console.log(env);
env = env.toLowerCase();

// C.env 为运行环境
allConfig.env = env;

// 载入配置文件
var file = path.resolve(__dirname, env + '.js');
try {
    var envConfig = require(file);
    var config = _.extend(allConfig, envConfig);
    //console.log('Load config: [%s] %s', env, file);
} catch (err) {
    console.error('Cannot load config: [%s] %s', env, file);
    throw err;
}

//加载debug文件，用于覆盖index.js中的变量
if (config.is_debug) {
    // 载入qn配置文件
    var qn_file = path.resolve(__dirname, 'debug.js');
    try {
        var qn_conf = require(qn_file);
        config = _.extend(config, qn_conf);
    } catch (err) {
        console.error('Cannot load config: debug.js');
        throw err;
    }
}


// 载入基础配置文件
var basic_file = path.resolve(__dirname, 'basic.js');
try {
    var basic_file = require(basic_file);
    config = _.extend(config, basic_file);
    //console.log('Load config: [%s] %s', env, file);
} catch (err) {
    console.error('Cannot load config: qn_conf.js');
    throw err;
}

module.exports = config;
