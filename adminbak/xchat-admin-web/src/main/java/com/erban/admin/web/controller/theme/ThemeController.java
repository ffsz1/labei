package com.erban.admin.web.controller.theme;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.MerchantApplyResultRecord;
import com.erban.admin.main.service.theme.ThemeService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Theme;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/theme")
public class ThemeController extends BaseController {

    @Autowired
    private ThemeService themeService;

    @RequestMapping(value = "getList")
    public void getList(String state) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Theme> pageInfo = themeService.getListWithPage(state, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 删除话题
     *
     * @param request
     */
    @RequestMapping(value = "del")
    public void del(HttpServletRequest request) {
        List<Long> ids = getRequestArray(request, "ids", Long.class);
        if (CollectionUtils.isEmpty(ids)) {
            writeJson(false, "参数有误");
            return;
        }
        try {
            int result = themeService.delete(ids.toArray(new Long[]{}));
            writeJson(true, "删除成功");
            return;
        } catch (Exception e) {
            logger.error("Failed to delete gift, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    /**
     * 获取单个话题
     *
     * @param id
     */
    @RequestMapping(value = "get")
    public Map get(@RequestParam("id") Long id) {
        Map<String,Object> map =new HashMap<>();
        Theme theme = themeService.get(id);
        if (theme != null) {
            map.put("entity", theme);
        }
        return map;
    }


    /**
     * 保存、更新
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(Theme theme) {
            if(theme==null) return;
            int result = themeService.save(theme);
            if (result <= 0) {
                return;
            }
            if (theme.getId() == null) {
                writeJson(true, "保存成功");
            }else{
                writeJson(true, "更新成功");
            }
    }
}
