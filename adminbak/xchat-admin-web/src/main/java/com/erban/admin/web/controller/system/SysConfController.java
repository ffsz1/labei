package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.SysConf;
import com.erban.main.service.SysConfService;
import com.erban.main.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/sysConf")
public class SysConfController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SysConfController.class);
    @Autowired
    SysConfService sysConfService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(SysConf sysConf)
    {
        if(StringUtils.isEmpty(sysConf.getConfigId())) {
            writeJson(false, "参数有误");
            return;
        } else {
            try {
                // 判断该config_id是否重复
                boolean isEdit = sysConfService.countById(sysConf.getConfigId()) > 0;
                int result = sysConfService.save(sysConf, isEdit);
                if(result>0) {
                    writeJson(true, "保存成功");
                    return;
                }
            } catch(Exception e) {
                logger.error("Failed to save sysConf. Cause by {}", e.getCause().getMessage());
            }
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public void del(HttpServletRequest request)
    {
        List<String> ids = getRequestArray(request, "ids", String.class);
        if(CollectionUtils.isEmpty(ids)) {
            writeJson(false, "参数有误");
            return;
        }
        try {
            int result = sysConfService.deleteByIds(ids.toArray(new String[]{}));
            if(result>0) {
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete sysConf, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id")String id)
    {
        JSONObject jsonObject = new JSONObject();
        SysConf sysConf = sysConfService.getById(id);
        if(sysConf != null) {
            jsonObject.put("entity", sysConf);
        }
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList() {
        JSONObject jsonObject = new JSONObject();
        List<SysConf> result = sysConfService.getList();
        if(!CollectionUtils.isEmpty(result)) {
            jsonObject.put("total", result.size());
            jsonObject.put("rows", result);
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "getByList", method = RequestMethod.GET)
    @ResponseBody
    public void getByList(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<SysConf> result = sysConfService.getByList(searchText);
        jsonObject.put("total", result.size());
        jsonObject.put("rows", result);
        writeJson(jsonObject.toJSONString());
    }
}
