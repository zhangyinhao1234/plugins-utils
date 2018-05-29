/**
 * Create Date: 2016年4月13日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.cloud.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送消息结果
 *
 * @author Francis
 * @version 1.0
 * @since 2016年4月13日
 */
public class MessageResult {
	
	/**
	 * 失败的消息
	 */
	private List<Message> failed=new ArrayList<Message>();
	
	/**
	 * 成功的消息
	 */
	private List<Message> successful=new ArrayList<Message>();
	
	/**
	 * 取得失败数量
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月19日
	 */
	public int getFailedNum(){
		if(this.failed == null){
			return 0;
		}
		return this.failed.size();
	}
	
	/**
	 * 取得成功数量
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月19日
	 */
	public int getSuccessfulNum(){
		if(this.successful == null){
			return 0;
		}
		return this.successful.size();
	}
	
	/**
	 * 增加失败的消息
	 * @param msg
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	public MessageResult addFailed(Message msg){
		if(this.failed == null){
			this.failed = new ArrayList<>();
		}
		this.failed.add(msg);
		return this;
	}
	
	/**
	 * 增加成功的消息
	 * @param msg
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	public MessageResult addSuccessful(Message msg){
		if(this.successful == null){
			this.successful = new ArrayList<>();
		}
		this.successful.add(msg);
		return this;
	}
	
	/**
	 * 是否存在失败的消息
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
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
	 * @return
	 *
	 * @author Francis
	 * @since 2016年4月13日
	 */
	public boolean existsSuccessful(){
		boolean flag = false;
		if(successful != null && !successful.isEmpty()){
			flag = true;
		}
		return flag;
	}

	/**
	 * @return the failed
	 */
	public List<Message> getFailed() {
		return failed;
	}

	/**
	 * @param failed the failed to set
	 */
	public void setFailed(List<Message> failed) {
		this.failed = failed;
	}

	/**
	 * @return the successful
	 */
	public List<Message> getSuccessful() {
		return successful;
	}

	/**
	 * @param successful the successful to set
	 */
	public void setSuccessful(List<Message> successful) {
		this.successful = successful;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("failed:");
		if(this.existsFailed()){
			strBuff.append(this.getFailed().size());
		}else{
			strBuff.append(0);
		}
		strBuff.append(",successful:");
		if(this.existsSuccessful()){
			strBuff.append(this.getSuccessful().size());
		}else{
			strBuff.append(0);
		}
		return strBuff.toString();
	}

}
