package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.user.PrettyNoAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.UserNoblePrettyNoApp;
import com.github.pagehelper.PageInfo;
import com.xchat.oauth2.service.model.PrettyErbanNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/1/5.
 * 靓号后台管理
 */

    @Controller
    @RequestMapping("/admin/*")
    @ResponseBody
    public class PrettyNoAdminController extends BaseController {

        @Autowired
        private PrettyNoAdminService prettyNoAdminService;

        @RequestMapping("/goodNoAdmin/list")
        @ResponseBody
        public void getList(Integer pageSize, Integer pageNum,Long goodNoId,Long oldNo, Byte status,Byte origin,
                                  String useDate,String outDate,String createDate){
            PageInfo<PrettyErbanNo> pageInfo = prettyNoAdminService.getPrettyNoList(pageSize,pageNum,goodNoId,oldNo,status,
                    origin,useDate,outDate,createDate);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",pageInfo.getTotal());
            jsonObject.put("rows",pageInfo.getList());
            writeJson(jsonObject.toJSONString());
        }


    //查询
    @RequestMapping("/getUsePrettyNo")
    @ResponseBody
    public PrettyErbanNo getUsePrettyNo(int rowId){
        return prettyNoAdminService.getUsePrettyNo(rowId);
    }


    /**
     * 新建靓号
     * @param prettyErbanNo  靓号
     * @param userId  拉贝号
     * @param isValid
     * @param startDate
     * @param endDate
     * @param origin  来源
     * @param isEffectType
     * @param addRemark
     * @return
     */
    @RequestMapping("/savePrettyNo")
    @ResponseBody
    public int savePrettyNo(Long prettyErbanNo, Long userId, byte isValid,String startDate,String endDate,byte origin , int isEffectType, String addRemark){
        return prettyNoAdminService.savePrettyNo(prettyErbanNo,userId,isValid,startDate,endDate,origin,isEffectType,addRemark);
    }


    /**
     * 分配靓号给用户
     * @param rowId 靓号ID
     * @param beautyNo 靓号
     * @param userId  拉贝号
     * @param startDate
     * @param endDate
     * @param origin
     * @param remark
     * @param isEffectType
     * @return
     */
    @RequestMapping("/usePrettyNo")
    @ResponseBody
    public int usePrettyNo(int rowId,Long beautyNo,Long userId,String startDate,String endDate,byte origin, String remark,int isEffectType){
        return prettyNoAdminService.usePrettyNo(rowId,beautyNo,userId,startDate,endDate,origin,remark,isEffectType);
    }

    //解绑
    @RequestMapping("/unBundlingPrettyNo")
    @ResponseBody
    public int unBundlingPrettyNo(int rowId){
        return prettyNoAdminService.unBundlingPrettyNo(rowId);
    }


    //删除
    @RequestMapping("/deletePrettyNo")
    @ResponseBody
    public int deletePrettyNo(int rowId){
        prettyNoAdminService.deletePrettyNo(rowId);
        return 1 ;
    }


    //审核接口
    @RequestMapping(value = "/savePrettyNoApplication",method = RequestMethod.POST )
    @ResponseBody
    public int checkPrettyNo(Integer rowId){
        return prettyNoAdminService.checkPrettyNo(rowId);
    }

    //审核分页查询
    @RequestMapping(value = "/PrettyNoApplicationList",method = RequestMethod.GET )
    @ResponseBody
    public void getPrettyNoAppList(Integer pageSize, Integer pageNum,Long prettyErbanNo, Long userErbanNo,byte approveResult){
        PageInfo<UserNoblePrettyNoApp> pageInfo = prettyNoAdminService.getPrettyNoAppList(pageSize,pageNum,prettyErbanNo,userErbanNo,approveResult);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    //删除申请
    @RequestMapping(value = "/deleteUserNobleApp",method = RequestMethod.POST )
    @ResponseBody
    public int deleteUserNobleApp(Integer rowId){
        if(rowId==null){
            return 2;//参数为空
        }
        return prettyNoAdminService.deleteUserNobleApp(rowId);
    }


}
