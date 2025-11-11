package com.erban.admin.web.controller.User;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.user.WarningSmsRecordExpandService;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.bo.WarningSmsRecordExportBO;
import com.erban.main.dto.WarningSmsRecordDTO;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chris
 * @Title: 预警信息
 * @date 2018/9/13
 * @time 上午11:34
 */

@Controller
@RequestMapping("/admin/warning/sms")
public class WarningSmsRecordController extends BaseController {

    @Autowired
    private WarningSmsRecordExpandService warningSmsRecordExpandService;


    @RequestMapping("/getall")
    @ResponseBody
    public void getAll(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<WarningSmsRecordDTO> pageInfo = warningSmsRecordExpandService.getListWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/exportExcel")
    public void download(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        Workbook workbook = null;
        List<WarningSmsRecordExportBO> recordExportBOS = new ArrayList<>();
        ExportParams params = new ExportParams("预警信息数据", "预警信息");
        List<WarningSmsRecordDTO> warningSmsRecordDTOS = this.warningSmsRecordExpandService.selectByList();
        warningSmsRecordDTOS.stream().forEach(item -> {
            WarningSmsRecordExportBO warningSmsRecordExportBO = new WarningSmsRecordExportBO();
            warningSmsRecordExportBO.setCreateTime(item.getCreateTime());
            warningSmsRecordExportBO.setErbanNo(item.getErbanNo());
            warningSmsRecordExportBO.setNick(item.getNick());
            warningSmsRecordExportBO.setRecordId(item.getRecordId());
            warningSmsRecordExportBO.setWarningType(item.getWarningType());
            warningSmsRecordExportBO.setUid(item.getUid());
            warningSmsRecordExportBO.setWarningValue(item.getWarningValue());
            recordExportBOS.add(warningSmsRecordExportBO);
        });
        workbook = ExcelExportUtil.exportBigExcel(params, WarningSmsRecordExportBO.class, recordExportBOS);
        recordExportBOS.clear();
        ExcelExportUtil.closeExportBigExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=密码_warningRecord.xlsx");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
}

