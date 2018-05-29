package com.github.zhangyinhao1234.plugin.wechat_plugin;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.wechat.open.AccessToken;
import com.binpo.plugin.wechat.open.WeChatOpen.Scope;
import com.binpo.plugin.wechat.open.WeChatOpenWebUtil;

public class SendRedirectTest {
	public void testGetUrl() throws Exception {
		String webPath = "http://dev.taocaimall.com/taocaimall";
		//中间服务器，用来处理我们拼接好的参数，在这个中间服务器上会重定向到
		String fullNotifyUrl = "http://u.taocai.mobi/taocaimall/wechat_redirec_center.json";
		String state = "1234";// 随机数
		String appId = "";
		String sessionId = "adfsdkfsdkjnfjsdk";
		Map<String, String> webParams = new HashMap<>();
		String stateKey = WeChatOpenWebUtil.getstateKey(sessionId);
		webParams.put("stateKey", stateKey);
		String redirect_uri = WeChatOpenWebUtil
				.createUniformRedirect_EncodeUri_2("",webPath + "/iphoneSEActivityIndex.htm", webParams);
		String authorizeUrl = WeChatOpenWebUtil.getAuthorizeUrl(redirect_uri, state, appId,Scope.snsapi_base);
		System.out.println(authorizeUrl);// 微信重定向地址

//		String code = "061cGRlN1lkTw51OFznN1C4XlN1cGRlJ";
//		// appId = "";
//		String appSecret = "";
//		AccessToken accessToken = WeChatOpenWebUtil.getAccessToken(code, appId, appSecret);
//		System.out.println(JSON.toJSONString(accessToken));

	}
}
