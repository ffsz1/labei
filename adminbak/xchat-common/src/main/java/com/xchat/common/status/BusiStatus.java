package com.xchat.common.status;

public enum BusiStatus {

    SUCCESS(200, "success"),            //成功
    NOTEXISTS(404, "不存在"),
    NOAUTHORITY(401, "没有权限"),
    NOTAUTHORITY(402, ""),
    SIGN_AUTHORITY(403, "非法的操作"),
    SERVERERROR(500, "服务器错误"),
    SERVERBUSY(503, "服务器正在维护中"),

    ACTIVITYNOTSTART(3100, "活动未开始"),
    ACTIVITYNOTEND(3200, "活动已结束"),

    INVALID_SERVICE(199, "invalid service"),//服务不可用

    SERVEXCEPTION(5000, "service exception"),

    UNKNOWN(999, "unknown"),//未知错误

    BUSIERROR(4000, "服务器维护中，请稍后"),

    PARAMETERILLEGAL(1444, "参数异常"),

    OP_TO_FAST(1445, "操作过于频繁，5s后重试"),

    USERNOTEXISTS(1404, "用户不存在"),

    USERINFONOTEXISTS(1405, "补全个人信息之后才能开房哦！"),

    NICKTOOLONG(1406, "昵称太长"),

    VIDEONOTEXISTS(1604, "该视频不存在"),

    COLLECTEXISTS(1605, "collect exists"),

    NOTLIKEONESELF(1500, "不能喜欢自己"),

    WEEKNOTWITHCASHTOWNUMS(1600, "对不起，每周只能提现两次"),

    ALIAPYACCOUNTNOTEXISTS(16001, "请先填写支付宝账号"),

    PRIVATEPHOTOSUPMAX(1700, "more than 8 albums"),

    ROOM_WORDS(500, "保存失败，小喵提醒您文明用语~"),
    ROOMRUNNING(1500, "房间在运行中"),
    ROOMRCLOSED(1501, "房间已经关闭"),
    ROOMNOTEXIST(1502, "房间不存在"),
    NOTHAVINGLIST(1600, "没有数据"),

    ROOM_RCMD_NOT_NEW(1700, "该用户不是新的用户"),
    ROOM_RCMD_NO_INFO(1701, "当前时间暂时没有推荐房间"),
    ROOM_RCMD_POOL_EMPTY(1702, "当前时间暂时没有推荐房间"),
    ROOM_RCMD_OPTION_CLOSE(1703, "推荐房间功能已经关闭"),
    ROOM_RCMD_TIME_FORMAT_ERROR(1704, "时间格式错误"),
    ROOM_RCMD_TIME_INVALID(1705, "无效的时间格式"),
    ROOM_RCMD_TIME_CONFLICT(1706, "该时间段已经存在推荐"),

    YOUNGG_DRAW_COUNT_NOT_ENOUGH(1801, "活动抽奖次数不足"),
    YOUNGG_DRAW_GIFT_NOT_ENOUGH(1802, "奖品池奖品已经被领光了"),
    YOUNGG_IS_END(1803, "养鸡活动已经结束"),


    AUCTCURDOING(2100, "已经在拍卖中了"),

    AUCTCURLESSTHANMAXMONEY(2101, "出价低于当前最高价格"),
    AUCTCURYOURSELFERROR(2102, "you can't auct yourself"),
    PURSEMONEYNOTENOUGH(2103, "账户余额不足，请充值"),
    DIAMONDNUMNOTENOUGH(2104, "钻石余额不足"),
    REDPACKETNUMNOTENOUGH(2105, "红包金额不足"),
    ORDERNOTEXISTS(3404, "密聊不存在"),
    ORDERFINISHED(3100, "密聊已经结束"),

    SMSSENDERROR(4001, "短信发送失败"),
    PHONEINVALID(4002, "请输入正确的手机号码"),
    SMSCODEERROR(4003, "短信验证码错误，请重新输入！"),

    MICRONOTININVIATEDLIST(6000, "you are not in invited list"),
    MICRONUMLIMIT(60001, "micro number limit"),

    GOLODEXCHANGEDIAMONDNOTENOUGH(70001, "钻石余额不足"),
    EXCHANGEINPUTERROR(70002, "输入错误，请输入10的整数倍数"),

    GIFTDOWNORNOTEXISTS(8000, "该礼物已下架，敬请期待更多礼物哦~"),
    GIFTCATNOTEXISTS(404, "该座驾不存在~"),
    GIFTCATPURSENOTEXISTS(405, "您还没有购买过该座驾~"),
    GIFTNOAUTHORITY(8401, "贵族等级不够哦~"),

    NOBLENORECOMCOUNT(3401, "该贵族没有推荐的机会"),
    NOBLENOAUTH(3402, "该贵族该特权"),
    NOBLENOTEXISTS(3404, "贵族不存在"),
    NOBLEUSEREXIST(3301, "该等级贵族已经开通"),
    NOBLEOPENERROR(3500, "开通贵族失败"),
    NOBLEEXPIRE(3501, "贵族等级已过期"),

    SMSIPTOOFTEN(302, "获取短信过于频繁"),

    PRETTYNOERROR(1200, "该靓号已存在"),
    HASPRETTYNOERROR(1300, "该用户已存在靓号"),
    iSPROVING(1400, "正在审核中..."),
    PARAMERROR(1900, "参数错误"),
    NORIGHT(1401, "您无此特权"),

    ISUNBANGSTATUS(1901, "已经是解绑状态"),

    NOMORECHANCE(9000, "没有抽奖机会了，赶紧充值获得机会吧！"),

    VERSIONISNULL(80001, "版本为空，请检查版本号"),
    VERSIONTOLOW(80002, "版本过低，请升级版本号"),

    HOTEXISTS(405, "该房间已经购买过此时间段的推荐位！"),
    DATEEXISTS(406, "此时间段已经过期，请购买其它时间段！"),
    USERUPDATEGENDER(407, "请选择性别"),

    /**
     * 支付错误提示
     */
    CHARGE_NOCHANNEL(4500, "抱歉，暂时不支持此渠道支付，请选择其他支付渠道"),

    /**
     * 财富 魅力等级错误码
     */
    LEVEL_EXPERIENCE_NOLEVEL(410, "抱歉，查询不到您的经验等级"),
    LEVEL_CHARM_NOLEVEL(411, "抱歉，查询不到您的魅力等级"),
    /**
     * 统计相关错误吗
     */
    STAT_ROOMFLOW_NOTEXIT(510, "抱歉,暂无此用户的房间流水记录"),
    STAT_ROOMFLOW_DETAILNOTEXIT(511, "抱歉,暂无房间流水明细记录"),
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

    ADMIN_USERS_NOTBINDCAR(440, "此用户没有绑定银行卡，无法进行提现"),
    ADMIN_USERS_NOTBINDWX(440, "此用户没有绑定微信，无法进行提现"),
    ADMIN_USERS_NOTBINDALIYAPACCOUNT(440, "此用户没有绑定支付宝，无法进行提现"),
    ADMIN_USERS_NOTNODIAMONDRECORD(441, "可能此提现订单已经提现,或者没有此提现记录"),

    ADMIN_ROOM_NOTEXIT(450, "房间不存在"),

    ROBOT_NOTEXIT(401, "机器人不足"),

    ADMIN_PRETTYNO_NOTEXITLIST(460, "抱歉,查询不到相关靓号信息"),
    ADMIN_PRETTYNO_EXIT(461, "靓号已经被使用或者用户已经拥有靓号"),
    ADMIN_PRETTYNO_DELETE(462, "靓号删除失败"),

    USERS_EXISTED_ERROR(464,"该用户已存在可用状态,故不能添加,请确认!"),
    FAMILY_JOIN_USERS(463, "该用户已加入别的家族"),
    USERS_WITHDRAW_WHITELIST_ISEXISTS_ERROR(480, "该用户已存在白名单列表中"),
    GIFT_NOT_EXISTS(490, "礼物不存在"),
    ADMIN_DATE_SUCCESS(465, "数据已处理"),
    TRANSFER_ERROR(488,"转账回调失败!"),
    GUILD_USER_IS_EXITIST(484,"该用户已加入其他公会"),
    GUILD_USER_ERBANNO_IS_EXITIST(484,"该账号已是其他公会长"),
    GUILD_HALL_USER_IS_EXITIST(494,"该用户已加入其他厅/公会"),
    GUILD_ROOMID_NOTEXIT(495,"房间Id数据不存在"),
    GUILD_HALLID_NOTEXIT(496,"厅Id数据不存在"),
    GUILD_GUILDID_NOTEXIT(497,"公会Id数据不存在"),
    GUILD_USERID_NOTEXIT(498,"用户Id数据不存在"),
    GUILD_USER_ERBANNO_EXITIST_WARN(484,"该用户是该厅的公会长"),
    ;

    private final int value;

    private final String reasonPhrase;

    private BusiStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }


    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
