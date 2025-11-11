package com.xchat.oauth2.service.core.http;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 工具类
 * 使用https协议发起网络请求
 * @author liuguofu
 * on 2015/6/15.
 */
public class HttpsUtils {
    private final static Logger LOG = LoggerFactory.getLogger(HttpsUtils.class);
    private final static String Default_Charset = "UTF-8";

    public static DefaultHttpClient getHttpsClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            };
            DefaultHttpClient client = new DefaultHttpClient();
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);

            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            //设置要使用的端口，默认是443
            sr.register(new Scheme("https", 443, ssf));
            return client;
        } catch (Exception ex) {
            LOG.error("", ex);
            return null;
        }
    }

    /**
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        DefaultHttpClient httpClient = getHttpsClient();
        HttpPost post = new HttpPost(url);
        HttpEntity entity = map2UrlEncodedFormEntity(params);
        if (entity != null) {
            post.setEntity(entity);
        }
        LOG.info("http post---{}", getUrlRequestInfo(url, params));
        HttpResponse response;
        try {
            response = httpClient.execute(post);
            return EntityUtils.toString(response.getEntity(),Default_Charset);
        } catch (IOException e) {
            LOG.error("", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return StringUtils.EMPTY;
    }
    /**
     * @param url
     * @param params
     * @return
     */
    public static String get(String url) {
        DefaultHttpClient httpClient = getHttpsClient();
        HttpGet get = new HttpGet(url);
        LOG.info("get uri:{}",get.getURI());
        HttpResponse response;
        try {
            response = httpClient.execute(get);
            return EntityUtils.toString(response.getEntity(),Default_Charset);
        } catch (IOException e) {
            LOG.error("error msg:{}", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return StringUtils.EMPTY;
    }

    /**
     * 生成post请求时的记录
     *
     * @param url
     * @param params
     * @return
     * @author asflex
     * @date 2014-3-28下午7:23:33
     * @modify 2014-3-28下午7:23:33
     */
    public static String getUrlRequestInfo(String url, Map<String, String> params) {

        StringBuilder paramStr = new StringBuilder();
        if (MapUtils.isNotEmpty(params)) {
            Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
            Joiner.on("&").appendTo(paramStr, iterator);
        }
        return String.format("curl -d '%s' '%s'", StringUtils.trimToEmpty(paramStr.toString()), StringUtils.trimToEmpty(url));
    }

    /**
     * 参数转换
     *
     * @param params
     * @return
     * @author asflex
     * @date 2014-3-28下午7:23:05
     * @modify 2014-3-28下午7:23:05
     */
    public static HttpEntity map2UrlEncodedFormEntity(Map<String, String> params) {
        if (MapUtils.isNotEmpty(params)) {
            Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
            List<NameValuePair> nvps = Lists.newArrayList();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                nvps.add(new BasicNameValuePair(StringUtils.trimToEmpty(entry.getKey()), StringUtils.trimToEmpty(entry.getValue())));
            }
            try {
                return new UrlEncodedFormEntity(nvps);
            } catch (UnsupportedEncodingException e) {
                LOG.error("", e);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxda36314651fcba6f&secret=32e46497b47f9bc87952f13d86996ac8";
        System.out.println(get(url));
    }
}
