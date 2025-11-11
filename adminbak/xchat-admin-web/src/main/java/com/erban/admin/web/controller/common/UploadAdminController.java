package com.erban.admin.web.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.web.base.BaseController;
import com.erban.main.service.api.QiniuService;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class UploadAdminController extends BaseController {

    @Autowired
    private QiniuService qiuniuService;


    @RequestMapping(value = "/upload/img",method = RequestMethod.POST)
    @ResponseBody
    public void uploadImage(@RequestParam("uploadFile") MultipartFile uploadFile){
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if(!uploadFile.isEmpty()){
            try{
                String filePath = qiuniuService.uploadByStream(uploadFile.getInputStream());
                if(!BlankUtil.isBlank(filePath)){
                    jsonObject.put("path",qiuniuService.mergeUrlAndSlim(filePath));
                }
            } catch (Exception e){
                logger.error("uploadImage fail, "+ e.getMessage(),e);
                msg = "上传失败，I/O流异常";
            }
        }else{
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg",msg);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 上传
     */
    @RequestMapping(value = "/upload/img2",method = RequestMethod.POST)
    @ResponseBody
    public void uploadImage(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("fileName")String fileName){
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if(!uploadFile.isEmpty()){
            try{
                String filePath = null;
                if (BlankUtil.isBlank(fileName)) {
                    filePath = qiuniuService.uploadByStream(uploadFile.getInputStream());
                } else {
                    filePath = qiuniuService.uploadByStream(uploadFile.getInputStream(), fileName);
                }
                if(!BlankUtil.isBlank(filePath)){
                    jsonObject.put("path",qiuniuService.mergeUrlAndSlim(filePath));
                }
            } catch (Exception e){
                logger.error("uploadImage fail, "+ e.getMessage(),e);
                msg = "上传失败，I/O流异常";
            }
        }else{
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg",msg);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 上传文件，文件路径不加瘦身参数
     *
     * @param uploadFile
     * @param fileName
     */
    @RequestMapping(value = "/upload/file",method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("fileName")String fileName){
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if(!uploadFile.isEmpty()){
            try{
                String filePath = null;
                if (BlankUtil.isBlank(fileName)) {
                    filePath = qiuniuService.uploadByStream(uploadFile.getInputStream());
                } else {
                    filePath = qiuniuService.uploadByStream(uploadFile.getInputStream(), fileName);
                }
                if(!BlankUtil.isBlank(filePath)){
                    jsonObject.put("path",qiuniuService.mergeUrl(filePath));
                }
            } catch (Exception e){
                logger.error("uploadFile fail, "+ e.getMessage(),e);
                msg = "上传失败，I/O流异常";
            }
        }else{
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg",msg);
        writeJson(jsonObject.toJSONString());
    }
}
