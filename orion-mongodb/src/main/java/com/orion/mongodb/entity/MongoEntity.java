package com.orion.mongodb.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;


/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月1日
 */
public abstract class MongoEntity {

    @Id
    private String id;
    
    private Date createTime;
    
    private Date updateTime;
    
    /**
     * 
     */
    public MongoEntity() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
