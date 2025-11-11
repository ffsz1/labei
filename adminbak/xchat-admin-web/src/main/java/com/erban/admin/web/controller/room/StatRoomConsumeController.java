package com.erban.admin.web.controller.room;

import com.erban.admin.main.dto.UserRoomDTO;
import com.erban.admin.main.service.room.StatRoomConsumeService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Users;
import com.erban.main.util.StringUtils;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/10 15:30
 */
@Controller
@RequestMapping("/room/statRoomConsume")
public class StatRoomConsumeController extends BaseController {


    @Autowired
    private StatRoomConsumeService roomConsumeService;

    @RequestMapping("listAll")
    @ResponseBody
    public BusiResult listAll(HttpServletRequest request, Long erbanNo) {
        return roomConsumeService.countRoomConsume(erbanNo);
    }

    @RequestMapping("addRoom")
    @ResponseBody
    public BusiResult addRoom(Long erbanNo) {
        try {
            boolean flag = roomConsumeService.addRoom(erbanNo);
            return flag ? new BusiResult(BusiStatus.SUCCESS) : new BusiResult(BusiStatus.BUSIERROR);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR, e.getMessage(), null);
        }
    }

    @RequestMapping("delRoom")
    @ResponseBody
    public BusiResult delRoom(Long roomUid) {
        boolean flag = roomConsumeService.delRoom(roomUid);
        return flag ? new BusiResult(BusiStatus.SUCCESS) : new BusiResult(BusiStatus.BUSIERROR);
    }

    @RequestMapping("listRoom")
    @ResponseBody
    public Map<String, Object> listRoom(Long erbanNo) {
        return roomConsumeService.listRoom(getPageNumber(), getPageSize(), erbanNo);
//        return userRoomConsumeConfDAO.listRoom(erbanNo);
    }
}
