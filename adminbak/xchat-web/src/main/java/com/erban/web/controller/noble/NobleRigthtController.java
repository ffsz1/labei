package com.erban.web.controller.noble;

import com.erban.main.model.NobleRight;
import com.erban.main.service.ChargeService;
import com.erban.main.service.noble.NoblePayService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 贵族开通或者续费
 *
 */
@Controller
@RequestMapping("/noble/right")
public class NobleRigthtController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(NobleRigthtController.class);

    @Autowired
    private NobleRightService nobleRightService;


    /**
     * 获取指定的贵族特权信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get")
    @ResponseBody
    public BusiResult getOne(Integer id) {
        if (id == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try{
            NobleRight nobleRight = nobleRightService.getNobleRight(id);
            return new BusiResult(BusiStatus.SUCCESS, nobleRight);
        } catch (Exception e) {
            logger.error("getOne error, id: " + id, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 获取所有的贵族特权信息
     *
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public BusiResult getList() {
        try {
            List<NobleRight> list = nobleRightService.getNobleRightList();
            return new BusiResult(BusiStatus.SUCCESS, list);
        } catch (Exception e) {
            logger.error("getList error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }



}
