package com.github.zhangyinhao1234.plugin.wechat_plugin;

import java.util.HashMap;
import java.util.Map;

import com.binpo.plugin.wechat.mp.template.WeChatTMessage;
import com.binpo.plugin.wechat.mp.template.bean.TMessage;
import com.binpo.plugin.wechat.mp.template.bean.TValue;
import com.binpo.plugin.wechat.mp.token.WeChatAccessToken;
import com.binpo.plugin.wechat.mp.token.bean.AccessToken;

public class TemplateMsgTest {

	public static void main(String[] a) {
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
		msg.addParams("first", "优惠券提醒");
		msg.addParams("keyword1", "商品优惠券");
		msg.addParams("keyword2", "优惠券2018-05-09");
		msg.addParams("remark", "优惠券remark");
		WeChatTMessage.sendMessage(msg, accessToken);
	}
}
