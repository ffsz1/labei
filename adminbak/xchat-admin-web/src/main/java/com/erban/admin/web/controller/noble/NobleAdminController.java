
package com.erban.admin.web.controller.noble;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.noble.NobleAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.NobleRes;
import com.erban.main.model.NobleRight;
import com.erban.main.model.Users;
import com.github.pagehelper.PageInfo;
import com.xchat.oauth2.service.http.HttpUitls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 贵族管理
 * Created by Administrator on 2018/1/19.
 */


    @Controller
    @RequestMapping("/admin/*")
    public class NobleAdminController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(NobleAdminController.class);

        @Autowired
        private NobleAdminService nobleAdminService;

    /**
     * 查询贵族
     * @return
     */
        @RequestMapping(value = "/nobleAdmin/getNobleRight", method = RequestMethod.GET)
        @ResponseBody
        public List<NobleRight> getNobleRight() {
           return nobleAdminService.getNobleRight();
        }

    /**
     * 查询普通用户
     * @param erbanNo
     * @return
     */
        @RequestMapping(value = "/nobleAdmin/getUser",method = RequestMethod.GET)
        @ResponseBody
        public void getNobleUser(Long erbanNo) {
            PageInfo<Users> pageInfo = nobleAdminService.getNobleUser(erbanNo);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",pageInfo.getTotal());
            jsonObject.put("rows",pageInfo.getList());
            writeJson(jsonObject.toJSONString());
            return ;
        }

    /**
     * 开通
     * @param erbanNo
     * @param nobleId
     * @return
     */
        @RequestMapping(value = "/nobleAdmin/openNoble",method = RequestMethod.POST)
        @ResponseBody
        public int openNoble(Long erbanNo, int nobleId) {
            int adminId =getAdminId();
            return nobleAdminService.openNoble(erbanNo,nobleId,adminId);
        }

    /**
     * 续费
     * @param erbanNo
     * @param nobleId
     * @return
     */
        @RequestMapping(value = "/nobleAdmin/renewNoble",method = RequestMethod.POST)
        @ResponseBody
        public int renewNoble(Long erbanNo, int nobleId) {
            int adminId =getAdminId();
            return nobleAdminService.renewNoble(erbanNo,nobleId,adminId);
        }


    }



