package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfDrawActSwitch;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.UserDrawDao;
import com.juxiao.xchat.dao.user.UserDrawRecordDao;
import com.juxiao.xchat.dao.user.domain.UserDrawDO;
import com.juxiao.xchat.dao.user.domain.UserDrawRecordDO;
import com.juxiao.xchat.dao.user.dto.UserDrawDTO;
import com.juxiao.xchat.dao.user.enumeration.UserDrawRecordStatus;
import com.juxiao.xchat.dao.user.enumeration.UserDrawRecordType;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UserDrawManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @class: UserDrawManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
@Service
public class UserDrawManagerImpl implements UserDrawManager {
    private final Logger logger = LoggerFactory.getLogger(UserDrawManager.class);
    @Autowired
    private UserDrawDao userDrawDao;
    @Autowired
    private UserDrawRecordDao userDrawRecordDao;
    @Autowired
    private SysConfManager sysconfManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;
    @Autowired
    private Gson gson;

    @Override
    public void saveChargeDraw(Long uid, Integer amount, String chargeId) {
//        saveChargeDraw(uid, UserDrawRecordType.CHARGE, amount, chargeId, "充值" + (amount / 100) + "元");
    }

    @Override
    public void saveChargeDraw(Long uid, UserDrawRecordType userDrawRecordType, Integer amount, String srcObjId, String srcObjName) {
        SysConfDTO sysConf = sysconfManager.getSysConf(SysConfigId.draw_act_switch);
        //开关关闭，不生成抽奖机会
        if (sysConf != null && !SysConfDrawActSwitch.isOpen(sysConf.getConfigValue())) {
            return;
        }
        if (userDrawRecordType == UserDrawRecordType.CHARGE) {
            //少于8元的没有抽奖机会
            if(amount < 3000L){
                return;
            }
        }
        UserDrawDO userDrawDo = new UserDrawDO();
        UserDrawRecordDO drawRecordDo = new UserDrawRecordDO();
        try {
            UserDrawDTO userDrawDto = this.getUserDraw(uid);
            userDrawDo.setUid(userDrawDto.getUid());

            userDrawDo.setLeftDrawNum(userDrawDto.getLeftDrawNum() + 1);
            userDrawDo.setTotalDrawNum(userDrawDto.getTotalDrawNum() + 1);
            this.updateUserDraw(userDrawDo);

            drawRecordDo = saveUserDrawRecord(uid, userDrawRecordType, amount, srcObjId, srcObjName);

            asyncNetEaseTrigger.sendMsg(String.valueOf(uid),"您获得了一次抽奖机会，快点去首页-幸运转盘抽奖吧！");
        } catch (Exception e) {
            logger.error("[ 充值抽奖机会 ]保存记录异常，userDrawDo:>{}，drawRecordDo:>{}", userDrawDo, drawRecordDo, e);
        }
    }


    @Override
    public UserDrawRecordDO saveUserDrawRecord(Long uid, UserDrawRecordType userDrawRecordType, Integer amount, String srcObjId, String srcObjName){

        SysConfDTO sysConf = sysconfManager.getSysConf(SysConfigId.draw_act_switch);
        if (sysConf != null && !SysConfDrawActSwitch.isOpen(sysConf.getConfigValue())) {//开关关闭，不生成抽奖机会
            return null;
        }
        UserDrawRecordDO drawRecordDo = new UserDrawRecordDO();
        drawRecordDo.setUid(uid);
        drawRecordDo.setType(userDrawRecordType.getValue());
        if (amount != null) {
            drawRecordDo.setSrcObjAmount(amount.longValue());
        }
        drawRecordDo.setDrawStatus(UserDrawRecordStatus.CREATE.getValue());
        if (srcObjId != null) {
            drawRecordDo.setSrcObjId(srcObjId);
        }
        if (srcObjName != null) {
            drawRecordDo.setSrcObjName(srcObjName);
        }
        drawRecordDo.setCreateTime(new Date());
        userDrawRecordDao.save(drawRecordDo);
        return drawRecordDo;
    }


    @Override
    public void updateUserDraw(UserDrawDO drawDo) {
        userDrawDao.update(drawDo);
        UserDrawDTO drawDto = this.getUserDraw(drawDo.getUid());
        if (drawDo.getLeftDrawNum() != null) {
            drawDto.setLeftDrawNum(drawDo.getLeftDrawNum());
        }
        if (drawDo.getTotalDrawNum() != null) {
            drawDto.setTotalDrawNum(drawDo.getTotalDrawNum());
        }

        if (drawDo.getTotalWinDrawNum() != null) {
            drawDto.setTotalWinDrawNum(drawDo.getTotalWinDrawNum());
        }

        if (drawDo.getIsFirstShare() != null) {
            drawDto.setIsFirstShare(drawDo.getIsFirstShare());
        }

        if (drawDo.getUpdateTime() != null) {
            drawDto.setUpdateTime(drawDo.getUpdateTime());
        }
        redisManager.hset(RedisKey.user_draw.getKey(), String.valueOf(drawDo.getUid()), gson.toJson(drawDto));
    }

    @Override
    public UserDrawDTO getUserDraw(Long uid) {
        String userDrawStr = redisManager.hget(RedisKey.user_draw.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(userDrawStr)) {
            return gson.fromJson(userDrawStr, UserDrawDTO.class);
        }

        UserDrawDTO userDraw = userDrawDao.getUserDraw(uid);
        if (userDraw == null) {
            Date date = new Date();
            UserDrawDO drawDo = new UserDrawDO();
            drawDo.setUid(uid);
            drawDo.setLeftDrawNum(0);
            drawDo.setTotalDrawNum(0);
            drawDo.setIsFirstShare(false);
            drawDo.setTotalWinDrawNum(0);
            drawDo.setCreateTime(date);
            drawDo.setUpdateTime(date);
            userDrawDao.save(drawDo);

            userDraw = new UserDrawDTO();
            BeanUtils.copyProperties(drawDo, userDraw);
        }
        redisManager.hset(RedisKey.user_draw.getKey(), String.valueOf(uid), gson.toJson(userDraw));
        return userDraw;
    }



    /**
     * 首次充值获取抽奖机会-- 内部会对金额进行判断
     *
     * @param uid uid
     * @param amount 金额
     * @param chargeId ping++ chargeId
     */
    @Override
    public void saveFirstChargeDraw(Long uid, Integer amount, String chargeId) {
        UserDrawDO userDrawDo = new UserDrawDO();
        UserDrawRecordDO drawRecordDo = new UserDrawRecordDO();
        try {
            UserDrawDTO userDrawDto = this.getUserDraw(uid);
            userDrawDo.setUid(userDrawDto.getUid());
            userDrawDo.setTotalDrawNum(userDrawDto.getTotalDrawNum() + 1);
            userDrawDo.setLeftDrawNum(userDrawDto.getLeftDrawNum() + 1);
            this.updateUserDraw(userDrawDo);
            drawRecordDo = saveUserDrawRecord(uid, UserDrawRecordType.CHARGE, amount, chargeId, "充值" + (amount / 100) + "元");
            asyncNetEaseTrigger.sendMsg(String.valueOf(uid),"您获得了一次抽奖机会，快点去首页-幸运转盘抽奖吧！");
        } catch (Exception e) {
            logger.error("[ 首次充值获取抽奖机会- ]保存记录异常，userDrawDo:>{}，drawRecordDo:>{}", userDrawDo, drawRecordDo, e);
        }
    }
}
