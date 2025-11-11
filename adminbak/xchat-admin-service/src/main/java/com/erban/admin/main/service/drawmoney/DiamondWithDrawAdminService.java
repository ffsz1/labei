package com.erban.admin.main.service.drawmoney;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.service.dingtalk.DingtalkChatbotService;
import com.erban.admin.main.service.dingtalk.bo.DingtalkTextMessageBO;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.model.BillTransfer;
import com.erban.main.model.PacketWithDrawRecord;
import com.erban.main.model.PayErrorInfo;
import com.erban.main.mybatismapper.BillRecordMapper;
import com.erban.main.mybatismapper.BillTransferMapper;
import com.erban.main.mybatismapper.PacketWithDrawRecordMapper;
import com.erban.main.mybatismapper.PayErrorInfoMapper;
import com.erban.main.param.admin.WithDrawParam;
import com.erban.main.service.JoinpayService;
import com.erban.main.service.PingxxService;
import com.erban.main.vo.admin.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pingplusplus.model.Transfer;
import com.xchat.common.config.GlobalConfig;
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
 * 后台管理-钻石提现service  create by zhaomiao 2018/2/28
 */
@Service
public class DiamondWithDrawAdminService extends BaseService {
    @Autowired
    private BillRecordMapper billRecordMapper;
    @Autowired
    private PingxxService pingxxService;
    @Autowired
    private PacketWithDrawRecordMapper packetWithDrawRecordMapper;
    @Autowired
    private BillTransferMapper billTransferMapper;

    @Autowired
    private JoinpayService joinpayService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private DingtalkChatbotService chatbotService;

    @Autowired
    private PayErrorInfoMapper payErrorInfoMapper;

    /**
     * 支付宝、微信转账
     * @param ids
     * @return
     */
    public BusiResult transfer(List<String> ids, int adminId, int tranType) throws Exception {
        List<BillTransferDTO> diamondWithDrawVos = billRecordMapper.selectBillTransferByIds(ids);
        if(diamondWithDrawVos==null || diamondWithDrawVos.size()==0){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        logger.info("查询的转账用户数量:{}",diamondWithDrawVos.size());
        List<String> ret=new ArrayList<>();
        BillTransfer billRecord;
        for (int i = 0; i < diamondWithDrawVos.size(); i++) {
            BillTransferDTO vo = diamondWithDrawVos.get(i);
            if(vo==null){
                return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
            }
            billRecord = new BillTransfer();
            billRecord.setId(vo.getId().intValue());
            Map<String, Object> transferMap = new HashMap<>();
            Map<String, Object> extraMap = new HashMap<>();
            if (tranType == 1) {// 微信是1
                if (StringUtils.isBlank(vo.getWxOpenId()) && StringUtils.isBlank(vo.getApplyWithdrawalAccount())) {
                    return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDWX);
                }
                if(StringUtils.isNotBlank(vo.getApplyWithdrawalAccount())){
                    transferMap.put("recipient", vo.getApplyWithdrawalAccount());
                    billRecord.setRealTransferAccount(vo.getApplyWithdrawalAccount());
                }else{
                    transferMap.put("recipient", vo.getWxOpenId());
                    billRecord.setRealTransferAccount(vo.getWxOpenId());
                }
                transferMap.put("channel", "wx");
            } else if (tranType == 2) {// 支付宝是2
                if (StringUtils.isBlank(vo.getAlipayAccount()) && StringUtils.isBlank(vo.getApplyWithdrawalAccount())) {
                    return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDALIYAPACCOUNT);
                }
                if(StringUtils.isNotBlank(vo.getApplyWithdrawalAccount())){
                    transferMap.put("recipient", vo.getApplyWithdrawalAccount());
                    //extra
                    extraMap.put("recipient_name", vo.getApplyWithdrawalName());
                    billRecord.setRealTransferAccount(vo.getApplyWithdrawalAccount());
                    billRecord.setRealTransferName(vo.getApplyWithdrawalName());
                }else{
                    transferMap.put("recipient", vo.getAlipayAccount());
                    //extra
                    extraMap.put("recipient_name", vo.getAlipayAccountName());
                    billRecord.setRealTransferAccount(vo.getAlipayAccount());
                    billRecord.setRealTransferName(vo.getAlipayAccountName());
                }
                transferMap.put("channel", "alipay");

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
                billRecord.setBillStatus(Constant.WithDraw.finish);
                billRecord.setBankCard(vo.getCardNumber());
                billRecord.setBankCardName(vo.getCardName());
                billRecord.setOpenBankCode(vo.getOpenBankCode());
            }
            transferMap.put("order_no", vo.getBillId());
            transferMap.put("amount", vo.getMoney() * 100);
            transferMap.put("type", "b2c");
            transferMap.put("currency", "cny");
            transferMap.put("description", "钻石提现" + vo.getMoney() + "元");
            //更新数据库
            billRecord.setRealTranType((byte) tranType);
            billRecord.setUpdateTime(new Date());
            billRecord.setAdminId(adminId);
            Transfer transfer = pingxxService.transfer(transferMap);
            //测试环境下需要手动调用才会有回调,生产环境去除此功能
//            pingxxService.transferRetrieve(transfer.getId());
            logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",i+1,vo.getBillId(), JSON.toJSONString(transfer),adminId);
            if (transfer != null && StringUtils.isNotBlank(transfer.getOrderNo())) {
                billTransferMapper.updateByPrimaryKeySelective(billRecord);
            } else {
                ret.add(transfer.getOrderNo());
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, ret);
    }

    /**
     * 转账回调
     * @param transfer
     * @return
     * @throws Exception
     */
    public BusiResult updateTransfer(Transfer transfer,int type) throws Exception{
        //校验数据
        Transfer pingxxTransfer = pingxxService.transferRetrieve(transfer.getId());
        if(pingxxTransfer==null){
            logger.error("ping++查询不到此转账记录,transfer:{}",JSON.toJSONString(transfer));
            throw  new RuntimeException("not have this transfer record : "+transfer.getId());
        }
        //转账成功回调
        if (type == 1) {
            if (!"paid".equals(transfer.getStatus()) || !"paid".equals(pingxxTransfer.getStatus())) {
                logger.error("此笔转账未支付成功,transfer:{}", JSON.toJSONString(transfer));
                throw new RuntimeException("not transfer success status :" + transfer.getId());
            }
        } else {//转账失败回调
            if(!"failed".equals(transfer.getStatus()) || !"failed".equals(pingxxTransfer.getStatus())){
                logger.error("此笔转账非失败状态,transfer:{}",JSON.toJSONString(transfer));
                throw  new RuntimeException("not transfer fail status :"+transfer.getId());
            }
            PayErrorInfo payErrorInfo = new PayErrorInfo();
            if(transfer.getDescription().contains("钻石提现")){
                payErrorInfo.setType(1);
            }else{
                payErrorInfo.setType(2);
            }
            payErrorInfo.setErrorCode(transfer.getStatus());
            payErrorInfo.setCreateTime(new Date());
            payErrorInfo.setBillId(transfer.getOrderNo());
            payErrorInfo.setErrorMsg(transfer.getFailureMsg());
            payErrorInfoMapper.insert(payErrorInfo);
        }

        BillTransfer billTransfer = billTransferMapper.selectByBillId(transfer.getOrderNo());
        if(billTransfer != null){
            // 银行卡默认改成了成功
            if (billTransfer.getRealTranType().intValue() != 3 && billTransfer.getBillStatus().toString().equals("2")) {
                logger.error("this billRecord status is success,billRecord:{}", JSON.toJSONString(billTransfer));
                throw new RuntimeException("this billRecord status is success :" + transfer.getId());
            }
            //更新数据库
            billTransfer.setBillStatus(type==1?Constant.WithDraw.finish:Constant.WithDraw.error);
            billTransfer.setUpdateTime(new Date());
            billTransfer.setPingxxId(transfer.getId());
            billTransferMapper.updateByPrimaryKeySelective(billTransfer);
            logger.info("钻石提现转账回调成功,钻石提现订单信息信息:{} ,ping++回调信息:{} ,ping++校验信息:{}",JSON.toJSON(billTransfer),JSON.toJSON(transfer),JSON.toJSON(pingxxTransfer));
            return new BusiResult(BusiStatus.SUCCESS);

        }
        PacketWithDrawRecord packetRecord = packetWithDrawRecordMapper.selectByPrimaryKey(transfer.getOrderNo());
        if(packetRecord != null){
            if(packetRecord.getRecordStatus().toString().equals("2")) {
                logger.error("this packetWithDrawRecord status is success,billRecord:{}", JSON.toJSONString(packetRecord));
                throw new RuntimeException("this packetWithDrawRecord status is success :" + transfer.getId());
            }
            //更新数据库
            packetRecord.setPingxxId(transfer.getId());
            packetRecord.setUpdateTime(new Date());
            packetRecord.setRecordStatus(type==1?Constant.WithDraw.finish:Constant.WithDraw.error);
            packetWithDrawRecordMapper.updateByPrimaryKeySelective(packetRecord);
            logger.info("红包提现转账回调成功,红包提现订单信息:{} ,ping++回调信息:{} ,ping++校验信息:{}",JSON.toJSON(packetRecord),JSON.toJSON(transfer),JSON.toJSON(pingxxTransfer));
            return new BusiResult(BusiStatus.SUCCESS);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[ 管理后台预警 ]");
        builder.append(GlobalConfig.envName);
        builder.append("提现转账ping++回调失败");
        builder.append("ping++回调信息[").append(JSON.toJSON(transfer)).append("]");
        builder.append("ping++校验信息[").append(JSON.toJSON(pingxxTransfer)).append("]");
        chatbotService.send(new DingtalkTextMessageBO(builder.toString(), null, false));
        logger.info("转账回调失败钻石或红包提现订单不存在,ping++回调信息:{},ping++校验信息:{}",JSON.toJSON(transfer),JSON.toJSON(pingxxTransfer));
        return new BusiResult(BusiStatus.TRANSFER_ERROR);
    }


    /**
     * 提现列表 查询+分页
     *
     * @param withDrawParam withDrawParam
     * @return BusiResult BusiResult
     */
    public BusiResult getList(WithDrawParam withDrawParam) {
        PageHelper.startPage(withDrawParam.getPage(), withDrawParam.getSize());
        if (StringUtils.isNotBlank(withDrawParam.getBeginDate())) {
            withDrawParam.setBeginDate(withDrawParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(withDrawParam.getEndDate())) {
            withDrawParam.setEndDate(withDrawParam.getEndDate() + " 23:59:59");
        }
        List<BillTransferDTO> billTransferDTOS = billRecordMapper.selectBillTransferByQuery(withDrawParam);
        billTransferDTOS.stream().forEach(item ->{
            if(item.getBillStatus() == 1) {
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
        if (billTransferDTOS == null || billTransferDTOS.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
        }
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(billTransferDTOS));
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

    public BusiResult getCount(WithDrawParam withDrawParam) {
        Integer userNum=0;
        Long sumMoney=0L;
        PageHelper.startPage(withDrawParam.getPage(), withDrawParam.getSize());
        if (StringUtils.isNotBlank(withDrawParam.getBeginDate())) {
            withDrawParam.setBeginDate(withDrawParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(withDrawParam.getEndDate())) {
            withDrawParam.setEndDate(withDrawParam.getEndDate() + " 23:59:59");
        }
        DiamondWithDrawVo result = new DiamondWithDrawVo();
        List<BillTransferDTO> statRet = billRecordMapper.statByBillTransferQuery(withDrawParam);
        for (BillTransferDTO billTransferDTO:statRet){
            if(billTransferDTO.getGender().toString().equals("1")){//男
                result.setMale(billTransferDTO.getUserNum());
            }else if (billTransferDTO.getGender().toString().equals("2")){//女
                result.setFemale(billTransferDTO.getUserNum());
            }else {
                result.setOther(billTransferDTO.getUserNum());
            }
            userNum=userNum+billTransferDTO.getUserNum();
            sumMoney=sumMoney+billTransferDTO.getMoney();
        }
        result.setSumMoney(sumMoney);
        result.setUserNum(userNum);
        return new BusiResult(BusiStatus.SUCCESS, result);
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
        List<BillTransferDTO> diamondWithDrawVos = billRecordMapper.selectBillTransferByQuery(withDrawParam);
        if (diamondWithDrawVos == null || diamondWithDrawVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_EXPORTEXCEL);
        }
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("钻石提现表");
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
        cell.setCellValue("账号名称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("钻石数量");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("提现金额");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("提现状态");
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);
        cell.setCellValue("提现方式");
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);
        cell.setCellValue("真实方式");
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);
        cell.setCellValue("微信openId");
        cell.setCellStyle(style);
        cell = row.createCell((short) 13);
        cell.setCellValue("申请转账账号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 14);
        cell.setCellValue("申请转账姓名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 15);
        cell.setCellValue("真实转账账号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 16);
        cell.setCellValue("真实转账姓名");
        cell.setCellStyle(style);
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (int i = 0; i < diamondWithDrawVos.size(); i++) {
            row = sheet.createRow((int) i + 1);
            BillTransferDTO diamondWithDrawVo = diamondWithDrawVos.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell((short) 0).setCellValue(diamondWithDrawVo.getBillId());
            row.createCell((short) 1).setCellValue(diamondWithDrawVo.getErbanNo());
            row.createCell((short) 2).setCellValue(diamondWithDrawVo.getNick());
            row.createCell((short) 3).setCellValue(diamondWithDrawVo.getPhone());
            row.createCell((short) 4).setCellValue(diamondWithDrawVo.getAlipayAccount());
            row.createCell((short) 5).setCellValue(diamondWithDrawVo.getAlipayAccountName());
            row.createCell((short) 6).setCellValue(diamondWithDrawVo.getCost()==null?"-":diamondWithDrawVo.getCost().toString());
            row.createCell((short) 7).setCellValue(diamondWithDrawVo.getMoney());
            row.createCell((short) 8).setCellValue(getStatus(diamondWithDrawVo.getBillStatus()));
            row.createCell((short) 9).setCellValue(DateTimeUtil.convertDate(diamondWithDrawVo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            if(diamondWithDrawVo.getTranType().intValue() == 1){
                row.createCell((short) 10).setCellValue("微信");
            }else if (diamondWithDrawVo.getTranType().intValue() == 2){
                row.createCell((short) 10).setCellValue("支付宝");
            }else if (diamondWithDrawVo.getTranType().intValue() == 3){
                row.createCell((short) 10).setCellValue("银行卡");
            }
            else if (diamondWithDrawVo.getTranType().intValue() == 4){
                row.createCell((short) 10).setCellValue("汇聚");
            }else {
                row.createCell((short) 10).setCellValue("旧版支付宝");
            }
            if (diamondWithDrawVo.getRealTranType() == null) {
                row.createCell((short) 11).setCellValue("");
            } else if(diamondWithDrawVo.getRealTranType().intValue() == 1){
                row.createCell((short) 11).setCellValue("微信");
            }else if (diamondWithDrawVo.getRealTranType().intValue() == 2){
                row.createCell((short) 11).setCellValue("支付宝");
            }else if (diamondWithDrawVo.getRealTranType().intValue() == 3){
                row.createCell((short) 11).setCellValue("银行卡");
            }else {
                row.createCell((short) 11).setCellValue("未知");
            }
            row.createCell((short) 12).setCellValue(diamondWithDrawVo.getWxOpenId());
            row.createCell((short) 13).setCellValue(diamondWithDrawVo.getApplyWithdrawalAccount());
            row.createCell((short) 14).setCellValue(diamondWithDrawVo.getApplyWithdrawalName());
            row.createCell((short) 15).setCellValue(diamondWithDrawVo.getRealTransferAccount());
            row.createCell((short) 16).setCellValue(diamondWithDrawVo.getRealTransferName());

        }
        return new BusiResult(BusiStatus.SUCCESS, wb);
    }

    public BusiResult frozen(Long uid,int adminId){
        logger.info("钻石提现->冻结用户UID:{},操作人ID:{}",uid,adminId);
        jdbcTemplate.update("UPDATE users set withdraw_status = 1 where uid = ?", uid);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult unfrozen(Long uid,int adminId){
        logger.info("钻石提现->解冻用户UID:{},操作人ID:{}",uid,adminId);
        jdbcTemplate.update("UPDATE users set withdraw_status = 0 where uid = ?", uid);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult multiFrozen(List<String> ids,int adminId) {
        BillTransfer billTransfer;
        List<String> ret=new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            billTransfer = billTransferMapper.selectByBillId(ids.get(i));
            if(billTransfer==null || billTransfer.getBillStatus()==null || billTransfer.getBillStatus().intValue() != 1){
                continue;
            }
            //更新数据库
            billTransfer.setBillStatus(Constant.WithDraw.frozen);
            billTransfer.setUpdateTime(new Date());
            billTransferMapper.updateByPrimaryKeySelective(billTransfer);
            logger.info("冻结第:{}笔,转账ID:{},操作人ID:{}",i+1,ids.get(i),adminId);
            ret.add(ids.get(i));
        }
        return new BusiResult(BusiStatus.SUCCESS, ret);
    }

    public BusiResult multiUnFrozen(List<String> ids,int adminId) {
        BillTransfer billTransfer;
        List<String> ret=new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            billTransfer = billTransferMapper.selectByBillId(ids.get(i));
            if(billTransfer==null || billTransfer.getBillStatus()==null || billTransfer.getBillStatus().intValue() != 4){
                continue;
            }
            //更新数据库
            billTransfer.setBillStatus(Constant.WithDraw.ing);
            billTransfer.setUpdateTime(new Date());
            billTransferMapper.updateByPrimaryKeySelective(billTransfer);
            logger.info("解冻第:{}笔,转账ID:{},操作人ID:{}",i+1,ids.get(i),adminId);
            ret.add(ids.get(i));
        }
        return new BusiResult(BusiStatus.SUCCESS, ret);
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
        } else if ("4".equals(s)) {
            return "提现冻结";
        } else {
            return "-";
        }
    }

    public BusiResult getBankCardList(Long uid) {
        Map<String, Object> map = Maps.newHashMap();
        String sql = "select id,uid,bank_card as cardNumber,bank_card_name as cardName,open_bank_code as openBankCode from user_bank_card where uid = ? order by create_time desc";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql,uid);
        List<BankCardDTO> bankCardDTOList = Lists.newArrayList();
        if(maps.size() > 0){
            maps.stream().forEach(item ->{
                BankCardDTO bankCardDTO = new BankCardDTO();
                bankCardDTO.setCardName(item.get("cardName").toString());
                bankCardDTO.setCardNumber((item.get("cardNumber").toString()));
                bankCardDTO.setOpenBankCode(getBankName(item.get("openBankCode").toString()));
                bankCardDTO.setId(Integer.parseInt(item.get("id").toString()));
                bankCardDTO.setUid(Long.valueOf(item.get("uid").toString()));
                bankCardDTOList.add(bankCardDTO);
            });
        }
        PageInfo page = new PageInfo(bankCardDTOList);
        map.put("rows", page.getList());
        map.put("total", page.getTotal());
        return new BusiResult(BusiStatus.SUCCESS, map);
    }

    public BusiResult getBankCount(Long uid) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user_bank_card  WHERE uid = "+uid+"", Integer.class);
        return new BusiResult(BusiStatus.SUCCESS, result);
    }

    public BusiResult transferByCard(String billId, String cardNumber, String openBankCode, String cardName , int adminId) throws Exception{
        BillTransferDTO diamondWithDrawVos = billRecordMapper.selectBillTransferByBillId(billId);
        if(diamondWithDrawVos==null ){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        List<String> ret=new ArrayList<>();
        BillTransfer billRecord;
        billRecord = new BillTransfer();
        billRecord.setId(diamondWithDrawVos.getId().intValue());
        Map<String, Object> transferMap = new HashMap<>();
        Map<String, Object> extraMap = new HashMap<>();
        transferMap.put("channel", "wx");
        extraMap.put("user_name", cardName);
        extraMap.put("card_number", cardNumber);
        extraMap.put("open_bank_code", getBankNameByCode(openBankCode));
        transferMap.put("extra", extraMap);
        billRecord.setBillStatus(Constant.WithDraw.finish);
        transferMap.put("order_no", diamondWithDrawVos.getBillId());
        transferMap.put("amount", diamondWithDrawVos.getMoney() * 100);
        transferMap.put("type", "b2c");
        transferMap.put("currency", "cny");
        transferMap.put("description", "拉贝钻石提现" + diamondWithDrawVos.getMoney() + "元");
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
        logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",1,diamondWithDrawVos.getBillId(), JSON.toJSONString(transfer),adminId);
        if (transfer != null && StringUtils.isNotBlank(transfer.getOrderNo())) {
            billTransferMapper.updateByPrimaryKeySelective(billRecord);
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

    /**
     * 汇聚转账回调
     * @param joinpayTransferCallback
     * @return
     * @throws Exception
     */
    public void updateJoinpayTransfer(JoinpayTransferCallback joinpayTransferCallback) throws Exception{
        logger.info("---------------transfer joinpay CallBack -------------------{}", gson.toJson(joinpayTransferCallback));
        JoinpayTransferCallback transfer = joinpayService.transferQuery(joinpayTransferCallback.getMerchantOrderNo());
        if(transfer==null){
            logger.error("汇聚查询不到此转账记录,transfer:{}",JSON.toJSONString(transfer));
            throw new RuntimeException("not have this transfer record : "+transfer.getMerchantOrderNo());
        }
        BillTransfer billTransfer = billTransferMapper.selectByBillId(transfer.getMerchantOrderNo());
        if(billTransfer==null){
            logger.error("billTransfer is null,billTransfer:{}",JSON.toJSONString(billTransfer));
            throw new RuntimeException("billRecord null :"+transfer.getMerchantOrderNo());
        }
        if(joinpayTransferCallback.getStatus().intValue() == 205){
            billTransfer.setBillStatus(Constant.WithDraw.finish);
        }else{
            billTransfer.setBillStatus(Constant.WithDraw.error);
            PayErrorInfo payErrorInfo = new PayErrorInfo();
            payErrorInfo.setBillId(billTransfer.getBillId());
            payErrorInfo.setCreateTime(new Date());
            payErrorInfo.setErrorCode(joinpayTransferCallback.getErrorCode());
            payErrorInfo.setErrorMsg(joinpayTransferCallback.getErrorCodeDesc());
            payErrorInfo.setStatus(joinpayTransferCallback.getStatus());
            payErrorInfo.setType(1);
            payErrorInfoMapper.insert(payErrorInfo);
        }
        //更新数据库
        billTransfer.setUpdateTime(new Date());
        billTransferMapper.updateByPrimaryKeySelective(billTransfer);
    }

    /**
     * 汇聚银行卡转账
     * @param ids
     * @return
     */
    public BusiResult joinTransfer(List<String> ids, int adminId) throws Exception {
        List<BillTransferDTO> diamondWithDrawVos = billRecordMapper.selectBillTransferByIds(ids);
        if(diamondWithDrawVos==null || diamondWithDrawVos.size()==0){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        logger.info("查询的转账用户数量:{}",diamondWithDrawVos.size());
        List<String> ret = Lists.newArrayList();
        BillTransfer billRecord;
        JoinpayTransfer joinpayTransfer;
        Date now = new Date();
        for (int i = 0; i < diamondWithDrawVos.size(); i++) {
            BillTransferDTO vo = diamondWithDrawVos.get(i);
            if(vo==null){
                ret.add(vo.getBillId());
                continue;
//                return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
            }
            billRecord = new BillTransfer();
            billRecord.setId(vo.getId().intValue());
            if (StringUtils.isBlank(vo.getCardNumber())) {
                return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDCAR);
            }
            joinpayTransfer = new JoinpayTransfer();
            joinpayTransfer.setRequestTime(DateTimeUtil.convertDate(now));
            joinpayTransfer.setMerchantOrderNo(vo.getBillId());
            joinpayTransfer.setReceiverAccountNoEnc(vo.getCardNumber());
            joinpayTransfer.setReceiverNameEnc(vo.getCardName());
            joinpayTransfer.setPaidAmount(new BigDecimal(vo.getMoney()));
            //更新数据库
            billRecord.setBillStatus(Constant.WithDraw.finish);
            billRecord.setBankCard(vo.getCardNumber());
            billRecord.setBankCardName(vo.getCardName());
            billRecord.setOpenBankCode(vo.getOpenBankCode());
            billRecord.setRealTranType((byte) 4);
            billRecord.setUpdateTime(new Date());
            billRecord.setAdminId(adminId);
            JoinpayRet joinpayRet = joinpayService.transfer(joinpayTransfer);
            logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",i+1,vo.getBillId(), JSON.toJSONString(joinpayRet),adminId);
            if (joinpayRet != null && StringUtils.isNotBlank(joinpayRet.getData().getMerchantOrderNo())) {
                billRecord.setBillId(joinpayRet.getData().getMerchantOrderNo());
                billTransferMapper.updateByPrimaryKeySelective(billRecord);
            } else {
                ret.add(vo.getBillId());
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, ret.size()==0?"":("，其中"+ret.toString()+"转账失败"));
    }

    public BusiResult joinTransferByCard(String billId, String cardNumber, String openBankCode, String cardName , int adminId) throws Exception{
        BillTransferDTO diamondWithDrawVos = billRecordMapper.selectBillTransferByBillId(billId);
        if(diamondWithDrawVos==null ){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTNODIAMONDRECORD);
        }
        List<String> ret = Lists.newArrayList();
        JoinpayTransfer joinpayTransfer = new JoinpayTransfer();
        joinpayTransfer.setRequestTime(DateTimeUtil.convertDate(new Date()));
        joinpayTransfer.setMerchantOrderNo(billId);
        joinpayTransfer.setReceiverAccountNoEnc(cardNumber);
        joinpayTransfer.setReceiverNameEnc(cardName);
        joinpayTransfer.setPaidAmount(new BigDecimal(diamondWithDrawVos.getMoney()));
        //更新数据库
        BillTransfer billRecord;
        billRecord = new BillTransfer();
        billRecord.setId(diamondWithDrawVos.getId().intValue());
        billRecord.setBillStatus(Constant.WithDraw.finish);
        billRecord.setBankCard(cardNumber);
        billRecord.setBankCardName(cardName);
        billRecord.setOpenBankCode(getBankNameByCode(openBankCode));
        billRecord.setRealTranType((byte) 4);
        billRecord.setUpdateTime(new Date());
        billRecord.setAdminId(adminId);
        JoinpayRet joinpayRet = joinpayService.transfer(joinpayTransfer);
        logger.info("给第:{}个用户转账,转账ID:{},转账transfer:{},操作人ID:{}",1,diamondWithDrawVos.getBillId(), JSON.toJSONString(joinpayRet),adminId);
        if (joinpayRet != null && StringUtils.isNotBlank(joinpayRet.getData().getMerchantOrderNo())) {
            billRecord.setBillId(joinpayRet.getData().getMerchantOrderNo());
            billTransferMapper.updateByPrimaryKeySelective(billRecord);
        } else {
            ret.add(billId);
        }
        return new BusiResult(BusiStatus.SUCCESS, ret.size()==0?"":("，其中"+ret.toString()+"转账失败"));
    }
}
