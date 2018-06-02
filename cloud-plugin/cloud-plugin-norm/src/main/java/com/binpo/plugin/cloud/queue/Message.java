/**
 * Create Date: 2016年4月13日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.cloud.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 消息
 *
 * @author Francis
 * @version 1.0
 * @since 2016年4月13日
 */
public class Message {

    private static final Log logger = LogFactory.getLog(Message.class);

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
     * 消息ID
     */
    private String messageId;

    /**
     * 消息内容
     */
    private String messageBody;

    /**
     * 消息接收句柄
     */
    private String receiptHandle;

    /**
     * 属性
     */
    private Map<String, String> attributes = new HashMap<String, String>();

    /**
     * 消息属性
     */
    private Map<String, String> messageAttributes = new HashMap<String, String>();

    /**
     * 批量发送消息时，消息在批次中的ID
     */
    private String id;

    /**
     * 默认构造函数
     *
     * @author Francis
     * @since 2016年4月14日
     */
    public Message() {
    }

    /**
     * 构造函数 用于发送消息，与addMessageAttributesEntry方法配合使用
     * 
     * @param messageBody
     *
     * @author Francis
     * @since 2016年4月14日
     */
    public Message(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * 构造函数 用于发送消息
     * 
     * @param messageBody
     * @param messageAttributes
     *
     * @author Francis
     * @since 2016年4月14日
     */
    public Message(String messageBody, Map<String, String> messageAttributes) {
        this.messageBody = messageBody;
        this.messageAttributes = messageAttributes;
    }

    /**
     * 构造函数 用户接收消息
     * 
     * @param messageId
     * @param messageBody
     * @param receiptHandle
     * @param attributes
     * @param messageAttributes
     *
     * @author Francis
     * @since 2016年4月14日
     */
    public Message(String messageId, String messageBody, String receiptHandle, Map<String, String> attributes,
            Map<String, String> messageAttributes) {
        this.messageId = messageId;
        this.messageBody = messageBody;
        this.receiptHandle = receiptHandle;
        this.attributes = attributes;
        this.messageAttributes = messageAttributes;
    }

    /**
     * 将Map对象转成JSON字符串并放入messageBody
     * 
     * @param bodyMap
     *
     * @author Francis
     * @since 2016年4月14日
     */
    public void setBodyMap2JSONString(Map<String, String> bodyMap) {
        this.messageBody = JSON.toJSONString(bodyMap);
    }

    /**
     * 如果mssageBody是JSON字符串，则将messageBody转成Map对象
     * 
     * @return
     *
     * @author Francis
     * @since 2016年4月14日
     */
    public Map<String, String> parseBodyToMap() {
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject jsonObj = JSON.parseObject(this.messageBody);
            Set<String> keySet = jsonObj.keySet();
            for (String key : keySet) {
                map.put(key, jsonObj.getString(key));
            }
        } catch (Exception e) {
            logger.error("MessageBody转Map异常", e);
        }
        return map;
    }

    /**
     * 添加属性
     * 
     * @param key
     * @param value
     * @return
     *
     * @author Francis
     * @since 2016年4月13日
     */
    public Message addAttributesEntry(String key, String value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
        return this;
    }

    /**
     * 添加消息属性
     * 
     * @param key
     * @param value
     * @return
     *
     * @author Francis
     * @since 2016年4月13日
     */
    public Message addMessageAttributesEntry(String key, String value) {
        if (this.messageAttributes == null) {
            this.messageAttributes = new HashMap<>();
        }
        this.messageAttributes.put(key, value);
        return this;
    }

    /**
     * 清空属性
     * 
     * @return
     *
     * @author Francis
     * @since 2016年4月13日
     */
    public Message clearAttributesEntries() {
        if (this.attributes != null) {
            this.attributes.clear();
        }
        return this;
    }

    /**
     * 清空消息属性
     * 
     * @return
     *
     * @author Francis
     * @since 2016年4月13日
     */
    public Message clearMessageAttributesEntries() {
        if (this.messageAttributes != null) {
            this.messageAttributes.clear();
        }
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @param messageBody the messageBody to set
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * @return the receiptHandle
     */
    public String getReceiptHandle() {
        return receiptHandle;
    }

    /**
     * @param receiptHandle the receiptHandle to set
     */
    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }

    /**
     * @return the attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the messageAttributes
     */
    public Map<String, String> getMessageAttributes() {
        return messageAttributes;
    }

    /**
     * @param messageAttributes the messageAttributes to set
     */
    public void setMessageAttributes(Map<String, String> messageAttributes) {
        this.messageAttributes = messageAttributes;
    }

    public Object getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(Object resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("requestId:");
        strBuff.append(this.getRequestId());
        strBuff.append(",code:");
        strBuff.append(this.getCode());
        strBuff.append(",messageId:");
        strBuff.append(this.getMessageId());
        strBuff.append(",messageBody:");
        strBuff.append(this.getMessageBody());
        strBuff.append(",receiptHandle:");
        strBuff.append(this.getReceiptHandle());
        strBuff.append(",attributes:");
        strBuff.append(this.getAttributes());
        strBuff.append(",messageAttributes:");
        strBuff.append(this.getMessageAttributes());
        return strBuff.toString();
    }

}
