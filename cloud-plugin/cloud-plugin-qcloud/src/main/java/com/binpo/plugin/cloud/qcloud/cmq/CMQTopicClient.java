package com.binpo.plugin.cloud.qcloud.cmq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.qcloud.QCloud.CCloudCode;
import com.binpo.plugin.cloud.qcloud.QCloud.QCloudRegion;
import com.binpo.plugin.cloud.qcloud.util.ApiRequest;
import com.binpo.plugin.cloud.qcloud.util.ApiResponse;
import com.binpo.plugin.cloud.qcloud.util.BspAPI;
import com.binpo.plugin.cloud.qcloud.util.BspAPI.QcloudHttpType;
import com.binpo.plugin.cloud.topic.ITopic;
import com.binpo.plugin.cloud.topic.Subscription;
import com.binpo.plugin.cloud.topic.Topic;
import com.binpo.plugin.cloud.topic.TopicResult;
/**
 * 订阅操作
 * @author zhang
 *
 */
public class CMQTopicClient implements ITopic{

	private String Region="sh";//华南
	
	/**
	 * 腾讯内网地址  建议使用内网
	 */
	private String URL = "cmq-topic-"+Region+".api.tencentyun.com/v2/index.php";
	/**
	 * 腾讯外网地址
	 */
	private String topic_url = "cmq-topic-"+Region+".api.qcloud.com/v2/index.php";
	private QcloudHttpType httptype = QcloudHttpType.http;
	/**
	 * 主题名
	 */
	private String topicName;
	
	private Log logger = LogFactory.getLog(getClass());
	private ISecretKey key ;
	/**
	 * 默认广州区 采用默认内网 http
	 * @param topicName 主题名
	 */
	public CMQTopicClient(String topicName,ISecretKey key){
		this.topicName = topicName;
		this.key = key;
	}
	/**
	 * 采用默认内网 http
	 * @param region 地区
	 * @param topicName
	 */
	public CMQTopicClient(QCloudRegion region,String topicName,ISecretKey key){
		this.topicName = topicName;
		this.Region = region.toString();
		this.key = key;
	}
	/**
	 * 内网 http 外网走https协议
	 * @param region 地区
	 * @param topicName 主题名
	 * @param httptype http/https
	 */
	public CMQTopicClient(QCloudRegion region,String topicName,QcloudHttpType httptype,ISecretKey key){
		this.topicName = topicName;
		this.Region = region.toString();
		if(httptype.equals(QcloudHttpType.https)){
			this.URL = this.topic_url;
		}
		this.httptype = httptype;
		this.key = key;
	}
	
	
	@Override
	public TopicResult publish(Topic topic) {
		if(topic==null||topic.getMessageBody()==null)
			throw new RuntimeException("消息的内容不能为空");
		Map<String,String> params =new HashMap<String,String>();
    	params.put("topicName", this.topicName);
    	params.put("msgBody", topic.getMessageBody());
        List<String> tags = topic.getTags();
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                params.put("msgTag." + i, tags.get(i));
            }
        }
    	params.putAll(topic.getAttributes());
    	String url = null;
    	try{
    		url = BspAPI.makeURL("GET", "PublishMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return new TopicResult();
    	}
    	logger.debug("发送消息内容："+topic.toString());
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("发送结果："+res.getBody());
		CMQTopicResult cmqtopicResult = JSON.parseObject(res.getBody().toString(), CMQTopicResult.class);
		TopicResult topicResult = new TopicResult();
		topic.setCode(cmqtopicResult.getCode()+"");
		topic.setResultInfo(cmqtopicResult.getMessage());
    	if(cmqtopicResult.getCode()==CCloudCode.success){
    		topic.setMessageId(cmqtopicResult.getMsgId());
    		topicResult.addSuccessful(topic);
    	}else{
    		topicResult.addFailed(topic);
    	}
    	logger.debug("解析后结果："+JSON.toJSONString(topicResult));
		return topicResult;
	}

	@Override
	public TopicResult publishBatch(List<Topic> topics) {
		Map<String,String> params =new HashMap<String,String>();
    	params.put("topicName", this.topicName);
    	int i = 1;
    	for(Topic topic : topics){
    		params.put("msgBody."+i, topic.getMessageBody());
    		params.putAll(topic.getAttributes());
    		i++;
    	}
    	String url = null;
    	try{
    		url = BspAPI.makeURL("GET", "BatchPublishMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return new TopicResult();
    	}
		
    	logger.debug("发送消息内容："+topics.toString());
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("发送结果："+res.getBody());
    	CMQTopicResult cmqtopicResult = JSON.parseObject(res.getBody().toString(), CMQTopicResult.class);
		TopicResult topicResult = new TopicResult();
		for(Topic topic : topics){
			topic.setCode(cmqtopicResult.getCode()+"");
			topic.setResultInfo(cmqtopicResult.getMessage());
			if(cmqtopicResult.getCode()==CCloudCode.success){
				topicResult.addSuccessful(topic);
			}else{
				topicResult.addFailed(topic);
			}
		}
		return topicResult;
	}

	@Override
	public Subscription subscription(Subscription subscription) {
		Map<String,String> params =new HashMap<String,String>();
    	params.put("topicName", this.topicName);
    	params.put("subscriptionName", subscription.getSubscriptionName());
        params.put("protocol", subscription.getProtocol().toString());
        params.put("endpoint", subscription.getEndpoint());
        List<String> filterTags = subscription.getFilterTags();
        if(filterTags!=null){
            for(int i =0;i<filterTags.size();i++){
                params.put("filterTag."+i, filterTags.get(i));
            }
        }
    	params.putAll(subscription.getAttributes());
    	String url = null;
    	try{
    		url = BspAPI.makeURL("GET", "Subscribe", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return subscription;
    	}
    	logger.debug("发送消息内容："+subscription.toString());
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("发送结果："+res.getBody());
    	
    	Map<String,Object> parseObject = JSON.parseObject(res.getBody().toString(), Map.class);
    	subscription.setCode(parseObject.get("code").toString());
    	subscription.setRequestId(parseObject.get("requestId").toString());
    	subscription.setResultInfo(parseObject.get("message").toString());
		return subscription;
	}
	public String getRegion() {
		return Region;
	}
	public void setRegion(String region) {
		Region = region;
	}
	public QcloudHttpType getHttptype() {
		return httptype;
	}
	public void setHttptype(QcloudHttpType httptype) {
		if(httptype.equals(QcloudHttpType.https)){
			this.URL = this.topic_url;
		}
		this.httptype = httptype;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public void setKey(ISecretKey key) {
		this.key = key;
	}

	
}
