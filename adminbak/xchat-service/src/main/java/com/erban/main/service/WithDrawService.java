package com.erban.main.service;

import com.erban.main.model.BillRecord;
import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.model.WithDrawCashProd;
import com.erban.main.mybatismapper.WithDrawCashProdMapper;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.erban.main.vo.WithDrawVo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.SmsRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class WithDrawService {

    @Autowired
    private WithDrawCashProdMapper withDrawCashProdMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private SmsRecordService smsRecordService;


    private Gson gson = new Gson();



    public Long getCashNum(String pid) {
        WithDrawCashProd withDraw = withDrawCashProdMapper.selectByPrimaryKey(pid);
        return withDraw.getCashNum();
    }

    public List<WithDrawCashProd> findList() {
        return withDrawCashProdMapper.selectAll();
    }

    /**
     * 获取用户提现信息
     * @param uid
     * @param busiResult
     * @return
     */
    public BusiResult getWithDraw(Long uid, BusiResult busiResult) {
        Users users = usersService.getUsersByUid(uid);
        WithDrawVo withDrawVo = new WithDrawVo();
        withDrawVo.setUid(uid);
        if (StringUtils.isNoneBlank(users.getAlipayAccount())) {
            withDrawVo.setAlipayAccount(users.getAlipayAccount());
        }
        if (StringUtils.isNoneBlank(users.getAlipayAccountName())) {
            withDrawVo.setAlipayAccountName(users.getAlipayAccountName());
        }
        if(CommonUtil.checkValidPhone(users.getPhone())){
            withDrawVo.setIsNotBoundPhone(false);
        }else{
            withDrawVo.setIsNotBoundPhone(true);
        }
        UserPurse userPurse = userPurseService.getPurseByUid(uid);
        withDrawVo.setDiamondNum(userPurse.getDiamondNum());
        busiResult.setData(withDrawVo);
        return busiResult;
    }

    public BusiResult boundPhone(Long uid, String phone, String code) throws Exception {
        return usersService.bindPhone(uid, phone, code);
    }

    public BusiResult getBoundPhoneCode(String phone,String ip,String deviceId,String imei,String os,String osversion,String channel,String appVersion,String model) throws Exception{
        BusiResult busiResult= smsService.sendSmsByType(phone, 4, ip, deviceId, imei, os, osversion, channel, appVersion, model);
        return busiResult;
    }

    public BusiResult getCode(Long uid,String ip,String deviceId,String imei,String os,String osversion,String channel,String appVersion,String model) {
        try {
            Users users = usersService.getUsersByUid(uid);
            return smsService.sendSmsByType(users.getPhone(), 5,ip, deviceId, imei, os, osversion, channel, appVersion, model);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SMSSENDERROR);
        }
    }

    public BusiResult bound(Long uid, String aliPayAccount, String aliPayAccountName, String code) throws Exception {
        if (StringUtils.isNoneBlank(aliPayAccount) && StringUtils.isNoneBlank(aliPayAccountName)
                && StringUtils.isNoneBlank(code)) {
            // 将用户输入验证码进行判断
            Users users = usersService.getUsersByUid(uid);
            if (smsService.verifySmsCodeByNetEase(users.getPhone(), code)) {
                users.setAlipayAccount(aliPayAccount);
                users.setAlipayAccountName(aliPayAccountName);
                usersService.saveOrUpdateUserByUidV2(users,null);
            } else {
                return new BusiResult(BusiStatus.SMSCODEERROR);
            }
            BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
            UserVo userVo = new UserVo();
            userVo.setUid(uid);
            userVo.setAlipayAccount(users.getAlipayAccount());
            userVo.setAlipayAccountName(users.getAlipayAccountName());
            busiResult.setData(userVo);
            busiResult.setCode(200);
            return busiResult;
        }
        return new BusiResult(BusiStatus.BUSIERROR);
    }

    /**
     * 钻石提现
     * @param uid
     * @param pid
     * @return
     *
     */
    public BusiResult withDraw(Long uid, String pid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        //1.判断一周内的提现记录（不能超过2次）
//        List<BillRecord> billRecordList = billRecordService.selectBillRecord(uid);
//        if (!CollectionUtils.isEmpty(billRecordList)) {
//            if (billRecordList.size() >= 2) {
//                return new BusiResult(BusiStatus.WEEKNOTWITHCASHTOWNUMS);
//            }
//        }
        //2.用户是否生成钱包
        UserPurse userPurse = userPurseService.getPurseByUid(uid);
        if (userPurse == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //3.是否有绑定支付包账号
        Users users=usersService.getUsersByUid(uid);
        if(StringUtils.isEmpty(users.getAlipayAccount())||StringUtils.isEmpty(users.getAlipayAccountName())){
            return new BusiResult(BusiStatus.ALIAPYACCOUNTNOTEXISTS);
        }
        Long cashNum = getCashNum(pid);//提现金额
        Long diamondNum = userPurse.getDiamondNum().longValue();//钱包钻石
        //4.比较用户余额与取现列表的金额
        WithDrawVo withDrawVo = new WithDrawVo();
        Double exDiamondNum = (cashNum * 100.0)/9.0;
        if(exDiamondNum<=0){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //5.提现，生成billRecord记录，更新userPurse
        if (diamondNum >= exDiamondNum) {
            userPurse.setDiamondNum(diamondNum - exDiamondNum);
            userPurse.setUpdateTime(new Date());
            userPurseService.updateUserPurse(userPurse);
            billRecordService.insertBillRecordByWithDraw(uid, uid, Constant.WithDraw.ing, null, Constant.BillType.getCash, -exDiamondNum, null,
                    cashNum);
            withDrawVo.setUid(uid);
            withDrawVo.setDiamondNum(userPurse.getDiamondNum());//用户钱包剩余钻石余额
        } else {
            return new BusiResult(BusiStatus.DIAMONDNUMNOTENOUGH);
        }
        busiResult.setData(withDrawVo);
        busiResult.setMessage("提现成功，审核通过后将会在1-2个工作日到账");
        return busiResult;
    }

    public BusiResult findWithDrawList() {
        List<WithDrawCashProd> list = new ArrayList<>();
        Map<String, String> map = jedisService.hgetAllBykey(RedisKey.withdraw_cash_list.getKey());
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String value = entry.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    WithDrawCashProd withDrawCashProd = gson.fromJson(value, WithDrawCashProd.class);
                    list.add(withDrawCashProd);
                }
            }
        } else {
            list = findList();
            /* 将查询到的提现列表存入redis中 */
            for (WithDrawCashProd withDrawCashProd : list) {
                jedisService.hwrite(RedisKey.withdraw_cash_list.getKey(), withDrawCashProd.getCashProdId().toString(),
                        gson.toJson(withDrawCashProd));
            }
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(list);
        busiResult.setCode(200);
        return busiResult;
    }
}
