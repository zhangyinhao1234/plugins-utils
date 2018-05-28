/**
 * Create Date: 2015年11月4日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.wechat.mp.token;

/**
 * 微信公众平台配置
 *
 * @author Francis
 * @version 1.0
 * @since 2015年11月4日
 */
public class WeChatConfig {
	
	
	/**
	 * 请求access_token的URL
	 */
	public static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
	
	/**
	 * 请求jsapi_ticket的URL
	 */
	public static final String URL_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
	
	

}
