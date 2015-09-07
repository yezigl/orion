package com.orion.mongodb.entity;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月1日
 */
public abstract class AbstractEntity {

    @Id
    private ObjectId id;
    
    @Property("ctime")
    private Date createTime;
    
    @Property("utime")
    private Date updateTime;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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
    
    public String stringifyId() {
        return id.toString();
    }
}
