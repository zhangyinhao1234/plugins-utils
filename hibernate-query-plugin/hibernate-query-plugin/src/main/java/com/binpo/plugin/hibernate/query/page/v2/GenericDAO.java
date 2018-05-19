package com.binpo.plugin.hibernate.query.page.v2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.binpo.plugin.hibernate.query.page.generic.GenericEntityDao;
import com.binpo.plugin.hibernate.query.page.generic.GenericEntityDaoSlave;
@Repository
public class GenericDAO implements IGenericDAO {
 

    @Autowired(required = false)
    private GenericEntityDao geDao;
    @Autowired(required = false)
    private GenericEntityDaoSlave slavedao;



    public GenericEntityDao getGeDao() {
        return this.geDao;
    }

    public void setGeDao(GenericEntityDao geDao) {
        this.geDao = geDao;
    }

    public GenericDAO() {
       
    }

    public GenericDAO(GenericEntityDao geDao) {
        this.geDao = geDao;
    }



    public int batchUpdate(String jpql, Object[] params) {
        return this.geDao.batchUpdate(jpql, params);
    }

    public int batchUpdate(String jpql, Map params) {
        return this.geDao.batchUpdate(jpql, params);
    }

    public List executeNamedQuery(String queryName, Object[] params, int begin, int max) {
        return this.geDao.executeNamedQuery(queryName, params, begin, max);
    }

    @Override
    public List executeNamedQueryOfSlave(String queryName, Object[] params, int begin, int max) {
        return this.slavedao.executeNamedQuery(queryName, params, begin, max);
    }

    public List executeNativeNamedQuery(String nnq) {
        return this.geDao.executeNativeNamedQuery(nnq);
    }

    @Override
    public List executeNativeNamedQueryOfSlave(String nnq) {
        return this.slavedao.executeNativeNamedQuery(nnq);
    }

    public List executeNativeQuery(String nnq, Object[] params, int begin, int max) {
        return this.geDao.executeNativeQuery(nnq, params, begin, max);
    }

    @Override
    public List executeNativeQueryOfSlave(String nnq, Object[] params, int begin, int max) {
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
        return this.geDao.executeNativeSQLMap(nnq, params);
    }

    @Override
    public  List find(Class zlass,String query, Map params, int begin, int max) {
        return getGeDao().find(zlass, query, params, begin, max);
    }

    @Override
    public List findNoCache(Class zlass,String query, Map params, int begin, int max) {
        return getGeDao().findNoCache(zlass, query, params, begin, max);
    }

    @Override
    public List findOfSlave(Class zlass,String query, Map params, int begin, int max) {
        return slavedao.find(zlass, query, params, begin, max);
    }

    public void flush() {
        this.geDao.flush();
    }

    public <T> T get(Class zlass,Serializable id) {
        return (T) getGeDao().get(zlass, id);
    }

    @Override
    public <T> T getOfSlave(Class zlass,Serializable id) {
        return (T) slavedao.get(zlass, id);
    }



    public List query(String query, Map params, int begin, int max) {
        return getGeDao().query(query, params, begin, max);
    }

    @Override
    public List queryOfSlave(String query, Map params, int begin, int max) {
        return slavedao.query(query, params, begin, max);
    }

    public void remove(Class zlass,Serializable id) {
        getGeDao().remove(zlass, id);
    }

    public void  save(Object newInstance) {
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

	@Override
	public void clearSessionCache() {
		geDao.clearSessionCache();		
	}

}