# 插件使用说明

## hibernate-query-plugin

### 组件描述

打包：mvn clean install 

封装了hibernate的基础的查询，保存，更新，分页查询。基础配置如下：

`spring.datasource.type=com.alibaba.druid.pool.DruidDataSource`
`spring.datasource.url=jdbc:mysql://127.0.0.1:3306/dump20161027?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8`
`spring.datasource.username= root`
`spring.datasource.password= 123456`
`spring.datasource.driverClassName = com.mysql.jdbc.Driver`
`spring.datasource.maxActive=20`
`spring.datasource.readurl=jdbc:mysql://127.0.0.1:3306/dump20161027?useUnicode=true&amp;characterEncoding=utf-8 #只读地址`
`hibernate`
`spring.jpa.properties.hibernate.dialect=com.binpo.plugin.hibernate.query.page.generic.SystemMySQL5Dialect`
`spring.jpa.properties.hibernate.show_sql=true`
`spring.jpa.properties.hibernate.format_sql=true`
`spring.jpa.properties.hibernate.cache.use_query_cache=false`
`spring.jpa.properties.hibernate.cache.use_second_level_cache=false`
`spring.jpa.properties.hibernate.hbm2ddl.auto=update`
`spring.jpa.properties.hibernate.memcached.servers=127.0.0.1:11211 #memcached二级缓存地址` 
`spring.jpa.properties.hibernate.memcached.cacheTimeSeconds=1200`

`spring.jpa.properties.hibernate.cache.memcached.version=1.6`
`spring.redis.hosts=node1:127.0.0.1:6379`
`spring.idworker.strategy= #id策略配置(可实现 EntityIdWorker 接口配置自定义的id生成规则，默认的为 snowflake)` 
`spring.jpa.properties.hibernate.pojoScan=com.binpo.plugin.hibernate.pojo,com.binpo.other.pojo`

如果需要使用[hibernate-memcached](https://github.com/zhangyinhao1234/hibernate-memcached) 实现hibernate的二级缓存，需要对hibernate-memcached进行打包，实现二级缓存。

### 使用举例

在test目录中有详细的单元测试的例子，包括普通的查询，保存，分页查询。

#### 基础接口

`boolean save(T obj);`

`T getObjById(Long id);`

`IPageList list(IPageQuery page);`

`IPageList listNoCache(IQueryObject queryObject);`

`boolean update(T obj);`

`List query(String nnq, Map params, int begin, int max);`

`T getObjByProperty(String propertyName, Object value);`

`int batchUpdate(String hql, Map params);`

`Object queryUnique(String hql, Map params);`

`List executeNativeQuery(String nnq, Map params, int begin, int max);`

`List<Map<String, Object>> queryForList(String sql, Map<String, ?> params);`

#### 示例代码

	public void testQuery() {
		String hql = "select obj from BIExample obj where obj.name=:name ";
		Map params = new HashMap<>();
		params.put("name", "zhang");
		List query = this.iBIExampleService.query(hql, params, 0, -1);
		logger.debug(JSON.toJSONString(query));
		
		hql = "select obj from BIExample obj where obj.name like :name ";
		params = new HashMap<>();
		params.put("name", "%"+"z"+"%");
		query = this.iBIExampleService.query(hql, params, 0, -1);
		logger.debug(JSON.toJSONString(query));
		
		String sql="select * from binpo_bi_example where name=:name";
		params.put("name", "zhang");
		List queryForList = this.iBIExampleService.queryForList(sql, params);
		logger.debug(queryForList);
	}

	public void testListPage() {
		BaseQueryObject qb = new BaseQueryObject("1");
		Map params = new HashMap<>();
		params.put("name", "zhang");
		qb.addQuery("obj.name=:name", params);
		qb.addQuery("obj.name", new SysMap("name","zhang"), "=");
		qb.addQuery("obj.name", new SysMap("name","%"+"zhang"+"%"), "like");
		IPageList list = this.iBIExampleService.list(qb);
		logger.debug(JSON.toJSONString(list));
	}
	public void testListPage2() {
		PageQuery pageQuery = new PageQuery("select * from binpo_bi_example where 1=1 ","1");
		pageQuery.addQuery("and 1=1 ");
		pageQuery.addQuery("and name=:name", new SysMap("name", "zhang"));
		IPageList list = this.iBIExampleService.list(pageQuery);
		logger.debug(JSON.toJSONString(list));
	}
#### 在spring boot中使用

[hibernate-in-springboot](https://github.com/zhangyinhao1234/plugins-utils/tree/master/hibernate-query-plugin/hibernate-in-springboot)