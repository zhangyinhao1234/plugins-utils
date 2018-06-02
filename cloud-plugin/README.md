# 插件使用说明

## cloud-plugin

​	对AWS和Qcloud的消息队列，订阅通知，文件上传设置了统一的接口，分别对定义的接口进行了实现，方便在项目中直接使用文件上传，消息发送订阅。可以在一个项目中定义多个文件上传的桶，消息队列，可以同时使用AWS和Qcloud的服务，通过接口的方式，方便日后进行实现的替换（例如从AWS迁移到腾讯云）



### 配置文件

	#aws key AWS内容
	aws.default.bucketName=桶名称
	aws.key.secretId=密钥id
	aws.key.secretKey=密钥key
	
	
	#qcloud key 腾讯云的配置内容
	qcloud.key.secretId=密钥id
	qcloud.key.secretKey=密钥key
	qcloud.default.region=使用的可用区。sh/gz
	qcloud.default.bucketName=存储桶
	qcloud.default.appId=appid
以上配置会初始化ObjClient的实现，可以注入ObjClient 进行文件的上传

### 配置文件（添加消息队列的配置）


	AWS的消息队列配置
	
	@Component
	@Configurable
	public class SQSConfig {
	
	    @Value("${aws.key.secretId}")
	    private String secretId;
	
	    @Value("${aws.key.secretKey}")
	    private String secretKey;
	
	    @Bean("sellerBalance")
	    public IMessageQueue sqsClient(ISecretKey secretKey) {
	        String queueName = "seller_balance";
	        String queueUrl = "https://sqs.cn-north-1.amazonaws.com.cn/xxxx/seller_balance";
	        return new AWSQueueClient(secretKey, queueName, queueUrl);
	    }
	
	    @Bean(name="sellerBalanceNotification")
	    public ITopic snsTopic(ISecretKey secretKey) {
	        String topicName = "arn:aws-cn:sns:cn-north-1:xxxx:seller_balance";
	        return new AWSTopicClient(secretKey,topicName);
	    }
	
	}
	
	腾讯云的消息队列配置
	@Component
	@Configurable
	public class CMQConfig {
	    @Bean("testqueue")
	    public IMessageQueue testqueue(ISecretKey secretKey) {
	        String queueName = "sellerBalanceDev";
	        return new CMQQueueClient(QCloudRegion.sh, queueName,QcloudHttpType.https, secretKey);
	    }
	}
	
### 代码示例

#### 上传文件/发送消息

		@Autowired
		private ObjClient objClient;
		
		@Resource(name="sellerBalance")
		private IMessageQueue queue;
		
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
	

