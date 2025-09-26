package com.legacy.egp.web;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Legacy Authentication Filter
 * Implements session-based authentication for the EGP portal
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
public class AuthFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(AuthFilter.class);
    private static final Logger securityLogger = Logger.getLogger("SECURITY_EVENTS");
    
    private List<String> excludePatterns;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing AuthFilter");
        
        // Legacy: Parse exclude patterns from init parameter
        String excludeParam = filterConfig.getInitParameter("excludePatterns");
        if (excludeParam != null) {
            excludePatterns = Arrays.asList(excludeParam.split(","));
            logger.info("Exclude patterns: " + excludePatterns);
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        logger.debug("Processing request: " + path);
        
        // Legacy: Check if path should be excluded from authentication
        if (shouldExclude(path)) {
            logger.debug("Path excluded from authentication: " + path);
            chain.doFilter(request, response);
            return;
        }
        
        // Legacy: Check for valid session
        HttpSession session = httpRequest.getSession(false);
        Object currentUser = null;
        
        if (session != null) {
            currentUser = session.getAttribute("currentUser");
        }
        
        if (currentUser == null) {
            logger.info("Unauthorized access attempt to: " + path + " from IP: " + getClientIP(httpRequest));
            securityLogger.warn("Unauthorized access attempt - Path: " + path + ", IP: " + getClientIP(httpRequest));
            
            // Legacy: Redirect to login page
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }
        
        // Legacy: Log successful access
        logger.debug("Authenticated access to: " + path + " by user: " + currentUser);
        
        // Legacy: Set security headers (basic)
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        logger.info("Destroying AuthFilter");
    }
    
    /**
     * Legacy: Check if path should be excluded from authentication
     */
    private boolean shouldExclude(String path) {
        if (excludePatterns == null) {
            return false;
        }
        
        for (String pattern : excludePatterns) {
            pattern = pattern.trim();
            
            // Legacy: Simple pattern matching
            if (pattern.endsWith("*")) {
                String prefix = pattern.substring(0, pattern.length() - 1);
                if (path.startsWith(prefix)) {
                    return true;
                }
            } else if (path.equals(pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Legacy: Get client IP address (with proxy support)
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
}