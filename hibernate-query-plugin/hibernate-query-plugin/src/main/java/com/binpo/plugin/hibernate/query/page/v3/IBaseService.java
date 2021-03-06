package com.binpo.plugin.hibernate.query.page.v3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.binpo.plugin.hibernate.query.page.support.IPageList;
import com.binpo.plugin.hibernate.query.page.support.IPageQuery;
import com.binpo.plugin.hibernate.query.page.support.IQueryObject;

/**
 * 基础的数据处理接口，新的接口可以继承这个接口
 * 
 * @author zhang
 *
 * @param <T>
 * @param <Dao>
 */
public interface IBaseService {
    /**
     * 保存对象 id为String 请使用uuid id为Long使用 IdService
     * 
     * @param obj obj
     * @return
     */
    boolean save(Object obj);

    /**
     * 通过id获取对象
     * 
     * @param id 数据库id
     * @return
     */
    public <T> T  getObjById(Class<T> zlass,Serializable id);

    /**
     * 物理删除数据库数据
     * 
     * @param id 数据库id
     * @return
     */
    <T> boolean delete(Class<T> zlass,Serializable id);



    /**
     * 
     * 查询
     * 
     * @param queryObject queryObject
     * @return
     */
    <T> IPageList list(Class<T> zlass,IQueryObject queryObject);

    /**
     * 
     * 分页查询
     * 
     * @param page {@link PageQuery}
     * @return
     */
    IPageList list(IPageQuery page);


    /**
     * 
     * 更新
     * 
     * @param obj obj
     * @return
     */
    boolean update(Object obj);

    /**
     * 
     * 查询 example
     * 
     * <pre>
     * String hql = "select obj from User obj where obj.userName = :userName";
     * List<{@link User}> list = query(hql, ImmutableMap.of("userName","张三"), -1, -1);
     * begin,max 为-1 表示查询出数据库中所有符合要求的数据
     * begin = 0 ，max = 1 表示从0开始查询一条数据
     * </pre>
     * 
     * 
     * @param nnq 查询语句
     * @param params 查询参数
     * @param begin 开始
     * @param max 查询数量
     * @return
     */
    List query(String nnq, Map params, int begin, int max);

    /**
     * 
     * 根据属性获取对象
     * 
     * <pre>
     * getObjByProperty(&quot;userName&quot;, &quot;张三&quot;)
     * </pre>
     * 
     * @param propertyName 属性名
     * @param value 属性值
     * @return
     */
    <T> T getObjByProperty(Class<T> zlass,String propertyName, Object value);

    /**
     * 
     * 批量更新
     * 
     * @param hql hql
     * @param params params
     * @return
     */
    int batchUpdate(String hql, Map params);

    /**
     * 
     * 查询单条数据，存在多条则返回第一条
     * 
     * <pre>
     * String hql = &quot;select obj from User obj where obj.userName = :userName&quot;;
     * Object user = queryUnique(hql, ImmutableMap.of(&quot;userName&quot;, &quot;张三&quot;));
     * 
     * </pre>
     * 
     * @param hql
     * @param params
     * @return
     */
    Object queryUnique(String hql, Map params);

    /**
     * 
     * 支持原生sql的查询，存在多条则返回第一条
     * 
     * <pre>
     * String sql = &quot;select obj.id,obj.userName from ces_tcm_user obj where obj.userName=:userName&quot;;
     * Object user = executeNativeQueryUnique(sql, ImmutableMap.of(&quot;userName&quot;, &quot;张三&quot;));
     * </pre>
     * 
     * @param nnq
     * @param params
     * @return
     */
    Object executeNativeQueryUnique(String nnq, Map params);

    /**
     * 
     * 支持原生sql的查询
     * 
     * <pre>
     * String sql = "select obj.id,obj.userName from ces_tcm_user obj where obj.userName=:userName";
     * Object user = executeNativeQueryUnique(sql,ImmutableMap.of("userName","张三"),-1,-1);
     * begin,max 为-1 表示查询出数据库中所有符合要求的数据
     * begin = 0 ，max = 1 表示从0开始查询一条数据
     * </pre>
     * 
     * @param nnq
     * @param params
     * @param begin 开始位置
     * @param max 查询数量
     * @return
     */
    List executeNativeQuery(String nnq, Map params, int begin, int max);

    /**
     * 
     * 从只读库查询数据
     * 
     * <pre>
     * String hql = "select obj from User obj where obj.userName = :userName";
     * List<{@link User}> list = query(hql, ImmutableMap.of("userName","张三"), -1, -1);
     * begin,max 为-1 表示查询出数据库中所有符合要求的数据
     * begin = 0 ，max = 1 表示从0开始查询一条数据
     * </pre>
     * 
     * @param hql
     * @param params
     * @param begin 数据开始位置
     * @param max 查询数量
     * @return
     */
    List queryOfSlave(String hql, Map params, int begin, int max);

    /**
     * 从只读库读取数据
     * 
     * @param hql
     * @param params
     * @return
     */
    Object queryUniqueOfSlave(String hql, Map params);

    /**
     * 从只读库读取数据
     * 
     * @param nnq
     * @param params
     * @param begin
     * @param max
     * @return
     */
    List executeNativeQueryOfSlave(String nnq, Map params, int begin, int max);

    /**
     * 从只读库读取数据
     * 
     * @param properties properties
     * @return
     */
    <T> IPageList listOfSlave(Class<T> zlass,IQueryObject properties);

    /**
     * 从只读库读取数据
     * 
     * @param id id
     * @return
     */
    <T> T  getObjOfSlave(Class<T> zlass,Serializable id);

    /**
     * 从只读库读取数据
     * 
     * @param propertyName
     * @param value
     * @return
     */
    <T> T getObjByPropertyOfSlave(Class<T> zlass,String propertyName, Object value);

    /**
     * 纯sql的查询 不需要通过hibernate进行sql解释
     * 
     * @param sql 例如：select obj.id,obj.userName from ces_tcm_user obj where
     *            obj.userName=:userName
     * @param params 参数
     * @return
     */
    List<Map<String, Object>> queryForList(String sql, Map<String, ?> params);
    
    void clearSessionCache();
}
