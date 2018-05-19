package com.binpo.plugin.hibernate.query.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.alibaba.druid.pool.DruidDataSource;
import com.binpo.plugin.hibernate.query.JdbcDao;
import com.binpo.plugin.hibernate.query.page.generic.GenericEntityDao;
import com.binpo.plugin.hibernate.query.page.generic.GenericEntityDaoSlave;

/**
 * <p>
 * 配置文件classpath:tcm.properties 配置数据库地址，memcached的二级缓存地址底层查询的dao<blockquote>
 * 
 * <pre>
 * # 数据库访问配置
 * # 主数据源，默认的
 * spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
 * spring.datasource.url=jdbc:mysql://127.0.0.1:3306/dump20161027?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8
 * spring.datasource.username= root
 * spring.datasource.password= 123456
 * spring.datasource.driverClassName = com.mysql.jdbc.Driver
 * spring.datasource.maxActive=20
 * #只读库地址
 * spring.datasource.readurl=jdbc:mysql://127.0.0.1:3306/dump20161027?useUnicode=true&amp;characterEncoding=utf-8
 * #hibernate
 * #spring.jpa.properties.hibernate.dialect=com.tcm.framework.core.dao.query.page.generic.SystemMySQL5Dialect
 * spring.jpa.properties.hibernate.show_sql=true
 * spring.jpa.properties.hibernate.format_sql=true
 * spring.jpa.properties.hibernate.cache.use_query_cache=true
 * spring.jpa.properties.hibernate.cache.use_second_level_cache=true
 * spring.jpa.properties.hibernate.hbm2ddl.auto=update
 * spring.jpa.properties.hibernate.memcached.servers=127.0.0.1:11211
 * spring.jpa.properties.hibernate.memcached.cacheTimeSeconds=1200
 * </pre>
 * 
 * </blockquote>
 * 
 * @author zhang
 *
 */
@Configuration
//@PropertySource(value = "classpath:tcm.properties")
public class GenericEntityDaoConfig {
    /**
     * 数据库地址
     */
    @Value("${spring.datasource.url}")
    private String url;
    /**
     * 数据库账号
     */
    @Value("${spring.datasource.username}")
    private String username;
    /**
     * 数据库密码
     */
    @Value("${spring.datasource.password}")
    private String password;
    /**
     * 使用的数据库驱动
     */
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    /**
     * 最大活动数量
     */
    @Value("${spring.datasource.maxActive:10}")
    private String maxActive;
    /**
     * 只读库地址
     */
    @Value("${spring.datasource.readurl}")
    private String readurl;
    /**
     * boolean false str
     */
    private String booleanFalseStr = "false";
    
    /**
     * hibernate映射包
     */
    @Value("${spring.jpa.properties.hibernate.pojoScan:}")
    private String[] pojoScan;

    /**
     * 初始化主数据连接实例
     * 
     * @return
     * @throws SQLException
     */
    @Bean
    @Primary
    public DruidDataSource tcmDataSource() throws SQLException {
        return createDataSource(url);
    }

    /**
     * 实例化只读库数据连接
     * 
     * @return
     * @throws SQLException
     */
    @Bean
    public DruidDataSource tcmDataSourceSlave() throws SQLException {
        return createDataSource(readurl);
    }

    /**
     * 
     * 创建数据库连接对象
     * 
     * @param realurl 数据库连接地址
     * @return
     * @throws SQLException
     */
    private DruidDataSource createDataSource(String realurl) throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(driverClassName);
        datasource.setUrl(realurl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setFilters("stat");
        datasource.setMaxActive(Integer.valueOf(maxActive));
        return datasource;
    }

    /**
     * hibernate方言
     */
    @Value("${spring.jpa.properties.hibernate.dialect:com.tcm.framework.core.dao.query.page.generic.SystemMySQL5Dialect}")
    private String hibernate_dialect;
    /**
     * 是否使用查询缓存
     */
    @Value("${spring.jpa.properties.hibernate.cache.use_query_cache:false}")
    private String use_query_cache;
    /**
     * 是否使用二级缓存
     */
    @Value("${spring.jpa.properties.hibernate.cache.use_second_level_cache:false}")
    private String use_second_level_cache;
    /**
     * 数据库操作类型
     */
    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto:update}")
    private String hbm2ddl_auto;
    /**
     * memcached 地址
     */
    @Value("${spring.jpa.properties.hibernate.memcached.servers}")
    private String memcached_servers;
    /**
     * 缓存失效时间
     */
    @Value("${spring.jpa.properties.hibernate.memcached.cacheTimeSeconds:1200}")
    private String cacheTimeSeconds;
    /**
     * 是否显示sql
     */
    @Value("${spring.jpa.properties.hibernate.show_sql:false}")
    private String showsql;
    /**
     * 是否格式化sql
     */
    @Value("${spring.jpa.properties.hibernate.format_sql:false}")
    private String format_sql;

    /**
     * 实例化主EntityManagerFactor
     * 
     * @return
     * @throws SQLException
     */
    @Bean("entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean tcmEntityManagerFactory() throws SQLException {
        return createEntityManagerFactory(tcmDataSource(), "masterData",hbm2ddl_auto);
    }

    /**
     * 实例化只读库的EntityManagerFactor
     * 
     * @return
     * @throws SQLException
     */
    @Bean(name = "tcmEntityManagerFactorySlave")
    public LocalContainerEntityManagerFactoryBean tcmEntityManagerFactorySlave() throws SQLException {
        return createEntityManagerFactory(tcmDataSourceSlave(), "slaveData","false");
    }

    /**
     * 
     * 创建EntityManagerFactory
     * 
     * @param datasource 数据库 连接实例
     * @param unitName unitName
     * @return
     * @throws SQLException
     */
    private LocalContainerEntityManagerFactoryBean createEntityManagerFactory(DruidDataSource datasource,
            String unitName,String hbm2ddlAuto ) throws SQLException {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        // org.hibernate.ejb.util.ConfigurationHelper.overrideProperties(Properties
        // properties, Map overrides)会载入配置 要求 key 和value 是String
        entityManagerFactory.setDataSource(datasource);
        entityManagerFactory.setPersistenceUnitName(unitName);
        entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter());
        if (pojoScan != null) {
            entityManagerFactory.setPackagesToScan(pojoScan);
        }
        Map<String, Object> jpaProperties = new HashMap<String, Object>();
        jpaProperties.put("hibernate.dialect", hibernate_dialect);
        jpaProperties.put("hibernate.cache.use_query_cache", use_query_cache);
        jpaProperties.put("hibernate.cache.use_second_level_cache", use_second_level_cache);
        if (Boolean.valueOf(use_second_level_cache)) {
            jpaProperties.put("hibernate.cache.region_prefix", "tcm.cache.memcached");
            jpaProperties.put("hibernate.cache.use_structured_entries", booleanFalseStr);
            jpaProperties.put("hibernate.cache.operationTimeout", "5000");
            jpaProperties.put("hibernate.memcached.servers", memcached_servers);
            jpaProperties.put("hibernate.memcached.cacheTimeSeconds", cacheTimeSeconds);
            jpaProperties.put("hibernate.cache.region.factory_class",
                    "com.googlecode.hibernate.memcached.MemcachedRegionFactory");
            jpaProperties.put("hibernate.memcached.hashAlgorithm", "HashAlgorithm.FNV1_64_HASH");
            jpaProperties.put("hibernate.memcached.connectionFactory", "KetamaConnectionFactory");
        }
        jpaProperties.put("hibernate.use_sql_comments", booleanFalseStr);
        jpaProperties.put("hibernate.generate_statistics", booleanFalseStr);
        jpaProperties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        jpaProperties.put("foreign-key", "none");
        jpaProperties.put("hibernate.format_sql", format_sql);
        entityManagerFactory.setJpaPropertyMap(jpaProperties);

        return entityManagerFactory;
    }
    /**
     * 
     * hibernateJpaVendorAdapter
     * 
     * @return
     */
    private HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        hibernateJpaVendorAdapter.setShowSql(Boolean.valueOf(showsql));
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        return hibernateJpaVendorAdapter;
    }

    /**
     * 实例化事务管理器
     * 
     * @return
     * @throws SQLException
     */
    @Bean
    public JpaTransactionManager transactionManager() throws SQLException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(tcmDataSource());
        transactionManager.setEntityManagerFactory(tcmEntityManagerFactory().getObject());
        return transactionManager;
    }

    /**
     * 实例化主库的dao
     * 
     * @return
     */
    @Bean
    public GenericEntityDao genericEntityDao() {
        return new GenericEntityDao();
    }

    /**
     * 实例化只读库的dao
     * 
     * @return
     */
    @Bean
    public GenericEntityDaoSlave genericEntityDaoSlave() {
        return new GenericEntityDaoSlave();
    }

    /**
     * 实例化JdbcTemplate
     * 
     * @return
     * @throws SQLException
     */
    @Bean
    public JdbcTemplate jdbcTemplate() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(tcmDataSource());
        return jdbcTemplate;
    }
    @Bean
    @Primary
    public JdbcDao jdbcDao() throws SQLException{
        JdbcDao jdbc = new JdbcDao();
        jdbc.setDataSource(tcmDataSource());
        return jdbc;
    }

    /**
     * 实例化返回fieldName和value的查询的实例：NamedParameterJdbcTemplate
     * 
     * @return
     * @throws SQLException
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate());
        return namedParameterJdbcTemplate;
    }
}
