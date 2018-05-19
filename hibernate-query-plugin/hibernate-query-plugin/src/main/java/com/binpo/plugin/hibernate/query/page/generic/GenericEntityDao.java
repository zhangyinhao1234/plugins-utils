package com.binpo.plugin.hibernate.query.page.generic;
import com.binpo.plugin.hibernate.query.JdbcDao;
import com.binpo.plugin.hibernate.query.strategy.IdService;
import com.binpo.plugin.hibernate.query.strategy.IdWorker;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


public class GenericEntityDao{
	@PersistenceContext(unitName = "masterData")
	private EntityManager entityManager;
	private IdService idw = new IdWorker();
	public Object get(Class clazz, Serializable id) {
		if (id == null) {
			return null;
		}
		return entityManager.find(clazz, id);
	}
	public List<Object> findNoCache(Class clazz, final String queryStr,
			final Map params, final int begin, final int max){
		final Class claz = clazz;
		String clazzName = claz.getName();
		StringBuffer sb = new StringBuffer("select obj from ");
		sb.append(clazzName).append(" obj").append(" where ").append(queryStr);
		Query query = entityManager.createQuery(sb.toString());
		for (Object key : params.keySet()) {
			query.setParameter(key.toString(), params.get(key));
		}
		if ((begin >= 0) && (max > 0)) {
			query.setFirstResult(begin);
			query.setMaxResults(max);
		}
	//	query.setHint("org.hibernate.cacheable", Boolean.valueOf(true));
		return query.getResultList();
	}
	
	public List<Object> find(Class clazz, final String queryStr,
			final Map params, final int begin, final int max) {
		final Class claz = clazz;
		String clazzName = claz.getName();
		StringBuffer sb = new StringBuffer("select obj from ");
		sb.append(clazzName).append(" obj").append(" where ").append(queryStr);
		Query query = entityManager.createQuery(sb.toString());
		for (Object key : params.keySet()) {
			query.setParameter(key.toString(), params.get(key));
		}
		if ((begin >= 0) && (max > 0)) {
			query.setFirstResult(begin);
			query.setMaxResults(max);
		}
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(true));
		return query.getResultList();
	}

	public List query(final String queryStr, final Map params, final int begin,
			final int max) {
		Query query = entityManager.createQuery(queryStr);
		if ((params != null) && (params.size() > 0)) {
			for (Object key : params.keySet()) {
				query.setParameter(key.toString(), params.get(key));
			}
		}
		if ((begin >= 0) && (max > 0)) {
			query.setFirstResult(begin);
			query.setMaxResults(max);
		}
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(true));
		return query.getResultList();
	}

	public void remove(Class clazz, Serializable id)
			throws CanotRemoveObjectException {
		Object object = get(clazz, id);
		if (object != null) {
			try {
				this.entityManager.remove(object);
			} catch (Exception e) {
				throw new CanotRemoveObjectException();
			}
		}
	}

	public void save(Object instance) {
		entityManager.persist(instance);
	}

	public Object getBy(Class clazz, final String propertyName,
			final Object value) {
		final Class claz = clazz;
		String clazzName = claz.getName();
		StringBuffer sb = new StringBuffer("select obj from ");
		sb.append(clazzName).append(" obj");
		Query query = null;
		if ((propertyName != null) && (value != null)) {
			sb.append(" where obj.").append(propertyName).append(" = :value");
			query = entityManager.createQuery(sb.toString()).setParameter(
					"value", value);
		} else {
			throw new RuntimeException("GenericEntityDao getBy propertyName or value is null ");
		}
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(true));
		List resultList = query.getResultList();
		if ((resultList != null) && (resultList.size() == 1)) {
			return resultList.get(0);
		}
		if ((resultList != null) && (resultList.size() > 1)) {
			throw new IllegalStateException(
					"worning  --more than one object find!!");
		}
		return null;
	}

	public List executeNamedQuery(final String queryName,
			final Object[] params, final int begin, final int max) {
		Query query = entityManager.createNamedQuery(queryName);
		int parameterIndex = 1;
		if ((params != null) && (params.length > 0)) {
			for (Object obj : params) {
				query.setParameter(parameterIndex++, obj);
			}
		}
		if ((begin >= 0) && (max > 0)) {
			query.setFirstResult(begin);
			query.setMaxResults(max);
		}
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(true));
		return query.getResultList();
	}

	public void update(Object instance) {
		entityManager.merge(instance);
	}

	public List executeNativeNamedQuery(final String nnq) {
		Query query = entityManager.createNativeQuery(nnq);
		return query.getResultList();
	}

	public List executeNativeQuery(final String nnq, final Map params,
			final int begin, final int max) {
		Query query = entityManager.createNativeQuery(nnq);
		int parameterIndex = 1;
		if (params != null) {
			Iterator its = params.keySet().iterator();
			while (its.hasNext()) {
				String k = null2String(its.next());
				query.setParameter(k, params.get(k));
			}
		}
		if ((begin >= 0) && (max > 0)) {
			query.setFirstResult(begin);
			query.setMaxResults(max);
		}
		return query.getResultList();
	}

    private String null2String(Object s) {
        return ((s == null) ? "" : s.toString().trim());
    }
    
	public List executeNativeQuery(final String nnq, final Object[] params,
			final int begin, final int max) {
		Query query = entityManager.createNativeQuery(nnq);
		int parameterIndex = 1;
		if ((params != null) && (params.length > 0)) {
			for (Object obj : params) {
				query.setParameter(parameterIndex++, obj);
			}
		}
		if ((begin >= 0) && (max > 0)) {
			query.setFirstResult(begin);
			query.setMaxResults(max);
		}
		return query.getResultList();
	}

	public int executeNativeSQL(final String nnq) {
		Query query = entityManager.createNativeQuery(nnq);
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(false));
		return Integer.valueOf(query.executeUpdate());
	}
	public int executeNativeSQLMap(final String nnq,Map params) {
		Query query = entityManager.createNativeQuery(nnq);
		if (params != null) {
			Iterator its = params.keySet().iterator();
			while (its.hasNext()) {
				String k = null2String(its.next());
				query.setParameter(k,params.get(k));
			}
		}
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(false));
		return Integer.valueOf(query.executeUpdate());
	}
	public int batchUpdate(final String jpql, final Object[] params) {
		Query query = entityManager.createQuery(jpql);
		int parameterIndex = 1;
		if ((params != null) && (params.length > 0)) {
			for (Object obj : params) {
				query.setParameter(parameterIndex++, obj);
			}
		}
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(true));
		return Integer.valueOf(query.executeUpdate());
	}

	public int batchUpdate(final String jpql, final Map params) {
		Query query = entityManager.createQuery(jpql);
		int parameterIndex = 1;
		if (params != null) {
			Iterator its = params.keySet().iterator();
			while (its.hasNext()) {
				String k = null2String(its.next());
				query.setParameter(k, params.get(k));
			}
		}
		query.setHint("org.hibernate.cacheable", Boolean.valueOf(true));
		return Integer.valueOf(query.executeUpdate());
	}

	public void flush() {
//		entityManager.getTransaction().commit();
		entityManager.flush();
	}

	/**
	 * 清空一级缓存
	 */
	public void clearSessionCache() {
	    entityManager.flush();
		entityManager.clear();
	}
	
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
	public Object queryUnique(final String queryStr, final Map params) {
		Object obj = null;
		List list = this.query(queryStr, params, 0, 1);
		if (isNotEmpty(list)) {
			obj = list.get(0);
		}
		return obj;
	}

    private boolean isNotEmpty(Collection list) {
        boolean flag = false;
        if (list != null && !list.isEmpty()) {
            flag = true;
        }
        return flag;
    }
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
	public Object executeNativeQueryUnique(final String nnq, final Map params) {
		Object obj = null;
		List list = this.executeNativeQuery(nnq, params, 0, 1);
		if (isNotEmpty(list)) {
			obj = list.get(0);
		}
		return obj;
	}
	
//	public void doInTransaction(final Transaction trans) throws TransactionException {
//		JpaTransactionManager txManager=(JpaTransactionManager)CommUtil.getBean("transactionManager");
//		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		TransactionStatus status = txManager.getTransaction(def);
//		try{
//			trans.run();
//			txManager.commit(status);
//		}catch(Exception e){
//			e.printStackTrace();
//			txManager.rollback(status);
//			throw new TransactionException(e);
//		}
//	}
}
