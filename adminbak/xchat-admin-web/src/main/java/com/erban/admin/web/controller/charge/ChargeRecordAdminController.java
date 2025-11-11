package com.erban.admin.web.controller.charge;


import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.charge.ChargeProdAdminService;
import com.erban.admin.main.service.charge.ChargeRecordAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.param.admin.ChargeRecordParam;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 后台管理-充值记录controller
 */
@Controller
@RequestMapping("/admin/charge")
public class ChargeRecordAdminController extends BaseController {
    @Autowired
    private ChargeRecordAdminService chargeRecordAdminService;

    @Autowired
    private ChargeProdAdminService chargeProdAdminService;

    /**
     * 充值列表
     *
     * @param chargeRecordParam
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getChargeRecordList(@RequestBody ChargeRecordParam chargeRecordParam) {
        logger.info("充值记录列表接口,接口入参:{}", JSON.toJSONString(chargeRecordParam));
        BusiResult busiResult = null;
        if (chargeRecordParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = chargeRecordAdminService.getList(chargeRecordParam);
        } catch (Exception e) {
            logger.error("充值记录列表数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        return busiResult;
    }

    /**
     * 充值产品（金币）列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/chargeProdList", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getChargeProdList() {
        logger.info("充值产品（金币）列表接口,接口入参：无");
        BusiResult busiResult = null;
        try {
            busiResult = chargeProdAdminService.getAll();
        } catch (Exception e) {
            logger.error("充值产品（金币）列表数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("充值产品（金币）列表接口,接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 导出到Excel表格
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/chargeRecordExport", method = RequestMethod.GET)
    public void diamondWithDrawExport(ChargeRecordParam chargeRecordParam, HttpServletResponse response) {
        logger.info("充值列表导出到EXCEL文件,接口入参:chargeRecordParam：{}", JSON.toJSONString(chargeRecordParam));
        BusiResult busiResult = null;
        OutputStream out = null;
        String fileName = "充值统计";
        try {
            busiResult = chargeRecordAdminService.exportExcel(chargeRecordParam);
            if (BusiStatus.SUCCESS.value() == (busiResult.getCode())) {
                HSSFWorkbook wb = (HSSFWorkbook) busiResult.getData();
                out = response.getOutputStream();
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(fileName + ".xls", "UTF-8"));
                wb.write(out);
                logger.info("=============提现列表导出到EXCEL文件:END=====================");
            }
        } catch (Exception e) {
            logger.error("提现列表导出到EXCEL文件异常，Exception：{}", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error("提现列表导出到EXCEL文件，关闭OutputStream异常，Exception：{}", e);
            }
        }
    }

}
