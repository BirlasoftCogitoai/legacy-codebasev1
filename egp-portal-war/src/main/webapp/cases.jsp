<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - Cases</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">Case Management System</div>
    </div>

    <div class="nav-bar">
      <a href="home.jsp">Home</a>
      <a href="profile">Profile</a>
      <a href="cases" class="active">Cases</a>
      <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
        <a href="admin/users.jsp">Admin</a>
      </c:if>
      <a href="login?action=logout" style="float: right">Logout</a>
    </div>

    <div class="content">
      <h2>Case Management</h2>

      <div style="margin: 15px 0">
        <a href="cases?action=new" class="button">Create New Case</a>
        <a href="cases?status=OPEN" class="button button-secondary"
          >Open Cases</a
        >
        <a href="cases?status=IN_PROGRESS" class="button button-secondary"
          >In Progress</a
        >
        <a href="cases?status=CLOSED" class="button button-secondary"
          >Closed Cases</a
        >
      </div>

      <h3>Recent Cases</h3>
      <table class="data-table">
        <tr>
          <th>Case Number</th>
          <th>Customer</th>
          <th>Type</th>
          <th>Priority</th>
          <th>Status</th>
          <th>Created</th>
          <th>Actions</th>
        </tr>
        <!-- Legacy: Sample data rows -->
        <tr>
          <td><a href="case/1">CASE-2024-001</a></td>
          <td>Alice Anderson</td>
          <td>BENEFITS</td>
          <td><span class="priority-high">HIGH</span></td>
          <td><span class="status-open">OPEN</span></td>
          <td>12/01/2024</td>
          <td>
            <a
              href="case/1"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >View</a
            >
          </td>
        </tr>
        <tr>
          <td><a href="case/2">CASE-2024-002</a></td>
          <td>Robert Brown</td>
          <td>COMPLAINT</td>
          <td><span class="priority-medium">MEDIUM</span></td>
          <td><span class="status-in-progress">IN_PROGRESS</span></td>
          <td>12/02/2024</td>
          <td>
            <a
              href="case/2"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >View</a
            >
          </td>
        </tr>
        <tr>
          <td><a href="case/3">CASE-2024-003</a></td>
          <td>Carol Davis</td>
          <td>INQUIRY</td>
          <td><span class="priority-low">LOW</span></td>
          <td><span class="status-closed">CLOSED</span></td>
          <td>11/28/2024</td>
          <td>
            <a
              href="case/3"
              class="button"
              style="font-size: 10px; padding: 2px 6px"
              >View</a
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
