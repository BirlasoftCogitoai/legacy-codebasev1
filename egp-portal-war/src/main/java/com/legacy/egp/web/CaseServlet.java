package com.legacy.egp.web;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Legacy Case Management Servlet
 */
public class CaseServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(CaseServlet.class);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        String status = request.getParameter("status");
        
        logger.debug("Case servlet - action: " + action + ", status: " + status);
        
        // Legacy: Simple routing logic
        if ("new".equals(action)) {
            request.getRequestDispatcher("/caseDetails.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/cases.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Legacy: Handle case creation/updates
        logger.info("Case operation request received");
        
        // For this legacy demo, just redirect back to cases
        response.sendRedirect(request.getContextPath() + "/cases");
    }
}