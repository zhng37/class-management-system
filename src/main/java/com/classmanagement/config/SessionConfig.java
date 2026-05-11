package com.classmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 会话配置类 - 处理登录拦截
 */
@Configuration
public class SessionConfig implements WebMvcConfigurer {
    
    @Bean
    public HandlerInterceptor loginInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
                    throws Exception {
                String uri = request.getRequestURI();
                
                // 允许访问的路径
                if (uri.equals("/") || uri.equals("/user/login") || uri.equals("/user/register") || 
                    uri.startsWith("/static/") || uri.startsWith("/css/") || uri.startsWith("/js/")) {
                    return true;
                }
                
                // 检查是否已登录
                HttpSession session = request.getSession();
                Object user = session.getAttribute("user");
                
                if (user == null) {
                    response.sendRedirect("/user/login");
                    return false;
                }
                
                return true;
            }
        };
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/user/login", "/user/register", "/static/**", "/css/**", "/js/**");
    }
}

