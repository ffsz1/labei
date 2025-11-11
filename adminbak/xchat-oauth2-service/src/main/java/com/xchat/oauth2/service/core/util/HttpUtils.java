package com.xchat.oauth2.service.core.util;

import com.xchat.oauth2.service.core.http.HttpClientPool;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * @author liuguofu
 *         on 10/22/14.
 */

public final class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
        // empty
    }

    public static String httpGetString(String httpUrl) {

        if (StringUtils.isBlank(httpUrl)) {
            return StringUtils.EMPTY;
        }

        CloseableHttpClient client = null;
        try {
            URL url = new URL(httpUrl);
            client = HttpClientPool.getClient(url.getHost(), url.getPort());
            HttpGet httpGet = new HttpGet(httpUrl);
            HttpResponse response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                return StringUtils.EMPTY;
            }
            return EntityUtils.toString(response.getEntity(),"UTF-8");
        } catch (Exception e) {
            LOG.error("http get string error", e);
        }
//        finally {
//            try {
//                if (client != null) {
//                    client.close();
//                }
//            } catch (Exception e) {
//                LOG.error("http close client error. url:{} msg:{}", new Object[]{httpUrl, e});
//            }
//        }
        return StringUtils.EMPTY;
    }
}
