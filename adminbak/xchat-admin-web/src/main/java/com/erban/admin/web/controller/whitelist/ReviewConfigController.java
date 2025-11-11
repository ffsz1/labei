package com.erban.admin.web.controller.whitelist;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.AndroidReviewConfigService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.ReviewConfigDTO;
import com.erban.main.model.ReviewConfig;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chris
 * @Title:
 * @date 2018/10/22
 * @time 15:35
 */
@Controller
@RequestMapping("/admin/review/config")
public class ReviewConfigController extends BaseController {

    @Autowired
    private AndroidReviewConfigService androidReviewConfigService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(ReviewConfig reviewConfig, boolean isEdit )
    {
        if(reviewConfig == null) {
            writeJson(false, "参数有误");
            return;
        } else {
            try {
                int result = androidReviewConfigService.save(reviewConfig, isEdit);
                if(result>0) {
                    writeJson(true, "保存成功");
                    return;
                }
            } catch(Exception e) {
                e.printStackTrace();
                logger.error("Failed to save reviewConfigService. Cause by {}", e.getCause().getMessage());
            }
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public void del(Integer id) {
        try {
            int result = androidReviewConfigService.deleteById(id);
            if(result>0) {
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete reviewConfigService, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id")Integer id)
    {
        JSONObject jsonObject = new JSONObject();
        ReviewConfigDTO androidReviewConfigDTO = androidReviewConfigService.getById(id);
        if(androidReviewConfigDTO != null) {
            jsonObject.put("entity", androidReviewConfigDTO);
        }
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<ReviewConfigDTO> pageInfo  = androidReviewConfigService.getList(searchText,getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/getTagList")
    @ResponseBody
    public BusiResult getTagList(){
        return new BusiResult(BusiStatus.SUCCESS, androidReviewConfigService.getTagList());
    }

    @RequestMapping("/getTypeByChannels")
    @ResponseBody
    public BusiResult getTypeByChannels(Integer systemId){
        return new BusiResult(BusiStatus.SUCCESS,androidReviewConfigService.selectTypeByChannels(systemId));
    }
}
