package com.binpo.plugin.cloud.queue;

/**
 * 
 * 消费消息的接口，实现这个接口，根据具体的消息内容去处理实际业务
 *
 * @author zhang 2017年7月21日 上午11:04:40
 */
public interface DOMessageReceive {
    void run(Message message);
}
