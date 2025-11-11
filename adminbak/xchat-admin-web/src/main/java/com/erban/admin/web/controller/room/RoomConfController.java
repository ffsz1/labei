package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.room.RoomConfService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.RoomConf;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author laizhilong
 * @Title: 用户配置
 * @Package com.erban.admin.web.controller.User
 * @date 2018/8/16
 * @time 09:54
 */
@Controller
@RequestMapping("/admin/room/conf")
public class RoomConfController extends BaseController {
    @Autowired
    private RoomConfService roomConfService;


    @ResponseBody
    @RequestMapping("/list")
    public void list(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<?> pageinfo = roomConfService.listWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageinfo.getTotal());
        jsonObject.put("rows", pageinfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @ResponseBody
    @RequestMapping("/save")
    public void save(RoomConf roomConf) {
        if (roomConf == null) {
            writeJsonResult(ErrorCode.ERROR_NULL_ARGU);
            return;
        }

        try {
            roomConfService.save(roomConf);
            writeJsonResult(1);
        } catch (Exception e) {
            logger.warn("save fail,cause by " + e.getMessage(), e);
            writeJsonResult(ErrorCode.SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping("/getOne")
    public void getOne(Long roomUid) {
        JSONObject jsonObject = new JSONObject();
        if (roomUid == null) {
            writeJson(jsonObject.toJSONString());
            return;
        }

        RoomConf roomConf = roomConfService.getRoomConf(roomUid);
        if (roomConf != null) {
            jsonObject.put("entity", roomConf);
        }

        writeJson(jsonObject.toJSONString());
    }
}
