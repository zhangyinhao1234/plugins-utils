package com.binpo.plugin.cloud.qcloud.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;
public class BspAPI {
	
	public enum QcloudHttpType{
		http,https
	}

	/**
	 * 编码
	 * 
	 * @param bstr
	 * @return String
	 */
	private static String encode(byte[] bstr) {
		String s = System.getProperty("line.separator");
		// return Base64.getEncoder().encodeToString(bstr).replaceAll(s, ""); //
		// JDK 1.8 可使用
		// return Base64.encodeBase64String(bstr).replaceAll(s, ""); // JDK
		// 1.7及以下使用第三方Jar包 commons-codec
		return new BASE64Encoder().encode(bstr).replaceAll(s, "");
	}

	/* Signature algorithm using HMAC-SHA1 */
	public static String hmacSHA1(String key, String text)
			throws InvalidKeyException, NoSuchAlgorithmException {
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA1"));
		return encode(mac.doFinal(text.getBytes()));
	}

	/* Assemble query string */
	private static String makeQueryString(Map<String, String> args,
			String charset) throws UnsupportedEncodingException {
		String url = "";

		for (Map.Entry<String, String> entry : args.entrySet())
			url += entry.getKey()
					+ "="
					+ (charset == null ? entry.getValue() : URLEncoder.encode(
							entry.getValue(), charset)) + "&";

		return url.substring(0, url.length() - 1);
	}

	/* Generates an available URL */
	public static String makeURL(String method, String action, String region,
			String secretId, String secretKey, Map<String, String> args,
			String charset,QcloudHttpType httpType,String URL) throws InvalidKeyException,
			NoSuchAlgorithmException, UnsupportedEncodingException {
		SortedMap<String, String> arguments = new TreeMap<String, String>();

		/* Sort all parameters, then calculate signature */
		arguments.putAll(args);
		arguments.put("Nonce",
				String.valueOf((int) (Math.random() * 0x7fffffff)));
		arguments.put("Action", action);
		arguments.put("Region", region);
		arguments.put("SecretId", secretId);
		arguments.put("Timestamp",
				String.valueOf(System.currentTimeMillis() / 1000));
		arguments.put(
				"Signature",
				hmacSHA1(
						secretKey,
						String.format("%s%s?%s", method, URL,
								makeQueryString(arguments, null))));

		/* Assemble final request URL */
		return String.format(httpType.toString()+"://%s?%s", URL,
				makeQueryString(arguments, charset));
	}

	/* Message-Struct constructor for API `UgcAntiSpam` */
	public abstract static class Message {
		/* Basic TLV structure */
		public abstract static class TLV {
			/* The "T" and "V" part of "TLV" */
			private final int type;
			private final String value;

			public TLV(int type) {
				this(type, "");
			}

			public TLV(int type, String value) {
				this.type = type;
				this.value = value;
			}

			/* Assemble a TLV element */
			public byte[] assemble() {
				byte[] bytes = value.getBytes();
				byte[] result = new byte[bytes.length + 8];

				/* Type */
				result[0] = (byte) ((type & 0xff000000) >>> 24);
				result[1] = (byte) ((type & 0x00ff0000) >>> 16);
				result[2] = (byte) ((type & 0x0000ff00) >>> 8);
				result[3] = (byte) ((type & 0x000000ff) >>> 0);

				/* Length */
				result[4] = (byte) ((bytes.length & 0xff000000) >>> 24);
				result[5] = (byte) ((bytes.length & 0x00ff0000) >>> 16);
				result[6] = (byte) ((bytes.length & 0x0000ff00) >>> 8);
				result[7] = (byte) ((bytes.length & 0x000000ff) >>> 0);

				/* Value */
				System.arraycopy(bytes, 0, result, 8, bytes.length);
				return result;
			}
		}

		/* Message items of the Message-Struct */
		public static class Content extends TLV {
			public Content() {
				super(1);
			}

			public Content(String value) {
				super(1, value);
			}
		}

		public static class ImageURL extends TLV {
			public ImageURL() {
				super(2);
			}

			public ImageURL(String value) {
				super(2, value);
			}
		}

		public static class VideoURL extends TLV {
			public VideoURL() {
				super(3);
			}

			public VideoURL(String value) {
				super(3, value);
			}
		}

		public static class AudioURL extends TLV {
			public AudioURL() {
				super(5);
			}

			public AudioURL(String value) {
				super(4, value);
			}
		}

		public static class WebsiteURL extends TLV {
			public WebsiteURL() {
				super(5);
			}

			public WebsiteURL(String value) {
				super(5, value);
			}
		}

		public static class Emoticon extends TLV {
			public Emoticon() {
				super(6);
			}

			public Emoticon(String value) {
				super(6, value);
			}
		}

		public static class Title extends TLV {
			public Title() {
				super(7);
			}

			public Title(String value) {
				super(7, value);
			}
		}

		public static class Location extends TLV {
			public Location() {
				super(8);
			}
		}

		public static class Custom extends TLV {
			public Custom() {
				super(9);
			}
		}

		public static class File extends TLV {
			public File() {
				super(10);
			}
		}

		public static class Other extends TLV {
			public Other() {
				super(1000);
			}
		}

		/* Factory method of Message-Struct */
		public static String build(TLV... items) {
			int length = 0;
			ArrayList<byte[]> parts = new ArrayList<byte[]>();

			/* Assemble each part */
			for (TLV item : items) {
				byte[] part = item.assemble();

				parts.add(part);
				length += part.length;
			}

			int current = 0;
			byte[] result = new byte[length];

			/* Copy to result */
			for (byte[] part : parts) {
				System.arraycopy(part, 0, result, current, part.length);
				current += part.length;
			}

			/* Encode using Base64 */
			return encode(result);
		}
	}

}
