package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.room.RoomAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Room;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/admin/roompool")
public class RoomPoolController extends BaseController {

    @Autowired
    private RoomAdminService roomAdminService;
    @Autowired
    private JedisService jedisService;


    @RequestMapping(value = "/getlist")
    @ResponseBody
    public void getRoomList() {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Room> pageInfo = roomAdminService.getRoomPoolByPage(getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 增加房间到配置池
     *
     * @param erbanNoStr 拉贝号，多个拉贝号可以用逗号分隔
     */
    @RequestMapping("/add")
    @ResponseBody
    public BusiResult addRoomForPool(String erbanNoStr) {
        if (BlankUtil.isBlank(erbanNoStr)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            String[] erbanArr = erbanNoStr.split(",");
            roomAdminService.addRoomPools(erbanArr);
        } catch (Exception e) {
            logger.error("addRoomForPool error, erbanNoStr: " + erbanNoStr, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }


    /**
     * 删除配置池某一个房间数据
     *
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public BusiResult delRoomPool(Long uid) {
        if (BlankUtil.isBlank(uid)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            roomAdminService.delRoomPool(uid);
        } catch (Exception e) {
            logger.error("delRoomPool error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }


    /**
     * 清空房间配置池的数据
     *
     * @return
     */
    @RequestMapping("/clear")
    @ResponseBody
    public BusiResult clearRoomPool() {
        try {
            roomAdminService.delAllRoomPools();
        } catch (Exception e) {
            logger.error("clearRoomPool error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }


    /**
     * 打开房间随机池，首页房间列表使用随机池数据
     *
     * @return
     */
    @RequestMapping("/open")
    @ResponseBody
    public BusiResult openRoomPool() {
        try {
            jedisService.set(RedisKey.config_home_random.getKey(), new Date().getTime() +"");
        } catch (Exception e) {
            logger.error("closeRoomPool error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     * 关闭房间随机池
     *
     * @return
     */
    @RequestMapping("/close")
    @ResponseBody
    public BusiResult closeRoomPool() {
        try {
            jedisService.del(RedisKey.config_home_random.getKey());
        } catch (Exception e) {
            logger.error("closeRoomPool error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
