package com.erban.admin.web.controller.charge;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.charge.RedeemCodeService;
import com.erban.admin.main.service.system.AdminLogService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.RedeemCode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/redeemcode")
public class RedeemCodeController extends BaseController {

    @Autowired
    private RedeemCodeService redeemCodeService2;
    @Autowired
    private AdminLogService adminLogService;

    /**
     *  查询兑换码，分页返回
     *
     * @param code
     * @param uid
     * @param status
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public void getRedeemCodeList(String code, Long uid, Integer status) {
        PageInfo<RedeemCode> pageInfo = redeemCodeService2.getRedeemCodeList(code, uid, status, getPageNumber(), getPageSize());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    /**
     * 生成兑换码
     *
     * @param num 指定生成的数量
     * @return
     */
    @RequestMapping(value = "/gener")
    @ResponseBody
    public BusiResult generRedeemCode(Integer num, long amount, Integer len) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (num == null || num < 1) {
            num = 10;
        }
        if (len == null || len < 8) {
            len = 16;
        }
        // 异步线程执行生成兑换码
        new GenerThread(num, amount, len).start();
        return busiResult;
    }


    class GenerThread extends Thread{

        private Integer num;
        private Long amount;
        private Integer len;

        public GenerThread(Integer num, Long amount, Integer len) {
            this.num = num;
            this.amount = amount;
            this.len = len;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            logger.info("generRedeemCode start, uid: {}, num: {}, amount: {}, len: {}", getAdminId(), num, amount, len);
            // 用于存储生成的兑换码
            List<String> codes = Lists.newArrayList();
            int tmp = 0;
            while (tmp < num) {
                RedeemCode redeemCode = new RedeemCode();
                redeemCode.setCode(redeemCodeService2.buildRandomCode(len));
                redeemCode.setAmount(amount);
                redeemCode.setCreateTime(new Date());
                redeemCode.setUseStatus(1);
                int result = redeemCodeService2.insertRedeemCode(redeemCode);
                if (result == 1) {
                    tmp++;
                    codes.add(redeemCode.getCode());
                }
            }
            long end = System.currentTimeMillis();
            logger.info("generRedeemCode end, gener num: {}, cast time: {}", codes.size(), (end-start)/1000);
            adminLogService.insertLog(getAdminId(), RedeemCodeController.class.getCanonicalName(), "generRedeemCode"
                    , "param===>>num:"+num+", amount:"+amount+",len:"+len+", cast time:"+ (end-start)/1000);
        }
    }
}
