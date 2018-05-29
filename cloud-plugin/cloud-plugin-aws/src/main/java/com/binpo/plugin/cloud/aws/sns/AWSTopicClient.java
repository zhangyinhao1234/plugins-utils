package com.binpo.plugin.cloud.aws.sns;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.topic.ITopic;
import com.binpo.plugin.cloud.topic.Subscription;
import com.binpo.plugin.cloud.topic.Topic;
import com.binpo.plugin.cloud.topic.TopicResult;

/**
 * aws 的订阅实现
 * @author zhang
 *
 */
public class AWSTopicClient implements ITopic{
	private Region region = Region.getRegion(Regions.CN_NORTH_1);
	private AmazonSNS sns;
	private ISecretKey key ;
	
	/**
	 * 主题名
	 */
	private String topicName;
	
	private Log logger = LogFactory.getLog(getClass());

	
	public AWSTopicClient (ISecretKey key,String topicName) {
	    this.key = key;
	    this.topicName = topicName;
        init();
	}
	
	private void init(){
		AWSCredentials credentials = new BasicAWSCredentials(key.getSecretId(),key.getSecretKey());
		AmazonSNSClient snsClient=new AmazonSNSClient(credentials);
		snsClient.setRegion(region);
		sns = snsClient;
	}
	@Override
	public TopicResult publish(Topic topic) {
		PublishRequest publishRequest = new PublishRequest(this.topicName,topic.getMessageBody());
		PublishResult publishResult = sns.publish(publishRequest);
		String messageId = publishResult.getMessageId();
		logger.debug(messageId);
		topic.setMessageId(messageId);
		TopicResult topicResult = new TopicResult();
		topicResult.addSuccessful(topic);
		return topicResult;
	}

	
	@Override
	public TopicResult publishBatch(List<Topic> topics) {
		TopicResult topicResult = new TopicResult();
		for(Topic topic : topics){
			PublishRequest publishRequest = new PublishRequest(this.topicName,topic.getMessageBody());
			PublishResult publishResult = sns.publish(publishRequest);
			String messageId = publishResult.getMessageId();
			topic.setMessageId(messageId);
			topicResult.addSuccessful(topic);
		}
		return topicResult;
	}

	@Override
	public Subscription subscription(Subscription subscription) {
		sns.subscribe(this.topicName, subscription.getProtocol().toString(), subscription.getEndpoint().toString());
		return subscription;
	}

    public void setKey(ISecretKey key) {
        this.key = key;
    }
	

}
