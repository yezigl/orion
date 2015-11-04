/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.web.exception;

import com.orion.web.res.Status;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年11月4日
 */
public class WebException extends Exception {

    private static final long serialVersionUID = 1L;

    private Status status;

    /**
     * 
     */
    public WebException() {
    }

    public WebException(Status status) {
        this.setStatus(status);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
