<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - Login</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">Legacy System - Version 1.0</div>
    </div>

    <div class="login-form">
      <h2>System Login</h2>

      <!-- Legacy: Display error messages -->
      <c:if test="${not empty param.error}">
        <div class="message message-error">
          Invalid username or password. Please try again.
        </div>
      </c:if>

      <c:if test="${not empty param.logout}">
        <div class="message message-info">
          You have been successfully logged out.
        </div>
      </c:if>

      <form action="login" method="post">
        <table class="form-table">
          <tr>
            <td class="label">Username:</td>
            <td>
              <input
                type="text"
                name="username"
                id="username"
                value="${param.username}"
                required
                maxlength="50"
              />
            </td>
          </tr>
          <tr>
            <td class="label">Password:</td>
            <td>
              <input
                type="password"
                name="password"
                id="password"
                required
                maxlength="50"
              />
            </td>
          </tr>
          <tr>
            <td colspan="2" style="text-align: center; padding-top: 15px">
              <input type="submit" value="Login" class="button" />
              <input
                type="reset"
                value="Clear"
                class="button button-secondary"
              />
            </td>
          </tr>
        </table>
      </form>

      <div style="margin-top: 20px; font-size: 10px; color: #666">
        <strong>Test Accounts:</strong><br />
        Admin: admin / password<br />
        User: jsmith / password<br />
        Supervisor: mjohnson / password
      </div>
    </div>

    <div class="footer">
      <p>
        &copy; 2008 Enterprise Government Portal System. All rights reserved.
      </p>
      <p>For technical support, contact IT Help Desk at ext. 5555</p>
    </div>

    <script type="text/javascript">
      // Legacy: Simple JavaScript for focus
      document.getElementById("username").focus();

      // Legacy: Form validation
      function validateForm() {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;

        if (username == "" || password == "") {
          alert("Please enter both username and password.");
          return false;
        }

        return true;
      }

      document.forms[0].onsubmit = validateForm;
    </script>
  </body>
</html>
