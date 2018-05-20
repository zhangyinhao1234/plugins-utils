package com.binpo.pojo;


import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.binpo.plugin.hibernate.IdEntity;
/**
 * 
 * 只是一个例子
 *
 * @author zhang 2017年5月31日 下午2:46:50
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "binpo_bi_example")
public class Example extends IdEntity{

    private String name;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
