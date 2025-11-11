package com.juxiao.xchat.service.api.user.draw.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.enumeration.UserWordDrawWordType;
import org.springframework.stereotype.Service;

/**
 * 扭蛋
 */
@Service("niuDanWordDraw")
public class NiuDanWordDrawImpl extends WordBaseDraw {

    @Override
    protected String getName() {
        return "扭蛋大转盘";
    }

    @Override
    protected String lock(Long uid) {
        return RedisKey.niudan_word_draw.getKey(uid.toString());
    }

    @Override
    protected UserWordDrawWordType draw(Long uid) throws WebServiceException {

        //概率
        //扭     蛋      中     大      奖
        //0.8    0.15    0.04  0.008   0.002
        double[] probability = new double[]{0, 0.8, 0.95, 0.99, 0.998, 1};
        double randomNumber = RandomUtils.randomDouble();
        logger.info("[ 抽奖结果 ]随机数为：{}", randomNumber);
        if (randomNumber < probability[1]) {
            return UserWordDrawWordType.ND_NIU;
        } else if (randomNumber >= probability[1] && randomNumber < probability[2]) {
            return UserWordDrawWordType.ND_DAN;
        } else if (randomNumber >= probability[2] && randomNumber < probability[3]) {
            return UserWordDrawWordType.ND_ZHONG;
        } else if (randomNumber >= probability[3] && randomNumber < probability[4]) {
            return UserWordDrawWordType.ND_DA;
        } else if (randomNumber >= probability[4] && randomNumber < probability[5]) {
            return UserWordDrawWordType.ND_JIANG;
        }
        logger.info("[ 抽奖结果 ] 概率配置错误");
        return null;
    }

    @Override
    protected boolean check(Long uid) {
        SysConfDTO option = sysconfManager.getSysConf(SysConfigId.word_draw_niu_dan_enable_option);
        if (option != null && Boolean.TRUE.toString().equals(option.getConfigValue())) {
            return true;
        }
        logger.info("[ 扭蛋大转盘失效 ]");
        return false;
    }
}
