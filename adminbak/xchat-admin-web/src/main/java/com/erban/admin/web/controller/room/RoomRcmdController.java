package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.RoomRcmd;
import com.erban.main.model.SysConf;
import com.erban.main.param.room.RoomRcmdParam;
import com.erban.main.service.SysConfService;
import com.erban.main.service.room.RoomRcmdService;
import com.erban.main.service.room.vo.RoomRcmdVo;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/room/rcmd")
public class RoomRcmdController extends BaseController {
    @Autowired
    private SysConfService confService;
    @Autowired
    private RoomRcmdService rcmdService;

    /**
     *
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public void list() {
        PageInfo<RoomRcmd> pageinfo = rcmdService.listRcmdRooms(getPageNumber(), getPageSize());
        JSONObject object = new JSONObject();
        object.put("total", pageinfo.getTotal());
        object.put("rows", pageinfo.getList());
        writeJson(object.toJSONString());
    }

    @ResponseBody
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BusiResult<RoomRcmdVo> get(@RequestParam("rcmdId") Integer rcmdId) {
        return rcmdService.getRcmdRoom(rcmdId);
    }

    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public BusiResult save(RoomRcmdParam param) {
        return rcmdService.save(param);
    }

    @ResponseBody
    @RequestMapping(value = "updateOption", method = RequestMethod.POST)
    public BusiResult updateRcmdOption(@RequestParam(value = "isOpen", required = false, defaultValue = "false") Boolean isOpen) {
        return rcmdService.updateRcmdOption(isOpen);
    }

    @ResponseBody
    @RequestMapping(value = "getOption", method = RequestMethod.GET)
    public BusiResult getRcmdOption() {
        String value = confService.getSysConfValueById("rcmd_room_option");
        return new BusiResult<>(BusiStatus.SUCCESS, value);
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public BusiResult delete(@RequestParam("rcmdId") Integer rcmdId) {
        rcmdService.delete(rcmdId);
        return new BusiResult(BusiStatus.SUCCESS);
    }
}
