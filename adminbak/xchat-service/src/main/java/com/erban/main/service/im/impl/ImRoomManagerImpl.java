package com.erban.main.service.im.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.erban.main.service.im.ImRoomManager;
import com.erban.main.service.im.bo.ImRoomMemberBO;
import com.erban.main.service.im.dto.RoomDTO;
import com.erban.main.service.im.vo.RoomMicVO;
import com.xchat.common.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@Service
public class ImRoomManagerImpl implements ImRoomManager {
    private final Logger logger = LoggerFactory.getLogger(ImRoomManager.class);

    @Resource(name = "jsonHeaders")
    private HttpHeaders jsonHeaders;
    public static final String IMUrl = PropertyUtil.getProperty("imUrl");

    @Override
    public void updateMemberInfo(ImRoomMemberBO memberBo) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject object = new JSONObject();
        object.put("user_info", memberBo);
        String body = object.toJSONString();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(body, jsonHeaders);
        String url = IMUrl + "/imroom/v1/updateMemberInfo";
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ]同步用户信息接口，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
    }

    @Override
    public Long createRoom(RoomDTO roomDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject object = new JSONObject();
        object.put("room_info", roomDto);
        String body = object.toJSONString();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(body, jsonHeaders);
        String url = IMUrl + "/imroom/v1/createRoom";
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        JSONObject ret = JSON.parseObject(result);
        logger.info("[ IM房间管理 ]创建聊天室，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        if (ret.getIntValue("errno") != 0) {
            throw new Exception(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
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
        HttpEntity<String> entity = new HttpEntity<>(body, jsonHeaders);
        String url = IMUrl + "/imroom/v1/updateRoomInfo";

        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ]更新聊天室，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new Exception(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
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
        HttpEntity<String> entity = new HttpEntity<>(body, jsonHeaders);
        String url = IMUrl + "/imroom/v1/pushRoomMicUpdateNotice";

        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ]更新聊天室，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new Exception(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    /**
     * 往单个房间推送自定义消息
     *
     * @param msgInfo
     * @return
     */
    @Override
    public void pushRoomMsg(String msgInfo) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(msgInfo, jsonHeaders);
        String url = IMUrl + "/imroom/v1/pushRoomMsg";
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ] 往单个房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, msgInfo, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new Exception(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    /**
     * 往所有房间推送自定义消息
     *
     * @param msgInfo
     * @return
     */
    @Override
    public void pushAllRoomMsg(String msgInfo) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(msgInfo, jsonHeaders);
        String url = IMUrl + "/imroom/v1/pushAllRoomMsg";
        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ] 往所有房间推送自定义消息，接口:>{},请求:>{},返回:>{},耗时:>{}", url, msgInfo, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new Exception(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    @Override
    public void addRobotToRoom(Long roomId, String accounts) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject object = new JSONObject();
        object.put("room_id", roomId);
        object.put("accounts", accounts);

        String body = object.toJSONString();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(body, jsonHeaders);
        String url = IMUrl + "/imroom/v1/addRobot";

        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ]添加机器人，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new Exception(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

    @Override
    public void deleteRobotToRoom(Long roomId, String accounts) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject object = new JSONObject();
        object.put("room_id", roomId);
        object.put("accounts", accounts);

        String body = object.toJSONString();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(body, jsonHeaders);
        String url = IMUrl + "/imroom/v1/delRobot";

        String result = restTemplate.postForObject(url, entity, String.class);
        long time = System.currentTimeMillis() - startTime;
        logger.info("[ IM房间管理 ]删除机器人，接口:>{},请求:>{},返回:>{},耗时:>{}", url, body, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("errno") != 0) {
            throw new Exception(ret.getIntValue("errno") + ":" + ret.getString("errmsg"));
        }
    }

}
