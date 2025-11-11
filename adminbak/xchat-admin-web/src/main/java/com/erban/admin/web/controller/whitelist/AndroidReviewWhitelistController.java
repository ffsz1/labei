package com.erban.admin.web.controller.whitelist;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.AndroidReviewWhitelistService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.AndroidReviewWhitelistDTO;
import com.erban.main.model.AndroidReviewWhitelist;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chris
 * @Title:
 * @date 2018/10/22
 * @time 15:35
 */
@Controller
@RequestMapping("/admin/android/reviewWhitelist")
public class AndroidReviewWhitelistController extends BaseController {

    @Autowired
    private AndroidReviewWhitelistService androidReviewWhitelistService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(AndroidReviewWhitelist androidReviewWhitelist)
    {
        if(androidReviewWhitelist == null) {
            writeJson(false, "参数有误");
            return;
        } else {
            try {
                int result = androidReviewWhitelistService.save(androidReviewWhitelist);
                if(result>0) {
                    writeJson(true, "保存成功");
                    return;
                }
            } catch(Exception e) {
                e.printStackTrace();
                logger.error("Failed to save androidReviewWhitelist. Cause by {}", e.getCause().getMessage());
            }
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public void del(Integer id) {
        try {
            int result = androidReviewWhitelistService.deleteById(id);
            if(result>0) {
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete androidReviewWhitelist, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }




    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<AndroidReviewWhitelistDTO> pageInfo  = androidReviewWhitelistService.getList(searchText,getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


}
