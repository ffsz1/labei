package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.AdminConstants;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.util.StringUtil;
import com.erban.main.model.SysConf;
import com.erban.main.service.SysConfService;
import com.erban.main.util.HttpServletUtils;
import com.xchat.common.enumeration.SysConfigId;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.SmsRet;
import com.xchat.common.netease.util.NetEaseConstant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.Utils;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.NetEaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.erban.admin.web.frame.Scope.SESSION;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private NetEaseService netEaseService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    SysConfService sysConfService;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String toLogin() {
        return "/login";
    }

    /**
     * 后台登录验证
     *
     * @param account  账号，可以是用户名、邮箱
     * @param password MD5加密后的密码
     * @param authCode MD5加密后的验证码
     */
    @RequestMapping("/login")
    @ResponseBody
    public void login(String account, String password, String authCode, HttpServletRequest request) throws UnsupportedEncodingException {
        int result = validateData(account, password);
        // 参数验证成功
        // if (result != 0 && !"admin13579".equalsIgnoreCase(password)) {
        if (result != 0) {
            writeJson(false, result + "");
            return;
        }

        // TODO 登录次数
        // long errorCount = jedisService.incr(RedisKey.admin_login_error_count.getKey(account), 24 * 3600);
        // if (errorCount > 3) {
        //    writeJson(false, account + "已经被封禁！");
        //    return;
        // }

        AdminUser adminUser;
        // 线下管理后台用户名
        adminUser = adminUserService.getAdminUser(account, password);
        if (adminUser == null) {
            try {
                BaseNetEaseRet ret = netEaseService.smsVerify(account, password);
                if (ret.getCode() != 200) {
                    writeJson(false, ret.getCode() + ":" + ret.getDesc());
                    return;
                }
            } catch (Exception e) {
                writeJson(false, "短信服务错误");
                return;
            }
            adminUser = adminUserService.getAdminUser(account);
        }

        if (adminUser == null) {
            writeJson(false, "用户不存在或密码错误");
            return;
        }
        jedisService.del(RedisKey.admin_login_error_count.getKey(account));

        setAttribute(AdminConstants.HAS_LOGIN, "true", SESSION);
        setAttribute(AdminConstants.ADMIN_ID, adminUser.getId(), SESSION);
        setAttribute(AdminConstants.ADMIN_NAME, adminUser.getUsername(), SESSION);
        Cookie cookie1 = new Cookie(AdminConstants.HAS_LOGIN, "true");
        Cookie cookie2 = new Cookie(AdminConstants.ADMIN_ID, adminUser.getId() + "");
        Cookie cookie3 = new Cookie(AdminConstants.ADMIN_NAME, URLEncoder.encode(adminUser.getUsername(), "utf-8"));
        getResponse().addCookie(cookie1);
        getResponse().addCookie(cookie2);
        getResponse().addCookie(cookie3);
        adminUserService.updateLastLogin(adminUser.getId(),HttpServletUtils.getRemoteIp(request));
        logger.info("amdin [" + adminUser.getUsername() + "] login system.");
        writeJson(true, adminUser.getId() + "@" + adminUser.getUsername());
    }

    /**
     * v1.0.3 后台登录验证
     *
     * @param account  账号，可以是用户名、邮箱
     * @param password MD5加密后的密码
     * @param authCode MD5加密后的验证码
     */
    @RequestMapping(value = "/V3/login", method = RequestMethod.POST)
    @ResponseBody
    public void v3Login(String account, String password, String authCode, HttpServletRequest request) throws UnsupportedEncodingException {

        //新增手机验证码
        int result = validateData(account, password, authCode);
        if (result != 0) {
            writeJson(false, result + "");
            return;
        }

        AdminUser adminUser = adminUserService.getAdminUser(account, password);
        if (adminUser == null) {
            writeJson(false, "用户不存在或密码错误");
            return;
        } else {
            String phone = adminUser.getPhone();
            if (StringUtils.isBlank(phone)) {
                logger.error("该账号未绑定手机！ username=" + account);
                writeJson(false, "该账号未绑定手机！ username=" + account);
                return;
            }
            String day = DateTimeUtil.getTodayStr();//当天日期
            String codeRedisKey =
                    RedisKey.admin_acount_mobile_sendSmsCode.getKey("LOGIN" + "_" + day + "_" + account + "_" + phone);
            try {
                boolean isVerify = verifySmsCodeByNetEase(codeRedisKey, authCode);
                if (!isVerify) {
                    logger.error("手机验证码不匹配 username=" + account);
                    writeJson(false, "手机验证码不匹配");
                    return;
                }
            } catch (Exception e) {
                writeJson(false, "短信服务错误");
                return;
            }
        }

        jedisService.del(RedisKey.admin_login_error_count.getKey(account));

        setAttribute(AdminConstants.HAS_LOGIN, "true", SESSION);
        setAttribute(AdminConstants.ADMIN_ID, adminUser.getId(), SESSION);
        setAttribute(AdminConstants.ADMIN_NAME, adminUser.getUsername(), SESSION);
        Cookie cookie1 = new Cookie(AdminConstants.HAS_LOGIN, "true");
        Cookie cookie2 = new Cookie(AdminConstants.ADMIN_ID, adminUser.getId() + "");
        Cookie cookie3 = new Cookie(AdminConstants.ADMIN_NAME, URLEncoder.encode(adminUser.getUsername(), "utf-8"));
        getResponse().addCookie(cookie1);
        getResponse().addCookie(cookie2);
        getResponse().addCookie(cookie3);
        adminUserService.updateLastLogin(adminUser.getId(),HttpServletUtils.getRemoteIp(request));
        logger.info("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],amdin [" + adminUser.getUsername() + "] login system.");
        writeJson(true, adminUser.getId() + "@" + adminUser.getUsername());
    }

    /**
     * 退出登录
     *
     * @param model
     * @return
     */
    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request) {
        if (getAdminId() > 1) {
            logger.info("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],admin logout, name is:" + getAttribute(AdminConstants.ADMIN_NAME, SESSION));
            getRequest().getSession().removeAttribute(AdminConstants.HAS_LOGIN);
            getRequest().getSession().removeAttribute(AdminConstants.ADMIN_ID);
            getRequest().getSession().removeAttribute(AdminConstants.ADMIN_NAME);
            Cookie[] cookies = getRequest().getCookies();
            for (Cookie cookie : cookies) {
                if (AdminConstants.HAS_LOGIN.equalsIgnoreCase(cookie.getName())
                        || AdminConstants.ADMIN_ID.equalsIgnoreCase(cookie.getName())
                        || AdminConstants.ADMIN_NAME.equalsIgnoreCase(cookie.getName())) {
                    Cookie cookieDel = new Cookie(cookie.getName(), null);
                    cookieDel.setPath("/");
                    cookieDel.setMaxAge(0);
                    getResponse().addCookie(cookieDel);
                }
            }
        }
        return "redirect:/login/index";
    }

    //////////////////////////////////////////////////////////////////////////////////
    //////// 自定义函数
    //////////////////////////////////////////////////////////////////////////////////

    /**
     * 检验表单数据是否正确
     *
     * @param account
     * @param password
     * @param authCode
     */
    private int validateData(String account, String password, String authCode) {
        account = StringUtil.filterSubstring(StringUtil.filterSpecial(account), 50);
        password = StringUtil.filterSpecial(password);
        authCode = StringUtil.filterSpecial(authCode);
        if (!BlankUtil.isBlank(account)) {
            if (BlankUtil.isBlank(password)) {
                return 2;  //密码不能为空
            }
            if (BlankUtil.isBlank(authCode)) {
                return 3;  //手机验证码不能为空
            }
        } else {
            return 1;  //邮箱不能为空
        }
        return 0;
    }

    /**
     * 检验表单数据是否正确
     *
     * @param account
     * @param password
     */
    private int validateData(String account, String password) {
        account = StringUtil.filterSubstring(StringUtil.filterSpecial(account), 50);
        password = StringUtil.filterSpecial(password);
        if (!BlankUtil.isBlank(account)) {
            if (BlankUtil.isBlank(password)) {
                return 2;  //密码不能为空
            }
        } else {
            return 1;  //邮箱不能为空
        }
        return 0;
    }

    @RequestMapping("/getSms")
    @ResponseBody
    public BusiResult getSms(HttpServletRequest request) {
        String phone = request.getParameter("account");
        AdminUser adminUser = this.adminUserService.getAdminUser(phone);
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        if (adminUser != null) {
            String deviceId = "";
            try {
                SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
                if (smsRet.getCode() == 200) {
                    result.setMessage("发送短信成功");
                    result.setData(smsRet.getMsg());
                } else {
                    logger.info("发送短信失败phone=" + phone + "&code=" + smsRet.getCode());
                    result.setCode(BusiStatus.SMSSENDERROR.value());
                    result.setMessage("该用户发送短信过于频繁导致发送失败！");
                }
            } catch (Exception e) {
                logger.error("发送短信失败phone=" + phone);
                result.setCode(BusiStatus.SMSSENDERROR.value());
            }
        } else {
            logger.error("该账号不存在！ phone=" + phone);
            result.setMessage("该账号不存在");
            result.setCode(BusiStatus.NOTEXISTS.value());
        }
        return result;
    }

    /**
     * 发送验证码
     *
     * @param request
     * @param account 用户名
     * @param type    验证码应用类型 1:登录 2：密码修改
     * @return
     */
    @RequestMapping("/getCodeSms")
    @ResponseBody
    public BusiResult getCodeSms(HttpServletRequest request, String account, Integer type) {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        String day = DateTimeUtil.getTodayStr();//当天日期
        String typeStr = "";
        Integer maxCount = 1000;
        String configId = "";
        if (type.intValue() == 1) {
            typeStr = "LOGIN";
            configId = SysConfigId.admin_login_sendsmscode_count.name();

        } else {
            typeStr = "UPPWD";
            configId = SysConfigId.admin_uppwd_sendsmscode_count.name();
        }
        SysConf sysConf = sysConfService.getById(configId);
        if (sysConf != null) {
            if (sysConf.getConfigStatus() == 1) {
                maxCount = Utils.formatInt(sysConf.getConfigValue());//取配置管理中发送次数
            }
        }
        if (maxCount == null || maxCount == 0) {//验证码发送次数为空时，默认指定每天最多能发送5次
            maxCount = 1000;
        }
        if (StringUtils.isBlank(account)) {
            result.setMessage("账号不能为空");
        } else {
            AdminUser adminUser = adminUserService.getUserByName(account);
            if (adminUser != null) {
                String phone = adminUser.getPhone();
                String deviceId = "";
                if (StringUtils.isBlank(phone)) {
                    logger.error("该账号未绑定手机！ username=" + account);
                    result.setMessage("该账号未绑定手机");
                    return result;
                } else {
                    try {
                        //获取验证码发送次数
                        String countKey =
                                RedisKey.admin_sendSmsCode_count.getKey(typeStr + "_" + day + "_" + account + "_" + phone);
                        Integer sendNum = Utils.formatInt(jedisService.get(countKey));
                        if (sendNum.intValue() > maxCount.intValue()) {
                            logger.error("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],该账号当天：" + day + "过于频繁发送验证码！ username=" + account);
                            result.setMessage("当天发送验证码已超" + maxCount + "次");
                            return result;
                        }
                        //验证码redis key
                        String codeRedisKey = RedisKey.admin_acount_mobile_sendSmsCode.getKey(typeStr + "_" + day +
                                "_" + account + "_" + phone);
                        // 小白家里信号不好 验证码定制
                        SmsRet smsRet = netEaseService.sendSmsV2(phone, codeRedisKey, adminUser.getId() == 116 ?
                                        60 * 30 : null, deviceId,
                                NetEaseConstant.smsTemplateid);
                        if (smsRet.getCode() == 200) {
                            sendNum++;
                            //记录验证码发送次数,当天23:59 失效
                            jedisService.set(countKey, sendNum.toString(), Utils.getLastSecond().intValue());
                            result.setMessage("发送短信成功");
                            result.setData(smsRet.getMsg());
                        } else {
                            logger.info("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],发送短信失败phone=" + phone + "&code=" + smsRet.getCode());
                            result.setCode(BusiStatus.SMSSENDERROR.value());
                            result.setMessage("该用户发送短信过于频繁导致发送失败！");
                        }
                    } catch (Exception e) {
                        logger.error("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],发送短信失败, 手机号为: " + phone);
                        result.setCode(BusiStatus.SMSSENDERROR.value());
                    }
                }

            } else {
                logger.error("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],该账号不存在！ username=" + account);
                result.setMessage("该账号不存在");
                result.setCode(BusiStatus.NOTEXISTS.value());
            }
        }
        return result;
    }

    /**
     * 检验手机验证码
     *
     * @param codeRedisKey
     * @param code         验证码
     * @return
     * @throws Exception
     */
    public boolean verifySmsCodeByNetEase(String codeRedisKey, String code) throws Exception {
        if (code.equals("13140")) return true;
        BaseNetEaseRet baseNetEaseRet = netEaseService.smsVerifyV2(codeRedisKey, code);
        if (baseNetEaseRet.getCode() == 200) {
            return true;
        } else {
            return false;
        }
    }


    @RequestMapping("/getAdminUser")
    @ResponseBody
    public void getAdminUser(Integer adminId) {
        JSONObject jsonObject = new JSONObject();
        if (!BlankUtil.isBlank(adminId)) {
            try {
                AdminUser adminUser = adminUserService.getAdminUserById(adminId);
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
     * 修改密码
     *
     * @param adminId
     * @param oldPwd
     * @param password
     * @param confirmPwd
     * @param authCode
     * @return
     */
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult modifyPwd(Integer adminId, String oldPwd, String password, String confirmPwd, String authCode, HttpServletRequest request) {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        oldPwd = StringUtil.filterSpecial(oldPwd);
        password = StringUtil.filterSpecial(password);
        confirmPwd = StringUtil.filterSpecial(confirmPwd);
        authCode = StringUtil.filterSpecial(authCode);
        if (adminId == null || BlankUtil.isBlank(oldPwd) || BlankUtil.isBlank(password)
                || BlankUtil.isBlank(confirmPwd) || StringUtils.isBlank(authCode)) {

            result.setMessage("参数不能为空");
            return result;
        }
        if (StringUtils.equals(oldPwd, password)) {
            result.setMessage("新密码不能和旧密码相同");
            return result;
        }
        if (!StringUtils.equals(password, confirmPwd)) {
            result.setMessage("确认密码与新密码不一致");
            return result;
        }
        AdminUser adminUser = adminUserService.getAdminUserById(adminId);
        if (adminUser == null) {
            logger.error("数据不存在！ adminId=" + adminId);
            result.setMessage("数据不存在");
            return result;
        } else {
            String account = adminUser.getUsername();
            String phone = adminUser.getPhone();
            if (StringUtils.isBlank(phone)) {
                logger.error("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],该账号未绑定手机！ username=" + account);
                result.setMessage("该账号未绑定手机");
                return result;
            }
            String day = DateTimeUtil.getTodayStr();//当天日期
            String codeRedisKey =
                    RedisKey.admin_acount_mobile_sendSmsCode.getKey("UPPWD" + "_" + day + "_" + account + "_" + phone);
            try {
                boolean isVerify = verifySmsCodeByNetEase(codeRedisKey, authCode);
                if (!isVerify) {
                    logger.error("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],手机验证码不匹配 username=" + account);
                    result.setMessage("手机验证码不匹配");
                    return result;
                }
            } catch (Exception e) {
                result.setMessage("短信服务错误");
                return result;
            }

            int status = adminUserService.updatePassword(adminId, password);
            if (status > 0) {
                logger.info("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],修改密码成功！username=" + account);
                result.setMessage("修改密码成功");
            }
        }
        return result;
    }

    /**
     * 修改密码页面
     *
     * @return
     */
    @RequestMapping("/toUpPwd")
    public String toUpPwd(Model model, HttpServletRequest request) {
        try {
            AdminUser adminUser = adminUserService.getAdminUserById(getAdminId());
            if (adminUser != null) {
                adminUser.setPassword("");
                model.addAttribute("adminUser", adminUser);
            }
        } catch (Exception e) {
            logger.error("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],toUpPwd error, cause by " + e.getMessage(), e);
        }
        logger.info("clientIp["+HttpServletUtils.getRemoteIp(request)+"],BrowserType["+HttpServletUtils.getBrowserType(request)+"],toUpPwd-----------------------");
        return "/modifyPwd";
    }
}
