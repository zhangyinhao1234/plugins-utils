/**
 * Create Date: 2016年4月8日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.cloud.queue;

import java.util.List;


/**
 * 消息队列接口 
 * （商户余额队列：sellerBalance）
 *
 * @author Francis
 * @version 1.0
 * @since 2016年4月8日
 */
public interface IMessageQueue {
	
	/**
	 * 发送单条消息
	 * @param msg
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	MessageResult sendMessage(Message msg);
	
	/**
	 * 批量发送消息
	 * @param msgs
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	MessageResult sendMessageBatch(List<Message> msgs);
	
	
	/**
	 * 接收消息
	 * @param maxNumberOfMessages
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	List<Message> receiveMessage(int maxNumberOfMessages);
	/**
	 * 接收一条消息
	 * @return
	 */
	Message receiveMessage();
	
	/**
	 * 删除消息
	 * @param msg
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	MessageResult deleteMessage(Message msg);
	
	/**
	 * 批量删除消息
	 * @param msgs
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	MessageResult deleteMessageBatch(List<Message> msgs);
	
	/**
	 * 取得队列中的消息数量
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	int getNumberOfMessages();

}
