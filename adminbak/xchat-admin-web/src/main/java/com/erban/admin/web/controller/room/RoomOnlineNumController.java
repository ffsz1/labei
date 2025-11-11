package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.room.RoomOnlineNumAdminService;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import com.erban.admin.main.vo.RoomRobotGroupVo;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/roomOnlineNum")
public class RoomOnlineNumController extends BaseController {
    @Autowired
    private RoomOnlineNumAdminService roomOnlineNumAdminService;

    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    @ResponseBody
    public void getAll(RoomRobotGroupParam roomRobotGroupParam) {
        PageInfo<RoomRobotGroupVo> pageInfo = roomOnlineNumAdminService.getAll(getPageNumber(),getPageSize(),roomRobotGroupParam);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult add(Long erbanNo, Integer robotNum) {
        if(erbanNo==null||robotNum==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return roomOnlineNumAdminService.add(erbanNo, robotNum);
        } catch (Exception e) {
            logger.error("addRobot error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "del", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult deleteByUid(Long uid) {
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return roomOnlineNumAdminService.remove(uid);
        } catch (Exception e) {
            logger.error("deleteByUid error,uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
