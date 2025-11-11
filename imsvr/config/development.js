var os = require('os');
var getLocalIP = function() {
    var ifaces = os.networkInterfaces();
    console.log(ifaces);
    for (var dev in ifaces) {
        if (dev.indexOf('eth') == -1) continue;
        return ifaces[dev][0]['address']
    }
    return '127.0.0.1';
};

module.exports = {
    //线上环境Java
    java_host: "127.0.0.1",
    java_port: 81,
    zego_host: "liveroom197844898-api.zego.im",
    port: parseInt(process.argv[2]), // proxy port; im-svr port = proxy port + 1
    http_port: parseInt(process.argv[2]) + 1,
    websocket_port: parseInt(process.argv[2]),
    socketio_port: parseInt(process.argv[2]) + 2,
    cronsvr_port: parseInt(process.argv[2]) + 3,
    image_ip:"www.charmlive.com",
    image_port:443,
    image_upload_path:"/uploadfiles/",
    image_static:"/public/",
    inner_host: getLocalIP(),
    // 字词过滤服务器配置
    wordFilter: {
        port: 99
    },

    rtmp_server:{    'test':{ip: '', port: 8855}  },

    /**
     * mysql config
     */

    mysqlServers: [
        {
            host: '127.0.0.1',
            port: 3306,
            user: 'root',
            password: 'mysql密碼'
        }
        //{
        //  host: '192.168.88.17',
        //  port: 3306,
        //  user: 'root',
        //  password: 'myroot123'
        //}
    ],
    mysqlDatabase: 'yingtao',
    mysqlMaxConnections: 2,
    mysqlQueryTimeout: 5000,


    // redis config
    // use for koa-limit module as storage
    redis: {
        host: '127.0.0.1',
        port: 6379,
        db : 0, // 业务逻辑使用的库
        db_cache : 1, //框架db缓存使用
        db_sub: 2,//im集群管理用
        options: {
            auth_pass: 'redis密碼'
        }
    },
    //redis: {
    //    host: '127.0.0.1',
    //    port: 6379,
    //    db : 4, // 业务逻辑使用的库
    //    db_cache : 5, //框架db缓存使用
    //    db_sub: 6,//im集群管理用
    //    options: {
    //        auth_pass: 'miaomiao2018dev'
    //    }
    //},
    javaredis: {
        host: '127.0.0.1',
        port: 6379,
        db : 0,
        options: {
            auth_pass: 'redisq1w2e3r4'
        }
    },

    // mongodb 设置
    mongo:{
        uri : 'mongodb://127.0.0.1:27017/artqiyi',
        options : {
            db: { native_parser: true },
            auto_reconnect: 1,
            server: { poolSize: 4 },
            user: 'artqiyi',
            pass: 'kjhDh38273erdfEd'
        }
    },
    anchor_approve: {
        play_prefix: 'http://stream-eu.charmdate.com:8083/approve_record/',
        admin_record_url: 'https://www.charmlive.com/',
        rtmp_approve_url:'rtmp://stream-eu.charmdate.com:1936/approve'
    },

    video_host:{
        host:'https://www.charmlive.com',
    },

    activity:{
        id: 1, // 活动ID 对应activity表记录ID -- 判断是否已经结算用
        start_time: '2017-12-18 00:00:00',
        end_time: '2017-12-19 23:59:59',
        user_reward_list: [
            {
                id:1,
                credit_cond: 10000, // 满足奖励的信用点条件
                reward_list: ['G00104','G00073'],
                reward_list_num:[1,10],
                can_use_days:['+5',40]
            },
            {
                id:2,
                credit_cond: 100000, // 满足奖励的信用点条件
                reward_list: ['G00105','G00085'],
                reward_list_num:[1,50],
                can_use_days:['+5',40]
            },
            {
                id:3,
                credit_cond: 500000, // 满足奖励的信用点条件
                reward_list: ['G00106','G00113','G00063'],
                reward_list_num:[1,100,1],
                can_use_days:['+5',40,15]
            },
        ]
    },
    activity_login: {
        start_time: '2018-01-08 00:00:00',
        end_time: '2020-02-28 23:59:59',
        activity_name: '登录赠送券活动2期',
        date_gmt: 518400, // 活动第一天后持续时间秒数 6天
        desc: [ // 数组下标对应赠送的第几期
            [ // type 1券 2礼物, user_type 1所有用户 2未转化用户
                // 当前配置
                // 2张公开试聊券
                // 1张私密试聊券
                {type: 1, user_type: 1, time: 0, options: [
                        // 有效开始时间 计算公式：当前时间+start_time
                        {id: 139, start_time: 0, end_time: 604800, num: 2},
                        {id: 149, start_time: 0, end_time: 604800, num: 1}
                    ]},
                {type: 2, user_type: 1, time: 0, options: [
                        {id: 134, start_time: 0, end_time: 604800, num: 3},
                        {id: 133, start_time: 0, end_time: 604800, num: 3},
                        {id: 132, start_time: 0, end_time: 604800, num: 3}
                    ]}
            ],
            [
                // 当前配置
                // 1张公开试聊券
                {type: 1, user_type: 2, time: 345600, options: [
                        {id: 139, start_time: 0, end_time: 604800, num: 1}
                    ]},
                {type: 2, user_type: 1, time: 345600, options: [
                        {id: 134, start_time: 0, end_time: 604800, num: 1},
                        {id: 133, start_time: 0, end_time: 604800, num: 1},
                        {id: 132, start_time: 0, end_time: 604800, num: 1}
                    ]}
            ]
        ],
        coupons:{
            'A1':[
                {id: 139, start_time: 0, end_time: 604800, num: 1},//房间类型：公开 绑定主播：不绑定 时长：3分钟
                {id: 149, start_time: 0, end_time: 604800, num: 3}//房间类型：私密 绑定主播：不绑定 时长: 1分钟
            ],
            'B1':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//房间类型：公开 绑定主播：不绑定 时长：3分钟
                {id: 149, start_time: 0, end_time: 604800, num: 3}//房间类型：私密 绑定主播：不绑定 时长: 1分钟
            ],
            'C1':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//房间类型：公开 绑定主播：不绑定 时长：3分钟
                {id: 149, start_time: 0, end_time: 604800, num: 3}//房间类型：私密 绑定主播：不绑定 时长: 1分钟
            ],
            'D1':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//房间类型：公开 绑定主播：不绑定 时长：3分钟
                {id: 149, start_time: 0, end_time: 604800, num: 1}//房间类型：私密 绑定主播：不绑定 时长: 1分钟
            ],
            'E1':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//房间类型：公开 绑定主播：不绑定 时长：3分钟
                {id: 149, start_time: 0, end_time: 604800, num: 0}//房间类型：私密 绑定主播：不绑定 时长: 1分钟
            ],
        },
        coupons_gift:[
            {id: 134, start_time: 0, end_time: 604800, num: 1},
            {id: 133, start_time: 0, end_time: 604800, num: 1},
            {id: 132, start_time: 0, end_time: 604800, num: 1},
        ],
        coupons411:{
            'A11':[
                {id: 139, start_time: 0, end_time: 604800, num: 2},//公开
                {id: 149, start_time: 0, end_time: 604800, num: 2}//私密
            ],
            'A12':[
                {id: 139, start_time: 0, end_time: 604800, num: 2},//公开
                {id: 149, start_time: 0, end_time: 604800, num: 2}//私密
            ],
            'B11':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//公开
                {id: 149, start_time: 0, end_time: 604800, num: 3}//私密
            ],
            'B12':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//公开
                {id: 149, start_time: 0, end_time: 604800, num: 3}//私密
            ],
            'B13':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//公开
                {id: 149, start_time: 0, end_time: 604800, num: 3}//私密
            ],
            'C11':[
                {id: 139, start_time: 0, end_time: 604800, num: 3},//公开
                {id: 149, start_time: 0, end_time: 604800, num: 1}//私密
            ],
            'C12':[
                {id: 139, start_time: 0, end_time: 604800, num: 3}//公开
            ],
        },
        coupons_gift411:[
            {id: 137, start_time: 0, end_time: 604800, num: 1},
            {id: 135, start_time: 0, end_time: 604800, num: 1},
            {id: 136, start_time: 0, end_time: 604800, num: 1},
            {id: 132, start_time: 0, end_time: 604800, num: 2},
            {id: 133, start_time: 0, end_time: 604800, num: 2},
            {id: 134, start_time: 0, end_time: 604800, num: 2},
        ]
    },
};
