package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.UserBankCardDTO;
import com.erban.admin.main.service.user.UserBankCardService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/user/bankCard")
public class UserBankCardController extends BaseController {
    @Autowired
    private UserBankCardService userBankCardService;

    @RequestMapping("/findUserBankCard")
    @ResponseBody
    public void getAll(String noList) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<UserBankCardDTO> pageInfo = userBankCardService.findUserBankCard(noList, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getByUid(UserBankCardDTO userBankCardDTO) {
        return userBankCardService.save(userBankCardDTO);
    }

    @RequestMapping(value = "/use", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult use(Integer id) {
        return userBankCardService.use(id);
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getByUid(Integer id) {
        return userBankCardService.getByUid(id);
    }


    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult del(Integer id) {
        return userBankCardService.delete(id,getAdminId());
    }
}
