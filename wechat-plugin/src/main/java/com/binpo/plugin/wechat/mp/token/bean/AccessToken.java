/**
 * Create Date: 2015年11月10日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.wechat.mp.token.bean;

/**
 * access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token
 *
 * @author Francis
 * @version 1.0
 * @since 2015年11月10日
 */
public class AccessToken {
	
	/**
	 * 获取到的凭证
	 */
	private String access_token;
	
	/**
	 * 凭证有效时间，单位：秒
	 */
	private int expires_in;
	
	/**
	 * 错误代码
	 */
	private int errcode;
	
	/**
	 * 错误信息
	 */
	private String errmsg;

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the expires_in
	 */
	public int getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * @return the errcode
	 */
	public int getErrcode() {
		return errcode;
	}

	/**
	 * @param errcode the errcode to set
	 */
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	/**
	 * @return the errmsg
	 */
	public String getErrmsg() {
		return errmsg;
	}

	/**
	 * @param errmsg the errmsg to set
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("access_token=");
		strBuff.append(this.access_token);
		strBuff.append(",");
		strBuff.append("expires_in=");
		strBuff.append(this.expires_in);
		strBuff.append(",");
		strBuff.append("errcode=");
		strBuff.append(this.errcode);
		strBuff.append(",");
		strBuff.append("errmsg=");
		strBuff.append(this.errmsg);
		return strBuff.toString();
	}

}
