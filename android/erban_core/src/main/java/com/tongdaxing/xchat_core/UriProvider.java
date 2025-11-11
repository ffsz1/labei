package com.tongdaxing.xchat_core;

import android.util.Log;

/**
 * 请求接口接口地址
 */
public class UriProvider {
    public static final String TAG = "UriProvider";
    public static String JAVA_WEB_URL;
    public static String IM_SERVER_URL;
    public static String ROOM_IM_URL;
    /**
     * 服务器地址
     */
    public static final String PRODUCT = "http://api.haijiaoxingqiu.cn";
    public static final String PRODUCT_18 = "http://api.haijiaoxingqiu.cn";
    public static final String DEBUG = "http://test.haijiaoxingqiu.cn";
    /**
     * 房间公聊屏IM地址
     */
    private static final String PRODUCT_WS_URL = "ws://api.haijiaoxingqiu.cn:3006/";//房间IM正式服
    private static final String PRODUCT_18_WS_URL = "ws://api.haijiaoxingqiu.cn:3006/";//房间IM预发服
    private static final String DEBUG_WS_URL = "ws://test.haijiaoxingqiu.cn:3006/";//房间IM测试服

    public static void init(int delEnv) {
        switch (delEnv) {
            case 1://测试服
                setUrl(DEBUG, DEBUG_WS_URL);
                break;
            case 2://正式服
                setUrl(PRODUCT, PRODUCT_WS_URL);
                break;
            case 3://预发服
                setUrl(PRODUCT_18, PRODUCT_18_WS_URL);
                break;
        }
    }

    /**
     * 动态更新ip
     *
     * @param ip
     */
    public static void upIp(String ip) {
        String url = "http://" + ip + ":80";
        String roomUrl = "ws://" + ip + ":3006/";
        setUrl(url, roomUrl);
    }

    private static void setUrl(String url, String roomUrl) {
        Log.e(TAG, "当前环境" + url + "\n" + roomUrl);
        JAVA_WEB_URL = url;
        IM_SERVER_URL = url;
        ROOM_IM_URL = roomUrl;
    }

    //-------------------------------------------以下猜拳相关接口-------------------------------------------

    /**
     * 获取绑定列表
     */
    public static String getFinancialAccount() {
        return IM_SERVER_URL.concat("/withDraw/getFinancialAccount");
    }

    /**
     * 是否实名
     */
    public static String realnameUser() {
        return IM_SERVER_URL.concat("/user/realname/v1/get");
    }

    /**
     * 打call
     *
     * @return
     */
    public static String callForUser() {
        return IM_SERVER_URL.concat("/gift/callForUser");
    }

    /**
     * 清除魅力值
     *
     * @return
     */
    public static String receiveRoomMicMsg() {
        return IM_SERVER_URL.concat("/room/receiveRoomMicMsg");
    }

    /**
     * 陪陪
     *
     * @return
     */
    public static String bestCompanies() {
        return IM_SERVER_URL.concat("/home/bestCompanies");
    } /**
     * 打call
     *
     * @return
     */

    /**
     * 兑换
     */
    public static String bindWithdrawAccount() {
        return IM_SERVER_URL.concat("/withDraw/bindWithdrawAccount");


    }

    public static String newUsers() {
        return IM_SERVER_URL.concat("/home/newUsers");
    }

    /**
     * 判断当前房间是否开启猜拳
     *
     * @return
     */
    public static String getFingerGuessingGameState() {
        return IM_SERVER_URL.concat("/play/mora/getState");
    }

    /**
     * 举报接口
     */
    public static String reportUrl() {
        return IM_SERVER_URL.concat("/user/report/save");
    }

    /**
     * 发起猜拳
     *
     * @return
     */
    public static String startFingerGuessingGame() {
        return IM_SERVER_URL.concat("/play/mora/getMoraInfo");
    }

    /**
     * 确定发起猜拳
     *
     * @return
     */
    public static String confirmFingerGuessingGame() {
        return IM_SERVER_URL.concat("/play/mora/confirmPk");
    }

    /**
     * 参与PK
     *
     * @return
     */
    public static String pkFingerGuessingGame() {
        return IM_SERVER_URL.concat("/play/mora/joinPk");
    }

    /**
     * 确定PK
     *
     * @return
     */
    public static String confrimPkFingerGuessingGame() {
        return IM_SERVER_URL.concat("/play/mora/confirmJoinPk");
    }

    /**
     * 获取猜拳记录
     *
     * @return
     */
    public static String getFingerGuessingGameRecord() {
        return IM_SERVER_URL.concat("/play/mora/record");
    }

    /**
     * 获取更多未被PK的猜拳消息
     *
     * @return
     */
    public static String getListFingerGuessingGame() {
        return IM_SERVER_URL.concat("/play/mora/getMoraRecord");
    }

    /**
     * @return
     */
    public static String getProbability() {
        return IM_SERVER_URL.concat("/play/mora/getProbability");
    }
    //-------------------------------------------以上猜拳相关接口-------------------------------------------

    public static String checkUpdata() {
        return IM_SERVER_URL.concat("/version/get");
    }

    /**
     * 注册接口
     */
    public static String getRegisterResourceUrl() {
        return IM_SERVER_URL.concat("/acc/signup");
    }


    /**
     * 登录接口
     */
    public static String getLoginResourceUrl() {
        return IM_SERVER_URL.concat("/oauth/token");
    }

    /**
     * 获取ticket
     */
    public static String getAuthTicket() {
        return IM_SERVER_URL.concat("/oauth/ticket");
    }

    /**
     * 获取短信验证码
     */
    public static String getSMSCode() {
        return IM_SERVER_URL.concat("/acc/sms");
    }

    /**
     * 找回，修改密码
     */
    public static String modifyPsw() {
        return IM_SERVER_URL.concat("/acc/pwd/reset");
    }

    //-------------------------------------------点点币相关接口---------------------------------------------------------------
    //点点币任务列表接口
    public static String getMengCoinTaskList() {
        return JAVA_WEB_URL.concat("/mcoin/v1/getInfo");
    }


    //公聊大厅领取红包记录接口
    public static String receiveMengCoinById() {
        return JAVA_WEB_URL.concat("/mcoin/v1/gainMcoin");
    }

    /**
     * 登出
     */
    public static String logout() {
        return IM_SERVER_URL.concat("/acc/logout");
    }

    /**
     * 等级
     */
    public static String levelCharmGet() {
        return IM_SERVER_URL.concat("/level/charm/get");
    }

    /**
     * 金币
     */
    public static String levelExeperienceGet() {
        return IM_SERVER_URL.concat("/level/exeperience/get");
    }

    public static String getUserInfo() {
        return IM_SERVER_URL.concat("/user/v3/get");
    }

    public static String getUserInfoListUrl() {
        return IM_SERVER_URL.concat("/user/list");
    }


    public static String updateUserInfo() {
        return IM_SERVER_URL.concat("/user/update");
    }

    public static String addPhoto() {
        return IM_SERVER_URL.concat("/photo/upload");
    }

    public static String deletePhoto() {
        return IM_SERVER_URL.concat("/photo/delPhoto");
    }

    public static String giftWall() {
        return IM_SERVER_URL.concat("/giftwall/get");
    }

    //获取用户收到的神秘礼物列表
    public static String getMysteryGiftWall() {
        return IM_SERVER_URL.concat("/giftwall/listMystic");
    }

    public static String praise() {
        return IM_SERVER_URL.concat("/fans/like");
    }

    public static String deleteLike() {
        return IM_SERVER_URL.concat("/fans/fdelete");
    }

    public static String isLike() {
        return IM_SERVER_URL.concat("/fans/islike");
    }

    public static String searchUserInfo() {
        return IM_SERVER_URL.concat("/search/user");
    }

    public static String getAllFans() {
        return IM_SERVER_URL.concat("/fans/following");

    }

    public static String getFansList() {
        return IM_SERVER_URL.concat("/fans/fanslist");

    }

    public static String getUserRoom() {
        return IM_SERVER_URL.concat("/userroom/get");
    }

    public static String userRoomIn() {
        return IM_SERVER_URL.concat("/userroom/in");
    }

    public static String userRoomOut() {
        return IM_SERVER_URL.concat("/userroom/out");
    }

    public static String isShowTeenager() {
        return IM_SERVER_URL.concat("/users/teens/mode/getUsersTeensMode");
    }

    /**
     * 房间标签列表
     */
    public static String getRoomTagList() {
        return IM_SERVER_URL.concat("/room/tag/all");
    }

    public static String openRoom() {
        return IM_SERVER_URL.concat("/room/open");
    }

    public static String getRoomInfo() {
        return IM_SERVER_URL.concat("/room/get");
    }

    public static String roomStatistics() {
        return IM_SERVER_URL.concat("/basicusers/v2/record");
    }

    public static String updateRoomInfo() {
        return IM_SERVER_URL.concat("/room/update");
    }

    public static String updateByAdimin() {
        return IM_SERVER_URL.concat("/room/updateByAdmin");
    }

    public static String closeRoom() {
        return IM_SERVER_URL.concat("/room/close");
    }

    public static String getRoomConsumeList() {
        return IM_SERVER_URL.concat("/roomctrb/query");
    }

    public static String roomSearch() {
        return IM_SERVER_URL.concat("/search/room");
    }

    public static String getAuctionInfo() {
        return IM_SERVER_URL.concat("/auction/get");
    }

    public static String auctionStart() {
        return IM_SERVER_URL.concat("/auction/start");
    }

    //声网动态的appkey获取
    public static String getRoomAgoraKey() {
        return IM_SERVER_URL.concat("/agora/getKey");
    }

    /**
     * 用户参与竞拍报价
     */
    public static String auctionUp() {
        return IM_SERVER_URL.concat("/auctrival/up");
    }

    /**
     * 房主结束竞拍
     */
    public static String finishAuction() {
        return IM_SERVER_URL.concat("/auction/finish");
    }

    public static String weekAucionList() {
        return IM_SERVER_URL.concat("/weeklist/query");
    }

    public static String totalAuctionList() {
        return IM_SERVER_URL.concat("/sumlist/query");
    }

    /**
     * 获取订单列表
     */
    public static String getOrderList() {
        return IM_SERVER_URL.concat("/order/list");
    }

    /**
     * 完成订单
     */
    public static String finishOrder() {
        return IM_SERVER_URL.concat("/order/finish");
    }


    /**
     * 获取指定订单
     */
    public static String getOrder() {
        return IM_SERVER_URL.concat("/order/get");
    }

    /**
     * 获取房间列表
     */
    public static String getRoomList() {
        return IM_SERVER_URL.concat("/home/get");

    }

    /**
     * 获取房间列表
     */
    public static String getRoomListV2() {
        return IM_SERVER_URL.concat("/home/getV2");
    }

    public static String getLink() {
        return IM_SERVER_URL.concat("/room/link");
    }

    /**
     * 获取banner列表
     */
    public static String getBannerList() {
        return IM_SERVER_URL.concat("/banner/list");

    }

    /**
     * 获取点点币礼物列表
     */
    public static String getDianDianGiftList() {
        return IM_SERVER_URL.concat("/gift/listPoint");
    }

    /**
     * 获取礼物列表
     */
    public static String getGiftList() {
        return IM_SERVER_URL.concat("/gift/listV3");
    }

    /**
     * 获取神秘礼物列表
     */
    public static String getMysteryGiftList() {
        return IM_SERVER_URL.concat("/gift/listMystic");
    }

    /**
     * 送礼物新接口
     */
    public static String sendGiftV3() {
        return IM_SERVER_URL.concat("/gift/sendV3");
    }

    /**
     * 全麦送
     */
    public static String sendWholeGiftV3() {
        return IM_SERVER_URL.concat("/gift/sendWholeMicroV3");
    }

    /**
     * 送活动礼物
     */
    public static String sendGiftProp() {
        return IM_SERVER_URL.concat("/gift/sendProp");
    }

    /**
     * 送活动礼物全麦送
     */
    public static String sendWholeGiftProp() {
        return IM_SERVER_URL.concat("/gift/sendPropWholeMicro");
    }

    /**
     * 取消用户解签houbang
     */
    public static String cancelRoomMatchConfirm() {
        return IM_SERVER_URL.concat("/room/game/cancel");
    }

    /**
     * 获取表情列表
     */
    public static String getFaceList() {
        return IM_SERVER_URL.concat("/client/init");
    }

    /**
     * 客户端初始化
     *
     * @return --
     */
    public static String getInit() {
        return IM_SERVER_URL.concat("/client/init");
    }

    /**
     * 获取版本号
     */
    public static String getVersions() {
        return IM_SERVER_URL.concat("/appstore/check");
    }

    /**
     * 获取钱包信息
     */
    public static String getWalletInfos() {
        return IM_SERVER_URL.concat("/purse/query");
    }

    /**
     * 获取点点币
     */
    public static String getDianDianCoinInfos() {
        return IM_SERVER_URL.concat("/mcoin/v1/getMcoinNum");
    }

    /**
     * 获取充值产品列表
     */
    public static String getChargeList() {
        return IM_SERVER_URL.concat("/chargeprod/list");
    }

    /**
     * 汇聚支付地址
     */
    public static String getJoinPay() {
        return IM_SERVER_URL.concat("/charge/joinpay/apply");
    }

    /**
     * 汇聚支付地址
     */
    public static String checkWxInfo() {
        return IM_SERVER_URL.concat("/accounts/checkWxInfo");
    }

    /**
     * 发起充值
     */
    public static String requestCharge() {
        return IM_SERVER_URL.concat("/charge/apply");
    }

    /**
     * 支付宝汇潮渠道充值
     */
    public static String requestAliHuiChaoCharge() {
        return IM_SERVER_URL.concat("/charge/ecpss/alipay/apply");
    }

    public static String requestCDKeyCharge() {
        return IM_SERVER_URL.concat("/redeemcode/use");
    }

    /**
     * 钻石兑换
     */
    public static String changeGold() {
        return IM_SERVER_URL.concat("/change/gold");
    }

    /**
     * 获取提现列表
     */
    public static String getWithdrawList() {
        return IM_SERVER_URL.concat("/withDraw/findList");
    }

    /**
     * 绑定微信提现
     */
    public static String bindWeixinWithdraw() {
        return IM_SERVER_URL.concat("/withDraw/boundThird");
    }

    /**
     * 发起兑换
     */
    public static String requestExchange() {
        return IM_SERVER_URL.concat("/withDraw/withDrawCash");
    }


    /**
     * 获取提现页用户信息
     */
    public static String getWithdrawInfo() {
        return IM_SERVER_URL.concat("/withDraw/exchange");
    }

    /**
     * 获取绑定支付宝/微信验证码
     */
    public static String getSms() {
        return IM_SERVER_URL.concat("/withDraw/getSms");
    }

    /**
     * 检查微信提现验证码正确性
     *
     * @return
     */
    public static String checkCode() {
        return IM_SERVER_URL.concat("/withDraw/checkCode");
    }

    /**
     * 获取绑定手机验证码
     */
    public static String getSmS() {
        return IM_SERVER_URL.concat("/withDraw/phoneCode");
    }

    /**
     * 获取绑定手机验证码
     */
    public static String getModifyPhoneSMS() {
        return IM_SERVER_URL.concat("/acc/sms");
    }

    /**
     * 绑定支付宝
     */

    public static String binder() {
        return IM_SERVER_URL.concat("/withDraw/bound");
    }

    /**
     * 绑定手机
     */
    public static String binderPhone() {
        return IM_SERVER_URL.concat("/withDraw/phone");
    }

    public static String modifyBinderPhone() {
        return IM_SERVER_URL.concat("/user/confirm");
    }

    public static String modifyBinderNewPhone() {
        return IM_SERVER_URL.concat("/user/replace");
    }


    /**
     * 提交反馈
     */
    public static String commitFeedback() {
        return IM_SERVER_URL.concat("/feedback");
    }


    /**
     * 发送验证码
     */
    public static String sendSmsCode() {
        return IM_SERVER_URL.concat("/accounts/getSmsCode");
    }

    /**
     * 解绑第三方
     */
    public static String unbindingThird() {
        return IM_SERVER_URL.concat("/accounts/untiedThird");
    }

    /**
     * 验证验证码
     */
    public static String verificationCode() {
        return IM_SERVER_URL.concat("/accounts/validateCode");
    }


    /**
     * 绑定第三方
     */
    public static String bindingThird() {
        return IM_SERVER_URL.concat("/accounts/bindThird");
    }


    /**
     * 微信登陆接口
     */
    public static String requestWXLogin() {
        return IM_SERVER_URL.concat("/acc/third/login");
    }

    /**
     * 获取order平均时长
     */
    public static String getAvgChattime() {
        return IM_SERVER_URL.concat("/basicorder/avgchattime");
    }

    /**
     * 是否绑定手机
     */
    public static String isPhones() {
        return IM_SERVER_URL.concat("/user/isBindPhone");
    }

    /**
     * 检测是否绑定过手机
     */
    public static String checkPwd() {
        return IM_SERVER_URL.concat("/user/checkPwd");
    }

    /**
     * 检测是否设置过密码
     */
    public static String checkSetPwd() {
        return IM_SERVER_URL.concat("/user/checkPwd");
    }

    /**
     * 修改登录密码
     */
    public static String modifyPwd() {
        return IM_SERVER_URL.concat("/user/modifyPwd");
    }

    /**
     * 设置登录密码
     */
    public static String setPassWord() {
        return IM_SERVER_URL.concat("/user/setPwd");
    }

    /**
     * 验证码
     */
    public static String checkSmsCode() {
        return IM_SERVER_URL.concat("/user/validateCode");
    }

    /**
     * 获取个人支出账单，包含礼物 充值 订单支出
     */
    public static String getAllBills() {
        return IM_SERVER_URL.concat("/personbill/list");
    }

    /**
     * 礼物，密聊，充值，提现账单查询（不包括红包）
     */
    public static String getBillRecord() {
        return IM_SERVER_URL.concat("/billrecord/get");
    }

    /**
     * 红包账单查询
     */
    public static String getPacketRecord() {
        return IM_SERVER_URL.concat("/packetrecord/get");
    }

    public static String getMyselfRankingValue() {
        return IM_SERVER_URL.concat("/allrank/getMeH5Rank");
    }

    public static String getTeenagerModelInfo() {
        return IM_SERVER_URL.concat("/users/teens/mode/getUsersTeensMode");
    }

    /**
     * 账单提现查询(红包提现)
     */
    public static String getPacketRecordDeposit() {
        return IM_SERVER_URL.concat("/packetrecord/deposit");
    }

    public static String getRedPacket() {
        return IM_SERVER_URL.concat("/statpacket/get");
    }


    public static String getShareRedPacket() {
        return IM_SERVER_URL.concat("/usershare/save");
    }

    /**
     * 是否第一次进入，获取红包弹窗
     */
    public static String getRedBagDialog() {
        return IM_SERVER_URL.concat("/packet/first");
    }

    /**
     * 获取红包弹窗活动类型
     */
    public static String getRedBagDialogType() {
        return IM_SERVER_URL.concat("/activity/query");
    }

    /**
     * 获取红包提现列表
     */
    public static String getRedBagList() {
        return IM_SERVER_URL.concat("/redpacket/list");
    }

    /**
     * 发起红包提现
     */
    public static String getRedWithdraw() {
        return IM_SERVER_URL.concat("/redpacket/withdraw");
    }

    /**
     * 发起红包提现v2
     */
    public static String getRedWithdrawSec() {
        return IM_SERVER_URL.concat("/redpacket/v2/withdraw");
    }

    /**
     * 红包提现列表
     */
    public static String getRedDrawList() {
        return IM_SERVER_URL.concat("/redpacket/drawlist");
    }

    /**
     * 首页排行列表
     */
    public static String getHomeRanking() {
        return IM_SERVER_URL.concat("/allrank/homeV2");
    }

    /**
     * 首页首页人气厅列表模块
     */
    public static String getHomePopularRoom() {
        return IM_SERVER_URL.concat("/home/v2/getindex");
    }

    /**
     * 获取首页tab数据
     */
    public static String getMainTabList() {
        return IM_SERVER_URL.concat("/room/tag/top");
    }

    /**
     * 获取首页热门数据
     */
    public static String getMainHotData() {
        return IM_SERVER_URL.concat("/home/v2/hotindex");
    }

    /**
     * 通过tag获取首页数据
     */
    public static String getMainDataByTab() {
        return IM_SERVER_URL.concat("/home/v2/tagindex");
    }

    /**
     * 首页头部广告
     */
    public static String getHomeHeadBanner() {
        return JAVA_WEB_URL.concat("/home/getIndexTopBanner");
    }


    public static String getMainDataByMenu() {
        return IM_SERVER_URL.concat("/room/tag/v2/classification");
    }

    public static String getLotteryActivityPage() {
        return IM_SERVER_URL.concat("/mm/luckdraw/index.html");
    }

    public static String getConfigUrl() {
        return IM_SERVER_URL.concat("/client/configure");
    }

    /**
     * 排行榜
     */
    public static String getRankingList() {
        return IM_SERVER_URL.concat("/allrank/geth5");
    }

    /**
     * 举报接口
     */
    public static String reportUserUrl() {
        return IM_SERVER_URL.concat("/user/report/save");
    }

    /**
     * 举报头像
     */
    public static String reportAvatar() {
        return IM_SERVER_URL.concat("/user/report/avatar");
    }

    /**
     * 举报相册
     */
    public static String reportAlbum() {
        return IM_SERVER_URL.concat("/user/report/album");
    }

    /**
     * 敏感词
     */
    public static String getSensitiveWord() {
        return IM_SERVER_URL.concat("/sensitiveWord/regex");
    }

    /**
     * 禁言
     */
    public static String getBannedType() {
        return IM_SERVER_URL.concat("/banned/checkBanned");
    }

    /**
     * 获取捡海螺排行榜列表
     */
    public static String getPoundEggRank() {
        return IM_SERVER_URL.concat("/user/giftPurse/getRank");
    }

    /**
     * 获取捡海螺中奖记录
     */
    public static String getPoundEggPrizePool() {
        return IM_SERVER_URL.concat("/user/giftPurse/getPrizePoolGift");
    }

    /**
     * 获取捡海螺中奖记录
     */
    public static String getPoundEggRewordRecord() {
        return IM_SERVER_URL.concat("/user/giftPurse/record");
    }

    //发现 -- 活动列表
    public static String getFindInfo() {
        return JAVA_WEB_URL.concat("/advertise/getList");
    }

    /**
     * 锁麦，开麦操作
     */
    public static String operateMicroPhone() {
        return IM_SERVER_URL.concat("/room/mic/lockmic");
    }

    /**
     * 锁坑，开坑操作
     */
    public static String getlockMicroPhone() {
        return IM_SERVER_URL.concat("/room/mic/lockpos");
    }

    /**
     * 任务列表
     */
    public static String getTaskList() {
        return IM_SERVER_URL.concat("/duty/list");
    }

    /**
     * 领取任务奖励
     */
    public static String getTaskReward() {
        return IM_SERVER_URL.concat("/duty/achieve");
    }

    public static String reportPublic() {
        return IM_SERVER_URL.concat("/duty/fresh/public");
    }

    /**
     * 获取房间速配活动状态
     */
    public static String getRoomMatch() {
        return IM_SERVER_URL.concat("/room/game/getState");
    }

    /**
     * 提交房间速配活动确认选择结果
     */
    public static String postRoomMatchChoose() {
        return IM_SERVER_URL.concat("/room/game/choose");
    }

    /**
     * 提交房间速配活动显示的结果
     */
    public static String postRoomMatchConfirm() {
        return IM_SERVER_URL.concat("/room/game/confirm");
    }

    /**
     * 新手推荐弹框
     */
    public static String getNewUserRecommend() {
        return IM_SERVER_URL.concat("/room/rcmd/get");
    }

    /***
     * 萌新列表
     * @return
     */
    public static String getMengXin() {
        return IM_SERVER_URL.concat("/user/newUserList");
    }


    /**
     * PK活动接口模块
     */
    //发起保存一个PK
    public static String savePk() {
        return JAVA_WEB_URL.concat("/room/pkvote/save");
    }

    //取消一个PK
    public static String cancelPk() {
        return JAVA_WEB_URL.concat("/room/pkvote/cancel");
    }

    //获取一个PK结果
    public static String getPkResult() {
        return JAVA_WEB_URL.concat("/room/pkvote/get");
    }

    //获取PK历史
    public static String getPkHistoryList() {
        return JAVA_WEB_URL.concat("/room/pkvote/list");
    }

    //取消一个PK
    public static String sendPkVote() {
        return JAVA_WEB_URL.concat("/room/pkvote/vote");
    }

    // ---------------------------------------------------房间相关接口 ---------------------------------------

    //房间背景图片列表
    public static String getRoomBackList() {
        return JAVA_WEB_URL.concat("/room/bg/list");
    }


    // --------------------------------------------------- 消息相关接口 --------------------------------------

    //获取是否发送关注消息的接口
    public static String getFocusMsgSwitch() {
        return JAVA_WEB_URL.concat("/user/setting/v1/get");
    }

    //保存是否关注消息的状态接口
    public static String saveFocusMsgSwitch() {
        return JAVA_WEB_URL.concat("/user/setting/v1/save");
    }


    //----------------------------------------------------- 装扮商城相关接口 -----------------------------------
    //头饰接口
    public static String getHeadWearList() {
        return JAVA_WEB_URL.concat("/headwear/listMall");
    }

    public static String headWearList() {
        return JAVA_WEB_URL.concat("/headwear/list");
    }

    //我的头饰接口
    public static String getMyHeadWearList() {
        return JAVA_WEB_URL.concat("/headwear/user/list");
    }

    //购买头饰接口
    public static String purseHeadWear() {
        return JAVA_WEB_URL.concat("/headwear/purse");
    }

    //改变头饰使用状态
    public static String changeHeadWearState() {
        return JAVA_WEB_URL.concat("/headwear/use");
    }

    /**
     * 赠送座驾
     */
    public static String giftCarGive() {
        return IM_SERVER_URL.concat("/giftCar/give");
    }

    public static String giftHeadWearGive() {
        return IM_SERVER_URL.concat("/headwear/give");
    }

    //座驾接口
    public static String getCarList() {
        return JAVA_WEB_URL.concat("/giftCar/listMall");
    }

    public static String carList() {
        return JAVA_WEB_URL.concat("/giftCar/list");
    }

    //我的座驾接口
    public static String getMyCarList() {
        return JAVA_WEB_URL.concat("/giftCar/user/list");
    }

    //购买座驾
    public static String purseCar() {
        return JAVA_WEB_URL.concat("/giftCar/purse");
    }

    //改变座驾使用状态
    public static String changeCarState() {
        return JAVA_WEB_URL.concat("/giftCar/use");
    }


    //-------------------------------------------------- 客户端配置相关接口 ---------------------------------
    //获取审核模式状态
    public static String getClientChannel() {
        return JAVA_WEB_URL.concat("/client/channel");
    }

    //上报安全检测的结果
    public static String reportSafetyCheckResult() {
        return JAVA_WEB_URL.concat("/client/security/saveInfo");
    }

    public static String fetchRoomMembers() {
        return JAVA_WEB_URL.concat("/imroom/v1/fetchRoomMembers");
    }

    //-------------------------------------------------- 房间内功能接口 ---------------------------------

    public static String markBlackList() {
        return JAVA_WEB_URL.concat("/imroom/v1/markChatRoomBlackList");
    }

    public static String kickMember() {
        return JAVA_WEB_URL.concat("/imroom/v1/kickMember");
    }


    public static String markChatRoomManager() {
        return JAVA_WEB_URL.concat("/imroom/v1/markChatRoomManager");
    }

    //查询房间管理员
    public static String fetchRoomManagers() {
        return JAVA_WEB_URL.concat("/imroom/v1/fetchRoomManagers");
    }

    //查询房间黑名单列表
    public static String fetchRoomBlackList() {
        return JAVA_WEB_URL.concat("/imroom/v1/fetchRoomBlackList");
    }

    /**
     * 上报当前房间的hls地址
     */
    public static String reportHls() {
        return JAVA_WEB_URL.concat("/imroom/v1/reporthls");
    }

    /**
     * 检测用户
     */
    public static String checkPushAuth() {
        return JAVA_WEB_URL.concat("/imroom/v1/checkPushAuth");
    }

    public static String publicTitle() {
        return JAVA_WEB_URL.concat("/imroom/v1/publicTitle");
    }

    /*------------------------------- 家族相关接口 -----------------------------------------*/

    /**
     * 申请退出
     */
    public static String applyExitTeam() {
        return JAVA_WEB_URL.concat("/family/applyExitTeam");
    }

    /**
     * 处理申请消息
     */
    public static String applyFamily() {
        return JAVA_WEB_URL.concat("/family/applyFamily");
    }

    /**
     * 申请加入家族
     */
    public static String applyJoinFamilyTeam() {
        return JAVA_WEB_URL.concat("/family/applyJoinFamilyTeam");
    }

    /**
     * 获取根据uid检测是否有加入家族
     */
    public static String checkFamilyJoin() {
        return JAVA_WEB_URL.concat("/family/checkFamilyJoin");
    }

    /**
     * 创建家族
     */
    public static String createFamilyTeam() {
        return JAVA_WEB_URL.concat("/family/createFamilyTeam");
    }

    /**
     * 编辑家族信息
     */
    public static String editFamilyTeam() {
        return JAVA_WEB_URL.concat("/family/editFamilyTeam");
    }

    /**
     * 根据家族Id获取家族信息
     */
    public static String getFamilyInfo() {
        return JAVA_WEB_URL.concat("/family/getFamilyInfo");
    }

    /**
     * 获取家族消息
     */
    public static String getFamilyMessage() {
        return JAVA_WEB_URL.concat("/family/getFamilyMessage");
    }

    /**
     * 获取加入家族成员信息
     */
    public static String getFamilyTeamJoin() {
        return JAVA_WEB_URL.concat("/family/getFamilyTeamJoin");
    }

    /**
     * 获取根据uid获取家族信息
     */
    public static String getJoinFamilyInfo() {
        return JAVA_WEB_URL.concat("/family/getJoinFamilyInfo");
    }

    /**
     * 获取家族列表
     */
    public static String getList() {
        return JAVA_WEB_URL.concat("/family/getList");
    }

    /**
     * 邀请他人权限
     */
    public static String invitationPermission() {
        return JAVA_WEB_URL.concat("/family/invitationPermission");
    }

    /**
     * 踢出家族
     */
    public static String kickOutTeam() {
        return JAVA_WEB_URL.concat("/family/kickOutTeam");
    }

    /**
     * 移除管理员
     */
    public static String removeAdmin() {
        return JAVA_WEB_URL.concat("/family/removeAdmin");
    }

    /**
     * 设置申请加入方式
     */
    public static String setApplyJoinMethod() {
        return JAVA_WEB_URL.concat("/family/setApplyJoinMethod");
    }

    /**
     * 设置禁言及解禁
     */
    public static String setBanned() {
        return JAVA_WEB_URL.concat("/family/setBanned");
    }

    /**
     * 设置消息提醒
     */
    public static String setMsgNotify() {
        return JAVA_WEB_URL.concat("/family/setMsgNotify");
    }

    /**
     * 设置管理员
     */
    public static String setupAdministrator() {
        return JAVA_WEB_URL.concat("/family/setupAdministrator");
    }

    /*------------------------------- 家族相关接口 -----------------------------------------*/


    //-------------------------------------------------- 房间关注相关接口 ---------------------------------

    //房间关注
    public static String roomAttention() {
        return JAVA_WEB_URL.concat("/room/attention/attentions");
    }

    //删除关注
    public static String deleteAttention() {
        return JAVA_WEB_URL.concat("/room/attention/delAttentions");
    }


    //检查是否关注
    public static String checkAttention() {
        return JAVA_WEB_URL.concat("/room/attention/checkAttentions");
    }

    //根据uid获取关注房间列表
    public static String getRoomAttentionById() {
        return JAVA_WEB_URL.concat("/room/attention/getRoomAttentionByUid");
    }

    //关注页面的推荐房间列表
    public static String getRecommendUsers() {
        return JAVA_WEB_URL.concat("/user/getRecommendUsers");
    }

    //获取房间内排行榜榜首头像
    public static String getRoomRankFirstAvatar() {
        return JAVA_WEB_URL.concat("/roomctrb/v1/listRoomCtrbTop");
    }

    //-------------------------------------------------- 海角星球新增接口 ---------------------------------

    /*获取大厅聊天数据*/
    public static String getLobbyChatInfo() {
        return JAVA_WEB_URL.concat("/room/getLobbyChatInfo");
    }

    /**
     * 获取所有活动
     */
    public static String findActivities() {
        return JAVA_WEB_URL.concat("/activity/queryAll");
    }

    /*-------------------------------------------- 获取声音匹配接口 --------------------------------------------------*/

    /**
     * 获取随机用户列表
     */
    public static String randomUser() {
        return JAVA_WEB_URL.concat("/user/soundMatch/randomUser");
    }

    /**
     * 获取魅力用户列表
     */
    public static String charmUser() {
        return JAVA_WEB_URL.concat("/user/soundMatch/charmUser");
    }

    /**
     * 喜欢某用户
     */
    public static String likeUser() {
        return JAVA_WEB_URL.concat("/user/soundMatch/likeUser");
    }

    public static String getIndexHomeIcons() {
        return JAVA_WEB_URL.concat("/home/getIndexHomeIcon");
    }

    /*-------------------------------------------- H5接口 --------------------------------------------------*/

    /**
     * 获取推荐位
     */
    public static String getRecommendLocation() {
        return JAVA_WEB_URL.concat("/front/hotroom/index.html");
    }

    /*-------------------------------------------- H5接口 --------------------------------------------------*/
}
