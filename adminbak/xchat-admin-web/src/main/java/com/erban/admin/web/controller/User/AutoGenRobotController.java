package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.erban.admin.main.service.AutoGenRobotAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.AutoGenRobot;
import com.erban.main.model.RoomTag;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成机器人
 *
 * @author chris
 * @Title:
 * @date 2019-03-11 11:52
 */
@Controller
@RequestMapping("/auto/gen/robot")
public class AutoGenRobotController extends BaseController {
    @Autowired
    private AutoGenRobotAdminService autoGenRobotService;

    /**
     * 获取所有机器人列表
     */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList() {
        PageInfo<AutoGenRobot> pageInfo = autoGenRobotService.getList(getPageNumber(), getPageSize());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 根据ID获取机器人信息
     *
     * @param id 机器人ID
     */
    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getOne(Integer id) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            if (id == null) {
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            busiResult.setData(autoGenRobotService.getOne(id));
        } catch (Exception e) {
            logger.error("getAutoRobotById error", e);
        }
        return busiResult;
    }

    /**
     * 生成机器人
     * @param type 生成类型 [1.机器人; 2.水军; 3.厅主号]
     * @return
     */
    @RequestMapping(value = "/gen")
    @ResponseBody
    public BusiResult gen(Integer type) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            autoGenRobotService.batchGenRobAccount(type);
        } catch (Exception e) {
            logger.warn("batchGenRobAccount fail,cause by " + e.getMessage(), e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    /**
     * 创建或修改机器人账户
     *
     * @param autoGenRobot 自动生成机器人信息
     * @param isEdit       是否修改
     * @return
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public BusiResult create(AutoGenRobot autoGenRobot, boolean isEdit) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            autoGenRobotService.createOrModifyRobot(autoGenRobot, isEdit);
        } catch (Exception e) {
            logger.warn("create fail,cause by " + e.getMessage(), e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    /**
     * 批量修改机器人密码
     *
     * @param robotIds 机器人ID
     * @return
     */
    @RequestMapping(value = "/modifiedPassword")
    @ResponseBody
    public BusiResult modifiedPassword(String robotIds, String password) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            autoGenRobotService.modifiedPassword(robotIds, password);
        } catch (Exception e) {
            logger.warn("create fail,cause by " + e.getMessage(), e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }
}
