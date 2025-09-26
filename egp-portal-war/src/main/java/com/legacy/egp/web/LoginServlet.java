package com.legacy.egp.web;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Legacy Login Servlet
 * Handles user authentication using MD5 password hashing (legacy security!)
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class);
    private static final Logger securityLogger = Logger.getLogger("SECURITY_EVENTS");
    
    // Legacy: Hardcoded test users (security issue!)
    private static final Map<String, String> TEST_USERS = new HashMap<String, String>();
    
    static {
        // Legacy: MD5 hashed passwords (weak security!)
        TEST_USERS.put("admin", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"); // "password"
        TEST_USERS.put("jsmith", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"); // "password"
        TEST_USERS.put("mjohnson", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"); // "password"
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("logout".equals(action)) {
            handleLogout(request, response);
        } else {
            // Redirect to login page
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        logger.info("Login attempt for username: " + username);
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            logger.warn("Invalid login attempt - missing credentials");
            redirectToLoginWithError(request, response);
            return;
        }
        
        // Legacy: Authenticate user
        UserInfo user = authenticateUser(username.trim(), password);
        
        if (user != null) {
            // Legacy: Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", user);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            logger.info("Successful login for user: " + username);
            securityLogger.info("LOGIN_SUCCESS - User: " + username + ", IP: " + getClientIP(request) + 
                              ", SessionId: " + session.getId());
            
            // Legacy: Update last login time
            updateLastLogin(username);
            
            // Redirect to home page
            response.sendRedirect(request.getContextPath() + "/home.jsp");
            
        } else {
            logger.warn("Failed login attempt for username: " + username);
            securityLogger.warn("LOGIN_FAILED - User: " + username + ", IP: " + getClientIP(request));
            
            redirectToLoginWithError(request, response);
        }
    }
    
    /**
     * Legacy: Authenticate user with MD5 password hash
     */
    private UserInfo authenticateUser(String username, String password) {
        try {
            // Legacy: First try hardcoded test users
            String expectedHash = TEST_USERS.get(username);
            if (expectedHash != null) {
                String passwordHash = md5Hash(password);
                if (expectedHash.equals(passwordHash)) {
                    return createTestUser(username);
                }
            }
            
            // Legacy: Try database authentication
            return authenticateFromDatabase(username, password);
            
        } catch (Exception e) {
            logger.error("Error during authentication for user: " + username, e);
            return null;
        }
    }
    
    /**
     * Legacy: Database authentication
     */
    private UserInfo authenticateFromDatabase(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Legacy: Get database connection
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/EgpDS");
            conn = ds.getConnection();
            
            // Legacy: Query user
            String sql = "SELECT user_id, username, password_hash, email, first_name, last_name, role, active " +
                        "FROM egp_users WHERE username = ? AND active = true";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String passwordHash = md5Hash(password);
                
                if (storedHash.equals(passwordHash)) {
                    UserInfo user = new UserInfo();
                    user.setUserId(rs.getLong("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
            
        } catch (Exception e) {
            logger.error("Database authentication error for user: " + username, e);
        } finally {
            // Legacy: Manual resource cleanup
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.error("Error closing database resources", e);
            }
        }
        
        return null;
    }
    
    /**
     * Legacy: Create test user object
     */
    private UserInfo createTestUser(String username) {
        UserInfo user = new UserInfo();
        user.setUsername(username);
        
        if ("admin".equals(username)) {
            user.setUserId(1L);
            user.setFirstName("System");
            user.setLastName("Administrator");
            user.setRole("ADMIN");
            user.setEmail("admin@egp.gov");
        } else if ("jsmith".equals(username)) {
            user.setUserId(2L);
            user.setFirstName("John");
            user.setLastName("Smith");
            user.setRole("SUPERVISOR");
            user.setEmail("john.smith@egp.gov");
        } else if ("mjohnson".equals(username)) {
            user.setUserId(3L);
            user.setFirstName("Mary");
            user.setLastName("Johnson");
            user.setRole("USER");
            user.setEmail("mary.johnson@egp.gov");
        }
        
        user.setLastLogin(new Date());
        return user;
    }
    
    /**
     * Legacy: MD5 password hashing (weak security!)
     */
    private String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 algorithm not available", e);
            return null;
        }
    }
    
    /**
     * Handle user logout
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserInfo user = (UserInfo) session.getAttribute("currentUser");
            if (user != null) {
                logger.info("User logout: " + user.getUsername());
                securityLogger.info("LOGOUT - User: " + user.getUsername() + ", IP: " + getClientIP(request));
            }
            
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/login.jsp?logout=true");
    }
    
    /**
     * Legacy: Update last login time
     */
    private void updateLastLogin(String username) {
        // Legacy: This would update the database, but for simplicity we'll skip it
        logger.debug("Updating last login time for user: " + username);
    }
    
    /**
     * Redirect to login page with error
     */
    private void redirectToLoginWithError(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=true");
    }
    
    /**
     * Get client IP address
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    
    /**
     * Legacy: Simple user info class
     */
    public static class UserInfo {
        private Long userId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private Date lastLogin;
        
        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public Date getLastLogin() { return lastLogin; }
        public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
    }
}