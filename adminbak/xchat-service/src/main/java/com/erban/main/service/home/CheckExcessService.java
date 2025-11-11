package com.erban.main.service.home;

import com.beust.jcommander.internal.Maps;
import com.erban.main.service.base.BaseService;
import com.erban.main.util.StringUtils;
import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.NetEaseBaseClient;
import com.xchat.common.netease.neteaseacc.result.SmsRet;
import com.xchat.common.netease.util.NetEaseConstant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.GetTimeUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CheckExcessService extends BaseService {

    public void check(){
        Date date=new Date();
        Date startTime = GetTimeUtils.getTimesnights(date,0);
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT u.erban_no AS erbanNo, SUM(o.sum_gold) AS tol FROM one_day_room_send_sum o INNER JOIN users u ON o.send_uid = u.uid WHERE o.create_time > ? GROUP BY o.send_uid HAVING tol > 200000", startTime);
        String str;
        if(list!=null&&list.size()>0){
            for(Map<String, Object> m:list){
                str = jedisService.hget(RedisKey.check_excess.getKey(), DateTimeUtil.convertDate(startTime)+m.get("erbanNo"));
                if(StringUtils.isBlank(str)){
                    // 如果没发过短信就发送
                    String url = NetEaseConstant.smsBasicUrl + NetEaseConstant.SmsUrl.sendSms;
                    NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url,true);
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("mobile", "18680200851");
                    param.put("deviceId", "");
                    param.put("templateid", 3893147);
                    param.put("authCode", m.get("erbanNo"));
                    String result = null;
                    SmsRet smsRet;
                    for(int i=0;i<1;i++){
                        try {
                            result = netEaseBaseClient.buildHttpPostParam(param).executePost();
                        } catch (Exception e) {
                            logger.error("检测发送短信失败 erbanNo:"+m.get("erbanNo")+" tol:"+m.get("tol")+e.getMessage());
                        }
                        Gson gson = new Gson();
                        smsRet = gson.fromJson(result, SmsRet.class);
                        if (smsRet.getCode() == 200) {
                            jedisService.hset(RedisKey.check_excess.getKey(), DateTimeUtil.convertDate(startTime)+m.get("erbanNo"), m.get("tol").toString());
                        }else {
                            logger.error("检测发送短信失败 erbanNo:"+m.get("erbanNo")+" tol:"+m.get("tol")+" code:"+smsRet.getCode());
                        }
                        try {
                            Thread.sleep(3 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    param.put("mobile", "13232951695");
                    param.put("deviceId", "");
                    param.put("templateid", 3893147);
                    param.put("authCode", m.get("erbanNo"));
                    try {
                        result = netEaseBaseClient.buildHttpPostParam(param).executePost();
                    } catch (Exception e) {
                        logger.error("检测发送短信失败 erbanNo:"+m.get("erbanNo")+" tol:"+m.get("tol")+e.getMessage());
                    }
                    smsRet = gson.fromJson(result, SmsRet.class);
                    if (smsRet.getCode() == 200) {
                        jedisService.hset(RedisKey.check_excess.getKey(), DateTimeUtil.convertDate(startTime)+m.get("erbanNo"), m.get("tol").toString());
                    }else {
                        logger.error("检测发送短信失败 erbanNo:"+m.get("erbanNo")+" tol:"+m.get("tol")+" code:"+smsRet.getCode());
                    }
                }
            }
        }
    }

    public void sendSms(String code){
        // 如果没发过短信就发送
        String url = NetEaseConstant.smsBasicUrl + NetEaseConstant.SmsUrl.sendSms;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url, true);
        Map<String, Object> param = Maps.newHashMap();
        param.put("mobile", "13119531240");
        param.put("deviceId", "");
        param.put("templateid", 3893147);
        param.put("authCode", code);
        String result = null;
        SmsRet smsRet;
        for(int i=0;i<1;i++){
            try {
                result = netEaseBaseClient.buildHttpPostParam(param).executePost();
            } catch (Exception e) {
                logger.error("检测发送短信失败 erbanNo:"+e.getMessage());
            }
            Gson gson = new Gson();
            smsRet = gson.fromJson(result, SmsRet.class);
            if (smsRet.getCode() != 200) {
                logger.error("检测发送短信失败 erbanNo:"+" code:"+smsRet.getCode());
            }
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        param.put("mobile", "13232951695");
        param.put("deviceId", "");
        param.put("templateid", 3893147);
        param.put("authCode", code);
        try {
            result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        } catch (Exception e) {
            logger.error("检测发送短信失败 erbanNo:"+e.getMessage());
        }
        smsRet = gson.fromJson(result, SmsRet.class);
        if (smsRet.getCode() != 200) {
            logger.error("检测发送短信失败 erbanNo:"+" code:"+smsRet.getCode());
        }
    }

}
