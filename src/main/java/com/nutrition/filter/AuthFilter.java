package com.nutrition.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Intercepts all /api/* requests and blocks unauthenticated access.
 * Auth endpoints (/api/auth/*) are excluded.
 */
@WebFilter("/api/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  httpReq  = (HttpServletRequest)  request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        String path = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        // Allow auth endpoints and OPTIONS pre-flight
        if (path.startsWith("/api/auth/") || "OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpReq.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.setContentType("application/json;charset=UTF-8");
            httpResp.getWriter().print("{\"success\":false,\"message\":\"Authentication required\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
