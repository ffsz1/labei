package com.xchat.oauth2.web.common.aes;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.xchat.common.utils.AESUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class AesDescryptServletRequestWrapper extends HttpServletRequestWrapper {
    private InputStream inputStream;
    private Map<String, String[]> params = new HashMap<>();

    public AesDescryptServletRequestWrapper(HttpServletRequest request) throws Exception {
        super(request);
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

            JSONObject inputJson = JSONObject.parseObject(inputStr);
            if (Objects.isNull(inputJson)) {
                return;
            }

            this.inputStream = IOUtils.toInputStream(inputJson.toJSONString(), "utf-8");
        } catch (Exception e) {

        }
    }

    private void analysisMap(HttpServletRequest request) throws Exception {
        String data = request.getParameter("ed");
        if (StringUtils.isBlank(data)) {
            return;
        }
        this.setAttribute("ase", true);

        String queryString = AESUtils.decrypt(data, AesConf.AES_KEY, AesConf.AES_IV);
        try {
            JSONObject object = JSONObject.parseObject(queryString);
            Iterator<String> iterator = object.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                this.params.put(key, new String[]{URLDecoder.decode(object.getString(key), "utf-8")});
            }
            return;
        } catch (JSONException e) {

        }

        Arrays.stream(queryString.split("&")).filter(kv -> kv.contains("=")).map(kv -> kv.split("=", 2)).forEach(array -> {
            try {
                if (array.length == 1) {
                    params.put(array[0], new String[]{""});
                } else {
                    params.put(array[0], new String[]{URLDecoder.decode(array[1], "utf-8")});
                }
            } catch (UnsupportedEncodingException e) {
                params.put(array[0], new String[]{array[1]});
            }
        });
    }
}
