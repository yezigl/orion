/**
 * Copyright 2016 yezi.gl. All Rights Reserved.
 */
package com.orion.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * description here
 *
 * @author yezi
 * @since 2016年4月23日
 */
public interface OrionMongoRepository<T> extends MongoRepository<T, String> {

}
