package com.erban.admin.main.service.drawmoney;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.model.BillTransfer;
import com.erban.main.model.PacketWithDrawRecord;
import com.erban.main.model.PayErrorInfo;
import com.erban.main.mybatismapper.PacketWithDrawRecordMapper;
import com.erban.main.mybatismapper.PayErrorInfoMapper;
import com.erban.main.param.admin.WithDrawParam;
import com.erban.main.service.JoinpayService;
import com.erban.main.service.PingxxService;
import com.erban.main.vo.admin.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pingplusplus.model.Transfer;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 后台管理-红包提现service  create by zhaomiao 2018/2/27
 */

@Service
public class RedPacketWithDrawAdminService extends BaseService {
    @Autowired
    private PacketWithDrawRecordMapper packetWithDrawRecordMapper;
    @Autowired
    private PingxxService pingxxService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private JoinpayService joinpayService;

    @Autowired
    private PayErrorInfoMapper payErrorInfoMapper;


    /**
     * 支付宝、微信,银行卡转账
     * @param ids
     * @return
     */
    public BusiResult transfer(List<String> ids, int adminId, int tranType) throws Exception {
        AdminUser adminUser = adminUserService.getAdminUserById(adminId);
        List<PacketWithdrawRecordVo> packetWithdrawRecordVos = packetWithDrawRecordMapper.selectByIds(ids);
        if(packetWithdrawRecordVos==null || packetWithdrawRecordVos.size()==0){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        logger.info("查询的转账用户数量:{}",packetWithdrawRecordVos.size());
        List<String> ret=new ArrayList<>();
        PacketWithDrawRecord packetWithDrawRecord;
        for (int i = 0; i < packetWithdrawRecordVos.size(); i++) {
            PacketWithdrawRecordVo vo = packetWithdrawRecordVos.get(i);
            if(vo==null){
                return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
            }
            packetWithDrawRecord = new PacketWithDrawRecord();
            packetWithDrawRecord.setRecordId(vo.getRecordId());
            Map<String, Object> transferMap = new HashMap<>();
            Map<String, Object> extraMap = new HashMap<>();
            if (tranType == 1) {// 微信是1
                if (StringUtils.isBlank(vo.getWxOpenId())) {
                    return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDWX);
                }
                transferMap.put("channel", "wx");
                transferMap.put("recipient", vo.getWxOpenId());
            } else if (tranType == 2) {// 支付宝是2
                if (StringUtils.isBlank(vo.getAlipayAccount())) {
                    return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDALIYAPACCOUNT);
                }
                transferMap.put("channel", "alipay");
                transferMap.put("recipient", vo.getAlipayAccount());
                //extra
                extraMap.put("recipient_name", vo.getAlipayAccountName());
                transferMap.put("extra", extraMap);
            } else {// 银行卡是3
                if (StringUtils.isBlank(vo.getCardNumber())) {
                    return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDCAR);
                }
                transferMap.put("channel", "wx");

                //extra
                extraMap.put("user_name", vo.getCardName());
                extraMap.put("card_number", vo.getCardNumber());
                extraMap.put("open_bank_code", vo.getOpenBankCode());
                transferMap.put("extra", extraMap);
                packetWithDrawRecord.setRecordStatus(Constant.WithDraw.finish);
                packetWithDrawRecord.setBankCard(vo.getCardNumber());
                packetWithDrawRecord.setBankCardName(vo.getCardName());
                packetWithDrawRecord.setOpenBankCode(vo.getOpenBankCode());
            }
            transferMap.put("order_no", vo.getRecordId());
            transferMap.put("amount", vo.getPacketNum() * 100);
            transferMap.put("type", "b2c");
            transferMap.put("currency", "cny");
            transferMap.put("description", "红包提现" + vo.getPacketNum() + "元");
            //更新数据库
            packetWithDrawRecord.setRealTranType((byte) tranType);
            packetWithDrawRecord.setUpdateTime(new Date());
            packetWithDrawRecord.setAdminId(adminId);
            Transfer transfer = pingxxService.transfer(transferMap);
            //测试环境下需要手动调用才会有回调,生产环境去除此功能
//            pingxxService.transferRetrieve(transfer.getId());
            logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{},操作人:{}",i+1,vo.getRecordId(), JSON.toJSONString(transfer),adminId,adminUser.getUsername());
            if (transfer != null && StringUtils.isNotBlank(transfer.getOrderNo())) {
                packetWithDrawRecordMapper.updateByPrimaryKeySelective(packetWithDrawRecord);
            } else {
                ret.add(transfer.getOrderNo());
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, ret);
    }


    /**
     * 支付宝转账
     * @param ids
     * @return
     */
    public BusiResult transfer(List<String> ids,int adminId) throws Exception {
        List<PacketWithdrawRecordVo> packetWithdrawRecordVoList = packetWithDrawRecordMapper.selectByIds(ids);
        if(packetWithdrawRecordVoList==null || packetWithdrawRecordVoList.size()==0){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        logger.info("查询的转账用户数量:{}",packetWithdrawRecordVoList.size());
        List<String> ret=new ArrayList<>();
        for (int i = 0; i < packetWithdrawRecordVoList.size(); i++) {
            PacketWithdrawRecordVo vo = packetWithdrawRecordVoList.get(i);
            if(vo==null || StringUtils.isBlank(vo.getAlipayAccount())){
                return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDALIYAPACCOUNT);
            }
            Map<String, Object> transferMap = new HashMap<>();
            Map<String, Object> extraMap = new HashMap<>();
            transferMap.put("channel", "alipay");
            transferMap.put("order_no", vo.getRecordId());
            transferMap.put("amount", vo.getPacketNum() * 100);
            transferMap.put("type", "b2c");
            transferMap.put("currency", "cny");
            transferMap.put("recipient", vo.getAlipayAccount());
            transferMap.put("description", "红包提现");
            //extra
            extraMap.put("recipient_name", vo.getAlipayAccountName());
            transferMap.put("extra", extraMap);
            Transfer transfer = pingxxService.transfer(transferMap);
            //测试环境下需要手动调用才会有回调,生产环境去除此功能
//            pingxxService.transferRetrieve(transfer.getId());
            logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",i+1,vo.getRecordId(), JSON.toJSONString(transfer),adminId);
            if (transfer != null && StringUtils.isBlank(transfer.getOrderNo())) {
                ret.add(transfer.getOrderNo());
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, ret);
    }

    /**
     * 提现列表 查询+分页
     * @param
     * @return
     */
    public BusiResult getList(WithDrawParam withDrawParam) {
        PageHelper.startPage(withDrawParam.getPage(), withDrawParam.getSize());
        if (StringUtils.isNotBlank(withDrawParam.getBeginDate())) {
            withDrawParam.setBeginDate(withDrawParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(withDrawParam.getEndDate())) {
            withDrawParam.setEndDate(withDrawParam.getEndDate() + " 23:59:59");
        }
        List<PacketWithdrawRecordVo> packetWithdrawRecordVos =packetWithDrawRecordMapper.selectByQuery(withDrawParam);
        packetWithdrawRecordVos.stream().forEach(item -> {
            if(item.getRecordStatus() == 1) {
                String sql = "select uid,bank_card as cardNumber,bank_card_name as cardName,open_bank_code as openBankCode from user_bank_card where uid = ? order by create_time desc";
                List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, item.getUid());
                if (maps.size() > 0) {
                    StringBuffer cardNumberSB = new StringBuffer();
                    StringBuffer cardNameSB = new StringBuffer();
                    StringBuffer openBankCodeSB = new StringBuffer();
                    maps.forEach(map -> {
                        cardNumberSB.append(map.get("cardNumber").toString());
                        cardNameSB.append(map.get("cardName").toString());
                        openBankCodeSB.append(getBankName(map.get("openBankCode").toString()));
                        cardNumberSB.append("\r\n");
                        cardNameSB.append("\r\n");
                        cardNameSB.append("\r\n");
                        openBankCodeSB.append("\r\n");
                    });
                    item.setCardName(cardNameSB.toString());
                    item.setCardNumber(cardNumberSB.toString());
                    item.setOpenBankCode(openBankCodeSB.toString());
                }
            }
            if(item.getAdminId() != null && item.getAdminId() != 0){
                AdminUser adminUser = adminUserService.getAdminUserById(item.getAdminId());
                if(adminUser != null){
                    item.setAdminName(adminUser.getUsername());
                }else{
                    item.setAdminName("未知");
                }
            }else{
                item.setAdminName("未知");
            }
        });
        if (packetWithdrawRecordVos == null || packetWithdrawRecordVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
        }
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(packetWithdrawRecordVos));
    }


    private String getBankName(String code){
        if("0100".equals(code)){
            return "邮政银行";
        } else if ("0102".equals(code)) {
            return "工商银行";
        } else if ("0103".equals(code)) {
            return "农业银行";
        } else if ("0104".equals(code)) {
            return "中国银行";
        } else if ("0105".equals(code)) {
            return "建设银行";
        } else if ("0301".equals(code)) {
            return "交通银行";
        } else if ("0302".equals(code)) {
            return "中信银行";
        } else if ("0303".equals(code)) {
            return "光大银行";
        } else if ("0304".equals(code)) {
            return "华夏银行";
        } else if ("0305".equals(code)) {
            return "民生银行";
        } else if ( "0306".equals(code)) {
            return "广发银行";
        } else if ( "0308".equals(code)) {
            return "招商银行";
        } else if ("0309".equals(code)) {
            return "兴业银行";
        } else if ("0310".equals(code)) {
            return "浦发银行";
        } else if ("0318".equals(code)) {
            return "平安银行";
        } else if ("0403".equals(code)) {
            return "北京银行";
        } else if ("0408".equals(code)) {
            return "宁波银行";
        } else {
            return "未知";
        }
    }

    /**
     * 导出到Excel表格
     *
     * @param
     * @return
     */
    public BusiResult exportExcel(WithDrawParam withDrawParam) {
        if (StringUtils.isNotBlank(withDrawParam.getBeginDate())) {
            withDrawParam.setBeginDate(withDrawParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(withDrawParam.getEndDate())) {
            withDrawParam.setEndDate(withDrawParam.getEndDate() + " 23:59:59");
        }
        List<PacketWithdrawRecordVo> packetWithdrawRecordVos =packetWithDrawRecordMapper.selectByQuery(withDrawParam);
        if (packetWithdrawRecordVos == null || packetWithdrawRecordVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_EXPORTEXCEL);
        }
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("红包提现表");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("ID");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("拉贝号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("昵称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("电话");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("支付宝");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("真实姓名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("提现金额");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("提现状态");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);
        cell.setCellValue("提现方式");
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);
        cell.setCellValue("真实方式");
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);
        cell.setCellValue("微信openId");
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);
        cell.setCellValue("银行卡号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 13);
        cell.setCellValue("银行卡户名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 14);
        cell.setCellValue("所属银行");
        cell.setCellStyle(style);
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (int i = 0; i < packetWithdrawRecordVos.size(); i++) {
            row = sheet.createRow((int) i + 1);
            PacketWithdrawRecordVo packetWithdrawRecordVo = packetWithdrawRecordVos.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell((short) 0).setCellValue(packetWithdrawRecordVo.getRecordId());
            row.createCell((short) 1).setCellValue(packetWithdrawRecordVo.getErbanNo());
            row.createCell((short) 2).setCellValue(packetWithdrawRecordVo.getNick());
            row.createCell((short) 3).setCellValue(packetWithdrawRecordVo.getPhone());
            row.createCell((short) 4).setCellValue(packetWithdrawRecordVo.getAlipayAccount());
            row.createCell((short) 5).setCellValue(packetWithdrawRecordVo.getAlipayAccountName());
            row.createCell((short) 6).setCellValue(packetWithdrawRecordVo.getPacketNum());
            row.createCell((short) 7).setCellValue(getStatus(packetWithdrawRecordVo.getRecordStatus()));
            row.createCell((short) 8).setCellValue(DateTimeUtil.convertDate(packetWithdrawRecordVo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            if(packetWithdrawRecordVo.getTranType().intValue() == 1){
                row.createCell((short) 9).setCellValue("微信");
            }else if (packetWithdrawRecordVo.getTranType().intValue() == 2){
                row.createCell((short) 9).setCellValue("支付宝");
            }else if (packetWithdrawRecordVo.getTranType().intValue() == 3){
                row.createCell((short) 9).setCellValue("银行卡");
            }else if (packetWithdrawRecordVo.getTranType().intValue() == 4){
                row.createCell((short) 9).setCellValue("汇聚");
            }else {
                row.createCell((short) 9).setCellValue("旧版支付宝");
            }
            if (packetWithdrawRecordVo.getRealTranType() == null) {
                row.createCell((short) 10).setCellValue("");
            } else if(packetWithdrawRecordVo.getRealTranType().intValue() == 1){
                row.createCell((short) 10).setCellValue("微信");
            }else if (packetWithdrawRecordVo.getRealTranType().intValue() == 2){
                row.createCell((short) 10).setCellValue("支付宝");
            }else if (packetWithdrawRecordVo.getRealTranType().intValue() == 3){
                row.createCell((short) 10).setCellValue("银行卡");
            }else {
                row.createCell((short) 10).setCellValue("未知");
            }
            row.createCell((short) 11).setCellValue(packetWithdrawRecordVo.getWxOpenId());
            row.createCell((short) 12).setCellValue(packetWithdrawRecordVo.getCardNumber());
            row.createCell((short) 13).setCellValue(packetWithdrawRecordVo.getCardName());
            row.createCell((short) 14).setCellValue(getBankName(packetWithdrawRecordVo.getOpenBankCode()));
        }
        return new BusiResult(BusiStatus.SUCCESS, wb);
    }

    /**
     * @param status
     * @return
     */
    private String getStatus(Byte status) {
        if (status == null) {
            return "-";
        }
        String s = status + "";
        if ("1".equals(s)) {
            return "提现中";
        } else if ("2".equals(s)) {
            return "提现完成";
        } else if ("3".equals(s)) {
            return "提现异常";
        } else {
            return "-";
        }
    }

    public BusiResult transferByCard(String recordId, String cardNumber, String openBankCode, String cardName , int adminId) throws Exception{
        PacketWithdrawRecordVo packetWithdrawRecordVo = packetWithDrawRecordMapper.selectById(recordId);
        if(packetWithdrawRecordVo==null ){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        List<String> ret=new ArrayList<>();
        PacketWithDrawRecord billRecord;
        billRecord = new PacketWithDrawRecord();
        billRecord.setRecordId(packetWithdrawRecordVo.getRecordId());
        Map<String, Object> transferMap = new HashMap<>();
        Map<String, Object> extraMap = new HashMap<>();
        transferMap.put("channel", "wx");
        extraMap.put("user_name", cardName);
        extraMap.put("card_number", cardNumber);
        extraMap.put("open_bank_code", getBankNameByCode(openBankCode));
        transferMap.put("extra", extraMap);
        billRecord.setRecordStatus(Constant.WithDraw.finish);
        transferMap.put("order_no", packetWithdrawRecordVo.getRecordId());
        transferMap.put("amount", packetWithdrawRecordVo.getPacketNum() * 100);
        transferMap.put("type", "b2c");
        transferMap.put("currency", "cny");
        transferMap.put("description", "红包提现");
        //更新数据库
        billRecord.setBankCard(cardNumber);
        billRecord.setBankCardName(cardName);
        billRecord.setOpenBankCode(getBankNameByCode(openBankCode));
        billRecord.setRealTranType((byte) 3);
        billRecord.setUpdateTime(new Date());
        billRecord.setAdminId(adminId);
        Transfer transfer = pingxxService.transfer(transferMap);
        //测试环境下需要手动调用才会有回调,生产环境去除此功能
//            pingxxService.transferRetrieve(transfer.getId());
        logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",1,packetWithdrawRecordVo.getRecordId(), JSON.toJSONString(transfer),adminId);
        if (transfer != null && StringUtils.isNotBlank(transfer.getOrderNo())) {
            packetWithDrawRecordMapper.updateByPrimaryKeySelective(billRecord);
        } else {
            ret.add(transfer.getOrderNo());
        }
        return new BusiResult(BusiStatus.SUCCESS, ret);
    }

    private String getBankNameByCode(String bankName){
        if("邮政银行".equals(bankName)){
            return "0100";
        }else if("工商银行".equals(bankName)){
            return "0102";
        }else if("农业银行".equals(bankName)){
            return "0103";
        }else if("中国银行".equals(bankName)){
            return "0104";
        }else if("建设银行".equals(bankName)){
            return "0105";
        }else if("交通银行".equals(bankName)){
            return "0301";
        }else if("中信银行".equals(bankName)){
            return "0302";
        }else if("光大银行".equals(bankName)){
            return "0303";
        }else if("华夏银行".equals(bankName)){
            return "0304";
        } else if("民生银行".equals(bankName)){
            return "0305";
        }else if("广发银行".equals(bankName)){
            return "0306";
        }else if("招商银行".equals(bankName)){
            return "0308";
        }else if("兴业银行".equals(bankName)){
            return "0309";
        }else if("浦发银行".equals(bankName)){
            return "0310";
        }else if("平安银行".equals(bankName)){
            return "0318";
        }else if("北京银行".equals(bankName)){
            return "0403";
        }else if("宁波银行".equals(bankName)){
            return "0408";
        }else {
            return "未知";
        }
    }

    public BusiResult joinTransfer(List<String> ids, int adminId) throws Exception{
        List<PacketWithdrawRecordVo> packetWithdrawRecordVoList = packetWithDrawRecordMapper.selectByIds(ids);
        if(packetWithdrawRecordVoList==null || packetWithdrawRecordVoList.size()==0){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        logger.info("查询的转账用户数量:{}",packetWithdrawRecordVoList.size());
        List<String> ret = Lists.newArrayList();
        PacketWithDrawRecord packetWithDrawRecord;
        JoinpayTransfer joinpayTransfer;
        Date now = new Date();
        for (int i = 0; i < packetWithdrawRecordVoList.size(); i++) {
            PacketWithdrawRecordVo vo = packetWithdrawRecordVoList.get(i);
            if(vo==null){
                return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
            }


            if (StringUtils.isBlank(vo.getCardNumber())) {
                return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDCAR);
            }
            joinpayTransfer = new JoinpayTransfer();
            joinpayTransfer.setRequestTime(DateTimeUtil.convertDate(now));
            joinpayTransfer.setMerchantOrderNo(vo.getRecordId());
            joinpayTransfer.setReceiverAccountNoEnc(vo.getCardNumber());
            joinpayTransfer.setReceiverNameEnc(vo.getCardName());
            joinpayTransfer.setPaidAmount(new BigDecimal(vo.getPacketNum()));
            //更新数据库
            packetWithDrawRecord = new PacketWithDrawRecord();
            packetWithDrawRecord.setRecordId(vo.getRecordId());
            packetWithDrawRecord.setRecordStatus(Constant.WithDraw.finish);
            packetWithDrawRecord.setBankCard(vo.getCardNumber());
            packetWithDrawRecord.setBankCardName(vo.getCardName());
            packetWithDrawRecord.setOpenBankCode(vo.getOpenBankCode());
            packetWithDrawRecord.setRealTranType((byte) 4);
            packetWithDrawRecord.setAdminId(adminId);
            JoinpayRet joinpayRet = joinpayService.transferRedPackage(joinpayTransfer);
            logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",i+1,vo.getRecordId(), JSON.toJSONString(joinpayRet),adminId);
            if (joinpayRet != null && StringUtils.isNotBlank(joinpayRet.getData().getMerchantOrderNo())) {
                packetWithDrawRecord.setMerchantOrderNo(joinpayRet.getData().getMerchantOrderNo());
                packetWithDrawRecordMapper.updateByPrimaryKeySelective(packetWithDrawRecord);
            } else {
                ret.add(vo.getRecordId());
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, ret.size()==0?"":("，其中"+ret.toString()+"转账失败"));
    }

    public BusiResult joinTransferByCard(String recordId, String cardNumber, String openBankCode, String cardName, int adminId) throws Exception{
        PacketWithdrawRecordVo packetWithdrawRecordVo = packetWithDrawRecordMapper.selectById(recordId);
        if(packetWithdrawRecordVo==null ){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }

        List<String> ret=new ArrayList<>();

        JoinpayTransfer joinpayTransfer = new JoinpayTransfer();
        joinpayTransfer.setRequestTime(DateTimeUtil.convertDate(new Date()));
        joinpayTransfer.setMerchantOrderNo(packetWithdrawRecordVo.getRecordId());
        joinpayTransfer.setReceiverAccountNoEnc(cardNumber);
        joinpayTransfer.setReceiverNameEnc(cardName);
        joinpayTransfer.setPaidAmount(new BigDecimal(packetWithdrawRecordVo.getPacketNum()));
        //更新数据库
        PacketWithDrawRecord billRecord;
        billRecord = new PacketWithDrawRecord();
        billRecord.setRecordId(packetWithdrawRecordVo.getRecordId());
        billRecord.setBankCard(cardNumber);
        billRecord.setBankCardName(cardName);
        billRecord.setOpenBankCode(getBankNameByCode(openBankCode));
        billRecord.setRealTranType((byte) 4);
        billRecord.setUpdateTime(new Date());
        billRecord.setAdminId(adminId);
        JoinpayRet joinpayRet = joinpayService.transferRedPackage(joinpayTransfer);
        logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",1,packetWithdrawRecordVo.getRecordId(), JSON.toJSONString(joinpayRet),adminId);
        if (joinpayRet != null && StringUtils.isNotBlank(joinpayRet.getData().getMerchantOrderNo())) {
            billRecord.setMerchantOrderNo(joinpayRet.getData().getMerchantOrderNo());
            packetWithDrawRecordMapper.updateByPrimaryKeySelective(billRecord);
        } else {
            ret.add(packetWithdrawRecordVo.getRecordId());
        }
        return new BusiResult(BusiStatus.SUCCESS, ret.size()==0?"":("，其中"+ret.toString()+"转账失败"));
    }

    public void updateJoinpayTransfer(JoinpayTransferCallback joinpayTransferCallback) throws Exception{
        logger.info("---------------transfer joinpay CallBack -------------------{}", gson.toJson(joinpayTransferCallback));
        JoinpayTransferCallback transfer = joinpayService.transferQuery(joinpayTransferCallback.getMerchantOrderNo());
        if(transfer==null){
            logger.error("汇聚查询不到此转账记录,transfer:{}",JSON.toJSONString(transfer));
            throw new RuntimeException("not have this transfer record : "+transfer.getMerchantOrderNo());
        }
        PacketWithDrawRecord packetWithDrawRecord = packetWithDrawRecordMapper.selectByMerchantOrderNo(transfer.getMerchantOrderNo());
        if(packetWithDrawRecord==null){
            logger.error("billRecord is null,billRecord:{}",JSON.toJSONString(packetWithDrawRecord));
            throw new RuntimeException("billRecord null :"+transfer.getMerchantOrderNo());
        }
        //更新数据库

        if(joinpayTransferCallback.getStatus().intValue() == 205){
            packetWithDrawRecord.setRecordStatus(Constant.WithDraw.finish);
        }else{
            PayErrorInfo payErrorInfo = new PayErrorInfo();
            payErrorInfo.setBillId(packetWithDrawRecord.getRecordId());
            payErrorInfo.setCreateTime(new Date());
            payErrorInfo.setErrorCode(joinpayTransferCallback.getErrorCode());
            payErrorInfo.setErrorMsg(joinpayTransferCallback.getErrorCodeDesc());
            payErrorInfo.setStatus(joinpayTransferCallback.getStatus());
            payErrorInfo.setType(2);
            payErrorInfoMapper.insert(payErrorInfo);
            packetWithDrawRecord.setRecordStatus(Constant.WithDraw.error);
        }
        packetWithDrawRecord.setUpdateTime(new Date());
        packetWithDrawRecordMapper.updateByPrimaryKeySelective(packetWithDrawRecord);
    }

    public BusiResult getCount(WithDrawParam withDrawParam) {
        PageHelper.startPage(withDrawParam.getPage(), withDrawParam.getSize());
        if (StringUtils.isNotBlank(withDrawParam.getBeginDate())) {
            withDrawParam.setBeginDate(withDrawParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(withDrawParam.getEndDate())) {
            withDrawParam.setEndDate(withDrawParam.getEndDate() + " 23:59:59");
        }
       PacketWithdrawRecordVo packetWithdrawRecordVos =packetWithDrawRecordMapper.selectByCount(withDrawParam);
        return new BusiResult(BusiStatus.SUCCESS, packetWithdrawRecordVos);
    }
}
