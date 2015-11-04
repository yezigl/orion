/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.web.interceptor;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.orion.web.Params;
import com.orion.web.res.Representation;
import com.orion.web.res.Status;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年11月4日
 */
public class SignInterceptor extends HandlerInterceptorAdapter {

    private static final long EXPIRE = 5 * 60 * 1000;

    @Value(value = "")
    String signKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new TreeMap<String, String>();
        for (Entry<String, String[]> entry : requestParams.entrySet()) {
            params.put(entry.getKey(), entry.getValue().length > 0 ? entry.getValue()[0] : "");
        }
        String sign = "";
        long timestamp = 0;
        StringBuilder builder = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().equals(Params.SIGN)) {
                sign = entry.getValue();
            } else {
                builder.append(entry.getKey()).append("=").append(entry.getValue());
                if (entry.getKey().equals(Params.TIMESTAMP)) {
                    try {
                        timestamp = Integer.parseInt(entry.getValue());
                    } catch (Exception e) {
                    }
                }
            }
        }
        builder.append(signKey);
        String mysign = DigestUtils.md5Hex(builder.toString());
        // 签名不一致
        if (!mysign.equals(sign)) {
            Representation rep = new Representation();
            rep.setError(Status.SIGN_INVALID);
            rep.output(response);
            return false;
        }
        // 请求过期
        if (System.currentTimeMillis() - timestamp > EXPIRE) {
            Representation rep = new Representation();
            rep.setError(Status.REQUEST_EXPIRE);
            rep.output(response);
            return false;
        }
        return true;
    }
    
}
