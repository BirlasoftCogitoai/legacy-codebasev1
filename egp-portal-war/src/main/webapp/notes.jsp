<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>EGP Portal - Case Notes</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
    <div class="header">
      <h1>Enterprise Government Portal</h1>
      <div class="subtitle">Case Notes Management</div>
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
      <h2>Case Notes - CASE-2024-001</h2>

      <h3>Add New Note</h3>
      <form action="notes" method="post">
        <table class="form-table">
          <tr>
            <td class="label">Note Type:</td>
            <td>
              <select name="noteType">
                <option value="GENERAL">General</option>
                <option value="INTERNAL">Internal</option>
                <option value="CUSTOMER_CONTACT">Customer Contact</option>
              </select>
            </td>
          </tr>
          <tr>
            <td class="label">Note Text:</td>
            <td>
              <textarea name="noteText" rows="4" cols="60" required></textarea>
            </td>
          </tr>
          <tr>
            <td class="label">Private Note:</td>
            <td>
              <input type="checkbox" name="isPrivate" value="true" />
              (Only visible to staff)
            </td>
          </tr>
          <tr>
            <td colspan="2" style="text-align: center; padding-top: 10px">
              <input type="submit" value="Add Note" class="button" />
            </td>
          </tr>
        </table>
      </form>

      <h3>Existing Notes</h3>
      <table class="data-table">
        <tr>
          <th>Date</th>
          <th>Type</th>
          <th>Note</th>
          <th>Created By</th>
          <th>Private</th>
        </tr>
        <!-- Legacy: Sample notes -->
        <tr>
          <td>12/01/2024 10:30 AM</td>
          <td>GENERAL</td>
          <td>
            Initial application received and reviewed. Medical documentation
            required.
          </td>
          <td>John Smith</td>
          <td>No</td>
        </tr>
        <tr>
          <td>12/01/2024 02:15 PM</td>
          <td>INTERNAL</td>
          <td>Medical records requested from Dr. Johnson office.</td>
          <td>John Smith</td>
          <td><span style="color: red">Yes</span></td>
        </tr>
        <tr>
          <td>12/02/2024 09:45 AM</td>
          <td>CUSTOMER_CONTACT</td>
          <td>
            Customer called to check status. Informed about pending medical
            records.
          </td>
          <td>Mary Johnson</td>
          <td>No</td>
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
