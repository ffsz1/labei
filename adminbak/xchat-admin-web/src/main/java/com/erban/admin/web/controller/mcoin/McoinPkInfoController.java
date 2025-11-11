package com.erban.admin.web.controller.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.McoinPkInfoService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.McoinPkInfo;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@Controller
@RequestMapping("/admin/mcoin/pk")
public class McoinPkInfoController extends BaseController {

    @Autowired
    private McoinPkInfoService mcoinPkInfoService;



    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(String term, int pkStatus) {

        Pattern pattern = compile("[0-9]*");
        Matcher isNum = pattern.matcher(term);
        if( !isNum.matches() ){
            term = "0";
        }

        int termInt = Integer.parseInt(term);

        PageInfo<McoinPkInfo> pageInfo = mcoinPkInfoService.getList(getPageNumber(), getPageSize(), termInt, pkStatus);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }



    @RequestMapping(value = "/save")
    @ResponseBody
    public void saveMcoinPkInfo(McoinPkInfo mcoinPkInfo) {
        int result = -1;
        if (mcoinPkInfo == null) {
            result = ErrorCode.ERROR_NULL_ARGU;
            writeJsonResult(result);
            return;
        }

        McoinPkInfo info = mcoinPkInfoService.findByPkStatus((byte)1);
        if (null != info && mcoinPkInfo.getPkStatus().equals((byte)1)){
            //String resultStr = "已经存在状态为‘正在进行’的活动，只能存在一个‘正在进行’的活动";
            writeJsonResult(20003);
            return;
        }

        try {
            result = mcoinPkInfoService.saveMcoinPkInfo(mcoinPkInfo);
        } catch (Exception e) {
            logger.warn("saveMcoinPkInfo fail,cause by " + e.getMessage(), e);
            result = ErrorCode.SERVER_ERROR;
        }
        writeJsonResult(result);
    }

    @RequestMapping(value = "/getOne")
    @ResponseBody
    public void getOne(Long id) {
        JSONObject jsonObject = new JSONObject();
        if (!BlankUtil.isBlank(id)) {
            try {
                McoinPkInfo mcoinPkInfo = mcoinPkInfoService.getOne(id);
                if (mcoinPkInfo != null) {
                    jsonObject.put("entity", mcoinPkInfo);
                }
            } catch (Exception e) {
                logger.warn("getBanner fail,cause by " + e.getMessage(), e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public void updateMcoinPkInfo(McoinPkInfo mcoinPkInfo) {
        int result = -1;

        McoinPkInfo info = mcoinPkInfoService.findByPkStatus((byte)1);
        /*if (null != info && mcoinPkInfo.getPkStatus().equals((byte)1)){
            //String resultStr = "已经存在状态为‘正在进行’的活动，只能存在一个‘正在进行’的活动";
            writeJsonResult("20003");
            return;
        }*/
        if(null == mcoinPkInfo){
            writeJsonResult(ErrorCode.ERROR_NULL_ARGU);
            return;
        }

        if (mcoinPkInfo!= null) {

            try {
                mcoinPkInfo.setPkStatus(mcoinPkInfo.getPkStatus());
                result = mcoinPkInfoService.updateMcoinPkInfo(mcoinPkInfo);
            } catch (Exception e) {
                logger.warn("updateMcoinPkInfo fail,cause by " + e.getMessage(), e);
            }
        }else{

            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }
}

