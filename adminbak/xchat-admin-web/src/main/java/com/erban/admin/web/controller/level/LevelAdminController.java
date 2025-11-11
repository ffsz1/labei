package com.erban.admin.web.controller.level;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.level.LevelAdminService;
import com.erban.admin.web.base.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 管理后台初始化 财富等级+魅力等级
 */
@Controller
@RequestMapping("/admin/level")
public class LevelAdminController extends BaseController{
    @Autowired
    private LevelAdminService levelAdminService;


    /**
     * 充值列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/initCache", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult init() {
        logger.info("初始化用户财富等级和魅力等级,接口入参:无");
        BusiResult busiResult = null;
        try {
            busiResult = levelAdminService.initLevelCash();
        } catch (Exception e) {
            logger.error("初始化用户财富等级和魅力等级出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("初始化用户财富等级和魅力等级,接口出参:{}",JSON.toJSONString(busiResult));
        return busiResult;
    }

}
