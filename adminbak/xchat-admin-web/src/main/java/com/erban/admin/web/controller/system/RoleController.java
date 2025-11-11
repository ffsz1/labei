package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.model.AdminRole;
import com.erban.admin.main.service.system.AdminMenuService;
import com.erban.admin.main.service.system.AdminRoleService;
import com.erban.admin.main.vo.MenuVO;
import com.erban.admin.main.vo.RoleMenuVO;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {
    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminMenuService adminMenuService;


    /**
     *
     */
    @RequestMapping("/getall")
    @ResponseBody
    public void getAll(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<AdminRole> pageInfo = adminRoleService.getRoleWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping("/saveRole")
    @ResponseBody
    public void saveRole(AdminRole adminRole, boolean isEdit) {
        int result = -1;
        if (adminRole != null) {
            try {
                result = adminRoleService.saveRole(adminRole, isEdit);
            } catch (Exception e) {
                logger.warn("saveRole fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/getRole")
    @ResponseBody
    public void getRole(Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            AdminRole adminRole = adminRoleService.getRoleById(id);
            if (adminRole != null) {
                jsonObject.put("entity", adminRole);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/delRoles")
    @ResponseBody
    public void delRole(Integer id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                adminRoleService.delRoleById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                logger.warn("delRole fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/getMenuByRole")
    @ResponseBody
    public void getMenuByRole(Integer roleId){
        JSONObject jsonObject = new JSONObject();
        if(roleId != null){
            List<RoleMenuVO> roleMenuVOS = adminRoleService.queryByRoleMenuLisy(roleId);
            if (roleMenuVOS != null) {
                jsonObject.put("roles", roleMenuVOS);
            }
        }
        jsonObject.put("result", 0);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/saveRoleMenu")
    @ResponseBody
    public void saveRoleMenu(MenuVO menuVO){
        JSONObject jsonObject = new JSONObject();
        int result = 0;
        //根据角色ID删除角色权限数据
        int status = adminMenuService.delRoleMenuByRoleId(menuVO.getRoleId());
        // 保存新的角色权限数据
        result = adminRoleService.insertRoleMenu(menuVO);
        if(result > 0){
            jsonObject.put("result", 1);
        }else{
            jsonObject.put("result", result);
        }
        writeJson(jsonObject.toJSONString());
    }

}
