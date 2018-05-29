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
