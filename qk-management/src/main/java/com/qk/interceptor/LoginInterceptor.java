package com.qk.interceptor;

import com.alibaba.fastjson.JSON;
import com.qk.common.Result;
import com.qk.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String uri = request.getRequestURI();
        log.info("拦截请求: {}", uri);

        // 判断是否为公共路径（放行）
        if (isPublicPath(uri)) {
            log.info("放行公共路径: {}", uri);
            return true;
        }

        // 1. 先尝试从 Authorization 获取
        String token = request.getHeader("Authorization");

        // 2. 如果 Authorization 没有，从 token 获取
        if (token == null || token.isEmpty()) {
            token = request.getHeader("token");
        }

        log.info("获取到的 token: {}", token);

        // 校验 token 是否有效
        if (token == null || token.isEmpty()) {
            log.warn("token 为空，请求被拦截");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("未登录，请先登录")));
            return false;
        }

        // 去掉 "Bearer " 前缀（如果有）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 token
        try {
            boolean valid = JwtUtils.validateToken(token);
            if (!valid) {
                log.warn("token 无效或已过期");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSON.toJSONString(Result.error("登录已过期，请重新登录")));
                return false;
            }
            log.info("token 验证通过");
            return true;
        } catch (Exception e) {
            log.error("token 验证异常: {}", e.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("登录已过期，请重新登录")));
            return false;
        }
    }

    /**
     * 判断是否为公共路径（不需要登录）
     */
    private boolean isPublicPath(String uri) {
        String[] publicPaths = {
                "/login",
                "/api/login",
                "/upload",
                "/api/upload",
                "/images/",
                "/css/",
                "/js/",
                "/favicon.ico"
        };

        for (String path : publicPaths) {
            if (path.endsWith("/")) {
                if (uri.startsWith(path)) {
                    return true;
                }
            } else if (uri.equals(path) || uri.endsWith(path)) {
                return true;
            }
        }
        return false;
    }
}