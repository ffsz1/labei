package com.erban.admin.web.controller.advertise;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.advertise.AdvertiseAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Advertise;
import com.erban.main.service.api.QiniuService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/advertise")
public class AdvertiseAdminController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseAdminController.class);
    @Autowired
    AdvertiseAdminService advertiseAdminService;
    @Autowired
    QiniuService qiniuService;

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList()
    {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Advertise> pageInfo = advertiseAdminService.getAdvertiseByPage(getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(Advertise advertise)
    {
        try {
            int result = advertiseAdminService.save(advertise, advertise.getAdvId() != null);
            if(result>0) {
                writeJson(true, "保存成功");
                return;
            }
        } catch(Exception e) {
            logger.error("Failed to save advertise. Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public void del(HttpServletRequest request)
    {
        List<Integer> ids = getRequestArray(request, "ids", Integer.class);
        if(CollectionUtils.isEmpty(ids)){
            writeJson(false, "参数有误");
            return;
        }
        try {
            int result = advertiseAdminService.delete(ids.toArray(new Integer[]{}));
            if(result>0) {
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete advertise, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id")Integer id)
    {
        JSONObject jsonObject = new JSONObject();
        Advertise advertise = advertiseAdminService.get(id);
        if(advertise != null) {
            jsonObject.put("entity", advertise);
        }
        writeJson(jsonObject.toJSONString());
    }
}
