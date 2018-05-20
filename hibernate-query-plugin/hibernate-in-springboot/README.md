# hibernate-query-plugin在spring boot的使用

将[hibernate-query-plugin](https://github.com/zhangyinhao1234/plugins-utils/tree/master/hibernate-query-plugin/hibernate-query-plugin)进行打包mvn clean install ，有私有仓库的可deploy到私有仓库。

		<dependency>
			<groupId>com.github.zhangyinhao1234.plugin</groupId>
			<artifactId>hibernate-query-plugin</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
引用依赖的文件，简单的添加service

public interface IBIExampleService extends IBaseService<Example> {

}

@Service
public class BIExampleServiceImpl extends BaseServiceImpl<Example> implements IBIExampleService {

}