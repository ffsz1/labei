
package com.erban.admin.main.service;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.config.SystemConfig;
import com.erban.main.dto.UsersDTO;
import com.erban.main.model.*;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.MsgPushRecordMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.mybatismapper.UsersMapperExpend;
import com.erban.main.param.neteasepush.*;
import com.erban.main.service.SendSysMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.common.utils.PropertyUtil;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountLoginRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 拉贝小秘书给前台发消息
 * Created by fxw on 2017/12/29.
 */
@Service
public class MsgPushAdminService extends BaseService {
    /**
     * 需要过滤的账号
     */
    private final List<String> FILTER_UID_LIST = Lists.newArrayList("500092");

    @Autowired
    private MsgPushRecordMapper msgPushRecordMapper;

    @Autowired
    private UsersMapperExpend usersMapperExpend;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    @Autowired
    private AccountLoginRecordMapper accountLoginRecordMapper;

    MsgPushRecord msgPushRecord = new MsgPushRecord();

    /**
     * 获取消息列表
     *
     * @param pageNumber
     * @param pageSize
     * @param msgType
     * @param erbanNos
     * @return
     */
    public PageInfo<MsgPushRecord> getMsgList(Integer pageNumber, Integer pageSize, Byte msgType, String erbanNos) {
        MsgPushRecordExample msgPushRecordExample = new MsgPushRecordExample();
        msgPushRecordExample.setOrderByClause("crate_time DESC");
        MsgPushRecordExample.Criteria criteria = msgPushRecordExample.createCriteria();

        if (!StringUtils.isEmpty(erbanNos)) {
            criteria.andToErbanNosLike("%" + erbanNos + "%");
        }

        if (!StringUtils.isEmpty(msgType)) {
            criteria.andMsgTypeEqualTo(msgType);
        }

        PageHelper.startPage(pageNumber, pageSize);
        List<MsgPushRecord> MsgPushRecordList = msgPushRecordMapper.selectByExample(msgPushRecordExample);
        return new PageInfo<>(MsgPushRecordList);
    }

    /**
     * 删除正在发送消息的标记
     *
     * @return
     */
    public BusiResult deleteFlag() {
        //
        jedisService.del(RedisKey.push_message_flag.getKey());
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     * 保存信息
     *
     * @param erbanNos
     * @param msgType
     * @param toObjType
     * @param words
     * @param pic
     * @param title
     * @param desc
     * @param picUrl
     * @param webUrl
     * @param skipType
     * @param adminId
     * @param skipContent
     * @return
     */
    public BusiResult saveMsg(String erbanNos, Byte msgType, Byte toObjType, String words, String pic, String title,
                              String desc, String picUrl, String webUrl, Byte skipType, String adminId,
                              String skipContent) {
        try {
            String status = jedisService.get(RedisKey.push_message_flag.getKey());
            if (!StringUtils.isEmpty(status)) {
                return new BusiResult(BusiStatus.SERVERERROR, "有消息正在发送, 请稍后再试...", status);
            }
            jedisService.set(RedisKey.push_message_flag.getKey(), DateUtil.date2Str(new Date(),
                    DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS));
            //默认为拉贝小秘书uid
            msgPushRecord.setFromAccid(Long.parseLong(SystemConfig.secretaryUid));
            msgPushRecord.setMsgType(msgType);
            msgPushRecord.setToObjType(toObjType);
            msgPushRecord.setToErbanNos(erbanNos);
            msgPushRecord.setCrateTime(new Date());
            msgPushRecord.setAdminId(adminId);
            msgPushRecord.setSkipType(skipType);
            msgPushRecord.setSkipUri(skipContent);
            StringBuffer sbf = new StringBuffer();
            if (!StringUtils.isEmpty(erbanNos)) {
                if (erbanNos.contains(",")) {
                    String[] split = erbanNos.split(",");
                    for (String erbanNo : split) {
                        Long uid = erbanNoToUid(erbanNo);
                        if (uid != null && FILTER_UID_LIST.contains(uid.toString())) {
                            // 过滤这个账号, IOS 审核账号
                            break;
                        }
                        sbf.append(uid).append(",");
                        String a = sbf.toString();
                        msgPushRecord.setToAccids(a.substring(0, a.length() - 1));
                    }
                } else {
                    String uid = erbanNoToUid(erbanNos).toString();
                    if (uid != null && FILTER_UID_LIST.contains(uid)) {
                        // 过滤这个账号
                        return new BusiResult(BusiStatus.SERVERERROR, "不能给 IOS 审核账号,发送消息", "");
                    }
                    msgPushRecord.setToAccids(uid);
                }
                if (StringUtils.isEmpty(msgPushRecord.getToAccids())) {
                    return new BusiResult(BusiStatus.SERVERERROR, "请选择一个用户", "");
                }
            } else {
                msgPushRecord.setToAccids("0");
            }

            saveMsgType(msgType, words, pic, title, desc, picUrl, webUrl);//存消息类型
            sendMsgType(msgPushRecord);//选消息类型进行发送
            if (title != null && title.length() > 20) {
                // 限制字符
                msgPushRecord.setTitle(title.substring(0, 20));
            }
            msgPushRecordMapper.insert(msgPushRecord);
        } catch (Exception e) {
            logger.info("推送消息失败", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        } finally {
            // 删除正在发送消息的标记
            jedisService.del(RedisKey.push_message_flag.getKey());
        }
        //
        return new BusiResult(BusiStatus.SUCCESS);
    }

    //保存消息类型
    public void saveMsgType(Byte msgType, String words, String pic, String title, String desc, String picUrl,
                            String webUrl) {
        if (!StringUtils.isEmpty(msgType)) {
            msgPushRecord.setMsgType(msgType);
            switch (msgType) {
                case 0://文字消息
                    msgPushRecord.setMsgDesc(words);
                    break;
                case 1://图片消息
                    msgPushRecord.setPicUrl(pic);
                    break;
                case 100://图文消息
                    msgPushRecord.setTitle(title);
                    msgPushRecord.setMsgDesc(desc);
                    msgPushRecord.setPicUrl(picUrl);
                    if (!StringUtils.isEmpty(webUrl)) {
                        msgPushRecord.setWebUrl(webUrl);
                    }
                    break;
            }
        } else {
            logger.error("参数错误");
        }

    }

    public void sendMsgType(MsgPushRecord msgPushRecord) {
        byte type = msgPushRecord.getMsgType();
        if (type == 0 || type == 1) {//发文本或者图片
            sendSysWordsOrPic(msgPushRecord);
        } else if (type == 100) {//发图文
            sendWordsAndPics(msgPushRecord);
        }
    }


    //发文字或者图片
    public void sendSysWordsOrPic(MsgPushRecord msgPushRecord) {
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgBatchParam.setFromAccid(String.valueOf(msgPushRecord.getFromAccid()));
        String uidListStr = msgPushRecord.getToAccids();
        byte type = msgPushRecord.getMsgType();
        Byte toObjType = msgPushRecord.getToObjType();
        List<String> uidList = new ArrayList<>();
        String[] uidArray = new String[100];

        if (!StringUtils.isEmpty(toObjType) && toObjType == 0) {//点对点
            if (uidListStr.contains(",")) {//一对多发文字或图片
                uidArray = uidListStr.split(",");
                for (String uid : uidArray) {
                    uidList.add(uid);
                    if (type == 0) {
                        neteaseSendMsgBatchParam.setContent(msgPushRecord.getMsgDesc());
                    } else if (type == 1) {
                        neteaseSendMsgBatchParam.setContent(msgPushRecord.getPicUrl());
                    } else {
                        logger.error("参数不正确");
                    }
                    neteaseSendMsgBatchParam.setToAccids(uidList);
                    Payload payload = new Payload();
                    int skip = msgPushRecord.getSkipType();
                    payload.setSkiptype(skip);//1跳app页面，2跳聊天室，3跳h5页面
                    payload.setData(msgPushRecord.getSkipUri());
                    neteaseSendMsgBatchParam.setPayload(payload);
                }
                sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
            } else {//一对一发文字或图片
                neteaseSendMsgParam.setTo(uidListStr);
                neteaseSendMsgParam.setFrom(msgPushRecord.getFromAccid().toString());
                neteaseSendMsgParam.setType(type);
                if (type == 0) {
                    neteaseSendMsgParam.setBody(msgPushRecord.getMsgDesc());
                } else if (type == 1) {
                    Picture picture = new Picture();
                    picture.setName("图片");
                    picture.setExt("jpg");
                    picture.setMd5(String.valueOf(new Date().getTime()));
                    picture.setUrl(msgPushRecord.getPicUrl());
                    picture.setH(6814);
                    picture.setW(2332);
                    picture.setSize(388245);
                    neteaseSendMsgParam.setPicture(picture);
                }
                neteaseSendMsgParam.setOpe(toObjType);
                Payload payload = new Payload();
                payload.setSkiptype(msgPushRecord.getSkipType());
                payload.setData(msgPushRecord.getSkipUri());
                neteaseSendMsgParam.setPayload(payload);
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
            }

        }
        /**
         * 1、分页查询，每次查询50个uid；
         * 2、循环处理，每次50，累计满5次，休眠10s；
         */
        if (!StringUtils.isEmpty(toObjType) && toObjType == 1) {//群发消息(高级群)
            sendGroupMsg(neteaseSendMsgBatchParam);
            //sendSysMsgService. broadCastMsg(msgPushRecord.getFromAccid().toString(),msgPushRecord.getMsgDesc());
        }
    }

    //发送自定义消息，图文消息
    public void sendWordsAndPics(MsgPushRecord msgPushRecord) {
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
        String uidListStr = msgPushRecord.getToAccids();
        List<String> uidList = new ArrayList<>();
        String[] uidArray = new String[100];
        if (msgPushRecord.getToObjType() == 0) {
            if (uidListStr.contains(",")) {//一对多发图文
                uidArray = uidListStr.split(",");
                for (String uid : uidArray) {
                    uidList.add(uid);
                    neteaseSendMsgBatchParam.setToAccids(uidList);
                }
                sendBatchPicAndWords(neteaseSendMsgBatchParam);
                sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
            } else {//一对一发图文
                NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                neteaseSendMsgParam.setFrom(msgPushRecord.getFromAccid().toString());
                neteaseSendMsgParam.setTo(uidListStr);
                neteaseSendMsgParam.setOpe(msgPushRecord.getToObjType());
                neteaseSendMsgParam.setType(msgPushRecord.getMsgType());
                Attach attach = new Attach();
                attach.setFirst(Constant.DefMsgType.PointToPointMsg);
                attach.setSecond(Constant.DefMsgType.PushPicWordMsg);
                attach.setData(msgPushRecord.getMsgDesc());
                neteaseSendMsgParam.setAttach(attach);
                PicWordMsgAttach picWordMsgAttach = new PicWordMsgAttach();
                picWordMsgAttach.setDesc(msgPushRecord.getMsgDesc());
                picWordMsgAttach.setPicUrl(msgPushRecord.getPicUrl());
                picWordMsgAttach.setTitle(msgPushRecord.getTitle());
                if (msgPushRecord.getWebUrl() != null) {
                    picWordMsgAttach.setWebUrl(msgPushRecord.getWebUrl());
                }
                if (msgPushRecord.getSkipUri() != null) {
                    picWordMsgAttach.setWebUrl(msgPushRecord.getSkipUri());
                }
                attach.setData(picWordMsgAttach);
                Payload payload = new Payload();
                payload.setSkiptype(msgPushRecord.getSkipType());
                payload.setData(msgPushRecord.getSkipUri());
                neteaseSendMsgParam.setPayload(payload);
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
            }
        }
        if (msgPushRecord.getToObjType() == 1) {
            //群发
            sendGroupMsg(neteaseSendMsgBatchParam);
            //sendSysMsgService. broadCastMsg(msgPushRecord.getFromAccid().toString(),msgPushRecord.getMsgDesc());
        }

    }

    //群发图文
    public void sendBatchPicAndWords(NeteaseSendMsgBatchParam neteaseSendMsgBatchParam) {
        neteaseSendMsgBatchParam.setType(msgPushRecord.getMsgType());
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.PointToPointMsg);
        body.setSecond(Constant.DefMsgType.PushPicWordMsg);
        PicWordMsgAttach picWordMsgAttach = new PicWordMsgAttach();
        picWordMsgAttach.setDesc(msgPushRecord.getMsgDesc());
        picWordMsgAttach.setPicUrl(msgPushRecord.getPicUrl());
        picWordMsgAttach.setTitle(msgPushRecord.getTitle());
        if (msgPushRecord.getWebUrl() != null) {
            picWordMsgAttach.setWebUrl(msgPushRecord.getWebUrl());
        }
        if (msgPushRecord.getSkipUri() != null) {
            picWordMsgAttach.setWebUrl(msgPushRecord.getSkipUri());
        }
        // picWordMsgAttach.setWebUrl(msgPushRecord.getWebUrl());
        body.setData(picWordMsgAttach);
        neteaseSendMsgBatchParam.setBody(body);
        Payload payload = new Payload();
        payload.setSkiptype(msgPushRecord.getSkipType());
        payload.setData(msgPushRecord.getSkipUri());
        neteaseSendMsgBatchParam.setPayload(payload);
    }


    //群发文字或者图片
    public void sendGroupMsg(NeteaseSendMsgBatchParam neteaseSendMsgBatchParam) {
        neteaseSendMsgBatchParam.setFromAccid(msgPushRecord.getFromAccid().toString());
        int leiXin = msgPushRecord.getMsgType();
        if (leiXin == 0) {
            neteaseSendMsgBatchParam.setContent(msgPushRecord.getMsgDesc());
            Payload payload = new Payload();
            payload.setSkiptype(msgPushRecord.getSkipType());//1跳app页面，2跳聊天室，3跳h5页面
            neteaseSendMsgBatchParam.setPayload(payload);
        } else if (leiXin == 1) {
            neteaseSendMsgBatchParam.setContent(msgPushRecord.getPicUrl());
            Payload payload = new Payload();
            payload.setSkiptype(msgPushRecord.getSkipType());//1跳app页面，2跳聊天室，3跳h5页面
            payload.setData(msgPushRecord.getSkipUri());
            neteaseSendMsgBatchParam.setPayload(payload);
        } else if (leiXin == 100) {
            sendBatchPicAndWords(neteaseSendMsgBatchParam);
        }

        // 查询活跃用户
        String dateStr = DateUtil.date2Str(DateUtil.addDay(new Date(), -60), DateUtil.DateFormat.YYYY_MM_DD);
        List<Long> uidList;
        List<String> temp = Lists.newArrayList();
        int pageNo = 0;
        int pageSize = 500;
        int total = 0;
        while (true) {
            // 过滤这个账号
            uidList = accountLoginRecordMapper.pageUid(dateStr, pageNo * pageSize, pageSize);
            if (uidList == null) {
                break;
            }
            for (Long uid : uidList) {
                if (uid == null) {
                    break;
                }
                if (!FILTER_UID_LIST.contains(uid.toString())) {
                    temp.add(uid.toString());
                    total++;
                }
            }
            neteaseSendMsgBatchParam.setToAccids(temp);
            sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
            pageNo++;
            logger.info("[群发消息]第{}次,每次{}人", pageNo, pageSize);
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
                    logger.error("[群发消息],线程休眠失败,pageNo:" + pageNo, e);
                }
            }
        }
        logger.info("[群发消息],共发送了" + pageNo + "次信息,每次" + pageSize + "条信息,共:" + total);
    }


    private Long erbanNoToUid(String erBanNo) {
        Long uids = 0l;
        if (!StringUtils.isEmpty(erBanNo)) {
            UsersExample example = new UsersExample();
            long longUid = Long.parseLong(erBanNo);
            example.createCriteria().andErbanNoEqualTo(longUid);
            List<Users> usersList = usersMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(usersList)) {
                uids = usersList.get(0).getUid();
            }
        } else {
            logger.error("参数错误");
        }
        return uids;
    }


}

