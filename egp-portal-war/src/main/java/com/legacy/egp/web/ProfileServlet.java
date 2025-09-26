package com.legacy.egp.web;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Legacy Profile Management Servlet
 */
public class ProfileServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(ProfileServlet.class);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Forward to profile JSP
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Legacy: Handle profile updates
        logger.info("Profile update request received");
        
        // For this legacy demo, just redirect back to profile
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}