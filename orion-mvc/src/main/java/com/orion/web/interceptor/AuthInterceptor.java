/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.orion.core.auth.IUser;
import com.orion.core.auth.IUserService;
import com.orion.web.Params;
import com.orion.web.Token;
import com.orion.web.UserContext;

/**
 * description here
 *
 * @author yezi
 * @since 2015年3月21日
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static String[] NO_LOGIN_URL = { "/register", "/login", "/captcha" };

    @Autowired
    IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = ((HttpServletRequest) request).getRequestURI();
        for (String nolgin : NO_LOGIN_URL) {
            if (uri.startsWith(nolgin)) {
                return true;
            }
        }
        String tokenStr = request.getParameter(Params.TOKEN);

        Token token = null;
        if (StringUtils.isNotBlank(tokenStr)) {
            // 基于token的认证
            token = Token.decrypt(tokenStr);
        } else {
            // 基于cookie的认证
        }
        if (token != null) {
            UserContext.setUid(token.getUid().toString());
            IUser user = userService.get(token.getUid());
            UserContext.setUser(user);
        }

        return true;
    }

}
