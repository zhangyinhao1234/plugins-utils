/**
 * Create Date: 2015年11月10日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.wechat.mp.token.bean;

/**
 * jsapi_ticket是公众号用于调用微信JS接口的临时票据
 *
 * @author Francis
 * @version 1.0
 * @since 2015年11月10日
 */
public class Ticket {
	
	/**
	 * 获取到的临时票据
	 */
	private String ticket;
	
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
	 * @return the ticket
	 */
	public String getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
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
		strBuff.append("ticket=");
		strBuff.append(this.ticket);
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
