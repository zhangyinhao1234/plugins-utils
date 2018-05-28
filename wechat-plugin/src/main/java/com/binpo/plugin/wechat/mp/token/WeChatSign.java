/**
 * Create Date: 2015年11月4日
 * Author: Francis
 * Copyright: www.cesgroup.com.cn
 */
package com.binpo.plugin.wechat.mp.token;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

import com.binpo.plugin.wechat.mp.token.bean.Sign;

/**
 * 微信签名
 *
 * @author Francis
 * @version 1.0
 * @since 2015年11月4日
 */
public class WeChatSign {
	
	/**
	 * 签名
	 * @param jsapiTicket
	 * @param url
	 * @param appId 
	 * @return
	 *
	 * @author Francis
	 * @since 2015年11月10日
	 */
	public static Sign sign(String jsapiTicket, String url,String appId) {
		Sign sign = null;
		String nonceStr = createNonceStr();
		String timestamp = createTimestamp();
		String str = null;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		str = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr
				+ "&timestamp=" + timestamp + "&url=" + url;

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		sign = new Sign();
		sign.setAppId(appId);
		sign.setTimestamp(timestamp);
		sign.setNonceStr(nonceStr);
		sign.setSignature(signature);
		sign.setUrl(url);
		sign.setJsapi_ticket(jsapiTicket);

		return sign;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String createNonceStr() {
		return UUID.randomUUID().toString();
	}

	private static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

}
