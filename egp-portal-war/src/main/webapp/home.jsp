<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - Home</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">
        Welcome, ${sessionScope.currentUser.firstName}
        ${sessionScope.currentUser.lastName}
      </div>
    </div>

    <div class="nav-bar">
      <a href="home.jsp" class="active">Home</a>
      <a href="profile">Profile</a>
      <a href="cases">Cases</a>
      <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
        <a href="admin/users.jsp">Admin</a>
      </c:if>
      <a href="login?action=logout" style="float: right">Logout</a>
    </div>

    <div class="content">
      <h2>Dashboard</h2>

      <!-- Legacy: Welcome message -->
      <div class="message message-info">
        Welcome to the Enterprise Government Portal System. Last login:
        <fmt:formatDate
          value="${sessionScope.currentUser.lastLogin}"
          pattern="MM/dd/yyyy HH:mm:ss"
        />
      </div>

      <!-- Legacy: Quick stats table -->
      <h3>System Overview</h3>
      <table class="data-table">
        <tr>
          <th>Metric</th>
          <th>Count</th>
          <th>Status</th>
        </tr>
        <tr>
          <td>Open Cases</td>
          <td>${dashboardStats.openCases}</td>
          <td><span class="status-open">Active</span></td>
        </tr>
        <tr>
          <td>Cases In Progress</td>
          <td>${dashboardStats.inProgressCases}</td>
          <td><span class="status-in-progress">Processing</span></td>
        </tr>
        <tr>
          <td>Closed Cases (This Month)</td>
          <td>${dashboardStats.closedCasesThisMonth}</td>
          <td><span class="status-closed">Complete</span></td>
        </tr>
        <tr>
          <td>Active Customers</td>
          <td>${dashboardStats.activeCustomers}</td>
          <td><span class="status-open">Active</span></td>
        </tr>
      </table>

      <!-- Legacy: Recent activity -->
      <h3>Recent Activity</h3>
      <c:choose>
        <c:when test="${not empty recentCases}">
          <table class="data-table">
            <tr>
              <th>Case Number</th>
              <th>Customer</th>
              <th>Type</th>
              <th>Priority</th>
              <th>Status</th>
              <th>Created</th>
            </tr>
            <c:forEach var="case" items="${recentCases}" varStatus="status">
              <tr>
                <td><a href="case/${case.caseId}">${case.caseNumber}</a></td>
                <td>${case.customer.firstName} ${case.customer.lastName}</td>
                <td>${case.caseType}</td>
                <td>
                  <span class="priority-${case.priority.toLowerCase()}">
                    ${case.priority}
                  </span>
                </td>
                <td>
                  <span
                    class="status-${case.status.toLowerCase().replace('_', '-')}"
                  >
                    ${case.formattedStatus}
                  </span>
                </td>
                <td>
                  <fmt:formatDate
                    value="${case.createdDate}"
                    pattern="MM/dd/yyyy"
                  />
                </td>
              </tr>
            </c:forEach>
          </table>
        </c:when>
        <c:otherwise>
          <div class="message message-info">
            No recent case activity to display.
          </div>
        </c:otherwise>
      </c:choose>

      <!-- Legacy: Quick actions -->
      <h3>Quick Actions</h3>
      <div style="margin: 15px 0">
        <a href="cases?action=new" class="button">Create New Case</a>
        <a href="cases?status=OPEN" class="button">View Open Cases</a>
        <a href="profile" class="button button-secondary">Update Profile</a>

        <c:if
          test="${sessionScope.currentUser.role == 'ADMIN' || sessionScope.currentUser.role == 'SUPERVISOR'}"
        >
          <a href="admin/audit.jsp" class="button button-secondary"
            >View Audit Log</a
          >
        </c:if>
      </div>

      <!-- Legacy: System announcements -->
      <h3>System Announcements</h3>
      <div class="message message-warning">
        <strong>Scheduled Maintenance:</strong> The system will be unavailable
        for maintenance on Sunday, December 15th from 2:00 AM to 6:00 AM EST.
      </div>

      <div class="message message-info">
        <strong>New Feature:</strong> Case notes can now be marked as private.
        See the user manual for details.
      </div>
    </div>

    <div class="footer">
      <p>
        &copy; 2008 Enterprise Government Portal System. All rights reserved.
      </p>
      <p>
        Session ID: ${pageContext.session.id} | Server:
        ${pageContext.servletContext.serverInfo}
      </p>
    </div>

    <!-- Legacy: Auto-refresh for dashboard -->
    <script type="text/javascript">
      // Legacy: Auto-refresh every 5 minutes
      setTimeout(function () {
        window.location.reload();
      }, 300000);

      // Legacy: Display current time
      function updateClock() {
        var now = new Date();
        var timeStr = now.toLocaleTimeString();
        document.title = "EGP Portal - Home (" + timeStr + ")";
      }

      setInterval(updateClock, 1000);
      updateClock();
    </script>
  </body>
</html>
