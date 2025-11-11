package com.erban.admin.web.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.BannerAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Banner;
import com.erban.main.service.api.QiniuService;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台管理--banner管理
 */

@Controller
@RequestMapping("/admin/banner")
public class BannerAdminController extends BaseController {
    @Autowired
    private BannerAdminService bannerAdminService;

    @Autowired
    private QiniuService qiuniuService;

    @RequestMapping(value = "/getBannerList", method = RequestMethod.GET)
    @ResponseBody
    public void getBanner(int skipType, int bannerStatus, Integer viewType) {
        PageInfo<Banner> pageInfo = bannerAdminService.getBannerList(getPageNumber(), getPageSize(), skipType,
                bannerStatus, viewType);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/delBannerList")
    @ResponseBody
    public void delBanner(Integer bannerId) {
        int result = 1;
        if (!BlankUtil.isBlank(bannerId)) {
            try {
                bannerAdminService.delBanner(bannerId);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                logger.warn("delBanner fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping(value = "/getOneBanner")
    @ResponseBody
    public void getOneBanner(Integer bannerId) {
        JSONObject jsonObject = new JSONObject();
        if (!BlankUtil.isBlank(bannerId)) {
            try {
                Banner banner = bannerAdminService.getOneBannerById(bannerId);
                if (banner != null) {
                    jsonObject.put("entity", banner);
                }
            } catch (Exception e) {
                logger.warn("getBanner fail,cause by " + e.getMessage(), e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public void saveBanner(Banner banner, boolean isEdit, String startTimeString, String endTimeString) {
        int result = -1;
        if (banner != null) {
            try {
                result = bannerAdminService.saveBanner(banner, isEdit, startTimeString, endTimeString);
            } catch (Exception e) {
                logger.warn("saveBanner fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping(value = "/headimg")
    @ResponseBody
    public void uploadImage(@RequestParam("uploadFile") MultipartFile uploadFile) {
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if (!uploadFile.isEmpty()) {
            try {
                String filepath = qiuniuService.uploadByStream(uploadFile.getInputStream());
                if (!BlankUtil.isBlank(filepath)) {
                    jsonObject.put("path", qiuniuService.mergeUrlAndSlim(filepath));
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
}
