package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.RoomMasterDto;
import com.erban.admin.main.service.room.RobotAdminService;
import com.erban.admin.main.utils.FileUtils;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import com.erban.admin.main.vo.RoomRobotGroupVo;
import com.erban.admin.web.bo.RoomMasterAccountBO;
import com.erban.admin.web.bo.UserRegisterBO;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.param.AccountParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 机器人
 */
@Controller
@RequestMapping("/admin/robot")
public class RobotController extends BaseController {
    @Autowired
    private RobotAdminService robotAdminService;

    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    @ResponseBody
    public void getAll(RoomRobotGroupParam roomRobotGroupParam) {
        PageInfo<RoomRobotGroupVo> pageInfo = robotAdminService.getAll(getPageNumber(), getPageSize(),
                roomRobotGroupParam);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "getAllRoomMaster", method = RequestMethod.GET)
    @ResponseBody
    public void getAllRoomMaster(String startDate, String endDate) {
        PageInfo<RoomMasterDto> pageInfo = robotAdminService.getAllRoomMaster(getPageNumber(), getPageSize(),
                startDate, endDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult create(Integer number, Integer gender, String phone, String password) {
        if (number == 0 && (phone == "" || phone == null) && (password == "" || password == null) && (gender == 0 || gender == null)) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        if (!robotAdminService.checkIsPhone(phone)) {
            return new BusiResult(BusiStatus.PHONEINVALID);
        }

        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            robotAdminService.create(number, gender, phone, password);
        } catch (Exception e) {
            logger.warn("Create Room Master Account Fail, Cause By " + e.getMessage(), e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    @ResponseBody
    public void exportTable(String startDate, String endDate, HttpServletResponse response) {
        List<RoomMasterDto> roomMasterDtoList = robotAdminService.exportAllRoomMaster(startDate, endDate);
        List<RoomMasterAccountBO> roomMasterAccountBOList = new ArrayList<>();
        roomMasterDtoList.forEach(item -> {
            RoomMasterAccountBO roomMasterAccountBO = new RoomMasterAccountBO();
            BeanUtils.copyProperties(item, roomMasterAccountBO);
            if (item.getGender() == 1) {
                roomMasterAccountBO.setGender("男");
            } else if (item.getGender() == 2) {
                roomMasterAccountBO.setGender("女");
            } else {
                roomMasterAccountBO.setGender("其他");
            }
            roomMasterAccountBOList.add(roomMasterAccountBO);
        });
        // 导出操作
        FileUtils.exportExcel(roomMasterAccountBOList, "厅主账号", "厅主账号", RoomMasterAccountBO.class, "厅主账号.xls", response);
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult add(Long erbanNo, Integer num) {
        if (erbanNo == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        try {
            return robotAdminService.add(erbanNo, (num == null ? 10 : num));
        } catch (Exception e) {
            logger.error("addRobot error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "del", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult deleteByErbanNo(Long erbanNo) {
        if (erbanNo == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        try {
            return robotAdminService.remove(erbanNo);
        } catch (Exception e) {
            logger.error("addRobot error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }
}
