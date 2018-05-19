package com.binpo.plugin.hibernate.query.page.generic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.binpo.plugin.hibernate.IdEntity;


public abstract interface IGenericDAO<T> {
	public abstract T get(Serializable paramSerializable);

	public <T extends IdEntity> void save(T paramT);

	public abstract void remove(Serializable paramSerializable);

	public abstract void update(T paramT);

	public abstract T getBy(String paramString, Object paramObject);

	public abstract List executeNamedQuery(String paramString,
			Object[] paramArrayOfObject, int paramInt1, int paramInt2);
	
	public abstract int executeNativeSQLMap(final String nnq,Map params);
	
	public abstract List<T> find(String paramString, Map paramMap,
			int paramInt1, int paramInt2);
	public  abstract List findNoCache(String query, Map params, int begin, int max);
	public abstract List query(String paramString, Map paramMap, int paramInt1,
			int paramInt2);

	public abstract int batchUpdate(String paramString,
			Object[] paramArrayOfObject);

	public abstract int batchUpdate(String paramString, Map paramArrayOfObject);

	public abstract List executeNativeNamedQuery(String paramString);

	public abstract List executeNativeQuery(String paramString,
			Object[] paramArrayOfObject, int paramInt1, int paramInt2);

	public abstract List executeNativeQuery(String nnq, Map params, int begin,
			int max);

	public abstract int executeNativeSQL(String paramString);

	public abstract void flush();

	/**
	 * HQL取得唯一记录
	 * 
	 * @param queryStr
	 * @param params
	 * @return
	 *
	 * @author Francis
	 * @since 2015年9月18日
	 */
	public abstract Object queryUnique(final String queryStr, final Map params);

	/**
	 * SQL取得唯一记录
	 * 
	 * @param nnq
	 * @param params
	 * @return
	 *
	 * @author Francis
	 * @since 2015年9月18日
	 */
	public abstract Object executeNativeQueryUnique(final String nnq,
			final Map params);

	T getOfSlave(Serializable id);

	Object queryUniqueOfSlave(String queryStr, Map params);

	List queryOfSlave(String query, Map params, int begin, int max);

	List findOfSlave(String query, Map params, int begin, int max);

	List executeNativeQueryOfSlave(String nnq, Map params, int begin, int max);

	List executeNativeQueryOfSlave(String nnq, Object[] params, int begin,
			int max);

	List executeNativeNamedQueryOfSlave(String nnq);

	List executeNamedQueryOfSlave(String queryName, Object[] params, int begin,
			int max);
}