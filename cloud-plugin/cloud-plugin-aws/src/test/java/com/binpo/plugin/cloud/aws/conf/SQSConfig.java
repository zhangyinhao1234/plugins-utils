package com.binpo.plugin.cloud.aws.conf;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.aws.AWSFactory;
import com.binpo.plugin.cloud.aws.AWSSecretKey;
import com.binpo.plugin.cloud.aws.sns.AWSTopicClient;
import com.binpo.plugin.cloud.aws.sqs.AWSQueueClient;
import com.binpo.plugin.cloud.queue.IMessageQueue;
import com.binpo.plugin.cloud.topic.ITopic;

@Component
@Configurable
public class SQSConfig {

    @Value("${aws.key.secretId}")
    private String secretId;

    @Value("${aws.key.secretKey}")
    private String secretKey;

    @Bean("sellerBalance")
    public IMessageQueue sqsClient(ISecretKey secretKey) {
        String queueName = "seller_balance";
        String queueUrl = "https://sqs.cn-north-1.amazonaws.com.cn/xxxx/seller_balance";
        return new AWSQueueClient(secretKey, queueName, queueUrl);
    }

    @Bean(name="sellerBalanceNotification")
    public ITopic snsTopic(ISecretKey secretKey) {
        String topicName = "arn:aws-cn:sns:cn-north-1:xxxx:seller_balance";
        return new AWSTopicClient(secretKey,topicName);
    }

}
