/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.web.res;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletResponse;

import org.springframework.http.MediaType;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * description here
 *
 * @author yezi
 * @since 2015年6月14日
 */
public class Representation {

    @JsonInclude(Include.NON_NULL)
    private Res error;
    @JsonInclude(Include.NON_NULL)
    private Res data;
    private long timestamp;

    public Representation() {
        setTimestamp(System.currentTimeMillis());
    }

    public Res getError() {
        return error;
    }

    public void setError(Status status, Object... args) {
        this.error = new Res();
        error.setCode(status.code());
        error.setMessage(String.format(status.msg(), args));
    }

    public Res getData() {
        return data;
    }

    public void setData(Res data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void output(ServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter out = response.getWriter();
        out.print(JSON.toJSONString(this));
        out.flush();
        out.close();
    }

}
