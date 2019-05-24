package com.binpo.plugin.sms.base;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

public class SMSUtil {

    public enum SendCode {
        SMS_IS_OK, SMS_IS_ERROR
    }

    public static Set<String> spllit(String s, String splitChar) {
        Set<String> set = new HashSet();
        if (s == null) {
            return set;
        }
        String[] split = s.split(splitChar);
        for (String str : split) {
            if (StringUtils.isNotBlank(str)) {
                set.add(str);
            }
        }
        return set;
    }

    public static String formatSetToString(Set<String> set) {
        StringBuffer buf = new StringBuffer();
        int i = set.size();
        int j = 0;
        for (String telephone : set) {
            if (j + 1 == i) {
                buf.append(telephone);
            } else {
                buf.append(telephone + ",");
            }
            j++;
        }
        return buf.toString();
    }

    public static String send(Map<Object, Object> params, String url) throws Exception {
        return send(createParams(params), url, null);
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
            httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
            body = httpclient.execute(httppost, new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response)
                        throws ClientProtocolException, IOException {
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

    public static String send(String url, String data) throws Exception {
        String body = "";
        // post请求
        HttpPost httppost = new HttpPost(url);
        byte[] byteData = data.getBytes("utf-8");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httppost.setEntity(new ByteArrayEntity(byteData));
            httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
            httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
            body = httpclient.execute(httppost, new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response)
                        throws ClientProtocolException, IOException {
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

    /**
     * 组装请求头
     * 
     * @param params
     * @return
     */
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

    /**
     * 创建请求参数
     * 
     * @param map 请求参数的map
     * @return
     */
    public static List<NameValuePair> createParams(Map<Object, Object> map) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<Object, Object> entity : map.entrySet()) {
            params.add(new BasicNameValuePair(null2String(entity.getKey()), null2String(entity.getValue())));
        }
        return params;
    }

    public static String null2String(Object s) {
        return ((s == null) ? "" : s.toString().trim());
    }

    /**
     * java自带的post请求
     * 
     * @param strUrl
     * @param param
     * @return
     */
    public static String postSend(String strUrl, String param) {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();

            // POST方法时使用
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(param);
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

}
