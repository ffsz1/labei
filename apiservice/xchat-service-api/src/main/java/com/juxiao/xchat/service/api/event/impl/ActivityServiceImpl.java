package com.juxiao.xchat.service.api.event.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.AppActivityDao;
import com.juxiao.xchat.dao.sysconf.dto.AppActivityWinDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.AppActivityActStatus;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @class: ActivityServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private AppActivityDao activityDao;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private UserPurseManager userPurseManager;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private Gson gson;

    /**
     * 获取官方活动
     *
     * @param alertWinLoc 弹窗位置: 1.活动首页; 2.直播间; 3.首页
     * @return
     */
    @Override
    public List<AppActivityWinDTO> listWinActivity(Integer alertWinLoc, Long uid) throws WebServiceException {
        if (alertWinLoc == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        List<AppActivityWinDTO> appActivityWinDTOS = getAppActivityWin(alertWinLoc);
        // TODO 首充大礼包
        // appActivityWinDTOS.addAll(getFirstChargePackageWinRoom(uid, alertWinLoc));
        return appActivityWinDTOS;
    }

    private List<AppActivityWinDTO> getAppActivityWin(Integer alertWinLoc) {
        List<AppActivityWinDTO> list = Lists.newArrayList();
        String string = redisManager.hget(RedisKey.act.getKey(), alertWinLoc.toString());
        if (StringUtils.isNotBlank(string)) {
            try {
                return gson.fromJson(string, new TypeToken<List<AppActivityWinDTO>>() {
                }.getType());
            } catch (Exception e) {
                log.error("getAppActivityWin GSON转换异常->异常信息:{}", e);
            }
        }
        list = activityDao.listWinActivity(AppActivityActStatus.using.getValue(), alertWinLoc);
        if (!CollectionUtils.isEmpty(list)) {
            redisManager.hset(RedisKey.act.getKey(), alertWinLoc.toString(), gson.toJson(list));
        }
        return list;
    }

    /**
     * 首充房间活动
     *
     * @param uid  uid
     * @param type type
     * @return List<AppActivityWinDTO>
     */
    private List<AppActivityWinDTO> getFirstChargePackageWinRoom(Long uid, Integer type) {
        List<AppActivityWinDTO> list = Lists.newArrayList();
        if (uid != null) {
            UserPurseDTO userPurse = userPurseManager.getUserPurse(uid);
            boolean isFirstCharge = userPurse.getIsFirstCharge();
            // 第一次充值的手机显示首冲活动
            if (isFirstCharge) {
                UsersDTO users = usersManager.getUser(uid);
                if (users != null) {
                    AppActivityWinDTO winDto = new AppActivityWinDTO();
                    winDto.setActId(0);
                    winDto.setActName("新手礼包");
                    winDto.setAlertWin(1);
                    winDto.setAlertWinLoc(new Byte("2"));
                    winDto.setAlertWinPic("http://pic.haijiaoxingqiu.cn/new_user.png");
                    winDto.setSkipType(new Byte("1"));
                    winDto.setActAlertVersion("1.0.0");
                    winDto.setSkipUrl("http://  :8080/front/newUser/index.html");
                    list.add(winDto);
                    return list;
                }
            }
        }
        return Lists.newArrayList();
    }

    /**
     * 获取所有活动
     *
     * @param uid 用户ID
     * @return
     * @throws WebServiceException
     */
    @Override
    public List<AppActivityWinDTO> listAllActivity(Long uid) throws WebServiceException {
        String string = redisManager.hget(RedisKey.act.getKey(), "all");
        if (StringUtils.isNotBlank(string)) {
            try {
                return gson.fromJson(string, new TypeToken<List<AppActivityWinDTO>>() {
                }.getType());
            } catch (Exception e) {
                log.error("listAllActivity GSON转换异常->异常信息:{}", e);
            }
        }
        List<AppActivityWinDTO> list = activityDao.listAllActivity(AppActivityActStatus.using.getValue());
        if (!CollectionUtils.isEmpty(list)) {
            redisManager.hset(RedisKey.act.getKey(), "all", gson.toJson(list));
        }
        return list;
    }
}
