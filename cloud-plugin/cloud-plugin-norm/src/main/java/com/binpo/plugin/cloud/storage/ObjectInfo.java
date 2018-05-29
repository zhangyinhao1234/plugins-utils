package com.binpo.plugin.cloud.storage;

import java.io.Serializable;

/**
 * 对象信息
 * @author zhang
 *
 */
public class ObjectInfo implements Serializable{
	
	
	/**
	 * 对象存储桶名
	 */
	private String bucketName;
	/**
	 * 相对路径
	 */
	private String cloudPath;
	/**
	 * 文件大小
	 */
	private int fileSize;
	/**
	 * 文件地址
	 */
	private String source_url;
	
	public ObjectInfo(){
		
	}
	public ObjectInfo(String bucketName,String source_url){
		this.bucketName = bucketName;
		this.source_url = source_url;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getSource_url() {
		return source_url;
	}
	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}
	public String getCloudPath() {
		return cloudPath;
	}
	public void setCloudPath(String cloudPath) {
		this.cloudPath = cloudPath;
	}
	
	
}
