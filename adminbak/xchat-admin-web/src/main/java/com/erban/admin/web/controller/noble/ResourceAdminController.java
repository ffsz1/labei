
package com.erban.admin.web.controller.noble;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.ResourceAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.NobleRes;
import com.erban.main.service.api.QiniuService;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台消息管理
 * Created by fxw on 2018/01/02
 */


    @Controller
    @RequestMapping("/admin/*")
    public class ResourceAdminController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(ResourceAdminController.class);

    @Autowired
    private ResourceAdminService resourceAdminService;

    @Autowired
    private QiniuService qiuniuService;

    /**
     * 资源列表
     */
    @RequestMapping(value="resAdmin/list")
    @ResponseBody
    public void getList(Byte type,int nobleId){
        PageInfo<NobleRes> pageInfo = resourceAdminService.getList(getPageNumber(),getPageSize(),type,nobleId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    /**
     * 保存配置资源
     * @param
     * @return
     */

    @RequestMapping(value = "resAdmin/saveResource",method = RequestMethod.POST)
    @ResponseBody
    public int saveResource(int nobleId,String name,String res,String preview,Byte resType, Byte status,Byte isDyn,Byte isDef,int seq){
        return resourceAdminService.saveResource(nobleId,name,res,preview,resType,status,isDyn,isDef,seq);
        }


    /**
     * 查询资源用户
     */
    @RequestMapping(value = "resAdmin/getResList",method = RequestMethod.POST)
    @ResponseBody
    public NobleRes getResList(Integer rowId){
        return resourceAdminService.getResList(rowId);
    }

    /**
     * 编辑保存
     */
    @RequestMapping(value = "resAdmin/updateRes",method = RequestMethod.POST)
    @ResponseBody
    public int updateRes(Integer rowId,int nobleType,byte resType,byte isDyn,byte isDef,byte status,String resName,String res,String preview,int seq ){
        return resourceAdminService.updateRes(rowId,nobleType,resType,isDyn,isDef,status,resName,res,preview,seq);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "resAdmin/delRes",method = RequestMethod.POST)
    @ResponseBody
    public int delRes(Integer rowId){
        return resourceAdminService.delRes(rowId);
    }



    /**
     * 上传
     */
    @RequestMapping(value = "resAdmin/headimg",method = RequestMethod.POST)
    @ResponseBody
    public void uploadImage(@RequestParam("uploadFile") MultipartFile uploadFile){
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if(!uploadFile.isEmpty()){
            try{
                String filepath = qiuniuService.uploadByStream(uploadFile.getInputStream());
                if(!BlankUtil.isBlank(filepath)){
                    jsonObject.put("path",qiuniuService.mergeUrlAndSlim(filepath));
                }
            } catch (Exception e){
                logger.error("upload fail, "+ e.getMessage());
                msg = "上传失败，I/O流异常";
            }
        }else{
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg",msg);
        writeJson(jsonObject.toJSONString());
    }

}



