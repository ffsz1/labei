package com.erban.admin.main.service.charge;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.mybatismapper.ChargeRecordMapper;
import com.erban.main.param.admin.ChargeRecordParam;
import com.erban.main.vo.admin.ChargeRecordVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 后台管理-充值记录service  create by zhaomiao 2018/2/27
 */
@Service
public class ChargeRecordAdminService extends BaseService {
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;

    /**
     * 充值列表 查询+分页
     *
     * @param
     * @return
     */
    public BusiResult getList(ChargeRecordParam chargeRecordParam) {
        PageHelper.startPage(chargeRecordParam.getPage(), chargeRecordParam.getSize());
        if (StringUtils.isNotBlank(chargeRecordParam.getBeginDate())) {
            chargeRecordParam.setBeginDate(chargeRecordParam.getBeginDate() + " 00:00:00");
        }

        if (StringUtils.isNotBlank(chargeRecordParam.getEndDate())) {
            chargeRecordParam.setEndDate(chargeRecordParam.getEndDate() + " 23:59:59");
        }

        List<ChargeRecordVo> chargeRecordVos = chargeRecordMapper.selectByQuery(chargeRecordParam);
        if (chargeRecordVos == null || chargeRecordVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_CHARGERECORD_NOTEXIT);
        }

        BigDecimal totalAmount = chargeRecordMapper.queryTotalAmount(chargeRecordParam);
        List<ChargeRecordVo> genderNum = chargeRecordMapper.queryUserCount(chargeRecordParam);

        ChargeRecordVo chargeRecordVo = chargeRecordVos.get(0);
        Integer totalNum = 0;
        for (ChargeRecordVo recordVo : genderNum) {
            if (recordVo.getGender().toString().equals("1")) {
                chargeRecordVo.setMale(recordVo.getNum());
            } else if (recordVo.getGender().toString().equals("2")) {
                chargeRecordVo.setFemale(recordVo.getNum());
            } else {
                chargeRecordVo.setOther(recordVo.getNum());
            }
            totalNum = totalNum + recordVo.getNum();
        }

        chargeRecordVo.setUserNum(totalNum); //总人数
        chargeRecordVo.setTotalAmount(totalAmount == null ? new BigDecimal("0") : totalAmount); //总金额
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(chargeRecordVos));
    }

    /**
     * 导出到Excel表格
     *
     * @param
     * @return
     */
    public BusiResult exportExcel(ChargeRecordParam chargeRecordParam) {
        if (StringUtils.isNotBlank(chargeRecordParam.getBeginDate())) {
            chargeRecordParam.setBeginDate(chargeRecordParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(chargeRecordParam.getEndDate())) {
            chargeRecordParam.setEndDate(chargeRecordParam.getEndDate() + " 23:59:59");
        }
        List<ChargeRecordVo> chargeRecordVos = chargeRecordMapper.selectByQuery(chargeRecordParam);
        if (chargeRecordVos == null || chargeRecordVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_EXPORTEXCEL);
        }
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("充值记录表");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("订单ID");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("ping++单号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("拉贝号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("昵称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("电话");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("用户IP");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("金币数量");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("产品描述");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("金额（元）");
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);
        cell.setCellValue("支付渠道");
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);
        cell.setCellValue("支付状态");
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);
        cell.setCellValue("购买时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);
        cell.setCellValue("回调时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 13);
        cell.setCellValue("平台");
        cell.setCellStyle(style);
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (int i = 0; i < chargeRecordVos.size(); i++) {
            row = sheet.createRow((int) i + 1);
            ChargeRecordVo chargeRecordVo = chargeRecordVos.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell((short) 0).setCellValue(chargeRecordVo.getChargeRecordId());
            row.createCell((short) 1).setCellValue(chargeRecordVo.getPingxxChargeId() == null ? "-" :
                    chargeRecordVo.getPingxxChargeId());
            row.createCell((short) 2).setCellValue(chargeRecordVo.getErbanNo());
            row.createCell((short) 3).setCellValue(chargeRecordVo.getNick());
            row.createCell((short) 4).setCellValue(chargeRecordVo.getPhone());
            row.createCell((short) 5).setCellValue(chargeRecordVo.getClientIp());
            row.createCell((short) 6).setCellValue(chargeRecordVo.getTotalGold() == null ? "-" :
                    chargeRecordVo.getTotalGold().toString());
            row.createCell((short) 7).setCellValue(StringUtils.isBlank(chargeRecordVo.getChargeDesc()) ?
                    chargeRecordVo.getSubject() : chargeRecordVo.getChargeDesc());
            row.createCell((short) 8).setCellValue(chargeRecordVo.getAmount() / 100);
            row.createCell((short) 9).setCellValue(getChannel(chargeRecordVo.getChannel()));
            row.createCell((short) 10).setCellValue(getStatus(chargeRecordVo.getChargeStatus()));
            row.createCell((short) 11).setCellValue(DateTimeUtil.convertDate(chargeRecordVo.getCreateTime(), "yyyy-MM" +
                    "-dd HH:mm:ss"));
            row.createCell((short) 12).setCellValue(DateTimeUtil.convertDate(chargeRecordVo.getUpdateTime(), "yyyy-MM" +
                    "-dd HH:mm:ss"));
            row.createCell((short) 13).setCellValue(chargeRecordVo.getOs());
        }
        return new BusiResult(BusiStatus.SUCCESS, wb);
    }

    private String getStatus(Byte status) {
        if (status == null) {
            return "-";
        }
        String s = status + "";
        if ("1".equals(s)) {
            return "未支付";
        } else if ("2".equals(s)) {
            return "已支付";
        } else if ("3".equals(s)) {
            return "支付异常";
        } else if ("4".equals(s)) {
            return "支付超时";
        } else {
            return "-";
        }
    }

    private String getChannel(String channel) {
        if (StringUtils.isBlank(channel)) {
            return "-";
        }
        if ("wx".equalsIgnoreCase(channel)) {
            return "微信支付";
        } else if ("alipay".equalsIgnoreCase(channel)) {
            return "支付宝支付";
        } else if ("exchange".equalsIgnoreCase(channel)) {
            return "钻石兑换金币";
        } else if ("company".equalsIgnoreCase(channel)) {
            return "打款至公账充值";
        } else {
            return channel;
        }
    }
}
