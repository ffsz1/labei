package com.erban.admin.web.task;

import com.erban.admin.main.service.room.StatRoomTotalFlowAdminService;
import com.erban.admin.web.controller.room.StatRoomTotalFlowAdminController;
import com.erban.main.param.admin.RoomFlowParam;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
public class ReportTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatRoomTotalFlowAdminService roomService;
    @Scheduled(cron = "0 0 7 * * ?")
    public void dayExport() {
        logger.info("[导出日报表] 开始------------------------------------");
        long time = System.currentTimeMillis();
        Date now = DateUtil.addDay(new Date(), -1);
        // Date begin = DateUtil.addDay(now, - 3);
        HSSFWorkbook wb = null;
        try {
            String path = getBasePath();
            trimFile(path);
            File file = new File(path + DateUtil.date2Str(now, DateUtil.DateFormat.YYYY_MM_DD) + "日报.xls");
            wb = exportExcel(now, now);
            if (wb != null) {
                wb.write(file);
            } else {
                logger.info("[导出日报表] 内容空");
            }
            logger.info("[导出日报表] 完成, 耗时:{}", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error("[导出日报表] 出错", e);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error("[导出日报表] 关闭文件失败", e);
                }
            }
        }
    }

    @Scheduled(cron = "0 30 7 ? * MON")
    public void weekExport() {
        logger.info("[导出周报表] 开始------------------------------------");
        long time = System.currentTimeMillis();
        Date now = new Date();
        Date begin = DateUtil.addDay(now, - 7);
        HSSFWorkbook wb = null;
        try {
            String beginStr = DateUtil.date2Str(begin, DateUtil.DateFormat.YYYY_MM_DD);
            String endStr = DateUtil.date2Str(DateUtil.addDay(now, - 1), DateUtil.DateFormat.YYYY_MM_DD);
            File file = new File(getBasePath() + beginStr + "__" + endStr + "周报.xls");
            wb = exportExcel(begin, now);
            if (wb != null) {
                wb.write(file);
            } else {
                logger.info("[导出周报表] 内容空");
            }
            logger.info("[导出周报表] 完成, 耗时:{}", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error("[导出周报表] 出错", e);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error("[导出周报表] 关闭文件失败", e);
                }
            }
        }
    }

    /**
     * 清除不需要的文件
     * @param path
     */
    public void trimFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] fileArr = file.listFiles();
            while (fileArr != null && fileArr.length > 35) {
                File temp = fileArr[0];
                temp.delete();
                logger.info("[清理报表] --------------------");
                fileArr = new File(path).listFiles();
            }
        }
    }

    /**
     * 获取报表数据
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     * @throws Exception
     */
    public HSSFWorkbook exportExcel(Date begin, Date end) throws Exception {
        RoomFlowParam param = new RoomFlowParam();
        param.setBeginDate(DateUtil.date2Str(begin, DateUtil.DateFormat.YYYY_MM_DD));
        param.setEndDate(DateUtil.date2Str(end, DateUtil.DateFormat.YYYY_MM_DD));
        BusiResult result = roomService.exportExcel(param);
        if (BusiStatus.SUCCESS.value() == result.getCode()) {
            return (HSSFWorkbook) result.getData();
        }
        return null;
    }


    /**
     * 获取文件存放的路径
     * @return
     */
    public String getBasePath() {
        //
        return StatRoomTotalFlowAdminController.EXCEL_FILE_PREFIX;
    }

}
