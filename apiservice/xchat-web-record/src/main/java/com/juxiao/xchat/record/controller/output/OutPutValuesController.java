package com.juxiao.xchat.record.controller.output;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.output.bo.OutputValueParam;
import com.juxiao.xchat.service.record.output.OutputValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chris
 * @Title:
 * @date 2018/10/17
 * @time 16:47
 */
@RequestMapping("outPut/values")
@RestController
@Api(value="产值报表controller",tags={"产值报表相关接口"})
public class OutPutValuesController {

    @Autowired
    private OutputValueService outputValueService;


    @PostMapping("getList")
    @ApiOperation("获取产值数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "os" , value = "平台(android,iOS)", required = true,dataType = "String"),
            @ApiImplicitParam(name = "linkedmeChannel" , value = "媒介()", required = true,dataType = "String"),
            @ApiImplicitParam(name = "gender" , value = "性别(1.男2.女0.其他)", required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "showType" , value = "显示数据(1.充值人数 2.充值金额 3.注册产值 4.付费产值 5.登陆人数)", required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "signBegin", value = "开始时间", required = true,dataType = "String"),
            @ApiImplicitParam(name = "signEnd",value = "结束时间", required = true,dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数", required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "medium" , value = "媒介", required = true,dataType = "String"),

    })
    public WebServiceMessage getList(OutputValueParam outputValueParam){
        return outputValueService.getList(outputValueParam);
    }

    @PostMapping("listRegister")
    @ApiOperation("获取注册用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkedmeChannel" , value = "媒介()", required = true,dataType = "String"),
            @ApiImplicitParam(name = "groupId" , value = "分组", required = true,dataType = "String"),
            @ApiImplicitParam(name = "date",value = "时间", required = true,dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数", required = true,dataType = "Integer")

    })
    public WebServiceMessage listRegister(OutputValueParam param) {
        //
        return outputValueService.listRegister(param);
    }

    @PostMapping("listCharge")
    @ApiOperation("获取用户充值记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkedmeChannel" , value = "媒介()", required = true,dataType = "String"),
            @ApiImplicitParam(name = "groupId" , value = "分组", required = true,dataType = "String"),
            @ApiImplicitParam(name = "date",value = "时间", required = true,dataType = "String"),
            @ApiImplicitParam(name = "days", value = "第几天", required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数", required = true,dataType = "Integer")

    })
    public WebServiceMessage listCharge(OutputValueParam param) {
        //
        return outputValueService.listCharge(param);
    }

    @PostMapping("getChannelList")
    @ApiOperation("获取媒介列表")
    public WebServiceMessage getChannelList(String groupId){
        return outputValueService.getChannelList(groupId);
    }
}
