package com.binpo.plugin.cloud.qcloud.cmq;

import java.util.ArrayList;
import java.util.List;


public class CMQTopicResult {
	private int code;
	/**
	 * 错误提示信息。
	 */
	private String message;
	
	/**
	 * 服务器生成的请求 Id。出现服务器内部错误时，用户可提交此 Id 给后台定位问题。
	 */
	private String requestId;
	
	/**
	 * 本次消费的消息唯一标识 Id。
	 */
	private String msgId;
	
	private List<CMQTopicResult> msgList=new ArrayList();

	
	public List<CMQTopicResult> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<CMQTopicResult> msgList) {
		this.msgList = msgList;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
