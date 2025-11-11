package com.erban.admin.web.controller.room;


import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.room.RoomBgService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.RoomBg;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;


@Controller
@RequestMapping("/admin/roomBg")
@ResponseBody
public class RoomBgController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(RoomBgController.class);

    @Autowired
    private RoomBgService roomBgService;

    @RequestMapping("list")
    @ResponseBody
    public void list(Integer pageNumber, Integer pageSize) {
        PageInfo<RoomBg> pageInfo = roomBgService.list(pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("get/{id}")
    @ResponseBody
    public BusiResult<RoomBg> get(@PathVariable("id") Integer id) {
        RoomBg roomBg = roomBgService.get(id);
        return new BusiResult<>(BusiStatus.SUCCESS, roomBg);
    }

    @RequestMapping("delete/{id}")
    @ResponseBody
    public BusiResult delete(@PathVariable("id") Integer id) {
        //
        return roomBgService.delete(id);
    }

    @RequestMapping("save")
    @ResponseBody
    public BusiResult save(RoomBg roomBg, String beginDateStr, String endDateStr) {
        //
        if (StringUtils.isNotBlank(beginDateStr)) {
            try {
                roomBg.setBeginDate(DateUtil.str2Date(beginDateStr, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS));
            } catch (Exception e) {
                return new BusiResult(BusiStatus.PARAMERROR);
            }
        }
        if (StringUtils.isNotBlank(endDateStr)) {
            try {
                roomBg.setEndDate(DateUtil.str2Date(endDateStr, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS));
            } catch (Exception e) {
                return new BusiResult(BusiStatus.PARAMERROR);
            }
        }
        return roomBgService.save(roomBg);
    }
}
