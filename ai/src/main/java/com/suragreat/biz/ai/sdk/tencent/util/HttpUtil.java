package com.suragreat.biz.ai.sdk.tencent.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String get(String url) {
        String result = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig());
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, DEFAULT_CHARSET);
        } catch (ClientProtocolException e) {
            logger.error("postResult错误", e);
        } catch (IOException e) {
            logger.error("postResult错误", e);
        }

        return result;

    }

    private static RequestConfig requestConfig(){
        return RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(20000).build();
    }
    public static String post(String url, List<NameValuePair> nvps) {
        String result = null;
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig());
            if (nvps != null && nvps.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
            }
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, DEFAULT_CHARSET);
        } catch (ClientProtocolException e) {
            logger.error("postResult错误", e);
        } catch (IOException e) {
            logger.error("postResult错误", e);
        }

        return result;
    }

    public static String post(String url, Map<String, String> params) {

        if (params == null || params.size() <= 0) {
            return "empty param";
        }

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        for (Iterator<String> it = params.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = params.get(key);
            nvps.add(new BasicNameValuePair(key, value));
        }

        return post(url, nvps);
    }
}
