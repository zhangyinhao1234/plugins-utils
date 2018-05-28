/**
 * Create Date: 2015年11月10日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.wechat.mp.token.bean;

/**
 * 签名
 *
 * @author Francis
 * @version 1.0
 * @since 2015年11月10日
 */
public class Sign {
	
	/**
	 * 公众号的唯一标识
	 */
	private String appId;
	
	/**
	 * 生成签名的时间戳
	 */
	private String timestamp;
	
	/**
	 * 生成签名的随机串
	 */
	private String nonceStr;
	
	/**
	 * 签名
	 */
	private String signature;
	
	/**
	 * 当前网页的URL，不包含#及其后面部分，location.href.split('#')[0]
	 */
	private String url;
	
	/**
	 * 获取到的临时票据
	 */
	private String jsapi_ticket;

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the nonceStr
	 */
	public String getNonceStr() {
		return nonceStr;
	}

	/**
	 * @param nonceStr the nonceStr to set
	 */
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the jsapi_ticket
	 */
	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	/**
	 * @param jsapi_ticket the jsapi_ticket to set
	 */
	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

}
