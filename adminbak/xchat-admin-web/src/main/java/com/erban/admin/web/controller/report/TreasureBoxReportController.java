package com.erban.admin.web.controller.report;

import com.erban.admin.main.dto.TreasureBoxReportDTO;
import com.erban.admin.main.service.report.TreasureBoxReportService;
import com.erban.admin.main.utils.FileUtil;
import com.erban.admin.main.utils.FileUtils;
import com.erban.admin.web.base.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TreasureBoxReportController extends BaseController {
    @Autowired
    private TreasureBoxReportService treasureBoxReportService;

    @RequestMapping(value = "/treasureBoxReport/list", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getList(String erbanNos, Integer pageSize, Integer pageNum, String os, Byte gender, Byte giftType,
                              String reportStartDate, String reportEndDate, String order) {
        return treasureBoxReportService.getList(pageSize, pageNum, os, gender, reportStartDate, reportEndDate, erbanNos, giftType, order);
    }

    @RequestMapping("/treasureBoxReport/export")
    public void export(HttpServletResponse response, String reportStartDate, String reportEndDate, String erbanNos, Byte giftType) {
        List<TreasureBoxReportDTO> treasureBoxReportDTOS = treasureBoxReportService.getExportList(reportStartDate,
                reportEndDate, erbanNos, giftType);
        // 导出操作
        FileUtils.exportExcel(treasureBoxReportDTOS, "拉贝报表", "拉贝报表", TreasureBoxReportDTO.class, "拉贝报表.xls", response);
    }

    @RequestMapping(value = "/treasureBoxReport/drawGiftInfo", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult drawGiftInfo() {
        return treasureBoxReportService.drawGift();
    }

    @RequestMapping(value = "/treasureBoxReport/getWhitelistList", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getWhitelistList(Integer pageSize, Integer pageNum, String os, Byte gender,
                                       String reportStartDate, String reportEndDate, String erbanNo) {
        return treasureBoxReportService.getWhitelistList(pageSize, pageNum, reportStartDate, reportEndDate, erbanNo);
    }

    @RequestMapping("/treasureBoxReport/exportWhitelist")
    public void exportWhitelist(HttpServletResponse response, String reportStartDate, String reportEndDate,
                                String erbanNo) {
        List<TreasureBoxReportDTO> treasureBoxReportDTOS =
                treasureBoxReportService.getExportWhitelist(reportStartDate, reportEndDate, erbanNo);
        // 导出操作
        FileUtil.exportExcel(treasureBoxReportDTOS, "挖矿白名单报表", "挖矿白名单报表", TreasureBoxReportDTO.class, "挖矿白名单报表.xls",
                response);
    }

    /**
     * 增加
     *
     * @param erbanNo 拉贝号
     * @return
     */
    @RequestMapping(value = "/treasureBoxReport/add")
    @ResponseBody
    public BusiResult add(Long erbanNo) {
        try {
            if (erbanNo == null) {
                return new BusiResult(BusiStatus.PARAMERROR);
            }
            return treasureBoxReportService.add(erbanNo, getAdminId());
        } catch (Exception e) {
            logger.error("add error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/treasureBoxReport/del")
    @ResponseBody
    public BusiResult delete(Long id) {
        try {
            return treasureBoxReportService.delete(id);
        } catch (Exception e) {
            logger.error("delete error,id=" + id, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }
}
