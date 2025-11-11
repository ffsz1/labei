package com.erban.main.service.user;

import com.beust.jcommander.internal.Lists;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.UserShareRecordMapper;
import com.erban.main.param.neteasepush.Body;
import com.erban.main.param.neteasepush.NeteaseSendMsgBatchParam;
import com.erban.main.param.neteasepush.Payload;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.statis.StatPacketActivityService;
import com.erban.main.service.statis.StatPacketRegisterService;
import com.erban.main.vo.UserPacketRecordVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.GetTimeUtils;
import com.xchat.common.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserShareRecordService extends BaseService {

    @Autowired
    private UserShareRecordMapper userShareRecordMapper;
    @Autowired
    private UserPacketService userPacketService;
    @Autowired
    private StatPacketActivityService statPacketActivityService;
    @Autowired
    private StatPacketRegisterService statPacketRegisterService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private UserConfigureService userConfigureService;
    private static DecimalFormat doubleFormat = new DecimalFormat("0.00");


    /**
     * 保存分享记录（分享奖励，邀请奖励）
     * @param uid
     * @param shareType
     * @param sharePageId
     * @param targetUid
     * @return
     */
    public BusiResult saveUserShareRecord(Long uid, String shareType, int sharePageId, Long targetUid) {
        Date date =new Date();
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        String shareId=UUIDUitl.get();
        //1.获取红包金额（每天第一次分享才有红包）
        double sharePacketNum=genSharePacketNum(uid);
        UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
        if(sharePacketNum>0.00){
            //如果红包金额>0，则
            userPacketService.addUserPacketNumTransactional(uid,sharePacketNum,Constant.RedPacket.share,shareId,null);
            userPacketRecordVo.setUid(uid);
            userPacketRecordVo.setPacketNum(sharePacketNum);
            userPacketRecordVo.setCreateTime(date);
            userPacketRecordVo.setType(Constant.RedPacket.share);
            userPacketRecordVo.setNeedAlert(true);
            userPacketRecordVo.setPacketName("分享");
            busiResult.setData(userPacketRecordVo);
            //发送系统消息给用户
            sendPush(userPacketRecordVo,Constant.DefMsgType.Packet,Constant.DefMsgType.PacketShare);
        }else{
            busiResult.setData(userPacketRecordVo);
        }
        //2.生成一条分享记录
        UserShareRecord userShareRecord=new UserShareRecord();
        userShareRecord.setShareId(shareId);
        userShareRecord.setUid(uid);
        userShareRecord.setShareId(UUIDUitl.get());
        userShareRecord.setSharePageId(sharePageId);
        userShareRecord.setShareType(new Byte(shareType));
        userShareRecord.setTargetUid(targetUid);
        userShareRecord.setCreateTime(date);
        userShareRecordMapper.insert(userShareRecord);
//        //3.是否分享的是抽奖页面
//        if(UserDrawService.sharePageId==sharePageId){
//            userDrawService.genUserDrawChanceByShare(uid,shareId);
//        }
        return busiResult;
    }
    public void sendPushTest(Long uid){
        UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
        userPacketRecordVo.setUid(uid);
        userPacketRecordVo.setPacketNum(20.0);
        userPacketRecordVo.setPacketName("首次送红包");
        userPacketRecordVo.setNeedAlert(true);
        sendPush(userPacketRecordVo,Constant.DefMsgType.Packet,Constant.DefMsgType.PacketFirst);
    }

    public void sendPush( UserPacketRecordVo userPacketRecordVo,int first,int second){
        List<String> toAccids= Lists.newArrayList();
        toAccids.add(userPacketRecordVo.getUid().toString());
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
        neteaseSendMsgBatchParam.setToAccids(toAccids);
        neteaseSendMsgBatchParam.setType(100);
        neteaseSendMsgBatchParam.setPushcontent("您获得了一个红包，点击查看");
        Body body = new Body();
        body.setFirst(first);
        body.setSecond(second);
        body.setData(userPacketRecordVo);
        neteaseSendMsgBatchParam.setBody(body);
        Payload payload = new Payload();
        neteaseSendMsgBatchParam.setPayload(payload);
        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
    }

    /**
     * Async异步执行红包邀请活动,用户首次注册，并且属于被人邀请人，则邀请人获得红包
     * @param uid  分享人uid
     * @param targetUid 注册用户的 uid
     * @return
     */
    @Async
    public BusiResult saveUserShareRegisterRecord(Long uid,  Long targetUid) {
        Date date =new Date();
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        //1.获取红包金额
        double registerPacketNum=getRegisterPacketNum(uid);
        UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
        //2.更新相关统计表
        if(registerPacketNum>0.00){
            userPacketService.addUserPacketNumTransactional(uid,registerPacketNum,Constant.RedPacket.register,String.valueOf(targetUid),targetUid);
            userPacketRecordVo.setPacketNum(registerPacketNum);
            userPacketRecordVo.setCreateTime(date);
            userPacketRecordVo.setUid(uid);
            userPacketRecordVo.setType(Constant.RedPacket.register);
            userPacketRecordVo.setNeedAlert(true);
            userPacketRecordVo.setPacketName("邀请");
            //3.发送系统消息
            sendPush(userPacketRecordVo,Constant.DefMsgType.Packet,Constant.DefMsgType.PacketRegister);
            busiResult.setData(userPacketRecordVo);
        }else{
            busiResult.setData(userPacketRecordVo);
        }
        return busiResult;
    }

    public BusiResult saveUserBonusRecord(Long uid, String objId,int chargeMoneyCent) {
        Date date =new Date();
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        //查询是否是他人邀请注册
        StatPacketRegister statPacketRegister=statPacketRegisterService.getShareRegisterByRegisterUid(uid);
        //没有被邀请记录，不做处理
        if(statPacketRegister==null){
            return busiResult;
        }
        Long gainBonusUid=statPacketRegister.getUid();
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(gainBonusUid.toString());
        Integer bonusLevel;
        //是否有上级分成权限
        if(userConfigure==null||userConfigure.getSuperiorBouns().intValue()==0){
            bonusLevel=1;
        }else{
            bonusLevel = getBonusLevel(gainBonusUid);
        }
        Double bonusRate;
        if(bonusLevel==1){
            bonusRate = Constant.PacketConst.bonusRate1;
        }else if(bonusLevel==2){
            bonusRate = Constant.PacketConst.bonusRate2;
        }else if(bonusLevel==3){
            bonusRate = Constant.PacketConst.bonusRate3;
        }else{
            bonusRate = Constant.PacketConst.bonusRate4;
        }
        double packetNumBonus=genBonus(chargeMoneyCent, bonusRate);
        UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
        if(packetNumBonus>0.00){
            userPacketService.addUserPacketNumTransactional(gainBonusUid,packetNumBonus,Constant.RedPacket.bouns,objId,uid);
            userPacketRecordVo.setPacketNum(packetNumBonus);
            userPacketRecordVo.setUid(gainBonusUid);
            userPacketRecordVo.setCreateTime(date);
            userPacketRecordVo.setType(Constant.RedPacket.bouns);
            userPacketRecordVo.setNeedAlert(true);
            userPacketRecordVo.setPacketName("分成");
            sendPush(userPacketRecordVo,Constant.DefMsgType.Packet,Constant.DefMsgType.PacketBouns);
            // 添加上一级邀请人分成
            saveUserSuperiorBounsRecord(uid, gainBonusUid, objId, chargeMoneyCent);
            busiResult.setData(userPacketRecordVo);
        }else{
            busiResult.setData(userPacketRecordVo);
        }

        return busiResult;
    }

    public void saveUserSuperiorBounsRecord(Long uid, Long gainBonusUid, String objId,int chargeMoneyCent) {
        //查询是否是他人邀请注册
        StatPacketRegister statPacketRegister=statPacketRegisterService.getShareRegisterByRegisterUid(gainBonusUid);
        //没有被邀请记录，不做处理
        if(statPacketRegister==null){
            return;
        }
        Long superiorBonusUid=statPacketRegister.getUid();
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(superiorBonusUid.toString());
        //是否有上级分成权限
        if(userConfigure==null||userConfigure.getSuperiorBouns().intValue()==0){
            return;
        }
        Integer bonusLevel = getBonusLevel(superiorBonusUid);
        Double bonusRate;
        if(bonusLevel==1){
            bonusRate = Constant.PacketConst.superiorBonusRate1;
        }else if(bonusLevel==2){
            bonusRate = Constant.PacketConst.superiorBonusRate2;
        }else if(bonusLevel==3){
            bonusRate = Constant.PacketConst.superiorBonusRate3;
        }else{
            bonusRate = Constant.PacketConst.superiorBonusRate4;
        }
        double packetNumBonus=genBonus(chargeMoneyCent, bonusRate);
        UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
        if(packetNumBonus>0.00){
            userPacketService.addUserPacketNumTransactional(superiorBonusUid,packetNumBonus,Constant.RedPacket.superiorBouns,objId,uid);
            userPacketRecordVo.setPacketNum(packetNumBonus);
            userPacketRecordVo.setUid(superiorBonusUid);
            userPacketRecordVo.setCreateTime(new Date());
            userPacketRecordVo.setType(Constant.RedPacket.superiorBouns);
            userPacketRecordVo.setNeedAlert(true);
            userPacketRecordVo.setPacketName("分成");
            sendPush(userPacketRecordVo,Constant.DefMsgType.Packet,Constant.DefMsgType.PacketBouns);
        }
    }

    public int getBonusLevel(Long uid) {
        String bonusLevel = jedisService.hget(RedisKey.bonus_level.getKey(), uid.toString());
        if(StringUtils.isBlank(bonusLevel)){
            Double lastBonus = jdbcTemplate.queryForObject("select SUM(packet_num) from user_packet_record where uid = ? and type in (1,3,4,6) and create_time BETWEEN ? and ?", Double.class, uid, DateTimeUtil.getLastMonthStart(), DateTimeUtil.getLastMonthEnd());
            if(lastBonus==null || lastBonus.intValue()<1000){
                bonusLevel = "1";
            }else if(lastBonus.intValue()<2000){
                bonusLevel = "2";
            }else if(lastBonus.intValue()<3000){
                bonusLevel = "3";
            }else{
                bonusLevel = "4";
            }
            jedisService.hset(RedisKey.bonus_level.getKey(), uid.toString(), bonusLevel);
        }
        return Integer.valueOf(bonusLevel);
    }

    private double genBonus(int chargeMoneyCent, double rate){
        double packetNumBonus=chargeMoneyCent/100*rate;
        packetNumBonus=new Double(doubleFormat.format(packetNumBonus));
        return packetNumBonus;
    }

    /**
     * 获取分享红包金额
     * @param uid
     * @return
     */
    private double genSharePacketNum(Long uid){
        double randomPacketNum=0.00;
        List<UserShareRecord> todayShareRecordList=getTodayShareRecordList(uid);
        if(CollectionUtils.isNotEmpty(todayShareRecordList)){//每天只能领取一个红包
            return randomPacketNum;
        }else{
            List<UserShareRecord> alleShareRecordList=getAllShareRecordList(uid);
            int size;
            if(CollectionUtils.isEmpty(alleShareRecordList)){
                size=0;
            }else{
                size=alleShareRecordList.size();
            }
            randomPacketNum=getSharePacketNumByCount(size);
        }
        randomPacketNum=new Double(doubleFormat.format(randomPacketNum));
        return randomPacketNum;
    }

    private UserPacketRecord genPacketRecord(Long uid,String objId){
//        UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
//        userPacketRecordVo.setPacketNum(1.5);

        double randomPacketNum=0.00;
        List<UserShareRecord> todayShareRecordList=getTodayShareRecordList(uid);
        if(CollectionUtils.isNotEmpty(todayShareRecordList)){//每天只能领取一个红包
            return null;
        }else{
            List<UserShareRecord> alleShareRecordList=getAllShareRecordList(uid);
            int size;
            if(CollectionUtils.isEmpty(alleShareRecordList)){
                size=0;
            }else{
                size=alleShareRecordList.size();
            }
            randomPacketNum=getSharePacketNumByCount(size);
        }
        UserPacketRecord userPacketRecord=new UserPacketRecord();
        userPacketRecord.setPacketNum(randomPacketNum);
        userPacketRecord.setUid(uid);
        userPacketRecord.setObjId(objId);
        userPacketRecord.setType(Constant.RedPacket.share);
        userPacketRecord.setCreateTime(new Date());
        return  userPacketRecord;
    }


    private double getRegisterPacketNum(Long uid){
        //1.获取用户参与活动的红包统计数据
        StatPacketActivity statPacketActivity=statPacketActivityService.getStatPacketActivityByUid(uid);
        double packetNum=0.00;
        //2.生成随机红包金额
        if(statPacketActivity.getRegisterCout()<=7){
            packetNum=genDoubleNumberByRegion(Constant.PacketNumRandomRegion.RegisterLess7);
        }else{
            packetNum=genDoubleNumberByRegion(Constant.PacketNumRandomRegion.RegisterMore7);
        }
        packetNum=new Double(doubleFormat.format(packetNum));
        return packetNum;

    }

    /**
     * 分享红包0.5到1.5，前三次1到1.5
     * 1.分享红包,前七次分享0.5到1.5，后面分享0.1到0.5 升级改版2017-10-26
     * @param size
     * @return
     */
    private double getSharePacketNumByCount(int size){
        double packetNum=0.00;
        if(size<=7){
            packetNum=genDoubleNumberByRegion(Constant.PacketNumRandomRegion.ShareLess7);
        }else{
            packetNum=genDoubleNumberByRegion(Constant.PacketNumRandomRegion.ShareMore7);
        }
        return packetNum;
    }
    private double genDoubleNumberByRegion(Double region[]){
        Random ra =new Random();
        double dd=ra.nextDouble()*(region[1]-region[0])+region[0];
        double number=new Double(doubleFormat.format(dd));
        return number;
    }

    public List<UserShareRecord> getTodayShareRecordList(Long uid){
        UserShareRecordExample userShareRecordExample=new UserShareRecordExample();
        userShareRecordExample.createCriteria().andUidEqualTo(uid).andCreateTimeBetween(GetTimeUtils.getTimesnight(0),GetTimeUtils.getTimesnight(24));
        List<UserShareRecord> userShareRecordList=userShareRecordMapper.selectByExample(userShareRecordExample);
        return userShareRecordList;
    }
    public List<UserShareRecord> getAllShareRecordList(Long uid){
        UserShareRecordExample userShareRecordExample=new UserShareRecordExample();
        userShareRecordExample.createCriteria().andUidEqualTo(uid);
        List<UserShareRecord> userShareRecordList=userShareRecordMapper.selectByExample(userShareRecordExample);
        return userShareRecordList;
    }



    public static void main(String args[]){
        Date date=new Date();
        System.out.println(GetTimeUtils.getTimesnights(date, 0));
        System.out.println(GetTimeUtils.getTimesnight(24));
//        List<UserShareRecord> alleShareRecordList=null;
//        System.out.print(alleShareRecordList.size());
    UserShareRecordService userShareRecordService=new UserShareRecordService();
        for(int i=0;i<100;i++){
            Random ra =new Random();
            double dd=userShareRecordService.getSharePacketNumByCount(2);
            System.out.println(dd);
        }

    }


}
