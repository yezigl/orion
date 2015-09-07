package com.orion.mongodb.dao;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.WriteResult;
import com.orion.mongodb.entity.AbstractEntity;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月1日
 */
public abstract class AbstractEntityDao<E extends AbstractEntity> extends BasicDAO<E, ObjectId> {

    @Autowired
    public AbstractEntityDao(Datastore datastore) {
        super(datastore);
    }

    public List<E> getAll() {
        return this.createQuery().asList();
    }

    public boolean update(E entity) {
        entity.setUpdateTime(new Date());
        Key<E> key = super.save(entity);

        if (null != key && null != key.getId()) {
            return true;
        } else {
            return false;
        }
    }

    public String create(E entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        Key<E> key = super.save(entity);

        ObjectId id = (ObjectId) key.getId();
        entity.setId(id);
        return id != null ? id.toString() : null;
    }

    public E get(String id) {
        return super.findOne("id", new ObjectId(id));
    }

    public int delete(String id) {
        WriteResult result = super.deleteById(new ObjectId(id));
        return result.getN();
    }

    public void saveOrUpdateEntity(E entity) {
        if (entity.getId() != null) {
            update(entity);
        } else {
            create(entity);
        }
    }

    public List<E> paginationQueryEntities(Query<E> query, int offset, int limit) {
        query.offset(offset).limit(limit);
        return query.asList();
    }

    public boolean uniqueField(E appEntity, String uniquefield, String fieldValue) {
        Query<E> query = this.createQuery();
        query.field(uniquefield).equal(fieldValue);
        if (appEntity.getId() != null) {
            query.field("id").notEqual(appEntity.getId());
        }
        return query.get() == null;
    }

    public E get(Query<E> query) {
        return query.get();
    }

    public List<E> getList(Query<E> query) {
        return query.asList();
    }

    public long countEntities(Query<E> query) {
        return query.countAll();
    }

}
