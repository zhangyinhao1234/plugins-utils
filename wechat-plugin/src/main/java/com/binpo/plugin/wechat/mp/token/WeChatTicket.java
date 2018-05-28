/**
 * Create Date: 2015年11月5日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.wechat.mp.token;

import com.binpo.plugin.wechat.mp.token.bean.Ticket;
import com.binpo.plugin.wechat.util.HttpUtil;

/**
 * 微信Ticket
 *
 * @author Francis
 * @version 1.0
 * @since 2015年11月5日
 */
public class WeChatTicket {
	
	/**
	 * 取得Ticket
	 * @param accessToken
	 * @return
	 *
	 * @author Francis
	 * @since 2015年11月10日
	 */
	public static Ticket getJsapiTicket(String accessToken){
		String url = String.format(WeChatConfig.URL_JSAPI_TICKET + "&access_token=%s", accessToken);
		Ticket ticket = HttpUtil.getJsonByRequestWithGet(url, null, Ticket.class);
		return ticket;
	}

}
