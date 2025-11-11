package com.erban.admin.web.back;

import com.erban.admin.main.service.system.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String login(String username, String password, HttpSession session) throws Exception {
//        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
//            return "../../login";
//        }
//        boolean flag = adminUserService.login(username, password);
//        if (flag) {
//            session.setAttribute("username", username);
//            return "../../index";
//        }
//        return "../../login";
//    }
}
