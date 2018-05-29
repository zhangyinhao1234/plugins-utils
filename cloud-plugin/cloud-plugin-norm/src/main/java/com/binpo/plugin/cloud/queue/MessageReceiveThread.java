package com.binpo.plugin.cloud.queue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 消费消息的线程，消息消费完成会删除消息
 *
 * @author zhang 2017年7月21日 上午11:03:04
 */
public class MessageReceiveThread extends Thread{
    
    private DOMessageReceive todo;
    private IMessageQueue queue;
    private Log logger = LogFactory.getLog(MessageReceiveThread.class);
    private int i = 1;
    public MessageReceiveThread(IMessageQueue queue,int maxNumberOfMessages,DOMessageReceive receiveMessage){
        this.i = maxNumberOfMessages;
        this.queue = queue;
        this.todo = receiveMessage;
    }
    
    
    @Override
    public void run() {
        List<Message> receiveMessage2 = queue.receiveMessage(i);
        for(Message message : receiveMessage2){
            try{
                todo.run(message);
                queue.deleteMessage(message);
            }catch(Exception e){
                logger.error("处理消息："+message.getMessageBody()+"异常",e);
                continue;
            }
        }
    }
}
