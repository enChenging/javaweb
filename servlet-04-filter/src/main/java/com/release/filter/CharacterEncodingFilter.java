package com.release.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * @author yancheng
 * @since 2022/7/6
 */
public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("==========init==========");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        System.out.println("=============执行前=================");
        filterChain.doFilter(request, response);
        System.out.println("=============执行后=================");
    }

    @Override
    public void destroy() {
        System.out.println("==========destroy==========");
    }
}
