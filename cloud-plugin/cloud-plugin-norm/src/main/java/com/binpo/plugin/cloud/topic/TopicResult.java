package com.binpo.plugin.cloud.topic;

import java.util.ArrayList;
import java.util.List;



/**
 * 消息投递结果
 * @author zhang
 *
 */
public class TopicResult {
	
	/**
	 * 失败的消息
	 */
	private List<Topic> failed=new ArrayList<Topic>();
	
	/**
	 * 成功的消息
	 */
	private List<Topic> successful=new ArrayList<Topic>();
	
	
	/**
	 * 取得失败数量
	 */
	public int getFailedNum(){
		if(this.failed == null){
			return 0;
		}
		return this.failed.size();
	}
	
	/**
	 * 取得成功数量
	 */
	public int getSuccessfulNum(){
		if(this.successful == null){
			return 0;
		}
		return this.successful.size();
	}
	
	/**
	 * 增加失败的消息
	 * @param topic
	 */
	public TopicResult addFailed(Topic topic){
		if(this.failed == null){
			this.failed = new ArrayList<>();
		}
		this.failed.add(topic);
		return this;
	}
	
	/**
	 * 增加成功的消息
	 * @param topic
	 */
	public TopicResult addSuccessful(Topic topic){
		if(this.successful == null){
			this.successful = new ArrayList<>();
		}
		this.successful.add(topic);
		return this;
	}
	
	/**
	 * 是否存在失败的消息
	 */
	public boolean existsFailed(){
		boolean flag = false;
		if(failed != null && !failed.isEmpty()){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 是否存在成功的消息
	 */
	public boolean existsSuccessful(){
		boolean flag = false;
		if(successful != null && !successful.isEmpty()){
			flag = true;
		}
		return flag;
	}


	public List<Topic> getFailed() {
		return failed;
	}

	public void setFailed(List<Topic> failed) {
		this.failed = failed;
	}

	public List<Topic> getSuccessful() {
		return successful;
	}

	public void setSuccessful(List<Topic> successful) {
		this.successful = successful;
	}

	
	
}
