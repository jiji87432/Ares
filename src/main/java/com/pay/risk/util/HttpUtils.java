package com.pay.risk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

@SuppressWarnings("all")
public class HttpUtils {

    static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    public static final String ENCODING = "UTF-8";

    public static final Integer CON_TIMEOUT = 30 * 1000;

    public static String getWebContentByPost(String url) throws Exception {
        return getWebContentByPost(url, ENCODING, ENCODING, null, null);
    }

    public static String getWebContentByPost(String url, Map params) throws Exception {
        return getWebContentByPost(url, ENCODING, ENCODING, null, params);
    }

    public static String getWebContentByPost(String url, String reqBodyCharset, String resBodyCharset) throws Exception {
        return getWebContentByPost(url, reqBodyCharset, resBodyCharset, null, null);
    }

    public static String getWebContentByGet(String url, String resBodyCharset, Map params) throws HttpException, IOException {
        long start = System.currentTimeMillis();
        GetMethod get = new GetMethod(url);

        if (params != null) {
            HttpMethodParams param = new HttpMethodParams();
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry en = (Map.Entry) it.next();
                param.setParameter((String) en.getKey(), en.getValue());
            }
            get.setParams(param);
        }
        HttpClient client = new HttpClient(connectionManager);
        try {
            // 1秒超时
            client.setTimeout(CON_TIMEOUT);
            int statusCode = client.executeMethod(get);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + get.getStatusLine());
            }
            byte[] responseBody = get.getResponseBody();
            return new String(responseBody, resBodyCharset);
        } finally {
            get.releaseConnection();
        }

    }

    public static String getWebContentByPost(String url, String reqBodyCharset, String resBodyCharset, String body, Map<String, String> params) throws Exception {
        long start = System.currentTimeMillis();
        PostMethod post = new PostMethod(url);

        if (body != null) {
            post.setRequestEntity(new ByteArrayRequestEntity(body.getBytes(reqBodyCharset), reqBodyCharset));
        }

        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String next = it.next();
                post.setParameter(next, params.get(next));
            }
        }
        HttpClient client = new HttpClient(connectionManager);
        try {
            client.setTimeout(CON_TIMEOUT);
            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + post.getStatusLine());
            }
            byte[] responseBody = post.getResponseBody();
            // System.out.println(new String(responseBody, "utf-8"));
            return new String(responseBody, resBodyCharset);
        } finally {
            System.out.println("excute httpclient times #######" + (System.currentTimeMillis() - start) + "ms");
            post.releaseConnection();
        }
    }

    public static String getWebContentByPostXml(String url, String reqBodyCharset, String resBodyCharset, String body, Map<String, String> params) throws Exception {
        long start = System.currentTimeMillis();
        PostMethod post = new PostMethod(url);
        post.addRequestHeader("Content-Type", "text/xml; charset=" + reqBodyCharset);

        if (body != null) {
            post.setRequestEntity(new ByteArrayRequestEntity(body.getBytes(reqBodyCharset), reqBodyCharset));
        }

        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String next = it.next();
                post.setParameter(next, params.get(next));
            }
        }
        HttpClient client = new HttpClient(connectionManager);
        try {
            client.setTimeout(CON_TIMEOUT);
            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + post.getStatusLine());
            }
            byte[] responseBody = post.getResponseBody();
            return new String(responseBody, resBodyCharset);
        } finally {
            System.out.println("excute httpclient times #######" + (System.currentTimeMillis() - start) + "ms");
            post.releaseConnection();
        }
    }

    public static String getHttpBodyContent(InputStream is, String resBodyCharset) throws Exception {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, resBodyCharset));
            // 读取HTTP请求内容
            String buffer = null;
            StringBuffer sb = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                // 在页面中显示读取到的请求参数
                sb.append(buffer + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new Exception("getHttpBodyContent Exception", e);
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new Exception("closeBufferReader IOException", e);
                }
            }
        }
    }
}
