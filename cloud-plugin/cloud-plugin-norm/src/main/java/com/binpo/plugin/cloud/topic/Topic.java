package com.binpo.plugin.cloud.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订阅信息
 * @author zhang
 *
 */
public class Topic {
	
	public Topic(){
		
	}
	public Topic (String messageBody){
		this.messageBody = messageBody;
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
	 * 消息内容
	 */
	private String messageBody;
	/**
	 * 消息ID
	 */
	private String messageId;
	
	/**
	 * 属性
	 */
	private Map<String, String> attributes=new HashMap<String, String>();
	
	/**
	 * 消息tag 用于被订阅者过滤使用
	 */
	private List<String> tags = new ArrayList<String>();

	
	
	public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	

	

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
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

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	
	
}
