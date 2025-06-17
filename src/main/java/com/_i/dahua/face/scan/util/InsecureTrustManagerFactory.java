package com._i.dahua.face.scan.util;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class InsecureTrustManagerFactory {
    public static final X509TrustManager INSTANCE = new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    };

    public static TrustManager[] getTrustManagers() {
        return new TrustManager[]{INSTANCE};
    }
}
