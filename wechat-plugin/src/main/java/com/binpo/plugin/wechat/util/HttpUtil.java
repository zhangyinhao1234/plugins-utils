package com.binpo.plugin.wechat.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class HttpUtil {
public static final String GET = "GET";
    
    public static final String POST = "POST";
    
    /**
     * Get请求取得JSON字符串转换后的对象
     * @param httpUrl
     * @param params
     * @param clazz
     * @return
     *
     * @author Francis
     * @since 2015年11月5日
     */
    public static <T> T getJsonByRequestWithGet(String httpUrl, Map<String, String> params, Class<T> clazz){
        return getJsonByRequest(httpUrl, GET, params, clazz);
    }
    
    /**
     * Post请求取得JSON字符串转换后的对象
     * @param httpUrl
     * @param params
     * @param clazz
     * @return
     *
     * @author Francis
     * @since 2015年11月5日
     */
    public static <T> T getJsonByRequestWithPost(String httpUrl, Map<String, String> params, Class<T> clazz){
        return getJsonByRequest(httpUrl, POST, params, clazz);
    }
    
    /**
     * 取得JSON字符串转换后的对象
     * @param httpUrl
     * @param method
     * @param params
     * @param clazz
     * @return
     *
     * @author Francis
     * @since 2015年11月5日
     */
    public static <T> T getJsonByRequest(String httpUrl, String method, Map<String, String> params, Class<T> clazz){
        String strJson = request(httpUrl, method, params);
        return JSON.parseObject(strJson, clazz);
    }
    
    /**
     * Get请求
     * @param httpUrl
     * @param params
     * @return
     *
     * @author Francis
     * @since 2015年11月5日
     */
    public static String requestWithGet(String httpUrl, Map<String, String> params){
        return request(httpUrl, GET, params);
    }
    
    /**
     * Post请求
     * @param httpUrl
     * @param params
     * @return
     *
     * @author Francis
     * @since 2015年11月5日
     */
    public static String requestWithPost(String httpUrl, Map<String, String> params){
        return request(httpUrl, POST, params);
    }
    
    /**
     * Http请求
     * @param httpUrl
     * @param method
     * @param params
     * @return
     *
     * @author Francis
     * @since 2015年11月5日
     */
    public static String request(String httpUrl, String method, Map<String, String> params) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod(method);
            
            if(isNotEmpty(params)){
                for (String key : params.keySet()) {
                    connection.setRequestProperty(key, params.get(key));
                }
            }

            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static boolean isNotEmpty(Map map) {
        boolean flag = false;
        if (map != null && !map.isEmpty()) {
            flag = true;
        }
        return flag;
    }
}
