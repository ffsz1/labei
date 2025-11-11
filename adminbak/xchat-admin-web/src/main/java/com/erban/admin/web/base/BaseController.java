package com.erban.admin.web.base;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.common.AdminConstants;
import com.erban.admin.web.frame.MvcContext;
import com.erban.admin.web.frame.Scope;
import com.erban.admin.web.support.CustomDateEditor;
import com.xchat.common.utils.BlankUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @description 控制器的基类
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected int pageNumber;

    protected int pageSize;

    protected int recordCount;

    protected HttpServletRequest getRequest() {
        return MvcContext.getRequest();
    }

    protected HttpServletResponse getResponse() {
        return MvcContext.getResponse();
    }

    protected int getAdminId() {
        try {
            Object tmp = getAttribute(AdminConstants.HAS_LOGIN, Scope.SESSION);
            if (tmp != null) {
                String isLogin = tmp.toString();
                if (!BlankUtil.isBlank(isLogin) && "true".equalsIgnoreCase(isLogin)) {
                    String adminId = getAttribute(AdminConstants.ADMIN_ID, Scope.SESSION).toString();
                    if (!BlankUtil.isBlank(adminId)) {
                        return Integer.valueOf(adminId);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("getAdminId fail,admin has not login", e);
        }
        return -1;
    }

    /**
     * 设置请求的属性
     *
     * @param key
     * @param obj
     */
    public void setAttribute(String key, Object obj) {
        setAttribute(key, obj, Scope.REQUEST);
    }

    public void setAttribute(String key, Object obj, Scope scope) {
        switch (scope) {
            case REQUEST:
                getRequest().setAttribute(key, obj);
                break;
            case SESSION:
                getRequest().getSession().setAttribute(key, obj);
                break;
            case APPLICATION:
                getRequest().getSession().getServletContext().setAttribute(key, obj);
                break;
            default:
                getRequest().setAttribute(key, obj);
                break;
        }
    }

    public Object getAttribute(String key, Scope scope) {
        Object attr = null;
        switch (scope) {
            case REQUEST:
                attr = getRequest().getAttribute(key);
                break;
            case SESSION:
                attr = getRequest().getSession().getAttribute(key);
                break;
            case APPLICATION:
                attr = getRequest().getSession().getServletContext().getAttribute(key);
                break;
            default:
                break;
        }
        return attr;
    }

    public void writeJson(String json) {
        try {
            json = HtmlUtils.htmlEscape(json).replaceAll("&quot;", "\"");
            getResponse().setContentType("text/json;charset=utf-8");
            Writer writer = getResponse().getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJsonResult(int result) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"result\":").append(result).append("}");
        writeJson(builder.toString());
    }

    public void writeJson(boolean success, String msg) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"success\":\"").append(success).append("\",");
        builder.append("\"msg\":\"").append(msg).append("\"}");
        writeJson(builder.toString());
    }


    public int getPageNumber() {
        String tmp = getRequest().getParameter("pageNumber");
        if (BlankUtil.isBlank(tmp)) {
            return 1;
        }
        return Integer.valueOf(tmp);
    }

    public int getPageSize() {
        String tmp = getRequest().getParameter("pageSize");
        if (BlankUtil.isBlank(tmp)) {
            return 10;
        }
        return Integer.valueOf(tmp);
    }

    /**
     * 获取参数数组值
     *
     * @param request
     * @param paramName
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getRequestArray(HttpServletRequest request, String paramName, Class<T> clazz) {
        if (request.getParameter(paramName) != null) {
            return JSON.parseArray(request.getParameter(paramName), clazz);
        }
        return null;
    }

    /**
     * 自定义日期类型绑定
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor("yyyy-MM-dd HH:mm:ss", true));
    }

    protected byte[] readBody(HttpServletRequest request) throws IOException {
        if (request.getContentLength() > 0) {
            byte[] body = new byte[request.getContentLength()];
            IOUtils.readFully(request.getInputStream(), body);
            return body;
        } else {
            return null;
        }
    }
}

