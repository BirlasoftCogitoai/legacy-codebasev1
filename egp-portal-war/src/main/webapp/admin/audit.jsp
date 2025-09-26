<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - Audit Log</title>
    <link rel="stylesheet" type="text/css" href="../styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">System Audit Log</div>
    </div>

    <div class="nav-bar">
      <a href="../home.jsp">Home</a>
      <a href="../profile">Profile</a>
      <a href="../cases">Cases</a>
      <a href="users.jsp">Admin</a>
      <a href="../login?action=logout" style="float: right">Logout</a>
    </div>

    <div class="content">
      <h2>Audit Trail</h2>

      <div class="message message-info">
        <strong>Compliance Notice:</strong> This audit log is maintained for
        regulatory compliance and security monitoring purposes. All system
        activities are logged and retained for 7 years.
      </div>

      <h3>Recent System Activity</h3>
      <table class="data-table">
        <tr>
          <th>Timestamp</th>
          <th>User</th>
          <th>Action</th>
          <th>Table</th>
          <th>Record ID</th>
          <th>IP Address</th>
        </tr>
        <!-- Legacy: Sample audit entries -->
        <tr>
          <td>12/03/2024 09:15:23</td>
          <td>jsmith</td>
          <td>UPDATE</td>
          <td>egp_case_records</td>
          <td>1</td>
          <td>192.168.1.100</td>
        </tr>
        <tr>
          <td>12/03/2024 09:10:45</td>
          <td>mjohnson</td>
          <td>INSERT</td>
          <td>egp_notes</td>
          <td>15</td>
          <td>192.168.1.101</td>
        </tr>
        <tr>
          <td>12/03/2024 08:55:12</td>
          <td>admin</td>
          <td>UPDATE</td>
          <td>egp_users</td>
          <td>5</td>
          <td>192.168.1.50</td>
        </tr>
        <tr>
          <td>12/03/2024 08:30:33</td>
          <td>admin</td>
          <td>LOGIN</td>
          <td>SYSTEM</td>
          <td>0</td>
          <td>192.168.1.50</td>
        </tr>
        <tr>
          <td>12/02/2024 16:45:18</td>
          <td>mjohnson</td>
          <td>INSERT</td>
          <td>egp_customers</td>
          <td>6</td>
          <td>192.168.1.101</td>
        </tr>
        <tr>
          <td>12/02/2024 16:30:07</td>
          <td>bwilson</td>
          <td>UPDATE</td>
          <td>egp_case_records</td>
          <td>3</td>
          <td>192.168.1.102</td>
        </tr>
        <tr>
          <td>12/02/2024 14:22:55</td>
          <td>jsmith</td>
          <td>DELETE</td>
          <td>egp_notes</td>
          <td>12</td>
          <td>192.168.1.100</td>
        </tr>
        <tr>
          <td>12/02/2024 11:15:41</td>
          <td>mjohnson</td>
          <td>LOGIN</td>
          <td>SYSTEM</td>
          <td>0</td>
          <td>192.168.1.101</td>
        </tr>
      </table>

      <div style="margin-top: 20px">
        <a href="audit.jsp?export=csv" class="button button-secondary"
          >Export to CSV</a
        >
        <a href="audit.jsp?filter=true" class="button button-secondary"
          >Advanced Filter</a
        >
      </div>
    </div>

    <div class="footer">
      <p>
        &copy; 2008 Enterprise Government Portal System. All rights reserved.
      </p>
      <p>
        <strong>Security Notice:</strong> Unauthorized access to audit logs is
        prohibited and monitored.
      </p>
    </div>
  </body>
</html>
