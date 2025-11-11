package com.erban.main.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.erban.main.config.SystemConfig;
import com.erban.main.param.neteasepush.*;
import com.erban.main.vo.UserPacketRecordVo;
import com.erban.main.wechat.util.StringUtil;
import com.google.gson.Gson;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.neteaseacc.result.RubbishRet;
import com.xchat.common.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liuguofu on 2017/6/6.
 */
@Service
public class SendSysMsgService {
    private static final Logger logger = LoggerFactory.getLogger(SendSysMsgService.class);
    @Autowired
    private  ErBanNetEaseService erBanNetEaseService;
    private static int defMsgType=100;
    private Gson gson=new Gson();


    /**
     * 发送系统通知，捕获异常不抛出。
     * @param neteasePushParam
     * @return
     */
    public int sendSysAttachMsg(NeteasePushParam neteasePushParam){
        String from=neteasePushParam.getFrom();
        int msgtype=neteasePushParam.getMsgtype();
        String to=neteasePushParam.getTo();
        String pushcontent=neteasePushParam.getPushcontent();
        Payload payload=neteasePushParam.getPayload();
        String payloadStr="";
        if(payload!=null){
            payloadStr=gson.toJson(payload);
        }
        Attach attach=neteasePushParam.getAttach();
        String attachStr=gson.toJson(attach);
        String sound=neteasePushParam.getSound();
        int save=neteasePushParam.getSave();
        Option option=neteasePushParam.getOption();
        String optionStr="";
        if(option!=null){
            optionStr=gson.toJson(option);
        }
        logger.info("sendSysAttachMsg发送系统通知payloadStr="+payloadStr+"&attachStr="+attachStr);
        RubbishRet rubbishRet= null;
        try {
            rubbishRet = erBanNetEaseService.sendSysAttachMsg(from,msgtype, to, attachStr, pushcontent, payloadStr, sound, save, optionStr);
        } catch (Exception e) {
            logger.error("发送自定义系统通知失败，失败code="+rubbishRet.getCode() + ", cause by " + e.getMessage());
        }
        if(rubbishRet.getCode()!=200){
            logger.error("发送自定义系统通知失败，失败code="+rubbishRet.getCode() + ", ret: " + rubbishRet.toString());
        }
        return rubbishRet.getCode();
    }

    /**
     * 批量发送系统通知
     * @param neteasePushBatchParam
     * @return
     */
    public int sendBatchSysAttachMsg(NeteasePushBatchParam neteasePushBatchParam){
        String fromAccid=neteasePushBatchParam.getFromAccid();
        List<String> toAccidsList=neteasePushBatchParam.getToAccids();
        String toAccids=gson.toJson(toAccidsList);
        String pushcontent=neteasePushBatchParam.getPushcontent();
        Payload payload=neteasePushBatchParam.getPayload();
        String payloadStr="";
        if(payload!=null){
            payloadStr=gson.toJson(payload);
        }
        Attach attach=neteasePushBatchParam.getAttach();
        String attachStr=gson.toJson(attach);
        String sound=neteasePushBatchParam.getSound();
        int save=neteasePushBatchParam.getSave();
        Option option=neteasePushBatchParam.getOption();
        String optionStr="";
        if(option!=null){
            optionStr=gson.toJson(option);
        }
        logger.info("sendBatchSysAttachMsg发送系统通知payloadStr="+payloadStr+"&attachStr="+attachStr);
        RubbishRet rubbishRet= null;
        try {
            rubbishRet = erBanNetEaseService.sendBatchSysAttachMsg(fromAccid, toAccids,attachStr, pushcontent, payloadStr,sound, save,optionStr);
        } catch (Exception e) {
            logger.error("批量发送自定义系统通知失败，失败code="+rubbishRet.getCode() + ", cause by " + e.getMessage());
        }
        if(rubbishRet.getCode()!=200){
            logger.error("批量发送自定义系统通知失败，失败code="+rubbishRet.getCode() + ", ret: " + rubbishRet.toString());
        }
        return 0;

    }


    /**
     * 批量点对点普通消息
     * @param neteaseSendMsgBatchParam
     * @return
     */
    public int sendBatchMsgMsg(NeteaseSendMsgBatchParam neteaseSendMsgBatchParam){
        logger.info("批量点对点普通消息,方法入参：{}",JSON.toJSONString(neteaseSendMsgBatchParam));
        String attachStr ="";
        String fromAccid=neteaseSendMsgBatchParam.getFromAccid();
        List<String> toAccidsList=neteaseSendMsgBatchParam.getToAccids();
        String toAccids=gson.toJson(toAccidsList);
        String pushcontent=neteaseSendMsgBatchParam.getPushcontent();
        Payload payload=neteaseSendMsgBatchParam.getPayload();
        String payloadStr="";
        if(payload!=null){
            payloadStr=gson.toJson(payload);
        }

        if(neteaseSendMsgBatchParam.getType()==0 ||neteaseSendMsgBatchParam.getType()==1){//图片或者文字
            String content=neteaseSendMsgBatchParam.getContent();
            JSONObject json =new JSONObject();
            json.put("msg",content);
            attachStr= gson.toJson(json);
        }
        if (neteaseSendMsgBatchParam.getType()==100){//图文
            Body body =neteaseSendMsgBatchParam.getBody();
            attachStr= gson.toJson(body);
        }
        int type=neteaseSendMsgBatchParam.getType();
        Option option=neteaseSendMsgBatchParam.getOption();
        String optionStr="";
        if(option!=null){
            optionStr=gson.toJson(option);
        }
        logger.info("sendBatchMsgMsg批量发送点对点普通消息payloadStr="+payloadStr+"&attachStr="+attachStr);
        RubbishRet rubbishRet= null;
        try {
            rubbishRet = erBanNetEaseService.sendBatchMsg( fromAccid,  toAccids,  type, attachStr,  pushcontent,
                     payloadStr,   optionStr);
        } catch (Exception e) {
            logger.error("批量发送点对点普通消息失败，失败code="+rubbishRet.getCode() + ", cause by " + e.getMessage());
        }
        if(rubbishRet.getCode()!=200){
            logger.error("批量发送点对点普通消息失败，失败code="+rubbishRet.getCode() + ", ret: " + rubbishRet.toString());
        }
        return 0;

    }

    /**
     * 点对点发信息
     * @param neteaseSendMsgParam
     * @return
     */
    public int sendMsg(NeteaseSendMsgParam neteaseSendMsgParam){
        String from =neteaseSendMsgParam.getFrom();
        int ope= neteaseSendMsgParam.getOpe();
        String to =neteaseSendMsgParam.getTo();
        String pushcontent=neteaseSendMsgParam.getPushcontent();
        Payload payload=neteaseSendMsgParam.getPayload();
        String payloadStr="";
        String bodyStr ="";
        if(!StringUtils.isEmpty(payload)){
            payloadStr=gson.toJson(payload);
        }
        String body = neteaseSendMsgParam.getBody();
        JSONObject json =new JSONObject();
        if(neteaseSendMsgParam.getType()==0 ){
            json.put("msg",body);
            bodyStr = gson.toJson(json);
        }else if(neteaseSendMsgParam.getType()== 1){
            Picture picture = neteaseSendMsgParam.getPicture();
            if(!StringUtils.isEmpty(picture)){
                bodyStr = gson.toJson(picture);
            }
        }
        else if(neteaseSendMsgParam.getType()==100){
            Attach attach = neteaseSendMsgParam.getAttach();
            if(!StringUtils.isEmpty(attach)){
                bodyStr = gson.toJson(attach);
            }
        }

        int type = neteaseSendMsgParam.getType();
        Option option=neteaseSendMsgParam.getOption();
        String optionStr="";
        if(option!=null){
            optionStr=gson.toJson(option);
        }
        logger.info("sendMsg发送点对点普通消息payloadStr="+payloadStr+"&attachStr="+bodyStr);
        RubbishRet rubbishRet= null;
        try {
            rubbishRet = erBanNetEaseService.sendMsg( from,ope, to, type, bodyStr,  pushcontent, payloadStr,   optionStr);
        } catch (Exception e) {
            logger.error("发送点对点普通消息失败，失败code="+rubbishRet.getCode() + ", cause by " + e.getMessage());
        }
        if(rubbishRet.getCode()!=200){
            logger.error("发送点对点普通消息失败，失败code="+rubbishRet.getCode());
        }
        return 0;

    }


    /**
     * 发广播消息
     * @param
     * @throws Exception
     */
    public int broadCastMsg(String from,String body){
//        JSONArray array =new JSONArray();
//        array.add("ios");
//        array.add("aos");
//        array.add("pc");
//        array.add("web");
//        array.add("mac");
//        array.add("android");
        GroupMsg group=new GroupMsg();
        group.setBody(body);
//        group.setBroadcastId(UUIDUitl.get());
//        group.setCreateTime(new Date());
//        group.setOffline(true);
//        group.setExpireTime(new Date(new Date().getTime()+60*1000*60*24));
//        group.setTargetOs(array);
        String bodyStr = gson.toJson(group);
//        String bodyStr ="msg:"+bodyStr1;
        /*switch(type){
            case 0:
                String body = neteaseSendMsgParam.getBody();
                json.put("msg",body);
                bodyStr = gson.toJson(json);
                break;
            case 1:
                Picture picture = neteaseSendMsgParam.getPicture();
                if(!StringUtils.isEmpty(picture)){
                    bodyStr = gson.toJson(picture);
                }
            case 100:
                Attach attach = neteaseSendMsgParam.getAttach();
                if(!StringUtils.isEmpty(attach)){
                    bodyStr = gson.toJson(attach);
                }
            }*/
        RubbishRet rubbishRet= null;
        try {
            rubbishRet = erBanNetEaseService.broadcastMsg(from, bodyStr);
        } catch (Exception e) {
            logger.error("发送广播消息失败，失败code="+rubbishRet.getCode() + ", cause by " + e.getMessage());
        }
        if(rubbishRet.getCode()!=200){
            logger.error("发送广播消息失败，失败code="+rubbishRet.getCode());
        }
        return 0;

    }




    public static void main(String args[]) throws Exception{

//        sendMishu();
//        for(int i=0;i<100;i++){
//            sendPacketTest();
//            Thread.sleep(60*1000);
//        }


    }
    public void sendPacket(){}

    public static void sendMishu(){
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
        List<String> toAccids= Lists.newArrayList();
        toAccids.add("90026");
//        toAccids.add("");
        neteaseSendMsgBatchParam.setToAccids(toAccids);
        neteaseSendMsgBatchParam.setType(100);
        neteaseSendMsgBatchParam.setPushcontent("图文推送");
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.PointToPointMsg);
        body.setSecond(Constant.DefMsgType.PushPicWordMsg);
        PicWordMsgAttach picWordMsgAttach=new PicWordMsgAttach();
        picWordMsgAttach.setDesc("图文推送你看不看");
        picWordMsgAttach.setPicUrl("http://nim.nos.netease.com/NDI3OTA4NQ==/bmltd18wXzE1MDY1Mzk4OTgyOTdfYjlmNTlkOWQtMmI2NS00ODhlLTg4OWEtYzlkNzIwNmI5YTQ3");
        picWordMsgAttach.setTitle("图文标题");
        picWordMsgAttach.setWebUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1506550111051&di=efe8d5b4acd00fa45bcd6277dbe6882c&imgtype=0&src=http%3A%2F%2Fimg2.ph.126.net%2FSJB6mcE46weMlzcn6ZbISw%3D%3D%2F6597548054215191722.jpg");
        body.setData(picWordMsgAttach);
        neteaseSendMsgBatchParam.setBody(body);
        Payload payload = new Payload();
        payload.setSkiptype(Constant.PayloadSkiptype.room);
//        payload.setData(uid);
        neteaseSendMsgBatchParam.setPayload(payload);
//        logger.info("uid=" + uid + "发送开播通知，当前关注人数=" + followLsit.size());

        SendSysMsgService sendSysMsgService=new SendSysMsgService();
//        erBanNetEaseService =new ErBanNetEaseService();
        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);

    }

    private static void sendPacketTest(){
        UserPacketRecordVo userPacketRecordVo=new UserPacketRecordVo();
        userPacketRecordVo.setPacketNum(10.00);
        userPacketRecordVo.setCreateTime(new Date());
        userPacketRecordVo.setUid(90666L);
        userPacketRecordVo.setType(Constant.RedPacket.register);
        userPacketRecordVo.setNeedAlert(true);
        userPacketRecordVo.setPacketName("邀请");

        List<String> toAccids= Lists.newArrayList();
        toAccids.add(userPacketRecordVo.getUid().toString());
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
        neteaseSendMsgBatchParam.setToAccids(toAccids);
        neteaseSendMsgBatchParam.setType(100);
        neteaseSendMsgBatchParam.setPushcontent("您获得了一个红包，点击查看");
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.Packet);
        body.setSecond(Constant.DefMsgType.PacketBouns);
        body.setData(userPacketRecordVo);
        neteaseSendMsgBatchParam.setBody(body);
        Payload payload = new Payload();
        neteaseSendMsgBatchParam.setPayload(payload);
        SendSysMsgService sendSysMsgService=new SendSysMsgService();
        sendSysMsgService.erBanNetEaseService =new ErBanNetEaseService();
        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
    }



    public int sendAppleMishu(String uid,String title,String desc,String picUrl,String webUrl){
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
        List<String> toAccids= Lists.newArrayList();
        toAccids.add(uid);
        neteaseSendMsgBatchParam.setToAccids(toAccids);
        neteaseSendMsgBatchParam.setType(100);
        neteaseSendMsgBatchParam.setPushcontent("图文推送");
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.PointToPointMsg);
        body.setSecond(Constant.DefMsgType.PushPicWordMsg);
        PicWordMsgAttach picWordMsgAttach=new PicWordMsgAttach();
        picWordMsgAttach.setDesc(desc);
        picWordMsgAttach.setPicUrl(picUrl);
        picWordMsgAttach.setTitle(title);
        picWordMsgAttach.setWebUrl(webUrl);
        body.setData(picWordMsgAttach);
        neteaseSendMsgBatchParam.setBody(body);
        Payload payload = new Payload();
        payload.setSkiptype(Constant.PayloadSkiptype.room);
        neteaseSendMsgBatchParam.setPayload(payload);
        SendSysMsgService sendSysMsgService=new SendSysMsgService();
        return sendSysMsgService.sendGraphicMsg(neteaseSendMsgBatchParam);
    }


    /**
     * 批量点对点图文消息
     * @param neteaseSendMsgBatchParam
     * @return
     */
    public int sendGraphicMsg(NeteaseSendMsgBatchParam neteaseSendMsgBatchParam){
        erBanNetEaseService = new ErBanNetEaseService();
        logger.info("批量点对点普通消息,方法入参：{}",JSON.toJSONString(neteaseSendMsgBatchParam));
        String attachStr ="";
        String fromAccid=neteaseSendMsgBatchParam.getFromAccid();
        List<String> toAccidsList=neteaseSendMsgBatchParam.getToAccids();
        String toAccids=gson.toJson(toAccidsList);
        String pushcontent=neteaseSendMsgBatchParam.getPushcontent();
        Payload payload=neteaseSendMsgBatchParam.getPayload();
        String payloadStr="";
        if(payload!=null){
            payloadStr=gson.toJson(payload);
        }

        if(neteaseSendMsgBatchParam.getType()==0 ||neteaseSendMsgBatchParam.getType()==1){//图片或者文字
            String content=neteaseSendMsgBatchParam.getContent();
            JSONObject json =new JSONObject();
            json.put("msg",content);
            attachStr= gson.toJson(json);
        }
        if (neteaseSendMsgBatchParam.getType()==100){//图文
            Body body =neteaseSendMsgBatchParam.getBody();
            attachStr= gson.toJson(body);
        }
        int type=neteaseSendMsgBatchParam.getType();
        Option option=neteaseSendMsgBatchParam.getOption();
        String optionStr="";
        if(option!=null){
            optionStr=gson.toJson(option);
        }
        logger.info("sendBatchMsgMsg批量发送点对点普通消息payloadStr="+payloadStr+"&attachStr="+attachStr);
        RubbishRet rubbishRet= null;
        try {
            rubbishRet = erBanNetEaseService.sendBatchMsg( fromAccid,  toAccids,  type, attachStr,  pushcontent,
                    payloadStr,   optionStr);
        } catch (Exception e) {
            logger.error("批量发送点对点普通消息失败，失败code="+rubbishRet.getCode() + ", cause by " + e.getMessage());
        }
        if(rubbishRet.getCode()!=200){
            logger.error("批量发送点对点普通消息失败，失败code="+rubbishRet.getCode() + ", ret: " + rubbishRet.toString());
        }
        return 0;

    }



}
