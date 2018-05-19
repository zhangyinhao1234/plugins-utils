package com.binpo.plugin.hibernate.query;

import com.binpo.plugin.hibernate.query.page.generic.GenericEntityDao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QueryService implements IQueryService {

    @Autowired
    @Qualifier("genericEntityDao")
    private GenericEntityDao geDao;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public GenericEntityDao getGeDao() {
        return this.geDao;
    }

    public void setGeDao(GenericEntityDao geDao) {
        this.geDao = geDao;
    }

    @Override
    public List query(String scope, Map params, int begin, int max) {
        return this.geDao.query(scope, params, begin, max);
    }

    @Override
    public List<Map<String, Object>> queryWithSQL(String sql, Map<String, ?> params) {
        return jdbcTemplate.queryForList(sql, params);
    }

    @Override
    public <T> List<T> query(Class<T> zlass, String queryStr, Map params, int begin, int max) {
        return (List<T>)this.geDao.query(queryStr, params, begin, max);
    }

    @Override
    public <T> T getObj(Class<T> zlass, Long id) {
        return (T)this.geDao.get(zlass, id);
    }

    @Override
    public Object queryUnique(String queryStr, Map params) {
        return this.geDao.queryUnique(queryStr, params);
    }
    
}