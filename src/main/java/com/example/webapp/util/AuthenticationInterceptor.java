package com.example.webapp.util;

import com.auth0.jwt.exceptions.*;
import com.example.webapp.service.usersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    usersService userService;
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken userLoginToken = method.getAnnotation(LoginToken.class);
            if (userLoginToken.required()) {
                try {
                    SignToken.verify(token);
                    return true;
                } catch (TokenExpiredException e) {
                    map.put("code", 201);
                    map.put("msg", "Token已经过期!");
                } catch (SignatureVerificationException e) {
                    map.put("code", 201);
                    map.put("msg", "签名错误!");
                } catch (AlgorithmMismatchException e) {
                    map.put("code", 201);
                    map.put("msg", "加密算法不匹配!");
                } catch (Exception e) {
                    map.put("code", 201);
                    map.put("msg", "无效token!");
                }
                String json = new ObjectMapper().writeValueAsString(map);
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getWriter().println(json);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }

}