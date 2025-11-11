package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.web.base.BaseController;
import com.erban.main.service.api.QiniuService;
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

import javax.servlet.http.HttpServletResponse;

/**
 * 文件操作控制器
 * Created by PaperCut on 2018/1/30.
 */
@Controller
@RequestMapping("/admin/file/")
public class FileController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    QiniuService qiniuService;

    /**
     * 文件上传
     *
     * @param uploadFile
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public void upload(@RequestParam("file") MultipartFile uploadFile) {
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if (!uploadFile.isEmpty()) {
            try {
                String filepath = qiniuService.uploadByStream(uploadFile.getInputStream());
                if (!BlankUtil.isBlank(filepath)) {
                    jsonObject.put("path", qiniuService.mergeUrlAndSlim(filepath));
                }
            } catch (Exception e) {
                logger.error("upload fail, " + e.getMessage());
                msg = "上传失败，I/O流异常";
            }
        } else {
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg", msg);

        // 禁止缓存
        HttpServletResponse response = getResponse();
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * @param uploadFile
     * @param version
     * @desc 上传app文件
     */
    @RequestMapping(value = "uploadApp", method = RequestMethod.POST)
    @ResponseBody
    public void uploadApp(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("version") String version) {
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if (!uploadFile.isEmpty()) {
            try {
                long t = System.currentTimeMillis();
                String fileName = "hjxg_V" + version + "_" + t + ".apk";
                String filepath = qiniuService.uploadByStream(uploadFile.getInputStream(), fileName);
                if (!BlankUtil.isBlank(filepath)) {
                    jsonObject.put("path", qiniuService.mergeUrlAndSlim(filepath));
                }
            } catch (Exception e) {
                logger.error("upload fail, " + e.getMessage());
                msg = "上传失败，I/O流异常";
            }
        } else {
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg", msg);

        writeJson(jsonObject.toJSONString());
    }

    /**
     * 获取文件扩展名
     *
     * @return
     */
    public static String ext(String filename) {
        int index = filename.lastIndexOf(".");

        if (index == -1) {
            return null;
        }
        String result = filename.substring(index + 1);
        return result;
    }
}
