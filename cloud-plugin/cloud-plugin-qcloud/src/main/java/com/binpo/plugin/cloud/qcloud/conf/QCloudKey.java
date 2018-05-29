package com.binpo.plugin.cloud.qcloud.conf;

import com.binpo.plugin.cloud.ISecretKey;

public class QCloudKey implements ISecretKey {

	private String secretId;

	private String secretKey;

	public QCloudKey(String secretId, String secretKey) {
		this.secretId = secretId;
		this.secretKey = secretKey;
	}

	@Override
	public String getSecretId() {
		return this.secretId;
	}

	@Override
	public String getSecretKey() {
		return this.secretKey;
	}

}
