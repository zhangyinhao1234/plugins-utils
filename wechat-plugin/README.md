# 插件使用说明

## wechat-plugin

​	对微信公众号中的授权进行处理，可以生成针对统一域名的授权转发，从重定向的地址中获取的用户信息，微信会传递一个code回来，通过code获取accessToken，来获取用户的信息。

​	微信公众号的模板消息发送，提供openid，模板id，appid，和appSecret，入参调用方法即可发送模板消息。



### 代码示例

#### 发送模板消息

		// 密钥
		String appSecret = "";
		// 公众号号的appid
		String appId = "";
		//token有效期2小时  可以缓存下来
		AccessToken accessTokenData = WeChatAccessToken.getAccessTokenData(appId, appSecret);
		String accessToken = accessTokenData.getAccess_token();
		// 用户的openid
		String touser = "o4TrB1dt1bevRzycmKI5kehDDlfk";
		String templateId = "V8LeGNNfmLibj2OT-mBGyUEXY8ylSm6EifOyGRDiWpw";
		TMessage msg = new TMessage(touser, templateId);
		msg.addParams("first", "优惠券提醒","keyword1", "商品优惠券","keyword2", "优惠券2018-05-09","remark", "优惠券remark");
		WeChatTMessage.sendMessage(msg, accessToken);


#### 获取需要获取用户信息的微信连接地址

		//获取从微信端打开的链接地址
		//需要重定向的地址
		String pagePath = "http://dev.taocaimall.com/taocaimall/superior/wechat/weChatShopIndex.htm";
		//中间服务器，用来处理我们拼接好的参数，在这个中间服务器上会重定向到
		String fullNotifyUrl = "http://u.taocai.mobi/taocaimall/wechat_redirec_center.json";
		String state = "1234";// 随机数
		String appId = "";
		String sessionId = "adfsdkfsdkjnfjsdk";
		Map<String, String> webParams = new HashMap<>();
		String stateKey = WeChatOpenWebUtil.getstateKey(sessionId);
		webParams.put("stateKey", stateKey);
		String redirect_uri = WeChatOpenWebUtil
				.createUniformRedirect_EncodeUri_2(fullNotifyUrl,pagePath, webParams);
		String authorizeUrl = WeChatOpenWebUtil.getAuthorizeUrl(redirect_uri, state, appId,Scope.snsapi_base);
		System.out.println(authorizeUrl);// 微信重定向地址
授权地址：

https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2f5db1c382f9ca7d&redirect_uri=http%3A%2F%2Fu.taocai.mobi%2Ftaocaimall%2Fwechat_redirec_center.json%3FwebParams%3DwebUrl%40http%3A%2F%2Fdev.taocaimall.com%2Ftaocaimall%2Fsuperior%2Fwechat%2FweChatShopIndex.htm%3BstateKey%40WeChat_state_adfsdkfsdkjnfjsdk&response_type=code&scope=snsapi_base&state=1234#wechat_redirect 

其实是微信重定向到：u.taocai.mobi 的接口，通过 wechat_redirec_center.json 接口再重定向到 pagePath 

在 http://dev.taocaimall.com/taocaimall/superior/wechat/weChatShopIndex.htm 地址中可以通过code获取微信用户的openid等信息

		String code = "061cGRlN1lkTw51OFznN1C4XlN1cGRlJ";
		String appSecret = "";
		AccessToken accessToken = WeChatOpenWebUtil.getAccessToken(code, appId, appSecret);
		System.out.println(JSON.toJSONString(accessToken));
服务器端 wechat_redirec_center.json 接口的代码

	@RequestMapping({ "/wechat_redirec_center" })
	public void redirectWebUrl(String state,String code,String webParams) throws IOException{
		String url = WeChatOpenWebUtil.getSendRedirectInWeb(stateKey, code, webParams);
		response.sendRedirect(url);
	}
