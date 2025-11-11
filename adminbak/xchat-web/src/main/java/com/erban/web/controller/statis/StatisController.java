package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatisService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by liuguofu on 2017/7/20.
 */
@Controller
@RequestMapping("/statis")
public class StatisController {
    private static final Logger logger = LoggerFactory.getLogger(StatisController.class);
    @Autowired
    private StatisService statisService;
    @RequestMapping(value = "logininfo",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult loginInfoStatic(Long uid,String url){
        if(StringUtils.isEmpty(url)||uid==null||uid==0L){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        BusiResult<List<UserVo>> busiResult=null;
        try {
            busiResult= statisService.saveLoginInfoStatic(uid,url);
        } catch (Exception e) {
            logger.error("loginInfoStatic error..uid="+uid,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;

    }
}
