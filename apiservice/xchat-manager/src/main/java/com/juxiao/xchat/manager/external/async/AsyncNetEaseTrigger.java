package com.juxiao.xchat.manager.external.async;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.dao.sysconf.enumeration.NetEaseMsgEventType;
import com.juxiao.xchat.dao.user.AccountLoginRecordDao;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *  异步处理发送小秘书消息
 */
@Slf4j
@Component
public class AsyncNetEaseTrigger {

    @Autowired
    private SystemConf systemConf;

    @Autowired
    private NetEaseMsgManager netEaseMsgManager;

    private final List<String> FILTER_UID_LIST = Lists.newArrayList("500092", "500092");

    @Autowired
    private AccountLoginRecordDao accountLoginRecordDao;

    @Autowired
    private Gson gson;

    /**
     *  异步发送小秘书消息
     * @param uid uid
     * @param msg msg
     */
    @Async
    public void sendMsg(String uid,String msg){
        NeteaseMsgBO msgBo = new NeteaseMsgBO();
        msgBo.setFrom(systemConf.getSecretaryUid());
        msgBo.setOpe(0);
        msgBo.setType(0);
        msgBo.setTo(uid);
        msgBo.setBody(msg);
        netEaseMsgManager.sendMsg(msgBo);
    }

    /**
     *  异步发送消息
     * @param fromUid 发送人
     * @param uid 接送人
     * @param msg 信息
     */
    @Async
    public void sendMsg(String fromUid,String uid,String msg){
        NeteaseMsgBO msgBo = new NeteaseMsgBO();
        msgBo.setFrom(fromUid);
        msgBo.setOpe(0);
        msgBo.setType(0);
        msgBo.setTo(uid);
        msgBo.setBody(msg);
        netEaseMsgManager.sendMsg(msgBo);
    }
    /**
     * 群发消息（暂用于萌币pk和萌币竞猜发全服）
     * @param neteaseSendMsgBatchParam
     */
    public void sendGroupMsg(NeteaseBatchMsgBO neteaseSendMsgBatchParam) {

        // 查询活跃用户
        String dateStr = date2Str(addDay(new Date(), -60), DateFormat.YYYY_MM_DD);
        List<Long> uidList;
        List<String> temp = Lists.newArrayList();
        int pageNo = 0;
        int pageSize = 500;
        int total = 0;
        while (true) {
            // 过滤这个账号
            uidList = accountLoginRecordDao.pageUid(dateStr, pageNo * pageSize, pageSize);
            if (uidList == null) {
                break;
            }
            for (Long uid : uidList) {
                if (uid == null || FILTER_UID_LIST.contains(uid.toString())) {
                    continue;
                }
                temp.add(uid.toString());
                total++;
            }
            neteaseSendMsgBatchParam.setToAccids(temp);
            netEaseMsgManager.sendBatchMsg(neteaseSendMsgBatchParam);
            pageNo++;
            log.info("[ 群发消息 ]第{}次发送对象：{}", pageNo, JSON.toJSONString(temp));
            if (uidList.size() < pageSize) {
                // 不够一页的数量, 表示最后一页
                break;
            }
            temp.clear();
            // 限制发送频率
            if (pageNo % 10 == 0) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    log.error("[群发消息],线程休眠失败,pageNo:" + pageNo, e);
                }
            }
        }
        log.info("[群发消息],共发送了pageNo:>{}次信息,每次pageSize:>{}条信息,共total:>{}", pageNo ,pageSize, total);
    }

    public static String date2Str(Date date, DateFormat format) {
        return format.getFormat().format(date);
    }

    public enum DateFormat {
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),

        YYYY_MM_DD("yyyy-MM-dd");

        private SimpleDateFormat format;

        DateFormat(String format) {
            this.format = new SimpleDateFormat(format);
            this.format.setLenient(false);
        }

        public SimpleDateFormat getFormat() {
            return format;
        }
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 异步发送图文消息（暂时使用于带链接的纯文字消息，待优化）
     * 参考：com.erban.admin.main.service.MsgPushAdminService#sendWordsAndPics
     * @param skipType  Payload.SkipType.H5
     */
    @Async
    public void sendPicAndWordMsg(String uid, String title, String msg, int skipType, String skipUrl){
        NeteaseMsgBO msgBo = new NeteaseMsgBO();
        Payload payload = new Payload();
        payload.setSkiptype(skipType);     //Payload.SkipType.H5或其他
        payload.setData(skipUrl);
        msgBo.setPayload(payload);

        msgBo.setFrom(systemConf.getSecretaryUid());
        msgBo.setOpe(0);
        msgBo.setType(100);
        msgBo.setTo(uid);
//        msgBo.setBody(msg);

        Attach attach = new Attach();
        attach.setFirst(10);  // admin后台项目用的是这个：Constant.DefMsgType.PointToPointMsg，值是10
        attach.setSecond(101);  // admin后台项目用的是这个：Constant.DefMsgType.PushPicWordMsg，值是101
        PicWordMsgAttach picWordMsgAttach = new PicWordMsgAttach();
        picWordMsgAttach.setDesc(msg);
//        picWordMsgAttach.setPicUrl(msgPushRecord.getPicUrl());
        picWordMsgAttach.setTitle(title);
//        if(msgPushRecord.getWebUrl()!=null){
//            picWordMsgAttach.setWebUrl(msgPushRecord.getWebUrl());
//        }
        if(StringUtils.isNotBlank(skipUrl)){
            String webUrl = systemConf.getServerDomain() + skipUrl;
            picWordMsgAttach.setWebUrl(webUrl);
        }

        attach.setData(picWordMsgAttach);

        msgBo.setAttach(attach);

        log.info("发送图文消息：> {}", gson.toJson(msgBo));
        netEaseMsgManager.sendMsg(msgBo);
    }

    @Async
    public void sendWordMsg(String uid, int toObjType, int msgType, String msg){
        if (toObjType == 1) {
            NeteaseBatchMsgBO neteaseBatchMsgBO = new NeteaseBatchMsgBO();
            neteaseBatchMsgBO.setFromAccid(systemConf.getSecretaryUid());
            neteaseBatchMsgBO.setContent(msg);

            Payload payload = new Payload();
            payload.setSkiptype(Payload.SkipType.APPPAGE);//1跳app页面，2跳聊天室，3跳h5页面
            neteaseBatchMsgBO.setPayload(payload);
            this.sendGroupMsg(neteaseBatchMsgBO);
        }
        else {
            NeteaseMsgBO msgBo = new NeteaseMsgBO();

            msgBo.setFrom(systemConf.getSecretaryUid());
            msgBo.setOpe(toObjType);
            msgBo.setType(msgType);
            msgBo.setTo(uid);

            msgBo.setBody(msg);

            log.info("发送全服文本消息：> {}", gson.toJson(msgBo));
            netEaseMsgManager.sendMsg(msgBo);
        }

    }
}
