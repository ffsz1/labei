package com.juxiao.xchat.service.common.aes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.spring.SpringAppContext;
import com.juxiao.xchat.base.utils.AESUtils;
import com.juxiao.xchat.base.utils.DataUtils;
import com.juxiao.xchat.manager.common.conf.AesConf;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class AesDescryptServletRequestWrapper extends HttpServletRequestWrapper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private InputStream inputStream;
    private Map<String, String[]> params = new HashMap<>();
    private static AesConf aesConf;


    AesDescryptServletRequestWrapper(HttpServletRequest request) throws Exception {
        super(request);
        request.setAttribute("ase", true);
        this.setAttribute("ase", true);
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
    private void dealRequestBody(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        if (Objects.isNull(inputStream)) {
            return;
        }

        String inputStr = IOUtils.toString(inputStream, "utf-8");
        if (StringUtils.isEmpty(inputStr)) {
            return;
        }

        try {
            JSONObject object = JSON.parseObject(inputStr);
            inputStr = AESUtils.decrypt(object.getString("ed"), getAesConf().getKey(), getAesConf().getIv());
        } catch (Exception e) {
            // 在这里解析正常请求
            logger.error("", e);
        }

        this.inputStream = IOUtils.toInputStream(inputStr, "utf-8");
    }

    private void analysisMap(HttpServletRequest request) throws Exception {
        String data = request.getParameter("ed");
        if (StringUtils.isBlank(data)) {
            return;
        }
        this.setAttribute("ase", true);

        String queryString = AESUtils.decrypt(data, getAesConf().getKey(), getAesConf().getIv());

        try {
            JSONObject object = JSONObject.parseObject(queryString);
            Iterator<String> iterator = object.keySet().iterator();
            String key;
            String value;
            while (iterator.hasNext()) {
                key = iterator.next();
                value = object.getString(key);
                if (StringUtils.isBlank(value)) {
                    this.params.put(key, new String[]{""});
                    continue;
                }

                try {
                    value = URLDecoder.decode(value, "utf8");
                } catch (IllegalArgumentException e) {
                    value = value.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                    value = URLDecoder.decode(value, "utf8");
                }
                this.params.put(key, new String[]{DataUtils.escapeHtml(value)});
            }
            return;
        } catch (JSONException e) {

        }

        Arrays.stream(queryString.split("&")).filter(kv -> kv.contains("=")).map(kv -> kv.split("=", 2)).forEach(array -> {
            try {
                if (array.length == 1) {
                    params.put(array[0], new String[]{""});
                    return;
                }

                if (StringUtils.isBlank(array[1])) {
                    params.put(array[0], new String[]{""});
                }

                String value;
                try {
                    value = URLDecoder.decode(array[1], "utf8");
                } catch (UnsupportedEncodingException e) {
                    value = array[1].replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                    value = URLDecoder.decode(value, "utf8");
                }
                this.params.put(array[0], new String[]{DataUtils.escapeHtml(value)});
            } catch (UnsupportedEncodingException e) {
                params.put(array[0], new String[]{array[1]});
            }
        });
    }

    private AesConf getAesConf() {
        if (aesConf == null) {
            aesConf = SpringAppContext.getBean(AesConf.class);
        }
        return aesConf;
    }
}