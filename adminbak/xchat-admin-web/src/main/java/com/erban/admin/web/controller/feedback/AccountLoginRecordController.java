package com.erban.admin.web.controller.feedback;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.account.AccountLoginRecordService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.oauth2.service.model.AccountLoginRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/12/21.
 */
@Controller
@RequestMapping("/admin/*")
public class AccountLoginRecordController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AccountLoginRecordController.class);
    @Autowired
    private AccountLoginRecordService accountLoginRecordService;
    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = "accountLoginRecord/list",method = RequestMethod.GET)
    @ResponseBody
    public void getAccountBlockList(Integer pageSize,Integer pageNum,String phone,Long erbanNo,byte loginType){
        PageInfo<AccountLoginRecord> pageInfo = accountLoginRecordService.getAccountLoginRecordList(pageSize,pageNum,erbanNo,phone,loginType);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


}
