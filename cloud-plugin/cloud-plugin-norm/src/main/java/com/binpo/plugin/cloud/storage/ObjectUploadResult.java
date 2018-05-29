package com.binpo.plugin.cloud.storage;

import java.io.Serializable;

public class ObjectUploadResult implements Serializable{
	/**
	 * 资源文件url(真实路径)
	 */
	private String url;
	/**
	 * 处理的返回结果
	 */
	private String resultStr;
	
	/**
	 * 小图绝对路径（上传图片才有值）{@link #url}拼接上_small.jpg
	 */
	private String smallImagePath;
	/**
	 * 中等图片绝对路径（上传图片才有值）{@link #url}拼接上_middle.jpg
	 */
	private String middleImagePath;
	
	/**
	 * 对象存储桶名
	 */
	private String bucketName;
	/**
	 * 相对路径(key)
	 */
	private String cloudPath;
	
	/**
	 * 图片的宽（上传图片才有值）
	 */
	private Integer width;
	/**
	 * 图片的高（上传图片才有值）
	 */
	private Integer height;
	
	public ObjectUploadResult(){}
	
	public String getExt(){
	    int lastIndexOf = this.cloudPath.lastIndexOf(".");
	    String ext = this.cloudPath.substring(lastIndexOf+1);
	    return ext;
	}
	/**
	 * 
	 * 因为以前的数据库结构不得不提供这个方法<br/>
	 * 例如：ba98850d-a4b4-44b8-acfa-ea4461b15261.jpg<br/>
	 * 建议直接保存{@link #url}到数据库
	 * @return
	 */
	@Deprecated
	public String getFileName(){
	    int lastIndexOf = this.cloudPath.lastIndexOf("/");
        return this.cloudPath.substring(lastIndexOf+1);
	}
	/**
     * 
     * 因为以前的数据库结构不得不提供这个方法<br/>
     * 例如：ba98850d-a4b4-44b8-acfa-ea4461b15261.jpg<br/>
     * 建议直接保存{@link #url}到数据库
     * @return
     */
	@Deprecated
	public String getName(){
	    return this.getFileName();
	}
	/**
     * 
     * 因为以前的数据库结构不得不提供这个方法<br/>
     * 例如：img/upload/test/2016/03/22<br/>
     * 返回：upload/test/2016/03/22
     * 建议直接保存{@link #url}到数据库
     * @return
     */
	@Deprecated
	public String getPath(){
	    int lastIndexOf = this.cloudPath.lastIndexOf("/");
        String substring = this.cloudPath.substring(0, lastIndexOf);
        if(substring.startsWith("img/")){
            String substring2 = substring.substring(4);
            return substring2;
        }
        return substring;
	}
	
	public String getRealPath(){
        int lastIndexOf = this.cloudPath.lastIndexOf("/");
        return this.cloudPath.substring(0, lastIndexOf);
    }
	
	public ObjectUploadResult(String url,String bucketName,String cloudPath){
		this.url = url;
		this.bucketName = bucketName;
		this.cloudPath = cloudPath;
	}
	public String getResultStr() {
		return resultStr;
	}
	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSmallImagePath() {
		return smallImagePath;
	}
	public void setSmallImagePath(String smallImagePath) {
		this.smallImagePath = smallImagePath;
	}
	public String getMiddleImagePath() {
		return middleImagePath;
	}
	public void setMiddleImagePath(String middleImagePath) {
		this.middleImagePath = middleImagePath;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getCloudPath() {
		return cloudPath;
	}
	public void setCloudPath(String cloudPath) {
		this.cloudPath = cloudPath;
	}
	
	
}
