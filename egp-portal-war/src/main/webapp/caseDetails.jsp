<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - Case Details</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">Case Details</div>
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
      <h2>Case Information</h2>

      <form action="cases" method="post">
        <table class="form-table">
          <tr>
            <td class="label">Case Number:</td>
            <td>
              <input
                type="text"
                name="caseNumber"
                readonly
                value="CASE-${param.id != null ? param.id : 'NEW'}"
                style="background-color: #f0f0f0"
              />
            </td>
          </tr>
          <tr>
            <td class="label">Customer Number:</td>
            <td>
              <input
                type="text"
                name="customerNumber"
                required
                maxlength="20"
              />
            </td>
          </tr>
          <tr>
            <td class="label">Case Type:</td>
            <td>
              <select name="caseType" required>
                <option value="">Select Type</option>
                <option value="BENEFITS">Benefits</option>
                <option value="COMPLAINT">Complaint</option>
                <option value="INQUIRY">Inquiry</option>
                <option value="APPEAL">Appeal</option>
              </select>
            </td>
          </tr>
          <tr>
            <td class="label">Priority:</td>
            <td>
              <select name="priority">
                <option value="LOW">Low</option>
                <option value="MEDIUM" selected>Medium</option>
                <option value="HIGH">High</option>
                <option value="URGENT">Urgent</option>
              </select>
            </td>
          </tr>
          <tr>
            <td class="label">Subject:</td>
            <td>
              <input
                type="text"
                name="subject"
                required
                maxlength="200"
                style="width: 400px"
              />
            </td>
          </tr>
          <tr>
            <td class="label">Description:</td>
            <td>
              <textarea
                name="description"
                rows="5"
                cols="50"
                maxlength="2000"
              ></textarea>
            </td>
          </tr>
          <tr>
            <td colspan="2" style="text-align: center; padding-top: 15px">
              <input type="submit" value="Save Case" class="button" />
              <input
                type="button"
                value="Cancel"
                class="button button-secondary"
                onclick="window.location.href='cases'"
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
