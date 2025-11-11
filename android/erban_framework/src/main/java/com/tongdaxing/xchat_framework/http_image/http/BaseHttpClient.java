package com.tongdaxing.xchat_framework.http_image.http;

import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by zhongyongsheng on 14-6-10.
 */
public class BaseHttpClient {

    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final int MAX_CONNECTIONS = DEFAULT_MAX_CONNECTIONS;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60 * 1000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
    public static final int SOCKET_TIMEOUT = DEFAULT_SOCKET_TIMEOUT;
    private static final int CONNECTION_TIMEOUT = DEFAULT_CONNECTION_TIMEOUT;

    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static DefaultHttpClient httpClient;

    public static DefaultHttpClient getHttpClient() {
        if (httpClient != null) {
            return httpClient;
        }

        BasicHttpParams httpParams = new BasicHttpParams();

        httpParams.setParameter("http.protocol.allow-circular-redirects", true);
        ConnManagerParams.setTimeout(httpParams, SOCKET_TIMEOUT);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(MAX_CONNECTIONS));
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);

        HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(httpParams, SOCKET_TIMEOUT);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        SSLSocketFactory sf;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            HttpLog.e(e, "SSLSocketFactoryEx load error.");
            sf = SSLSocketFactory.getSocketFactory();
        }

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", sf, 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        httpClient = new DefaultHttpClient(cm, httpParams);

        return httpClient;
    }

    public static class SSLSocketFactoryEx extends SSLSocketFactory {

        private SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {

            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}
