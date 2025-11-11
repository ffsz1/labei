package com.erban.main.service.wx;

import com.alibaba.fastjson.JSONObject;
import com.erban.main.config.WxConfig;
import com.erban.main.model.ChargeProd;
import com.erban.main.model.ChargeRecord;
import com.erban.main.model.WxRecord;
import com.erban.main.model.WxUsers;
import com.erban.main.mybatismapper.ChargeRecordMapper;
import com.erban.main.mybatismapper.WxRecordMapper;
import com.erban.main.mybatismapper.WxUsersMapper;
import com.erban.main.service.ChargeProdService;
import com.erban.main.service.ChargeRecordUpdateService;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.util.AccessTokenUtil;
import com.erban.main.util.HttpUtil;
import com.erban.main.util.OpenIdUtil;
import com.erban.main.vo.UnifiedOrderReturnVo;
import com.erban.main.vo.WxConfigVo;
import com.erban.main.vo.WxToken;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.EncryptUtils;
import com.xchat.common.wx.*;
import com.xchat.common.wx.result.WxAccessTokenRet;
import com.xchat.common.wx.result.WxTicketRet;
import com.xchat.oauth2.service.core.exception.BusinessException;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WxService extends CacheBaseService<WxUsers, WxUsers> {

//        private String appid = "wx009d793f92c24eec";
//        private String secret = "d99ac5ed29071943d1654a40bb0adb68";
//        private String mch_id = "1484701192";//@1484701192/143409";
    //支付通知回调接口
//        private String returnUrl = "http://beta.daxiaomao.com/wx/payCallback";
/*
    private String appid = ServerPropAdapter.getValByCode("weixin_appid", "wxda36314651fcba6f");
    private String appkey = ServerPropAdapter.getValByCode("weixin_appkey", "32e46497b47f9bc87952f13d86996ac8");
*/

    private static final String wx_access_token_url_base = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private static final String wx_jsapi_ticket_url_base = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
    private static final int early_expire = 200;//比微信返回的失效时间提前的时间,单位s
    private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    private static final List<String> jsApi = Arrays.asList("onMenuShareTimeline", "onMenuShareAppMessage", "onMenuShareQQ", "onMenuShareWeibo");

    private ChargeProd chargeProd;

    @Autowired
    private JedisService jedisService;
    @Autowired
    private ChargeProdService chargeProdService;
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    @Autowired
    ChargeRecordUpdateService chargeRecordUpdateService;
    @Autowired
    WxUsersMapper wxUsersMapper;
    @Autowired
    WxRecordMapper wxRecordMapper;
    @Autowired
    WxRecordService wxRecordService;

    @Override
    public WxUsers getOneByJedisId(String jedisId) {
        return getOne(RedisKey.wx_users.getKey(), jedisId, "select * from wx_users where wx_openId = ? ", jedisId);
    }

    @Override
    public WxUsers entityToCache(WxUsers entity) {
        return entity;
    }

    /**
     * 微信公众号支付校验
     *
     * @param chargeRecord 用户订单
     * @return
     */
        /*
                <xml>
                <appid>wx2421b1c4370ec43b</appid>
                <mch_id>10000100</mch_id>
                <nonce_str>ec2316275641faa3aacf3cc599e8730f</nonce_str>
                <out_trade_no>1008450740201411110005820873</out_trade_no>
                <sign>FDD167FAA73459FD921B144BAF4F4CA2</sign>
                </xml>
         */
        /*
                <xml>
                <return_code><![CDATA[SUCCESS]]></return_code>
                <return_msg><![CDATA[OK]]></return_msg>
                <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
                <mch_id><![CDATA[10000100]]></mch_id>
                <device_info><![CDATA[1000]]></device_info>
                <nonce_str><![CDATA[TN55wO9Pba5yENl8]]></nonce_str>
                <sign><![CDATA[BDF0099C15FF7BC6B1585FBB110AB635]]></sign>
                <result_code><![CDATA[SUCCESS]]></result_code>
                <openid><![CDATA[oUpF8uN95-Ptaags6E_roPHg7AG0]]></openid>
                <is_subscribe><![CDATA[Y]]></is_subscribe>
                <trade_type><![CDATA[MICROPAY]]></trade_type>
                <bank_type><![CDATA[CCB_DEBIT]]></bank_type>
                <total_fee>1</total_fee>
                <fee_type><![CDATA[CNY]]></fee_type>
                <transaction_id><![CDATA[1008450740201411110005820873]]></transaction_id>
                <out_trade_no><![CDATA[1415757673]]></out_trade_no>
                <attach><![CDATA[订单额外描述]]></attach>
                <time_end><![CDATA[20141111170043]]></time_end>
                <trade_state><![CDATA[SUCCESS]]></trade_state>
                </xml>
         */
    public boolean getRecharge(ChargeRecord chargeRecord) throws Exception {


        //订单号
        String outTradeNo = chargeRecord.getChargeRecordId();
        String nonce_str = PayUtil.getRandomStringByLength(16);//生成随机数，可直接用系统提供的方法
        HashMap map = new HashMap();
        map.put("appid", WxConfig.appId);
        map.put("mch_id", WxConfig.mchId);
        map.put("nonce_str", nonce_str);
        map.put("out_trade_no", outTradeNo);                 //订单ID
        String sign = PayUtil.getSign(map, WxConfig.key);
        map.put("sign", sign);
        String content = XMLParser.getXMLFromMap(map);
        logger.info("========  查询微信公众号订单：=======");
        logger.info(content);
        //查询微信公众号订单
        String postResult = HttpUtils.sendPost(ORDER_QUERY_URL, content);
        logger.info("=========查询微信公众号订单返回结果：=======");
        logger.info(postResult);
        //=============解析返回的结果=======================//
        Map cbMap;
        cbMap = XMLParser.getMapFromXML(postResult);
        //下单成功
        if (cbMap.get("return_code").equals("SUCCESS") &&
                cbMap.get("result_code").equals("SUCCESS") &&
                cbMap.get("trade_state").equals("SUCCESS")) {
            BusiResult busiResult = chargeRecordUpdateService.updatePurse(cbMap);
            if (busiResult.getMessage().equals("SUCCESS")) {
                return true;
            }
        }
        return false;


    }

    /**
     * 刷新access_token
     */
    public String refreshToken() throws Exception {
        String url = wx_access_token_url_base + "&appid=" + WxConfig.appId + "&secret=" + WxConfig.appSecret;
        String result = com.xchat.common.utils.HttpUtils.executeGet(url);
        logger.info("refreshToken result=" + result);
        WxAccessTokenRet wxAccessTokenRet = gson.fromJson(result, WxAccessTokenRet.class);
        if (StringUtils.isBlank(wxAccessTokenRet.getAccess_token())) {
            throw new BusinessException(wxAccessTokenRet.getErrmsg());
        }
        int expires_in = wxAccessTokenRet.getExpires_in();
        jedisService.write(RedisKey.wx_token.getKey(), wxAccessTokenRet.getAccess_token(), expires_in - early_expire);
        return wxAccessTokenRet.getAccess_token();
    }

    /**
     * 刷新jsapi_ticket
     */
    public String refreshWxTicket(String token) throws Exception {
        if (StringUtils.isBlank(token)) {
            throw new BusinessException("微信refreshTicket刷新tikcet异常....");
        }
        String url = wx_jsapi_ticket_url_base + "&access_token=" + token;
        String result = com.xchat.common.utils.HttpUtils.executeGet(url);
        logger.info("weixin refreshWxTicket result=" + result);
        WxTicketRet wxTicketRet = gson.fromJson(result, WxTicketRet.class);
        if (StringUtils.isBlank(wxTicketRet.getTicket())) {
            throw new BusinessException(wxTicketRet.getErrmsg());
        }
        int expires_in = wxTicketRet.getExpires_in();
        jedisService.write(RedisKey.wx_ticket.getKey(), wxTicketRet.getTicket(), expires_in - early_expire);
        return wxTicketRet.getTicket();
    }

    /**
     * 生成签名
     *
     * @return String
     */
    public String createSignature(String url, String ticket, String noncestr, String timestamp) {
//        步骤1. 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1：
//        jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg
//                &noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value
        //注意这里参数名必须全部小写，且必须有序
        String string1 = "jsapi_ticket=" + ticket +
                "&noncestr=" + noncestr +
                "&timestamp=" + timestamp +
                "&url=" + url;
        logger.info("createSignature string=" + string1);
        return EncryptUtils.getSHA1(string1);
    }

    /**
     * 生成页面配置信息
     *
     * @return WXConfigVo
     */
    public BusiResult<WxConfigVo> getWxConfig(String url) throws Exception {
        WxConfigVo wxConfigVo = new WxConfigVo();
        wxConfigVo.setAppId(WxConfig.appId);
        wxConfigVo.setJsApiList(jsApi);
        String timestamp = createTimestamp();
        String nonceStr = createNonceStr();
        String ticket = getTicket();
        String sign = createSignature(url, ticket, nonceStr, timestamp);
        wxConfigVo.setNonceStr(nonceStr);
        wxConfigVo.setTimestamp(timestamp);
        wxConfigVo.setSignature(sign);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(wxConfigVo);
        return busiResult;
    }

    public String getTicket() throws Exception {
        String result = jedisService.read(RedisKey.wx_ticket.getKey());
        if (StringUtils.isBlank(result)) {
            result = refreshWxTicket(getToken());
        }
        return result;
    }

    public String getToken() throws Exception {
        String result = jedisService.read(RedisKey.wx_token.getKey());
        if (StringUtils.isBlank(result)) {
            result = refreshToken();
        }
        return result;
    }

    private static String createNonceStr() {
        return UUIDUitl.get();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


    public BusiResult submitPayWx(Long uid, String chargeProdId, String nick, Long erban_no, String openId) throws Exception {
        String nonce_str = PayUtil.getRandomStringByLength(16);//生成随机数，可直接用系统提供的方法
        String spbill_create_ip = Util.getIpAdd();//Util.getIpAdd ( );//用户端ip,这里随意输入的
        String trade_type = "JSAPI";
        String prepay_id;//预支付id
                /*
                        调用统一下载接口
                 */
        //查询订单类型ID
        String outTradeNo = UUIDUitl.get();
        chargeProd = chargeProdService.getChargeProdById(chargeProdId);
        Long money = chargeProd.getMoney();
        Long amount = money * 100;
        HashMap map = new HashMap();
        map.put("appid", WxConfig.appId);
        map.put("mch_id", WxConfig.mchId);
        map.put("device_info", "WEB");
        map.put("nonce_str", nonce_str);
        map.put("body", "充值 " + amount / 10 + " 金币");                                      //订单标题（描述）
        map.put("out_trade_no", outTradeNo);                 //订单ID
                /*
                        测试环境下，total_fee为money（分）
                        正式环境为amount
                 */
        //TODO 正式环境为amount(测试环境下统统一分钱)
        map.put("total_fee", String.valueOf(amount));                     //订单需要支付的金额
        map.put("spbill_create_ip", spbill_create_ip);
        map.put("trade_type", trade_type);
        //TODO 添加回调地址
        map.put("notify_url", WxConfig.returnUrl);              //notify_url 支付成功之后 微信会进行异步回调的地址
        map.put("openid", openId);                                   //trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
        String sign = PayUtil.getSign(map, WxConfig.key);
        map.put("sign", sign);
        String content = XMLParser.getXMLFromMap(map);
        logger.info("========统一接口发送内容：=======" + content);
        //调用统一下单接口
//        String postResult = HttpUtils.sendPost(UNIFIEDORDER_URL, content);

        String postResult = HttpUtil.doHttpPost(content, UNIFIEDORDER_URL);
        logger.info("=========统一下单返回结果：=======" + postResult);
        //=============解析返回的结果=======================//
        Map<String, Object> cbMap = XMLParser.getMapFromXML(postResult);
        //下单成功
        logger.info("return_code={}, result_code={}", cbMap.get("return_code"), cbMap.get("result_code"));
        if ("SUCCESS".equals(cbMap.get("return_code")) && "SUCCESS".equals(cbMap.get("result_code"))) {
            /* 将订单记录到数据库 */
            payWXPla(uid, outTradeNo, chargeProdId, spbill_create_ip, openId);
            /* 将订单记录转成Json发送给H5 */
            UnifiedOrderReturnVo returnVo = new UnifiedOrderReturnVo();
            returnVo.setAppid(WxConfig.appId);
            returnVo.setSign_type("MD5");
            //( String ) cbMap.get ( "nonce_str" ) );
            returnVo.setNonce_str(nonce_str);
            //这就是预支付id
            prepay_id = cbMap.get("prepay_id") + "";
            returnVo.setPrepay_id("prepay_id=" + prepay_id);
            returnVo.setTimestamp(String.valueOf(new Date().getTime() / 1000));//new Date().getTime ()/1000 ) );
            sign = getSign(returnVo);//参数加密
            logger.info("=======给H5的签名===" + sign);
            returnVo.setSign(sign);
            returnVo.setErban_no(String.valueOf(erban_no));
            returnVo.setNick(nick);
            returnVo.setMch_id(WxConfig.mchId);

            BusiResult<UnifiedOrderReturnVo> busiResult = new BusiResult(BusiStatus.SUCCESS);
            busiResult.setData(returnVo);
            return busiResult;

        } else {

            logger.error("=======uid：" + uid + "==========微信公众号下单失败=================");
            return new BusiResult(BusiStatus.BUSIERROR);

        }


    }

    /**
     * 将订单写入数据库
     *
     * @param uid              用户ID
     * @param outTradeNo       微信订单号
     * @param chargeProdId     该订单存入的金额
     * @param spbill_create_ip 用户IP
     */
    private void payWXPla(Long uid, String outTradeNo, String chargeProdId, String spbill_create_ip, String openId) {

        //1.创建订单号
        String chargeRecordId = outTradeNo;
        com.erban.main.model.ChargeRecord chargeRecord = new com.erban.main.model.ChargeRecord();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(uid);
        //微信公众号支付
        chargeRecord.setChannel(Constant.ChargeChannel.wx_pub);
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.create);
        Long money = chargeProd.getMoney();
        Long amount = money * 100;
        chargeRecord.setAmount(amount);
        String body = chargeProd.getProdName() + "金币充值";
        String subject = chargeProd.getProdName() + "金币充值";
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        chargeRecord.setClientIp(spbill_create_ip);
        chargeRecord.setWxPubOpenid(openId);

        logger.info(" ======uid=" + uid + "chargeProdId=" + chargeProdId + "订单写入数据库操作========");
        logger.info(chargeRecord.toString());
        //写入数据库
        insertChargeRecord(chargeRecord);
    }


    private void insertChargeRecord(com.erban.main.model.ChargeRecord chargeRecord) {
        chargeRecord.setCreateTime(new Date());
        chargeRecordMapper.insertSelective(chargeRecord);
    }

    private String getSign(UnifiedOrderReturnVo returnVo) {

        Map map = new HashMap();
        map.put("appId", returnVo.getAppid());
        map.put("nonceStr", returnVo.getNonce_str());
        map.put("package", returnVo.getPrepay_id());
        map.put("timeStamp", returnVo.getTimestamp());
        map.put("signType", returnVo.getSign_type());
        return getSignByMap(map);
    }


    private String getSignByMap(Map<String, String> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != "" && !entry.toString().equals("return_code") && !entry.toString().equals("return_msg") && !entry.toString().equals("result_code")) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }

        String result = sb.toString();
        //key 该方法key的需要根据你当前公众号的key进行修改
        result += "key=" + WxConfig.key;
        String md5 = MD5.MD5Encode(result).toUpperCase();
        logger.info("[ 微信支付 ] 待加密字符串:>{}, 签名:>{}", result, md5);
        return md5;
    }

        /*
                当收到通知进行处理时，
                首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，
                如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，
                以避免函数重入造成的数据混乱。
                特别提醒：商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，
                防止数据泄漏导致出现“假通知”，造成资金损失。
                 */
    //【※返回结果处理※】

    /**
     * @param map 回调数据
     * @return 返回结果
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult getPayResult(Map<String, String> map) throws Exception {
        //支付成功
        //3.判断该通知是否已经处理过
        //4.订单金额校验
        //5.数据库写入
        return chargeRecordUpdateService.updatePurse(map);


    }

    /*public String getOpenId ( String code ) {

     *//*
                        获取openID(√)
                        【测试时屏蔽】
                *//*
                logger.info ( "==============服务器未获取给用户的openid==============" );
                HashMap <String, String> openIdMap = new HashMap <String, String> (  );
                openIdMap.put ( "appid", appid );
                openIdMap.put ( "secret", secret );
                openIdMap.put ( "code", code );
                openIdMap.put ( "grant_type", "authorization_code" );
                //==================解析openIdInfo json字符串=========================//
                String openIdInfo = HttpUtils.URLGet (
                        "https://api.weixin.qq.com/sns/oauth2/access_token", openIdMap, HttpUtils.URL_PARAM_DECODECHARSET_UTF8 );
                logger.info ( "===========获取openIdInfo===========" + openIdInfo );
                Gson gson = new Gson ( );
                OpenIdInfoVo openIdInfoVo = gson.fromJson ( openIdInfo, OpenIdInfoVo.class );
                String openId = openIdInfoVo.getOpenid ( );
                if ( openId == null ) {
                        logger.error ( "==========请求openId失败=================" );
                        return null;
                }
                return openId;

        }*/


    public static void main(String[] args) {

        HashMap<String, String> openIdMap = new HashMap<>();
        openIdMap.put("appid", "wx009d793f92c24eec");
        openIdMap.put("secret", "d99ac5ed29071943d1654a40bb0adb68");
        openIdMap.put("code", "011HBVjj2aLOzI0mqMkj2F23kj2HBVjN");
        openIdMap.put("grant_type", "authorization_code");
        String openIdInfo = HttpUtils.URLGet(
                "https://api.weixin.qq.com/sns/oauth2/access_token", openIdMap, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
        System.out.println(openIdInfo);

    }

    public String getAccessToken() {
        String accessToken = jedisService.get(RedisKey.wx_access_oken.getKey() + "1");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = AccessTokenUtil.oauth2GetAccessToken("1");
            jedisService.write(RedisKey.wx_access_oken.getKey() + "1", accessToken, 7200);
        }
        return accessToken;
    }

    public BusiResult getToken(Integer appid, String appKey, String idname, Long expiredAdd) {
        String nonce = UUIDUitl.get();
        Long expired = new Date().getTime() + expiredAdd;
        appKey = appKey.replaceAll("0x", "").replaceAll(",", "");
        if (appKey.length() < 32) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        String appKey32 = appKey.substring(0, 32);
        String source = appid + appKey32 + idname + nonce + expired;
        String sum = MD5.MD5Encode(source);
        WxToken wxToken = new WxToken();
        wxToken.setVer(1);
        wxToken.setHash(sum);
        wxToken.setNonce(nonce);
        wxToken.setExpired(expired);
        byte[] bytes = gson.toJson(wxToken).getBytes();
        String token = Base64.getEncoder().encodeToString(bytes);
        return new BusiResult(BusiStatus.SUCCESS, token);
    }

    public BusiResult getUser(String rawData, String code) {
        JSONObject jsonObject = OpenIdUtil.oauth2GetOpenid(code, "1");
        if (jsonObject == null || jsonObject.get("errcode") != null) {
            return new BusiResult(BusiStatus.NOTEXISTS, "获取openId失败" + jsonObject.get("errmsg"), "");
        }
        String sessionKey = String.valueOf(jsonObject.get("session_key"));
        String openId = String.valueOf(jsonObject.get("openid"));
        Map<String, Object> type = new HashMap<>();
        Map<String, Object> map = gson.fromJson(rawData, type.getClass());
        WxUsers wxUsers = getOneByJedisId(openId);
        Object gender = map.get("gender");
        Object nickName = map.get("nickName");
        Object avatarUrl = map.get("avatarUrl");
        if (wxUsers == null) {
            wxUsers = new WxUsers();
            wxUsers.setWxOpenid(openId);
            wxUsers.setGender(gender == null ? new Byte("0") : new Double(gender.toString()).byteValue());
            wxUsers.setNick(nickName == null ? "" : nickName.toString());
            wxUsers.setAvatar(avatarUrl == null ? "" : avatarUrl.toString());
            wxUsers.setCreateTime(new Date());
            wxUsers.setUpdateTime(new Date());
            wxUsersMapper.insertSelective(wxUsers);
        } else {
            wxUsers.setGender(gender == null ? new Byte("0") : new Double(gender.toString()).byteValue());
            wxUsers.setNick(nickName == null ? "" : nickName.toString());
            wxUsers.setAvatar(avatarUrl == null ? "" : avatarUrl.toString());
            wxUsers.setUpdateTime(new Date());
            wxUsersMapper.updateByPrimaryKeySelective(wxUsers);
        }
        saveOneCache(wxUsers, RedisKey.wx_users.getKey(), openId);
//        String accessToken = getAccessToken();
//        if(StringUtils.isBlank(accessToken)){
//            return new BusiResult(BusiStatus.NOTEXISTS, "获取accessToken失败", "");
//        }
//        JSONObject object = OpenIdUtil.getUserInfo(encryptedData, sessionKey, iv);
//        //URL
//        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info";
//        //请求参数
//        String params = "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
//        //发送请求
//        String data = HttpUtil.get(requestUrl, params);
//        Map<String, Object> map = new HashMap<>();
//        //解析相应内容（转换成json对象
//        JSONObject json = new JSONObject(gson.fromJson(data, map.getClass()));
        return new BusiResult(BusiStatus.SUCCESS, wxUsers);
    }

    public BusiResult getUserByOpenId(String openId) {
        WxUsers wxUsers = getOneByJedisId(openId);
        if (wxUsers == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        return new BusiResult(BusiStatus.SUCCESS, wxUsers);
    }

    public BusiResult saveRecord(String anchorId, String openId) {
        WxUsers wxUsers = getOneByJedisId(openId);
        if (wxUsers == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        WxUsers anchor = getOneByJedisId(anchorId);
        if (anchor == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        String str = wxRecordService.getStrList(wxUsers.getWxUid().toString());
        WxRecord wxRecord = new WxRecord();
        wxRecord.setUid(wxUsers.getWxUid());
        wxRecord.setAnchor(anchor.getWxUid());
        wxRecord.setCreateTime(new Date());
        wxRecordMapper.insertSelective(wxRecord);
        if (StringUtils.isEmpty(str)) {
            str = "" + wxRecord.getRecordId();
        } else {
            str = wxRecord.getRecordId() + "," + str;
        }
        jedisService.hwrite(RedisKey.wx_record_list.getKey(), wxUsers.getWxUid().toString(), str);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult getRecord(String openId) {
        WxUsers wxUsers = getOneByJedisId(openId);
        if (wxUsers == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        String str = wxRecordService.getStrList(wxUsers.getWxUid().toString());
        return new BusiResult(BusiStatus.SUCCESS, str == null ? new ArrayList<>() : wxRecordService.getList(str));
    }

}
