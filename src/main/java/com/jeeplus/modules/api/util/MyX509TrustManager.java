package com.jeeplus.modules.api.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by 哈士奇 on 2017/5/9.
 */
public class MyX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
