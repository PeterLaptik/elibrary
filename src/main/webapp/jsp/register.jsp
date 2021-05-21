<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
      <title>Registration page</title>
	  <link rel="stylesheet" type="text/css" href="css/register.css" />
   </head>
   <body>
		<form name="loginform" action="register" method="POST">
			<div class="login_form">
				<script>
					let msg_row = 4;
					let cleanMsg = function(){
					if(table.rows[msg_row].cells[0].innerHTML != '')
						table.rows[msg_row].cells[0].innerHTML = '';
					}
				</script>
				<p> User registration:  </p>
				<table id="table">
				<tr>
					<td>User name </td>
					<td><input label="name" name="name" onclick="cleanMsg()" type="text" size="25" maxlength="30" 
					value="<%=request.getParameter("name")!=null ? request.getParameter("name") : "" %>" /> </td>
				</tr>
				<tr>
					<td>Login </td>
					<td><input name="login" onclick="cleanMsg()" type="text" size="25" maxlength="30" 
					value="<%=request.getParameter("login")!=null ? request.getParameter("login") : "" %>" /></td>
				</tr>
				<tr>
					<td>Password </td>
					<td><input name="password" onclick="cleanMsg()" type="password" size="25" maxlength="30" 
					value="<%=request.getParameter("password")!=null ? request.getParameter("password") : "" %>" /></td>
				</tr>
				<tr>
					<td>Repeat password </td>
					<td><input name="repeat_password" onclick="cleanMsg()" type="password" size="25" maxlength="30" 
					value="<%=request.getParameter("repeat_password")!=null ? request.getParameter("repeat_password") : "" %>" /></td>
				</tr>
				<tr>
					<td id="message" colspan="2"> <%= request.getAttribute("MSG_SHOW")!=null ? (String)request.getAttribute("MSG_SHOW") : "" %> </td>
				</tr>
				</table>
				<p> 
					<button name="action" value="cancel"> Cancel </button> 
					<button id="register" name="action" value="register"> OK </button> 
				</p>
			</div>
		</form>
   </body>
</html>