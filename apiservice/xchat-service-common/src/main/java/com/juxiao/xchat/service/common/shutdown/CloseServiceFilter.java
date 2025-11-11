package com.juxiao.xchat.service.common.shutdown;

import com.juxiao.xchat.manager.common.conf.SignalConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = "/*", filterName = "shutDownFilter")
public class CloseServiceFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CloseServiceFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if(servletResponse instanceof HttpServletResponse){
            if(SignalConf.signalNum == 12){
                logger.error("==============关闭服务=========");
                HttpServletResponse resp = (HttpServletResponse) servletResponse;
                resp.getWriter().print("close service");
                resp.setContentType(MediaType.TEXT_HTML_VALUE);
                resp.setCharacterEncoding("utf-8");
                resp.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
                resp.flushBuffer();
                return;
            }
        }


        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
