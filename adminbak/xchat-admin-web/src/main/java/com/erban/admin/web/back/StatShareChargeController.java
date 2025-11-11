package com.erban.admin.web.back;


import com.erban.admin.main.service.StatShareChargeService;
import com.erban.main.model.StatShareCharge;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/statshare")
@Controller
public class StatShareChargeController {
    @Autowired
    private StatShareChargeService statShareChargeService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public String getShareCharge() {
        List<StatShareCharge> statShareCharges = statShareChargeService.getShareChargeLists();
        return gson.toJson(statShareCharges);
    }
}
