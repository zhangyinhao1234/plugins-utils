package com.binpo.plugin.cloud.qcloud.cmq;

import java.util.ArrayList;
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
import com.binpo.plugin.cloud.queue.IMessageQueue;
import com.binpo.plugin.cloud.queue.Message;
import com.binpo.plugin.cloud.queue.MessageResult;
/**
 * 腾讯云的消息队列发送和接收
 * 默认内网http
 * 默认广州区
 * @author zhang
 *
 */
public class CMQQueueClient implements IMessageQueue{
	private String Region="sh";//华南
	/**
	 * 内网域名地址 仅支持http
	 */
	private String URL = "cmq-queue-"+Region+".api.tencentyun.com/v2/index.php";
	/**
	 * 外网域名地址，仅支持https
	 */
	private String  cmq_url="cmq-queue-"+Region+".api.qcloud.com/v2/index.php";
	
	/**
	 * 队列名
	 */
	private String queueName;
	
	private QcloudHttpType httptype = QcloudHttpType.http;
	
	private Log logger = LogFactory.getLog(getClass());
	
	private ISecretKey key ;
	
	public CMQQueueClient(){
		
	}
	/**
	 * 默认广州区 采用默认内网 http
	 * @param queueName 队列名
	 */
	public CMQQueueClient(String queueName,ISecretKey key){
		this.queueName = queueName;
		this.key = key;
	}
	/**
	 * 采用默认内网 http
	 * @param queueName 队列名
	 */
	public CMQQueueClient(QCloudRegion region,String queueName,ISecretKey key){
		this.queueName = queueName;
		this.Region = region.toString();
		this.key = key;
	}
	
	public CMQQueueClient(QCloudRegion region,String queueName,QcloudHttpType httptype,ISecretKey key){
		this.queueName = queueName;
		this.Region = region.toString();
		if(httptype.equals(QcloudHttpType.https)){
			this.URL = this.cmq_url;
		}
		this.httptype = httptype;
		this.key = key;
	}
	

	@Override
	public MessageResult sendMessage(Message msg) {
		if(msg==null||msg.getMessageBody()==null){
			throw new RuntimeException("消息的内容不能为空");
		}
		Map<String,String> params =new HashMap<String,String>();
    	params.put("queueName", this.queueName);
    	params.put("msgBody", msg.getMessageBody());
    	String url = null;
    	try{
    		url = BspAPI.makeURL("GET", "SendMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    		logger.debug(url);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return new MessageResult();
    	}
    	logger.debug("发送消息内容："+msg.toString());
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("发送结果："+res.getBody());
		CMQQueueResult cmqResult = JSON.parseObject(res.getBody().toString(), CMQQueueResult.class);
		MessageResult result = new MessageResult();
		msg.setCode(cmqResult.getCode()+"");
		msg.setResultInfo(cmqResult.getMessage());
		if(CCloudCode.success==cmqResult.getCode()){
			msg.setMessageId(cmqResult.getMsgId());
			msg.setRequestId(cmqResult.getRequestId());
			result.addSuccessful(msg);
		}else{
			result.addFailed(msg);
		}
		logger.debug("封装后的数据："+JSON.toJSONString(result));
		return result;
	}

	@Override
	public MessageResult sendMessageBatch(List<Message> msgs) {
		if(msgs.isEmpty())
			return new MessageResult();
		Map<String,String> params =new HashMap<String,String>();
    	params.put("queueName", this.queueName);
    	int i = 1;
    	for(Message msg : msgs){
    		if(msg.getMessageBody()==null)
    			throw new RuntimeException("待发送的消息内容中存在消息内容为空的对象");
    		params.put("msgBody."+i, msg.getMessageBody());
    		i++;
    	}
    	String url = null;
    	try{
    		url = BspAPI.makeURL("GET", "BatchSendMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return new MessageResult();
    	}
    	for(Message msg : msgs){
    		logger.debug("发送消息内容："+msg.toString());
    	}
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("发送结果："+res.getBody());
		CMQQueueResult cmqResult = JSON.parseObject(res.getBody().toString(), CMQQueueResult.class);
		
		MessageResult result = new MessageResult();
		List<CMQQueueResult> msgList = cmqResult.getMsgList();
		for(int j =0;j<msgList.size();j++){
			CMQQueueResult cmqResult2 = msgList.get(j);
			Message message = msgs.get(j);
			message.setMessageId(cmqResult2.getMsgId());
			message.setCode(cmqResult2.getCode()+"");
			message.setRequestId(cmqResult.getRequestId());
			if(cmqResult.getCode()==CCloudCode.success){
				result.addSuccessful(message);
			}else{
				result.addFailed(message);
			}
		}
		logger.debug("封装后的数据："+JSON.toJSONString(result));
		return result;
	}

	@Override
	public List<Message> receiveMessage(int maxNumberOfMessages) {
		ArrayList<Message> msgs = new ArrayList<Message>();
		if(maxNumberOfMessages<=0)
			return msgs;
		Map<String,String> params =new HashMap<String,String>();
    	params.put("queueName", this.queueName);
    	params.put("numOfMsg",maxNumberOfMessages+"");
    	String url=null;
    	try{
    		url = BspAPI.makeURL("GET", "BatchReceiveMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return msgs;
    	}
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("接收到的消息："+res.getBody());
    	CMQQueueResult cmqResult = JSON.parseObject(res.getBody().toString(), CMQQueueResult.class);
		if(cmqResult.getCode()==CCloudCode.success){
			List<CMQQueueResult> msgInfoList = cmqResult.getMsgInfoList();
			for(CMQQueueResult info : msgInfoList){
				Message msg = new Message();
				msg.setCode(cmqResult.getCode()+"");
				msg.setResultInfo(cmqResult.getMessage());
				msg.setMessageBody(info.getMsgBody());
	    		msg.setMessageId(info.getMsgId());
	    		msg.setReceiptHandle(info.getReceiptHandle());
	    		msg.setRequestId(info.getRequestId());
	    		msgs.add(msg);
			}
		}
		logger.debug("封装后的数据："+JSON.toJSONString(msgs));
		return msgs;
	}

	@Override
	public Message receiveMessage() {
		Map<String,String> params =new HashMap<String,String>();
    	params.put("queueName", this.queueName);
    	String url=null;
    	try{
    		url = BspAPI.makeURL("GET", "ReceiveMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return new Message();
    	}
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("接收到的消息："+res.getBody());
    	CMQQueueResult cmqResult = JSON.parseObject(res.getBody().toString(), CMQQueueResult.class);
    	Message msg = new Message();
    	msg.setCode(cmqResult.getCode()+"");
		msg.setResultInfo(cmqResult.getMessage());
    	if(CCloudCode.success==cmqResult.getCode()){
    		msg.setMessageBody(cmqResult.getMsgBody());
    		msg.setMessageId(cmqResult.getMsgId());
    		msg.setReceiptHandle(cmqResult.getReceiptHandle());
    		msg.setRequestId(cmqResult.getRequestId());
    	}
    	logger.debug("封装后的数据："+JSON.toJSONString(msg));
		return msg;
	}

	@Override
	public MessageResult deleteMessage(Message msg) {
		if(msg==null||msg.getReceiptHandle()==null){
			throw new RuntimeException("消息接收句柄不能为空");
		}
		Map<String,String> params =new HashMap<String,String>();
    	params.put("queueName", this.queueName);
    	params.put("receiptHandle", msg.getReceiptHandle());
    	String url=null;
    	try{
    		url = BspAPI.makeURL("GET", "DeleteMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return new MessageResult();
    	}
    	MessageResult messageResult = new MessageResult();
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("删除的消息返回结果："+res.getBody());
    	CMQQueueResult cmqResult = JSON.parseObject(res.getBody().toString(), CMQQueueResult.class);
    	msg.setCode(cmqResult.getCode()+"");
		msg.setResultInfo(cmqResult.getMessage());
    	if(CCloudCode.success==cmqResult.getCode()){
    		msg.setRequestId(cmqResult.getRequestId());
    		messageResult.addSuccessful(msg);
    	}else{
    		messageResult.addFailed(msg);
    	}
    	logger.debug("封装后的数据："+JSON.toJSONString(messageResult));
    	return messageResult;
	}

	@Override
	public MessageResult deleteMessageBatch(List<Message> msgs) {
		if(msgs.isEmpty())
			return new MessageResult();
		Map<String,String> params =new HashMap<String,String>();
    	params.put("queueName", this.queueName);
    	int i = 1;
    	for(Message msg : msgs){
    		if(msg.getReceiptHandle()==null)
    			throw new RuntimeException("消息接收句柄不能为空");
    		params.put("receiptHandle."+i, msg.getReceiptHandle());
    		i++;
    	}
    	String url=null;
    	try{
    		url = BspAPI.makeURL("GET", "BatchDeleteMessage", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return new MessageResult();
    	}
    	MessageResult messageResult = new MessageResult();
    	ApiResponse res = ApiRequest.sendGet(url, "");
    	logger.debug("删除的消息返回结果："+res.getBody());
    	CMQQueueResult cmqResult = JSON.parseObject(res.getBody().toString(), CMQQueueResult.class);
    	List<CMQQueueResult> errorList = cmqResult.getErrorList();
    	
    	for(Message msg : msgs){
    		boolean fail=false;
    		for(CMQQueueResult error:errorList){
    			String receiptHandle = error.getReceiptHandle();
    			if(msg.getReceiptHandle().equals(receiptHandle)){
    				fail = true;
    				break;
    			}
    		}
    		if(fail){
    			messageResult.addFailed(msg);
    		}else{
    			messageResult.addSuccessful(msg);
    		}
    		
    	}
		logger.debug("封装后的数据："+JSON.toJSONString(messageResult));
		return messageResult;
	}

	@Override
	public int getNumberOfMessages() {
		Map<String,String> params =new HashMap<String,String>();
    	params.put("queueName", this.queueName);
    	String url = null;
    	try{
    		url = BspAPI.makeURL("GET", "GetQueueAttributes", Region, key.getSecretId(), key.getSecretKey(), params, "utf-8",
    				this.httptype,URL);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("构造请求url失败",e);
    		return 10;
    	}
    	ApiResponse res = ApiRequest.sendGet(url, "");
		CMQQueueInfo info = JSON.parseObject(res.getBody().toString(), CMQQueueInfo.class);
		logger.debug("队列信息："+JSON.toJSONString(info));
		if(CCloudCode.success==info.getCode()){
			return info.getActiveMsgNum();
		}
		return 10;
	}
	public String getRegion() {
		return Region;
	}
	public void setRegion(String region) {
		Region = region;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public QcloudHttpType getHttptype() {
		return httptype;
	}
	public void setHttptype(QcloudHttpType httptype) {
		if(httptype.equals(QcloudHttpType.https)){
			this.URL = this.cmq_url;
		}
		this.httptype = httptype;
	}
	public void setKey(ISecretKey key) {
		this.key = key;
	}

	
}
