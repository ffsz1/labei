package com.erban.admin.web.controller.User;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.user.UserPurseSurplusService;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.bo.UserPurseSurplusBO;
import com.erban.main.model.UserPurseSurplus;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/9
 * @time 16:26
 */
@Controller
@RequestMapping("admin/userPurse/surplus")
@ResponseBody
public class UserPurseSurplusController extends BaseController {
    @Autowired
    private UserPurseSurplusService userPurseSurplusService;

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(String beginDate, String endDate) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<UserPurseSurplus> pageInfo = userPurseSurplusService.getByPage(beginDate, endDate, getPageNumber(),
                getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/exportExcel")
    public void download(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        Workbook workbook = null;
        List<UserPurseSurplusBO> recordExportBOS = new ArrayList<>();
        ExportParams params = new ExportParams("统计生成平台的剩余金币和钻石报表数据", "统计生成平台的剩余金币和钻石报表信息");
        List<UserPurseSurplus> userPurseSurplusList = this.userPurseSurplusService.selectByList();
        userPurseSurplusList.stream().forEach(item -> {
            UserPurseSurplusBO userPurseSurplus = new UserPurseSurplusBO();
            userPurseSurplus.setSurplusDate(item.getSurplusDate());
            userPurseSurplus.setSurplusDiamonds(item.getSurplusDiamonds());
            userPurseSurplus.setSurplusGold(item.getSurplusGold());
            userPurseSurplus.setSurplusFull(item.getSurplusFull());
            recordExportBOS.add(userPurseSurplus);
        });
        workbook = ExcelExportUtil.exportBigExcel(params, UserPurseSurplusBO.class, recordExportBOS);
        recordExportBOS.clear();
        ExcelExportUtil.closeExportBigExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=xiaobandian_userPurseSurPlusRecord.xlsx");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
}

