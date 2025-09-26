<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - User Administration</title>
    <link rel="stylesheet" type="text/css" href="../styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">User Administration</div>
    </div>

    <div class="nav-bar">
      <a href="../home.jsp">Home</a>
      <a href="../profile">Profile</a>
      <a href="../cases">Cases</a>
      <a href="users.jsp" class="active">Admin</a>
      <a href="../login?action=logout" style="float: right">Logout</a>
    </div>

    <div class="content">
      <h2>User Management</h2>

      <div style="margin: 15px 0">
        <a href="users.jsp?action=new" class="button">Add New User</a>
        <a href="audit.jsp" class="button button-secondary">View Audit Log</a>
      </div>

      <h3>System Users</h3>
      <table class="data-table">
        <tr>
          <th>User ID</th>
          <th>Username</th>
          <th>Name</th>
          <th>Email</th>
          <th>Role</th>
          <th>Status</th>
          <th>Last Login</th>
          <th>Actions</th>
        </tr>
        <!-- Legacy: Sample user data -->
        <tr>
          <td>1</td>
          <td>admin</td>
          <td>System Administrator</td>
          <td>admin@egp.gov</td>
          <td>ADMIN</td>
          <td><span style="color: green">Active</span></td>
          <td>12/03/2024 08:30 AM</td>
          <td>
            <a
              href="users.jsp?action=edit&id=1"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >Edit</a
            >
          </td>
        </tr>
        <tr>
          <td>2</td>
          <td>jsmith</td>
          <td>John Smith</td>
          <td>john.smith@egp.gov</td>
          <td>SUPERVISOR</td>
          <td><span style="color: green">Active</span></td>
          <td>12/03/2024 09:15 AM</td>
          <td>
            <a
              href="users.jsp?action=edit&id=2"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >Edit</a
            >
          </td>
        </tr>
        <tr>
          <td>3</td>
          <td>mjohnson</td>
          <td>Mary Johnson</td>
          <td>mary.johnson@egp.gov</td>
          <td>USER</td>
          <td><span style="color: green">Active</span></td>
          <td>12/02/2024 04:45 PM</td>
          <td>
            <a
              href="users.jsp?action=edit&id=3"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >Edit</a
            >
          </td>
        </tr>
        <tr>
          <td>4</td>
          <td>bwilson</td>
          <td>Bob Wilson</td>
          <td>bob.wilson@egp.gov</td>
          <td>USER</td>
          <td><span style="color: green">Active</span></td>
          <td>12/01/2024 11:20 AM</td>
          <td>
            <a
              href="users.jsp?action=edit&id=4"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >Edit</a
            >
          </td>
        </tr>
        <tr>
          <td>5</td>
          <td>slee</td>
          <td>Susan Lee</td>
          <td>susan.lee@egp.gov</td>
          <td>USER</td>
          <td><span style="color: red">Inactive</span></td>
          <td>11/15/2024 02:30 PM</td>
          <td>
            <a
              href="users.jsp?action=edit&id=5"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >Edit</a
            >
          </td>
        </tr>
      </table>
    </div>

    <div class="footer">
      <p>
        &copy; 2008 Enterprise Government Portal System. All rights reserved.
      </p>
    </div>
  </body>
</html>
