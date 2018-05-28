package com.binpo.plugin.wechat.open;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.wechat.open.WeChatOpen.Scope;
import com.binpo.plugin.wechat.util.HttpUtil;
import com.binpo.plugin.wechat.util.PostUtil;

/**
 * 微信开放平台 网站 的授权登陆url
 * 
 * @author zhang
 *
 */
public class WeChatOpenWebUtil extends PostUtil {
	private static String wechat_web_login = "https://open.weixin.qq.com/connect/oauth2/authorize?";
	private static String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
	private static String refresh_token_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

	private static String jsaccessTokenUrl = "https://api.weixin.qq.com/sns/jscode2session?";

	private Log logger = LogFactory.getLog(WeChatOpenWebUtil.class);

	/**
	 * 获取授权登陆的 url
	 * 
	 * @param redirect_uri
	 * @param state
	 * @author zhang
	 * @date 2016年3月14日下午4:49:19
	 * @version 0.01
	 * @return String
	 */
	public static String getAuthorizeUrl(String redirect_uri, String state, String appId, Scope scope) {
		String authorizeUrl = wechat_web_login + "appid=" + appId + "&" + "redirect_uri=" + redirect_uri + ""
				+ "&response_type=code&scope=" + scope.toString() + "&state=" + state + "#wechat_redirect ";
		return authorizeUrl;
	}

	/**
	 * 
	 * 网页授权回调地址通过code获取信息。微信：token有效期是7200秒
	 * 
	 * @param code
	 *            回调域返回的code
	 * @param appId
	 * @param appSecret
	 * @return
	 * @throws Exception
	 */
	public static AccessToken getAccessToken(String code, String appId, String appSecret) throws Exception {
		Map params = new HashMap<>();
		params.put("appid", appId);
		params.put("secret", appSecret);
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		String response = send(params, accessTokenUrl);
		AccessToken accessToken = JSON.parseObject(response, AccessToken.class);
		return accessToken;
	}

	/**
	 * 
	 * 获取微信的Token信息，微信：token有效期是7200秒，openid(微信小程序JS中获取)
	 * 
	 * @param code
	 * @param appId
	 * @param appSecret
	 * @return
	 * @throws Exception
	 */
	public AccessToken getJSAccessToken(String code, String appId, String appSecret) throws Exception {
		// 微信获取oppenid的接口
		String openIdUrl = jsaccessTokenUrl;
		String url = String.format(openIdUrl + "appid=%s&secret=%s&js_code=%s&grant_type=%s", appId, appSecret, code,
				"authorization_code");
		String request = HttpUtil.request(url, "GET", null);
		logger.debug("getOpenidInfo:" + request);
		AccessToken accessToken = JSON.parseObject(request, AccessToken.class);
		return accessToken;
	}

	/**
	 * 
	 * 根据{@link #getAccessToken(String, String, String)} 返回的值获取用户的基础信息
	 * 
	 * @param accessToken
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public static UserInfo getUserInfo(String accessToken, String openid) throws Exception {
		Map params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("openid", openid);
		String response = send(params, userInfoUrl);
		String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
		UserInfo userinfo = JSON.parseObject(str, UserInfo.class);
		return userinfo;
	}

	/**
	 * 由于access_token拥有较短的有效期，当access_token超时后，<br>
	 * 可以使用refresh_token进行刷新，refresh_token拥有较长的有效期（7天、30天、60天、90天），<br>
	 * 当refresh_token失效的后，需要用户重新授权<br>
	 */
	public static AccessToken refreshToken(String accessToken, String appId) throws Exception {
		Map params = new HashMap<>();
		params.put("appid", appId);
		params.put("grant_type", "refresh_token");
		params.put("refresh_token", accessToken);
		String response = send(params, refresh_token_url);
		return JSON.parseObject(response, AccessToken.class);
	}

	/**
	 * 获取 state 值得key
	 * 
	 * @param sessionId
	 * @author zhang
	 * @date 2016年5月10日下午10:43:18
	 * @version 0.01
	 * @return String
	 */
	public static String getstateKey(String sessionId) {
		String key = "WeChat_state_" + sessionId;
		return key;
	}

	/**
	 * 获取用户信息的key
	 * 
	 * @param openid
	 * @author zhang
	 * @date 2016年5月10日下午10:43:43
	 * @version 0.01
	 * @return String
	 */
	public static String getopenIdUserInfoKey(String openid) {
		String key = "WeChat_openid_" + openid + "_userinfo";
		return key;
	}

	/**
	 * 
	 * 获取统一授权域的重定向地址
	 * 
	 * @param notifyUrl
	 *            微信公众平台配置的域名开头的回调地址
	 *            例如http://me.dishcat.com/example/wechat_redirec_center.json
	 * @param redirect_uri
	 *            重定向目标地址
	 * @param webParams
	 *            参数
	 * @return
	 */
	public static String createUniformRedirect_uri_2(String fullNotifyUrl, String redirect_uri,
			Map<String, String> webParams) {
		// 生产环境域名作为统一的授权服务地址
		String webPath = fullNotifyUrl;
		webParams.put("webUrl", redirect_uri);
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (Map.Entry<String, String> entity : webParams.entrySet()) {
			buf.append(entity.getKey()).append("@").append(entity.getValue());
			i++;
			if (i < webParams.size()) {
				buf.append(";");
			}
		}
		String url = webPath + "?webParams=" + buf.toString();
		return url;
	}

	/**
	 * 
	 * 获取统一授权域的重定向地址（地址utf8编码）
	 * 
	 * @param notifyUrl
	 *            微信公众平台配置的域名开头的回调地址
	 * @param redirect_uri
	 *            重定向到其他服务器链接的地址
	 * @param webParams
	 * @return
	 */
	public static String createUniformRedirect_EncodeUri_2(String fullNotifyUrl, String redirect_uri,
			Map<String, String> webParams) {
		String url = createUniformRedirect_uri_2(fullNotifyUrl, redirect_uri, webParams);
		String encode = "";
		try {
			encode = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encode;
	}

	/**
	 * 转换统一授权域重定向地址参数
	 * 
	 * @param webParams
	 * @author zhang
	 * @date 2016年5月10日下午10:34:49
	 * @version 0.01
	 * @return Map<String,String>
	 */
	public static Map<String, String> parseWebParamsStringToMap(String webParams) {
		Map<String, String> map = new HashMap<>();
		String[] key_value = webParams.split(";");
		for (String kv : key_value) {
			if (StringUtils.isBlank(kv))
				continue;
			String[] split = kv.split("@");
			if (split.length == 2) {
				map.put(split[0], split[1]);
			}
		}
		return map;
	}

	public static UserInfo parseObjectToUserInfo(Object obj) {
		if (obj == null)
			return new UserInfo();
		return (UserInfo) obj;
	}

	/**
	 * 应用服务器端的代码，用来解析微信授权进行重新的重定向
	 * @param state 我们定义的值
	 * @param code 微信服务器回传的code
	 * @param webParams 我们定义好的参数
	 * @return
	 */
	public static String getSendRedirectInWeb(String state,String code,String webParams) {
    	String getwebRedirUrl = getwebRedirUrl(webParams);
		if(!getwebRedirUrl.contains("http")){
		    getwebRedirUrl = "http://"+getwebRedirUrl;
		}
		return getwebRedirUrl+"?state="+state+"&code="+code+"&webParams="+webParams;
    }

	private static String getwebRedirUrl(String webParams) {
		String[] key_value = webParams.split(";");
		for (String kv : key_value) {
			if (StringUtils.isBlank(kv))
				continue;
			String[] split = kv.split("@");
			if (split[0].equals("webUrl")) {
				return split[1].toString();
			}
			if (split[0].equals("webRedirUrl")) {
				return split[1].toString();
			}
		}
		return null;
	}


}
