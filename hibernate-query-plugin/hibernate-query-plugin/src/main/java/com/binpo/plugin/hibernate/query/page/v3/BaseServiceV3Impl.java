package com.binpo.plugin.hibernate.query.page.v3;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.binpo.plugin.hibernate.query.page.support.IPageList;
import com.binpo.plugin.hibernate.query.page.support.IPageQuery;
import com.binpo.plugin.hibernate.query.page.support.IQueryObject;
import com.binpo.plugin.hibernate.query.page.support.PageList;
import com.binpo.plugin.hibernate.query.page.support.PageObject;
import com.binpo.plugin.hibernate.query.page.v2.GenericPageList;
import com.binpo.plugin.hibernate.query.page.v2.IGenericDAO;
import com.binpo.plugin.hibernate.query.strategy.IdService;

/**
 * 
 * 基础的业务处理
 *
 * @author zhang 2017年4月19日 下午4:31:51
 * @param <T> pojo
 */
@Service
@Transactional
public class BaseServiceV3Impl implements IBaseService {
    private Log logger = LogFactory.getLog(BaseServiceV3Impl.class);
    @Autowired
    protected IGenericDAO dao;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private IdService idw;
    


    @Transactional
    @Override
    public boolean save(Object paramModel) {
        this.dao.save(paramModel);
        return true;
    }

    @Override
    public <T> T  getObjById(Class<T> zlass,Serializable id) {
        T bean = this.dao.get(zlass,id);
        if (bean != null) {
            return bean;
        }
        return null;
    }

    @Override
    public <T> T  getObjOfSlave(Class<T> zlass,Serializable id) {
        T bean = this.dao.getOfSlave(zlass,id);
        if (bean != null) {
            return bean;
        }
        return null;
    }

    @Override
    @Transactional
    public <T> boolean  delete(Class<T> zlass,Serializable id) {
        this.dao.remove(zlass,id);
        return true;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public <T> IPageList list(Class<T> zlass,IQueryObject properties) {
        if (properties == null) {
            return null;
        }
        String query = properties.getQuery();
        Map params = properties.getParameters();
        GenericPageList pList = new GenericPageList(zlass, query, params, this.dao);
        PageObject pageObj = properties.getPageObj();
        if (pageObj != null) {
            pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj.getCurrentPage().intValue(),
                    pageObj.getPageSize() == null ? 0 : pageObj.getPageSize().intValue());
        }
        return pList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IPageList list(IPageQuery page) {
        //count
        String query = page.getQuery();
        String sqlCount = "select count(*) as count from ( "+query+" ) a ";
        Map<String, Object> queryForMap = this.jdbcTemplate.queryForMap(sqlCount, page.getParameters());
        Object object = queryForMap.get("count");
        int rowCount = Integer.valueOf(object.toString());
        int pages = ((rowCount + page.getPageObj().getPageSize() - 1) / page.getPageObj().getPageSize());
        if (pages <= 0) {
            pages = 1;
        }
        String sql = query + " limit " + (page.getPageObj().getCurrentPage() - 1)
                * page.getPageObj().getPageSize() + "," + page.getPageObj().getPageSize();
        if(logger.isDebugEnabled()){
            logger.debug("sql:"+sql);
        }
        List<Map<String, Object>> queryForList = this.jdbcTemplate.queryForList(sql, page.getParameters());
        PageList pageList = new PageList(rowCount, 
                pages, page.getPageObj().getCurrentPage(), page.getPageObj().getPageSize(), 
                queryForList);
        return pageList;
    }
    
    @Override
    public <T> IPageList listOfSlave(Class<T> zlass,IQueryObject properties) {
        if (properties == null) {
            return null;
        }
        String query = properties.getQuery();
        Map params = properties.getParameters();
        GenericPageList pList = new GenericPageList(zlass, query, params, this.dao);
        PageObject pageObj = properties.getPageObj();
        if (pageObj != null) {
            pList.doListOfSlave(pageObj.getCurrentPage() == null ? 0 : pageObj.getCurrentPage().intValue(),
                    pageObj.getPageSize() == null ? 0 : pageObj.getPageSize().intValue());
        }
        return pList;
    }

    @Override
    @Transactional
    public boolean update(Object bean) {
        this.dao.update(bean);
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List query(String query, Map params, int begin, int max) {
        return this.dao.query(query, params, begin, max);
    }

    @Override
    public List queryOfSlave(String query, Map params, int begin, int max) {
        return this.dao.queryOfSlave(query, params, begin, max);
    }

    @Override
    public <T> T getObjByProperty(Class<T> zlass,String propertyName, Object value) {
        // return (T) this.dao.getBy(propertyName, value);
        Map param = new HashMap();
        param.put("value", value);
        List<T> listT = this.dao.query("from " + zlass.getName() + " where " + propertyName + "=:value",
                param, 0, 1);
        if (listT == null || listT.size() == 0) {
            return null;
        } else {
            return listT.get(0);
        }
    }

    @Override
    public <T> T getObjByPropertyOfSlave(Class<T> zlass,String propertyName, Object value) {
        // return (T) this.dao.getBy(propertyName, value);
        Map param = new HashMap();
        param.put("value", value);
        List<T> listT = this.dao.queryOfSlave("from " + zlass.getName() + " where " + propertyName
                + "=:value", param, 0, 1);
        if (listT == null || listT.size() == 0) {
            return null;
        } else {
            return listT.get(0);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public int batchUpdate(String paramString, Map paramArrayOfObject) {
        return this.dao.batchUpdate(paramString, paramArrayOfObject);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object queryUnique(String queryStr, Map params) {
        return this.dao.queryUnique(queryStr, params);
    }

    @Override
    public Object queryUniqueOfSlave(String queryStr, Map params) {
        return this.dao.queryUniqueOfSlave(queryStr, params);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object executeNativeQueryUnique(String nnq, Map params) {
        return this.dao.executeNativeQueryUnique(nnq, params);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List executeNativeQuery(String nnq, Map params, int begin, int max) {
        return this.dao.executeNativeQuery(nnq, params, begin, max);
    }

    @Override
    public List executeNativeQueryOfSlave(String nnq, Map params, int begin, int max) {
        return this.dao.executeNativeQueryOfSlave(nnq, params, begin, max);
    }
    /**
     * 纯sql的查询 不需要通过hibernate进行sql解释
     * 
     * @param sql 例如：select obj.id,obj.userName from iskyshop_user obj where
     *            obj.userName=:userName
     * @param params 参数
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> params) {
        return jdbcTemplate.queryForList(sql, params);
    }
    
    public Map getParams() {
        return new HashMap();
    }

	@Override
	public void clearSessionCache() {
		dao.clearSessionCache();
	}
}
