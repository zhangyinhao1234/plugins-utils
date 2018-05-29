package com.binpo.plugin.cloud.qcloud.cmq;
/**
 * 队列信息
 * @author zhang
 *
 */
public class CMQQueueInfo {
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
	 * 最大堆积消息数。取值范围在公测期间为 1,000,000 - 10,000,000，
	 * 正式上线后范围可达到 1000,000-1000,000,000。
	 * 默认取值在公测期间为 10,000,000，正式上线后为 1000,000,000。
	 */
	private int maxMsgHeapNum;
	/**
	 * 消息接收长轮询等待时间。取值范围0-30秒，默认值0。
	 */
	private int pollingWaitSeconds;
	/**
	 * 消息可见性超时。取值范围1-43200秒（即12小时内），默认值30。
	 */
	private int visibilityTimeout;
	/**
	 * 消息最大长度。取值范围1024-65536 Byte（即1-64K），默认值65536。
	 */
	private int maxMsgSize;
	
	/**
	 * 消息保留周期。取值范围60-1296000秒（1min-15天），默认值345600 (4 天)。
	 */
	private int msgRetentionSeconds;
	/**
	 * 队列的创建时间。返回Unix时间戳，精确到秒。
	 */
	private long createTime;
	/**
	 * 最后一次修改队列属性的时间。返回Unix时间戳，精确到秒。
	 */
	private long lastModifyTime;
	/**
	 * 在队列中处于 Active 状态（不处于被消费状态）的消息总数，为近似值。
	 */
	private int activeMsgNum;
	
	/**
	 * 在队列中处于 Inactive 状态（正处于被消费状态）的消息总数，为近似值。
	 */
	private int inactiveMsgNum;

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

	public int getMaxMsgHeapNum() {
		return maxMsgHeapNum;
	}

	public void setMaxMsgHeapNum(int maxMsgHeapNum) {
		this.maxMsgHeapNum = maxMsgHeapNum;
	}

	public int getPollingWaitSeconds() {
		return pollingWaitSeconds;
	}

	public void setPollingWaitSeconds(int pollingWaitSeconds) {
		this.pollingWaitSeconds = pollingWaitSeconds;
	}

	public int getVisibilityTimeout() {
		return visibilityTimeout;
	}

	public void setVisibilityTimeout(int visibilityTimeout) {
		this.visibilityTimeout = visibilityTimeout;
	}

	public int getMaxMsgSize() {
		return maxMsgSize;
	}

	public void setMaxMsgSize(int maxMsgSize) {
		this.maxMsgSize = maxMsgSize;
	}

	public int getMsgRetentionSeconds() {
		return msgRetentionSeconds;
	}

	public void setMsgRetentionSeconds(int msgRetentionSeconds) {
		this.msgRetentionSeconds = msgRetentionSeconds;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public int getActiveMsgNum() {
		return activeMsgNum;
	}

	public void setActiveMsgNum(int activeMsgNum) {
		this.activeMsgNum = activeMsgNum;
	}

	public int getInactiveMsgNum() {
		return inactiveMsgNum;
	}

	public void setInactiveMsgNum(int inactiveMsgNum) {
		this.inactiveMsgNum = inactiveMsgNum;
	}
	
	
	
	
}
