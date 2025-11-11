package com.juxiao.xchat.manager.external.im.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.utils.AESUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.manager.common.conf.AesConf;
import com.juxiao.xchat.manager.common.room.vo.RoomMicVO;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMemberBO;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMessage;
import com.juxiao.xchat.manager.external.im.conf.ImConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImRoomManagerImpl implements ImRoomManager {
    private final Logger logger = LoggerFactory.getLogger(ImRoomManager.class);
    @Resource(name = "jsonHeaders")
    private HttpHeaders jsonHeaders;
    @Autowired
    private AesConf aesConf;
    @Autowired
    private ImConf imConf;


    @Override
    public void updateMemberInfo(ImRoomMemberBO memberBo) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            JSONObject object = new JSONObject();
            object.put("user_info", memberBo);

            String body = object.toJSONString();
            long startTime = System.currentTimeMillis();
            HttpEntity<String> entity = new HttpEntity<>(aesConf.encryptData(body), jsonHeaders);
            String url = imConf.getImUrl("/imroom/v1/updateMemberInfo");
            String result = restTemplate.postForObject(url, entity, String.class);
            long time = System.currentTimeMillis() - startTime;

            result = AESUtils.decrypt(JSON.parseObject(result).getString("ed"), aesConf.getKey(), aesConf.getIv());
            logger.info("[ IM房间管理 ]同步用户信息接口，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        } catch (Exception e) {
            logger.error("[ IM房间管理 ]同步用户信息接口,异常信息：", e);
        }
    }

    @Override
    public Long createRoom(RoomDTO roomDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject object = new JSONObject();
        object.put("room_info", roomDto);

        String body = object.toJSONString();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(aesConf.encryptData(body), jsonHeaders);
        String url = imConf.getImUrl("/imroom/v1/createRoom");
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        result = AESUtils.decrypt(JSON.parseObject(result).getString("ed"), aesConf.getKey(), aesConf.getIv());
        JSONObject ret = JSON.parseObject(result);
        logger.info("[ IM房间管理 ]创建聊天室，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }

        return ret.getLongValue("data");
    }

    @Override
    public void updateRoom(RoomDTO roomDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject object = new JSONObject();
        object.put("room_info", roomDto);

        String body = object.toJSONString();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(aesConf.encryptData(body), jsonHeaders);
        String url = imConf.getImUrl("/imroom/v1/updateRoomInfo");

        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        result = AESUtils.decrypt(JSON.parseObject(result).getString("ed"), aesConf.getKey(), aesConf.getIv());
        logger.info("[ IM房间管理 ]更新聊天室，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    @Override
    public void pushRoomMicUpdateNotice(Long roomId, Integer type, RoomMicVO micVo) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject object = new JSONObject();
        object.put("room_id", roomId);
        object.put("type", type);
        object.put("key", micVo.getPosition());
        object.put("value", micVo);

        String body = object.toJSONString();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(aesConf.encryptData(body), jsonHeaders);
        String url = imConf.getImUrl("/imroom/v1/pushRoomMicUpdateNotice");

        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;

        result = AESUtils.decrypt(JSON.parseObject(result).getString("ed"), aesConf.getKey(), aesConf.getIv());
        logger.info("[ IM房间管理 ]更新聊天室，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }


    /**
     * 往单个房间推送自定义消息
     *
     * @param msginfo
     * @return
     */
    @Override
    public void pushRoomMsg(Object msginfo) throws Exception {
        JSONObject object = new JSONObject();
        object.put("msg_info", msginfo);
        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(object.toJSONString(), jsonHeaders);
        String url = imConf.getImUrl("/imroom/v1/pushRoomMsg");
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
//        result = AESUtils.decrypt(JSON.parseObject(result).getString("ed"), aesConf.getKey(), aesConf.getIv());
        logger.info("[ IM房间管理 ] 往单个房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, JSON.toJSONString(msginfo), result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    /**
     * 往所有房间推送自定义消息
     *
     * @param msginfo
     * @return
     */
    @Override
    public void pushAllRoomMsg(Object msginfo, List<Long> interceptRoomList) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        JSONObject object = new JSONObject();
        object.put("msg_info", msginfo);
        object.put("intercept_room_list", interceptRoomList);
        HttpEntity<String> entity = new HttpEntity<>(object.toJSONString(), jsonHeaders);
        String url = imConf.getImUrl("/imroom/v1/pushAllRoomMsg");
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ] 往所有房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, object.toJSONString(), result, time);
        //result = AESUtils.decrypt(result, aesConf.getKey(), aesConf.getIv());
//        logger.info("[ IM房间管理 ] 往所有房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, msgInfo, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    @Override
    public boolean isRoomManager(Long roomId, Long uid) throws WebServiceException {
        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        JSONObject object = new JSONObject();

        Map<String, String> params = new HashMap<>();
        params.put("roomId", String.valueOf(roomId));
        params.put("uid", String.valueOf(uid));

        String url = imConf.getImUrl("/imroom/v1/getRoomMemberRole?room_id=" + roomId + "&uid=" + uid);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, params);
        String result = responseEntity.getBody();
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ] 往所有房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, object.toJSONString(), result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }

        JSONObject data = ret.getJSONObject("data");
        if (data.getBoolean("is_creator")) {
            return true;
        }

        if (data.getBoolean("is_manager")) {
            return true;
        }

        return false;
    }

    @Override
    public void pushUserMsg(ImRoomMessage msginfo) throws Exception {
        JSONObject object = new JSONObject();
        object.put("msg_info", msginfo);

        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(object.toJSONString(), jsonHeaders);
        String url = imConf.getImUrl("/imroom/v1/pushUserMsg");
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ] 往单个房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, JSON.toJSONString(msginfo), result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    /**
     * 往单个房间推送自定义消息
     *
     * @param msginfo
     * @return
     */
    @Override
    public void pushRoomCustomMsg(Object msginfo) throws Exception {
        JSONObject object = new JSONObject();
        object.put("msg_info", msginfo);
        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(object.toJSONString(), jsonHeaders);
        String url = imConf.getImUrl("/imroom/v1/pushRoomMsg");
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ] 往单个房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, JSON.toJSONString(msginfo), result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new WebServiceException(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }
}
