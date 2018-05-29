package com.binpo.plugin.cloud.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.binpo.plugin.cloud.AbstractColudProperties.ContentFormat;
import com.binpo.plugin.cloud.AbstractColudProperties.Protocol;

/**
 * 订阅
 * 
 * @author zhang
 *
 */
public class Subscription {

    public Subscription() {

    }

    /**
     * 创建订阅
     * 
     * @param subscriptionName 订阅的名字
     * @param endpoint 目标对象
     */
    public Subscription(String subscriptionName, String endpoint) {
        this.subscriptionName = subscriptionName;
        this.endpoint = endpoint;
    }

    /**
     * 创建订阅
     * 
     * @param subscriptionName 订阅的名字
     * @param endpoint 目标对象
     * @param protocol 订阅的协议
     * @param notifyContentFormat 内容形式
     */
    public Subscription(String subscriptionName, String endpoint, Protocol protocol,
            ContentFormat notifyContentFormat) {
        this.subscriptionName = subscriptionName;
        this.endpoint = endpoint;
        this.protocol = protocol;
        this.notifyContentFormat = notifyContentFormat;
    }

    /**
     * 批量发送消息时，消息在批次中的ID
     */
    private String requestId;

    /**
     * 错误编码
     */
    private String code;
    /**
     * 返回的结果内容
     */
    private Object resultInfo;

    /**
     * 订阅名字
     */
    private String subscriptionName;
    /**
     * 订阅的协议
     */
    private Protocol protocol = Protocol.http;
    /**
     * 接收通知的endpoint
     */
    private String endpoint;
    /**
     * 向endpoint推送消息出现错误时，CMQ推送服务器的重试策略
     */
    private String notifyStrategy;
    /**
     * 推送内容的格式
     */
    private ContentFormat notifyContentFormat = ContentFormat.JSON;

    /**
     * 属性
     */
    private Map<String, String> attributes = new HashMap<String, String>();

    /**
     * 消息正文。消息标签（用于消息过滤)。标签数量不能超过5个，每个标签不超过16个字符。
     */
    private List<String> filterTags = new ArrayList<String>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(Object resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getNotifyStrategy() {
        return notifyStrategy;
    }

    public void setNotifyStrategy(String notifyStrategy) {
        this.notifyStrategy = notifyStrategy;
    }

    public ContentFormat getNotifyContentFormat() {
        return notifyContentFormat;
    }

    public void setNotifyContentFormat(ContentFormat notifyContentFormat) {
        this.notifyContentFormat = notifyContentFormat;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getFilterTags() {
        return filterTags;
    }

    public void setFilterTags(List<String> filterTags) {
        this.filterTags = filterTags;
    }

    
}
