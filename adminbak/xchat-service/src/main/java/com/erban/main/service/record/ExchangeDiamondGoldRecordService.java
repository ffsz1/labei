package com.erban.main.service.record;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.ExchangeDiamondGoldRecordMapper;
import com.erban.main.mybatismapper.GiftCarGetRecordMapper;
import com.erban.main.mybatismapper.GiftCarPurseRecordMapper;
import com.erban.main.service.ChargeService;
import com.erban.main.service.SmsService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.gift.GiftCarPurseService;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserPurseVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

@Service
public class ExchangeDiamondGoldRecordService extends BaseService {
    @Autowired
    private ExchangeDiamondGoldRecordMapper exchangeDiamondGoldRecordMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private GiftCarService giftCarService;
    @Autowired
    private GiftCarPurseService giftCarPurseService;
    @Autowired
    private GiftCarPurseRecordMapper giftCarPurseRecordMapper;
    @Autowired
    private GiftCarGetRecordMapper giftCarGetRecordMapper;
    @Autowired
    private RoomService roomService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UsersService usersService;

    //    @Transactional(rollbackFor = Exception.class)
    public BusiResult exchangeDiamondToGold(Long uid, double diamondNum, boolean doDraw, String smsCode, String os) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        UserPurse userPurse = userPurseService.getPurseByUid(uid);
        if(userPurse==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Room room = roomService.getRoomByUid(uid);
        if(room != null && (room.getIsPermitRoom().intValue()==1 || room.getIsPermitRoom().intValue()==3)){
            if(StringUtils.isBlank(smsCode)){
                if("android".equalsIgnoreCase(os)){
                    return new BusiResult(BusiStatus.NOAUTHORITY);
                }else{
                    return new BusiResult(BusiStatus.NOTAUTHORITY);
                }
            }else {
                Users users = usersService.getUsersByUid(uid);
                if(users==null){
                    return new BusiResult(BusiStatus.USERNOTEXISTS);
                }
                if(!smsService.verifySmsCodeByNetEase(users.getPhone(), smsCode)){
                    return new BusiResult(BusiStatus.SMSCODEERROR);
                }
            }
        }
        double totalDiamondNum = userPurse.getDiamondNum();
        if (diamondNum <= 0) {
            return new BusiResult<UserPurseVo>(BusiStatus.PARAMETERILLEGAL);
        }
        if (diamondNum % 10 != 0) {
            return new BusiResult<UserPurseVo>(BusiStatus.EXCHANGEINPUTERROR);
        }
        if (diamondNum > totalDiamondNum) {
            return new BusiResult<UserPurseVo>(BusiStatus.DIAMONDNUMNOTENOUGH);
        }
        Double goldD = diamondNum * Constant.ExchangeDiamondGold.rate;
        Long exGoldNum = goldD.longValue();
        String drawMsg="";
        String drawUrl="";
        GiftCar giftCar=null;
        Integer carId=null;
        Integer carDate=0;
        if(doDraw && exGoldNum >= 100L){// 如果大于100并且是新版本，添加抽奖
            double randomNumber = genDoubleNumber();
            double exchangeDiamond[];
            Double drawGold = 0D;
            if(exGoldNum<1000){
                exchangeDiamond = Constant.ExchangeDiamondRange.exchangeDiamond100;
                if (randomNumber >= exchangeDiamond[0] && randomNumber <= exchangeDiamond[1]) {
                    drawGold = 0D;
                } else if (randomNumber > exchangeDiamond[1] && randomNumber <= exchangeDiamond[2]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange1;
                } else if (randomNumber > exchangeDiamond[2] && randomNumber <= exchangeDiamond[3]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange2;
                } else if (randomNumber > exchangeDiamond[3] && randomNumber <= exchangeDiamond[4]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange3;
                } else if (randomNumber > exchangeDiamond[4] && randomNumber <= exchangeDiamond[5]) {
                    carId = Constant.ExchangeDiamoudDraw.exchange7;
                    carDate = 3;
                } else if (randomNumber > exchangeDiamond[5] && randomNumber <= exchangeDiamond[6]) {
                    carId = Constant.ExchangeDiamoudDraw.exchange8;
                    carDate = 3;
                }
            }else if(exGoldNum<5000) {
                exchangeDiamond = Constant.ExchangeDiamondRange.exchangeDiamond1000;
                if (randomNumber >= exchangeDiamond[0] && randomNumber <= exchangeDiamond[1]) {
                    drawGold = 0D;
                } else if (randomNumber > exchangeDiamond[1] && randomNumber <= exchangeDiamond[2]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange1;
                } else if (randomNumber > exchangeDiamond[2] && randomNumber <= exchangeDiamond[3]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange2;
                } else if (randomNumber > exchangeDiamond[3] && randomNumber <= exchangeDiamond[4]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange3;
                } else if (randomNumber > exchangeDiamond[4] && randomNumber <= exchangeDiamond[5]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange4;
                } else if (randomNumber > exchangeDiamond[5] && randomNumber <= exchangeDiamond[6]) {
                    carId = Constant.ExchangeDiamoudDraw.exchange7;
                    carDate = 7;
                } else if (randomNumber > exchangeDiamond[6] && randomNumber <= exchangeDiamond[7]) {
                    carId = Constant.ExchangeDiamoudDraw.exchange9;
                    carDate = 7;
                }
            }else {
                exchangeDiamond = Constant.ExchangeDiamondRange.exchangeDiamond5000;
                if (randomNumber >= exchangeDiamond[0] && randomNumber <= exchangeDiamond[1]) {
                    drawGold = 0D;
                } else if (randomNumber > exchangeDiamond[1] && randomNumber <= exchangeDiamond[2]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange2;
                } else if (randomNumber > exchangeDiamond[2] && randomNumber <= exchangeDiamond[3]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange3;
                } else if (randomNumber > exchangeDiamond[3] && randomNumber <= exchangeDiamond[4]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange4;
                } else if (randomNumber > exchangeDiamond[4] && randomNumber <= exchangeDiamond[5]) {
                    drawGold = goldD * Constant.ExchangeDiamoudDraw.exchange5;
                } else if (randomNumber > exchangeDiamond[5] && randomNumber <= exchangeDiamond[6]) {
                    carId = Constant.ExchangeDiamoudDraw.exchange10;
                    carDate = 7;
                } else if (randomNumber > exchangeDiamond[6] && randomNumber <= exchangeDiamond[7]) {
                    carId = Constant.ExchangeDiamoudDraw.exchange11;
                    carDate = 7;
                }
            }
            if(carId!=null){// 中了座驾
                giftCar = giftCarService.getOneByJedisId(carId.toString());
                if(giftCar==null){
                    return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
                }
                drawMsg = "获得"+giftCar.getCarName()+carDate+"天使用权";
                drawUrl = giftCar.getPicUrl();
            }else if(drawGold>0){// 中了金币
                drawMsg = "额外爆出+"+drawGold.longValue();
                drawUrl = "http://res.91fb.com/dh_jinbi.png";
            }else{
                drawMsg = "很遗憾，没有爆中";
                drawUrl = "0";
            }
            exGoldNum = exGoldNum+drawGold.longValue();
        }
        //更新userpurse,并刷新缓存
        userPurse.setDiamondNum(totalDiamondNum - diamondNum);
        userPurse.setGoldNum(userPurse.getGoldNum() + exGoldNum);
        userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() + exGoldNum);
        int result = userPurseUpdateService.exchageDiamondToChargeGold(userPurse, uid, diamondNum, exGoldNum);
        if (result != 200) {
            return new BusiResult<UserPurseVo>(BusiStatus.DIAMONDNUMNOTENOUGH);
        }
        if(giftCar!=null){// 中了座驾
            String purse = giftCarPurseService.getPurse(uid);
            if(StringUtils.isNotBlank(purse)){
                if(!StringUtils.splitToList(purse, ",").contains(carId.toString())){
                    purse+=","+carId;
                }
            }else{
                purse=""+carId;
            }
            jedisService.hwrite(RedisKey.gift_car_purse_list.getKey(), uid.toString(), purse);
            GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), carId.toString());
            if(giftCarPurseRecord==null){
                giftCarPurseRecord=new GiftCarPurseRecord();
                giftCarPurseRecord.setUid(uid);
                giftCarPurseRecord.setCarId(carId.longValue());
                giftCarPurseRecord.setTotalGoldNum(0L);
                giftCarPurseRecord.setCarDate(carDate);
                giftCarPurseRecord.setIsUse(new Byte("0"));
                giftCarPurseRecord.setCreateTime(new Date());
                giftCarPurseRecordMapper.insertSelective(giftCarPurseRecord);
            }else{
                giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate()+carDate);
                giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
            }
            jedisService.hset(RedisKey.gift_car_purse.getKey(), uid.toString() + "_" + carId.toString(), gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
            GiftCarGetRecord giftCarGetRecord = new GiftCarGetRecord();
            giftCarGetRecord.setUid(uid);
            giftCarGetRecord.setCarId(carId.longValue());
            giftCarGetRecord.setCarDate(carDate);
            giftCarGetRecord.setType(new Byte("3"));
            giftCarGetRecord.setCreateTime(new Date());
            giftCarGetRecordMapper.insert(giftCarGetRecord);
        }
        String reocrdId = UUIDUitl.get();
        saveExchageRecord(reocrdId, uid, diamondNum, exGoldNum);
        saveBillRecord(uid, reocrdId, diamondNum, exGoldNum);
        saveChargeRecord(reocrdId, uid, diamondNum, exGoldNum);
        UserPurseVo userPurseVo = userPurseService.getUserPurseVo(uid);
        userPurseVo.setDrawMsg(drawMsg);
        userPurseVo.setDrawUrl(drawUrl);
        busiResult.setData(userPurseVo);
        return busiResult;
    }

    private static double genDoubleNumber() {
        DecimalFormat doubleFormat = new DecimalFormat("0.0000");
        Random ra = new Random();
        double dd = ra.nextDouble();
        double number = new Double(doubleFormat.format(dd));
        return number;
    }

    private void saveExchageRecord(String recordId, Long uid, double exDiamondNum, Long exGold) {
        Date date = new Date();
        ExchangeDiamondGoldRecord exchangeDiamondGoldRecord = new ExchangeDiamondGoldRecord();
        exchangeDiamondGoldRecord.setRecordId(recordId);
        exchangeDiamondGoldRecord.setUid(uid);
        exchangeDiamondGoldRecord.setExDiamondNum(exDiamondNum);
        exchangeDiamondGoldRecord.setExGoldNum(exGold);
        exchangeDiamondGoldRecord.setCreateTime(date);
        exchangeDiamondGoldRecordMapper.insert(exchangeDiamondGoldRecord);
    }

    private void saveChargeRecord(String recordId, Long uid, double exDiamondNum, Long exGold) throws Exception {
        Double amountDouble = exDiamondNum * 10;//统一人民币单位：分
        Long amount = amountDouble.longValue();
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setChargeRecordId(recordId);
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(Constant.ChargeChannel.exchageByDiamond);
        chargeRecord.setChargeProdId(Constant.ChargeChannel.exchageByDiamond);
        chargeRecord.setAmount(amount);
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
        String chargeDesc = exDiamondNum + "钻石兑换" + exGold + "金币";
        chargeRecord.setTotalGold(exGold);
        chargeRecord.setChargeDesc(chargeDesc);
        Date date = new Date();
        chargeRecord.setCreateTime(date);
        chargeRecord.setUpdateTime(date);
        chargeService.insertChargeRecord(chargeRecord);
    }

    private void saveBillRecord(Long uid, String objId, double exDiamondNum, Long exGold) {
        billRecordService.insertBillRecord(uid, uid, objId, Constant.BillType.exchangeDimondToGoldIncome, null, exGold, null);

        billRecordService.insertBillRecord(uid, uid, objId, Constant.BillType.exchangeDimondToGoldPay, -exDiamondNum, null, null);
    }

}
