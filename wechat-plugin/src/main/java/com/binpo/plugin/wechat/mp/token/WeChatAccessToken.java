/**
 * Create Date: 2015年11月5日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.wechat.mp.token;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.binpo.plugin.wechat.mp.token.bean.AccessToken;
import com.binpo.plugin.wechat.util.HttpUtil;

/**
 * 
 * 微信AccessToken
 *
 * @author zhang 2017年7月14日 上午11:12:10
 */
public class WeChatAccessToken {
    
    private static Log logger = LogFactory.getLog(WeChatAccessToken.class);
    /**
     * 缓存中jsapi_ticket的key
     */
    private static final String CACHE_KEY_JSAPI_TICKET = "wechat_jsapi_ticket_";
    
    /**
     * 缓存中accessToken的key
     */
    private static final String CACHE_KEY_ACCESS_TOKEN = "wechat_access_token_";
	/**
	 * 
	 * 取得AccessToken，有效期为2个小时
	 * 
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	public static AccessToken getAccessTokenData(String appId,String appSecret){
		String url = String.format(WeChatConfig.URL_ACCESS_TOKEN + "&appid=%s&secret=%s", appId, appSecret);
		AccessToken accessToken = HttpUtil.getJsonByRequestWithGet(url, null, AccessToken.class);
		logger.debug("token:"+accessToken.getAccess_token()+";expires_in:"+accessToken.getExpires_in()+";errorinfo:"+accessToken.getErrmsg());
		return accessToken;
	}
	/**
	 * 
	 * 获取accessToken的缓存数据key
	 * 
	 * @param appId
	 * @return
	 */
    public static String getAccessTokenCacheKey(String appId) {
        return CACHE_KEY_ACCESS_TOKEN + appId;
    }

    /**
     * 
     * 获取jsTicket的缓存数据key
     * 
     * @param appId
     * @return
     */
    public static String getJSapiTicketCacheKey(String appId) {
        return CACHE_KEY_JSAPI_TICKET + appId;
    }
	
	public static void main(String[] a){
	    getAccessTokenData("", "");
	}
	

}
