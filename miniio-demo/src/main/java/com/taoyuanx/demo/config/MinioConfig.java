package com.taoyuanx.demo.config;

import io.minio.MinioClient;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2020/8/6
 */
@Configuration
public class MinioConfig {
    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        try {
            MinioClient.Builder minioClientBuilder = MinioClient.builder().endpoint(minioProperties.getEndpoint()).credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey());
            if (StringUtils.hasText(minioProperties.getRegion())) {
                minioClientBuilder.region(minioProperties.getRegion());
            }
            minioClientBuilder.httpClient(newOkhttpClient(minioProperties));
            return minioClientBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("配置初始化失败", e);
        }

    }


    private OkHttpClient newOkhttpClient(MinioProperties minioProperties) throws Exception {
        //1 初始化http client
        SSLParams sslParams = new SSLParams();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        X509TrustManager trustManager = new UnSafeTrustManager();
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        sslParams.sSLSocketFactory = sslContext.getSocketFactory();
        sslParams.trustManager = trustManager;
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(minioProperties.getConnectTimeout(), TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(minioProperties.getMaxIdleConnections(), minioProperties.getKeepAliveDuration(), TimeUnit.SECONDS))
                .retryOnConnectionFailure(false)
                .build();
        return okHttpClient;
    }

    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

}
