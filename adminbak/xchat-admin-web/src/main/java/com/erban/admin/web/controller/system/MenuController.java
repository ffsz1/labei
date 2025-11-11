package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.Combobox;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.model.AdminMenu;
import com.erban.admin.main.service.system.AdminMenuService;
import com.erban.admin.main.vo.AdminMenuVo;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/menu")
public class MenuController extends BaseController {
    @Autowired
    private AdminMenuService adminMenuService;

    /**
     * 获取所有菜单导航栏
     */
    @RequestMapping("/getall")
    @ResponseBody
    public void getAllMenu() {
        JSONObject jsonObject = new JSONObject();
        int adminId = getAdminId();
        if (adminId > -1) {
            List<AdminMenuVo> list = adminMenuService.getAllMenu(adminId);
            if (BlankUtil.isBlank(list)) {
                jsonObject.put("result", 1);
            } else {
                jsonObject.put("result", 0);
                List<AdminMenuVo> parents = Lists.newArrayList();
                List<AdminMenuVo> childs = Lists.newArrayList();
                for (AdminMenuVo adminMenu : list) {
                    if (adminMenu.getParentid() == 0) {
                        parents.add(adminMenu);
                    } else {
                        childs.add(adminMenu);
                    }
                }
                Collections.sort(parents, new Comparator<AdminMenuVo>() {
                    @Override
                    public int compare(AdminMenuVo o1, AdminMenuVo o2) {
                        return o1.getShoworder() - o2.getShoworder();
                    }
                });
                Collections.sort(childs, new Comparator<AdminMenuVo>() {
                    @Override
                    public int compare(AdminMenuVo o1, AdminMenuVo o2) {
                        return o1.getShoworder() - o2.getShoworder();
                    }
                });
                jsonObject.put("parents", parents);
                jsonObject.put("childs", childs);
            }
        } else {
            jsonObject.put("result", ErrorCode.NOT_LOGIN);
        }
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 查询具体某个菜单列表
     *
     * @param searchText 菜单名称
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public void getMenuList(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<AdminMenuVo> pageInfo = adminMenuService.getMenuByPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 根据菜单ID查询对应的菜单信息
     *
     * @param id
     */
    @RequestMapping("/getone")
    @ResponseBody
    public void getMenuById(Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            AdminMenu adminMenu = adminMenuService.getAdminMenuById(id);
            if (adminMenu != null) {
                jsonObject.put("entity", adminMenu);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 保存菜单栏
     *
     * @param adminMenu 菜单信息
     * @param isEdit    是否为修改
     */
    @RequestMapping("/save")
    @ResponseBody
    public void saveMenu(AdminMenu adminMenu, boolean isEdit) {
        int result = -1;
        if (adminMenu != null) {
            try {
                result = adminMenuService.saveMenu(adminMenu, isEdit);
            } catch (Exception e) {
                logger.warn("saveMenu fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    /**
     * 删除菜单栏
     *
     * @param id 菜单ID
     */
    @RequestMapping("/del")
    @ResponseBody
    public void delMenus(Integer id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                adminMenuService.delMenuById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                logger.warn("delMenus fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    /**
     * 获取所有父级菜单, 转换成组合框结构
     */
    @RequestMapping("/getparentlist")
    @ResponseBody
    public void getParentMenus() {
        String result = "[{txt:'--  无  --',val:'0'}]";
        try {
            List<AdminMenu> list = adminMenuService.getParentMenus();
            result = JSONObject.toJSONString(tranToCombobox(list));
        } catch (Exception e) {
            logger.warn("getParentMenus fail,cause by " + e.getMessage(), e);
        }
        writeJson(result);
    }

    /**
     * 转换组合框结构
     *
     * @param list 菜单集合
     * @return
     */
    private List<Combobox> tranToCombobox(List<AdminMenu> list) {
        List<Combobox> boxs = new ArrayList<Combobox>();
        Combobox box = null;
        box = new Combobox();
        box.setOtxt("--  无  --");
        box.setOval("0");
        boxs.add(box);
        if (!BlankUtil.isBlank(list))
            for (AdminMenu menu : list) {
                box = new Combobox();
                box.setOtxt(menu.getName());
                box.setOval(menu.getId() + "");
                boxs.add(box);
            }
        return boxs;
    }
}
