package com.erban.admin.web.interceptor;


import com.erban.admin.main.common.AdminConstants;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    JedisService jedisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            Object tmp = request.getSession().getAttribute(AdminConstants.HAS_LOGIN);
            if (tmp != null) {
                String isLogin = tmp.toString();
                if (!BlankUtil.isBlank(isLogin) && "true".equalsIgnoreCase(isLogin)) {
                    String adminId = request.getSession().getAttribute(AdminConstants.ADMIN_ID).toString();
                    if (!BlankUtil.isBlank(adminId)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("getAdminId fail,admin has not login", e);
        }
        response.setContentType("text/html;charset=utf-8");
        Writer writer = response.getWriter();
        writer.write("<script>window.location='/login/index';</script>");
        // writer.write("登录已过期, 请重新刷新后登录 ~");
        writer.flush();
        writer.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
