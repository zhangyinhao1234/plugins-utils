package com.binpo.plugin.cloud.aws.sqs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.BatchResultErrorEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResultEntry;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageBatchResultEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.queue.IMessageQueue;
import com.binpo.plugin.cloud.queue.Message;
import com.binpo.plugin.cloud.queue.MessageResult;

/**
 * 
 * AWS实现的消息队列
 *
 * @author zhang 2017年5月15日 下午6:27:08
 */
public class AWSQueueClient implements IMessageQueue {
    private Region region = Region.getRegion(Regions.CN_NORTH_1);
    private AmazonSQS sqsClient;
    private final IdWorker idWorker = new IdWorker();
    private ISecretKey key;

    private int queueBatchMessageAmount = 10;
    /**
     * 队列名
     */
    private String queueName;

    private String queueUrl;

    private String getQueueUrl() {
        return this.queueUrl;
    }

    private Log logger = LogFactory.getLog(getClass());


    public AWSQueueClient(ISecretKey key, String queueName, String queueUrl) {
        this.key = key;
        this.queueName = queueName;
        this.queueUrl = queueUrl;
        init();
    }

    private void init() {
        AWSCredentials credentials = new BasicAWSCredentials(key.getSecretId(), key.getSecretKey());
        AmazonSQSClient sqsClient_ = new AmazonSQSClient(credentials);
        sqsClient_.setRegion(region);
        this.sqsClient = sqsClient_;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.taocaimall.common.mq.IMessageQueue#sendMessage(com.taocaimall.common.
     * mq.Message)
     */
    @Override
    public MessageResult sendMessage(Message msg) {
        MessageResult rs = new MessageResult();
        try {
            this.logDebug("SQS发送消息", this.queueName, msg.toString());

            SendMessageRequest request = new SendMessageRequest();
            request.setQueueUrl(this.getQueueUrl());
            request.setMessageBody(msg.getMessageBody());

            Map<String, String> attributes = msg.getMessageAttributes();
            if (attributes != null && !attributes.isEmpty()) {
                Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
                Set<String> attributesKeySet = attributes.keySet();
                for (String key : attributesKeySet) {
                    MessageAttributeValue attributeValue = new MessageAttributeValue();
                    attributeValue.setStringValue(attributes.get(key));
                    messageAttributes.put(key, attributeValue);
                }
                request.setMessageAttributes(messageAttributes);
            }

            com.amazonaws.services.sqs.model.SendMessageResult result = sqsClient.sendMessage(request);
            String messageId = result.getMessageId();
            // 根据messageId判断是否发送成功
            if (messageId != null) {
                rs.addSuccessful(msg);
            } else {
                rs.addFailed(msg);
            }
        } catch (Exception e) {
            this.logError("SQS发送消息异常", this.queueName, msg.toString(), e);
        }
        return rs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.taocaimall.common.mq.IMessageQueue#sendMessageBatch(java.util.List)
     */
    @Override
    public MessageResult sendMessageBatch(List<Message> msgs) {
        MessageResult rs = new MessageResult();
        try {
            this.logDebug("SQS批量发送消息", this.queueName, msgs.toString());

            SendMessageBatchRequest request = new SendMessageBatchRequest();
            request.setQueueUrl(this.getQueueUrl());

            List<SendMessageBatchRequestEntry> entries = new ArrayList<>();
            SendMessageBatchRequestEntry entry = null;
            for (Message msg : msgs) {
                String id = String.valueOf(idWorker.getId());
                msg.setId(id);
                entry = new SendMessageBatchRequestEntry();
                entry.setId(id);
                entry.setMessageBody(msg.getMessageBody());

                Map<String, String> attributes = msg.getMessageAttributes();
                if (attributes != null && !attributes.isEmpty()) {
                    Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
                    Set<String> attributesKeySet = attributes.keySet();
                    for (String key : attributesKeySet) {
                        MessageAttributeValue attributeValue = new MessageAttributeValue();
                        attributeValue.setStringValue(attributes.get(key));
                        messageAttributes.put(key, attributeValue);
                    }
                    entry.setMessageAttributes(messageAttributes);
                }

                entries.add(entry);
            }
            request.setEntries(entries);

            // 封装返回结果
            SendMessageBatchResult result = sqsClient.sendMessageBatch(request);
            List<BatchResultErrorEntry> errorEntries = result.getFailed();
            if (errorEntries != null && !errorEntries.isEmpty()) {
                for (BatchResultErrorEntry errorEntry : errorEntries) {
                    for (Message msg : msgs) {
                        if (errorEntry.getId().equals(msg.getId())) {
                            msg.setCode(errorEntry.getCode());
                            rs.addFailed(msg);
                        }
                    }
                }
            }
            List<SendMessageBatchResultEntry> resultEntries = result.getSuccessful();
            if (resultEntries != null && !resultEntries.isEmpty()) {
                for (SendMessageBatchResultEntry resultEntry : resultEntries) {
                    for (Message msg : msgs) {
                        if (resultEntry.getId().equals(msg.getId())) {
                            rs.addSuccessful(msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            this.logError("SQS批量发送消息异常", this.queueName, msgs.toString(), e);
        }
        return rs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.taocaimall.common.mq.IMessageQueue#receiveMessage(int)
     */
    @Override
    public List<Message> receiveMessage(int maxNumberOfMessages) {
        List<Message> msgs = new ArrayList<>();
        Message msg = null;
        try {
            // 控制每次接收消息的数量不能超出队列批量处理能力
            if (maxNumberOfMessages <= 0) {
                maxNumberOfMessages = 1;
            } else if (maxNumberOfMessages > queueBatchMessageAmount) {
                maxNumberOfMessages = queueBatchMessageAmount;
            }

            this.logDebug("SQS接收消息", this.queueName, "maxNumberOfMessages:" + maxNumberOfMessages);

            ReceiveMessageRequest request = new ReceiveMessageRequest();
            request.setQueueUrl(this.getQueueUrl());
            request.setMaxNumberOfMessages(maxNumberOfMessages);
            List<String> attributeNames = new ArrayList<>();
            attributeNames.add("All");
            request.setAttributeNames(attributeNames);
            List<String> messageAttributeNames = new ArrayList<>();
            messageAttributeNames.add("All");
            request.setMessageAttributeNames(messageAttributeNames);

            // 封装返回结果
            ReceiveMessageResult result = sqsClient.receiveMessage(request);
            List<com.amazonaws.services.sqs.model.Message> rsMsgs = result.getMessages();
            for (com.amazonaws.services.sqs.model.Message rsMsg : rsMsgs) {
                msg = new Message();
                msg.setMessageId(rsMsg.getMessageId());
                msg.setMessageBody(rsMsg.getBody());
                msg.setReceiptHandle(rsMsg.getReceiptHandle());
                msg.setAttributes(rsMsg.getAttributes());
                Map<String, MessageAttributeValue> rsMsgAttributes = rsMsg.getMessageAttributes();
                if (rsMsgAttributes != null && !rsMsgAttributes.isEmpty()) {
                    Map<String, String> messageAttributes = new HashMap<>();
                    Set<String> rsMsgAttributesKeySet = rsMsgAttributes.keySet();
                    for (String key : rsMsgAttributesKeySet) {
                        MessageAttributeValue attributeValue = rsMsgAttributes.get(key);
                        messageAttributes.put(key, attributeValue.getStringValue());
                    }
                    msg.setMessageAttributes(messageAttributes);
                }
                this.logDebug("SQS接收消息的内容", this.queueName, "msg：:" + msg.toString());
                msgs.add(msg);
            }
        } catch (Exception e) {
            this.logError("SQS接收消息异常", this.queueName, "maxNumberOfMessages:" + maxNumberOfMessages, e);
        }
        return msgs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.taocaimall.common.mq.IMessageQueue#deleteMessage(com.taocaimall.
     * common.mq.Message)
     */
    @Override
    public MessageResult deleteMessage(Message msg) {
        MessageResult rs = new MessageResult();
        try {
            this.logDebug("SQS删除消息", this.queueName, msg.toString());

            sqsClient.deleteMessage(this.getQueueUrl(), msg.getReceiptHandle());
            rs.addSuccessful(msg);
        } catch (Exception e) {
            this.logError("SQS删除消息异常", this.queueName, msg.toString(), e);
        }
        return rs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.taocaimall.common.mq.IMessageQueue#deleteMessageBatch(java.util.List)
     */
    @Override
    public MessageResult deleteMessageBatch(List<Message> msgs) {
        MessageResult rs = new MessageResult();
        try {
            this.logDebug("SQS批量删除消息", this.queueName, msgs.toString());

            List<DeleteMessageBatchRequestEntry> requestEntries = new ArrayList<>();
            DeleteMessageBatchRequestEntry requestEntry = null;
            for (Message msg : msgs) {
                String id = String.valueOf(idWorker.getId());
                msg.setId(id);
                requestEntry = new DeleteMessageBatchRequestEntry();
                requestEntry.setId(id);
                requestEntry.setReceiptHandle(msg.getReceiptHandle());
                requestEntries.add(requestEntry);
            }

            // 封装返回结果
            DeleteMessageBatchResult result = sqsClient.deleteMessageBatch(this.getQueueUrl(),
                    requestEntries);
            List<BatchResultErrorEntry> errorEntries = result.getFailed();
            if (errorEntries != null && !errorEntries.isEmpty()) {
                for (BatchResultErrorEntry errorEntry : errorEntries) {
                    for (Message msg : msgs) {
                        if (errorEntry.getId().equals(msg.getId())) {
                            msg.setCode(errorEntry.getCode());
                            rs.addFailed(msg);
                        }
                    }
                }
            }
            List<DeleteMessageBatchResultEntry> resultEntries = result.getSuccessful();
            if (resultEntries != null && !resultEntries.isEmpty()) {
                for (DeleteMessageBatchResultEntry resultEntry : resultEntries) {
                    for (Message msg : msgs) {
                        if (resultEntry.getId().equals(msg.getId())) {
                            rs.addSuccessful(msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            this.logError("SQS批量删除消息异常", this.queueName, msgs.toString(), e);
        }
        return rs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.taocaimall.common.mq.IMessageQueue#getNumberOfMessages()
     */
    @Override
    public int getNumberOfMessages() {
        int numberOfMessages = -1;
        try {
            this.logDebug("SQS取得队列中的消息数量", this.queueName, null);

            List<String> attributeNames = new ArrayList<>();
            // 可见消息数量
            attributeNames.add("ApproximateNumberOfMessages");
            // 不可见消息数量
            attributeNames.add("ApproximateNumberOfMessagesNotVisible");
            GetQueueAttributesResult attributesResult = sqsClient.getQueueAttributes(this.getQueueUrl(),
                    attributeNames);
            Map<String, String> attributes = attributesResult.getAttributes();
            int approximateNumberOfMessages = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));
            int approximateNumberOfMessagesNotVisible = Integer
                    .parseInt(attributes.get("ApproximateNumberOfMessagesNotVisible"));
            // 消息数量=可见消息数量+不可见消息数量
            numberOfMessages = approximateNumberOfMessages + approximateNumberOfMessagesNotVisible;
        } catch (Exception e) {
            this.logError("SQS取得队列中的消息数量异常", this.queueName, null, e);
        }
        return numberOfMessages;
    }

    /**
     * 记录错误日志
     * 
     * @param msg
     * @param queueName
     * @param content
     * @param e
     *
     * @author Francis
     * @since 2016年4月19日
     */
    private void logError(String msg, String queueName, String content, Exception e) {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(msg);
        strBuff.append(":");
        strBuff.append(queueName);
        if (content != null && !content.equals("")) {
            strBuff.append("->");
            strBuff.append(content);
        }
        logger.error(strBuff.toString(), e);
    }

    /**
     * 记录调试信息
     * 
     * @param msg
     * @param queueName
     * @param content
     *
     * @author Francis
     * @since 2016年4月19日
     */
    private void logDebug(String msg, String queueName, String content) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(msg);
        strBuff.append(":");
        strBuff.append(queueName);
        if (content != null && !content.equals("")) {
            strBuff.append("->");
            strBuff.append(content);
        }
        logger.debug(strBuff.toString());
    }

    @Override
    public Message receiveMessage() {
        List<Message> receiveMessage = receiveMessage(1);
        if (receiveMessage.isEmpty())
            return new Message();
        return receiveMessage.get(0);
    }
}
