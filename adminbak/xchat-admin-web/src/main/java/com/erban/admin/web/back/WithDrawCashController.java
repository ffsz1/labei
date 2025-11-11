package com.erban.admin.web.back;


import com.erban.admin.main.service.WithDrawCashService;
import com.erban.main.vo.BillRecordVo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/withdrawcash")
public class WithDrawCashController {

    private Gson gson = new Gson();

    @Autowired
    private WithDrawCashService withDrawCashService;

    @RequestMapping(value = "/finish", method = RequestMethod.GET)
    @ResponseBody
    public String updateWithDrawStatus(String billId) {
        withDrawCashService.updateWithDrawStatus(billId);
        return "success";
    }

    @RequestMapping(value = "/getbilllist", method = RequestMethod.GET)
    @ResponseBody
    public String getBillRecord() {
        List<BillRecordVo> billRecordVos = withDrawCashService.getBillRecordList();
        if (billRecordVos == null) {
            return null;
        }
        return gson.toJson(billRecordVos);
    }

}
