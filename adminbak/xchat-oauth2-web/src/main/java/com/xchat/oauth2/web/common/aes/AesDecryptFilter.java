package com.xchat.oauth2.web.common.aes;

import com.alibaba.fastjson.JSON;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AesDecryptFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // TODO:过滤接口
        String data = request.getParameter("ed");
        if (StringUtils.isBlank(data)) {
            request.setAttribute("ase", false);
            chain.doFilter(request, response);
            return;
        }
        try {
            chain.doFilter(new AesDescryptServletRequestWrapper(request), response);
        } catch (Exception e) {
            logger.error("", e);
            this.writeJson(response, new BusiResult<>(BusiStatus.BUSIERROR));
        }
    }

    /**
     * @param response
     */
    public void writeJson(HttpServletResponse response, Object object) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(JSON.toJSONString(object));
            out.flush();
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
