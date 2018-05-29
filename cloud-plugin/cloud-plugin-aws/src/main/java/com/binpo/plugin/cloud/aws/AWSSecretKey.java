package com.binpo.plugin.cloud.aws;

import com.binpo.plugin.cloud.ISecretKey;

public class AWSSecretKey implements ISecretKey{
	private String secretId = "";
	private String secretKey = "";
	
	public AWSSecretKey(String secretId,String secretKey) {
	    this.secretId = secretId;
	    this.secretKey = secretKey;
	}
	@Override
	public String getSecretId() {
		return secretId;
	}

	@Override
	public String getSecretKey() {
		return secretKey;
	}

}
