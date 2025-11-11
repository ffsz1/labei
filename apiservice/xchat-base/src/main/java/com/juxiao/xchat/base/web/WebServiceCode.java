package com.juxiao.xchat.base.web;

/**
 * @author huangjiahui
 * 统一错误状态码
 */
public enum WebServiceCode {
    SUCCESS(200, "success"),
    //服务不可用
    INVALID_SERVICE(199, "invalid service"),

    SMSIPTOOFTEN(302, "获取短信过于频繁"),
    PARAM_EXCEPTION(400, "服务器无法理解您的请求"),
    VALID_TOKEN(409, "登录过期, 请重新登录"),
    METHOD_NOT_ALLOWED(405, "请求方法错误"),
    NOT_ACCEPTABLE(406, "不支持的内容类型"),
    NO_AUTHORITY(401, "没有权限"),
    NOT_AUTHORITY(402, "需要验证短信验证码"),
    SIGN_AUTHORITY(403, "非法的客户端请求"),
    NOT_EXISTS(404, "不存在"),
    GIFT_CAR_NOT_EXISTS(404, "该座驾不存在~"),
    GIFT_CAR_PURSE_NOTEXISTS(405, "您还没有购买过该座驾~"),
    HOT_EXISTS(405, "该房间已经购买过此时间段的推荐位！"),
    DATE_EXISTS(406, "此时间段已经过期，请购买其它时间段！"),
    USER_UPDATE_GENDER(407, "请选择性别"),
    ROBOT_NOTEXIT(408, "机器人不足"),
    /**
     * 财富 魅力等级错误码
     */
    LEVEL_EXPERIENCE_NOLEVEL(410, "抱歉，查询不到您的经验等级"),
    LEVEL_CHARM_NOLEVEL(411, "抱歉，查询不到您的魅力等级"),
    /**
     * 后台管理错误码统一添加:ADMIN
     */
    ADMIN_WITHDRAW_NOTEXIT(410, "暂时查询不到相关提现记录"),
    ADMIN_WITHDRAW_EXPORTEXCEL(411, "暂无记录，无法导出数据"),

    ADMIN_CHARGERECORD_NOTEXIT(420, "暂时查询不到相关充值记录"),
    ADMIN_SHAREREGISTER_NOUSERS(430, "分享人和注册用户不存在或分享人和注册用户UID不存在"),
    ADMIN_SHAREREGISTER_CONFILT(431, "此注册用户已经有关联用户，不能再关联其他用户"),
    ADMIN_SHAREREGISTER_NOTEXIT(432, "抱歉,暂无关联记录"),
    ADMIN_SHAREREGISTER_NOTEXITLIST(433, "抱歉,查询不到被邀请用户信息"),
    ADMIN_USERS_NOTBINDALIYAPACCOUNT(440, "此用户没有绑定支付宝，无法进行提现"),
    ADMIN_USERS_NOTNODIAMONDRECORD(441, "可能此提现订单已经提现,或者没有此提现记录"),
    ADMIN_ROOM_NOTEXIT(450, "房间不存在"),
    ADMIN_PRETTYNO_NOTEXITLIST(460, "抱歉,查询不到相关靓号信息"),
    ADMIN_PRETTYNO_EXIT(461, "靓号已经被使用或者用户已经拥有靓号"),
    ADMIN_PRETTYNO_DELETE(462, "靓号删除失败"),

    SERVER_ERROR(500, "服务器错误"),
    UPDATE_UINFO_ERROR(500, "更新账号信息异常"),
    INTERFACE_IS_SUSPENDED(500, "接口暂停使用"),

    PHONE_IS_EXISTS(501, "此号码已被绑定，请重新输入!"),
    SERVER_BUSY(503, "服务繁忙，请稍后重试"),
    /**
     * 统计相关错误吗
     */
    STAT_ROOMFLOW_NOTEXIT(510, "抱歉,暂无此用户的房间流水记录"),
    STAT_ROOMFLOW_DETAILNOTEXIT(511, "抱歉,暂无房间流水明细记录"),

    /**
     * 房间中该麦位有人
     */
    ROOM_MICRO_SOMEONE(601, "房间中该麦位有人"),
    //未知错误
    UNKNOWN(999, "unknown"),

    PRETTY_NO_ERROR(1200, "该靓号已存在"),
    HAS_PRETTY_NO_ERROR(1300, "该用户已存在靓号"),
    /**
     * 用户相关的状态码
     */
    iS_PROVING(1400, "正在审核中..."),
    NO_RIGHT(1401, "您无此特权"),
    USER_NOT_EXISTS(1404, "用户不存在"),
    USER_INFO_NOT_EXISTS(1405, "补全个人信息之后才能开房哦！"),
    NICK_TOO_LONG(1406, "昵称太长"),
    NICK_SENSITIVE_WORDS(1407, "官方语言提醒您文明用语"),
    NOT_LIKE_ONESELF(1408, "不能喜欢自己"),
    ROOM_WORDS(1409, "保存失败，官方语言提醒您文明用语~"),
    USER_SHARE_CODE_ERROR(1410, "错误的注册邀请码"),
    USER_SHARE_CODE_BIND_SELF(1411, "不能绑定自己"),
    USER_SHARE_CODE_NOT_EXITS(1412, "分享码不存在"),
    PARAMETER_ILLEGAL(1444, "参数异常"),
    PHONE_EXPIRE(4002, "验证码已过期"),
    TARGET_USER_NOT_EXISTS(1450, "目标用户不存在"),

    /**
     * 房间相关的状态码
     */
    ROOM_RUNNING(1500, "房间在运行中"),
    ROOM_CLOSED(1501, "房间已经关闭"),
    ROOM_NOT_EXIST(1502, "房间不存在"),
    ROOM_NO_AUTHORITY(1503, "不是房主或管理员"),


    NOT_HAVING_LIST(1600, "没有数据"),
    VIDEO_NOT_EXISTS(1604, "该视频不存在"),
    COLLECT_EXISTS(1605, "collect exists"),
    WEEK_NOT_WITH_CASH_TOW_NUMS(1600, "对不起，每周只能提现两次"),
    ALIAPY_ACCOUNT_NOTEXISTS(16001, "请先填写支付宝账号"),
    REQUEST_FREQUENT(16002, "请求过于频繁"),
    WITHDRAWAL_AMOUNT_INCORRECT(16003, "请输入1000的整数倍"),

    PRIVATE_PHOTOS_UP_MAX(1700, "more than 8 albums"),
    ROOM_RCMD_NO_INFO(1701, "当前时间暂时没有推荐房间"),
    ROOM_RCMD_POOL_EMPTY(1702, "当前时间暂时没有推荐房间"),
    ROOM_RCMD_OPTION_CLOSE(1703, "推荐房间功能已经关闭"),
    ROOM_RCMD_NOT_NEW(1707, "该用户不是新的用户"),

    NOT_ALLOW_INVITE_SELF(1801, "不允许邀请自己"),
    UR_INVITED_TODAY(1802, "您今天被邀请过了"),
    SHARE_USER_ENOUGH(1803, "已经超过邀请旧用户限额"),

    PARAM_ERROR(1900, "参数错误"),
    IS_UNBANG_STATUS(1901, "已经是解绑状态"),

    AUCTCUR_DOING(2100, "已经在拍卖中了"),
    AUCTCUR_LESS_THAN_MAX_MONEY(2101, "出价低于当前最高价格"),
    AUCTCUR_YOURSELF_ERROR(2102, "you can't auct yourself"),
    PURSE_MONEY_NOT_ENOUGH(2103, "账户余额不足，请充值"),
    DIAMOND_NUM_NOT_ENOUGH(2104, "钻石余额不足"),
    REDPACKET_NUM_NOT_ENOUGH(2105, "红包金额不足"),
    PURSE_CONCH_NUM_ERROR(2106, "次数异常"),
    PURSE_CONCH_NOT_ENOUGH(2107, "次数不足"),

    DUTY_NOT_EXISTS(2201, "任务不存在"),
    DUTY_SERVICE_NULL(2202, "任务不存在"),
    DUTY_NOT_FINISH(2203, "请先完成任务"),
    DUTY_ACHIEVED(2204, "您已经领取该奖励"),


    ACTIVITY_NOT_START(3100, "活动未开始"),
    ACTIVITY_END(3200, "活动已结束"),
    ORDER_NOT_EXISTS(3404, "密聊不存在"),
    ORDER_FINISHED(3100, "密聊已经结束"),

    BUSI_ERROR(4000, "服务器维护中，请稍后"),
    SMS_SEND_ERROR(4001, "短信发送失败"),
    PHONE_INVALID(4002, "请输入正确的手机号码"),
    SMS_CODE_ERROR(4003, "短信验证码错误, 请重新输入!"),
    PASSWORD_ERROR(4005, "输入密码错误, 请重新输入!"),
    PHONE_A_LOT(4006, "厅主账号请使用ID登录!"),
    PASSWORD_SECOND_ERROR(4007, "输入二级密码错误, 请重新输入!"),

    /**
     * 支付错误提示
     */
    CHARGE_NOCHANNEL(4500, "抱歉，暂时不支持此渠道支付，请选择其他支付渠道"),
    CHARGE_PROD_NOT_EXISTS(4501, "充值产品不存在"),
    CHARGE_WX_REQUEST(4502, "微信接口错误"),
    CHARGE_VALIDATE_ERROR(4503, "验证信息错误"),
    CHARGE_SERVER_BUSY(4504, "充值繁忙，请稍后重试"),
    CHARGE_ILLEGAL(4505, "非法的充值请求"),
    INVALID_RECEIPT(4506, "非法的支付凭据"),


    SERV_EXCEPTION(5000, "service exception"),

    MICRO_NOT_ININVIATED_LIST(6000, "you are not in invited list"),
    MICRO_NUM_LIMIT(6001, "micro number limit"),

    GOLOD_EXCHANGE_DIAMOND_NOT_ENOUGH(7001, "钻石余额不足"),
    EXCHANGE_INPUT_ERROR(7002, "输入错误，请输入10的整数倍数"),
    GIVE_INPUT_ERROR(7003, "金币数量异常"),


    GIFT_DOWN_OR_NOT_EXISTS(8000, "该礼物已下架，敬请期待更多礼物哦~"),
    GIFT_DRAW_NOT_ENOUGH(8001, "捡海螺礼物数量不足,可以通过捡海螺获得"),
    GIFT_CANNOT_GIVE(8002, "该礼物不允许赠送"),
    MALL_CANNOT_PURSE(8003, "购买失败,该商品不能购买,请联系客服"),
    MALL_PURSE_LEVEL_LEFT(8004, "购买失败,未达到规定条件"),
    MALL_GIVE_LEVEL_LEFT(8005, "赠送失败,对方未达到规定条件"),
    MALL_CANNOT_GIVE(8006, "赠送失败,该商品不能赠送,请联系客服"),
    GIFT_CONTROL(8007, "宝箱礼物一次只能送出一个"),
    GIFT_DRAW_ILLEGAL(8008, "该房间不能捡海螺"),
    GIFT_NOT_SEND_IN_DRAW(8009, "该礼物不能在捡海螺厅送出哦"),
    GIFT_SEND_IN_DRAW(8010, "该礼物只能在捡海螺厅送出哦"),
    GIFT_PONIT_NOT_ENOUGH(8011, "点点币礼物数量不足"),
    GIFT_NOT_CALL_(8012, "只能赠送打call礼物"),
    GIFT_NOT_EXISTS(8020,"礼物不存在"),
    GIFT_PROP_NOT_ENOUGH(8100, "活动礼物数量不足,可以通过打call或商城购买获得"),
    GIFT_TYPE_ERROR(8101, "礼物类型错误"),
    HL_GIFT_TYPE_ERROR(8102, "只能在非相亲房里使用噢~"),
    XQ_GIFT_TYPE_ERROR(8103, "只能在相亲房里使用噢~"),
    GIFT_XQ_NOT_ENOUGH(8104, "捡信物礼物数量不足,可以通过拆信物获得"),

    NO_MORE_CHANCE(9000, "没有抽奖机会了，赶紧充值获得机会吧！"),
    NO_COLLECT_WORD_FINISH(9001, "没有收集完成，赶紧抽奖获得机会吧！"),

    VERSION_IS_NULL(80001, "版本为空，请检查版本号"),

    VERSION_TO_LOW(80002, "版本过低，请升级版本号"),

    LIKE_USER_ENOUGH(90001, "您今天喜欢的人数已达上限,升级后可以喜欢更多的人!"),


    ERROR1(10081, "请选择一个碎片"),
    ERROR2(10082, "服务端响应:"),
    ERROR3(10083, "保存数据失败"),
    ERROR4(10084, "今天已经签过到了.."),

    // 中秋活动相关状态码
    MID_DRAW_NOT_ENOUGH(10101, "捡海螺次数不足, 不能进行兑换"),
    MID_EXCHANGE_USED_UP(10102, "兑换次数已经用完"),
    // 国庆活动相关
    NATIONAL_BAG_NOT_ENOUGH(10201, "福袋不足, 不能进行抽奖"),
    WX_OPENID_UNION_ID_NOT_MISMATCH(10202,"微信openId,unionId不匹配"),
    WX_OPENID_UNION_ID_NOT_DIFFER(10203,"你提供的微信号与你绑定的微信号不一致,请校验"),
    WX_OPENID_NOT_REGISTER(10204, "该微信账号未注册！"),
    OPE_FREQUENT(701, "您操作得太频繁了，请休息一会"),
    PHONE_NO_BINDED(4004, "该手机号未绑定任何官方APP账号，请先在APP绑定"),


    //实名验证:2501 - 2599
    USER_REAL_NAME_NTO_VERIFIED(2501, "您还没有进行实名验证"),
    USER_REAL_NAME_NEED_VERIFIED(2502, "该功能需要进行实名验证"),
    USER_REAL_NAME_AUDITING(2503, "您的实名认证信息正在审核中……"),
    USER_REAL_NAME_VERIFIED(2504, "您已经通过了实名认证"),
    USER_REAL_NAME_IDCARDNO_EXISTS(2505, "身份证号已经存在"),
    USER_REAL_NAME_ID_CARD_NO_LIMIT(2506, "该身份信息超过验证上限"),
    USER_REAL_NAME_ROOM_CHAT(2507, "房间内发言，需要先实名验证"),
    USER_REAL_NAME_HALL_CHAT(2507, "大厅内发言，需要先实名验证"),
    USER_REAL_NAME_OPEN_ROOM(2507, "创建房间，需要先实名验证"),
    USER_REAL_NAME_AUDITING_CHAT(2508, "很抱歉审核期内暂时无法使用该功能"),
    USER_REAL_NAME_NEED_PHONE_ROOM_CHAT(2511, "房间内发言，需要先绑定手机"),
    USER_REAL_NAME_NEED_PHONE_HALL_CHAT(2511, "大厅内发言，需要先绑定手机"),
    USER_REAL_NAME_NEED_PHONE_OPEN_ROOM(2511, "创建房间，需要先绑定手机"),
    USER_REAL_NAME_NOT_VERIFIED_ERROR(2501, "为保证您的财产安全，请先进行实名认证"),
    USER_REAL_NAME_UNDER_REVIEW_ERROR(2503, "实名认证审核中"),

    PAY_CHANNEL_NOT_EXISTS(2600, "支付渠道不正确,请确认"),

    TEENS_CHARGE_LIMIT(2700, "你已开启青少年模式，充值额度受到限制"),

    // 萌币中心：2401-2499
    MCOIN_PURSE_NOT_ENOUGH(2401, "点点币不足！"),
    MCOIN_DRAW_ISSUE_NOT_EXITS(2402, "该期号不存在或者已失效"),
    MCOIN_DRAW_ISSUE_UNEFFECT(2403, "该期号已失效"),
    MCOIN_DRAW_ISSUE_NOT_START(2403, "该期号抽奖还没有开始"),
    MCOIN_DRAW_TICKET_NOT_ENOUGH(2403, "可购买号码不足"),
    MISSION_NOT_EXISTS(4101, "任务不存在"),
    MISSION_SERVICE_NULL(4102, "任务不存在"),
    MISSION_NOT_FINISH(4103, "请先完成任务"),
    MISSION_ACHIEVED(4104, "您已经领取该奖励"),
    MISSION_GAIN_FAIL(4105, "该任务领取失败"),
    MISSION_UNKONWN(4106, "领取失败，未知任务！"),
    POINT_PURSE_MONEY_NOT_ENOUGH(2103, "点点币余额不足,请通过任务获得。"),

    //萌币PK相关
    MCOIN_PK_STATUS_ERR(10401, "活动不在有效期内"),
    CHECK_PROHIBIT_MODIFICATION_ERROR(10402, "由于系统升级暂停使用此功能"),

    MORA_NUMBER_TIMES_EXCEEDED(60000,"您今日的游戏体验次数已用完"),
    MORA_RECORD_NOT_EXISTS(60001,"该PK记录不存在"),
    MORA_PK_OVER(60002,"该PK已结束"),
    MORA_SELF_NOT_PK_OVER(60002,"不能与自己PK"),
    MORA_INSUFFICIEN_BALANCE(60003,"你的余额不足无法参与猜拳"),
    MORA_LEVEL_NOT_ENOUGH(60004,"你的等级不够无法参与猜拳"),
    MORA_PROBABILITY_NOT_EXISTS(60005,"概率不存在"),
    MORA_PK_SLOW_DOWN(60006,"好像慢了一步,无法参与本次猜拳"),



    // box礼盒相关
    BOX_NUM_NOT_ENOUGH(60100,"礼盒次数不足"),
    LACK_OF_INTEGRAL(70001, "积分不足"),
    REWARD_FINISHED(70002, "奖励已被领取完啦"),
    SIGNUP_TOAST_ERROR(70003, "暂不支持未成年人注册"),


    //公会相关 3500 - 3599
    GUILD_NOT_EXIST(3500, "公会不存在或已解散"),
    GUILD_MEMBER_NOT_EXIST(3501, "未加入公会或已退出公会"),
    GUILD_APPLY_JOIN_REASON_TOO_LONG(3502, "申请理由太长"),
    GUILD_ALREADY_JOIN(3503, "已加入该公会"),
    GUILD_ALREADY_JOIN_OTHER(3504, "已加入其他公会，不能再申请"),
    GUILD_ALREADY_APPLY(3505, "你已提交申请，请耐心等待小秘书通知"),
    GUILD_HALL_NOT_EXIST(3506, "厅不存在"),
    GUILD_HALL_IS_FULL(3507, "该厅已满员"),
    GUILD_APPLY_ERROR(3508, "申请提交失败，请稍后重试"),
    GUILD_APPLY_NOT_EXIST(3509, "审核记录不存在"),
    GUILD_APPLY_HANDLED(3510, "该审核已由他人处理"),
    GUILD_APPLY_HANDLE_FAILD(3511, "此次审核操作失败，请稍后重试"),
    GUILD_HALL_USER_CANNOT_EXIT(3512, "厅主不能提交申请"),
    GUILD_PRESIDENT_CANNOT_EXIT(3513, "会长不能提交申请"),
    GUILD_APPLY_NO_PERMISSION(3514, "你没有审核权限"),
    GUILD_HALL_ALREADY_JOIN(3515, "该成员已加入该厅"),
    GUILD_HALL_ALREADY_JOIN_OTHER(3516, "该成员已加入该公会的其他厅"),
    GUILD_MEMBER_NOT_JOIN_HALL(3516, "该成员未加入厅"),



    //房间PK相关 3600 - 3699
    ROOMVS_NOT_EXIST(3600, "房间PK不存在"),
    ROOMVS_IS_END(3601, "房间PK已结束"),
    ROOM_NO_MICUSER(1504, "不是麦上用户"),
    ;

    private final int value;

    private final String message;

    WebServiceCode(int value, String message) {
        this.value = value;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{\"code\":" + value + ",\"message\":\"" + message + "\"}";
    }

    public int getValue() {
        return this.value;
    }

    public String getMessage() {
        return message;
    }
}