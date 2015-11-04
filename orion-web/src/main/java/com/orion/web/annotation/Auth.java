/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description here
 *
 * @author yezi
 * @since 2015年3月18日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

}
