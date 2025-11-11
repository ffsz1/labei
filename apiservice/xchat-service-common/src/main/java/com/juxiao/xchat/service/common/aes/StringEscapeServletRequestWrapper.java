package com.juxiao.xchat.service.common.aes;

import com.juxiao.xchat.base.utils.DataUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class StringEscapeServletRequestWrapper extends HttpServletRequestWrapper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private InputStream inputStream;
    private Map<String, String[]> params = new HashMap<>();


    StringEscapeServletRequestWrapper(HttpServletRequest request) {
        super(request);
        request.setAttribute("ase", false);
        this.setAttribute("ase", false);
        this.dealRequestBody(request);
        this.analysisMap(request);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.params;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return Objects.isNull(inputStream) ? null : new ServletInputStream() {

            @Override
            public boolean isFinished() {
                try {
                    return inputStream.available() <= 0;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public boolean isReady() {
                try {
                    return inputStream.available() > 0;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public int available() throws IOException {
                return inputStream.available();
            }

            @Override
            public void close() throws IOException {
                super.close();
                inputStream.close();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return Objects.isNull(inputStream) ? null : new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * 处理请求body的内容
     *
     * @param request 请求
     */
    private void dealRequestBody(HttpServletRequest request) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            if (Objects.isNull(inputStream)) {
                return;
            }

            String inputStr = IOUtils.toString(inputStream, "utf-8");
            if (StringUtils.isEmpty(inputStr)) {
                return;
            }

            this.inputStream = IOUtils.toInputStream(inputStr, "utf-8");
        } catch (Exception e) {
            // 在这里解析正常请求
            logger.error("", e);
        }
    }

    private void analysisMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap == null || parameterMap.size() == 0) {
            return;
        }

        this.setAttribute("ase", false);
        parameterMap.keySet().forEach(key -> this.params.put(key, new String[]{DataUtils.escapeHtml(request.getParameter(key))}));
    }
}