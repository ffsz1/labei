package com.juxiao.xchat.manager.common.mcoin.impl;


import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.spring.SpringAppContext;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.mcoin.McoinDrawIssueDao;
import com.juxiao.xchat.dao.mcoin.McoinDrawTicketDao;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssueDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssuesDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;

import java.util.List;

/**
 * 抽奖产品类型枚举
 */
public enum IssueItemTypeEnum {

    /**
     * 靓号抽奖类型
     */
    PRETY_NO((byte) 1, "靓号") {
        @Override
        public void sendRewards(McoinDrawIssueDTO issueDto, Long uid) {
            NeteaseMsgBO msgBo = new NeteaseMsgBO();
            msgBo.setFrom(systemConf.getSecretaryUid());
            msgBo.setTo(String.valueOf(uid));
            msgBo.setOpe(0);
            msgBo.setType(0);
            msgBo.setBody(String.format("恭喜你成为第%s期的幸运儿，获得%s", issueDto.getId(), issueDto.getItemName()));
            neteaseMsgManager.sendMsg(msgBo);

            UsersDTO usersDto = usersManager.getUser(uid);
            if (usersDto != null) {
                this.sendUnRewards(issueDto.getId(), uid, String.format("点点币竞猜开奖啦！恭喜%s欧皇附体，获得了%s！", usersDto.getNick(), issueDto.getItemName()));
                //下一期活动开始发小秘书
                sendFullMsg((byte)1);
            }
        }
    },

    /**
     * 抽奖得座驾
     */
    GIFT_CAR((byte) 2, "座驾") {
        @Override
        public void sendRewards(McoinDrawIssueDTO issueDto, Long uid) {
            GiftCarManager giftCarManager = SpringAppContext.getBean(GiftCarManager.class);
            if (giftCarManager == null) {
                return;
            }

            GiftCarDTO giftcarDto = giftCarManager.getGiftCar(issueDto.getItemId());
            if (giftcarDto == null) {
                return;
            }

            int carDate = 7;
            StringBuilder message = new StringBuilder();
            message.append("恭喜你成为第").append(issueDto.getId());
            message.append("期的幸运儿，获得限量座驾“").append(giftcarDto.getCarName());
            message.append("” * ").append(carDate).append("日");
            giftCarManager.saveUserCar(uid, issueDto.getItemId(), carDate, 5, message.toString());

            UsersDTO usersDto = usersManager.getUser(uid);
            if (usersDto != null) {
                this.sendUnRewards(issueDto.getId(), uid, String.format("点点币竞猜开奖啦！恭喜%s欧皇附体，获得了限量版点点币专属座驾%s*%s天！", usersDto.getNick(), giftcarDto.getCarName(), carDate));
                //下一期活动开始发小秘书
                sendFullMsg((byte)2);
            }
        }
    },

    /**
     * 抽奖得头饰
     */
    HEADWEAR((byte) 3, "头饰") {
        @Override
        public void sendRewards(McoinDrawIssueDTO issueDto, Long uid) {
            HeadwearManager headwearManager = SpringAppContext.getBean(HeadwearManager.class);
            if (headwearManager == null) {
                return;
            }

            HeadwearDTO headwearDto = headwearManager.getHeadwear(issueDto.getItemId());
            if (headwearDto == null) {
                return;
            }
            int headwearDate = 7;
            StringBuilder message = new StringBuilder();
            message.append("恭喜你成为第").append(issueDto.getId());
            message.append("期的幸运儿，获得限量头饰“").append(headwearDto.getHeadwearName());
            message.append("” * ").append(headwearDate).append("日");
            headwearManager.saveUserHeadwear(uid, issueDto.getItemId(), headwearDate, 5, message.toString());

            UsersDTO usersDto = usersManager.getUser(uid);
            if (usersDto != null) {
                this.sendUnRewards(issueDto.getId(), uid, String.format("点点币竞猜开奖啦！恭喜%s欧皇附体，获得了限量版点点币专属头饰%s*%s天！", usersDto.getNick(), headwearDto.getHeadwearName(), headwearDate));
                //下一期活动开始发小秘书
                sendFullMsg((byte)3);
            }
        }
    },;

    private Byte itemType;
    private String itemTypeName;
    protected SystemConf systemConf;
    protected NetEaseMsgManager neteaseMsgManager;
    protected UsersManager usersManager;
    protected McoinDrawIssueDao mcoinDrawIssueDao;
    protected AsyncNetEaseTrigger msgPushManager;

    IssueItemTypeEnum(byte itemType, String itemTypeName) {
        this.itemType = itemType;
        this.itemTypeName = itemTypeName;
        this.systemConf = SpringAppContext.getBean(SystemConf.class);
        this.neteaseMsgManager = SpringAppContext.getBean(NetEaseMsgManager.class);
        this.usersManager = SpringAppContext.getBean(UsersManager.class);
        this.mcoinDrawIssueDao = SpringAppContext.getBean(McoinDrawIssueDao.class);
        this.msgPushManager = SpringAppContext.getBean(AsyncNetEaseTrigger.class);
    }

    /**
     * 发送奖励
     *
     * @param issueDto
     */
    public abstract void sendRewards(McoinDrawIssueDTO issueDto, Long uid);

    /**
     * @param issueId
     * @param rewardUid
     * @param message
     */
    protected void sendUnRewards(Long issueId, Long rewardUid, String message) {
        //查询所有参与用户
        McoinDrawTicketDao mcoinDrawTicketDao = SpringAppContext.getBean(McoinDrawTicketDao.class);
        List<Long> issueUsers = mcoinDrawTicketDao.listIssueUsers(issueId);

        issueUsers.forEach(item -> {
            if (item == null) {
                return;
            }

            RedisManager redisManager = SpringAppContext.getBean(RedisManager.class);
            if (redisManager != null) {
                redisManager.hincrBy(RedisKey.mcoin_draw_user_issue_hash.getKey(), String.valueOf(item), 1L);
            }

            if (item.equals(rewardUid)) {
                return;
            }

            // 发送消息给用户
            NeteaseMsgBO msgBo = new NeteaseMsgBO();
            msgBo.setFrom(systemConf.getSecretaryUid());
            msgBo.setTo(String.valueOf(item));
            msgBo.setOpe(0);
            msgBo.setType(0);
            msgBo.setBody(message);
            neteaseMsgManager.sendMsg(msgBo);
        });
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public static IssueItemTypeEnum itemTypeOf(Byte itemType) {
        if (itemType == null) {
            return null;
        }

        for (IssueItemTypeEnum typeName : IssueItemTypeEnum.values()) {
            if (typeName.itemType == itemType.byteValue()) {
                return typeName;
            }
        }
        return null;
    }

    public void sendFullMsg(Byte itemType){
        //下一期活动开始发小秘书
        McoinDrawIssuesDTO drawIssuesDTO =  mcoinDrawIssueDao.findNewIssues(itemType);
        if (null != drawIssuesDTO && drawIssuesDTO.getPushMsgStatus().equals((byte)0)){
            mcoinDrawIssueDao.updatePushMsgStatus(drawIssuesDTO.getIssueId(),(byte)1);
            NeteaseBatchMsgBO neteaseSendMsgBatchParam = new NeteaseBatchMsgBO();
            neteaseSendMsgBatchParam.setFromAccid(systemConf.getSecretaryUid());
            //10
            StringBuilder sb = new StringBuilder("点点币竞猜第");
            sb.append(drawIssuesDTO.getIssueId());
            sb.append("期-");
            sb.append(drawIssuesDTO.getItemTypeName());
            sb.append("，快去任务中心-点点币竞猜赢取你的专属");
            sb.append(drawIssuesDTO.getItemTypeName());
            neteaseSendMsgBatchParam.setContent(sb.toString());
            msgPushManager.sendGroupMsg(neteaseSendMsgBatchParam);
        }
    }
}
