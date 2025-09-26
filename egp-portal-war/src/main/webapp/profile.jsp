<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - Profile</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">User Profile Management</div>
    </div>

    <div class="nav-bar">
      <a href="home.jsp">Home</a>
      <a href="profile" class="active">Profile</a>
      <a href="cases">Cases</a>
      <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
        <a href="admin/users.jsp">Admin</a>
      </c:if>
      <a href="login?action=logout" style="float: right">Logout</a>
    </div>

    <div class="content">
      <h2>User Profile</h2>

      <form action="profile" method="post">
        <table class="form-table">
          <tr>
            <td class="label">User ID:</td>
            <td>${sessionScope.currentUser.userId}</td>
          </tr>
          <tr>
            <td class="label">Username:</td>
            <td>${sessionScope.currentUser.username}</td>
          </tr>
          <tr>
            <td class="label">First Name:</td>
            <td>
              <input
                type="text"
                name="firstName"
                value="${sessionScope.currentUser.firstName}"
                maxlength="50"
              />
            </td>
          </tr>
          <tr>
            <td class="label">Last Name:</td>
            <td>
              <input
                type="text"
                name="lastName"
                value="${sessionScope.currentUser.lastName}"
                maxlength="50"
              />
            </td>
          </tr>
          <tr>
            <td class="label">Email:</td>
            <td>
              <input
                type="email"
                name="email"
                value="${sessionScope.currentUser.email}"
                maxlength="100"
              />
            </td>
          </tr>
          <tr>
            <td class="label">Role:</td>
            <td>${sessionScope.currentUser.role}</td>
          </tr>
          <tr>
            <td colspan="2" style="text-align: center; padding-top: 15px">
              <input type="submit" value="Update Profile" class="button" />
              <input
                type="reset"
                value="Reset"
                class="button button-secondary"
              />
            </td>
          </tr>
        </table>
      </form>
    </div>

    <div class="footer">
      <p>
        &copy; 2008 Enterprise Government Portal System. All rights reserved.
      </p>
    </div>
  </body>
</html>
