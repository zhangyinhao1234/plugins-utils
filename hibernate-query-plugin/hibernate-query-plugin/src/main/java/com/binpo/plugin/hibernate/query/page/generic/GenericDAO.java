package com.binpo.plugin.hibernate.query.page.generic;

import com.binpo.plugin.hibernate.IdEntity;
import com.binpo.plugin.hibernate.query.JdbcDao;
import com.binpo.plugin.hibernate.query.strategy.IdService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class GenericDAO<T> implements IGenericDAO<T> {
	protected Class<T> entityClass;

	@Autowired
	@Qualifier("genericEntityDao")
	private GenericEntityDao geDao;
	@Autowired
	@Qualifier("genericEntityDaoSlave")
	private GenericEntityDaoSlave slavedao;
	@Autowired
    private IdService idw;
	
	public Class<T> getEntityClass() {
		return this.entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public GenericEntityDao getGeDao() {
		return this.geDao;
	}

	public void setGeDao(GenericEntityDao geDao) {
		this.geDao = geDao;
	}

	public GenericDAO() {
		this.entityClass = ((Class) ((java.lang.reflect.ParameterizedType) super
				.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public GenericDAO(GenericEntityDao geDao, Class clazz) {
		this.entityClass = clazz;
		this.geDao = geDao;
	}

	public GenericDAO(Class<T> type) {
		this.entityClass = type;
	}

	public int batchUpdate(String jpql, Object[] params) {
		return this.geDao.batchUpdate(jpql, params);
	}

	public int batchUpdate(String jpql, Map params) {
		return this.geDao.batchUpdate(jpql, params);
	}

	public List executeNamedQuery(String queryName, Object[] params, int begin,
			int max) {
		return this.geDao.executeNamedQuery(queryName, params, begin, max);
	}
	@Override
	public List executeNamedQueryOfSlave(String queryName, Object[] params, int begin,
			int max) {
		return this.slavedao.executeNamedQuery(queryName, params, begin, max);
	}

	public List executeNativeNamedQuery(String nnq) {
		return this.geDao.executeNativeNamedQuery(nnq);
	}
	@Override
	public List executeNativeNamedQueryOfSlave(String nnq) {
		return this.slavedao.executeNativeNamedQuery(nnq);
	}

	public List executeNativeQuery(String nnq, Object[] params, int begin,
			int max) {
		return this.geDao.executeNativeQuery(nnq, params, begin, max);
	}
	@Override
	public List executeNativeQueryOfSlave(String nnq, Object[] params, int begin,
			int max) {
		return this.slavedao.executeNativeQuery(nnq, params, begin, max);
	}

	public List executeNativeQuery(String nnq, Map params, int begin, int max) {
		return this.geDao.executeNativeQuery(nnq, params, begin, max);
	}
	@Override
	public List executeNativeQueryOfSlave(String nnq, Map params, int begin, int max) {
		return this.slavedao.executeNativeQuery(nnq, params, begin, max);
	}

	public int executeNativeSQL(String nnq) {
		return this.geDao.executeNativeSQL(nnq);
	}

	@Override
	public int executeNativeSQLMap(String nnq, Map params) {
		return this.geDao.executeNativeSQLMap(nnq,params);
	}
	@Override
	public List find(String query, Map params, int begin, int max) {
		return getGeDao().find(this.entityClass, query, params, begin, max);
	}
	
	@Override
	public List findNoCache(String query, Map params, int begin, int max) {
		return getGeDao().findNoCache(this.entityClass, query, params, begin, max);
	}

	@Override
	public List findOfSlave(String query, Map params, int begin, int max) {
		return slavedao.find(this.entityClass, query, params, begin, max);
	}

	public void flush() {
		this.geDao.flush();
	}

	public T get(Serializable id) {
		return (T) getGeDao().get(this.entityClass, id);
	}
	@Override
	public T getOfSlave(Serializable id) {
		return (T) slavedao.get(this.entityClass, id);
	}

	public T getBy(String propertyName, Object value) {
		return (T) getGeDao().getBy(this.entityClass, propertyName, value);
	}

	public List query(String query, Map params, int begin, int max) {
		return getGeDao().query(query, params, begin, max);
	}
	@Override
	public List queryOfSlave(String query, Map params, int begin, int max) {
		return slavedao.query(query, params, begin, max);
	}

	public void remove(Serializable id) {
		getGeDao().remove(this.entityClass, id);
	}

	public <T extends IdEntity> void save(T newInstance) {
	    if(newInstance.getId() == null ){
	        newInstance.setId(idw.getId(newInstance.getClass()));
	    }
		getGeDao().save(newInstance);
	}

	public void update(Object transientObject) {
		getGeDao().update(transientObject);
	}

	public Object queryUnique(final String queryStr, final Map params) {
		return getGeDao().queryUnique(queryStr, params);
	}
	@Override
	public Object queryUniqueOfSlave(final String queryStr, final Map params) {
		return slavedao.queryUnique(queryStr, params);
	}

	public Object executeNativeQueryUnique(final String nnq, final Map params) {
		return getGeDao().executeNativeQueryUnique(nnq, params);
	}
}