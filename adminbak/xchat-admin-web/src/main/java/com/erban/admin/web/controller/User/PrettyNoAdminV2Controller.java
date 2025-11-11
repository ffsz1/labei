package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.user.PrettyNoAdminV2Service;
import com.erban.admin.web.base.BaseController;
import com.erban.main.param.admin.PrettyErbanNoParam;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 新靓号管理controller,原有靓号管理controller不再使用
 * 靓号后台管理
 */

@Controller
@RequestMapping("/admin/prettyErbanNo")
@ResponseBody
public class PrettyNoAdminV2Controller extends BaseController {
    @Autowired
    private PrettyNoAdminV2Service prettyNoAdminV2Service;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getChargeRecordList(@RequestBody PrettyErbanNoParam prettyErbanNoParam) {
        logger.info("靓号列表接口,接口入参:{}", JSON.toJSONString(prettyErbanNoParam));
        BusiResult busiResult = null;
        if (prettyErbanNoParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = prettyNoAdminV2Service.getList(prettyErbanNoParam);
        } catch (Exception e) {
            logger.error("靓号列表接口出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("靓号列表接口,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 新建靓号
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/savePrettyNo", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult savePrettyNo(Long prettyErbanNo, Long erbanNo, Byte seq) {
        logger.info("新建靓号,接口入参:prettyErbanNo:{},erbanNo:{}", prettyErbanNo, erbanNo);
        BusiResult busiResult = null;
        if (prettyErbanNo == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = prettyNoAdminV2Service.save(prettyErbanNo, erbanNo, seq);
        } catch (Exception e) {
            logger.error("新建靓号接口出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }

        logger.info("新建靓号,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult delete(Long prettyErbanNo) {
        logger.info("删除靓号, 接口入参: prettyErbanNo: {}", prettyErbanNo);
        BusiResult busiResult = null;
        if (prettyErbanNo == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = prettyNoAdminV2Service.delete(prettyErbanNo);
        } catch (Exception e) {
            logger.error("删除靓号接口出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("删除靓号, 接口出参: {}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult bindPrettyNo(Long prettyErbanNo, Long erbanNo) {
        logger.info("绑定靓号,接口入参:prettyErbanNo:{},erbanNo:{}", prettyErbanNo, erbanNo);
        BusiResult busiResult = null;
        if (prettyErbanNo == null || erbanNo == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = prettyNoAdminV2Service.bind(prettyErbanNo, erbanNo);
        } catch (Exception e) {
            logger.error("绑定靓号接口出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }

        logger.info("绑定靓号,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }
}
