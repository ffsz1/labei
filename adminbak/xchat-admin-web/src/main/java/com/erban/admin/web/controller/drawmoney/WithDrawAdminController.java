package com.erban.admin.web.controller.drawmoney;


import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.drawmoney.DiamondWithDrawAdminService;
import com.erban.admin.main.service.drawmoney.RedPacketWithDrawAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Users;
import com.erban.main.param.admin.WithDrawParam;
import com.erban.main.service.RedPacketWithDrawService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.core.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 后台管理-提现controller
 */
@Controller
@RequestMapping("/admin/withdraw")
public class WithDrawAdminController extends BaseController {
    @Autowired
    private RedPacketWithDrawAdminService redPacketWithDrawAdminService;
    @Autowired
    private DiamondWithDrawAdminService diamondWithDrawAdminService;
    @Autowired
    private RedPacketWithDrawService redPacketWithDrawService;//仅用于后台手动红包提现
    @Autowired
    private UsersService usersService;

    /**
     * 钻石提现转账
     * @param
     * @return
     */
    @RequestMapping(value = "/transferByWx", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferByWx(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/transferByWx），转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult = null;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = diamondWithDrawAdminService.transfer(ids,getAdminId(),1);
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transferByWx），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    @RequestMapping(value = "/transferByAlipay", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferByAlipay(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/transferByAlipay），转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult = null;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = diamondWithDrawAdminService.transfer(ids,getAdminId(),2);
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transferByAlipay），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 银行卡转账
     * @param request
     * @return
     */
    @RequestMapping(value = "/transferByCar", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferByCar(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/transferByCar），转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult = null;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = diamondWithDrawAdminService.transfer(ids,getAdminId(),3);
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transferByCar），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 银行卡转账
     * @param billId
     * @param cardNumber
     * @param openBankCode
     * @return
     */
    @RequestMapping(value = "/transferByCard", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferByCard(String billId,String cardNumber,String openBankCode,String cardName) {
        logger.info("接口调用：（/admin/withdraw/transferByCard），转账接口,billId:{},cardNumber:{},openBankCode:{},cardName:{}", billId,cardNumber,openBankCode,cardName);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (billId == null || cardNumber == null || openBankCode == null || cardName == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = diamondWithDrawAdminService.transferByCard(billId,cardNumber,openBankCode,cardName,getAdminId());
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transferByCard），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 红包提现转账
     * @param
     * @return
     */
    @RequestMapping(value = "/redPacketTransfer", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult redPacketTransfer(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/redPacketTransfer），转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult = null;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = redPacketWithDrawAdminService.transfer(ids,getAdminId());
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/redPacketTransfer），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 红包提现列表
     *
     * @param withDrawParam
     * @return
     */
    @RequestMapping(value = "/redPacketWithDrawlist", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getRedPacketWithDrawList(@RequestBody WithDrawParam withDrawParam) {
        logger.info("接口调用：（/admin/withdraw/redPacketWithDrawlist），红包提现列表接口,接口入参:{}", JSON.toJSONString(withDrawParam));
        BusiResult busiResult = null;
        if (withDrawParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = redPacketWithDrawAdminService.getList(withDrawParam);
        } catch (Exception e) {
            logger.error("红包提现列表接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("红包提现列表接口:（/admin/withdraw/redPacketWithDrawlist），,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 钻石提现列表
     *
     * @param withDrawParam
     * @return
     */
    @RequestMapping(value = "/diamondWithDrawlist", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getDiamondWithDrawList(@RequestBody WithDrawParam withDrawParam) {
        logger.info("接口调用：（/admin/withdraw/diamondWithDrawlist）钻石提现列表接口,接口入参:{}", JSON.toJSONString(withDrawParam));
        BusiResult busiResult = null;
        if (withDrawParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = diamondWithDrawAdminService.getList(withDrawParam);
        } catch (Exception e) {
            logger.error("钻石提现列表数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("钻石提现列表接口(/admin/withdraw/diamondWithDrawlist),接口出参：{}",JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 钻石提现汇总
     *
     * @param withDrawParam
     * @return
     */
    @RequestMapping(value = "/diamondWithDrawCount", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getDiamondWithDrawCount(WithDrawParam withDrawParam) {
        if (withDrawParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return diamondWithDrawAdminService.getCount(withDrawParam);
        } catch (Exception e) {
            logger.error("钻石提现列表汇总出错,错误原因:{}", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 红包提现汇总
     *
     * @param withDrawParam
     * @return
     */
    @RequestMapping(value = "/redPacketWithDrawCount", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getRedPacketWithDrawCount(WithDrawParam withDrawParam) {
        if (withDrawParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return redPacketWithDrawAdminService.getCount(withDrawParam);
        } catch (Exception e) {
            logger.error("钻石提现列表汇总出错,错误原因:{}", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 导出到Excel表格
     *
     * @param type (1.钻石提现，2.红包提现)
     * @return
     */
    @RequestMapping(value = "/withDrawExport", method = RequestMethod.GET)
    public void diamondWithDrawExport(Integer type, WithDrawParam withDrawParam, HttpServletResponse response) {
        logger.info("接口调用：（/admin/withdraw/withDrawExport），提现列表导出到EXCEL文件,接口入参:type:{},withDrawParam:{}", type,JSON.toJSONString(withDrawParam));
        BusiResult busiResult = null;
        OutputStream out = null;
        String fileName = "";
        try {
            if (Integer.valueOf("1").equals(type)) {//钻石提现
                fileName = "钻石提现";
                busiResult = diamondWithDrawAdminService.exportExcel(withDrawParam);
            } else {//红包提现
                fileName = "红包提现";
                busiResult=redPacketWithDrawAdminService.exportExcel(withDrawParam);
            }
            if (BusiStatus.SUCCESS.value()==(busiResult.getCode())) {
                HSSFWorkbook wb = (HSSFWorkbook) busiResult.getData();
                out = response.getOutputStream();
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(fileName+".xls", "UTF-8"));
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

    /**
     * 后台手动红包提现
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/redPacketWithDraw", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult redPacketWithDraw(Long erbanNo, Integer packetId) {
        logger.info("接口调用：（/admin/withdraw/redPacketWithDraw）后台手动红包提现接口,接口入参:erbanNo:{},packetId:{}",erbanNo,packetId);
        BusiResult busiResult = null;
        if (erbanNo == null || packetId == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Users user = usersService.getUsersByErBanNo(erbanNo);
        if(user==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if(StringUtils.isBlank(user.getAlipayAccount()) || StringUtils.isBlank(user.getAlipayAccountName())){
            return new BusiResult(BusiStatus.ADMIN_USERS_NOTBINDALIYAPACCOUNT);
        }
        try {
            busiResult = redPacketWithDrawService.withDraw(user.getUid(),packetId);
        } catch (Exception e) {
            logger.error("后台手动红包提现数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("后台手动红包提现接口（/admin/withdraw/redPacketWithDraw）,接口出参:{}",JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 红包产品  findWithDrawList
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/redPacketWithDrawProdList", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult findWithDrawList() {
        logger.info("接口调用：（/admin/withdraw/redPacketWithDrawProdList）后台手动红包提现接口,接口入参:无");
        BusiResult busiResult = null;
        try {
            busiResult = redPacketWithDrawService.findWithDrawList();
        } catch (Exception e) {
            logger.error("后台手动红包提现数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("后台手动红包提现接口（/admin/withdraw/redPacketWithDrawProdList）,接口出参:{}",JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     *  冻结
     */
    @RequestMapping(value = "/frozen", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult frozen(Long uid) {
        if (uid == null || uid==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            return diamondWithDrawAdminService.frozen(uid,getAdminId());
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     *  解冻
     */
    @RequestMapping(value = "/unfrozen", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult unfrozen(Long uid) {
        if (uid == null || uid==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            return diamondWithDrawAdminService.unfrozen(uid,getAdminId());
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 冻结钻石提现
     * @param
     * @return
     */
    @RequestMapping(value = "/multiFrozen", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult multiFrozen(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return diamondWithDrawAdminService.multiFrozen(ids,getAdminId());
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 解冻钻石提现
     * @param
     * @return
     */
    @RequestMapping(value = "/multiUnFrozen", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult multiUnFrozen(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return diamondWithDrawAdminService.multiUnFrozen(ids,getAdminId());
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }


    @RequestMapping("listBankCard")
    @ResponseBody
    public BusiResult getBankCardList(Long uid) {
        return diamondWithDrawAdminService.getBankCardList(uid);
    }


    @RequestMapping(value = "/getBankCount", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getBankCount(Long uid) {
        return diamondWithDrawAdminService.getBankCount(uid);
    }



    /**
     * 红包微信提现转账
     * @param
     * @return
     */
    @RequestMapping(value = "/transfeRedPacketByWx", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferRedPacketByWx(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/transfeRedPacketByWx），红包微信转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult = null;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = redPacketWithDrawAdminService.transfer(ids,getAdminId(),1);
        } catch (Exception e) {
            logger.error("红包微信提现转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transfeRedPacketByWx），红包微信转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 红包支付宝提现转账
     * @param request
     * @return
     */
    @RequestMapping(value = "/transferRedPacketByAlipay", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferRedPacketByAlipay(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/transferRedPacketByAlipay），红包支付宝转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult = null;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = redPacketWithDrawAdminService.transfer(ids,getAdminId(),2);
        } catch (Exception e) {
            logger.error("红包支付宝提现转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transferRedPacketByAlipay），红包支付宝转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 红包提现银行卡转账
     * @param request
     * @return
     */
    @RequestMapping(value = "/transferRedPacketByCar", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferRedPacketByCar(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/transferRedPacketByCar），红包银行卡批量转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult = null;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = redPacketWithDrawAdminService.transfer(ids,getAdminId(),3);
        } catch (Exception e) {
            logger.error("红包提现银行卡转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transferRedPacketByCar），红包银行卡批量转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 红包银行卡转账
     * @param recordId
     * @param cardNumber
     * @param openBankCode
     * @return
     */
    @RequestMapping(value = "/transferRedPacketByCard", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult transferRedPacketByCard(String recordId,String cardNumber,String openBankCode,String cardName) {
        logger.info("接口调用：（/admin/withdraw/transferRedPacketByCard），红包银行卡转账接口,recordId:{},cardNumber:{},openBankCode:{},cardName:{}", recordId,cardNumber,openBankCode,cardName);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (recordId == null || cardNumber == null || openBankCode == null || cardName == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = redPacketWithDrawAdminService.transferByCard(recordId,cardNumber,openBankCode,cardName,getAdminId());
        } catch (Exception e) {
            logger.error("红包银行卡转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/transferRedPacketByCard），红包银行卡转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 汇聚银行卡转账
     * @param request
     * @return
     */
    @RequestMapping(value = "/joinpay/transferByCar", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult joinTransferByCar(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/joinpay/transferByCar），转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = diamondWithDrawAdminService.joinTransfer(ids,getAdminId());
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/joinpay/transferByCar），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 汇聚银行卡转账
     * @param billId
     * @param cardNumber
     * @param openBankCode
     * @return
     */
    @RequestMapping(value = "/joinpay/transferByCard", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult joinTransferByCard(String billId,String cardNumber,String openBankCode,String cardName) {
        logger.info("接口调用：（/admin/withdraw/joinpay/transferByCard），转账接口,billId:{},cardNumber:{},openBankCode:{},cardName:{}", billId,cardNumber,openBankCode,cardName);
        BusiResult busiResult;
        if (billId == null || cardNumber == null || openBankCode == null || cardName == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = diamondWithDrawAdminService.joinTransferByCard(billId,cardNumber,openBankCode,cardName,getAdminId());
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/joinpay/transferByCard），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 汇聚银行卡红包转账
     * @param request
     * @return
     */
    @RequestMapping(value = "/joinpay/joinRedPacketTransferByCar", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult joinRedPacketTransferByCar(HttpServletRequest request) {
        List<String> ids = getRequestArray(request, "ids", String.class);
        logger.info("接口调用：（/admin/withdraw/joinpay/joinRedPacketTransferByCar），转账接口,接口入参:{}", JSON.toJSONString(ids));
        BusiResult busiResult;
        if (ids == null || ids.size()==0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = redPacketWithDrawAdminService.joinTransfer(ids,getAdminId());
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/joinpay/joinRedPacketTransferByCar），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 汇聚银行卡红包转账
     * @param recordId
     * @param cardNumber
     * @param openBankCode
     * @return
     */
    @RequestMapping(value = "/joinpay/joinRedPacketTransferByCard", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult joinRedPacketTransferByCard(String recordId,String cardNumber,String openBankCode,String cardName) {
        logger.info("接口调用：（/admin/withdraw/joinpay/joinRedPacketTransferByCard），转账接口,billId:{},cardNumber:{},openBankCode:{},cardName:{}", recordId,cardNumber,openBankCode,cardName);
        BusiResult busiResult;
        if (recordId == null || cardNumber == null || openBankCode == null || cardName == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = redPacketWithDrawAdminService.joinTransferByCard(recordId,cardNumber,openBankCode,cardName,getAdminId());
        } catch (Exception e) {
            logger.error("转账接口,数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
            busiResult.setMessage(e.getMessage());
        }
        logger.info("转账接口:（/admin/withdraw/joinpay/joinRedPacketTransferByCard），转账接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

}
