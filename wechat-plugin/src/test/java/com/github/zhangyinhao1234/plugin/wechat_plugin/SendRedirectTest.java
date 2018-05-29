package com.github.zhangyinhao1234.plugin.wechat_plugin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.wechat.open.AccessToken;
import com.binpo.plugin.wechat.open.WeChatOpen.Scope;
import com.binpo.plugin.wechat.open.WeChatOpenWebUtil;

public class SendRedirectTest {
	@Test
	public void testGetUrl() throws Exception {
		
		//获取从微信端打开的链接地址
		//需要重定向的地址
		String pagePath = "http://dev.taocaimall.com/taocaimall/superior/wechat/weChatShopIndex.htm";
		//中间服务器，用来处理我们拼接好的参数，在这个中间服务器上会重定向到
		String fullNotifyUrl = "http://u.taocai.mobi/taocaimall/wechat_redirec_center.json";
		String state = "1234";// 随机数
		String appId = "wx2f5db1c382f9ca7d";
		String sessionId = "adfsdkfsdkjnfjsdk";
		Map<String, String> webParams = new HashMap<>();
		String stateKey = WeChatOpenWebUtil.getstateKey(sessionId);
		webParams.put("stateKey", stateKey);
		String redirect_uri = WeChatOpenWebUtil
				.createUniformRedirect_EncodeUri_2(fullNotifyUrl,pagePath, webParams);
		String authorizeUrl = WeChatOpenWebUtil.getAuthorizeUrl(redirect_uri, state, appId,Scope.snsapi_base);
		System.out.println(authorizeUrl);// 微信重定向地址

		String code = "061cGRlN1lkTw51OFznN1C4XlN1cGRlJ";
		String appSecret = "";
		AccessToken accessToken = WeChatOpenWebUtil.getAccessToken(code, appId, appSecret);
		System.out.println(JSON.toJSONString(accessToken));

//		String url = WeChatOpenWebUtil.getSendRedirectInWeb(stateKey, code, webParams);
	}
}
