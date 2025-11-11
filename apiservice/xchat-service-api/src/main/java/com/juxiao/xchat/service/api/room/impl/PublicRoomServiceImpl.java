package com.juxiao.xchat.service.api.room.impl;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.service.api.room.PublicRoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @Title:
 * @date 2019-05-22
 * @time 17:34
 */
@Slf4j
@Service
public class PublicRoomServiceImpl implements PublicRoomService {

    @Autowired
    private McoinMissionManager missionManager;

    @Override
    public void receiveMsg(String body) {
        if (StringUtils.isBlank(body)) {
            return;
        }

        JSONObject object;
        try {
            object = JSONObject.parseObject(body);
        } catch (Exception e) {
            return;
        }

        JSONObject custom = object.getJSONObject("custom");
        if (custom == null) {
            return;
        }


        JSONObject data = custom.getJSONObject("data");
        if (data == null) {
            return;
        }

        JSONObject params = data.getJSONObject("params");
        if (params == null) {
            return;
        }

        Long uid = params.getLong("uid");
        if (uid == null) {
            return;
        }

        try {
            missionManager.finish(uid, 3);
        } catch (WebServiceException e) {
            log.error("[接收大厅消息] 接收大厅消息完成点点币任务出现异常:",e);
        }
    }
}
