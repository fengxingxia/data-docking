package com.data.docking.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OssUtil {
    private static final String BOUNDARY = "----WebKitFormBoundary0Okc1VI60nPUsSPE";
    private static final String NEWLINE = "\r\n";
    private static final String BOUNDARYPREFIX = "--";

    private static String ossIp = "";

    public static String getImgUrl(String imgBase64) {
        long start = System.currentTimeMillis();
        String ossImgUrl = uploadImgByOSS(imgBase64);
        if (ossImgUrl != null) {
            return "http://" + ossIp + "/" + ossImgUrl;
        }
        return "";
    }

    public static String uploadImgByOSS(String imgBase64) {
        if (!StringUtils.hasText(imgBase64)) {
            return null;
        }
        try {
            String fileName = "1_" + System.currentTimeMillis() + ".jpg";
            String url = "http://" + ossIp + "/webupload/1.0/" + fileName;

            byte[] decode = Base64Utils.decode(imgBase64.getBytes());

            Map<String, Object> paramMap = new HashMap();
            paramMap.put("Content-Type", "image/jpeg");
            paramMap.put("Content-Disposition", "form-data; name=\"file\"; filename=\"" + fileName);

            paramMap.put("file", decode);
            HttpResponse httpResponse = HttpClientPoolUtil.postToForm(url, paramMap, fileName, "----WebKitFormBoundary0Okc1VI60nPUsSPE");
            if (null == httpResponse) {
                return null;
            }
            Header[] uris = httpResponse.getHeaders("X-Dss-Object-Uri");
            Header[] codes = httpResponse.getHeaders("X-Dss-Errcode");

            String code = "";
            if ((null != codes) && (codes.length > 0)) {
                code = codes[0].getValue();
            }
            if ((null != uris) && (uris.length > 0) && ("0".equals(code))) {
                return uris[0].getValue();
            }
        } catch (Exception localException) {
        }
        return null;
    }

    public static String getImageBase64(String domainImg) {
        if (StringUtils.isEmpty(domainImg)) {
            return null;
        }
        long start = System.currentTimeMillis();
        try {
            byte[] responseBytes = HttpClientPoolUtil.getResponseBytes(domainImg, new HashMap());
            return new String(Base64Utils.encode(responseBytes));
        } catch (Exception e) {
            log.error("get image base64 exception", e);
        }
        return null;
    }
}