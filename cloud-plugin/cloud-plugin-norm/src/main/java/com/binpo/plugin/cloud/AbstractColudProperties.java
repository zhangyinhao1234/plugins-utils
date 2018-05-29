package com.binpo.plugin.cloud;

public class AbstractColudProperties {
	/**
	 * 订阅的协议
	 * @author zhang
	 *
	 */
	public enum Protocol{
		http,queue,email,https,sqs,sms
	}
	/**
	 * 推送内容的格式
	 * @author zhang
	 *
	 */
	public enum ContentFormat{
		JSON,SIMPLIFIED,EMAIL
	}
}
