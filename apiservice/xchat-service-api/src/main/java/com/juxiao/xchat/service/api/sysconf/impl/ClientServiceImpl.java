package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.utils.MD5;
import com.juxiao.xchat.base.utils.Utils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.ad.AdKuaiShouDao;
import com.juxiao.xchat.dao.ad.dto.AdKuaiShouRecordDTO;
import com.juxiao.xchat.dao.sysconf.ClientLogDAO;
import com.juxiao.xchat.dao.sysconf.ClientSecurityInfoDAO;
import com.juxiao.xchat.dao.sysconf.FaceJsonDao;
import com.juxiao.xchat.dao.sysconf.domain.ClientLogDO;
import com.juxiao.xchat.dao.sysconf.domain.ClientSecurityInfoDO;
import com.juxiao.xchat.dao.sysconf.dto.FaceJsonDTO;
import com.juxiao.xchat.dao.sysconf.dto.SplashScreenDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.dao.task.domain.DeviceInfoDO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.ad.AdKuaiShouService;
import com.juxiao.xchat.service.api.sysconf.ClientService;
import com.juxiao.xchat.service.api.sysconf.vo.AppInitVo;
import com.juxiao.xchat.service.api.sysconf.vo.ClientConfigVo;
import com.juxiao.xchat.service.api.sysconf.vo.SplashVo;
import com.juxiao.xchat.service.api.user.bo.DeviceInfoBO;
import com.juxiao.xchat.service.common.bo.BaseParamBO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisManager redisManager;

    @Resource
    private FaceJsonDao faceJsonDao;

    @Resource
    private SysConfManager sysConfManager;

    @Resource
    private Gson gson;

    @Resource
    private AppVersionManager appVersionService;

    @Resource
    private ClientSecurityInfoDAO clientSecurityInfoDAO;

    @Resource
    private UsersManager usersManager;

    @Resource
    private ClientLogDAO clientLogDAO;

    @Resource
    private AdKuaiShouService adKuaiShouService;

    @Resource
    private AdKuaiShouDao adKuaiShouDao;

    @Resource
    private TaskDao taskDao;

    private final long LIMIT_VERSION = Utils.version2long("0.0.0");

    private final long IOS_LIMIT_VERSION = Utils.version2long("0.0.0");

    @Override
    public AppInitVo init(String os, String app, String appVersion, String ip, Long uid) throws WebServiceException {
        AppInitVo appInitVo = new AppInitVo();
        appInitVo.setFaceJson(getFaceJson(os, app, appVersion, ip));
        appInitVo.setSplashVo(getSplashConf(app, uid));
        return appInitVo;
    }

    @Override
    public ClientConfigVo getConfig(String idfa, String imei, DeviceInfoBO deviceInfo, String ip, String app) {
        ClientConfigVo configVo = new ClientConfigVo();
        SysConfDTO sysConfDTO = sysConfManager.getSysConf(SysConfigId.is_exchange_awards);
        configVo.setIsExchangeAwards(sysConfDTO == null ? "0" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.timestamps);
        configVo.setTimestamps(sysConfDTO == null ? "1" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.mic_in_list_option);
        configVo.setMicInListOption(sysConfDTO == null ? "0" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.lottery_box_option);
        configVo.setLottery_box_option(sysConfDTO == null ? "0" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.green_room_index);
        configVo.setGreenRoomIndex(sysConfDTO == null ? "6" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.lottery_box_big_gift);
        configVo.setLotteryBoxBigGift(sysConfDTO == null ? "" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.pay_channel);
        configVo.setPayChannel(sysConfDTO == null ? "1" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.pay_money);
        configVo.setPayMoney(sysConfDTO == null ? "488" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.mora_timeout);
        configVo.setMoraTime(sysConfDTO == null ? "30" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.gift_car_switch);
        configVo.setGiftCarSwitch(sysConfDTO == null ? "1" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.prohibit_modification);
        configVo.setProhibitModification(sysConfDTO == null ? "2" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.public_chat_hall_time);
        configVo.setPublicChatHallTime(sysConfDTO == null ? "30" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.send_pic_left_level);
        configVo.setSendPicLeftLevel(sysConfDTO == null ? "0" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.alipay_switch);
        configVo.setAliPaySwitch(sysConfDTO == null ? "1" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.ali_switch);
        configVo.setAliSwitch(sysConfDTO == null ? "1" : sysConfDTO.getConfigValue());

        sysConfDTO = sysConfManager.getSysConf(SysConfigId.xq_tag_id);
        configVo.setXqTagId(sysConfDTO == null ? "0" : sysConfDTO.getConfigValue());

        if (StringUtils.isNotBlank(idfa)) {
            redisManager.hset(RedisKey.idfa.getKey(), idfa, "1");
        }

        if (StringUtils.isNotBlank(imei)) {
            //快手上报
            String reportStr = redisManager.hget(RedisKey.report_kuaishou_imei.getKey(), imei);
            if (StringUtils.isBlank(reportStr)) {
                reportAdClient(imei, idfa);
            }
        }

        if (StringUtils.isNotBlank(idfa)) {
            redisManager.hset(RedisKey.idfa.getKey(), idfa, "1");
            //快手上报
            String reportStr = redisManager.hget(RedisKey.report_kuaishou_idfa.getKey(), idfa);
            if (StringUtils.isBlank(reportStr)) {
                reportAdClient(imei, idfa);
            }
        }

        if (StringUtils.isNotBlank(deviceInfo.getDeviceId()) && StringUtils.isBlank(redisManager.hget(RedisKey.device_id.getKey(), deviceInfo.getDeviceId()))) {
            try {
                deviceInfo.setIp(ip);
                DeviceInfoDO deviceInfoDO = new DeviceInfoDO();
                BeanUtils.copyProperties(deviceInfo, deviceInfoDO);
                taskDao.saveActivationInfo(deviceInfoDO);
                redisManager.hset(RedisKey.device_id.getKey(), deviceInfo.getDeviceId(),
                        System.currentTimeMillis() + "");
            } catch (Exception e) {
                logger.error("[ 保存设备激活失败 ]", e);
            }
        }

        return configVo;
    }

    /**
     * 调用快手上报接口
     *
     * @param imei
     * @param idfa
     * @return
     */
    public void reportAdClient(String imei, String idfa) {
        AdKuaiShouRecordDTO adKuaiShouRecordDTO = adKuaiShouService.getKuaiShouRecord(imei, idfa);
        if (adKuaiShouRecordDTO != null) {
            if (adKuaiShouRecordDTO.getIsReport() != null && adKuaiShouRecordDTO.getIsReport() == 0) {
                //1、如果没有上报快手，则进行上报操作
                boolean isReport = false;
                try {
                    if (StringUtils.isNotBlank(adKuaiShouRecordDTO.getCallback())) {
                        // 1.创建客户端访问服务器的httpclient对象 打开浏览器
                        HttpClient httpclient = HttpClientBuilder.create().build();
                        // 2.以请求的连接地址创建get请求对象 浏览器中输入网址
                        HttpGet httpget = new HttpGet(adKuaiShouRecordDTO.getCallback());
                        // 3.向服务器端发送请求 并且获取响应对象 浏览器中输入网址点击回车
                        HttpResponse response = httpclient.execute(httpget);
                        // 4.获取响应对象中的响应码
                        StatusLine statusLine = response.getStatusLine();
                        // 从状态行中获取状态码
                        int responseCode = statusLine.getStatusCode();
                        if (responseCode == 200 || responseCode == 302) {
                            isReport = true;
                        } else {
                            logger.error("[ 调用上报快手接口 ] 上报失败:>code{}", responseCode);
                        }
                    }
                } catch (Exception e) {
                    logger.error("[ 上报快手数据 ] 调用上报接口异常", e);
                }
                //2、更新快手广告点击数据记录表
                if (isReport) {
                    adKuaiShouRecordDTO.setIsReport((byte) 1);
                    if (StringUtils.isNotBlank(imei)) {
                        imei = MD5.getMD5(imei);
                        adKuaiShouDao.updateByIMEI(imei);
                        redisManager.hset(RedisKey.report_kuaishou_imei.getKey(), imei, System.currentTimeMillis() +
                                "");
                        redisManager.hset(RedisKey.report_kuaishou_device_imei.getKey(), imei,
                                gson.toJson(adKuaiShouRecordDTO));
                    } else if (StringUtils.isNotBlank(idfa)) {
                        idfa = MD5.getMD5(idfa);
                        adKuaiShouDao.updateByIDFA(idfa);
                        redisManager.hset(RedisKey.report_kuaishou_idfa.getKey(), idfa, System.currentTimeMillis() +
                                "");
                        redisManager.hset(RedisKey.report_kuaishou_device_idfa.getKey(), idfa,
                                gson.toJson(adKuaiShouRecordDTO));
                    }
                }
            }
        }
    }

    /**
     * 获取闪屏的配置信息
     *
     * @return
     */
    public SplashVo getSplashConf(String app, Long uid) {
        String result = redisManager.get(RedisKey.sys_conf_splash.getKey(app));
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, SplashVo.class);
        }

        SplashScreenDTO splashScreenDTO;
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (uid == null) {
            splashScreenDTO = sysConfManager.getSplashScreen(0);
        } else if (usersDTO.getCreateTime().after(DateUtils.duDate(new Date(), -30))) {
            splashScreenDTO = sysConfManager.getSplashScreen(2);
        } else {
            splashScreenDTO = sysConfManager.getSplashScreen(1);
        }

        if (splashScreenDTO == null)return null;

        SplashVo splashVo = new SplashVo();
        splashVo.setPict(splashScreenDTO.getPict());
        splashVo.setLink("https://" + splashScreenDTO.getLink());
        splashVo.setType(splashScreenDTO.getType());
        // splashVo.setPict(sysConfManager.getSysConf(SysConfigId.splash_pict).getConfigValue());
        // splashVo.setLink(sysConfManager.getSysConf(SysConfigId.splash_link).getConfigValue());
        // String splashType = sysConfManager.getSysConf(SysConfigId.splash_type).getConfigValue();
        // if (StringUtils.isNotBlank(splashType)) {
        //     splashVo.setType(Integer.valueOf(splashType));
        // }

        redisManager.set(RedisKey.sys_conf_splash.getKey(app), gson.toJson(splashVo));
        return splashVo;
    }

    /**
     * 表情包的JSON数据
     *
     * @param os
     * @param appVersion
     * @param
     * @return
     */
    private FaceJsonDTO getFaceJson(String os, String app, String appVersion, String clientIp) throws WebServiceException {
        //低版本隐藏
        if ("android".equalsIgnoreCase(os)) {
            if (appVersion != null && Utils.version2long(appVersion) <= LIMIT_VERSION) {
                return gson.fromJson(redisManager.get(RedisKey.face_json.getKey("emoji_face")), FaceJsonDTO.class);
            }
        }

        if ("1001".equalsIgnoreCase(app)) {
            if ("iOS".equalsIgnoreCase(os)) {
                if (appVersion != null && Utils.version2long(appVersion) <= IOS_LIMIT_VERSION) {
                    return gson.fromJson(redisManager.get(RedisKey.face_json.getKey("emoji_face")), FaceJsonDTO.class);
                }
            }
        }

        FaceJsonDTO resultJson;
        String result = redisManager.get(RedisKey.face_json.getKey());

        if (StringUtils.isNotBlank(result)) {
            try {
                return gson.fromJson(result, FaceJsonDTO.class);
            } catch (Exception e) {
                redisManager.del(RedisKey.face_json.getKey());
            }
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        logger.error("请求sql 前：", df.format(new Date()));
        List<FaceJsonDTO> list = faceJsonDao.list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        logger.error("请求sql 后：", df.format(new Date()));
        logger.info("[#######]face_json:{}", list.get(0));

        redisManager.set(RedisKey.face_json.getKey(), gson.toJson(list.get(0)));
        resultJson = list.get(0);
        return resultJson;
    }

    @Override
    public int saveSecurityInfo(ClientSecurityInfoDO infoDO) {
        if (infoDO == null) {
            return 0;
        }

        if (infoDO.getUid() == null) {
            infoDO.setUid(0L);
        }

        if (infoDO.getDeviceId() == null) {
            infoDO.setDeviceId("");
        }

        if (infoDO.getMsgId() == null) {
            infoDO.setMsgId("");
        }

        logger.info("[安全上报]infoDO:{}", infoDO);
        if (StringUtils.isNotBlank(infoDO.getContent())) {
            if (infoDO.getContent().length() > 2048) {
                infoDO.setContent(infoDO.getContent().substring(0, 2046));
            }
        }

        try {
            return clientSecurityInfoDAO.save(infoDO);
        } catch (Exception e) {
            logger.error("[安全上报] 保存信息异常, infoDO:" + infoDO, e);
            return 0;
        }
    }

    /**
     * 保存客户端上传的日志
     *
     * @param paramBO paramBO
     * @param url     url
     * @param ip      ip
     * @return boolean
     */
    @Override
    public boolean saveLogInfo(BaseParamBO paramBO, String url, String ip) {
        if (paramBO == null || StringUtils.isBlank(url)) {
            return false;
        }

        ClientLogDO logDO = new ClientLogDO();
        BeanUtils.copyProperties(paramBO, logDO);
        if (paramBO.getUid() != null) {
            UsersDTO usersDTO = usersManager.getUser(paramBO.getUid());
            if (usersDTO != null && usersDTO.getErbanNo() != null) {
                logDO.setErbanNo(usersDTO.getErbanNo().toString());
            }
        }

        logDO.setCreateTime(new Date());
        logDO.setUrl(url);
        logDO.setIp(ip);
        clientLogDAO.save(logDO);
        return true;
    }
}
