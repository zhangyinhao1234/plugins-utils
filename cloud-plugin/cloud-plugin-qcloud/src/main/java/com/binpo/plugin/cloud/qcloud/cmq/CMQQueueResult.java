package com.binpo.plugin.cloud.qcloud.cmq;

import java.util.ArrayList;
import java.util.List;

public class CMQQueueResult {
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
	/**
	 * 本次消费的消息正文。
	 */
	private String msgBody;
	/**
	 * 消费被生产出来，进入队列的时间。返回Unix时间戳，精确到秒。
	 */
	private long enqueueTime;
	/**
	 * 每次消费返回唯一的消息句柄。用于删除该消息，仅上一次消费时产生的消息句柄能用于删除消息。
	 */
	private String receiptHandle;
	/**
	 * 消息的下次可见（可再次被消费）时间。返回Unix时间戳，精确到秒。
	 */
	private long nextVisibleTime;
	/**
	 * 第一次消费该消息的时间。返回Unix时间戳，精确到秒。
	 */
	private long firstDequeueTime;
	/**
	 * 消息被消费的次数。
	 */
	private int dequeueCount;
	
	/**
	 * 批量发送消息返回对象
	 */
	private List<CMQQueueResult> msgList=new ArrayList<CMQQueueResult>();
	
	/**
	 * 批量消费消息返回对象
	 */
	private List<CMQQueueResult> msgInfoList =new ArrayList<CMQQueueResult>();
	
	/**
	 * 批量删除失败错误信息
	 */
	private List<CMQQueueResult> errorList = new ArrayList<CMQQueueResult>();
	
	
	public List<CMQQueueResult> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<CMQQueueResult> errorList) {
		this.errorList = errorList;
	}

	public List<CMQQueueResult> getMsgInfoList() {
		return msgInfoList;
	}

	public void setMsgInfoList(List<CMQQueueResult> msgInfoList) {
		this.msgInfoList = msgInfoList;
	}

	public List<CMQQueueResult> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<CMQQueueResult> msgList) {
		this.msgList = msgList;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public long getEnqueueTime() {
		return enqueueTime;
	}

	public void setEnqueueTime(long enqueueTime) {
		this.enqueueTime = enqueueTime;
	}

	public String getReceiptHandle() {
		return receiptHandle;
	}

	public void setReceiptHandle(String receiptHandle) {
		this.receiptHandle = receiptHandle;
	}

	public long getNextVisibleTime() {
		return nextVisibleTime;
	}

	public void setNextVisibleTime(long nextVisibleTime) {
		this.nextVisibleTime = nextVisibleTime;
	}

	public long getFirstDequeueTime() {
		return firstDequeueTime;
	}

	public void setFirstDequeueTime(long firstDequeueTime) {
		this.firstDequeueTime = firstDequeueTime;
	}

	public int getDequeueCount() {
		return dequeueCount;
	}

	public void setDequeueCount(int dequeueCount) {
		this.dequeueCount = dequeueCount;
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

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	
}
