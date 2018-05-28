package com.binpo.plugin.wechat.mp.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.wechat.mp.template.bean.TMessage;
import com.binpo.plugin.wechat.mp.template.bean.TMessageResult;
import com.binpo.plugin.wechat.mp.template.bean.TValue;
import com.binpo.plugin.wechat.mp.token.WeChatAccessToken;
import com.binpo.plugin.wechat.util.PostUtil;

/**
 * 
 * 微信模板消息发送
 *
 * @author zhang 2017年7月14日 上午11:35:20
 */
public class WeChatTMessage {

	private static Log logger = LogFactory.getLog(WeChatTMessage.class);
	private final static String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

	/**
	 * 
	 * 发送微信模板消息
	 * 
	 * @param message
	 *            发送内容
	 * @param accessToken
	 *            {@link WeChatAccessToken#getAccessTokenData(String, String)}
	 */
	public static TMessageResult sendMessage(TMessage message, String accessToken) {
		String requestUrl = url + accessToken;
		String jsonString = JSON.toJSONString(message);

		String postJson = "";
		try {
			postJson = PostUtil.postJson(requestUrl, jsonString);
		} catch (Exception e) {
			logger.error(e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("微信模板消息发送内容：" + JSON.toJSONString(message));
			logger.debug("微信模板消息发送结果" + postJson);
		}
		if (!"".equals(postJson)) {
			JSON.parseObject(postJson, TMessageResult.class);
		}
		return new TMessageResult();
	}

}
