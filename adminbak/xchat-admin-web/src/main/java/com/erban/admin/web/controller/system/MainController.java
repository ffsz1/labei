package com.erban.admin.web.controller.system;

import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.admin.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class MainController extends BaseController {
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 跳转到管理首页
     *
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String toMain(Model model) {
        try {
            AdminUser adminUser = adminUserService.getAdminUserById(getAdminId());
            if (adminUser != null) {
                adminUser.setPassword("");
                model.addAttribute("adminUser", adminUser);
				// getRequest().setAttribute("adminUser", adminUser);
            }
        } catch (Exception e) {
            logger.error("toMain error, cause by " + e.getMessage(), e);
        }
        logger.info("main-----------------------");
        return "/main";
    }
}
