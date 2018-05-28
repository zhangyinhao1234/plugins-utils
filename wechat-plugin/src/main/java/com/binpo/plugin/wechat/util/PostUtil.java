package com.binpo.plugin.wechat.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PostUtil {
    public static String send(Map<Object, Object> params, String url) throws Exception {
        return send(createParams(params), url, null);
    }

    public static String postJson(String url,String jsonString) throws Exception{
        String resData = "";
        HttpPost httppost = new HttpPost(url);  
        HttpClient httpClient = new DefaultHttpClient();  
        StringEntity entity = new StringEntity(jsonString,"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        httppost.setEntity(entity);   
        try{
            HttpResponse result = httpClient.execute(httppost);  
            // 请求结束，返回结果  
            resData = EntityUtils.toString(result.getEntity());  
            return resData;
        }catch(Exception e){
            throw e;
        }finally{
            httppost.releaseConnection();
        }
    }

    public static String send(final List<NameValuePair> params, String url, Map<String, String> headers)
            throws Exception {
        String body = "";
        // post请求
        HttpPost httppost = new HttpPost(url);
        // 设置 header
        Header headerss[] = buildHeader(headers);
        if (headerss != null && headerss.length > 0) {
            httppost.setHeaders(headerss);
        }
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            body = httpclient.execute(httppost, new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response) throws ClientProtocolException,
                        IOException {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            String string = new String(EntityUtils.toString(entity));
                            return string;
                        } else {
                            return "error";
                        }
                    }
                    return "";
                }
            });
        } catch (Exception e) {
            throw e;
        } finally {
            httppost.releaseConnection();
        }
        return body;
    }

    public static Header[] buildHeader(Map<String, String> params) {
        Header[] headers = null;
        if (params != null && params.size() > 0) {
            headers = new BasicHeader[params.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                headers[i] = new BasicHeader(entry.getKey(), entry.getValue());
                i++;
            }
        }
        return headers;
    }

    public static List<NameValuePair> createParams(Map<Object, Object> map) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<Object, Object> entity : map.entrySet()) {
            params.add(new BasicNameValuePair(entity.getKey() == null ? null : entity.getKey().toString(),
                    entity.getValue() == null ? null : entity.getValue().toString()));
        }
        return params;
    }
    
    

}
