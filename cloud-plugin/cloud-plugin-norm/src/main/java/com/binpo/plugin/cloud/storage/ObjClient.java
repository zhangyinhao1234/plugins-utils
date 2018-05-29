package com.binpo.plugin.cloud.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * simple Storage  Service 
 * 使用腾讯云注入 name=cos 使用 亚马逊的注入 name=s3client
 * @author zhang
 *
 */
public interface ObjClient {
	
	public enum ImageExt{
		jpg,png
	}
	/**
	 * 上传文件
	 * @param cloudPath 文件云服务器文件地址
	 * @param localPath 本地文件地址
	 * @return
	 */
	ObjectUploadResult uploadObject(String cloudPath,String localPath);
	/**
	 * 上传文件
	 * @param cloudPath 文件云服务器文件地址
	 * @param file 本地文件
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	ObjectUploadResult uploadObject(String cloudPath,File file) throws FileNotFoundException, IOException;
	/**
	 * 上传文件
	 * @param cloudPath 文件云服务器文件地址
	 * @param in 文件流
	 * @return
	 * @throws IOException 
	 */
	ObjectUploadResult uploadObject(String cloudPath,InputStream in) throws IOException;
	/**
	 * 上传图片（会创建小图和中等图片）<br/>
     * 指定了名称的使用指定名称，未指定名称的使用uuid作为名称<br/>
     * 例如<br/>
     *  cloudPath ： /dev/test.jpg 那么使用test.jpg作为文件名 <br/>
     *  cloudPath ： /dev/ 或者 /dev 那么使用uuid.extend 作为文件名 <br/>
	 * @param cloudPath 文件云服务器文件地址
	 * @param localPath 本地文件地址
	 * @param extend 图片后缀名
	 * @return
	 * @throws IOException 
	 */
	ObjectUploadResult uploadImage(String cloudPath,String localPath,String extend) throws IOException;
	/**
	 * 上传图片（会创建小图和中等图片）<br/>
     * 指定了名称的使用指定名称，未指定名称的使用uuid作为名称<br/>
     * 例如<br/>
     *  cloudPath ： /dev/test.jpg 那么使用test.jpg作为文件名 <br/>
     *  cloudPath ： /dev/ 或者 /dev 那么使用uuid.extend 作为文件名 <br/>
	 * @param cloudPath 文件云服务器文件地址
	 * @param file 本地地址
	 * @param extend 图片后缀名
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	ObjectUploadResult uploadImage(String cloudPath,File file,String extend) throws FileNotFoundException, IOException;
	/**
	 * 
     * 指定了名称的使用指定名称，未指定名称的使用uuid作为名称<br/>
     * 例如<br/>
     *  cloudPath ： /dev/test.jpg 那么使用test.jpg作为文件名 <br/>
     *  cloudPath ： /dev/ 或者 /dev 那么使用uuid.extend 作为文件名 <br/>
	 * @param cloudPath 文件云服务器文件地址
	 * @param fileBase64 图片的base64码
	 * @param extend 图片后缀名
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	ObjectUploadResult uploadBaes64Image(String cloudPath,String fileBase64,String extend) throws FileNotFoundException, IOException;
	/**
	 * 上传图片（会创建小图和中等图片）<br/>
	 * 指定了名称的使用指定名称，未指定名称的使用uuid作为名称<br/>
	 * 例如<br/>
	 *  cloudPath ： /dev/test.jpg 那么使用test.jpg作为文件名 <br/>
	 *  cloudPath ： /dev/ 或者 /dev 那么使用uuid.extend 作为文件名 <br/>
	 * @param cloudPath 文件云服务器文件地址
	 * @param in 文件流
	 * @param extend 图片后缀名
	 * @return
	 * @throws IOException 
	 */
	ObjectUploadResult uploadImage(String cloudPath,InputStream in,String extend) throws IOException;
	
	/**
	 * 获取对象的流文件
	 * @param cloudPath 文件云服务器文件地址
	 * @return
	 * @throws Exception 
	 */
	InputStream	getObject(String cloudPath) throws Exception;
	/**
	 * 获取文件信息
	 * @param cloudPath 文件云服务器文件地址
	 * @return
	 */
	ObjectInfo getObjectInfo(String cloudPath);
	/**
	 * 删除文件
	 * @param cloudPath 文件云服务器文件地址
	 */
	void deleteObject(String cloudPath);
	/**
	 * 关闭连接
	 */
	void close();
	
}
