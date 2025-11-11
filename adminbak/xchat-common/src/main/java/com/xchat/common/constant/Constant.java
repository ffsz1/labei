
package com.xchat.common.constant;

import com.xchat.common.utils.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Constant {
//    public static final long LOW_VERSION = Utils.version2long("1.0.6");



    public static class RoomOptStatus {
        public static byte in = 1;
        public static byte out = 2;
    }

    public static class AuctCurStatus {
        public static Byte ing = 1;// 竞拍中
        public static Byte finish = 2;// 竞拍完成
        public static Byte error = 2;// 竞拍中断
    }

    public static class UsersAtt {
        public static Byte A = 1;// A面用户
        public static Byte B = 2;// B面用户
    }

    public static class RoomAtt {
        public static Byte A = 1;// A面房间
        public static Byte B = 2;// B面房间
    }

    public static class OrderServType {
        public static Byte auct = 1;// 竞拍单
        public static Byte reward = 2;// 悬赏单
    }

    public static class OrderServStatus {
        public static Byte create = 1;// 订单生成
        public static Byte ing = 2;// 订单处理中
        public static Byte finish = 3;// 订单已完成
        public static Byte error = 4;// 订单已完成
    }

    public static class OrderTimeOutType {
        public static int oneTimeOut = 1;
        public static int towTimeOut = 2;
    }

    public static class BillType {
        public static Byte charge = 1;// 充值
        public static Byte getCash = 2;// 提现
        public static Byte orderPay = 3;// 消费订单支出
        public static Byte orderIncome = 4;// 服务订单收入
        public static Byte giftPay = 5;// 刷礼物消费
        public static Byte giftIncome = 6;// 收礼物收入
        public static Byte redPackPay = 7;// 发个人红包消费
        public static Byte redPackIncome = 8;// 收到个人红包收入
        public static Byte roomOwnerIncome = 9;// 房主佣金收入
        public static Byte drawGift = 10;// 抽礼物
        public static Byte useGift = 11;//  使用礼物
        public static Byte interSendGold = 12;//官方直接送金币
        public static Byte followSendGold = 13;// 关注公众号送金币
        public static Byte exchangeDimondToGoldPay = 14;// 钻石换金币
        public static Byte exchangeDimondToGoldIncome = 15;// 钻石换金币
        public static Byte purseCar = 16;    // 购买座驾
        public static Byte purseHot = 17;// 购买推荐位
        public static Byte clockAttend = 18;// 参加打卡活动
        public static Byte clockResult = 19;//打卡活动结算
        public static Byte chargeByCompanyAccount = 20; // 打款至公账充值金币
        public static Byte redeemCode = 21;  //兑换码兑换金币
        public static Byte draw = 23;  //抽奖得金币
        public static Byte bonusPerDaySend = 24;  //发送的
        public static Byte bonusPerDayRecv = 25;  //钻石回馈账单
        public static Byte openNoble = 26;   //开通贵族
        public static Byte renewNoble = 27;  //续费贵族
        public static Byte roomNoble = 28;   //房间内开通贵族分成
        public static Byte purseHeadwear = 29;    // 购买头饰
        public static Byte questionnaireDraw = 30;// 问卷抽奖
        public static Byte huodongsend = 37;   // 活动奖励金币---后台使用
    }

    public static class AppVersion {
        public static Byte online = 1;
        public static Byte audit = 2;
        public static Byte forceupdate = 3;
        public static Byte recommupdate = 4;
        public static Byte deleted = 5;
    }


    public static class ExchangeDiamondGold {
        public static double rate = 1;//2018-03-05发布更新

    }

    public static class RedPacket {
        public static Byte first = 1;//注册
        public static Byte share = 2;//分享房间
        public static Byte register = 3;//注册
        public static Byte bouns = 4;//推荐人充值分成奖励
        public static Byte withDraw = 5;//红包提现
        public static Byte superiorBouns = 6;//上级推荐人充值分成奖励
    }

    public static class PacketStatus {
        public static Byte create = 1;//创建提现申请
        public static Byte suc = 2;//成功提现并且已经转账
        public static Byte error = 3;//判定为刷红包行为
    }

    public static class BillStartTime {
        public static Date startTime = new Date(1503072000000L);
    }

    public static class SexRobotType {
        public static Byte man = 1;// 男性
        public static Byte woMan = 2;// 女性
    }

    public static class DefMsgType {

        public static int Auction = 1;
        public static int AuctionStart = 11;
        public static int AuctionFinish = 12;
        public static int AuctionUpdate = 13;

        public static int Reward = 2;
        public static int RewardChange = 21;

        public static int Gift = 3;
        public static int GiftSend = 31;

        public static int GiftServiceSend = 14;

        public static int GiftWhole = 12;//全麦送礼物，兼容新旧版本，first不能用Gift=3
        public static int GiftSendWholeMicro = 121;//全麦送礼物

        public static int Micro = 4;
        public static int MicroUp = 41;// 用户自己上麦
        public static int MicroDown = 42;// 用户自己下麦
        public static int MicroApply = 43;// 用户申请上麦
        public static int MicroOwnerExchange = 44;// 更新交换麦序
        public static int MicroOwnerUpUser = 45;// 房主给用户上麦
        public static int MicroOwnerKickUser = 46;// 房主踢用户下麦
        public static int MicroOwnerTopUser = 47;// 房主将用户麦序置顶

        // ---------------micro version2---------------------
        public static int MicroInvite = 411;// 房主邀请用户上麦
        public static int MicroAccept = 412;// 用户同意上麦
        public static int MicroOwnerKickUserV2 = 413;// 房主踢用户下麦
        public static int MicroOwnerTopUserV2 = 414;// 房主将麦序用户置顶
        public static int MicroUserLeftV2 = 415;// 用户自行离开麦序
        // ---------------micro version2-----------------------

        public static int Purse = 5;
        public static int PurseGoldMinus = 51;// 用户金币消费

        public static int RoomOpen = 6;
        public static int RoomOpenNotice = 61;// 用户金币消费

        public static int DealFinish = 7;
        public static int DealFinishNotice = 71; //用户拍卖消费

        public static int OrderReTime = 8;
        public static int OrderReTimeNotice = 81; //订单剩余时间


        public static int PointToPointMsg = 10;//发送点对点消息
        public static int PushPicWordMsg = 101; //发送图文消息

        public static int Packet = 11;
        public static int PacketFirst = 111;
        public static int PacketShare = 112;
        public static int PacketRegister = 113;
        public static int PacketBouns = 114;

        public static int baoliu = 12;//客户端已经使用该字段，保留该字段


        public static int Draw = 13;
        public static int DrawChance = 131;

        public static int roomMessage = 16;

        public static final int RoomPk = 19;
        public static final int RoomPkResult = 28;
        public static int userRealAudit = 35;
        public static int CLIENT_UPLOAD_LOG = 52;

    }

    /**
     * 上麦操作状态
     */
    public static class MicroHandleStatus {
        public static int invited = 1;// 房主发起邀请上麦
        public static int inmicro = 2;// 已经在麦上
    }

    public static class EventType {
        public static String CONVERSATION = "1";// 表示CONVERSATION消息，即会话类型的消息（目前包括P2P聊天消息，群组聊天消息，群组操作，好友操作）
        public static String LOGIN = "2";// 表示LOGIN消息，即用户登录事件的消息
        public static String LOGOUT = "3";// 表示LOGOUT消息，即用户登出事件的消息
        public static String CHATROOM = "4";// 表示CHATROOM消息，即聊天室中聊天的消息
        public static String AUDIO = "5";// 表示AUDIO/VEDIO/DataTunnel消息，即汇报实时音视频通话时长、白板事件时长的消息
        public static String AUDIOSAVE = "6";// 表示音视频/白板文件存储信息，即汇报音视频/白板文件的大小、下载地址等消息
        public static String DRAWBACK = "7";// 表示单聊消息撤回抄送
        public static String QDRAWBACK = "8";// 表示群聊消息撤回抄送
        public static String CHATROOMINTOUT = "9";// 表示CHATROOM_INOUT信息，即汇报主播或管理员进出聊天室事件消息
        public static String ECPCALLBACK = "10"; // 表示ECP_CALLBACK信息，即汇报专线电话通话结束回调抄送的消息
        public static String SMSCALLBACK = "11"; // 表示SMS_CALLBACK信息，即汇报短信回执抄送的消息
        public static String SMSREPLY = "12"; // 表示SMS_REPLY信息，即汇报短信上行消息
        public static String AVROOMINOUT = "13"; // 表示AVROOM_INOUT信息，即汇报用户进出音视频/白板房间的消息
        public static String CHATROOMQUEUEOPERATE = "14"; // 表示CHATROOM_QUEUE_OPERATE信息，即汇报聊天室队列操作的事件消息
    }

    /**
     * 设置聊天室内用户角 色 1: 设置为管理员，operator必须是创建者 2:设置普通等级用户，operator必须是创建者或管理员
     * -1:设为黑名单用户，operator必须是创建者或管理员 -2:设为禁言用户，operator必须是创建者或管理员
     */
    public static class RoleOpt {
        public static int admin = 1;
        public static int common = 2;
        public static int bloack = -1;
        public static int shutup = -2;
    }

    /**
     * 充值货币类型
     */
    public static class Currency {
        public static String cny = "cny";// 人民币

    }

    /**
     * 充值记录状态
     */
    public static class ChargeRecordStatus {
        public static Byte create = 1;
        public static Byte finish = 2;
        public static Byte error = 3;
        public static Byte timeout = 4;

    }

    public static class ChargeChannel {
        public static final String alipay = "alipay";  // 支付宝支付
        public static final String wx = "wx";          // 微信支付
        public static final String wx_pub = "wx_pub";  // 微信公众号支付
        public static final String ios_pay = "ios_pay";// IOS支付
        public static final String wx_wap = "wx_wap";  // 微信H5支付
        public static final String alipay_wap = "alipay_wap";  // 支付宝H5支付
        public static final String exchageByDiamond = "exchange";//钻石兑换
        public static final String companyAccount = "companyAccount";//充值打公账
    }

    public static class DepositStatus {
        public static Byte create = 1;
        public static Byte finish = 2;
    }

    public static class DepositUseType {
        public static Byte auct = 1;
        public static Byte reward = 2;
    }

    public static class Visitor {
        public static Long visitorErbanNo = 99999999999L;
    }

    /**
     * 送礼物对象类型 1轻聊房间、竞拍房间给主播直接刷礼物，2私聊送个人礼物,3坑位房中，给坑位中的人刷礼物
     */
    public static class SendGiftType {
        public static final byte room = 1;
        public static final byte person = 2;
        public static final byte roomToperson = 3;
        /**
         * 表白
         */
        public static final byte express = 4;
    }

    public static class official {
        public static Long uid = 500001L;
    }

    public static class PayloadSkiptype {
        // 1跳app页面，2跳聊天室，3跳h5页面
        public static int apppage = 1;
        public static int room = 2;
        public static int h5 = 3;
    }

    public static class PayloadRoute {
        //1.跳转订单页面2.跳转打电话页面
        public static int order = 1;
        public static int call = 2;
    }

    public static class Rate {
        public static double moneyToGold = 10;
    }

    public static class Commission {
        //        public static double officialGift = 0.6;// 礼物官方佣金
//        public static double officialGift = 0.5;// 礼物官方佣金
//        public static double officialAuctOrder = 0.57;// 竞拍订单官方佣金
//        public static double roomOwnerAuctOrder = 0.03;// 竞拍订单房主佣金
        //        public static double akira = 0.4;// 声优，收礼物方、订单服务者佣金
//        public static double akira = 0.5;// 声优，收礼物方、订单服务者佣金
//        public static double akira = Double.parseDouble(PropertyUtil.getProperty("app_akira"));
    }

    /**
     * 预扣款操作类型，1是在原来基础上增加，2是直接替换
     */
    public static class DepositUpdateType {
        public static int plus = 1;
        public static int replace = 2;
    }

    public static class SysConfId {
        public static final String auditing_version = "auditing_version";
        public static final String cur_gift_version = "cur_gift_version";
        public static final String cur_gift_car_version = "cur_gift_car_version";//座驾
        public static final String face_version = "face_version";
        public static final String homerecomm_sort_type = "homerecomm_sort_type";
        public static final String draw_act_switch = "draw_act_switch";  // 抽奖活动开关
        public static final String splash_pict = "splash_pict";      // 闪屏图片
        public static final String splash_link = "splash_link";      // 闪屏链接
        public static final String splash_type = "splash_type";      // 跳转类型
        public static final String newest_version = "newest_version";  //当前最新版本

        /**
         * 猜神秘嘉宾名字活动的开始时间
         */
        public static final String guess_name_start_date = "guess_name_start_date";
        /**
         * 猜神秘嘉宾名字活动的天数,
         */
        public static final String guess_name_days = "guess_name_days";
        /**
         * 提示的图片
         */
        public static final String guess_name_prompt_pic = "guess_name_prompt_pic";
    }

    public static class ActiveMq {
        public static final String send_gift = "sendGift";
        public static final String sum_list = "sumList";
        public static final String MESS_FIELD_TYPE = "field_type";
        public static final String MESS_FIELD_GIFT = "field_gift";
        public static final String MESS_FIELD_STAT = "field_stat";
        public static final int MESS_TYPE_GIFT = 1; // 送礼物
        public static final int MESS_TYPE_STAT = 2; // 统计

    }


    public static class SendGold {
        public static final Long boundPhone = 50L; //绑定手机送50金币
        public static final Long shareSendGold = 20L; //分享人送20金币
        public static final Long followWX = 20L; //关注微信公众号送20金币
    }

    public static class WithDraw {
        public static final Byte ing = 1; //发起提现
        public static final Byte finish = 2; //提现完成
        public static final Byte error = 3; //提现异常
        public static final Byte frozen = 4; //提现异常
    }

    public static class usersType {
        public static final Byte account = 1; //普通账号
        public static final Byte vipAccount = 2; //官方账号
        public static final Byte robotAccount = 3; //机器人账号
    }

    public static class ChargeProdStatus {
        public static final Byte using = 1;
        public static final Byte deleted = 2;

    }

    public static class FaceStatus {
        public static final Byte using = 1;
        public static final Byte deleted = 2;

    }

    public static class IOSAuditAccount {
        public static final List auditAccountList = Arrays.asList(1000000L, 1000004L, 1000005L, 1000006L, 1000007L, 1000814L,
                1000834L, 1000836L, 1000837L, 1000969L, 1000970L, 1000971L, 1000972L, 1000973L, 1000974L, 1000819L, 1111599L,
                1152595L, 1153883L);
        public static final List auditAccountRoomList = Arrays.asList(11463893L, 11472789L, 11465903L, 11477213L);
    }

    public static class ActStatus {
        public static final Byte using = 1;
        public static final Byte deleted = 2;

    }

    public static class PacketProStauts {
        public static final Byte using = 1;
        public static final Byte deleted = 2;
    }

    /**
     * 2017-10-26升级
     * 1.分享红包,前七次分享0.5到1.5，后面分享0.1到0.5
     * 2.邀请红包，前七次邀请1到3，后面邀请0.5到1，限同一设备
     * 3.分成5%,限同一设备
     */
    public static class PacketNumRandomRegion {
        public static final Double ShareLess7[] = {0.50, 1.50};
        public static final Double ShareMore7[] = {0.10, 0.50};

        public static final Double RegisterLess7[] = {1.00, 3.00};
        public static final Double RegisterMore7[] = {0.50, 1.50};

    }

    public static class PacketConst {
        public static final double fistrtPacketNum = 20.00;
        public static final double bonusRate1 = 0.02;//推荐人充值分成
        public static final double bonusRate2 = 0.03;//推荐人充值分成
        public static final double bonusRate3 = 0.04;//推荐人充值分成
        public static final double bonusRate4 = 0.06;//推荐人充值分成
        public static final double superiorBonusRate1 = 0.005;//上级推荐人充值分成
        public static final double superiorBonusRate2 = 0.01;//上级推荐人充值分成
        public static final double superiorBonusRate3 = 0.015;//上级推荐人充值分成
        public static final double superiorBonusRate4 = 0.02;//上级推荐人充值分成
    }

    public static class RoomType {
        public static final Byte auct = 1; // 拍卖房
        public static final Byte radio = 2;// 轻聊房
        public static final Byte game = 3; // 轰趴房

    }

    public static class RoomTagType {
        public static final int NORMAL = 1; // 普通
        public static final int MENXIN = 2; // 拉贝新
        public static final int GATHER = 3; // 聚类
        public static final int ACTIVI = 4; // 活动,
        public static final int RADIOS = 5; // 电台
    }

    public static final Integer TAG_MENXIN_DAY = 30; // 拉贝新标签定义的天数
    public static final Integer ROOM_PAGE_SIZE = 10; // 房间列表的默认分页
    public static final Integer HOME_PAGE_SIZE = 12; // 首页列表的默认分页
    public static final Integer CACHE_MAX_SIZE = 180;// 缓存最大房间的数量

    public static class HomePageTag {
        public static final String hot = "1";
        public static final String radio = "2";
        public static final String game = "3";

    }

    public static class RankType {
        public static final Byte star = 1;
        public static final Byte noble = 2;
        public static final Byte room = 3;
    }

    public static class RankDatetype {
        public static final Byte day = 1;
        public static final Byte week = 2;
        public static final Byte total = 3;
    }

    /**
     * 首页排行榜计算占比
     */
    public static class HomeRankCalc {
        public static final double personNumValue = 0.4;//人气值
        public static final double goldFlow = 0.6;//流水

    }

    /**
     * 抽奖记录状态
     */
    public static class DrawStatus {
        public static final Byte create = 1;
        public static final Byte nonePrize = 2;
        public static final Byte hasPrize = 3;
    }

    public static class DrawSwitch {
        public static final String open = "1";
        public static final String close = "2";
    }

    public static class DrawRecordType {
        public static final Byte charge = 1;
        public static final Byte share = 2;
    }

    public static class RoomMic {
        public static final String ROOM_INFO = "roomInfo";
        public static final String MIC_QUEUE = "micQueue";
        public static final String MIC_INFO = "micInfo";
        public static final String KEY_INVITEUID = "inviteUid";
        public static final String KEY_POSITION = "position";
        public static final int ROOM_NOTIFY_TYPE_ROOM = 1;     // 房间更新
        public static final int ROOM_NOTIFY_TYPE_MIC = 2;      // 坑位更新

    }

    public static class ExchangeDiamondRange {
        public static final double exchangeDiamond100[] = {0, 0.28, 0.68, 0.88, 0.93, 0.98, 1};
        public static final double exchangeDiamond1000[] = {0, 0.2, 0.6, 0.8, 0.88, 0.93, 0.98, 1};
        public static final double exchangeDiamond5000[] = {0, 0.15, 0.55, 0.8, 0.88, 0.93, 0.98, 1};
    }

    public static class ExchangeDiamoudDraw {
        public static final double exchange1 = 0.01;// 1%
        public static final double exchange2 = 0.02;// 2%
        public static final double exchange3 = 0.03;// 3%
        public static final double exchange4 = 0.04;// 4%
        public static final double exchange5 = 0.05;// 5%
        public static final Integer exchange7 = 6;// 社会人
        public static final Integer exchange8 = 1;// 千纸鹤
        public static final Integer exchange9 = 3;// 豪爵
        public static final Integer exchange10 = 4;// 兰博
        public static final Integer exchange11 = 5;// 天马
    }

    public static class GiftDrawRange {
        public static final double gift20[] = {0, 0.66202, 0.8719, 0.9919, 0.992, 0.998, 1};
        public static final double gift200[] = {0, 0.66202, 0.8719, 0.9919, 0.992, 0.998, 1};
        public static final double gift_first20[] = {0, 0.32, 0.71198, 0.99198, 0.992, 0.998, 1};
        public static final double gift_first200[] = {0, 0.32, 0.71198, 0.99198, 0.992, 0.998, 1};
    }

    public static class GiftDrawList {
        public static final Integer giftDrawList[] = {1, 2, 4, 14, 15, 9};
    }

    public static class GiftDraw {
        public static final Integer gift1 = 1;// 玫瑰花
        public static final Integer gift2 = 2;// 棒棒糖
        public static final Integer gift3 = 4;// 空气猫
        public static final Integer gift4 = 14;// 余生有你
        public static final Integer gift5 = 15;// 跑车
        public static final Integer gift6 = 9;// 大飞机
    }

    public static class DrawRange {
        public static final double charge8[] = {0, 0.89, 0.99, 1};
        public static final double charge48[] = {0, 0.41, 0.95, 1};
        public static final double charge98[] = {0, 0.24, 0.87, 0.98, 1};
        public static final double charge198[] = {0, 0.72, 0.82, 0.97, 1};
        public static final double charge498[] = {0, 0.73, 0.88, 0.98, 1};
        public static final double charge998[] = {0, 0.57, 0.92, 0.99, 1};
        public static final double charge4998[] = {0, 0.42, 0.75, 0.87, 0.95, 1};
        public static final double charge9999[] = {0, 0.1, 0.6, 0.9, 1};
        public static final double charge30000[] = {0, 0.7, 1};
        public static final double charge60000[] = {0, 0.4, 1};
    }

    public static class DrawProd {
        public static final Integer none = 0;
        public static final Integer gold8 = 8;
        public static final Integer gold50 = 50;
        public static final Integer gold100 = 100;
        public static final Integer gold300 = 300;
        public static final Integer gold1000 = 1000;
        public static final Integer gold3000 = 3000;
        public static final Integer gold8888 = 8888;
        public static final Integer prettySeven = 10000;//七位靓号
    }

    public static class DrawPrettyErbanNoType {
        public static final Byte seven = new Byte("1");
    }

    public static class QuestionnaireRange {
        public static final double questionnaire[] = {0, 0.7, 0.95, 1};
    }

    public static class QuestionnaireProd {
        public static final Integer gold10 = 10;
        public static final Integer gold20 = 20;
        public static final Integer gold50 = 50;
    }

    /**
     * 支付业务的类型
     */
    public static class PayBussType {
        public static final byte charge = 0; // 充值金币
        public static final byte openNoble = 1;  // 开通贵族
        public static final byte renewNoble = 2; // 续费贵族
    }

    public static class NobleOptType {
        public static final byte open = 1;   // 开通贵族
        public static final byte renew = 2;  // 续费贵族
    }

    /**
     * 贵族开通或者续费的支付类型，支付类型，1：金币开通，2：自己付款开通，3：打款公账开通
     */
    public static class NoblePayType {
        public static final byte gold = 1;     // 金币开通
        public static final byte money = 2;    // 付款开通
        public static final byte company = 3;  // 公账开通
    }

    public static class NobleResType {
        public static final byte roombg = 1;  // 房间背景
        public static final byte bubble = 2;  // 聊天气泡
        public static final byte michalo = 3; // 上麦光晕
        public static final byte headwear = 4;// 个人头饰
        public static final byte badge = 5;   // 个人勋章
        public static final byte cardbg = 6;  // 卡片背景
        public static final byte zonebg = 7;  // 主页背景

    }


    /**
     * 推送云信自定义消息的子协议
     */
    public static class DefineProtocol {

        public static final int CUSTOM_MESS_DEFINE = 100;       // 表示自定义消息
        public static final int CUSTOM_MESS_HEAD_QUEUE = 8;     // 队列

        public static final int CUSTOM_MESS_SUB_INVITE = 81;    // 邀请上麦
        public static final int CUSTOM_MESS_SUB_KICKIT = 82;    // 踢它下麦

        public static final int CUSTOM_MESS_HEAD_NOBLE = 14;        // 贵族
        public static final int CUSTOM_MESS_SUB_OPENNOBLE = 142;    // 开通贵族
        public static final int CUSTOM_MESS_SUB_RENEWNOBLE = 143;   // 续费贵族
        public static final int CUSTOM_MESS_SUB_WILLEXPIRE = 144;   // 贵族快到期
        public static final int CUSTOM_MESS_SUB_HADEXPIRE = 145;    // 贵族已到期
        public static final int CUSTOM_MESS_SUB_GOODNUM_OK = 146;   // 靓号生效
        public static final int CUSTOM_MESS_SUB_GOODNUM_NOTOK = 147;// 靓号未生效
        public static final int CUSTOM_MESS_SUB_ROOM_INCOME = 148;  // 房主分成
        public static final int CUSTOM_MESS_SUB_RECOM_ROOM = 149;   // 推荐房间
    }

    public static class status {
        public static final Byte valid = 1; //有效
        public static final Byte invalid = 2; //无效
    }

    public static String DEFAULT_NICK = "拉贝新人";
    public static String DEFAULT_HEAD = "http://res.91fb.com/default_head.png";
    public static String DEFAULT_LOGO = "http://res.91fb.com/logo.png";

    public static class Regex {//正则匹配
        public static final String PHONE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\\\d{8}$";
    }

    public static class Gender {
        public static final String male = "1";//男
        public static final String female = "2";//女
    }

    public static final List<String> APPVERSION = Arrays.asList("2.4.2");//appversion

}

