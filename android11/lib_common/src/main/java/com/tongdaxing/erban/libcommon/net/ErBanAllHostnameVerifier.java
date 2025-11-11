package com.tongdaxing.erban.libcommon.net;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/16
 */
public final class ErBanAllHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        //信任所有的证书
        return true;
    }
}
