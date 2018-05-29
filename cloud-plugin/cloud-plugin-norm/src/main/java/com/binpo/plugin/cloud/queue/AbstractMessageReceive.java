package com.binpo.plugin.cloud.queue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 抽象的消息消费,继承这个类并且实现 {@link #receive(Message)}
 * 处理消息的业务处理完没有抛出异常会删除队列消息
 *
 * @author zhang 2017年7月21日 上午11:20:38
 */
public abstract class AbstractMessageReceive {

    private Log logger = LogFactory.getLog(MessageReceiveThread.class);
    private IMessageQueue queue;
    private int i = 1;
    
    public AbstractMessageReceive(IMessageQueue queue){
        this.queue = queue;
    }
    
    
    public void receive(int maxNumberOfMessages){
        List<Message> receiveMessage = queue.receiveMessage(maxNumberOfMessages);
        for(Message message : receiveMessage){
            try{
                receive(message);
                queue.deleteMessage(message);
            }catch(Exception e){
                logger.error("处理消息："+message.getMessageBody()+"异常",e);
                continue;
            }
        }
    }
    /**
     * 
     * 子类实现这个方法，读取消息，处理具体的业务
     * 
     * @param message
     */
    protected abstract void receive(Message message);
    
}
