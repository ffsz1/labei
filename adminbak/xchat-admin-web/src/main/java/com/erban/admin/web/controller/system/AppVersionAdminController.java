package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.service.system.AppVersionAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.AppVersion;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
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

/**
 * Created by PaperCut on 2018/1/20.
 */
@Controller
@RequestMapping("/admin/version")
public class AppVersionAdminController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AppVersionAdminController.class);
    @Autowired
    AppVersionAdminService appVersionAdminService;
    @Autowired
    private JedisService jedisService;

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList()
    {
        JSONObject jsonObject = new JSONObject();
        PageInfo<AppVersion> pageInfo = appVersionAdminService.getVersionByPage(getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(AppVersion appVersion)
    {
        try {
            // 保存版本记录
            int result = appVersionAdminService.save(appVersion, appVersion.getVersionId() != null);
            if(result > 0) {
                Gson gson = new Gson();
                if(StringUtils.isNotBlank(appVersion.getAppid())){
                    jedisService.hwrite(RedisKey.app_version.getKey(),appVersion.getVersion()+appVersion.getOs().toLowerCase()+appVersion.getAppid(),gson.toJson(appVersion));
                }else{
                    jedisService.hwrite(RedisKey.app_version.getKey(),appVersion.getVersion()+appVersion.getOs().toLowerCase(),gson.toJson(appVersion));
                }
                writeJson(true, "保存成功");
                return;
            }
        } catch(BusinessException e) {
            writeJson(false, e.getMessage());
            return;
        } catch(Exception e) {
            logger.error("Failed to save appVersion. Cause by {}", e.getMessage());
        }
        writeJson(false, "保存失败");
    }



    @RequestMapping(value = "del")
    @ResponseBody
    public void del(HttpServletRequest request)
    {
        List<Integer> ids = getRequestArray(request, "ids", Integer.class);
        if(CollectionUtils.isEmpty(ids)) {
            writeJson(false, "参数有误");
            return;
        }
        AppVersion appVersion = appVersionAdminService.get(ids.get(0));
        if(appVersion==null){
            writeJson(false, "此版本数据不存在");
            return;
        }
        try {
            int result = appVersionAdminService.delete(ids.get(0));
            if(result>0) {
                Gson gson = new Gson();
                jedisService.hdel(RedisKey.app_version.getKey(),appVersion.getVersion()+appVersion.getOs());
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete appVersion, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id")Integer id)
    {
        JSONObject jsonObject = new JSONObject();
        AppVersion appVersion = appVersionAdminService.get(id);
        if(appVersion != null) {
            jsonObject.put("entity", appVersion);
        }
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value = "resetAudit", method = RequestMethod.POST)
    @ResponseBody
    public void resetAudit(@RequestParam("id")Integer id)
    {
        try {
            // 统计是否有审核状态中的记录
            int count = appVersionAdminService.countByStatus(Constant.AppVersion.audit);
            if(count > 0) {
                writeJson(false, "已存在审核状态中的版本记录.请修改后再试");
                return;
            }

            // 设置该版本记录为审核状态中
            int result = appVersionAdminService.resetAudit(id);
            if(result>0) {
                writeJson(true, "设置成功");
                return;
            }
        } catch(Exception e) {
            logger.error("Failed to resetAudit, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "设置失败");
    }

    /**
     * 置为强制更新或建议更新
     * @param type
     * @param request
     */
    @RequestMapping(value = "resetStatus", method = RequestMethod.POST)
    @ResponseBody
    public void resetStatus(@RequestParam("type")String type, HttpServletRequest request)
    {
        List<Integer> ids = getRequestArray(request, "ids", Integer.class);
        if(CollectionUtils.isEmpty(ids)) {
            writeJson(false, "参数有误");
            return;
        }
        try {
            int result = appVersionAdminService.resetStatus(type, ids);
            if(result > 0) {
                writeJson(true, "设置成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to updateStatus, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "设置失败");
    }
}
