package com.erban.admin.web.controller.record;

import com.erban.admin.main.service.record.StatReportAdminService;
import com.erban.admin.main.utils.FileUtils;
import com.erban.admin.main.vo.GiftVo;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.bo.StatReportBO;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.param.AccountParam;
import com.xchat.oauth2.service.vo.admin.AccountVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 整体报表
 */
@Controller
@RequestMapping("/admin/stat")
public class StatAdminController extends BaseController {
    @Autowired
    private StatReportAdminService statReportAdminService;

    /**
     * 整体报表
     */
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getReport(@RequestBody AccountParam accountParam) {
        long begin = System.currentTimeMillis();
        if (accountParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        BusiResult busiResult;
        try {
            busiResult = statReportAdminService.getList(accountParam);
        } catch (Exception e) {
            logger.error("接口调用：（/admin/stat/report），整体报表异常,错误原因:{}", e.getMessage());
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage());
        }

        long end = System.currentTimeMillis();
        System.out.println("接口调用：（/admin/stat/report）,耗时统计:" + (end - begin));
        return busiResult;
    }

    /**
     * 查询用户在时间段内明细
     *
     * @param uid
     * @param erbanNo
     * @param type
     * @param beginDate
     * @param endDate
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getDetail(Long uid, Long erbanNo, Integer type, String beginDate, String endDate, Integer page,
                                Integer size) {
        if (uid == null || type == null || erbanNo == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return statReportAdminService.getDetail(uid, erbanNo, beginDate, endDate, type, page, size);
        } catch (Exception e) {
            logger.error("统计详情列表数据出错,错误原因:{}", e.getMessage());
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 导出到Excel表格
     *
     * @param accountParam
     * @param response
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void exportExcel(AccountParam accountParam, HttpServletResponse response) {
        BusiResult busiResult = statReportAdminService.getList(accountParam);
        PageInfo pageInfo = (PageInfo) busiResult.getData();
        List<AccountVo> accountVoList = pageInfo.getList();
        List<StatReportBO> statReportBOList = new ArrayList<>();
        accountVoList.forEach(item -> {
            StatReportBO statReportBO = new StatReportBO();
            BeanUtils.copyProperties(item, statReportBO);
            statReportBOList.add(statReportBO);
        });
        // 导出操作
        FileUtils.exportExcel(statReportBOList, "整体报表", "整体报表", StatReportBO.class, "整体报表.xls", response);
    }

    /**
     * 导出明细到Excel表格
     *\
     * @param response
     */
    @RequestMapping(value = "/exportDetailExcel", method = RequestMethod.GET)
    public void exportDetailExcel(Long uid, Long erbanNo, Integer type, String beginDate, String endDate, HttpServletResponse response) {
        BusiResult busiResult = statReportAdminService.getDetail(uid, erbanNo, beginDate, endDate, type, 1, 20000);
        PageInfo pageInfo = (PageInfo) busiResult.getData();
        List<GiftVo> giftVoList = pageInfo.getList();
        giftVoList.forEach(item -> {
            if (item.getGiftType() == 1) {
                item.setGiftTypeName("打Call礼物");
            } else if (item.getGiftType() == 2) {
                item.setGiftTypeName("普通礼物");
            } else if (item.getGiftType() == 3) {
                item.setGiftTypeName("捡海螺礼物");
            } else if (item.getGiftType() == 4) {
                item.setGiftTypeName("活动礼物");
            }
        });
        if (type == 4) {
            // 导出操作
            FileUtils.exportExcel(giftVoList, "财富变化报表", "财富变化报表", GiftVo.class, "财富变化报表.xls", response);
        } else if (type == 5) {
            // 导出操作
            FileUtils.exportExcel(giftVoList, "魅力变化报表", "魅力变化报表", GiftVo.class, "魅力变化报表.xls", response);
        }
    }
}
