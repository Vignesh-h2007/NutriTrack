package com.nutrition.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Sets UTF-8 encoding on all requests and adds CORS headers for development.
 */
@WebFilter("/*")
public class EncodingCorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpServletResponse httpResp = (HttpServletResponse) response;
        HttpServletRequest  httpReq  = (HttpServletRequest)  request;

        httpResp.setHeader("Access-Control-Allow-Origin", httpReq.getHeader("Origin") != null
                ? httpReq.getHeader("Origin") : "*");
        httpResp.setHeader("Access-Control-Allow-Credentials", "true");
        httpResp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            httpResp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
