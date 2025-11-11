package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.model.AdminRefUserRoleKey;
import com.erban.admin.main.model.AdminRole;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminRoleService;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.admin.main.vo.AdminRoleVo;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.util.AdminUtil;
import com.erban.admin.web.util.ImageType;
import com.erban.main.service.api.QiniuService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.EncrytcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {


    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminRoleService adminRoleService;

    /**
     * 查询用户列表
     *
     * @param searchText 用户名称
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public void getUsers(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<AdminUser> pageInfo = adminUserService.getUserWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/getone")
    @ResponseBody
    public void getAdminUser(Integer uid) {
        JSONObject jsonObject = new JSONObject();
        if (!BlankUtil.isBlank(uid)) {
            try {
                AdminUser adminUser = adminUserService.getAdminUserById(uid);
                if (adminUser != null) {
                    jsonObject.put("entity", adminUser);
                }
            } catch (Exception e) {
                logger.warn("getAdminUser fail,cause by " + e.getMessage(), e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 保存/更新用户信息
     * @param adminUser
     * @param isEdit
     */
    @RequestMapping("/save")
    @ResponseBody
    public void saveUser(AdminUser adminUser, boolean isEdit) {
        int result = -1;
        if (adminUser != null) {
            try {
                adminUser.setLastlogin(new Date());
                adminUser.setStatus(true);
                adminUser.setPassword(EncrytcUtil.encodeMD5String(adminUser.getPassword()));
                result = adminUserService.saveUser(adminUser, isEdit);
            } catch (Exception e) {
                logger.warn("saveUser fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/del")
    @ResponseBody
    public void delUsers(Integer id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                adminUserService.delUserById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                logger.warn("delUser fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/getrole")
    @ResponseBody
    public void getRoleByUser(Integer uid) {
        JSONObject jsonObject = new JSONObject();
        if (uid != null && uid > 0) {
            List<AdminRole> list = adminRoleService.getAllRole();
            List<AdminRefUserRoleKey> refUserRoles = adminRoleService.getRoleByAdminId(uid);
            List<AdminRoleVo> roleVos = Lists.newArrayList();
            for (AdminRole adminRole : list) {
                AdminRoleVo adminRoleVo = new AdminRoleVo();
                adminRoleVo.setRole(false);
                adminRoleVo.setId(adminRole.getId());
                adminRoleVo.setName(adminRole.getName());
                for (AdminRefUserRoleKey adminRefUserRole : refUserRoles) {
                    if (adminRefUserRole.getRoleId() == adminRole.getId()) {
                        adminRoleVo.setRole(true);
                        break;
                    }
                }
                roleVos.add(adminRoleVo);
            }
            jsonObject.put("result", 0);
            jsonObject.put("list", roleVos);
        } else {
            jsonObject.put("result", 1);
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/role/save")
    @ResponseBody
    public void saveUserRole(Integer uid, Integer[] roles) {
        int result = -1;
        if (uid != null) {
            try {
                result = adminRoleService.saveUserRole(uid, roles);
            } catch (Exception e) {
                logger.warn("saveUserRole fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @Autowired
    private QiniuService qiniuService;

    /**
     * 上传图像，使用的是Spring的CommonsFileUpload组件，
     * 需要在mvc配置文件中配置一个MultipartResolver解析器实例
     *
     * @param uploadFile 上传的文件对象
     * @return
     */
    @RequestMapping("/headimg")
    @ResponseBody
    public void uploadImage(@RequestParam("uploadFile") MultipartFile uploadFile) {
        String msg = null;//记录上传过程中的提示信息
        JSONObject jsonObject = new JSONObject();
        if (!uploadFile.isEmpty()) {
            try {
                String filePath = qiniuService.uploadByStream(uploadFile.getInputStream());
                if (!BlankUtil.isBlank(filePath)) {
                    jsonObject.put("path", qiniuService.mergeUrlAndSlim(filePath));
                }
            } catch (Exception e) {
                logger.error("upload fail，" + e.getMessage());
                msg = "上传失败，I/O流异常！";
            }
        } else {
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg", msg);
        //代码执行到这里，说明上传已经失败
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 过滤文件
     *
     * @return
     */
    private String filterImgFile(String name, long size) {
        String msg = null;
        //过滤大小不符合要求的图片
        if (size < 2 * 1024 * 1024) {
            String type = AdminUtil.getFileType(name);
            //过滤类型不符合要求的图片
            if (!ImageType.contains(type)) {
                msg = "上传失败，图片类型不符合要求!";
            }
        } else {
            msg = "上传失败，图片大小超过允许的最大值2M!";
        }
        return msg;
    }
}
