package com.erban.main.service.user;

import com.beust.jcommander.internal.Lists;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.UserPacket;
import com.erban.main.mybatismapper.UserPacketMapper;
import com.erban.main.mybatismapper.UserPacketMapperMgr;
import com.erban.main.param.neteasepush.Body;
import com.erban.main.param.neteasepush.NeteaseSendMsgBatchParam;
import com.erban.main.param.neteasepush.Payload;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.statis.StatPacketActivityService;
import com.erban.main.service.statis.StatPacketRegisterService;
import com.erban.main.vo.UserPacketRecordVo;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户红包service
 */
@Service
public class UserPacketService {

    @Autowired
    private UserPacketMapper userPacketMapper;
    @Autowired
    private UserPacketMapperMgr userPacketMapperMgr;
    @Autowired
    private StatPacketActivityService statPacketActivityService;
    @Autowired
    private UserPacketRecordService userPacketRecordService;
    @Autowired
    private StatPacketRegisterService statPacketRegisterService;
    @Autowired
    private SendSysMsgService sendSysMsgService;

    private static final Logger logger = LoggerFactory.getLogger(UserPacketService.class);


    public UserPacket getUserPacketByUid(Long uid) {
        return userPacketMapper.selectByPrimaryKey(uid);
    }

    public void updateUserPacket(UserPacket userPacket) {
        userPacket.setUpdateTime(new Date());
        userPacket.setFirstGetTime(null);
        userPacket.setHistPacketNum(null);
        userPacketMapper.updateByPrimaryKeySelective(userPacket);
    }
    public UserPacket createUserPacket(Long uid){
        Date date =new Date();
        UserPacket userPacket=new  UserPacket();
        userPacket.setUid(uid);
        userPacket.setPacketNum(Constant.PacketConst.fistrtPacketNum);
        userPacket.setHistPacketNum(Constant.PacketConst.fistrtPacketNum);
        userPacket.setCreateTime(date);
        userPacket.setFirstGetTime(date);
        userPacketMapper.insert(userPacket);
        statPacketActivityService.insertStatPacketActivityByFirstPacket(uid,Constant.PacketConst.fistrtPacketNum);
        userPacketRecordService.savePacketRecordByAction( uid,Constant.RedPacket.first, Constant.PacketConst.fistrtPacketNum,
                 null, null);
        return userPacket;

    }

    public BusiResult<UserPacketRecordVo> checkAndGetFirsetPacket(Long uid) throws Exception{
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        UserPacket getUserPacketByUid=getUserPacketByUid(uid);
        logger.info("UserPacketService checkAndGetFirsetPacket ");
        if(getUserPacketByUid!=null){
            logger.info("getUserPacketByUid="+uid);
            busiResult.setData(new UserPacketRecordVo());
            return busiResult;
        }else{
            logger.info("getUserPacketByUid="+getUserPacketByUid);
            UserPacket userPacket= createUserPacket(uid);
            UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
            userPacketRecordVo.setUid(userPacket.getUid());
            userPacketRecordVo.setType(Constant.RedPacket.first);
            userPacketRecordVo.setPacketNum(userPacket.getPacketNum());
            userPacketRecordVo.setNeedAlert(true);
            userPacketRecordVo.setPacketName("新人");
            busiResult.setData(userPacketRecordVo);
            sendPush(userPacketRecordVo,Constant.DefMsgType.Packet,Constant.DefMsgType.PacketFirst);
        }
        return busiResult;

    }
    public void sendPush( UserPacketRecordVo userPacketRecordVo,int first,int second) throws Exception{
        List<String> toAccids= Lists.newArrayList();
        toAccids.add(userPacketRecordVo.getUid().toString());
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
        neteaseSendMsgBatchParam.setToAccids(toAccids);
        neteaseSendMsgBatchParam.setType(100);
        neteaseSendMsgBatchParam.setPushcontent("您获得了一个红包，点击查看~");
        Body body = new Body();
        body.setFirst(first);
        body.setSecond(second);
        body.setData(userPacketRecordVo);
        neteaseSendMsgBatchParam.setBody(body);
        Payload payload = new Payload();
        neteaseSendMsgBatchParam.setPayload(payload);
        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
    }




    public UserPacket getUserPacket(Long uid){
        UserPacket userPacket=userPacketMapper.selectByPrimaryKey(uid);
        if(userPacket==null){
             userPacket=createUserPacket(uid);
        }
        return userPacket;
    }

    /**
     *发红包并更新统计表
     * @param uid  分享人uid
     * @param packetNum 红包金额
     * @param type 红包类型
     * @param objId
     * @param srcUid 被分享人uid
     */
    public void addUserPacketNumTransactional(Long uid,double packetNum,Byte type,String objId,Long srcUid){
        UserPacket userPacket =new UserPacket();
        userPacket.setUid(uid);
        userPacket.setPacketNum(packetNum);
        //1.将此次获得的红包发放给用户，累加到用户红包表（userpacket表为用户红包表）
        userPacketMapperMgr.addUserPacketNumTransactional(userPacket);
        //2.更新红包获得统计表
        if(Constant.RedPacket.share.equals(type)){
            statPacketActivityService.updateStatPacketAcitivtyByShare(uid);
        }else if(Constant.RedPacket.register.equals(type)){
            //邀请注册，如果statpacketregister表中不存在此条注册数据，则生成一条statpacketregister数据，并更新statpacketactivity表，否则不做任何操作
            int count=statPacketRegisterService.saveStatPacketRegister(uid,srcUid);
            if(count>0){
                statPacketActivityService.updateStatPacketActivityByRegister(uid);
            }
        }else if(Constant.RedPacket.bouns.equals(type) || Constant.RedPacket.superiorBouns.equals(type)){
            //充值分成奖励
            statPacketActivityService.updateStatPacketActivityByBouns(uid,packetNum);
        }else{
            return;
        }
        //3.生成一条用户红包记录
        userPacketRecordService.savePacketRecordByAction( uid, type, packetNum, objId, srcUid);
    }

}
