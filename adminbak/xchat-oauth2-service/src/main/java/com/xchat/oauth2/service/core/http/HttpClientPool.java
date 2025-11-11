package com.xchat.oauth2.service.core.http;

import com.google.common.collect.Maps;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Map;

/**
 * @author liuguofu
 *         on 10/30/14.
 */
public final class HttpClientPool {

    private static final int MAX_TOTAL_CONNECTION = 200;
    private static final int DEFAULT_MAX_CONNECTION_ROUTE = 50;

    private static Map<String, PoolingHttpClientConnectionManager> cmMap = Maps.newHashMap();
    private static final Object writeCMMapLock = new Object();

    private HttpClientPool() {
        // empty
    }

    public static CloseableHttpClient getClient(String url) {
        return getClient(url, 80);
    }

    public static CloseableHttpClient getClient(String url, int port) {
        PoolingHttpClientConnectionManager cm = cmMap.get(url + port);
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(MAX_TOTAL_CONNECTION);
            cm.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTION_ROUTE);
            HttpHost httpHost = new HttpHost(url, port);
            cm.setMaxPerRoute(new HttpRoute(httpHost), 50);
            synchronized (writeCMMapLock) {
                Map<String, PoolingHttpClientConnectionManager> cmMapCopy = Maps.newHashMap(cmMap);
                cmMapCopy.put(url + port, cm);
                cmMap = cmMapCopy;
            }
        }

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        return httpClient;
    }
}
