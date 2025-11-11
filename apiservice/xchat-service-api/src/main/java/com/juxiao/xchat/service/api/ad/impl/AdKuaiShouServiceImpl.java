package com.juxiao.xchat.service.api.ad.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.MD5;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.ad.AdKuaiShouDao;
import com.juxiao.xchat.dao.ad.domain.AdKuaiShouRecordDO;
import com.juxiao.xchat.dao.ad.dto.AdKuaiShouRecordDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.ad.AdKuaiShouService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @date 2019-06-20
 */
@Slf4j
@Service
public class AdKuaiShouServiceImpl implements AdKuaiShouService {


    @Autowired
    private AdKuaiShouDao adKuaiShouDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    @Override
    public int reciveKuaiShouMsg(AdKuaiShouRecordDO adKuaiShouRecordDO) {
        if(adKuaiShouRecordDO == null){
            log.warn("[ 接收快手信息 ] 请求内容格式有误");
            return 412;
        }
        if (StringUtils.isBlank(adKuaiShouRecordDO.getImei2()) && StringUtils.isBlank(adKuaiShouRecordDO.getIdfa2())) {
            log.warn("[ 接收快手信息 ] 客户端唯一标识为空");
            return 415;
        }
        if (StringUtils.isBlank(adKuaiShouRecordDO.getCallback())) {
            log.warn("[ 接收快手信息 ] 回调函数为空");
            return 416;
        }
        String redisKey = RedisKey.report_kuaishou_device_lock.getKey(adKuaiShouRecordDO.getImei2());
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            AdKuaiShouRecordDTO adKuaiShouRecordDTO;
            if (StringUtils.isNotBlank(adKuaiShouRecordDO.getImei2())) {
                adKuaiShouRecordDTO = adKuaiShouDao.getKuaiShouRecordByIMEI(adKuaiShouRecordDO.getImei2());
                if (adKuaiShouRecordDTO != null) {
                    log.warn("[ 接收快手信息 ] 已存在该android客户端消息，请勿重复发送");
                    return 420;
                }
            } else if (StringUtils.isNotBlank(adKuaiShouRecordDO.getIdfa2())) {
                adKuaiShouRecordDTO = adKuaiShouDao.getKuaiShouRecordByIDFA(adKuaiShouRecordDO.getIdfa2());
                if (adKuaiShouRecordDTO != null) {
                    log.warn("[ 接收快手信息 ] 已存在该ios客户端消息，请勿重复发送");
                    return 420;
                }
            }
            adKuaiShouRecordDO.setIsReport((byte)0);
            adKuaiShouRecordDTO = new AdKuaiShouRecordDTO();
            BeanUtils.copyProperties(adKuaiShouRecordDO, adKuaiShouRecordDTO);
            adKuaiShouDao.save(adKuaiShouRecordDTO);
            if(StringUtils.isNotBlank(adKuaiShouRecordDTO.getImei2())){
                redisManager.hset(RedisKey.report_kuaishou_device_imei.getKey(),adKuaiShouRecordDTO.getImei2(),gson.toJson(adKuaiShouRecordDTO));
            }
            if(StringUtils.isNotBlank(adKuaiShouRecordDTO.getIdfa2())){
                redisManager.hset(RedisKey.report_kuaishou_device_idfa.getKey(),adKuaiShouRecordDTO.getIdfa2(),gson.toJson(adKuaiShouRecordDTO));
            }
        } catch (Exception e) {
            log.error("[ 接收快手信息 ] 保存快手数据错误", e);
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
        return 200;
    }

    @Override
    public AdKuaiShouRecordDTO getKuaiShouRecord(String imei, String idfa){
        String str = "";
        AdKuaiShouRecordDTO adKuaiShouRecordDTO = null;
        if (StringUtils.isNotBlank(imei)) {
            imei = MD5.getMD5(imei);
            str= redisManager.hget(RedisKey.report_kuaishou_device_imei.getKey(), imei);
        }else if (StringUtils.isNotBlank(idfa)) {
            idfa = MD5.getMD5(idfa);
            str= redisManager.hget(RedisKey.report_kuaishou_device_idfa.getKey(), idfa);
        }
        if (StringUtils.isNotBlank(str)) {
            adKuaiShouRecordDTO = gson.fromJson(str, AdKuaiShouRecordDTO.class);
        }
        return adKuaiShouRecordDTO;


    }
}
