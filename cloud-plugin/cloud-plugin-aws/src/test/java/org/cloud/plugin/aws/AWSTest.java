package org.cloud.plugin.aws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.binpo.plugin.cloud.queue.IMessageQueue;
import com.binpo.plugin.cloud.queue.Message;
import com.binpo.plugin.cloud.queue.MessageResult;
import com.binpo.plugin.cloud.storage.ObjClient;
import com.binpo.plugin.cloud.storage.ObjectUploadResult;
import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-*.xml")
//@ComponentScan(basePackages="com.binpo")
//@ImportResource(locations= {"classpath:application.properties"})
public class AWSTest {
	@Autowired
	private ObjClient objClient;
	
	
	@Resource(name="sellerBalance")
	private IMessageQueue queue;
	
	private Log logger = LogFactory.getLog(AWSTest.class);

	/**
	 * 上传文件到S3
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testUploadObj() throws FileNotFoundException, IOException {
		ObjectUploadResult uploadObject = 
				objClient.uploadObject("dev/temp/"+UUID.randomUUID()+".png", new File("C:\\Temp\\222222.png"));
		logger.debug(uploadObject.getUrl());
	}
	/**
	 * 
	 * 发送消息，配置文件在SQSConfig.java
	 *
	 */
	@Test
	public void pushMessage() {
	    Message message = new Message("hii");
	    MessageResult sendMessage = queue.sendMessage(message);
	    logger.debug(JSON.toJSONString(sendMessage));
	}
	/**
	 * 
	 * 读取消息 SQSConfig.java
	 *
	 */
	@Test
	public void readMessage() {
	    List<Message> receiveMessage = queue.receiveMessage(10);
	    logger.debug(JSON.toJSONString(receiveMessage));
	}
}
