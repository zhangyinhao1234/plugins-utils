package com.binpo.plugin.cloud.topic;

import java.util.List;

/**
 * 主题订阅（异常邮件注入：exceptionTopic；sellerBalance
 * @author zhang
 *
 */
public interface ITopic {
	/**
	 * 发布消息
	 * @param topic 发送内容
	 * @return
	 */
	TopicResult publish(Topic topic);
	
	/**
	 * 批量发布消息
	 * @param topics 发送内容
	 * @return
	 */
	TopicResult publishBatch(List<Topic> topics);
	
	/**
	 * 订阅
	 * @param subscription
	 * @return
	 */
	Subscription subscription(Subscription subscription);
	
	
}
