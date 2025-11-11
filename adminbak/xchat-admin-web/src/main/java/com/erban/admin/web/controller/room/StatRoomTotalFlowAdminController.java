package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.room.StatRoomTotalFlowAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.param.admin.ChargeRecordParam;
import com.erban.main.param.admin.RoomFlowParam;
import com.google.common.collect.Lists;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 房间流水统计controller
 */
@Controller
@RequestMapping("/admin/roomFlow")
public class StatRoomTotalFlowAdminController extends BaseController {
    @Autowired
    private StatRoomTotalFlowAdminService statRoomTotalFlowAdminService;

    /** excel 存放的路径前缀 */
    public static final String EXCEL_FILE_PREFIX = "/data/java/webapps/static/excel/";

    /**
     * 房间流水列表
     *
     * @param roomFlowParam
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getList(@RequestBody RoomFlowParam roomFlowParam) {
        logger.info("房间流水列表接口,接口入参:{}", JSON.toJSONString(roomFlowParam));
        BusiResult busiResult = null;
        if (roomFlowParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = statRoomTotalFlowAdminService.getList(roomFlowParam);
        } catch (Exception e) {
            logger.error("房间流水列表数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR, e.getMessage());
        }
        return busiResult;
    }

    /**
     * 房间流水导出EXCEL
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/roomFlowExport", method = RequestMethod.GET)
    public void exportExcel(RoomFlowParam roomFlowParam, HttpServletResponse response) {
        logger.info("房间流水导出到EXCEL文件,接口入参:roomFlowParam：{}", JSON.toJSONString(roomFlowParam));
        BusiResult busiResult = null;
        OutputStream out = null;
        String fileName = "房间流水统计";
        try {
            busiResult = statRoomTotalFlowAdminService.exportExcel(roomFlowParam);
            if (BusiStatus.SUCCESS.value() == (busiResult.getCode())) {
                HSSFWorkbook wb = (HSSFWorkbook) busiResult.getData();
                out = response.getOutputStream();
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(fileName + ".xls", "UTF-8"));
                wb.write(out);
                logger.info("=============房间流水列表导出到EXCEL文件:END=====================");
            }
        } catch (Exception e) {
            logger.error("房间流水列表导出到EXCEL文件异常，Exception：{}", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error("房间流水列表导出到EXCEL文件，关闭OutputStream异常，Exception：{}", e);
            }
        }
    }

    /**
     * 获取所有的报表文件列表
     * @param request
     * @return
     */
    @RequestMapping("/files")
    @ResponseBody
    public BusiResult files(HttpServletRequest request) {
        List<String> list = Lists.newArrayList();
        // 获取文件夹下的所有报表文件
        getFiles(EXCEL_FILE_PREFIX, list);
        return new BusiResult(BusiStatus.SUCCESS, list);
    }

    /**
     * 获取文件夹下是文件列表
     * @param path 文件夹路径
     * @param files 文件列表
     */
    public static void getFiles(String path, List<String> files) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList == null) {
            return ;
        }
        File tempFile;
        for (int i = 0; i < tempList.length; i++) {
            tempFile = tempList[i];
            if (tempFile.isFile()) {
                files.add(tempFile.getName());
            }
//            if (tempList[i].isDirectory()) {
//                getFiles(tempList[i].toString(), files);
//            }
        }
    }


    /**
     * 查询一个月内的房间每天的流水
     * @param uid 用户UID
     * @return
     */
    @RequestMapping("getRoomFlow")
    @ResponseBody
    public BusiResult getRoomFlow(Long uid) {
        if(uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Map<String, Object> map = statRoomTotalFlowAdminService.getOneMonthFlow(uid);
        BusiResult busiResult = new BusiResult<>(BusiStatus.SUCCESS);
        busiResult.setData(map);
        return  busiResult;
    }

    @RequestMapping("echarts")
    @ResponseBody
    public BusiResult echarts(Integer classType, String uids, String beginDate, String endDate, String minNum) {
        Map<String, Object> map = statRoomTotalFlowAdminService.echarts(classType, uids, beginDate, endDate, minNum);
        return new BusiResult(BusiStatus.SUCCESS, map);
    }


}
