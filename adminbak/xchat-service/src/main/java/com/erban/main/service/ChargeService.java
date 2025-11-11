package com.erban.main.service;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.ChargeRecordMapper;
import com.erban.main.mybatismapper.GiftCarGetRecordMapper;
import com.erban.main.mybatismapper.GiftCarPurseRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.drawprize.UserDrawService;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.DutyType;
import com.erban.main.service.gift.GiftCarPurseService;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.noble.NoblePayService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UserShareRecordService;
import com.erban.main.service.user.UsersService;
import com.google.common.collect.Maps;
import com.pingplusplus.model.Charge;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChargeService extends BaseService {
    @Autowired
    private ChargeProdService chargeProdService;
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    @Autowired
    private PingxxService pingxxService;
    @Autowired
    private NoblePayService noblePayService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private UserDrawService userDrawService;
    @Autowired
    private UserShareRecordService userShareRecordService;
    @Autowired
    private GiftCarService giftCarService;
    @Autowired
    private GiftCarPurseService giftCarPurseService;
    @Autowired
    private GiftCarGetRecordMapper giftCarGetRecordMapper;
    @Autowired
    private GiftCarPurseRecordMapper giftCarPurseRecordMapper;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private DutyService dutyService;
    // 你生成的私钥路径
    private final static String privateKeyFilePath = "res/your_rsa_private_key_pkcs8.pem";

    /**
     * 发起充值
     *
     * @param uid
     * @param chargeProdId
     * @param channel
     * @param clientIp
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult applyCharge(Long uid, String chargeProdId, String channel, String clientIp, String successUrl) throws Exception {
        if (StringUtils.isEmpty(clientIp)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        ChargeProd chargeProd = chargeProdService.getChargeProdById(chargeProdId);
        if (chargeProd == null) {
            logger.error("发起充值支付接口（/apply/charge），充值产品数据库中不存在，chargeProdId：{}", chargeProdId);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        // 支持的充值方式，支付宝app充值和微信公众号充值
        if (!(Constant.ChargeChannel.alipay.equals(channel)
                || Constant.ChargeChannel.alipay_wap.equals(channel)
                || Constant.ChargeChannel.wx_pub.equals(channel)
                || Constant.ChargeChannel.ios_pay.equals(channel) || Constant.ChargeChannel.wx.equals(channel) || Constant.ChargeChannel.wx_wap.equals(channel))) {
            logger.error("发起充值支付接口（/apply/charge）,不存在的支付渠道：{}", channel);
            return new BusiResult(BusiStatus.CHARGE_NOCHANNEL);
        }

        // 保存充值记录
        String chargeRecordId = UUIDUitl.get();
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(channel);
        chargeRecord.setBussType(Constant.PayBussType.charge);
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.create);
        Long money = chargeProd.getMoney();
        Long amount = money * 100;// 支付宝充值，以分为单位
        // //测试生产环境，充值1分钱
        chargeRecord.setAmount(amount);
        String body = chargeProd.getProdName() + "金币充值";
        String subject = chargeProd.getProdName() + "金币充值";
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        chargeRecord.setChargeDesc(body);//充值描述
        chargeRecord.setClientIp(clientIp);

        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        // 发起ping++充值
        Map<String, Object> chargeMap = buildChargeMap(amount, chargeRecordId, subject, body, channel, clientIp);
        chargeMap.put("extra", buildExtra(channel, successUrl));
        Charge charge = pingxxService.charge(chargeMap);
        chargeRecord.setPingxxChargeId(charge.getId());
        busiResult.setData(charge);
        insertChargeRecord(chargeRecord);
        return busiResult;
    }

    /**
     * 贵族开通或者续费支付请求
     *
     * @param uid
     * @param nobleId
     * @param money
     * @param payDesc
     * @param channel
     * @param bussType 1：开通，2：续费
     * @param clientIp
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult noblePay(Long uid, Integer nobleId, Long roomUid, Long money, String payDesc, String channel
            , Byte bussType, String clientIp, String successUrl) throws Exception {
        // 支持的充值方式，支付宝app充值和微信公众号充值
        if (!(Constant.ChargeChannel.alipay.equals(channel)
                || Constant.ChargeChannel.wx.equals(channel)
                || Constant.ChargeChannel.wx_pub.equals(channel)
                || Constant.ChargeChannel.ios_pay.equals(channel)
                || Constant.ChargeChannel.wx_wap.equals(channel)
                || Constant.ChargeChannel.alipay_wap.equals(channel))) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        String chargeRecordId = UUIDUitl.get();
        Long amount = money * 10;      // 支付宝充值，以分为单位

        // 发起ping++充值
        Map<String, Object> chargeMap = buildChargeMap(amount, chargeRecordId, payDesc, payDesc, channel, clientIp);
        chargeMap.put("extra", buildExtra(channel, successUrl));
        Charge charge = pingxxService.charge(chargeMap);
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(nobleId.toString());
        chargeRecord.setUid(uid);
        chargeRecord.setBody(payDesc);
        chargeRecord.setAmount(amount); // 测试生产环境，充值1分钱
        chargeRecord.setSubject(payDesc);
        chargeRecord.setRoomUid(roomUid);
        chargeRecord.setChannel(channel);
        chargeRecord.setBussType(bussType);
        chargeRecord.setClientIp(clientIp);
        chargeRecord.setPingxxChargeId(charge.getId());
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.create);
        // 保存充值记录
        insertChargeRecord(chargeRecord);

        return new BusiResult(BusiStatus.SUCCESS, charge);
    }

    public void insertChargeRecord(ChargeRecord chargeRecord) throws Exception {
        chargeRecord.setCreateTime(new Date());
        chargeRecordMapper.insertSelective(chargeRecord);
    }

    /**
     * charge 支付回调
     *
     * @param charge
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateChargeData(Charge charge) throws Exception {
        //1.校验charge chargeRecord
        Boolean falge = true;
        Charge chargePingxx = pingxxService.retrieve(charge.getId());
        if (chargePingxx == null) {
            logger.error("updateChargeData exception,retrieve charge not exist,charge id="
                    + charge.getId() + ",orderNo=" + charge.getOrderNo());
            falge = false;
        }
        if (!chargePingxx.getPaid() || !charge.getPaid()) {
            logger.error("updateChargeData exception,paid is false,charge id=" + charge.getId() + ",orderNo="
                    + charge.getOrderNo());
            falge = false;
        }
        ChargeRecord chargeRecord = getChargeRecordById(charge.getOrderNo());
        if (chargeRecord == null || !Constant.ChargeRecordStatus.create.equals(chargeRecord.getChargeStatus())) {
            logger.error("updateChargeData exception,db not exist chargeRecord, charge id=" + charge.getId()
                    + ",orderNo=" + charge.getOrderNo());
            falge = false;
        }
        if (!falge) {
            throw new RuntimeException("支付回调失败，原因：charge或chargeRecord校验失败");
        }
        //2.按业务类型进行后续的处理
        switch (chargeRecord.getBussType()) {
            case Constant.PayBussType.charge:    // 充值金币
                return handleChargeGoldBuss(charge, chargeRecord);
            case Constant.PayBussType.openNoble: // 开通贵族
                return handleOpenNobleBuss(chargeRecord);
            case Constant.PayBussType.renewNoble:// 续费贵族
                return handleRenewNobleBuss(chargeRecord);
        }

        logger.error("updateChargeData exception,bussType not exist, charge id=" + charge.getId()
                + ",orderNo=" + charge.getOrderNo());
        throw new RuntimeException("支付回调失败，原因：业务不存在");
    }

    /**
     * 充值金币
     *
     * @return
     */
    private int handleChargeGoldBuss(Charge charge, ChargeRecord chargeRecord) throws Exception {
        Long amountLong = new Long(charge.getAmount());
        String chargeRecordProdId = chargeRecord.getChargeProdId();
        ChargeProd chargeProd = chargeProdService.getChargeProdById(chargeRecordProdId);
        Long money = chargeProd.getMoney();
        if (!amountLong.equals(money * 100)) {// 充值金额与实际价格不一致
            logger.info("updateChargeData orderNo=" + charge.getOrderNo() + "&amountLong=" + amountLong + "&money="
                    + money + "数据异常");
            return 0;
        }
        Integer goldRate = chargeProd.getChangeGoldRate();
        // 转换后的金币数
        Long chargeGoldNum = goldRate * money;
        // 赠送金币数量(每次充值均赠送)
        Integer giftGoldNum = chargeProd.getGiftGoldNum();
        // 赠送金币与购买的金币
        chargeGoldNum = chargeGoldNum + giftGoldNum.longValue();
        UserPurse uuserPurseBefore = userPurseService.getPurseByUid(chargeRecord.getUid());
        // 判断是否首次充值（如果首次充值还需要加上首次充值赠送的金币数量）
        boolean isFirstCharge = uuserPurseBefore.getIsFirstCharge();
        Long realGoldNum = chargeGoldNum;
        if (isFirstCharge) {
            realGoldNum = realGoldNum + chargeProd.getFirstGiftGoldNum();
            Users users = usersService.getUsersByUid(chargeRecord.getUid());
            if (users != null) {
                // 首冲大礼包
                firstGiftBag(users.getUid(), chargeRecord.getAmount(), charge.getId());
            }
        }

        UserPurse userPurse = userPurseUpdateService.addChargeGoldDbAndCache2(chargeRecord.getUid(), realGoldNum);
        if (userPurse != null) {
            userPurseService.sendSysMsgByModifyGold(userPurse);
            logger.info("handleChargeGoldBuss sendSysMsgByModifyGold success, uid:{}", userPurse.getUid());
        }
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
        chargeRecord.setTotalGold(realGoldNum);
        updateChargeRecord(chargeRecord);
        billRecordService.insertBillRecord(chargeRecord.getUid(), chargeRecord.getUid(), chargeRecord.getChargeRecordId()
                , Constant.BillType.charge, null, realGoldNum, money);
        try {
            userShareRecordService.saveUserBonusRecord(chargeRecord.getUid(), chargeRecordProdId, amountLong.intValue());
        } catch (Exception e) {
            logger.error("updateChargeData  error while saveUserBonusRecord,still continue save user...uid="
                    + chargeRecord.getUid(), e);
            throw new RuntimeException("updateChargeData  error while saveUserBonusRecord,still continue save user");
        }
        try {
            userDrawService.genUserDrawChanceByCharge(chargeRecord.getUid(), amountLong, charge.getId());
        } catch (Exception e) {
            logger.error("updateChargeData  error while genUserDrawChance,still continue save user...uid="
                    + chargeRecord.getUid(), e);
            throw new RuntimeException("updateChargeData  error while genUserDrawChance,still continue save user");
        }

        try {
            dutyService.updateDailyDuty(chargeRecord.getUid(), DutyType.charge.getDutyId());
        } catch (Exception e) {
            logger.error("保存每日任务报错：uid:>{}", chargeRecord.getUid(), e);
        }
        return 200;
    }

    public void firstGiftBag(Long uid, Long amount, String id) {
        String str = jedisService.hget(RedisKey.first_charge.getKey(), uid.toString());
        if (StringUtils.isBlank(str)) {
            GiftCar giftCar = giftCarService.getOneByJedisId("1");
            if (giftCar != null) {
                String purse = giftCarPurseService.getPurse(uid);// 赠送新人专属座驾2天
                if (StringUtils.isNotBlank(purse)) {
                    if (!StringUtils.splitToList(purse, ",").contains("1")) {
                        purse += ",1";
                    }
                } else {
                    purse = "1";
                }
                jedisService.hwrite(RedisKey.gift_car_purse_list.getKey(), uid.toString(), purse);
                GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), "1");
                if (giftCarPurseRecord == null) {
                    giftCarPurseRecord = new GiftCarPurseRecord();
                    giftCarPurseRecord.setUid(uid);
                    giftCarPurseRecord.setCarId(1L);
                    giftCarPurseRecord.setTotalGoldNum(0L);
                    giftCarPurseRecord.setCarDate(3);
                    giftCarPurseRecord.setIsUse(new Byte("0"));
                    giftCarPurseRecord.setCreateTime(new Date());
                    giftCarPurseRecordMapper.insertSelective(giftCarPurseRecord);
                } else {
                    giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() + 3);
                    giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
                }
                jedisService.hset(RedisKey.gift_car_purse.getKey(), uid.toString() + "_1", gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
                GiftCarGetRecord giftCarGetRecord = new GiftCarGetRecord();
                giftCarGetRecord.setUid(uid);
                giftCarGetRecord.setCarId(1L);
                giftCarGetRecord.setCarDate(3);
                giftCarGetRecord.setType(new Byte("4"));
                giftCarGetRecord.setCreateTime(new Date());
                giftCarGetRecordMapper.insert(giftCarGetRecord);
                // 发送消息给用户
                NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                neteaseSendMsgParam.setOpe(0);
                neteaseSendMsgParam.setType(0);
                neteaseSendMsgParam.setTo(uid.toString());
                neteaseSendMsgParam.setBody("恭喜您，获得官方赠送座驾【" + giftCar.getCarName() + "】,快点坐上去游玩吧！");
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
            }
            jedisService.hset(RedisKey.first_charge.getKey(), uid.toString(), "1");
        }
    }


    /**
     * 贵族开通业务
     *
     * @return
     */
    private int handleOpenNobleBuss(ChargeRecord chargeRecord) {
        NobleRight nobleRight = noblePayService.getNobleRightById(Integer.valueOf(chargeRecord.getChargeProdId()));
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
        updateChargeRecord(chargeRecord);
        billRecordService.insertBillRecord(chargeRecord.getUid(), chargeRecord.getUid(), chargeRecord.getChargeRecordId()
                , Constant.BillType.openNoble, null, null, chargeRecord.getAmount() / 100);
        noblePayService.sendNobleMessage(chargeRecord.getUid(), chargeRecord.getRoomUid(), Constant.NobleOptType.open,
                Constant.NoblePayType.money, chargeRecord.getAmount() / 100, nobleRight);
        return 200;
    }

    /**
     * 贵族续费业务
     *
     * @return
     */
    private int handleRenewNobleBuss(ChargeRecord chargeRecord) {
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
        updateChargeRecord(chargeRecord);
        NobleRight nobleRight = noblePayService.getNobleRightById(Integer.valueOf(chargeRecord.getChargeProdId()));
        billRecordService.insertBillRecord(chargeRecord.getUid(), chargeRecord.getUid(), chargeRecord.getChargeRecordId()
                , Constant.BillType.renewNoble, null, null, chargeRecord.getAmount() / 100);
        noblePayService.sendNobleMessage(chargeRecord.getUid(), chargeRecord.getRoomUid(), Constant.NobleOptType.renew,
                Constant.NoblePayType.money, chargeRecord.getAmount() / 100, nobleRight);
        return 200;
    }

    private void updateChargeRecord(ChargeRecord chargeRecord) {
        chargeRecord.setUpdateTime(new Date());
        chargeRecordMapper.updateByPrimaryKeySelective(chargeRecord);
    }

    private ChargeRecord getChargeRecordById(String chargeRecordId) {
        ChargeRecord chargeRecord = chargeRecordMapper.selectByPrimaryKey(chargeRecordId);
        return chargeRecord;
    }

    private Map<String, Object> buildChargeMap(Long amount, String orderNo, String subject, String body, String channel,
                                               String clientIp) {

        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);// 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        chargeMap.put("currency", Constant.Currency.cny);
        chargeMap.put("subject", subject);// 商品的标题，该参数最长为 32 个 Unicode 字符，银联全渠道（
        // upacp / upacp_wap ）限制在 32 个字节。
        chargeMap.put("body", body);// 商品的描述信息，该参数最长为 128 个 Unicode
        // 字符，yeepay_wap 对于该参数长度限制为 100 个 Unicode
        // 字符。
        chargeMap.put("order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("channel", channel);// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("client_ip", clientIp); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如:
        // 127.0.0.1

        // extra 取值请查看相应方法说明

        return chargeMap;
    }

    private Map<String, Object> buildExtra(String channel, String... successUrl) {
        Map<String, Object> extra = new HashMap<>();
        switch (channel) {
            case "alipay":
                extra = buildAlipayExtra();
                break;
            case "com/xchat/common/wx":
                extra = buildWxExtra();
                break;
            case "wx":
                extra = buildWxExtra();
                break;
            case Constant.ChargeChannel.alipay_wap:
                extra = buildAlipayWapExtra(successUrl[0]);
                break;
            case Constant.ChargeChannel.wx_wap:
                extra = buildWxWapExtra(successUrl[0]);
                break;
        }
        return extra;
    }

    // extra 根据渠道会有不同的参数

    private Map<String, Object> buildAlipayWapExtra(String successUrl) {
        Map<String, Object> extra = Maps.newHashMap();
        extra.put("success_url", successUrl);
        return extra;
    }

    private Map<String, Object> buildWxWapExtra(String successUrl) {
        Map<String, Object> extra = Maps.newHashMap();
        extra.put("result_url", successUrl);
        return extra;
    }

    private Map<String, Object> buildAlipayExtra() {
        Map<String, Object> extra = new HashMap<>();

        // 可选，开放平台返回的包含账户信息的 token（授权令牌，商户在一定时间内对支付宝某些服务的访问权限）。通过授权登录后获取的
        // alipay_open_id ，作为该参数的 value ，登录授权账户即会为支付账户，32 位字符串。
        // extra.put("extern_token", "TOKEN");

        // 可选，是否发起实名校验，T 代表发起实名校验；F 代表不发起实名校验。
        extra.put("rn_check", "F");

        return extra;
    }

    private Map<String, Object> buildWxExtra() {
        Map<String, Object> extra = new HashMap<>();
        // 可选，指定支付方式，指定不能使用信用卡支付可设置为 no_credit 。
//        extra.put("limit_pay", "no_credit");
//        extra.put("rn_check", "F");
        // 可选，商品标记，代金券或立减优惠功能的参数。
        // extra.put("goods_tag", "YOUR_GOODS_TAG");

        return extra;
    }

    public boolean getRechargeByAliId(String chargeRecordId, String pingxxChargeId) throws Exception {
        Charge chargePingxx = pingxxService.retrieve(pingxxChargeId);
        if (chargePingxx == null) {
            logger.info("getRechargeByAliId chargeRecordId=" + chargeRecordId + "pingxx数据为空，回调异常");
            return false;
        }
        ChargeRecord chargeRecord = getChargeRecordById(chargeRecordId);
        if (chargeRecord == null || !chargeRecord.getChargeStatus().equals(Constant.ChargeRecordStatus.create)) {
            logger.info("getRechargeByAliId chargeRecordId=" + chargeRecordId + "数据库中不存在或者状态异常");
            return false;
        }
        boolean pingxxPaid = chargePingxx.getPaid();
        if (!pingxxPaid) {
            logger.info("getRechargeByAliId chargeRecordId=" + chargeRecordId + "&pingxxPaid=" + "数据异常");
            return false;
        }
        Integer amount = chargePingxx.getAmount();
        Long amountLong = new Long(amount);
        String chargeRecordProdId = chargeRecord.getChargeProdId();
        ChargeProd chargeProd = chargeProdService.getChargeProdById(chargeRecordProdId);
        Long money = chargeProd.getMoney();
        if (!amountLong.equals(money * 100)) {// 充值金额与实际价格不一致
            logger.info("getRechargeByAliId chargeRecordId=" + chargeRecordId + "&amountLong=" + amountLong + "&money="
                    + money + "数据异常");
            return false;
        }
        Integer goldRate = chargeProd.getChangeGoldRate();
        Long chargeGoldNum = goldRate * money;
        Integer giftGoldNum = chargeProd.getGiftGoldNum();
        chargeGoldNum = chargeGoldNum + giftGoldNum.longValue();
        userPurseService.updateGoldByCharge(chargeRecord.getUid(), chargeGoldNum, chargeProd.getFirstGiftGoldNum());
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
        updateChargeRecord(chargeRecord);
        return true;
    }

    public boolean getRechargeByForWPub(String chargeRecordId) {

        ChargeRecord chargeRecord = getChargeRecordById(chargeRecordId);
        if (chargeRecord.getChargeStatus().equals(Constant.ChargeRecordStatus.create)) {
            //处理长时间未付款的订单
            return false;
        }
        return true;

    }


}
