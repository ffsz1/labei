package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;

import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.model.AdminDict;
import com.erban.admin.main.service.system.AdminDictService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/admin/dict")
public class DictController extends BaseController{
    @Autowired
    private AdminDictService adminDictService;

    /**
     * 查询字典列表
     * @param searchText 字典名称
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public void getDictList(String searchText){
        JSONObject jsonObject = new JSONObject();
        PageInfo pageInfo = adminDictService.getDictByPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/getone")
    @ResponseBody
    public void getOneDict(String code,String dictkey){
        JSONObject jsonObject = new JSONObject();
        if(!BlankUtil.isBlank(code) && !BlankUtil.isBlank(dictkey)){
            try {
                AdminDict adminDict = adminDictService.getOneAdminDict(code, dictkey);
                if (adminDict != null) {
                    jsonObject.put("entity", adminDict);
                }
            }catch (Exception e){
                logger.warn("getAdminDict fail,cause by "+e.getMessage(),e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/save")
    @ResponseBody
    public void saveDict(AdminDict adminDict, boolean isEdit){
        int result = -1;
        if(adminDict!=null){
            try {
                result = adminDictService.saveDict(adminDict, isEdit);
            }catch (Exception e){
                logger.warn("saveDict fail,cause by "+e.getMessage(),e);
            }
        }else{
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/del")
    @ResponseBody
    public void delDict(String code,String dictkey){
        int result = 1;
        if(!BlankUtil.isBlank(code) && !BlankUtil.isBlank(dictkey)){
            try {
                adminDictService.delAdminDict(code, dictkey);
            }catch (Exception e){
                result = ErrorCode.SERVER_ERROR;
                logger.warn("delDict fail,cause by "+e.getMessage(),e);
            }
        }else{
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }
}
