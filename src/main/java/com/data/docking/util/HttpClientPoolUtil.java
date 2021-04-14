package com.data.docking.util;

import com.alibaba.fastjson.JSON;
import com.data.docking.config.PropertiesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

@Component
public class HttpClientPoolUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientPoolUtil.class);

    public static CloseableHttpClient httpClient = null;

    private static PropertiesConfig config = SpringApplicationContext.getBean(PropertiesConfig.class);

    public static synchronized void initPools()
            throws KeyManagementException, NoSuchAlgorithmException {
        if (httpClient == null) {
            SSLContext sslcontext = createIgnoreVerifySSL();

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                    PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslcontext)).build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setDefaultMaxPerRoute(config.getHttpDefaultMaxPerRoute());
            cm.setMaxTotal(config.getHttpMaxTotal());
            httpClient = HttpClients.custom().setKeepAliveStrategy(defaultStrategy).setConnectionManager(cm).build();
        }
    }

    public static ConnectionKeepAliveStrategy defaultStrategy = new ConnectionKeepAliveStrategy() {
        @Override
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));
            int keepTime = config.getHttpDefaultKeepTime();
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if ((value != null) && (param.equalsIgnoreCase("timeout"))) {
                    try {
                        return Long.parseLong(value) * 1000L;
                    } catch (Exception localException) {
                    }
                }
            }
            return keepTime * 1000;
        }
    };

    public static SSLContext createIgnoreVerifySSL()
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public static HttpRequestBase getRequest(String url, String methodName, Map<String, String> headMap)
            throws KeyManagementException, NoSuchAlgorithmException {
        if (httpClient == null) {
            initPools();
        }
        HttpRequestBase method = null;

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(config.getHttpDefaultTimeOut() * 1000)
                .setConnectTimeout(config.getHttpDefaultTimeOut() * 1000)
                .setConnectionRequestTimeout(config.getHttpDefaultTimeOut() * 1000)
                .setExpectContinueEnabled(false).build();
        if ("PUT".equalsIgnoreCase(methodName)) {
            method = new HttpPut(url);
        } else if ("POST".equalsIgnoreCase(methodName)) {
            method = new HttpPost(url);
        } else if ("GET".equalsIgnoreCase(methodName)) {
            method = new HttpGet(url);
        } else if ("DELETE".equalsIgnoreCase(methodName)) {
            method = new HttpDelete(url);
        } else {
            method = new HttpPost(url);
        }
        if (null != headMap) {
            for (Map.Entry<String, String> value : headMap.entrySet()) {
                method.addHeader((String) value.getKey(), (String) value.getValue());
            }
        } else {
            method.addHeader("Content-Type", "application/json");
        }
        method.setConfig(requestConfig);
        return method;
    }

    public static String get(String url, Map<String, String> headMap)
            throws Exception {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpRequestBase method = null;
        String responseBody = "";
        try {
            if (httpClient == null) {
                initPools();
            }
            method = getRequest(url, "GET", headMap);
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(method, context);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
            }
            return EntityUtils.toString(httpEntity, "UTF-8");
        } catch (Exception e) {
            if (method != null) {
                method.abort();
            }
            throw e;
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

    public static String post(String url, String data, Map<String, String> headMap)
            throws Exception {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpEntityEnclosingRequestBase method = null;
        String responseBody = "";
        try {
            if (httpClient == null) {
                initPools();
            }
            method = (HttpEntityEnclosingRequestBase) getRequest(url, "POST", headMap);
            method.setEntity(new StringEntity(data, Charset.forName("UTF-8")));
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(method, context);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
            }
            return EntityUtils.toString(httpEntity, "UTF-8");
        } catch (Exception e) {
            if (method != null) {
                method.abort();
            }
            throw e;
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

    public static HttpResponse postToForm(String url, Map<String, Object> data, String fileName, String boundary) {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        String responseBody = "";
        try {
            if (httpClient == null) {
                initPools();
            }
            HttpPost httpPost = new HttpPost(url);

            MultipartEntityBuilder mub = MultipartEntityBuilder.create();
            if (!StringUtils.isEmpty(boundary)) {
                mub.setBoundary(boundary);
            }
            Object mapValue;
            try {
                for (String key : data.keySet()) {
                    mapValue = data.get(key);
                    if ((mapValue instanceof byte[])) {
                        mub.addBinaryBody(key, (byte[]) mapValue, ContentType.DEFAULT_BINARY, fileName);
                    } else if (((mapValue instanceof Object)) || ((mapValue instanceof Map)) || ((mapValue instanceof List))) {
                        mub.addPart(key, new StringBody(JSON.toJSONString(mapValue), Consts.UTF_8));
                    } else {
                        mub.addPart(key, new StringBody(mapValue.toString(), Consts.UTF_8));
                    }
                }
            } catch (UnsupportedEncodingException e1) {
                logger.error("��api����HttpClientPoolUtil�� body convert to {} exception", Consts.UTF_8);
            }
            HttpEntity entity = mub.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                responseBody = EntityUtils.toString(httpEntity, "UTF-8");
            }
            return httpResponse;
        } catch (Exception e) {
            logger.error("��api����HttpClientPoolUtil��execute post request exception,url:{}", url, e);
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    logger.error("��api����HttpClientPoolUtil��post request close response exception, url:{}, exception:{}, cost time(ms):{}", new Object[]{url, e
                            .getMessage(), Long.valueOf(System.currentTimeMillis() - startTime)}, e);
                }
            }
        }
        return null;
    }

    public static byte[] getResponseBytes(String url, Map<String, String> headMap)
            throws Exception {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(url)) {
            return new byte[0];
        }
        HttpEntity httpEntity = null;
        HttpRequestBase method = null;
        try {
            if (httpClient == null) {
                initPools();
            }
            method = getRequest(url, "GET", headMap);
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(method, context);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                InputStream in = httpEntity.getContent();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] bytes = new byte['?'];
                int numRead = 0;
                while ((numRead = in.read(bytes, 0, bytes.length)) >= 0) {
                    out.write(bytes, 0, numRead);
                }
                return out.toByteArray();
            }
            return new byte[0];
        } catch (Exception e) {
            if (method != null) {
                method.abort();
            }
            throw e;
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

    public static String put(String url, String data, Map<String, String> headMap)
            throws Exception {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpEntityEnclosingRequestBase method = null;
        String responseBody = "";
        try {
            if (httpClient == null) {
                initPools();
            }
            method = (HttpEntityEnclosingRequestBase) getRequest(url, "PUT", headMap);
            method.setEntity(new StringEntity(data, Charset.forName("UTF-8")));
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(method, context);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
            }
            return EntityUtils.toString(httpEntity, "UTF-8");
        } catch (Exception e) {
            if (method != null) {
                method.abort();
            }
            throw e;
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

    public static String delete(String url, Map<String, String> headMap)
            throws Exception {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpRequestBase method = null;
        String responseBody = "";
        try {
            if (httpClient == null) {
                initPools();
            }
            method = getRequest(url, "DELETE", headMap);
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(method, context);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
            }
            return EntityUtils.toString(httpEntity, "UTF-8");
        } catch (Exception e) {
            if (method != null) {
                method.abort();
            }
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return "";
    }
}
