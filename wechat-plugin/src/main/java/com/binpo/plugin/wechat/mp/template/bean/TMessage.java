package com.binpo.plugin.wechat.mp.template.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 微信公众号模板消息发送内容
 *
 * @author zhang 2017年7月14日 上午11:20:59
 */
public class TMessage {

    /**
     * openid  
     */
    private String touser;
    /**
     * 模板消息id
     */
    private String template_id;
    /**
     * 可以跳转的url
     */
    private String url;
    /**
     * 需要跳转到的小程序
     */
    private Miniprogram miniprogram;
    
    private String topcolor = "#FF0000";
    /**
     * 模板消息内容
     */
    private Map<String,TValue> data = new HashMap<String,TValue>();
    
    public TMessage(String touser,String template_id){
        this.template_id = template_id;
        this.touser = touser;
    }
    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void addParams(String key,String value) {
    	this.data.put(key, new TValue(value));
    }
    
    public TMessage(String touser,String template_id,Map<String,TValue> data){
        this.template_id = template_id;
        this.touser = touser;
        this.data = data;
    }
    
    public TMessage(String touser,String template_id,Map<String,TValue> data,String topcolor){
        this.template_id = template_id;
        this.touser = touser;
        this.topcolor = topcolor;
        this.data = data;
    }
    
    public TMessage(String touser,String template_id,Map<String,TValue> data,String topcolor,String url){
        this.template_id = template_id;
        this.touser = touser;
        this.topcolor = topcolor;
        this.url = url;
        this.data = data;
    }
    /**
     * 
     * 添加参数
     * 
     * @param key
     * @param value
     * @return
     */
    public Map<String,TValue> addValue(String key,TValue value){
        this.data.put(key, value);
        return this.data;
    }
    /**
     * @return the touser
     */
    public String getTouser() {
        return touser;
    }
    /**
     * @param touser the touser to set
     */
    public void setTouser(String touser) {
        this.touser = touser;
    }
    /**
     * @return the template_id
     */
    public String getTemplate_id() {
        return template_id;
    }
    /**
     * @param template_id the template_id to set
     */
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return the topcolor
     */
    public String getTopcolor() {
        return topcolor;
    }
    /**
     * @param topcolor the topcolor to set
     */
    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }
    /**
     * @return the data
     */
    public Map<String, TValue> getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(Map<String, TValue> data) {
        this.data = data;
    }
    public Miniprogram getMiniprogram() {
        return miniprogram;
    }
    public void setMiniprogram(Miniprogram miniprogram) {
        this.miniprogram = miniprogram;
    }
    
    
    
    
}
